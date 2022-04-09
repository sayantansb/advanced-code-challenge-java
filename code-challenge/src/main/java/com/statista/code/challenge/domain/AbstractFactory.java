package com.statista.code.challenge.domain;

public interface AbstractFactory<T> {
    T create(String type) ;
}
