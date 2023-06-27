package com.projet3.safertogether.services.impl;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String s) {

        super(s);
    }
}
