package org.helsinki.paytrail;

import org.helsinki.paytrail.request.auth.constants.PaytrailAuthHeaders;
import org.helsinki.paytrail.service.PaytrailSignatureService;
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
    private final String merchantId;
    private final String secretKey;
    private final String version;
    private final String baseUrl;
    private transient OkHttpClient httpClient;

    public PaytrailClient(String merchantId, String secretKey) {
        this(merchantId, secretKey, "wm3.1");
    }

    public PaytrailClient(String merchantId, String secretKey, String version) {
        this(merchantId, secretKey, version, API_URL, defaultHttpClient(secretKey));
    }

    public PaytrailClient(String merchantId, String secretKey, String version, String baseUrl) {
        this(merchantId, secretKey, version, baseUrl, defaultHttpClient(secretKey));
    }

    public PaytrailClient(String merchantId, String secretKey, String version, String baseUrl, OkHttpClient client) {
        this.merchantId = merchantId;
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

                try (ResponseBody body = response.body()) {
                    responseFuture.complete(new Pair<>(response, body.string()));
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

            return chain.proceed(request);
        });

        return builder.build();
    }


}