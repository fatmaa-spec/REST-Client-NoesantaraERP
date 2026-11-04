package com.example.neosantaraerp.network;


import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {

    @POST("api/login")
    Call<Map<String, Object>> login(
            @Body Map<String, String> body
    );

    @POST("api/logout")
    Call<Map<String, Object>> logout(
            @Header("Authorization") String token
    );

    @GET("api/kampanye-pemasaran")
    Call<Map<String, Object>> getKampanye(
            @Header("Authorization") String token
    );

    @POST("api/kampanye-pemasaran")
    Call<Map<String, Object>> tambahKampanye(
            @Header("Authorization") String token,
            @Body Map<String, Object> body
    );
}
