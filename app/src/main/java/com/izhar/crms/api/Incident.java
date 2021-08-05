package com.izhar.crms.api;

import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Incident {

    String DJANGO_SITE = "http://192.168.0.112:8000/image/";

    @Multipart
    @POST("incidentReport/")
    Call<RequestBody> uploadFile(@Part MultipartBody.Part file, JSONObject json);
}
