package com.lucidity.validator.cart;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lucidity.enums.OfferType;
import com.lucidity.pojo.request.cart.CartRequest;
import com.lucidity.pojo.request.offer.OfferRequest;
import com.lucidity.pojo.response.cart.ApplyCartResponse;
import com.lucidity.pojo.response.segment.GetUserSegment;
import com.lucidity.restclients.clients.MockServiceRestClient;
import com.lucidity.utils.DeserializerHelper;
import io.restassured.response.Response;
import org.testng.asserts.SoftAssert;

public class CartValidator {

    public static void validateCartValue(ApplyCartResponse applyCartResponse,
                                         CartRequest cartRequest,
                                         OfferRequest offerRequest, SoftAssert softAssert) throws JsonProcessingException {
        int expectedCartValue = getCartValue(cartRequest, offerRequest);
        softAssert.assertEquals(applyCartResponse.getCartValue(), expectedCartValue, "cart value doesn't match");
    }

    private static int getCartValue(CartRequest cartRequest, OfferRequest offerRequest) throws JsonProcessingException {
        Response response = MockServiceRestClient.getInstance()
                .getSegmentForUser(cartRequest.getUserId());
        GetUserSegment userSegment = DeserializerHelper.getObjectMapper()
                .readValue(response.getBody().asString(), GetUserSegment.class);
        int cartVal = cartRequest.getCartValue();
        if(userSegment.getSegment().isEmpty()) {
            return cartRequest.getCartValue();
        }
        if (offerRequest == null) {
            return cartVal;
        } else if (offerRequest.getRestaurantId() == cartRequest.getRestaurantId()
                && offerRequest.getCustomerSegment().contains(userSegment.getSegment())) {
            if (offerRequest.getOfferType().equals(OfferType.FLATX.name())) {
                return cartVal - offerRequest.getOfferValue();
            } else {
                return (int) (cartVal - cartVal * offerRequest.getOfferValue() * (0.01));
            }
        } else {
            return cartVal;
        }
    }
}
