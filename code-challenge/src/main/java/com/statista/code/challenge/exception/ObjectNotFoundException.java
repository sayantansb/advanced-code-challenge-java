package com.statista.code.challenge.exception;

public class ObjectNotFoundException extends ObjectPersistenceException{
    private static final long serialVersionUID = 1L;

    public ObjectNotFoundException(String message, String type) {
        super(message,type);
    }
}
