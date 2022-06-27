package org.helsinki.vismapay.example.service;

import org.helsinki.vismapay.VismaPayClient;
import org.helsinki.vismapay.example.factory.VismaPayClientFactory;
import org.helsinki.vismapay.example.util.Strings;
import org.helsinki.vismapay.model.payment.Customer;
import org.helsinki.vismapay.model.payment.PaymentMethod;
import org.helsinki.vismapay.model.payment.Product;
import org.helsinki.vismapay.model.payment.ProductType;
import org.helsinki.vismapay.request.payment.ChargeRequest;
import org.helsinki.vismapay.response.payment.ChargeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class GetVismaPayTokenCommand {

	@Autowired
	private VismaPayClientFactory vismaPayClientFactory;

	public String getToken(String returnUrl, String method, String selected) {
		// NB: version should be wm3.1 for channel payments!
		VismaPayClient client = vismaPayClientFactory.create("w3.1");

		CompletableFuture<ChargeResponse> responseCF =
				client.sendRequest(new ChargeRequest(getSamplePayload(returnUrl, method, selected)));

		try {
			ChargeResponse response = responseCF.get();
			if (response.getResult() == 0) {
				return response.getToken();
			} else {
				throw new RuntimeException(buildErrorMsg(response));
			}
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException("Got the following exception: " + e.getMessage());
		}
	}

	private ChargeRequest.PaymentTokenPayload getSamplePayload(String returnUrl, String method, String selected) {
		if (Strings.isNullOrEmpty(returnUrl)) {
			throw new IllegalArgumentException("Return url cannot be empty.");
		}

		PaymentMethod paymentMethod = new PaymentMethod();
		paymentMethod.setType(method)
				.setReturnUrl(returnUrl)
				.setNotifyUrl(returnUrl);

		if (!Strings.isNullOrEmpty(selected)) {
			paymentMethod.setSelected(new String[] { selected });
		}

		Customer customer = new Customer();
		customer.setFirstname("Example")
				.setLastname("Testaaja")
				.setAddressStreet("Testaddress 1")
				.setAddressCity("Testlandia")
				.setAddressZip("12345")
				.setEmail("test.person@example.com");

		Product product = new Product();
		product.setId("product123")
				.setType(ProductType.TYPE_PRODUCT)
				.setTitle("Product 1")
				.setCount(1)
				.setPretaxPrice(BigDecimal.valueOf(100))
				.setTax(24)
				.setPrice(BigDecimal.valueOf(124));

		ChargeRequest.PaymentTokenPayload payload = new ChargeRequest.PaymentTokenPayload();
		payload.setAmount(BigInteger.valueOf(124)) // needs to match the sum of products
				.setOrderNumber("example-payment-" + Instant.now().toEpochMilli())
				.setCurrency("EUR")
				.setPaymentMethod(paymentMethod)
				.addProduct(product)
				.setCustomer(customer)
				.setEmail("test.person@example.com");

		return payload;
	}

	private String buildErrorMsg(ChargeResponse response) {
		String errorMsg = "Unable to create a payment. ";
		if (response == null) {
			throw new IllegalArgumentException("Response can't be null.");
		}

		if (response.getResult() != 0) {
			if (response.getErrors().length > 0) {
				return errorMsg + "Validation errors: " + String.join(", ", response.getErrors());
			}
			return errorMsg + "Please check that api key and private key are correct.";
		}

		throw new IllegalArgumentException("Response was successful. Should never get here if called properly.");
	}
}
