package org.helsinki.paytrail.service;

import okhttp3.Request;
import okhttp3.RequestBody;
import org.helsinki.paytrail.util.HMACUtil;
import org.helsinki.paytrail.util.RequestUtil;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.TreeMap;

public class PaytrailSignatureService {

    public static String calculateSignature(TreeMap<String, String> checkoutSignatureParameters, RequestBody body, String secretKey) throws NoSuchAlgorithmException, InvalidKeyException {
        StringBuilder signature = new StringBuilder();

        checkoutSignatureParameters.forEach((key, value) -> {
            signature
                    .append(key)
                    .append(":")
                    .append(value)
                    .append("\n");
        });

        if (body != null && body.contentType() != null) {
            signature.append(RequestUtil.bodyToString(body));
        }

        return HMACUtil.sha256Encrypt(secretKey, signature.toString());
    }

    public static String createSignature(String secretKey, Request original, TreeMap<String, String> checkoutSignatureParameters) {
        String calculatedSignature;
        try {
            calculatedSignature = calculateSignature(checkoutSignatureParameters, original.body(), secretKey);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        return calculatedSignature;
    }

    public static void filterRequestAuthenticationHeaders(Request original, TreeMap<String, String> checkoutSignatureParameters) {
        // Keep only checkout- params, more relevant for response validation. Filter query
        // string parameters the same way - the signature includes only checkout- values.
        for (String headerName : original.headers().names()) {
            for (String headerValue : original.headers(headerName)) {
                if (headerName.contains("checkout-") && headerValue != null) {
                    checkoutSignatureParameters.put(headerName, headerValue);
                }
            }
        }
    }

    public static void filterRequestQueryParameters(Request original, TreeMap<String, String> checkoutSignatureParameters) {
        original.url().queryParameterNames().forEach(parameterName -> {
            if (parameterName.contains("checkout-")) {
                String value = original.url().queryParameter(parameterName);
                if (value != null) {
                    checkoutSignatureParameters.put(parameterName, value);
                }
            }
        });
    }
}
