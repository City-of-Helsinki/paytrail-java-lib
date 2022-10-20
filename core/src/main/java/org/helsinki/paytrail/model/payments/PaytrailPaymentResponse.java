package org.helsinki.paytrail.model.payments;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

@Data
@Accessors(chain = true)
public class PaytrailPaymentResponse implements Serializable {
    public String transactionId;
    public String href;
    public String terms;
    public List<PaymentMethodGroupData> groups;
    public List<Provider> providers;
    public String reference;
    public JsonNode customProviders;

}
