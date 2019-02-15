package com.gz.mozixing.event;

public class UpdateEvent {
    private String mMsg;

    public UpdateEvent(String msg) {
        mMsg = msg;
    }

    public String getMsg() {
        return mMsg;
    }
}