package org.helsinki.paytrail.constants;

public enum CheckoutAlgorithm {
    SHA256("sha256"),
    SHA512("sha512");

    private final String algorithm;

    private CheckoutAlgorithm(String algorithm) { this.algorithm = algorithm; }

    @Override
    public String toString() { return this.algorithm; }
}
