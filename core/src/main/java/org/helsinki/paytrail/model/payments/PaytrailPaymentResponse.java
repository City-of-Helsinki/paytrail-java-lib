package org.helsinki.paytrail.model.payments;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.helsinki.paytrail.response.PaytrailResponse;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PaytrailPaymentResponse extends PaytrailResponse implements Serializable {
    private String transactionId;
    private String href;
    private String terms;
    private List<PaymentMethodGroupData> groups;
    private List<Provider> providers;
    private String reference;
    private JsonNode customProviders;
}
