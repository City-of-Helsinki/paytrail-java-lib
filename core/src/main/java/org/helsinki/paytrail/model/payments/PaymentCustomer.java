package org.helsinki.paytrail.model.payments;

import lombok.Data;

@Data
public class PaymentCustomer {
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
}
