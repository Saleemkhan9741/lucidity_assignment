package com.lucidity.restclients.clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lucidity.endpoints.OfferEndPoints;
import com.lucidity.pojo.request.offer.OfferRequest;
import com.lucidity.pojo.response.offer.AddOfferResponse;
import com.lucidity.restclients.BaseRestClient;
import com.lucidity.utils.DeserializerHelper;
import com.lucidity.utils.PropertyReader;
import com.lucidity.utils.SerializerHelper;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import java.util.HashMap;
import java.util.Map;

public class OfferRestClient {

    private static final Logger LOGGER = LogManager.getLogger(OfferRestClient.class);
    private static OfferRestClient offerClient;
    private static BaseRestClient baseRestClient;
    private static final PropertyReader propertyReader = PropertyReader.getPropertyReader();

    private OfferRestClient(){
        baseRestClient = new BaseRestClient(
                propertyReader.getValueForGivenKey("offer.baseurl")
        );
    }

    public static OfferRestClient getInstance(){
        if(offerClient == null) {
            offerClient = new OfferRestClient();
        }
        return offerClient;
    }

    public Response addOffer(OfferRequest offerRequest) throws JsonProcessingException {
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type","application/json");
        String request = SerializerHelper.getObjectMapper()
                .writeValueAsString(offerRequest);
        Response response = baseRestClient.whenPostRequestIsInvoked(OfferEndPoints.ADD_OFFER,
                headers, request);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK);
        AddOfferResponse addOfferResponse = DeserializerHelper.getObjectMapper()
                .readValue(response.getBody().asString(), AddOfferResponse.class);
        Assert.assertEquals(addOfferResponse.getResponseMsg(),"success");
        LOGGER.info("Add Offer response {}", response.getBody().asString());
        return response;
    }


}
