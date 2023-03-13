package org.helsinki.paytrail.exception;

import com.google.gson.Gson;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.helsinki.paytrail.response.PaytrailResponse;

import java.io.IOException;

@Getter
@Slf4j
public class PaytrailResponseException extends IOException {
	@NonNull
	private final PaytrailResponse response;

	public PaytrailResponseException(PaytrailResponse response, String message) {
		super(message);
		this.response = response;
	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	public static class PaytrailFailedResponse extends PaytrailResponse {
		private final String message;

		public static PaytrailResponse of(Throwable throwable) {
			if (throwable instanceof PaytrailResponseException)
				return ((PaytrailResponseException)throwable).getResponse();
			return new PaytrailFailedResponse(throwable.toString());
		}

		public static <T> T of(Throwable throwable, Class<T> clazz) {
			Gson gson = new Gson();
			if (throwable.getCause() instanceof PaytrailResponseException && throwable.getCause() != null) {
				PaytrailResponseException exception = (PaytrailResponseException) throwable.getCause();
				PaytrailResponse paytrailResponse = exception.getResponse();

				// Set valid to false if exception is caused from paytrail request
				paytrailResponse.setValid(false);
				String json = gson.toJson(paytrailResponse);

				log.info("PaytrailFailedResponse response : {}", json);
				return new Gson().fromJson(json, clazz);
			}
			// TODO: there has to be a better way to do this
			PaytrailResponse paytrailResponse = new PaytrailResponse();
			// Set valid to false if exception is caused from paytrail request
			paytrailResponse.setValid(false);
			String json = gson.toJson(paytrailResponse);
			log.info("PaytrailFailedResponse2 response : {}", json);
			return new Gson().fromJson(json, clazz);
		}
	}
}
