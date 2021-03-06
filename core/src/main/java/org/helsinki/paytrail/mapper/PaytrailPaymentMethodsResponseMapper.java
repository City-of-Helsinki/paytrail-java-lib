package org.helsinki.paytrail.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.helsinki.paytrail.model.paymentmethods.PaytrailPaymentMethod;
import org.helsinki.paytrail.response.PaytrailResponse;
import org.helsinki.paytrail.response.paymentmethods.PaytrailPaymentMethodsResponse;

import java.lang.reflect.Array;
import java.util.List;

/**
 * > This class is a Spring component that maps a PaymentMethodModel object to a PaymentMethodDTO object and back
 */
public class PaytrailPaymentMethodsResponseMapper extends AbstractModelMapper<PaytrailResponse, PaytrailPaymentMethodsResponse> {

    public PaytrailPaymentMethodsResponseMapper(ObjectMapper mapper) {
        super(mapper, PaytrailResponse::new, PaytrailPaymentMethodsResponse::new);
    }

    @Override
    public PaytrailPaymentMethodsResponse to(PaytrailResponse paytrailResponse) {
        PaytrailPaymentMethodsResponse to = super.to(paytrailResponse);
        try {
            to.setPaymentMethods(mapper.readValue(paytrailResponse.getResultJson(), new TypeReference<>() {
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
