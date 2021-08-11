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
    //String host_ip = "http://192.168.137.252:8000/";
    String host_ip = "http://10.240.72.81:8000/";

    //report incident
    @Multipart
    @POST("reportIncident/")
    Call<RequestBody> reportIncident(
            @Part("type") RequestBody type,
            @Part("description") RequestBody description,
            @Part("address") RequestBody address,
            @Part("date") RequestBody date,
            @Part MultipartBody.Part image);

    //request certificate
    @Multipart
    @POST("requestCertificate/")
    Call<RequestBody> requestCertificate(
            @Part("name") RequestBody name,
            @Part("phone") RequestBody phone,
            @Part("address") RequestBody address,
            @Part("kebele_id") RequestBody kebele_id,
            @Part("date") RequestBody date,
            @Part MultipartBody.Part id_photo,
            @Part MultipartBody.Part photo);

    //report wanted criminal
    @Multipart
    @POST("reportCriminal/")
    Call<RequestBody> reportCriminal(
            @Part("criminal_id") RequestBody id,
            @Part("address") RequestBody address);


    //report missing person
    @Multipart
    @POST("reportMissing/")
    Call<RequestBody> reportMissing(
            @Part("name") RequestBody name,
            @Part("sex") RequestBody sex,
            @Part("age") RequestBody age,
            @Part("color") RequestBody color,
            @Part("height") RequestBody height,
            @Part("description") RequestBody description,
            @Part("address") RequestBody address,
            @Part("date") RequestBody date,
            @Part("lost_date") RequestBody lost_date,
            @Part("status") RequestBody status,
            @Part MultipartBody.Part image);
}
