package org.helsinki.paytrail.request.tokenization;

import lombok.NonNull;
import org.helsinki.paytrail.PaytrailClient;
import org.helsinki.paytrail.request.common.PaytrailPostRequest;
import org.helsinki.paytrail.request.contracts.paytrail.PaytrailPayload;
import org.helsinki.paytrail.response.tokenization.PaytrailGetTokenResponse;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class PaytrailGetTokenRequest extends PaytrailPostRequest<PaytrailGetTokenResponse> {

    private static final String TOKENIZATION_ID_HEADER = "checkout-tokenization-id";

    private final String checkoutTokenizationId;
    private final Map<String, String> requestSpecificHeaders;

    public PaytrailGetTokenRequest(@NonNull String checkoutTokenizationId) {
        this.checkoutTokenizationId = checkoutTokenizationId;
        this.requestSpecificHeaders = Collections.singletonMap(TOKENIZATION_ID_HEADER, checkoutTokenizationId);
    }

    @Override
    public String path() {
        return "tokenization/" + checkoutTokenizationId;
    }

    @Override
    public Class<PaytrailGetTokenResponse> getResponseType() {
        return PaytrailGetTokenResponse.class;
    }

    @Override
    protected PaytrailPayload<?> getPayload(PaytrailClient client) {
        return null;
    }

    @Override
    protected Map<String, String> getRequestSpecificHeaders() {
        return requestSpecificHeaders;
    }
}
