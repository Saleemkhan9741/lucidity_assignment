package com.lucidity.restclients.clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lucidity.endpoints.CartEndPoints;
import com.lucidity.pojo.request.cart.CartRequest;
import com.lucidity.restclients.BaseRestClient;
import com.lucidity.utils.PropertyReader;
import com.lucidity.utils.SerializerHelper;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import java.util.HashMap;
import java.util.Map;

public class CartServiceRestClient {

    private static final Logger LOGGER = LogManager.getLogger(CartServiceRestClient.class);

    private static CartServiceRestClient cartServiceRestClient;
    private static BaseRestClient baseRestClient;
    private static final PropertyReader propertyReader = PropertyReader.getPropertyReader();

    private CartServiceRestClient(){
        baseRestClient = new BaseRestClient(
                propertyReader.getValueForGivenKey("cart.baseurl")
        );
    }

    public static CartServiceRestClient getInstance(){
        if(cartServiceRestClient == null) {
            cartServiceRestClient = new CartServiceRestClient();
        }
        return cartServiceRestClient;
    }

    public Response applyOffer(CartRequest cartRequest) throws JsonProcessingException {
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type","application/json");
        String request = SerializerHelper.getObjectMapper()
                .writeValueAsString(cartRequest);
        Response response = baseRestClient.whenPostRequestIsInvoked(
                CartEndPoints.APPLY_OFFER, headers, request);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK);
        LOGGER.info("Apply Offer response {}", response.getBody().asString());
        return response;
    }
}
