package com.soccer.whosin.models;

/**
 * Created by Mario on 05/11/16.
 **/

public class ErrorMessage {

    private String mMessage;

    public ErrorMessage(String pMessage) {
        this.mMessage = pMessage;
    }

    public String getMessage() {
        return mMessage;
    }
}
