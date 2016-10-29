package com.watwarrior.watchatbot.models;

import com.google.gson.annotations.Expose;

/**
 * Created by yifan on 16/10/29.
 */

public class MapChatMessage extends AbstractChatMessage {
    @Expose public String user;
    @Expose public float latitude;
    @Expose public float longitude;

    public MapChatMessage(String user, float latitude, float longitude) {
        this.user = user;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
