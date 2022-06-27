package org.helsinki.vismapay.example.factory;

import org.helsinki.vismapay.VismaPayClient;
import org.helsinki.vismapay.example.Constants;
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
