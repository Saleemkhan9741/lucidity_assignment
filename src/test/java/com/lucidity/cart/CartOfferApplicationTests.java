package com.lucidity.cart;

import com.aventstack.extentreports.ExtentTest;
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
import com.lucidity.reporting.ReportManager;
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
            String testCaseNo,
            String restaurantId,
            String userSegment,
            String offerType,
            int offerValue,
            int cartValue,
            int expectedFinalValue
    ) throws JsonProcessingException {

        LOGGER.info("Running test for test-id : {}, offerType: {}, segment: {}, value: {}", testCaseNo, offerType, userSegment, offerValue);
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

    @Test(description = "TC07: Verify apply cart API for when user is not in any offer segment")
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

    @Test(description = "TC08: Verify when offer created for restaurant 1 and user is trying from restaurant 2")
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

    @Test(description = "TC09 : Verify when offer value is more than cart value, then value should not be negative")
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

    @Test(description = "TC10: verify offer created for multiple restaurants for same segment id")
    public void verifyOfferCreatedForMultipleRestForSameSegmentId() throws JsonProcessingException {
        SoftAssert softAssert = new SoftAssert();

        // Step 1: Mock user segment
        MockServiceRestClient.mockUserSegment("20", "p15");

        // Step 2: Create offer
        OfferRequest offerRequest1 = OfferRequest.builder()
                .offerType(OfferType.FLATX.name())
                .restaurantId(1)
                .offerValue(100)
                .customerSegment(List.of("p15"))
                .build();
        OfferRestClient.getInstance().addOffer(offerRequest1);

        OfferRequest offerRequest2 = OfferRequest.builder()
                .offerType(OfferType.FLATX.name())
                .restaurantId(2)
                .offerValue(20)
                .customerSegment(List.of("p15"))
                .build();
        OfferRestClient.getInstance().addOffer(offerRequest2);

        // Step 3: Apply cart offer
        CartRequest cartRequest1 = CartRequest.builder()
                .cartValue(150)
                .restaurantId(1)
                .userId(20)
                .build();
        Response response = CartServiceRestClient.getInstance().applyOffer(cartRequest1);

        ApplyCartResponse applyCartResponse1 = DeserializerHelper.getObjectMapper()
                .readValue(response.getBody().asString(), ApplyCartResponse.class);

        // Step 5: Assertion for expected value
        softAssert.assertEquals(applyCartResponse1.getCartValue(), 50,
                "Cart value response 1 after applying offer should match expected final value");

        CartRequest cartRequest2 = CartRequest.builder()
                .cartValue(150)
                .restaurantId(2)
                .userId(20)
                .build();
        // Step 4: Deserialize and validate

        Response response2 = CartServiceRestClient.getInstance().applyOffer(cartRequest2);

        ApplyCartResponse applyCartResponse2 = DeserializerHelper.getObjectMapper()
                .readValue(response2.getBody().asString(), ApplyCartResponse.class);

        // Step 5: Assertion for expected value
        softAssert.assertEquals(applyCartResponse2.getCartValue(), 130,
                "Cart value response 2 after applying offer should match expected final value");

        MockServiceRestClient.clearAllMocks();
        softAssert.assertAll();
    }

    @Test(description = "TC11: Verify apply cart without cart value, error msg should be thrown")
    public void verifyApplyCartWithoutCartValue() throws JsonProcessingException {
        SoftAssert softAssert = new SoftAssert();

        // Step 1: Mock user segment
        MockServiceRestClient.mockUserSegment("10", "p12");

        // Step 2: Create offer
        OfferRequest offerRequest = OfferRequest.builder()
                .offerType(OfferType.FLATX.name())
                .restaurantId(1)
                .offerValue(150)
                .customerSegment(List.of("p12"))
                .build();
        OfferRestClient.getInstance().addOffer(offerRequest);

        // Step 3: Apply cart offer
        CartRequest cartRequest = CartRequest.builder()
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

    @Test(description = "TC12: Verify when multiple offers are applied for same segment")
    public void verifyWhenMultipleOffersAreAppliedForSameSegment() throws JsonProcessingException {
        SoftAssert softAssert = new SoftAssert();

        // Step 1: Mock user segment
        MockServiceRestClient.mockUserSegment("1", "p3");

        // Step 2: Create offer
        OfferRequest offerRequest1 = OfferRequest.builder()
                .offerType(OfferType.FLATX.name())
                .restaurantId(1)
                .offerValue(150)
                .customerSegment(List.of("p3"))
                .build();
        OfferRestClient.getInstance().addOffer(offerRequest1);

        // Step 2: Create offer
        OfferRequest offerRequest2 = OfferRequest.builder()
                .offerType(OfferType.FLATX.name())
                .restaurantId(1)
                .offerValue(200)
                .customerSegment(List.of("p3"))
                .build();
        OfferRestClient.getInstance().addOffer(offerRequest2);

        // Step 3: Apply cart offer
        CartRequest cartRequest = CartRequest.builder()
                .restaurantId(1)
                .userId(1)
                .cartValue(200)
                .build();
        Response response = CartServiceRestClient.getInstance().applyOffer(cartRequest);

        // Step 4: Deserialize and validate
        ApplyCartResponse applyCartResponse = DeserializerHelper.getObjectMapper()
                .readValue(response.getBody().asString(), ApplyCartResponse.class);

        // Step 5: Assertion for expected value
        softAssert.assertEquals(applyCartResponse.getCartValue(), 200,
                "Max offer should get applied");
        MockServiceRestClient.clearAllMocks();
        softAssert.assertAll();
    }

    @Test(description = "TC13: Verify when offer value is negative")
    public void verifyWhenOfferValueIsNegative() throws JsonProcessingException {
        SoftAssert softAssert = new SoftAssert();

        // Step 1: Mock user segment
        MockServiceRestClient.mockUserSegment("20", "p1");

        // Step 2: Create offer
        OfferRequest offerRequest = OfferRequest.builder()
                .offerType(OfferType.FLATX.name())
                .restaurantId(1)
                .offerValue(-10)
                .customerSegment(List.of("p1"))
                .build();
        OfferRestClient.getInstance().addOffer(offerRequest);

        // Step 3: Apply cart offer
        CartRequest cartRequest = CartRequest.builder()
                .restaurantId(1)
                .userId(20)
                .cartValue(200)
                .build();
        Response response = CartServiceRestClient.getInstance().applyOffer(cartRequest);

        // Step 4: Deserialize and validate
        ApplyCartResponse applyCartResponse = DeserializerHelper.getObjectMapper()
                .readValue(response.getBody().asString(), ApplyCartResponse.class);

        // Step 5: Assertion for expected value
        softAssert.assertEquals(applyCartResponse.getCartValue(), 200,
                "Max offer should get applied");
        MockServiceRestClient.clearAllMocks();
        softAssert.assertAll();
    }
}
