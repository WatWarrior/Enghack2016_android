package com.watwarrior.watchatbot.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by yifan on 16/10/29.
 */

public class MyResponse {

    @Expose
    @SerializedName("data")
    public BuildingResponse mBuildingResponse;

    public MyResponse(BuildingResponse buildingResponse) {
        mBuildingResponse = buildingResponse;
    }
}
