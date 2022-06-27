package org.helsinki.vismapay.model.payment;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.experimental.Accessors;
import org.helsinki.vismapay.util.BooleanUtils;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class Initiator implements Serializable {
	public final static short TYPE_MERCHANT_INITIATED = 1;
	public final static short TYPE_CUSTOMER_INITIATED = 2;

	private Short type;

	@SerializedName(value = "return_url")
	private String returnUrl;

	@SerializedName(value = "notify_url")
	private String notifyUrl;

	private String lang = "fi";

	@SerializedName(value = "browser_info")
	private BrowserInfo browserInfo;

	@Data
	@Accessors(chain = true)
	public static class BrowserInfo implements Serializable {

		@SerializedName(value = "accept_header")
		private String acceptHeader;

		@SerializedName(value = "java_enabled")
		private Byte javaEnabled;

		@SerializedName(value = "language")
		private String language;

		@SerializedName(value = "color_depth")
		private Integer colorDepth;

		@SerializedName(value = "screen_height")
		private Integer screenHeight;

		@SerializedName(value = "screen_width")
		private Integer screenWidth;

		@SerializedName(value = "timezone_offset")
		private Integer timezoneOffset;

		@SerializedName(value = "user_agent")
		private String userAgent;

		public Boolean getJavaEnabled() {
			return BooleanUtils.toBoolean(javaEnabled);
		}

		public BrowserInfo setJavaEnabled(Boolean javaEnabled) {
			this.javaEnabled = BooleanUtils.toByte(javaEnabled);
			return this;
		}
	}
}
