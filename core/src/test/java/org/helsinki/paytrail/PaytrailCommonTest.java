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
    // OK 	            OK 	        4153 0139 9970 0313 	11/2023 	313 	Successful 3D Secure. 3DS form password "secret".
    protected final String cardToken1 = "2f4de4b9-94ec-4bd5-ab39-45f1f164bda0";
    // OK           	FAIL 	    4153 0139 9970 0354 	11/2023 	354 	Successful 3D Secure. 3DS form password "secret". Insufficient funds in the test bank account.
    protected final String cardToken2 = "0ff52860-894f-4a69-b457-604249e9ba03";
}
