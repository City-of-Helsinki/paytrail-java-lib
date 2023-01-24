package org.helsinki.paytrail.request.tokenization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.helsinki.paytrail.PaytrailClient;
import org.helsinki.paytrail.PaytrailCommonTest;
import org.helsinki.paytrail.mapper.PaytrailGetTokenResponseMapper;
import org.helsinki.paytrail.response.tokenization.PaytrailGetTokenResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
public class PaytrailGetTokenRequestTest extends PaytrailCommonTest {

    PaytrailGetTokenResponseMapper mapper;

    public PaytrailGetTokenRequestTest() {
        this.mapper = new PaytrailGetTokenResponseMapper(new ObjectMapper());
    }

    @Test
    void getToken() throws ExecutionException, InterruptedException {
        PaytrailClient client = new PaytrailClient(merchantId, secretKey);

        // Requires a valid checkout tokenization id
        String tokenizationId = "123";
        PaytrailGetTokenRequest request = new PaytrailGetTokenRequest(tokenizationId);

        CompletableFuture<PaytrailGetTokenResponse> response = client.sendRequest(request);

        PaytrailGetTokenResponse tokenResponse = mapper.to(response.get());

        // With a valid checkout tokenization id, you can test assertion with NOT NULL
        if (tokenResponse.getTokenResponse() != null) {
            Assertions.assertNotNull(tokenResponse.getTokenResponse());
        } else {
            Assertions.assertNull(tokenResponse.getTokenResponse());
        }

        try {
            Gson gson = new Gson();
            String json = gson.toJson(response.get());
            System.out.println(json);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
