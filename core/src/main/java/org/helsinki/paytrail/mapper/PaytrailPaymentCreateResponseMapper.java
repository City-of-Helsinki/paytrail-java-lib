package org.helsinki.paytrail.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.helsinki.paytrail.response.PaytrailResponse;
import org.helsinki.paytrail.response.payments.PaytrailPaymentCreateResponse;


public class PaytrailPaymentCreateResponseMapper extends AbstractModelMapper<PaytrailResponse, PaytrailPaymentCreateResponse> {

    public PaytrailPaymentCreateResponseMapper(ObjectMapper mapper) {
        super(mapper, PaytrailResponse::new, PaytrailPaymentCreateResponse::new);
    }

    @Override
    public PaytrailPaymentCreateResponse to(PaytrailResponse paytrailResponse) {
        PaytrailPaymentCreateResponse to = super.to(paytrailResponse);
        try {
            to.setPaymentResponse(mapper.readValue(paytrailResponse.getResultJson(), new TypeReference<>() {
            }));
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
