package com.watwarrior.watchatbot.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by yifan on 16/10/29.
 */

public class BuildingSection {

    @Expose
    @SerializedName("section_name")
    private String[] section_name;

    @Expose
    @SerializedName("latitude")
    private float latitude;

    @Expose
    @SerializedName("longitude")
    private float longitude;

    public BuildingSection() {
    }

    public BuildingSection(String[] section_name, float latitude, float longitude) {
        this.section_name = section_name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String[] getSection_name() {
        return section_name;
    }

    public void setSection_name(String[] section_name) {
        this.section_name = section_name;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
}
