package org.helsinki.vismapay.util;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReturnDataAuthCodeBuilder {

	@NonNull
	private final String privateKey;

	@NonNull
	private final Short returnCode;

	@NonNull
	private final String orderNumber;

	private Byte settled;
	private String incidentId;

	public ReturnDataAuthCodeBuilder withSettled(Boolean settled) {
		this.settled = settled == null ? null : (settled ? (byte)1 : (byte)0);
		return this;
	}

	public ReturnDataAuthCodeBuilder withSettled(Byte settled) {
		if (settled != null && (settled != 0 && settled != 1)) {
			throw new IllegalArgumentException("Unsupported settled value given as parameter.");
		}
		this.settled = settled;
		return this;
	}

	public ReturnDataAuthCodeBuilder withIncidentId(String incidentId) {
		if (incidentId != null && incidentId.trim().equals("")) {
			incidentId = null;
		}
		this.incidentId = incidentId;
		return this;
	}

	public String build() {
		StringBuilder sb = new StringBuilder();
		sb.append(returnCode);
		sb.append("|");
		sb.append(orderNumber);

		if (settled != null) {
			sb.append("|");
			sb.append(settled);
		}
		if (incidentId != null) {
			sb.append("|");
			sb.append(incidentId);
		}

		return AuthCodeCalculator.calcAuthCode(privateKey, sb.toString());
	}
}
