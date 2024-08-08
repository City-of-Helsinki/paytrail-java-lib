package org.helsinki.paytrail;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.helsinki.paytrail.exception.PaytrailResponseException;
import org.helsinki.paytrail.mapper.ConfiguredObjectMapper;
import org.helsinki.paytrail.request.auth.constants.PaytrailAuthHeaders;
import org.helsinki.paytrail.request.common.PaytrailRequest;
import org.helsinki.paytrail.response.PaytrailResponse;
import org.helsinki.paytrail.service.PaytrailSignatureService;
import org.helsinki.paytrail.util.Pair;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Data
@Slf4j
public class PaytrailClient implements Serializable {


    public final static String API_URL = "https://services.paytrail.com";
    public final static String PAYMENT_UI_URL = "https://pay.paytrail.com";

    public final static String CHECKOUT_ALGORITHM = "sha256";
    public final static long DEFAULT_TIMEOUT_SECONDS = 60;
    public final static long WRITE_TIMEOUT_SECONDS = 30;
    public final static long READ_TIMEOUT_SECONDS = 30;

    private static final long serialVersionUID = -893265874836L;
    private final String internalMerchantId;
    private final String secretKey;
    private final String version;
    private final String baseUrl;
    private transient OkHttpClient httpClient;

    // Merchant ID for the item. Required for Shop-in-Shop payments, do not use for normal payments.
    private String customerMerchantId;

    public PaytrailClient(String internalMerchantId, String secretKey) {
        this(internalMerchantId, secretKey, "wm3.1", API_URL, defaultHttpClient(secretKey));
    }

    public PaytrailClient(String internalMerchantId, String secretKey, String version) {
        this(internalMerchantId, secretKey, version, API_URL, defaultHttpClient(secretKey));
    }

    public PaytrailClient(String internalMerchantId, String secretKey, String version, String baseUrl) {
        this(internalMerchantId, secretKey, version, baseUrl, defaultHttpClient(secretKey));
    }

    public PaytrailClient(String internalMerchantId, String secretKey, String version, String baseUrl, OkHttpClient client) {
        this.internalMerchantId = internalMerchantId;
        this.secretKey = secretKey;
        this.version = version;
        this.baseUrl = baseUrl;
        this.httpClient = client;
    }

