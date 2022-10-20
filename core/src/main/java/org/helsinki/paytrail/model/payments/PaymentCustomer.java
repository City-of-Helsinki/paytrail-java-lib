package org.helsinki.paytrail.model.payments;

import lombok.Data;

@Data
public class PaymentCustomer {
    public String email;
    public String firstName;
    public String lastName;
    public String phone;
}
