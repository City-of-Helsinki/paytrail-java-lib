package org.helsinki.paytrail.response;

import lombok.Data;

@Data
public class PaytrailResponse {
	private Integer result;
 	private String errorStatus; // Can contain error data
	private String message;     // Can contain error data
	private String resultJson;  // Contains paytrail json result
	private boolean isValid = true; // Changed to false if paytrail response throws exception (Status is not 2XX)
}
