package com.statista.code.challenge.exception;

public class ObjectNotPersistedException extends ObjectPersistenceException{
    private static final long serialVersionUID = 1L;

    public ObjectNotPersistedException(String message,String type) {
        super(message,type);
    }
}
