package org.helsinki.paytrail;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.helsinki.paytrail.request.auth.constants.PaytrailAuthHeaders;
import org.helsinki.paytrail.service.PaytrailSignatureService;
import org.helsinki.paytrail.util.DateTimeUtil;
import org.helsinki.paytrail.util.Pair;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.helsinki.paytrail.exception.PaytrailResponseException;
import org.helsinki.paytrail.request.common.PaytrailRequest;
import org.helsinki.paytrail.response.PaytrailResponse;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Data
@Slf4j
public class PaytrailClient implements Serializable {


    public final static String API_URL = "https://services.paytrail.com";

    public final static String CHECKOUT_ALGORITHM = "sha256";
    public final static long DEFAULT_TIMEOUT_SECONDS = 60;

    private static final long serialVersionUID = -893265874836L;
    private final String internalMerchantId;
    private final String secretKey;
    private final String version;
    private final String baseUrl;
    private transient OkHttpClient httpClient;

    // Merchant ID for the item. Required for Shop-in-Shop payments, do not use for normal payments.
    private String customerMerchantId;

    public PaytrailClient(String internalMerchantId, String secretKey) {
        this(internalMerchantId, secretKey, "wm3.1");
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
                responseFuture.completeExceptionally(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                log.info("Response for {} : {}", call.request().url().toString(), response.code());
                debugResponseAndHeaders(call, response);
                try (ResponseBody body = response.body()) {
                    String bodyString = body.string();
                    debugResponseBody(call, bodyString);
                    responseFuture.complete(new Pair<>(response, bodyString));
                }
            }


        });

        return responseFuture.thenApply(result -> {
            log.info(
                    "Response for {} with body : {}",
                    result.getKey().request().url(),
                    result.getValue()
            );
            return request.parseResponse(result);
        }).exceptionally((tr) -> {
            log.info("exception message: {}", tr.getMessage());
            return PaytrailResponseException.PaytrailFailedResponse.of(tr, request.getResponseType());
        });
    }

    private void debugResponseBody(Call call, String bodyString) throws IOException {
        if (Boolean.parseBoolean(System.getenv("DEBUG_CLIENT"))) {
            ObjectMapper mapper = new ObjectMapper();
            String bodyWriteAsString = mapper.writeValueAsString(bodyString);
            bodyWriteAsString = bodyWriteAsString.substring(1);
            bodyWriteAsString = bodyWriteAsString.substring(0, bodyWriteAsString.length() - 1);
            bodyWriteAsString = bodyWriteAsString.replace("\\\"","\"");
            Files.writeString(Path.of("debug/"+ call.request().url().encodedPath().replace("/","-").substring(1) + "-body.json"), bodyWriteAsString);
        }
    }
    private void debugResponseAndHeaders(Call call, Response response) throws IOException {
        if (Boolean.parseBoolean(System.getenv("DEBUG_CLIENT"))) {
            ObjectMapper mapper = new ObjectMapper();
            String fileName = call.request().url().encodedPath().replace("/", "-").substring(1);
            Files.writeString(Path.of("debug/"+ fileName + "-response.json"), mapper.writeValueAsString(response));
            Files.writeString(Path.of("debug/"+ fileName + "-request-headers.json"), mapper.writeValueAsString(call.request().headers().toMultimap()));
        }
    }

    public static OkHttpClient defaultHttpClient(String secretKey) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);

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
            return chain.proceed(request);
        });

        return builder.build();
    }

    private static void debugRequest(Request request) throws IOException {
        if (Boolean.parseBoolean(System.getenv("DEBUG_CLIENT"))) {
            ObjectMapper mapper = new ObjectMapper();
            Files.writeString(Path.of("debug/"+ request.url().encodedPath().replace("/","-").substring(1) + "-headers-with-signature.json"), mapper.writeValueAsString(request.headers().toMultimap()));
        }
    }


}