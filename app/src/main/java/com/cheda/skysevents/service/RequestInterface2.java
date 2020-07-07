package com.cheda.skysevents.service;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RequestInterface2 {

    @GET("/cheda/sky2events.json")
    Call<JSONResponse> getJSON();
}
