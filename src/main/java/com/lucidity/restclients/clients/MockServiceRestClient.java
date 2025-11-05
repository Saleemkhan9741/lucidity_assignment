package com.lucidity.restclients.clients;

import com.lucidity.endpoints.CartEndPoints;
import com.lucidity.endpoints.MockEndPoints;
import com.lucidity.enums.HTTPRequestType;
import com.lucidity.restclients.BaseRestClient;
import com.lucidity.utils.PropertyReader;
import io.restassured.response.Response;
import org.mockserver.client.MockServerClient;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class MockServiceRestClient {

    private static MockServiceRestClient mockServiceRestClient;
    private static BaseRestClient baseRestClient;
    private static final PropertyReader propertyReader = PropertyReader.getPropertyReader();
    private static final String HOST = propertyReader.getValueForGivenKey("mock_service_host");
    private static final int PORT = Integer.parseInt(propertyReader.getValueForGivenKey("mock_service_port"));

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

    /**
     * Adds a user segment mock to the running MockServer
     * Example: mockUserSegment("1", "p1");
     */
    public static void mockUserSegment(String userId, String segment) {
        MockServerClient client = new MockServerClient(HOST, PORT);
        client
                .when(
                        request()
                                .withMethod(HTTPRequestType.GET.name())
                                .withPath(MockEndPoints.GET_USER_SEGMENT.getResourceURL())
                                .withQueryStringParameter("user_id", userId)
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody("{\"segment\": \"" + segment + "\"}")
                );
    }

    /**
     * Clears all existing mocks from the MockServer
     */
    public static void clearAllMocks() {
        MockServerClient client = new MockServerClient(HOST, PORT);
        client.reset();
    }


}
