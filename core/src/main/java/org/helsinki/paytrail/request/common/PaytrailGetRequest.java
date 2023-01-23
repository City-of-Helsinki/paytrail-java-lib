package org.helsinki.paytrail.request.common;

import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.Request;
import org.helsinki.paytrail.PaytrailClient;
import org.helsinki.paytrail.constants.CheckoutMethod;
import org.helsinki.paytrail.request.contracts.paytrail.PaytrailPayload;
import org.helsinki.paytrail.response.PaytrailResponse;

import java.util.Map;

@Slf4j
public abstract class PaytrailGetRequest<T extends PaytrailResponse> extends PaytrailRequest<T> {

	protected abstract PaytrailPayload<?> getPayload(PaytrailClient client);

	protected Map<String,String> getParameters;

	@Override
	public Request formRequest(PaytrailClient client) {
		Request.Builder req = new Request.Builder().url(this.formUrl(client));
		setDefaultQueryParameters();
		req = applyHeaders(client, req);
		req = applyDefaultAuthHeaders(client, req, String.valueOf(CheckoutMethod.GET));

		req = setQueryParameters(req);

		return req.build();
	}

	protected void setDefaultQueryParameters() {
		// Nothing by default
	}

	private Request.Builder setQueryParameters(Request.Builder req) {
		HttpUrl.Builder httpBuilder = HttpUrl.parse(String.valueOf(req.build().url())).newBuilder();
		if (getParameters != null) {
			for (Map.Entry<String, String> param : getParameters.entrySet()) {
				httpBuilder.addQueryParameter(param.getKey(), param.getValue());
			}
		}
		req.url(httpBuilder.build());
		return req;
	}

	public Map<String, String> getGetParameters() {
		return getParameters;
	}

	public void setGetParameters(Map<String, String> getParameters) {
		this.getParameters = getParameters;
	}
}
