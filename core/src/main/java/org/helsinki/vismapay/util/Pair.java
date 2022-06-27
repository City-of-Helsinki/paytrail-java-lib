package org.helsinki.vismapay.util;

import java.io.Serializable;
import java.util.Objects;

/**
 * A convenience class to represent name-value pairs.
 */
public class Pair <K, V> implements Serializable {

	private final K key;
	private final V value;

	public Pair(K key, V value) {
		this.key = key;
		this.value = value;
	}

	public K getKey() {
		return key;
	}

	public V getValue() {
		return value;
	}

	@Override
	public String toString() {
		return key + "=" + value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o instanceof Pair<?,?>) {
			return Objects.equals(key, ((Pair<?,?>)o).getKey()) &&
					Objects.equals(value, ((Pair<?,?>)o).getValue());
		}
		return false;
	}

	@Override
	public int hashCode() {
		if (key == null) return (value == null) ? 0 : value.hashCode() + 1;
		else if (value == null) return key.hashCode() + 2;
		else return key.hashCode() * 17 + value.hashCode();
	}
}
