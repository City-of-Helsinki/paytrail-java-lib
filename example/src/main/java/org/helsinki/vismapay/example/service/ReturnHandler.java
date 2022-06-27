package org.helsinki.vismapay.example.service;

import org.helsinki.vismapay.example.util.Strings;
import org.helsinki.vismapay.util.ReturnDataAuthCodeBuilder;
import org.helsinki.vismapay.example.Constants;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Service
public class ReturnHandler {

	public Result handle(HttpServletRequest request) {
		try {
			return doHandle(request);
		} catch (Exception e) {
			return new Result("Got the following exception: " + e.getMessage());
		}
	}

	private Result doHandle(HttpServletRequest request) {
		Map<String, String[]> parameterMap = request.getParameterMap();

		if (!areParametersRequiredForAuthCodeBuildPresent(parameterMap)) {
			return new Result("Required parameters are missing.");
		}

		String returnCode = parameterMap.get("RETURN_CODE")[0];
		String expectedAuthCode = parameterMap.get("AUTHCODE")[0];

		if(!isSuccessResponse(returnCode)) {
			return new Result("Payment failed (RETURN_CODE: " + returnCode + ")");
		}

		if (!buildAuthCodeFromReturnData(parameterMap).equals(expectedAuthCode)) {
			return new Result("MAC authentication failed.");
		} else {
			return new Result();
		}
	}

	private boolean areParametersRequiredForAuthCodeBuildPresent(Map<String, String[]> parameterMap) {
		return parameterMap.containsKey("RETURN_CODE")
				&& parameterMap.containsKey("ORDER_NUMBER")
				&& parameterMap.containsKey("AUTHCODE");
	}

	private boolean isSuccessResponse(String returnCode) {
		return returnCode.equals("0");
	}

	private String buildAuthCodeFromReturnData(Map<String, String[]> parameterMap) {
		ReturnDataAuthCodeBuilder builder = new ReturnDataAuthCodeBuilder(
				Constants.PRIVATE_KEY,
				Short.valueOf(parameterMap.get("RETURN_CODE")[0]),
				parameterMap.get("ORDER_NUMBER")[0]
		);

		if (parameterMap.containsKey("SETTLED")) {
			builder.withSettled(Byte.valueOf(parameterMap.get("SETTLED")[0]));
		}
		if (parameterMap.containsKey("INCIDENT_ID")) {
			builder.withIncidentId(parameterMap.get("INCIDENT_ID")[0]);
		}

		return builder.build();
	}

	public static class Result {
		private String message;

		public Result() {
		}

		public Result(String message) {
			if (Strings.isNullOrEmpty(message)) {
				throw new IllegalArgumentException("Message can't be empty.");
			}
			this.message = message;
		}

		public boolean isValid() {
			return message == null;
		}

		public String getMessage() {
			return message;
		}
	}
}
