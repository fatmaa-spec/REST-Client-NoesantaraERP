
package com.example.neosantaraerp.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.neosantaraerp.model.KampanyePemasaran;
import com.example.neosantaraerp.repository.KampanyeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KampanyeViewModel extends ViewModel {

    private final KampanyeRepository repository =
            new KampanyeRepository();

    public MutableLiveData<List<KampanyePemasaran>> daftarKampanye =
            new MutableLiveData<>();

    public MutableLiveData<String> pesan =
            new MutableLiveData<>();

    public MutableLiveData<Boolean> sedangMemuat =
            new MutableLiveData<>(false);

    // Hasil khusus untuk proses tambah kampanye
    public MutableLiveData<String> hasilTambah =
            new MutableLiveData<>();

    // Mengambil daftar kampanye
    public void ambilData(String token) {

        sedangMemuat.setValue(true);

        repository.ambilKampanye(token,
                new Callback<Map<String, Object>>() {

                    @Override
                    public void onResponse(
                            Call<Map<String, Object>> call,
                            Response<Map<String, Object>> response) {

                        sedangMemuat.setValue(false);

                        if (response.isSuccessful()
                                && response.body() != null) {

                            Object data = response.body().get("data");

                            List<KampanyePemasaran> daftar =
                                    new ArrayList<>();

                            if (data instanceof List<?>) {

                                for (Object item : (List<?>) data) {

                                    if (item instanceof Map<?, ?>) {

                                        @SuppressWarnings("unchecked")
                                        Map<String, Object> itemMap =
                                                (Map<String, Object>) item;

                                        daftar.add(
                                                new KampanyePemasaran(itemMap)
                                        );
                                    }
                                }
                            }

                            daftarKampanye.setValue(daftar);

                        } else {

                            String detailError = "Tidak ada detail error";

                            try {
                                if (response.errorBody() != null) {
                                    detailError = response.errorBody().string();
                                }
                            } catch (Exception e) {
                                detailError = e.getMessage();
                            }

                            pesan.setValue(
                                    "ERROR: Kode "
                                            + response.code()
                                            + " - "
                                            + detailError
                            );
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<Map<String, Object>> call,
                            Throwable t) {

                        sedangMemuat.setValue(false);
                        pesan.setValue("ERROR: " + t.getMessage());
                    }
                });
    }

    // Menambahkan kampanye ke Laravel

    public void tambahData(
            String token,
            Map<String, Object> data) {

        sedangMemuat.setValue(true);

        hasilTambah.setValue(null);

        repository.tambahKampanye(
                token,
                data,
                new Callback<Map<String, Object>>() {

                    @Override
                    public void onResponse(
                            Call<Map<String, Object>> call,
                            Response<Map<String, Object>> response) {

                        sedangMemuat.setValue(false);

                        if (response.isSuccessful()) {

                            hasilTambah.setValue("BERHASIL");

                            pesan.setValue(
                                    "Kampanye berhasil ditambahkan!"
                            );

                            ambilData(token);

                        } else {

                            String detailError = "Detail error tidak tersedia";

                            try {
                                if (response.errorBody() != null) {
                                    detailError =
                                            response.errorBody().string();
                                }
                            } catch (Exception e) {
                                detailError = e.getMessage();
                            }

                            String pesanError =
                                    "ERROR: Kode "
                                            + response.code()
                                            + " - "
                                            + detailError;

                            hasilTambah.setValue(pesanError);
                            pesan.setValue(pesanError);
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<Map<String, Object>> call,
                            Throwable t) {

                        sedangMemuat.setValue(false);

                        String pesanError =
                                "ERROR: " + t.getMessage();

                        hasilTambah.setValue(pesanError);
                        pesan.setValue(pesanError);
                    }
                });
    }

}
