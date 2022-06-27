package org.helsinki.paytrail.request.payment;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.helsinki.paytrail.VismaPayClient;
import org.helsinki.paytrail.model.payment.Customer;
import org.helsinki.paytrail.model.payment.Initiator;
import org.helsinki.paytrail.model.payment.Product;
import org.helsinki.paytrail.request.VismaPayPostRequest;
import org.helsinki.paytrail.request.payload.trait.impl.BaseOrderIdentifiablePayload;
import org.helsinki.paytrail.response.payment.ChargeCardTokenResponse;
import org.helsinki.paytrail.util.AuthCodeCalculator;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class ChargeCardTokenRequest extends
		VismaPayPostRequest<ChargeCardTokenResponse, ChargeCardTokenRequest.CardTokenPayload> {

	@NonNull
	private final CardTokenPayload payload;

	@Override
	protected CardTokenPayload getPayload(VismaPayClient client) {
		String authCodeStr = client.getApiKey() + "|" +
				payload.getOrderNumber() + "|" + payload.getCardToken();
		payload.setAuthCode(AuthCodeCalculator.calcAuthCode(client.getPrivateKey(), authCodeStr));

		return payload;
	}

	@Override
	public String path() {
		return "charge_card_token";
	}

	@Override
	public Class<ChargeCardTokenResponse> getResponseType() {
		return ChargeCardTokenResponse.class;
	}

	@EqualsAndHashCode(callSuper = true)
	@Data
	@Accessors(chain = true)
	public static class CardTokenPayload extends BaseOrderIdentifiablePayload<CardTokenPayload> {

		private BigInteger amount;
		private String currency;
		private String email;

		@SerializedName("card_token")
		private String cardToken;

		private Customer customer;
		private Set<Product> products;
		private Initiator initiator;

		public void addProduct(Product product) {
			if (products == null) {
				products = new HashSet<>();
			}
			products.add(product);
		}
	}
}
