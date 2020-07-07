package com.cheda.skysevents.service;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RequestInterface {

    @GET("/cheda/skyevents.json")
    Call<JSONResponse> getJSON();
}
