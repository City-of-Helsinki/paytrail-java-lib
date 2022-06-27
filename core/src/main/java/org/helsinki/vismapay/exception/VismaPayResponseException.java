package org.helsinki.vismapay.exception;

import com.google.gson.Gson;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.helsinki.vismapay.response.VismaPayResponse;

import java.io.IOException;

@Getter
public class VismaPayResponseException extends IOException {
	@NonNull
	private final VismaPayResponse response;

	public VismaPayResponseException(VismaPayResponse response, String message) {
		super(message);
		this.response = response;
	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	public static class VismaPayFailedResponse extends VismaPayResponse {
		private final String message;

		public static VismaPayResponse of(Throwable throwable) {
			if (throwable instanceof VismaPayResponseException)
				return ((VismaPayResponseException)throwable).getResponse();
			return new VismaPayFailedResponse(throwable.toString());
		}

		public static <T> T of(Throwable throwable, Class<T> clazz) {
			// TODO: there has to be a better way to do this
			return new Gson().fromJson("{}", clazz);
		}
	}
}
