package org.helsinki.paytrail.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.helsinki.paytrail.model.paymentmethods.PaytrailPaymentMethod;
import org.helsinki.paytrail.response.PaytrailResponse;
import org.helsinki.paytrail.response.paymentmethods.PaytrailPaymentMethodsResponse;

import java.util.List;


public class PaytrailPaymentMethodsResponseMapper extends AbstractModelMapper<PaytrailResponse, PaytrailPaymentMethodsResponse> {

    public PaytrailPaymentMethodsResponseMapper(ObjectMapper mapper) {
        super(mapper, PaytrailResponse::new, PaytrailPaymentMethodsResponse::new);
    }

    @Override
    public PaytrailPaymentMethodsResponse to(PaytrailResponse paytrailResponse) {
        PaytrailPaymentMethodsResponse to = super.to(paytrailResponse);
        try {
            to.setPaymentMethods(mapper.readValue(paytrailResponse.getResultJson(), mapper.getTypeFactory().constructCollectionType(List.class, PaytrailPaymentMethod.class)));
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
