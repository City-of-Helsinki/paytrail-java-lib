package org.helsinki.paytrail.request.contracts.paytrail;

import org.helsinki.paytrail.request.contracts.common.Versionable;

public abstract class PaytrailPayload<T extends Versionable<T>> implements Versionable<T> {
	private String version;

	public String getVersion() {
		return version;
	}

	public T setVersion(String version) {
		this.version = version;
		return (T) this;
	}
}
