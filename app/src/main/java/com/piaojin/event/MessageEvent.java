package com.piaojin.event;

import com.piaojin.domain.Message;

/**
 * Created by piaojin on 2015/5/3.
 */
public class MessageEvent {

    private Message message;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public MessageEvent(Message message) {
        this.message = message;
    }
}
