package com.project.domain.event;


public final class EventDeserializationException extends RuntimeException
{

    private static final long serialVersionUID = 1199156767830719670L;

    public EventDeserializationException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
