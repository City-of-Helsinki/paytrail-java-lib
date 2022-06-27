package org.helsinki.vismapay.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class ReturnDataAuthCodeBuilderTest {

	@Test
	public void testBuildExample1() {
		final String privateKey = "xad3b23s2a0d0328b4ea08yd95";
		final Short returnCode = 0;
		final String orderNumber = "12345";
		final Byte settled = 1;
		final String expectedAuthCode = "CCE582990AFB0BFF29A928E8B47E88403505AAF083181E8A7F93B7A42C306E77";

		ReturnDataAuthCodeBuilder builder = new ReturnDataAuthCodeBuilder(privateKey, returnCode, orderNumber);
		String authCode = builder.withSettled(settled).build();

		assertEquals(expectedAuthCode, authCode);
	}

	@Test
	public void testBuildExample1WithBooleanSettled() {
		final String privateKey = "xad3b23s2a0d0328b4ea08yd95";
		final Short returnCode = 0;
		final String orderNumber = "12345";
		final String expectedAuthCode = "CCE582990AFB0BFF29A928E8B47E88403505AAF083181E8A7F93B7A42C306E77";

		ReturnDataAuthCodeBuilder builder = new ReturnDataAuthCodeBuilder(privateKey, returnCode, orderNumber);
		String authCode = builder.withSettled(true).build();

		assertEquals(expectedAuthCode, authCode);
	}

	@Test
	public void testBuildExample2() {
		final String privateKey = "xad3b23s2a0d0328b4ea08yd95";
		final Short returnCode = 1;
		final String orderNumber = "12345";
		final String incidentId = "";
		final String expectedAuthCode = "864F74A47EDEDE35FBD3676C4F41376157AD54D17164633E09A54C4BAD1E573F";

		ReturnDataAuthCodeBuilder builder = new ReturnDataAuthCodeBuilder(privateKey, returnCode, orderNumber);
		String authCode = builder.withIncidentId(incidentId).build();

		assertEquals(expectedAuthCode, authCode);
	}

	@Test(expected = NullPointerException.class)
	public void testInitializingBuilderThrowsExceptionIfNotEnoughData() {
		new ReturnDataAuthCodeBuilder(null, (short) 0, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBuildThrowsExceptionIfInvalidSettled() {
		final String privateKey = "xad3b23s2a0d0328b4ea08yd95";
		final Short returnCode = 0;
		final String orderNumber = "12345";
		final Byte settled = 3;

		ReturnDataAuthCodeBuilder builder = new ReturnDataAuthCodeBuilder(privateKey, returnCode, orderNumber);
		builder.withSettled(settled).build();
	}
}