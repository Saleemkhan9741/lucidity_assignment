package com.lucidity.cart;

import com.lucidity.restclients.clients.MockServiceRestClient;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

public class CartOfferApplicationTests {

    private static final Logger LOGGER = LogManager.getLogger(CartOfferApplicationTests.class);

    @Test(description = "verify apply cart")
    public void verifyApplyCart(){
        LOGGER.info("verify apply cart");
        Response response = MockServiceRestClient.getInstance().getSegmentForUser(1);
        System.out.println(response.getBody().asString());
    }
}
