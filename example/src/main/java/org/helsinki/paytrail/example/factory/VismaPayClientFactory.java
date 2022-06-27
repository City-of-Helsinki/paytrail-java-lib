package org.helsinki.paytrail.example.factory;

import org.helsinki.paytrail.VismaPayClient;
import org.helsinki.paytrail.example.Constants;
import org.springframework.stereotype.Component;

@Component
public class VismaPayClientFactory {

	public VismaPayClient create() {
		return new VismaPayClient(
				Constants.API_KEY,
				Constants.PRIVATE_KEY
		);
	}

	public VismaPayClient create(String version) {
		return new VismaPayClient(
				Constants.API_KEY,
				Constants.PRIVATE_KEY,
				version
		);
	}
}
