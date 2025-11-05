package com.lucidity.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class SerializerHelper {
  public static ObjectMapper getObjectMapper() {
    return new ObjectMapper();
  }

  public static ObjectMapper getObjectMapperWithDateTime() {
    return new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
  }
}
