package com.watwarrior.watchatbot.network;

import com.google.gson.annotations.Expose;

/**
 * Created by yifan on 16/10/29.
 */

public class BuildingResponse {
    @Expose public String building_id;
    @Expose public String building_code;
    @Expose public float latitude;
    @Expose public float longitude;
}
