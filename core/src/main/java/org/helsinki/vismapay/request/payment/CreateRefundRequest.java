package org.helsinki.vismapay.request.payment;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.helsinki.vismapay.VismaPayClient;
import org.helsinki.vismapay.model.payment.Product;
import org.helsinki.vismapay.request.VismaPayPostRequest;
import org.helsinki.vismapay.request.payload.trait.impl.BaseOrderIdentifiablePayload;
import org.helsinki.vismapay.response.payment.CreateRefundResponse;
import org.helsinki.vismapay.util.AuthCodeCalculator;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class CreateRefundRequest
		extends VismaPayPostRequest<CreateRefundResponse, CreateRefundRequest.CreateRefundPayload> {

	@NonNull
	private final CreateRefundPayload payload;

	@Override
	protected CreateRefundPayload getPayload(VismaPayClient client) {
		String authCodeStr = client.getApiKey() + "|" + payload.getOrderNumber();
		payload.setAuthCode(AuthCodeCalculator.calcAuthCode(client.getPrivateKey(), authCodeStr));
		return payload;
	}

	@Override
	public String path() {
		return "create_refund";
	}

	@Override
	public Class<CreateRefundResponse> getResponseType() {
		return CreateRefundResponse.class;
	}

	@EqualsAndHashCode(callSuper = true)
	@Data
	@Accessors(chain = true)
	public static class CreateRefundPayload extends BaseOrderIdentifiablePayload<CreateRefundPayload> {

		private String email;
		private BigDecimal amount;
		private Set<Product> products;

		@SerializedName("notify_url")
		private String notifyUrl;

		@SuppressWarnings("UnusedReturnValue")
		public CreateRefundPayload addProduct(Product product) {
			if (products == null) {
				products = new HashSet<>();
			}
			products.add(product);

			return this;
		}
	}
}
