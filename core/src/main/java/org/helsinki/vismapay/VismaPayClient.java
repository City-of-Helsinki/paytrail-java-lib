package org.helsinki.vismapay;

import org.helsinki.vismapay.util.Pair;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.helsinki.vismapay.exception.VismaPayResponseException;
import org.helsinki.vismapay.request.payload.trait.impl.VismaPayPayload;
import org.helsinki.vismapay.request.VismaPayRequest;
import org.helsinki.vismapay.response.VismaPayResponse;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Data
@Slf4j
public class VismaPayClient implements Serializable {

	public final static String API_URL = "https://www.vismapay.com/pbwapi";
	public final static long DEFAULT_TIMEOUT_SECONDS = 60;

	private static final long serialVersionUID = -893265874836L;
	private final String apiKey;
	private final String privateKey;
	private final String version;
	private final String baseUrl;
	private transient OkHttpClient httpClient;

	public VismaPayClient(String apiKey, String privateKey) {
		this(apiKey, privateKey, "wm3.1");
	}

	public VismaPayClient(String apiKey, String privateKey, String version) {
		this(apiKey, privateKey, version, API_URL);
	}

	public VismaPayClient(String apiKey, String privateKey, String version, String baseUrl) {
		this(apiKey, privateKey, version, baseUrl, defaultHttpClient());
	}

	public VismaPayClient(String apiKey, String privateKey, String version, String baseUrl, OkHttpClient client) {
		this.apiKey = apiKey;
		this.privateKey = privateKey;
		this.version = version;
		this.baseUrl = baseUrl;
		this.httpClient = client;
	}

	public <T extends VismaPayResponse> CompletableFuture<T> sendRequest(@NonNull VismaPayRequest<T> request) {
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
		}).exceptionally((tr) ->
				VismaPayResponseException.VismaPayFailedResponse.of(tr, request.getResponseType())
		);
	}

	public void setVismaPayPayloadDefaults(VismaPayPayload<?> load) {
		load.setApiKey(getApiKey());
		load.setVersion(getVersion());
	}

	public static OkHttpClient defaultHttpClient() {
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		builder.connectTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
		// TODO: certificate/auth check?

		return builder.build();
	}
}