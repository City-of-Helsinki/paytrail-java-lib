package org.helsinki.vismapay.request.payment;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.helsinki.vismapay.VismaPayClient;
import org.helsinki.vismapay.model.payment.Customer;
import org.helsinki.vismapay.model.payment.PaymentMethod;
import org.helsinki.vismapay.model.payment.Product;
import org.helsinki.vismapay.request.VismaPayPostRequest;
import org.helsinki.vismapay.request.payload.trait.impl.BaseOrderIdentifiablePayload;
import org.helsinki.vismapay.response.payment.ChargeResponse;
import org.helsinki.vismapay.util.AuthCodeCalculator;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class ChargeRequest extends VismaPayPostRequest<ChargeResponse, ChargeRequest.PaymentTokenPayload> {

	@NonNull
	private final PaymentTokenPayload payload;

	@Override
	protected PaymentTokenPayload getPayload(VismaPayClient client) {
		String authCodeStr = client.getApiKey() + "|" + payload.getOrderNumber();
		payload.setAuthCode(AuthCodeCalculator.calcAuthCode(client.getPrivateKey(), authCodeStr));
		return payload;
	}

	@Override
	public String path() {
		return "auth_payment";
	}

	@Override
	public Class<ChargeResponse> getResponseType() {
		return ChargeResponse.class;
	}

	@EqualsAndHashCode(callSuper = true)
	@Data
	@Accessors(chain = true)
	public static class PaymentTokenPayload extends BaseOrderIdentifiablePayload<PaymentTokenPayload> {

		private BigInteger amount;
		private String currency;
		private String email;

		@SerializedName("payment_method")
		private PaymentMethod paymentMethod;

		private Customer customer;
		private Set<Product> products;

		@SuppressWarnings("UnusedReturnValue")
		public PaymentTokenPayload addProduct(Product product) {
			if (products == null) {
				products = new HashSet<>();
			}
			products.add(product);

			return this;
		}
	}
}
