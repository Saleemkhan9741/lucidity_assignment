package com.lucidity.restclients;


import com.lucidity.enums.HTTPRequestType;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.text.MessageFormat;
import java.util.Map;

public class BaseRestClient {
    private static final Logger LOGGER = LogManager.getLogger(BaseRestClient.class);
    protected RequestSpecification request;
    protected Response response;
    String baseURL;
    String userName;
    String password;

    public BaseRestClient(String baseURL) {
        this.baseURL = baseURL;
    }

    public BaseRestClient(String baseURL, String userName, String password) {
        this.baseURL = baseURL;
        this.userName = userName;
        this.password = password;
        RestAssured.baseURI = baseURL;
        setBasicAuthentication();
    }

    protected void setBasicAuthentication() {
        if (this.userName != null && this.password != null) {
            request.auth().preemptive().basic(userName, password);
        }
    }

    public Response whenRequestIsInvoked(
            HTTPRequestType requestType,
            Resource resource,
            Map<String, String> headers,
            Object payload,
            String entityId,
            String queryParameter,
            Map<String, ?> formParam,
            String[] urlParams) {
        RestAssured.baseURI = this.baseURL;
        request = RestAssured.given();
        if (headers != null && !headers.isEmpty()) {
            request.headers(headers);
        }

        if (formParam != null && !formParam.isEmpty()) {
            request.formParams(formParam);
        }

        String endPoint = formattedUrl(resource, entityId, queryParameter, urlParams);

        if (payload != null) {
            if (payload instanceof Map) {
                if (((Map<Object, Object>) payload).containsKey("file")) {
                    if (((Map<Object, Object>) payload).containsKey("octetHandle")) {
                        request.multiPart("file", new File(((Map<Object, Object>) payload).get("file").toString()), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                    }
                    else
                    {
                        request.multiPart("file", new File(((Map<Object, Object>) payload).get("file").toString()));
                    }
                    if(((Map<Object, Object>) payload).containsKey("accept")){
                        request.accept(((Map<Object, Object>) payload).get("accept").toString());
                    }
                    else request.accept(ContentType.TEXT);

                }
                if (((Map<Object, Object>) payload).containsKey("image")) {
                    request.multiPart(
                            "image", new File(((Map<Object, Object>) payload).get("image").toString()));
                    request.accept(ContentType.TEXT);
                }
            } else {
                request.body(payload);
            }
        }
        LOGGER.info("Server URL : {}", this.baseURL);
        LOGGER.info(String.format("Http Request type : %s", requestType));
        LOGGER.info(String.format("Http Request endpoint: %s", endPoint));
        LOGGER.info(String.format("Http Request payload: %s", payload));

        switch (requestType) {
            case GET:
                if (endPoint != null) {
                    return request.get(endPoint);
                } else return request.get();
            case PUT:
                if (endPoint != null) {
                    return request.put(endPoint);
                } else return request.put();
            case POST:
                if (endPoint != null) {
                    return request.post(endPoint);
                } else return request.post();
            case DELETE:
                if (endPoint != null) {
                    return request.delete(endPoint);
                } else return request.delete();
            case PATCH:
                if (endPoint != null) {
                    return request.patch(endPoint);
                } else return request.patch();
            default:
                throw new RuntimeException("Invalid Request body Type");
        }
    }

    public Response whenGetRequestIsInvoked(
            Resource resource,
            Map<String, String> headers,
            String entityId,
            String queryParameter,
            String... urlParams) {
        return whenRequestIsInvoked(
                HTTPRequestType.GET, resource, headers, null, entityId, queryParameter, null, urlParams);
    }

    public Response whenGetRequestIsInvoked(Resource response) {
        return whenGetRequestIsInvoked(response, null, null, null, null);
    }

    public Response whenGetRequestIsInvoked(Map<String, String> headers) {
        return whenGetRequestIsInvoked(null, headers, null, null, null);
    }

    public Response whenGetRequestIsInvoked(Resource resource, Map<String, String> headers) {
        return whenGetRequestIsInvoked(resource, headers, null, null, null);
    }

    public Response whenGetRequestIsInvoked(
            Resource resource, Map<String, String> headers, String entityId) {
        return whenGetRequestIsInvoked(resource, headers, entityId, null, null);
    }

    public Response whenPostRequestIsInvoked(
            Resource resource,
            Map<String, String> headers,
            Object payload,
            String entityId,
            String queryParameter,
            Map<String, ?> formParams,
            String... urlParams) {
        return whenRequestIsInvoked(
                HTTPRequestType.POST,
                resource,
                headers,
                payload,
                entityId,
                queryParameter,
                formParams,
                urlParams);
    }

    public Response whenPostRequestIsInvoked(
            Resource resource, Map<String, String> headers, Object payload) {
        return whenPostRequestIsInvoked(resource, headers, payload, null, null, null, null, null);
    }

    public Response whenPostRequestIsInvoked(
            Resource resource,
            Map<String, String> headers,
            Object payload,
            String entityId,
            String queryParam) {
        return whenPostRequestIsInvoked(
                resource, headers, payload, entityId, queryParam, null, null, null);
    }

    public Response whenPutRequestIsInvoked(
            Resource resource,
            Map<String, String> headers,
            Object payload,
            String entityId,
            String queryParameter,
            Map<String, ?> formParams,
            String... urlParams) {
        return whenRequestIsInvoked(
                HTTPRequestType.PUT,
                resource,
                headers,
                payload,
                entityId,
                queryParameter,
                formParams,
                urlParams);
    }

    public Response whenPutRequestIsInvoked(
            Resource resource, Map<String, String> headers, Object payload) {

        return whenPutRequestIsInvoked(resource, headers, payload, null, null, null);
    }

    public Response whenDeleteRequestIsInvoked(
            Resource resource,
            Map<String, String> headers,
            String entityId,
            String queryParameter,
            String... urlParams) {
        return whenRequestIsInvoked(
                HTTPRequestType.DELETE, resource, headers, null, entityId, queryParameter, null, urlParams);
    }

    public Response whenDeleteRequestIsInvoked(
            Resource resource, Map<String, String> headers, String entityId) {
        return whenDeleteRequestIsInvoked(resource, headers, entityId, null, null);
    }

    public Response whenDeleteRequestIsInvoked(Resource resource, Map<String, String> headers) {
        return whenDeleteRequestIsInvoked(resource, headers, null, null, null);
    }
    public Response whenPatchRequestIsInvoked(
            Resource resource,
            Map<String, String> headers,
            Object payload,
            String entityId,
            String queryParameter,
            Map<String, ?> formParams,
            String... urlParams) {
        return whenRequestIsInvoked(
                HTTPRequestType.PATCH,
                resource,
                headers,
                payload,
                entityId,
                queryParameter,
                formParams,
                urlParams);
    }
    public Response whenPatchRequestIsInvoked(
            Resource resource, Map<String, String> headers, Object payload) {
        return whenPatchRequestIsInvoked(resource, headers, payload, null, null, null,null);
    }

    private String formattedUrl(
            Resource resource, String entityId, String queryParamter, String... urlParams) {
        if (resource == null) {
            return null;
        }
        String endPoint = resource.getResourceURL();

        if (entityId != null) {
            endPoint = endPoint + "/" + entityId;
        }

        if (queryParamter != null) {
            endPoint = endPoint + "?" + queryParamter;
        }
        if (urlParams != null) {
            endPoint = MessageFormat.format(endPoint, urlParams);
        }

        LOGGER.info("Endpoint : {}", endPoint);
        return endPoint;
    }
}
