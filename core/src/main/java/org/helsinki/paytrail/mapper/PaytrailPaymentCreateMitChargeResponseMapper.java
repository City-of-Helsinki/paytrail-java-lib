package org.helsinki.paytrail.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.helsinki.paytrail.response.PaytrailResponse;
import org.helsinki.paytrail.response.payments.PaytrailPaymentCreateMitChargeResponse;

@Slf4j
public class PaytrailPaymentCreateMitChargeResponseMapper extends AbstractModelMapper<PaytrailResponse, PaytrailPaymentCreateMitChargeResponse> {
    public PaytrailPaymentCreateMitChargeResponseMapper(ObjectMapper mapper) {
        super(mapper, PaytrailResponse::new, PaytrailPaymentCreateMitChargeResponse::new);
    }

    @Override
    public PaytrailPaymentCreateMitChargeResponse to(PaytrailResponse paytrailResponse) {
        PaytrailPaymentCreateMitChargeResponse to = super.to(paytrailResponse);
        try {
            if (paytrailResponse.isValid()) {
                to.setSuccess(mapper.readValue(paytrailResponse.getResultJson(), new TypeReference<>() {
                }));
            } else {
                to.setFailure(mapper.readValue(paytrailResponse.getResultJson(), new TypeReference<>() {
                }));
            }
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
