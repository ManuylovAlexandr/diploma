package com.project.domain.event;

public final class EventSerializationException extends RuntimeException
{

    private static final long serialVersionUID = -6361926318484474191L;

    public EventSerializationException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
