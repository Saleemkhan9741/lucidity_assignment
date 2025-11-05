package com.lucidity.utils;

import org.mockserver.client.MockServerClient;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class MockServerUtil {

    private static final String HOST = "localhost";
    private static final int PORT = 1080;

    /**
     * Adds a user segment mock to the running MockServer
     * Example: mockUserSegment("1", "p1");
     */
    public static void mockUserSegment(String userId, String segment) {
        try (MockServerClient client = new MockServerClient(HOST, PORT)) {
            client
                    .when(
                            request()
                                    .withMethod("GET")
                                    .withPath("/api/v1/user_segment")
                                    .withQueryStringParameter("user_id", userId)
                    )
                    .respond(
                            response()
                                    .withStatusCode(200)
                                    .withBody("{\"segment\": \"" + segment + "\"}")
                    );
        }
    }

    /**
     * Clears all existing mocks from the MockServer
     */
    public static void clearAllMocks() {
        try (MockServerClient client = new MockServerClient(HOST, PORT)) {
            client.reset();
        }
    }
}
