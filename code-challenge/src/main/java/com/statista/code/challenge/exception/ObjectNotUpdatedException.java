package com.statista.code.challenge.exception;


public class ObjectNotUpdatedException  extends ObjectPersistenceException{
    private static final long serialVersionUID = 1L;

    public ObjectNotUpdatedException(String message,String type) {
        super(message,type);
    }


}
