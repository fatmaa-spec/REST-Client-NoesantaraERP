
package com.example.neosantaraerp.model;

import java.util.Map;

public class KampanyePemasaran {

    private int id;
    private String nama_kampanye;
    private String jenis_kampanye;
    private String target_audiens;
    private double anggaran;
    private String tanggal_mulai;
    private String tanggal_selesai;
    private String status;
    private String deskripsi;

    public KampanyePemasaran(Map<String, Object> data) {
        id = getInt(data, "id");
        nama_kampanye = getString(data, "nama_kampanye");
        jenis_kampanye = getString(data, "jenis_kampanye");
        target_audiens = getString(data, "target_audiens");
        anggaran = getDouble(data, "anggaran");
        tanggal_mulai = getString(data, "tanggal_mulai");
        tanggal_selesai = getString(data, "tanggal_selesai");
        status = getString(data, "status");
        deskripsi = getString(data, "deskripsi");
    }

    private String getString(Map<String, Object> data, String key) {
        Object value = data.get(key);
        return value == null ? "" : value.toString();
    }

    private int getInt(Map<String, Object> data, String key) {
        Object value = data.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        try {
            return Integer.parseInt(String.valueOf(value));
        } catch (Exception e) {
            return 0;
        }
    }

    private double getDouble(Map<String, Object> data, String key) {
        Object value = data.get(key);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        try {
            return Double.parseDouble(String.valueOf(value));
        } catch (Exception e) {
            return 0;
        }
    }

    public int getId() {
        return id;
    }

    public String getNama_kampanye() {
        return nama_kampanye;
    }

    public String getJenis_kampanye() {
        return jenis_kampanye;
    }

    public String getTarget_audiens() {
        return target_audiens;
    }

    public double getAnggaran() {
        return anggaran;
    }

    public String getTanggal_mulai() {
        return tanggal_mulai;
    }

    public String getTanggal_selesai() {
        return tanggal_selesai;
    }

    public String getStatus() {
        return status;
    }

    public String getDeskripsi() {
        return deskripsi;
    }
}
