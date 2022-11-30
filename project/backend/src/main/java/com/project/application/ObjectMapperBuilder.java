package com.project.application;

import java.util.TimeZone;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public final class ObjectMapperBuilder
{

    private boolean findAllModules;

    private AccessPolicy accessPolicy = AccessPolicy.DEFAULT;

    @Nonnull
    public static ObjectMapperBuilder builder()
    {
        return new ObjectMapperBuilder();
    }

    public static void applyDefaultConfiguration(ObjectMapper objectMapper)
    {
        if (objectMapper != null)
        {
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            objectMapper.getDeserializationConfig().with(TimeZone.getDefault());
        }
    }

    private ObjectMapperBuilder()
    {
        super();
    }

    @Nonnull
    public ObjectMapperBuilder withAllModules()
    {
        this.findAllModules = true;
        return this;
    }

    @Nonnull
    public ObjectMapperBuilder useFields()
    {
        this.accessPolicy = AccessPolicy.FIELDS;
        return this;
    }

    @Nonnull
    public ObjectMapper build()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        if (this.findAllModules)
        {
            objectMapper.findAndRegisterModules();
        }
        this.accessPolicy.apply(objectMapper);

        applyDefaultConfiguration(objectMapper);
        return objectMapper;
    }

    private enum AccessPolicy
    {
        DEFAULT
            {
                @Override
                public void apply(ObjectMapper objectMapper)
                {
                    // nop
                }
            },
        FIELDS
            {
                @Override
                public void apply(ObjectMapper objectMapper)
                {
                    BeanAccessPolicyConfigurer.preferFieldAccess(objectMapper);
                }
            },
        PROPERTIES
            {
                @Override
                public void apply(ObjectMapper objectMapper)
                {
                    BeanAccessPolicyConfigurer.preferPropertyAccess(objectMapper);
                }
            };

        public abstract void apply(ObjectMapper objectMapper);
    }
}
