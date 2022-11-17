package org.helsinki.paytrail.constants;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RefundStatus {

    OK("ok"),
    FAIL("fail"),
    PENDING("pending");

    @JsonValue
    private final String status;

    private RefundStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return status;
    }
}
