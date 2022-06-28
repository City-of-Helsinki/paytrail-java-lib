package org.helsinki.paytrail.request.auth;

import junit.framework.TestCase;
import org.helsinki.paytrail.request.auth.constants.PaytrailAuthHeaders;
import org.junit.Assert;

import java.util.List;

public class PaytrailAuthHeadersTest extends TestCase {

    public void testKeys() {
        List<String> sortedKeys = PaytrailAuthHeaders.getSortedKeys();
        System.out.println(sortedKeys);
        Assert.assertNotNull(sortedKeys);
        Assert.assertEquals(PaytrailAuthHeaders.class.getEnumConstants().length, sortedKeys.size());
    }
}