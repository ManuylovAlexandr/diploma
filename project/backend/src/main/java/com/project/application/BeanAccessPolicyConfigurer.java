package com.project.application;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class BeanAccessPolicyConfigurer
{

    public static ObjectMapper preferPropertyAccess(ObjectMapper objectMapper)
    {
        if (objectMapper != null)
        {
            resetVisibility(objectMapper);
            objectMapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.DEFAULT);
            objectMapper.setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.DEFAULT);
            objectMapper.setVisibility(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.DEFAULT);
        }
        return objectMapper;
    }

    public static ObjectMapper preferFieldAccess(ObjectMapper objectMapper)
    {
        if (objectMapper != null)
        {
            resetVisibility(objectMapper);
            objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        }
        return objectMapper;
    }

    private BeanAccessPolicyConfigurer()
    {
        super();
    }

    private static void resetVisibility(ObjectMapper objectMapper)
    {
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.CREATOR, JsonAutoDetect.Visibility.DEFAULT);
    }
}
