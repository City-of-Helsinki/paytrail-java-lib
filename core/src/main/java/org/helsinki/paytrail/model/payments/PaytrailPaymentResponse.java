package org.helsinki.paytrail.model.payments;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

@Data
@Accessors(chain = true)
public class PaytrailPaymentResponse implements Serializable {
    private String transactionId;
    private String href;
    private String terms;
    private List<PaymentMethodGroupData> groups;
    private List<Provider> providers;
    private String reference;
    private JsonNode customProviders;

}
