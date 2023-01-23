package org.helsinki.paytrail.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.helsinki.paytrail.model.tokenization.PaytrailTokenResponse;
import org.helsinki.paytrail.response.PaytrailResponse;
import org.helsinki.paytrail.response.tokenization.PaytrailGetTokenResponse;


public class PaytrailGetTokenResponseMapper extends AbstractModelMapper<PaytrailResponse, PaytrailGetTokenResponse> {

    public PaytrailGetTokenResponseMapper(ObjectMapper mapper) {
        super(mapper, PaytrailResponse::new, PaytrailGetTokenResponse::new);
    }

    @Override
    public PaytrailGetTokenResponse to(PaytrailResponse paytrailResponse) {
        PaytrailGetTokenResponse to = super.to(paytrailResponse);
        try {
            to.setTokenResponse(mapper.readValue(paytrailResponse.getResultJson(), PaytrailTokenResponse.class));
        } catch (JsonProcessingException e) {
            String[] errors = new String[]{
                    e.getMessage(),
                    e.getOriginalMessage(),
                    paytrailResponse.getResultJson()
            };
            to.setErrors(errors);
        }
        return to;
    }
}
