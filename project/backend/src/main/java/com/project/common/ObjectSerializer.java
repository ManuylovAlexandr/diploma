package com.project.common;

import java.io.IOException;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.application.ObjectMapperBuilder;

public class ObjectSerializer
{

    private static final ObjectSerializer INSTANCE = new ObjectSerializer(ObjectMapperBuilder.builder()
        .withAllModules()
        .useFields()
        .build()
    );

    private final ObjectMapper objectMapper;

    @Nonnull
    public static ObjectSerializer instance()
    {
        return INSTANCE;
    }

    private ObjectSerializer(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    public String serialize(Object object)
    {
        try
        {
            return this.objectMapper.writeValueAsString(object);
        }
        catch (JsonProcessingException e)
        {
            throw new JsonSerializationException("Failed to serialize object " + object, e);
        }
    }

    public <T> T deserialize(String json, Class<T> objectType)
    {
        try
        {
            return this.objectMapper.readValue(json, objectType);
        }
        catch (IOException e)
        {
            throw new JsonDeserializationException("Failed to deserialize JSON.", e);
        }
    }

    public static class JsonDeserializationException extends RuntimeException
    {

        private static final long serialVersionUID = -2041445360900625526L;

        public JsonDeserializationException(String message, Throwable cause)
        {
            super(message, cause);
        }
    }

    public static class JsonSerializationException extends RuntimeException
    {

        private static final long serialVersionUID = -6103959560513679478L;

        public JsonSerializationException(String message, Throwable cause)
        {
            super(message, cause);
        }
    }
}
