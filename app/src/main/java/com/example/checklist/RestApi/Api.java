package com.example.checklist.RestApi;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {

    @GET("api/users?page=2")
    Call<Model> getPhotos();

}