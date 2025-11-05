package com.lucidity.restclients.clients;

import com.lucidity.endpoints.CartEndPoints;
import com.lucidity.pojo.CartRequest;
import com.lucidity.restclients.BaseRestClient;
import com.lucidity.utils.PropertyReader;
import io.restassured.response.Response;

public class CartServiceRestClient {

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

    public Response applyOffer(CartRequest cartRequest){
       return baseRestClient.whenPostRequestIsInvoked(CartEndPoints.APPLY_OFFER,null,cartRequest);
    }
}
