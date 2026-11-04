
package com.example.neosantaraerp;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private final SharedPreferences preferences;

    public SessionManager(Context context) {
        preferences = context.getSharedPreferences(
                "NeosantaraSession",
                Context.MODE_PRIVATE
        );
    }

    // Menyimpan token login
    public void saveToken(String token) {
        preferences.edit()
                .putString("token", token)
                .apply();
    }

    // Mengambil token login
    public String getToken() {
        return preferences.getString("token", null);
    }

    // Memeriksa apakah pengguna sudah login
    public boolean isLoggedIn() {
        return getToken() != null && !getToken().isEmpty();
    }

    // Menghapus token saat logout
    public void logout() {
        preferences.edit()
                .clear()
                .apply();
    }
}
