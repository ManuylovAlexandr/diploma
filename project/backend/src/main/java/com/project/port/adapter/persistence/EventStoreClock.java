package com.project.port.adapter.persistence;

import java.time.OffsetDateTime;

import javax.annotation.Nonnull;

import org.springframework.stereotype.Component;

@Component
public class EventStoreClock
{

    @Nonnull
    public OffsetDateTime now()
    {
        return OffsetDateTime.now();
    }
}
