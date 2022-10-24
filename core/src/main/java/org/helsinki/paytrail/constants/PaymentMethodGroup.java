package org.helsinki.paytrail.constants;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentMethodGroup {
    MOBILE("mobile"),
    BANK("bank"),
    CREDITCARD("creditcard"),
    CREDIT("credit");

    @JsonValue
    private final String id;

    PaymentMethodGroup(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return id;
    }
}
