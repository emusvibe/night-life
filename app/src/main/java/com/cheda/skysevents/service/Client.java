package com.cheda.skysevents.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Cheda on 2017-04-20.
 */

public class Client {

    //public static final String BASE_URL ="http://www.maccloudgroup.co.za";
    public static final String BASE_URL = "http://10.0.0.6";
    public static Retrofit retrofit = null;

    public static Retrofit getClient(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }



}
