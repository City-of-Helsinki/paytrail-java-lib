package org.helsinki.vismapay.request;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.helsinki.vismapay.VismaPayClient;
import org.helsinki.vismapay.request.payload.trait.impl.VismaPayPayload;
import org.helsinki.vismapay.response.VismaPayResponse;

import java.nio.charset.StandardCharsets;

@Slf4j
public abstract class VismaPayPostRequest<T extends VismaPayResponse, B extends VismaPayPayload<?>> extends VismaPayRequest<T> {

	protected abstract B getPayload(VismaPayClient client);

	@Override
	public Request formRequest(VismaPayClient client) {
		Request.Builder req = new Request.Builder().url(this.formUrl(client));
		applyHeaders(client, req);
		req.post(getRequestBody(client));

		return req.build();
	}

	protected RequestBody getRequestBody(VismaPayClient client) {
		if (getPayload(client) == null) {
			return RequestBody.create(null, "");
		}
		client.setVismaPayPayloadDefaults(getPayload(client));

		Gson gson = getGson(true);
		String payload = gson.toJson(getPayload(client));

		log.debug("Payload : {}", payload);

		return RequestBody.create(MediaType.parse("application/json; charset=" + StandardCharsets.UTF_8), payload);
	}
}
