package org.helsinki.vismapay.example.service;

import org.helsinki.vismapay.VismaPayClient;
import org.helsinki.vismapay.example.factory.VismaPayClientFactory;
import org.helsinki.vismapay.model.paymentmethods.PaymentMethod;
import org.helsinki.vismapay.request.paymentmethods.PaymentMethodsRequest;
import org.helsinki.vismapay.response.paymentmethods.PaymentMethodsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class GetPaymentMethodsCommand {

	@Autowired
	private VismaPayClientFactory vismaPayClientFactory;

	@Cacheable(value="paymentMethods", unless="#result.length <1")
	public PaymentMethod[] getMerchantPaymentMethods() {
		// Version is different for payment method API
		VismaPayClient client = vismaPayClientFactory.create(String.valueOf(2));

		CompletableFuture<PaymentMethodsResponse> responseCF =
				client.sendRequest(new PaymentMethodsRequest(getSamplePayload()));

		try {
			PaymentMethodsResponse response = responseCF.get();
			if (response.getResult() == 0) {
				return response.getPaymentMethods();
			} else {
				throw new RuntimeException(
					"Unable to get the payment methods for the merchant. " +
					"Please check that api key and private key are correct."
				);
			}
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException("Got the following exception: " + e.getMessage());
		}
	}

	private PaymentMethodsRequest.PaymentMethodsPayload getSamplePayload() {
		PaymentMethodsRequest.PaymentMethodsPayload payload
				= new PaymentMethodsRequest.PaymentMethodsPayload();
		payload.setCurrency("EUR");
		return payload;
	}
}
