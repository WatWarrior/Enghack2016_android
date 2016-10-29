package com.watwarrior.watchatbot.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by yifan on 16/10/29.
 */

public interface Api {
    @GET("buildings/{building_code}.{format}")
    Call<BuildingResponse> getBuildingInfo(
            @Path("building_code") String building_code,
            @Path("format") String format,
            @Query("key") String apiKey);
}
