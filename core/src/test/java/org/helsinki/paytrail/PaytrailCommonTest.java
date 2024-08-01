package org.helsinki.paytrail;

public class PaytrailCommonTest {
    /* Normal test merchant account */
    protected final String merchantId = "375917";
    protected final String secretKey = "SAIPPUAKAUPPIAS";

    /* Shop-in-shop test merchant account */
    protected final String aggregateMerchantId = "695861";
    protected final String aggregateSecretKey = "MONISAIPPUAKAUPPIAS";
    protected final String shopInShopMerchantId = "695874";

    // Tokenization 	Payment 	Card number 	        Expiry 	    CVC 	Description
    // OK 	            OK 	        4153 0139 9970 0313 	11/2026 	313 	Successful 3D Secure. 3DS form password "secret".
    protected final String cardToken1 = "e4e2f161-8078-45de-925a-50c8cfc2fed0";
    // OK           	FAIL 	    4153 0139 9970 0354 	11/2023 	354 	Successful 3D Secure. 3DS form password "secret". Insufficient funds in the test bank account.
    protected final String cardToken2 = "0ff52860-894f-4a69-b457-604249e9ba03";
}