    public <T extends PaytrailResponse> CompletableFuture<T> sendRequest(@NonNull PaytrailRequest<T> request) {
        CompletableFuture<Pair<Response, String>> responseFuture = new CompletableFuture<>();
        log.info("Send request: {}", request.formUrl(this));
        this.httpClient.newCall(request.formRequest(this)).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                log.info("onFailure response for {}", call.request());
                ObjectMapper mapper = ConfiguredObjectMapper.getMapper();
                ;
                try {
                    log.info("defaultHttpClient request : {}", mapper.writeValueAsString(call.request()));
                } catch (JsonProcessingException ex) {
                    log.info("defaultHttpClient request error : ", ex);
                }

                responseFuture.completeExceptionally(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                log.info("Response for {} : {}", call.request().url().toString(), response.code());
                log.info("Call for: {}", call);

                debugResponseAndHeaders(call, response);
                try (ResponseBody body = response.body()) {
                    String bodyString = body.string();
                    debugResponseBody(call, bodyString);
                    log.info("onResponse bodyString : {}", bodyString);
                    responseFuture.complete(new Pair<>(response, bodyString));
                }
            }


        });

        return responseFuture.thenApply(result -> {
            log.info(
                    "responseFuture Response for {} with body : {}",
                    result.getKey().request().url(),
                    result.getValue()
            );
            return request.parseResponse(result);
        }).exceptionally((tr) -> {
            log.info("responseFuture exception timeouts used - callTimeout: {}, connectTimeout: {}, writeTimeout: {}, readTimeout: {}", this.httpClient.callTimeoutMillis(), this.httpClient.connectTimeoutMillis(), this.httpClient.writeTimeoutMillis(), this.httpClient.readTimeoutMillis());
            log.info("responseFuture exception message: {}", tr.getMessage());
            return PaytrailResponseException.PaytrailFailedResponse.of(tr, request.getResponseType());
        });
    }

    private void debugResponseBody(Call call, String bodyString) throws IOException {
        if (Boolean.parseBoolean(System.getenv("DEBUG_CLIENT"))) {
            ObjectMapper mapper = new ObjectMapper();
            String bodyWriteAsString = mapper.writeValueAsString(bodyString);
            bodyWriteAsString = bodyWriteAsString.substring(1);
            bodyWriteAsString = bodyWriteAsString.substring(0, bodyWriteAsString.length() - 1);
            bodyWriteAsString = bodyWriteAsString.replace("\\\"", "\"");
            String nameOfResponseBody = call.request().url().encodedPath().replace("/", "-").substring(1) + "-body.json";
            log.info("{} : {}", nameOfResponseBody, bodyWriteAsString);

        }
    }

    private void debugResponseAndHeaders(Call call, Response response) throws IOException {
        if (Boolean.parseBoolean(System.getenv("DEBUG_CLIENT"))) {
            ObjectMapper mapper = new ObjectMapper();
            String fileName = call.request().url().encodedPath().replace("/", "-").substring(1);

            String responseLogName = fileName + "-response.json";
            log.info("{} : {}", responseLogName, mapper.writeValueAsString(response));

            String requestHeadersName = "debug/" + fileName + "-request-headers.json";
            log.info("{} : {}", requestHeadersName, mapper.writeValueAsString(call.request().headers().toMultimap()));
        }
    }

    public static OkHttpClient defaultHttpClient(String secretKey) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        builder.writeTimeout(WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        builder.readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        builder.addInterceptor(chain -> {
            Request original = chain.request();
            // Keys must be sorted alphabetically, TreeMap that sorts them automatically
            TreeMap<String, String> checkoutSignatureParameters = new TreeMap<>();

            PaytrailSignatureService.filterRequestAuthenticationHeaders(original, checkoutSignatureParameters);

            PaytrailSignatureService.filterRequestQueryParameters(original, checkoutSignatureParameters);

            String calculatedSignature = PaytrailSignatureService.createSignature(secretKey, original, checkoutSignatureParameters);

            Request request = original.newBuilder()
                    .method(original.method(), original.body())
                    .addHeader(String.valueOf(PaytrailAuthHeaders.SIGNATURE), calculatedSignature)
                    .build();

            debugRequest(request);
            ObjectMapper mapper = ConfiguredObjectMapper.getMapper();

            log.info("defaultHttpClient original : {}", mapper.writeValueAsString(original));
            log.info("defaultHttpClient request : {}", mapper.writeValueAsString(request));
            log.info("defaultHttpClient request original body : {}", mapper.writeValueAsString(original.body()));
            log.info("defaultHttpClient request request body : {}", mapper.writeValueAsString(request.body()));
            log.info("defaultHttpClient request original headers : {}", mapper.writeValueAsString(original.headers().toMultimap()));
            log.info("defaultHttpClient request request headers : {}", mapper.writeValueAsString(request.headers().toMultimap()));
            log.info("defaultHttpClient checkoutSignatureParameters : {}", mapper.writeValueAsString(checkoutSignatureParameters));
            log.info("defaultHttpClient calculatedSignature : {}", mapper.writeValueAsString(calculatedSignature));

            return chain.proceed(request);
        });

        return builder.build();
    }

    private static void debugRequest(Request request) throws IOException {
        if (Boolean.parseBoolean(System.getenv("DEBUG_CLIENT"))) {
            ObjectMapper mapper = new ObjectMapper();
            Files.writeString(Path.of("debug/" + request.url().encodedPath().replace("/", "-").substring(1) + "-headers-with-signature.json"), mapper.writeValueAsString(request.headers().toMultimap()));
        }
    }

}