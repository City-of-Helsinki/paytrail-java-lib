package org.helsinki.paytrail.constants;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CardFunding {
    CREDIT("credit"),
    DEBIT("debit"),
    UNKNOWN("unknown");

    @JsonValue
    private final String funding;

    CardFunding(String funding) {
        this.funding = funding;
    }

    public String getFunding() {
        return funding;
    }
}
