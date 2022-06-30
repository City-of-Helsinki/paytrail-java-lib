package org.helsinki.paytrail.request.common;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.helsinki.paytrail.PaytrailClient;
import org.helsinki.paytrail.constants.CheckoutMethod;
import org.helsinki.paytrail.request.contracts.paytrail.PaytrailPayload;
import org.helsinki.paytrail.response.PaytrailResponse;

import java.nio.charset.StandardCharsets;

@Slf4j
public abstract class PaytrailPostRequest<T extends PaytrailResponse, B extends PaytrailPayload<?>> extends PaytrailRequest<T> {

	protected abstract B getPayload(PaytrailClient client);

	@Override
	public Request formRequest(PaytrailClient client) {
		Request.Builder req = new Request.Builder().url(this.formUrl(client));
		req = applyHeaders(client, req);
		req = applyDefaultAuthHeaders(client, req, String.valueOf(CheckoutMethod.POST));

		req.post(getRequestBody(client));

		return req.build();
	}

	protected RequestBody getRequestBody(PaytrailClient client) {
		if (getPayload(client) == null) {
			return RequestBody.create(null, "");
		}

		Gson gson = getGson(true);
		String payload = gson.toJson(getPayload(client));

		log.debug("Payload : {}", payload);

		return RequestBody.create(MediaType.parse("application/json; charset=" + StandardCharsets.UTF_8), payload);
	}
}
