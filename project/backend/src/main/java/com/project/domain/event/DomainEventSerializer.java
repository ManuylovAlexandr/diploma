package com.project.domain.event;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import com.project.application.ObjectMapperBuilder;

@Component
public class DomainEventSerializer
{

    private static final ObjectMapper OBJECT_MAPPER = ObjectMapperBuilder.builder()
        .withAllModules()
        .useFields()
        .build();

    @Nonnull
    public <T extends DomainEvent> String serialize(@Nonnull T event)
    {
        try
        {
            return OBJECT_MAPPER.writeValueAsString(event);
        }
        catch (JsonProcessingException e)
        {
            throw new EventSerializationException("Failed to serialize event.", e);
        }
    }
}
