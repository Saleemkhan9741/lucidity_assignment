package com.lucidity.cart;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lucidity.pojo.request.cart.CartRequest;
import com.lucidity.pojo.request.offer.OfferRequest;
import com.lucidity.pojo.response.cart.ApplyCartResponse;
import com.lucidity.pojo.response.offer.AddOfferResponse;
import com.lucidity.restclients.clients.CartServiceRestClient;
import com.lucidity.restclients.clients.MockServiceRestClient;
import com.lucidity.restclients.clients.OfferRestClient;
import com.lucidity.utils.DeserializerHelper;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class CartOfferApplicationTests {

    private static final Logger LOGGER = LogManager.getLogger(CartOfferApplicationTests.class);

    @Test(description = "verify apply cart with offer_type FLATX for restaurant_id 1")
    public void verifyApplyCartWithOfferTypeAndRestaurantId() throws JsonProcessingException {
        LOGGER.info("verify apply cart");
        MockServiceRestClient.mockUserSegment("2", "p2");
        OfferRequest offerRequest = OfferRequest.builder()
                .offerType("FLATX")
                .restaurantId(1)
                .offerValue(10)
                .customerSegment(List.of("p2"))
                .build();
        OfferRestClient.getInstance().addOffer(offerRequest);
        CartRequest cartRequest = CartRequest.builder()
                .cartValue(200)
                .restaurantId(1)
                .userId(2)
                .build();
        Response response = CartServiceRestClient.getInstance().applyOffer(cartRequest);
        ApplyCartResponse applyCartResponse = DeserializerHelper.getObjectMapper()
                .readValue(response.getBody().asString(), ApplyCartResponse.class);
        Assert.assertEquals(applyCartResponse.getCartValue(), 190);
    }

    @Test(description = "verify apply cart with offer_type FLATX for restaurant_id 1")
    public void verifyApplyCart() throws JsonProcessingException {
        LOGGER.info("verify apply cart");
        MockServiceRestClient.mockUserSegment("2", "p2");
        OfferRequest offerRequest = OfferRequest.builder()
                .offerType("FLATX_%")
                .restaurantId(1)
                .offerValue(10)
                .customerSegment(List.of("p2"))
                .build();
        OfferRestClient.getInstance().addOffer(offerRequest);
        CartRequest cartRequest = CartRequest.builder()
                .cartValue(200)
                .restaurantId(1)
                .userId(2)
                .build();
        Response response = CartServiceRestClient.getInstance().applyOffer(cartRequest);
        ApplyCartResponse applyCartResponse = DeserializerHelper.getObjectMapper()
                .readValue(response.getBody().asString(), ApplyCartResponse.class);
        Assert.assertEquals(applyCartResponse.getCartValue(), 190);
    }
}
