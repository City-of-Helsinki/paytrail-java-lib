package org.helsinki.paytrail.model.payments;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;

@Data
@Accessors(chain = true)
public class PaytrailPayment implements Serializable {
    public String stamp;
    public String reference;
    public int amount;
    public String currency;
    public String language;
    public ArrayList<PaymentItem> items;
    public PaymentCustomer customer;
    public PaymentCallbackUrls redirectUrls;
    public PaymentCallbackUrls callbackUrls;

    public String transactionId;
    public String status;
    public String createdAt;
    public String href;
    public String provider;
    public String filingCode;
    public String paidAt;
}
