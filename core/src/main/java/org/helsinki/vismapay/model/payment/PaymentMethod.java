package org.helsinki.vismapay.model.payment;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.experimental.Accessors;
import org.helsinki.vismapay.util.BooleanUtils;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class PaymentMethod implements Serializable {

	@SuppressWarnings("SpellCheckingInspection")
	public final static String TYPE_EPAYMENT = "e-payment";
	public final static String TYPE_EMBEDDED = "embedded";
	public final static String TYPE_TERMINAL = "terminal";
	public final static String TYPE_CARD = "card";

	public PaymentMethod() {
		setType(TYPE_EPAYMENT);
	}

	private String type;

	@SerializedName(value = "register_card_token")
	private Byte registerCardToken;

	@SerializedName(value = "return_url")
	private String returnUrl;

	@SerializedName(value = "notify_url")
	private String notifyUrl;

	private String lang = "fi";

	@SerializedName(value = "token_valid_until")
	private String tokenValidUntil;

	@SerializedName(value = "override_auto_settlement")
	private Integer overrideAutoSettlement;

	@SerializedName(value = "selected_terminal")
	private String[] selectedTerminal;

	@SerializedName(value = "skip_receipt")
	private Byte skipReceipt;

	private String[] selected;

	public Boolean getRegisterCardToken() {
		return BooleanUtils.toBoolean(registerCardToken);
	}

	public PaymentMethod setRegisterCardToken(Boolean registerCardToken) {
		this.registerCardToken = BooleanUtils.toByte(registerCardToken);
		return this;
	}

	public Boolean getSkipReceipt() {
		return BooleanUtils.toBoolean(skipReceipt);
	}

	public PaymentMethod setSkipReceipt(Boolean skipReceipt) {
		this.skipReceipt = BooleanUtils.toByte(skipReceipt);
		return this;
	}
}
