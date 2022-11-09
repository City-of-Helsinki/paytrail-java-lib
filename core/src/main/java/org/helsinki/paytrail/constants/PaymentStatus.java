package org.helsinki.paytrail.constants;

public enum PaymentStatus {

    NEW("new"),
    OK("ok"),
    FAIL("fail"),
    PENDING("pending"),
    DELAYED("delayed");

    private final String status;

    private PaymentStatus(String status) {
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
