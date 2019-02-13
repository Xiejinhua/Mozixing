package com.gz.mozixing.event;

public class SwitchEvent {
    private String mMsg;

    public SwitchEvent(String msg) {
        mMsg = msg;
    }

    public String getMsg() {
        return mMsg;
    }
}