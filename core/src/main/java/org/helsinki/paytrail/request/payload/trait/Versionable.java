package org.helsinki.paytrail.request.payload.trait;

public interface Versionable<T extends Versionable<T>> extends Versioned {

	T setVersion(String version);
}
