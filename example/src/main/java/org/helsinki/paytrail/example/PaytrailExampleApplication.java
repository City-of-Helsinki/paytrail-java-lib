package org.helsinki.paytrail.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PaytrailExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaytrailExampleApplication.class, args);
	}
}
