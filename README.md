\# REST Client Android - NeosantaraERP



Aplikasi REST Client berbasis Android untuk mengelola data kampanye pemasaran pada sistem NeosantaraERP.



\## Deskripsi

Aplikasi ini dibuat menggunakan Java di Android Studio dan terhubung dengan REST API Laravel menggunakan Retrofit.



\## Fitur Aplikasi

\- Login menggunakan email dan password.

\- Menyimpan token autentikasi.

\- Menampilkan daftar kampanye pemasaran.

\- Menambahkan kampanye pemasaran.

\- Melihat detail kampanye.

\- Logout dari aplikasi.

\- Validasi data sebelum dikirim ke server.



\## Teknologi yang Digunakan

\- Java

\- Android Studio

\- Retrofit

\- Gson

\- LiveData

\- ViewModel

\- Repository Pattern

\- Laravel REST API

\- Laravel Sanctum



\## Struktur Aplikasi

\- `MainActivity.java` — Mengatur tampilan dan navigasi aplikasi.

\- `SessionManager.java` — Mengelola token login.

\- `ApiClient.java` — Konfigurasi koneksi REST API.

\- `ApiService.java` — Mendefinisikan layanan API.

\- `KampanyePemasaran.java` — Model data kampanye.

\- `KampanyeRepository.java` — Mengelola komunikasi data.

\- `KampanyeViewModel.java` — Mengelola data kampanye.

\- `LoginViewModel.java` — Mengelola proses login.



\## Cara Menjalankan

1\. Clone repository ini.

2\. Buka project menggunakan Android Studio.

3\. Tunggu proses Gradle Sync selesai.

4\. Pastikan REST Server Laravel sudah berjalan.

5\. Sesuaikan alamat server pada `ApiClient.java`.

6\. Jalankan aplikasi menggunakan emulator atau perangkat Android.

7\. Login menggunakan akun yang terdaftar pada REST Server.



\## Repository

REST Client: https://github.com/fatmaa-spec/REST-Client-NoesantaraERP



REST Server: \[Tambahkan link repository REST Server di sini]



\---

Project Ujian Tengah Semester Program Mobile Lanjutan.

