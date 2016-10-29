package com.watwarrior.watchatbot.models;

import com.google.gson.annotations.Expose;

/**
 * Created by yifan on 16/10/29.
 */

public class TextChatMessage extends AbstractChatMessage {
    @Expose public String user;
    @Expose public String message;

    public TextChatMessage(String user, String message) {
        this.user = user;
        this.message = message;
    }
}
