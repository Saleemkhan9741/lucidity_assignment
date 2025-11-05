package com.lucidity.cart;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lucidity.data.cart.CartDataProvider;
import com.lucidity.enums.OfferType;
import com.lucidity.pojo.request.cart.CartRequest;
import com.lucidity.pojo.request.offer.OfferRequest;
import com.lucidity.pojo.response.cart.ApplyCartResponse;
import com.lucidity.restclients.clients.CartServiceRestClient;
import com.lucidity.restclients.clients.MockServiceRestClient;
import com.lucidity.restclients.clients.OfferRestClient;
import com.lucidity.utils.DeserializerHelper;
import com.lucidity.validator.cart.CartValidator;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

public class CartOfferApplicationTests {

    private static final Logger LOGGER = LogManager.getLogger(CartOfferApplicationTests.class);

    @Test(dataProvider = "offerTestData", dataProviderClass = CartDataProvider.class,
            description = "Verify apply cart API for multiple offer types and segments")
    public void verifyApplyCartWithVariousOffers(
            String restaurantId,
            String userSegment,
            String offerType,
            int offerValue,
            int cartValue,
            int expectedFinalValue
    ) throws JsonProcessingException {

        LOGGER.info("Running test for offerType: {}, segment: {}, value: {}", offerType, userSegment, offerValue);
        SoftAssert softAssert = new SoftAssert();

        // Step 1: Mock user segment
        MockServiceRestClient.mockUserSegment("1", userSegment);

        // Step 2: Create offer
        OfferRequest offerRequest = OfferRequest.builder()
                .offerType(offerType)
                .restaurantId(Integer.parseInt(restaurantId))
                .offerValue(offerValue)
                .customerSegment(List.of(userSegment))
                .build();
        OfferRestClient.getInstance().addOffer(offerRequest);

        // Step 3: Apply cart offer
        CartRequest cartRequest = CartRequest.builder()
                .cartValue(cartValue)
                .restaurantId(Integer.parseInt(restaurantId))
                .userId(1)
                .build();
        Response response = CartServiceRestClient.getInstance().applyOffer(cartRequest);

        // Step 4: Deserialize and validate
        ApplyCartResponse applyCartResponse = DeserializerHelper.getObjectMapper()
                .readValue(response.getBody().asString(), ApplyCartResponse.class);

        LOGGER.info("Response cart value: {}", applyCartResponse.getCartValue());
        CartValidator.validateCartValue(applyCartResponse, cartRequest, offerRequest, softAssert);

        // Step 5: Assertion for expected value
        softAssert.assertEquals(applyCartResponse.getCartValue(), expectedFinalValue,
                "Cart value after applying offer should match expected final value");
        MockServiceRestClient.clearAllMocks();
        softAssert.assertAll();
    }

    @Test(description = "Verify apply cart API for when user is not in any offer segment")
    public void verifyApplyCartWhenUserIsNotInAnyOfferSegment() throws JsonProcessingException {
        SoftAssert softAssert = new SoftAssert();

        // Step 1: Mock user segment
        MockServiceRestClient.mockUserSegment("5", "p10");
        // Step 2: Apply cart offer
        CartRequest cartRequest = CartRequest.builder()
                .cartValue(100)
                .restaurantId(1)
                .userId(5)
                .build();
        Response response = CartServiceRestClient.getInstance().applyOffer(cartRequest);

        // Step 3: Deserialize and validate
        ApplyCartResponse applyCartResponse = DeserializerHelper.getObjectMapper()
                .readValue(response.getBody().asString(), ApplyCartResponse.class);

        // Step 4: Assertion for expected value
        softAssert.assertEquals(applyCartResponse.getCartValue(), 100,
                "Cart value after applying offer should match expected final value");
        MockServiceRestClient.clearAllMocks();
        softAssert.assertAll();
    }

    @Test(description = "Verify when offer created for restaurant 1 and user is trying from restaurant 2")
    public void verifyWhenOfferCreatedRest1AndUserInRest2() throws JsonProcessingException {
        SoftAssert softAssert = new SoftAssert();

        // Step 1: Mock user segment
        MockServiceRestClient.mockUserSegment("10", "p4");

        // Step 2: Create offer
        OfferRequest offerRequest = OfferRequest.builder()
                .offerType(OfferType.FLATX.name())
                .restaurantId(1)
                .offerValue(10)
                .customerSegment(List.of("p4"))
                .build();
        OfferRestClient.getInstance().addOffer(offerRequest);

        // Step 3: Apply cart offer
        CartRequest cartRequest = CartRequest.builder()
                .cartValue(100)
                .restaurantId(2)
                .userId(1)
                .build();
        Response response = CartServiceRestClient.getInstance().applyOffer(cartRequest);

        // Step 4: Deserialize and validate
        ApplyCartResponse applyCartResponse = DeserializerHelper.getObjectMapper()
                .readValue(response.getBody().asString(), ApplyCartResponse.class);

        // Step 5: Assertion for expected value
        softAssert.assertEquals(applyCartResponse.getCartValue(), 100,
                "Cart value after applying offer should match expected final value");
        MockServiceRestClient.clearAllMocks();
        softAssert.assertAll();
    }

    @Test(description = "Verify when offer value is more than cart value, then value should not be negative")
    public void verifyWhenOfferValueIsMoreThanCartValue() throws JsonProcessingException {
        SoftAssert softAssert = new SoftAssert();

        // Step 1: Mock user segment
        MockServiceRestClient.mockUserSegment("1", "p11");

        // Step 2: Create offer
        OfferRequest offerRequest = OfferRequest.builder()
                .offerType(OfferType.FLATX.name())
                .restaurantId(1)
                .offerValue(150)
                .customerSegment(List.of("p11"))
                .build();
        OfferRestClient.getInstance().addOffer(offerRequest);

        // Step 3: Apply cart offer
        CartRequest cartRequest = CartRequest.builder()
                .cartValue(100)
                .restaurantId(1)
                .userId(1)
                .build();
        Response response = CartServiceRestClient.getInstance().applyOffer(cartRequest);

        // Step 4: Deserialize and validate
        ApplyCartResponse applyCartResponse = DeserializerHelper.getObjectMapper()
                .readValue(response.getBody().asString(), ApplyCartResponse.class);

        // Step 5: Assertion for expected value
        softAssert.assertEquals(applyCartResponse.getCartValue(), 0,
                "Cart value after applying offer should match expected final value");
        MockServiceRestClient.clearAllMocks();
        softAssert.assertAll();
    }
}
