package org.helsinki.paytrail.request.auth.constants;

import com.fasterxml.jackson.annotation.JsonValue;

import java.text.Collator;
import java.util.*;
import java.util.stream.Collectors;

public enum PaytrailAuthHeaders {
    CHECKOUT_ACCOUNT("checkout-account"),
    CHECKOUT_ALGORITHM("checkout-algorithm"),
    CHECKOUT_METHOD("checkout-method"),
    CHECKOUT_NONCE("checkout-nonce"),
    CHECKOUT_TIMESTAMP("checkout-timestamp"),
    CHECKOUT_TRANSACTION_ID("checkout-transaction-id"),
    PLATFORM_NAME("platform-name"),
    SIGNATURE("signature");

    @JsonValue
    private final String type;

    PaytrailAuthHeaders(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return type;
    }

    public static List<String> getSortedKeys() {
        PaytrailAuthHeaders[] enums = PaytrailAuthHeaders.class.getEnumConstants();
        return Arrays.stream(enums).map(PaytrailAuthHeaders::toString).sorted(Collator.getInstance()).collect(Collectors.toList());
    }

}