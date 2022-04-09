package com.statista.code.challenge.exception;


public class ObjectPersistenceException  extends RuntimeException{
    private static final long serialVersionUID = 1L;
    private String type;

    public ObjectPersistenceException(String message,String type) {
        super(message);
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
