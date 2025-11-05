package com.lucidity.restclients.clients;

import com.lucidity.endpoints.CartEndPoints;
import com.lucidity.endpoints.MockEndPoints;
import com.lucidity.restclients.BaseRestClient;
import com.lucidity.utils.PropertyReader;
import io.restassured.response.Response;

public class MockServiceRestClient {

    private static MockServiceRestClient mockServiceRestClient;
    private static BaseRestClient baseRestClient;
    private static final PropertyReader propertyReader = PropertyReader.getPropertyReader();

    private MockServiceRestClient(){
        baseRestClient = new BaseRestClient(
                propertyReader.getValueForGivenKey("mock_service.baseurl")
        );
    }

    public static MockServiceRestClient getInstance(){
        if(mockServiceRestClient == null) {
            mockServiceRestClient = new MockServiceRestClient();
        }
        return mockServiceRestClient;
    }

    public Response getSegmentForUser(int userId){
        return baseRestClient.whenGetRequestIsInvoked(MockEndPoints.GET_USER_SEGMENT,null,null, String.format("user_id=%s",userId),null);
    }


}
