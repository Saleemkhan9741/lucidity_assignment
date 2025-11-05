package com.lucidity.restclients.clients;

import com.lucidity.endpoints.OfferEndPoints;
import com.lucidity.pojo.request.offer.OfferRequest;
import com.lucidity.restclients.BaseRestClient;
import com.lucidity.utils.PropertyReader;
import io.restassured.response.Response;

public class OfferRestClient {

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

    public Response addOffer(OfferRequest offerRequest){
        return baseRestClient.whenPostRequestIsInvoked(OfferEndPoints.ADD_OFFER,null,offerRequest);
    }


}
