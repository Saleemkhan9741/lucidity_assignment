package com.lucidity.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.HashMap;

public class DeserializerHelper {
  public static ObjectMapper getObjectMapperWithDateTime() {
    return new ObjectMapper()
        .registerModule(new JavaTimeModule())
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
  }

  public static ObjectMapper getObjectMapper() {
    return new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  public static HashMap<String, Object> toMap(String payload) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    TypeReference<HashMap<String, Object>> typeRef =
        new TypeReference<HashMap<String, Object>>() {};

    HashMap<String, Object> object = mapper.readValue(payload, typeRef);
    return object;
  }

  public static JsonNode toJsonNode(String responseAsString) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setNodeFactory(JsonNodeFactory.withExactBigDecimals(true));
    objectMapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);
    JsonNode jsonNode = objectMapper.readValue(responseAsString, JsonNode.class);
    return jsonNode;
  }

  public static <T> T treeToValue(JsonNode jsonNode, Class<T> c) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setNodeFactory(JsonNodeFactory.withExactBigDecimals(true));
    objectMapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);
    return objectMapper.treeToValue(jsonNode, c);
  }
}
