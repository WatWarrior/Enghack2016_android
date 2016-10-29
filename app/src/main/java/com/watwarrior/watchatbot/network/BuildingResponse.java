package com.watwarrior.watchatbot.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by yifan on 16/10/29.
 */

public class BuildingResponse {

    @SerializedName("building_id")
    @Expose
    private String building_id;

    @SerializedName("building_code")
    @Expose
    private String building_code;

    @SerializedName("alternate_names")
    @Expose
    private String[] alternate_names;

    @SerializedName("latitude")
    @Expose
    private float latitude;

    @SerializedName("longitude")
    @Expose
    private float longitude;

    @SerializedName("building_sections")
    @Expose
    private BuildingSection[] building_sections;

    public BuildingResponse() {
    }

    public BuildingResponse(String building_id, String building_code, String[] alternate_names, float latitude, float longitude, BuildingSection[] building_sections) {
        this.building_id = building_id;
        this.building_code = building_code;
        this.alternate_names = alternate_names;
        this.latitude = latitude;
        this.longitude = longitude;
        this.building_sections = building_sections;
    }

    public String getBuilding_id() {
        return building_id;
    }

    public void setBuilding_id(String building_id) {
        this.building_id = building_id;
    }

    public String getBuilding_code() {
        return building_code;
    }

    public void setBuilding_code(String building_code) {
        this.building_code = building_code;
    }

    public Object getAlternate_names() {
        return alternate_names;
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

    public Object getBuilding_sections() {
        return building_sections;
    }

    public void setAlternate_names(String[] alternate_names) {
        this.alternate_names = alternate_names;
    }

    public void setBuilding_sections(BuildingSection[] building_sections) {
        this.building_sections = building_sections;
    }
}
