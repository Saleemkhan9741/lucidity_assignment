package com.lucidity.cart;

import com.lucidity.pojo.request.offer.OfferRequest;
import com.lucidity.restclients.clients.MockServiceRestClient;
import com.lucidity.restclients.clients.OfferRestClient;
import com.lucidity.utils.DeserializerHelper;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

public class CartOfferApplicationTests {

    private static final Logger LOGGER = LogManager.getLogger(CartOfferApplicationTests.class);

    @Test(description = "verify apply cart")
    public void verifyApplyCart(){
        LOGGER.info("verify apply cart");
        MockServiceRestClient.mockUserSegment("2","p2");
        OfferRequest offerRequest = OfferRequest.builder()
                .offerType("FLATX")
                .restaurantId(1)
                .offerValue(10)
                .build();
        Response response = OfferRestClient.getInstance()
                .addOffer(offerRequest);
        DeserializerHelper.getObjectMapper().readValue()

    }
