package org.helsinki.paytrail.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.helsinki.paytrail.model.payments.PaytrailPayment;
import org.helsinki.paytrail.response.PaytrailResponse;
import org.helsinki.paytrail.response.payments.PaytrailPaymentGetResponse;

@Slf4j
public class PaytrailPaymentGetResponseMapper extends AbstractModelMapper<PaytrailResponse, PaytrailPaymentGetResponse> {
    public PaytrailPaymentGetResponseMapper(ObjectMapper mapper) {
        super(mapper, PaytrailResponse::new, PaytrailPaymentGetResponse::new);
    }

    @Override
    public PaytrailPaymentGetResponse to(PaytrailResponse paytrailResponse) {
        PaytrailPaymentGetResponse to = super.to(paytrailResponse);
        try {
            to.setPaytrailPayment(mapper.readValue(paytrailResponse.getResultJson(), PaytrailPayment.class));
        } catch (Exception e) {
            String[] errors = new String[]{
                    e.getMessage(),
                    e instanceof JsonProcessingException ? ((JsonProcessingException) e).getOriginalMessage() : "",
                    paytrailResponse.getResultJson()
            };
            to.setErrors(errors);
            to.setResultJson(paytrailResponse.getResultJson());
            to.setErrorStatus(paytrailResponse.getErrorStatus());
            log.debug(e.getMessage());
        }
        return to;
    }
}
