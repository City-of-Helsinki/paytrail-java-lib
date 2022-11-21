package org.helsinki.paytrail.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.helsinki.paytrail.response.PaytrailResponse;
import org.helsinki.paytrail.response.refunds.PaytrailRefundCreateResponse;

@Slf4j
public class PaytrailRefundCreateResponseMapper extends AbstractModelMapper<PaytrailResponse, PaytrailRefundCreateResponse> {

    public PaytrailRefundCreateResponseMapper(ObjectMapper mapper) {
        super(mapper, PaytrailResponse::new, PaytrailRefundCreateResponse::new);
    }

    @Override
    public PaytrailRefundCreateResponse to(PaytrailResponse paytrailResponse) {
        PaytrailRefundCreateResponse to = super.to(paytrailResponse);
        try {
            to.setRefundResponse(mapper.readValue(paytrailResponse.getResultJson(), new TypeReference<>() {
            }));
        } catch (JsonProcessingException e) {
            String[] errors = new String[]{
                    e.getMessage(),
                    e.getOriginalMessage(),
                    paytrailResponse.getResultJson()
            };
            to.setErrors(errors);
            log.debug(e.getMessage());
        }
        return to;
    }
}
