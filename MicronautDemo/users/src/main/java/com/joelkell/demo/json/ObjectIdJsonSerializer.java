package com.joelkell.demo.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ObjectIdJsonSerializer extends JsonSerializer<ObjectId> {
  private static final Logger LOGGER = LoggerFactory.getLogger(ObjectIdJsonSerializer.class);

  @Override
  public void serialize(
      ObjectId objectId, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) {
    try {
      jsonGenerator.writeString(objectId.toString());
    } catch (IOException e) {
      LOGGER.error("Object ID not serialized", e);
    }
  }
}
