package com.example.neosantaraerp.repository;

import com.example.neosantaraerp.network.ApiClient;
import com.example.neosantaraerp.network.ApiService;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KampanyeRepository {

    private final ApiService api;

    public KampanyeRepository() {
        api = ApiClient.getClient().create(ApiService.class);
    }

    public void ambilKampanye(
            String token,
            Callback<Map<String, Object>> callback
    ) {
        api.getKampanye("Bearer " + token).enqueue(callback);
    }

    public void tambahKampanye(
            String token,
            Map<String, Object> data,
            Callback<Map<String, Object>> callback
    ) {
        api.tambahKampanye("Bearer " + token, data).enqueue(callback);
    }
}