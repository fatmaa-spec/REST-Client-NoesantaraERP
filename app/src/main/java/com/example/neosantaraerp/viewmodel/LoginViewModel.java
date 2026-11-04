package com.example.neosantaraerp.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.neosantaraerp.network.ApiClient;
import com.example.neosantaraerp.network.ApiService;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {

    public MutableLiveData<String> hasilLogin = new MutableLiveData<>();

    public void login(String email, String password) {
        ApiService api = ApiClient.getClient().create(ApiService.class);

        Map<String, String> body = new HashMap<>();
        body.put("email", email);
        body.put("password", password);

        api.login(body).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Object token = response.body().get("token");
                    if (token != null) {
                        hasilLogin.setValue(token.toString());
                    } else {
                        hasilLogin.setValue("ERROR: Token tidak ditemukan");
                    }
                } else {
                    hasilLogin.setValue("ERROR: Login gagal. Periksa email dan password.");
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                hasilLogin.setValue("ERROR: " + t.getMessage());
            }
        });
    }
}