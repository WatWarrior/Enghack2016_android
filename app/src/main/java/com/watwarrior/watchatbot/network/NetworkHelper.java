package com.watwarrior.watchatbot.network;

import android.content.Context;
import android.util.Log;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
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
        mContext = context;
        ExclusionStrategy metaExclusionStrategy = new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return f.getName().equals("meta");
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        };

        Gson gson = new GsonBuilder()
                .addDeserializationExclusionStrategy(metaExclusionStrategy)
                .create();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        mApi = mRetrofit.create(Api.class);
    }

    public BuildingResponse getBuildingInfo(String building_code){
        Call<MyResponse> buildingCall = mApi.getBuildingInfo(building_code, FORMAT_JSON,
                mContext.getString(R.string.uwApiKey));

        BuildingResponse buildingResponse= null;

        try{
            Response<MyResponse> response = buildingCall.execute();
            buildingResponse = response.body().mBuildingResponse;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return buildingResponse;
    }

}
