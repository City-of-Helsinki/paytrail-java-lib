package org.helsinki.paytrail.request.contracts.common;

public interface Versionable<T extends Versionable<T>> extends Versioned {
	T setVersion(String version);
}
