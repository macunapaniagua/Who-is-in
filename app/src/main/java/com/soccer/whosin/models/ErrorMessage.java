package com.soccer.whosin.models;

/**
 * Created by Mario on 05/11/16.
 **/

public class ErrorMessage {

    private String error;

    public ErrorMessage(String pMessage) {
        this.error = pMessage;
    }

    public String getMessage() {
        return error;
    }
}
