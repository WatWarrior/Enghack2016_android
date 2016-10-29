package com.watwarrior.watchatbot.network;

import android.content.Context;

import com.google.gson.GsonBuilder;
import com.watwarrior.watchatbot.R;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yifan on 16/10/29.
 */

public class NetworkHelper {

    private static final String TAG = NetworkHelper.class.getSimpleName();
    public static final String FORMAT_JSON = "json";
    public static final String API_URL = "https://api.uwaterloo.ca/v2/";

    private final Retrofit mRetrofit;
    private final Api mApi;
    private final Context mContext;

    public NetworkHelper(Context context) {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();
        mApi = mRetrofit.create(Api.class);
        mContext = context;
    }

    public BuildingResponse getBuildingInfo(String building_code){
        Call<BuildingResponse> buildingCall = mApi.getBuildingInfo(building_code, FORMAT_JSON,
                mContext.getString(R.string.uwApiKey));

        BuildingResponse buildingResponse= null;

        try{
            buildingResponse = buildingCall.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return buildingResponse;
    }

}
