package com.vkr.analytics_service.exception;

public class LobbyNotFoundException extends RuntimeException{
    public LobbyNotFoundException(String message) {
        super(message);
    }
}
