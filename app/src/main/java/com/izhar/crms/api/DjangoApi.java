package com.izhar.crms.api;


import com.google.gson.JsonObject;

import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface DjangoApi {
    String boundary = "*****";
    String DJANGO_SITE = "http://192.168.137.252:8000/";

    @Multipart
    @POST("up/")
    //@Headers({"Accept: application/json", "Content-Type: image/*"})
    Call<RequestBody> uploadFile(
            @Part MultipartBody.Part file);
    //Call<String> uploadImage(@Part MultipartBody.Part file, @Part("filename") RequestBody name);

}
