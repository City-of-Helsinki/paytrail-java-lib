package org.helsinki.vismapay.util;

import org.junit.Assert;
import org.junit.Test;

public class AuthCodeCalculatorTest {

	@Test
	public void testCalculateAuthCodeForApiKey() {
		String expected = "7AEBC755706F5699162A4359A6947DD1CEA8F65FB7E6C38C6E0ED3C03B808E07";
		String authCodeString = "TESTAPIKEY";

		Assert.assertEquals(expected, AuthCodeCalculator.calcAuthCode("private_key", authCodeString));
	}

	@Test
	public void testCalculateAuthCodeForApiKeyAndOrderNumber() {
		String expected = "23746DEF83C7EC93A1A6D9E03E569032804535230DC3DEDDF575699456C63BFF";
		String authCodeString = "TESTAPIKEY" + "|" + "a";

		Assert.assertEquals(expected, AuthCodeCalculator.calcAuthCode("private_key", authCodeString));
	}

	@Test
	public void testCalculateAuthCodeForApiKeyAndToken() {
		String expected = "B33468AA646E3835C40929AA3361A44F4923E95D90F84CB14CDF9C70507E4384";
		String authCodeString = "TESTAPIKEY" + "|" + "test_token";

		Assert.assertEquals(expected, AuthCodeCalculator.calcAuthCode("private_key", authCodeString));
	}

	@Test
	public void testCalculateAuthCodeForApiKeyAndCardToken() {
		String expected = "A2080435816D3C7C893E246B3651F12F80131984617468B0426DC6D9DD9ED0ED";
		String authCodeString = "TESTAPIKEY" + "|" + "card_token";

		Assert.assertEquals(expected, AuthCodeCalculator.calcAuthCode("private_key", authCodeString));
	}

	@Test
	public void testCalculateAuthCodeForApiKeyOrderNumberAndCardToken() {
		String expected = "AD8F8D0FB039C5685D54E9EC1DEEA2972C00C46F2ED4B260481BD225209C0982";
		String authCodeString = "TESTAPIKEY" + "|" + "a" + "|" + "card_token";

		Assert.assertEquals(expected, AuthCodeCalculator.calcAuthCode("private_key", authCodeString));
	}

	@Test(expected = NullPointerException.class)
	public void testCalculateAuthCodeThrowsExceptionWhenArgumentsHaveNullValues() {
		AuthCodeCalculator.calcAuthCode("private_key", null);
	}
}