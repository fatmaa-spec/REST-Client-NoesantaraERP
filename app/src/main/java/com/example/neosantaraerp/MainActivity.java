
package com.example.neosantaraerp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.neosantaraerp.model.KampanyePemasaran;
import com.example.neosantaraerp.viewmodel.LoginViewModel;
import com.example.neosantaraerp.viewmodel.KampanyeViewModel;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // Warna tema pastel pink
    private final int PINK = Color.rgb(217, 130, 173);
    private final int PINK_DARK = Color.rgb(184, 95, 145);
    private final int BG = Color.rgb(255, 247, 251);
    private final int TEXT = Color.rgb(57, 45, 54);
    private final int MUTED = Color.rgb(138, 119, 131);
    private final int WHITE = Color.WHITE;

    private SessionManager session;
    private LoginViewModel loginViewModel;
    private KampanyeViewModel kampanyeViewModel;

    private LinearLayout root;
    private LinearLayout content;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new SessionManager(this);

        // Pengguna harus login setiap membuka aplikasi
        session.logout();

        loginViewModel = new ViewModelProvider(this)
                .get(LoginViewModel.class);

        kampanyeViewModel = new ViewModelProvider(this)
                .get(KampanyeViewModel.class);

        tampilkanHalaman();
    }

    // ==========================================
    // UTILITAS TAMPILAN
    // ==========================================

    private int dp(float value) {
        return (int) (value * getResources()
                .getDisplayMetrics().density);
    }

    private GradientDrawable shape(
            int color, int radius, int strokeColor) {

        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(dp(radius));

        if (strokeColor != 0) {
            drawable.setStroke(dp(1), strokeColor);
        }

        return drawable;
    }

    private TextView text(
            String value, int size, int color, boolean bold) {

        TextView tv = new TextView(this);
        tv.setText(value);
        tv.setTextSize(size);
        tv.setTextColor(color);

        if (bold) {
            tv.setTypeface(null, Typeface.BOLD);
        }

        return tv;
    }

    // ==========================================
    // NAVIGASI HALAMAN
    // ==========================================

    private void tampilkanHalaman() {

        token = session.getToken();

        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(BG);

        setContentView(root);

        if (token == null || token.isEmpty()) {
            halamanLogin();
        } else {
            halamanKampanye();
        }
    }

    private void buatHeader(String judul) {

        // HAPUS seluruh tampilan halaman sebelumnya
        root.removeAllViews();

        LinearLayout header = new LinearLayout(this);
        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setPadding(dp(22), dp(20), dp(22), dp(20));
        header.setBackgroundColor(PINK);

        TextView title = text(judul, 20, WHITE, true);

        header.addView(title,
                new LinearLayout.LayoutParams(0, -2, 1));

        Button logout = new Button(this);
        logout.setText("Logout");
        logout.setTextColor(WHITE);
        logout.setTextSize(13);
        logout.setAllCaps(false);
        logout.setBackgroundColor(Color.TRANSPARENT);

        logout.setOnClickListener(v -> {

            // Hapus token login
            session.logout();

            // Bersihkan hasil login sebelumnya
            loginViewModel.hasilLogin.setValue(null);

            // Tampilkan notifikasi logout berhasil
            Toast.makeText(
                    MainActivity.this,
                    "Logout berhasil!",
                    Toast.LENGTH_SHORT
            ).show();

            // Kembali ke halaman login
            tampilkanHalaman();
        });

        header.addView(logout);
        root.addView(header);
    }

    private void buatAreaKonten() {

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);

        content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(
                dp(20), dp(24), dp(20), dp(35)
        );

        scroll.addView(content);

        root.addView(scroll,
                new LinearLayout.LayoutParams(-1, 0, 1));
    }

    // ==========================================
    // HALAMAN LOGIN
    // ==========================================

    private void halamanLogin() {

        root.setGravity(Gravity.CENTER);
        root.setPadding(
                dp(24), dp(25), dp(24), dp(25)
        );

        TextView logo = text("♡", 58, PINK, true);
        logo.setGravity(Gravity.CENTER);
        root.addView(logo);

        TextView judul = text(
                "Marketing Campaign", 27, TEXT, true
        );

        judul.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams titleParams =
                new LinearLayout.LayoutParams(-1, -2);

        titleParams.bottomMargin = dp(12);
        root.addView(judul, titleParams);

        TextView subjudul = text(
                "NEOSANTARA ERP", 14, MUTED, false
        );

        subjudul.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams subParams =
                new LinearLayout.LayoutParams(-1, -2);

        subParams.bottomMargin = dp(40);
        root.addView(subjudul, subParams);

        EditText email = input("Email");
        email.setInputType(
                android.text.InputType.TYPE_CLASS_TEXT
                        | android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        );

        root.addView(email);

        EditText password = input("Password");
        password.setInputType(
                android.text.InputType.TYPE_CLASS_TEXT
                        | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        );

        LinearLayout.LayoutParams passParams =
                new LinearLayout.LayoutParams(-1, -2);

        passParams.topMargin = dp(18);
        root.addView(password, passParams);

        Button login = tombol("Login", PINK);

        LinearLayout.LayoutParams buttonParams =
                new LinearLayout.LayoutParams(-1, dp(55));

        buttonParams.topMargin = dp(28);
        root.addView(login, buttonParams);

        login.setOnClickListener(v -> {

            String emailValue =
                    email.getText().toString().trim();

            String passwordValue =
                    password.getText().toString();

            if (emailValue.isEmpty()
                    || passwordValue.isEmpty()) {

                Toast.makeText(
                        this,
                        "Email dan password wajib diisi",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            login.setEnabled(false);
            login.setText("Memproses...");

            loginViewModel.login(emailValue, passwordValue);
        });

        loginViewModel.hasilLogin.observe(this, hasil -> {

            if (hasil == null) return;

            login.setEnabled(true);
            login.setText("Login");

            if (hasil.startsWith("ERROR:")) {

                Toast.makeText(
                        this, hasil, Toast.LENGTH_LONG
                ).show();

            } else {

                session.saveToken(hasil);

                Toast.makeText(
                        this,
                        "Login berhasil!",
                        Toast.LENGTH_SHORT
                ).show();

                tampilkanHalaman();
            }
        });
    }

    // ==========================================
    // HALAMAN DAFTAR KAMPANYE
    // ==========================================

    private void halamanKampanye() {

        buatHeader("Marketing Campaign");
        buatAreaKonten();

        TextView judul = text(
                "Daftar Kampanye", 25, TEXT, true
        );

        content.addView(judul);

        TextView keterangan = text(
                "Kelola semua kampanye pemasaran Anda",
                14, MUTED, false
        );

        LinearLayout.LayoutParams infoParams =
                new LinearLayout.LayoutParams(-1, -2);

        infoParams.topMargin = dp(6);
        infoParams.bottomMargin = dp(22);

        content.addView(keterangan, infoParams);

        Button tambah = tombol("+  Tambah Kampanye", PINK);

        content.addView(tambah,
                new LinearLayout.LayoutParams(-1, dp(52)));

        tambah.setOnClickListener(v -> halamanTambah());

        TextView daftarJudul = text(
                "Kampanye Anda", 18, TEXT, true
        );

        LinearLayout.LayoutParams listParams =
                new LinearLayout.LayoutParams(-1, -2);

        listParams.topMargin = dp(26);
        listParams.bottomMargin = dp(14);

        content.addView(daftarJudul, listParams);

        LinearLayout daftar = new LinearLayout(this);
        daftar.setOrientation(LinearLayout.VERTICAL);

        content.addView(daftar);

        TextView loading = text(
                "Memuat data kampanye...",
                14, MUTED, false
        );

        daftar.addView(loading);

        kampanyeViewModel.daftarKampanye.observe(
                this, data -> {

                    daftar.removeAllViews();

                    if (data == null || data.isEmpty()) {

                        TextView kosong = text(
                                "Belum ada kampanye.",
                                15, MUTED, false
                        );

                        kosong.setGravity(Gravity.CENTER);
                        kosong.setPadding(
                                0, dp(30), 0, dp(30)
                        );

                        daftar.addView(kosong);
                        return;
                    }

                    for (KampanyePemasaran item : data) {
                        daftar.addView(buatCard(item));
                    }
                });

        kampanyeViewModel.pesan.observe(this, pesan -> {

            if (pesan != null) {
                Toast.makeText(
                        this, pesan, Toast.LENGTH_SHORT
                ).show();
            }
        });

        kampanyeViewModel.ambilData(token);
    }

    private View buatCard(KampanyePemasaran item) {

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(
                dp(18), dp(18), dp(18), dp(18)
        );

        card.setBackground(shape(
                WHITE, 18, Color.rgb(246, 225, 237)
        ));

        LinearLayout.LayoutParams cardParams =
                new LinearLayout.LayoutParams(-1, -2);

        cardParams.bottomMargin = dp(15);
        card.setLayoutParams(cardParams);

        TextView nama = text(
                item.getNama_kampanye(),
                18, TEXT, true
        );

        card.addView(nama);

        tambahInfo(card, "Kode: " + item.getId());

        tambahInfo(card,
                "Jenis: " + item.getJenis_kampanye());

        tambahInfo(card,
                "Target: " + item.getTarget_audiens());

        NumberFormat format =
                NumberFormat.getNumberInstance(
                        new Locale("id", "ID")
                );

        tambahInfo(card,
                "Anggaran: Rp " +
                        format.format(item.getAnggaran()));

        TextView status = text(
                "Status: " + item.getStatus(),
                13, PINK_DARK, true
        );

        status.setPadding(
                dp(10), dp(7), dp(10), dp(7)
        );

        status.setBackground(shape(
                Color.rgb(252, 234, 243), 10, 0
        ));

        LinearLayout.LayoutParams statusParams =
                new LinearLayout.LayoutParams(-2, -2);

        statusParams.topMargin = dp(12);
        card.addView(status, statusParams);

        card.setOnClickListener(v ->
                halamanDetail(item));

        return card;
    }

    private void tambahInfo(
            LinearLayout parent, String value) {

        TextView info = text(
                value, 14, MUTED, false
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(-1, -2);

        params.topMargin = dp(7);

        parent.addView(info, params);
    }

    // ==========================================
    // HALAMAN TAMBAH KAMPANYE
    // ==========================================

    private void halamanTambah() {

        buatHalamanForm("Tambah Kampanye");

        // Bersihkan hasil penyimpanan sebelumnya
        kampanyeViewModel.hasilTambah.removeObservers(this);
        kampanyeViewModel.hasilTambah.setValue(null);

        // Nama Kampanye
        EditText nama = tambahField("Nama Kampanye *");

        // Jenis Kampanye
        tambahLabel("Jenis Kampanye *");

        String[] pilihanJenis = {
                "Pilih Jenis Kampanye",
                "Media Sosial",
                "Email Marketing",
                "Iklan Digital",
                "Event atau Pameran",
                "Lainnya"
        };

        Spinner jenis = buatDropdown(pilihanJenis);

        // Target Audiens
        EditText target = tambahField("Target Audiens");
        target.setSingleLine(false);
        target.setMinLines(2);
        target.setGravity(Gravity.TOP);

        // Anggaran
        EditText anggaran = tambahField("Anggaran (Rp) *");

        anggaran.setInputType(
                android.text.InputType.TYPE_CLASS_NUMBER
                        | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
        );

        // Tanggal Mulai
        tambahLabel("Tanggal Mulai *");

        EditText mulai = buatInputTanggal();
        content.addView(mulai);

        mulai.setOnClickListener(v -> pilihTanggal(mulai));

        // Tanggal Selesai
        tambahLabel("Tanggal Selesai *");

        EditText selesai = buatInputTanggal();
        content.addView(selesai);

        selesai.setOnClickListener(v -> pilihTanggal(selesai));

        // Status Kampanye
        tambahLabel("Status Kampanye *");

        String[] pilihanStatus = {
                "Direncanakan",
                "Berjalan",
                "Selesai",
                "Dibatalkan"
        };

        Spinner status = buatDropdown(pilihanStatus);

        // Deskripsi Kampanye
        EditText deskripsi = tambahField("Deskripsi Kampanye");

        deskripsi.setSingleLine(false);
        deskripsi.setMinLines(3);
        deskripsi.setGravity(Gravity.TOP);

        // Tombol Simpan
        Button simpan = tombol("Simpan Kampanye", PINK);

        LinearLayout.LayoutParams simpanParams =
                new LinearLayout.LayoutParams(-1, dp(54));

        simpanParams.topMargin = dp(25);
        content.addView(simpan, simpanParams);

        simpan.setOnClickListener(v -> {

            String namaValue =
                    nama.getText().toString().trim();

            String targetValue =
                    target.getText().toString().trim();

            String anggaranValue =
                    anggaran.getText().toString().trim();

            String mulaiValue =
                    mulai.getText().toString().trim();

            String selesaiValue =
                    selesai.getText().toString().trim();

            String deskripsiValue =
                    deskripsi.getText().toString().trim();

            String jenisValue =
                    jenis.getSelectedItem().toString();

            String statusValue =
                    status.getSelectedItem().toString()
                            .toLowerCase(Locale.ROOT);
            // Validasi kolom wajib
            if (namaValue.isEmpty()
                    || jenis.getSelectedItemPosition() == 0
                    || anggaranValue.isEmpty()
                    || mulaiValue.isEmpty()
                    || selesaiValue.isEmpty()) {

                Toast.makeText(
                        this,
                        "Mohon lengkapi semua kolom wajib",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            // Validasi tanggal
            if (selesaiValue.compareTo(mulaiValue) < 0) {

                Toast.makeText(
                        this,
                        "Tanggal selesai tidak boleh sebelum tanggal mulai",
                        Toast.LENGTH_LONG
                ).show();

                return;
            }

            // Validasi anggaran
            double anggaranNumber;

            try {
                anggaranNumber = Double.parseDouble(
                        anggaranValue.replace(",", ".")
                );

                if (anggaranNumber < 0) {
                    throw new NumberFormatException();
                }

            } catch (NumberFormatException e) {

                Toast.makeText(
                        this,
                        "Anggaran tidak valid",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            // Siapkan data untuk Laravel
            Map<String, Object> data = new HashMap<>();

            data.put("nama_kampanye", namaValue);
            data.put("jenis_kampanye", jenisValue);
            data.put("target_audiens", targetValue);
            data.put("anggaran", anggaranNumber);
            data.put("tanggal_mulai", mulaiValue);
            data.put("tanggal_selesai", selesaiValue);
            data.put("status", statusValue);
            data.put("deskripsi", deskripsiValue);

            simpan.setEnabled(false);
            simpan.setText("Menyimpan...");

            kampanyeViewModel.tambahData(token, data);
        });

        // Pantau respons penyimpanan
        kampanyeViewModel.hasilTambah.observe(this, hasil -> {

            if (hasil == null) return;

            if ("BERHASIL".equals(hasil)) {

                Toast.makeText(
                        this,
                        "Kampanye berhasil ditambahkan!",
                        Toast.LENGTH_SHORT
                ).show();

                halamanKampanye();

            } else if (hasil.startsWith("ERROR:")) {

                simpan.setEnabled(true);
                simpan.setText("Simpan Kampanye");

                Toast.makeText(
                        this,
                        hasil,
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    // ==========================================
    // KOMPONEN FORM
    // ==========================================

    private void tambahLabel(String label) {

        TextView judul = text(
                label, 14, TEXT, true
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(-1, -2);

        params.topMargin = dp(15);
        params.bottomMargin = dp(7);

        content.addView(judul, params);
    }

    private Spinner buatDropdown(String[] pilihan) {

        Spinner spinner = new Spinner(this);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        pilihan
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinner.setAdapter(adapter);

        spinner.setPadding(
                dp(12), dp(5), dp(12), dp(5)
        );

        spinner.setBackground(shape(
                WHITE, 12, Color.rgb(235, 207, 222)
        ));

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(-1, dp(52));

        params.bottomMargin = dp(5);

        content.addView(spinner, params);

        return spinner;
    }

    private EditText buatInputTanggal() {

        EditText tanggal = input("Pilih tanggal");

        tanggal.setFocusable(false);
        tanggal.setClickable(true);
        tanggal.setTextSize(15);

        tanggal.setCompoundDrawablePadding(dp(8));

        tanggal.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                android.R.drawable.ic_menu_my_calendar,
                0
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(-1, dp(52));

        params.bottomMargin = dp(5);
        tanggal.setLayoutParams(params);

        return tanggal;
    }

    private void pilihTanggal(EditText field) {

        Calendar calendar = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {

                    String tanggal = String.format(
                            Locale.US,
                            "%04d-%02d-%02d",
                            year,
                            month + 1,
                            dayOfMonth
                    );

                    field.setText(tanggal);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        dialog.show();
    }

    private void buatHalamanForm(String judul) {

        buatHeader("Marketing Campaign");
        buatAreaKonten();

        TextView title = text(
                judul, 24, TEXT, true
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(-1, -2);

        params.bottomMargin = dp(25);
        content.addView(title, params);

        Button kembali = tombol(
                "← Kembali", PINK_DARK
        );

        LinearLayout.LayoutParams backParams =
                new LinearLayout.LayoutParams(-1, dp(45));

        backParams.bottomMargin = dp(20);
        content.addView(kembali, backParams);

        kembali.setOnClickListener(v ->
                halamanKampanye());
    }

    private EditText tambahField(String label) {

        tambahLabel(label);

        EditText field = input("Masukkan " + label);

        LinearLayout.LayoutParams fieldParams =
                new LinearLayout.LayoutParams(-1, -2);

        fieldParams.bottomMargin = dp(5);

        content.addView(field, fieldParams);

        return field;
    }

    // ==========================================
    // HALAMAN DETAIL KAMPANYE
    // ==========================================

    private void halamanDetail(KampanyePemasaran item) {

        buatHeader("Marketing Campaign");
        buatAreaKonten();

        TextView judul = text(
                "Detail Kampanye", 25, TEXT, true
        );

        content.addView(judul);

        tambahDetail(
                "Nama Kampanye",
                item.getNama_kampanye()
        );

        tambahDetail(
                "Kode Kampanye",
                String.valueOf(item.getId())
        );

        tambahDetail(
                "Jenis Kampanye",
                item.getJenis_kampanye()
        );

        tambahDetail(
                "Target Audiens",
                item.getTarget_audiens()
        );

        tambahDetail(
                "Deskripsi",
                item.getDeskripsi()
        );

        tambahDetail(
                "Tanggal Mulai",
                item.getTanggal_mulai()
        );

        tambahDetail(
                "Tanggal Selesai",
                item.getTanggal_selesai()
        );

        NumberFormat format =
                NumberFormat.getNumberInstance(
                        new Locale("id", "ID")
                );

        tambahDetail(
                "Anggaran",
                "Rp " + format.format(item.getAnggaran())
        );

        tambahDetail(
                "Status",
                item.getStatus()
        );

        Button kembali = tombol(
                "Kembali ke Daftar", PINK
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(-1, dp(52));

        params.topMargin = dp(25);

        content.addView(kembali, params);

        kembali.setOnClickListener(v ->
                halamanKampanye());
    }

    private void tambahDetail(
            String label, String value) {

        TextView nama = text(
                label, 14, PINK_DARK, true
        );

        LinearLayout.LayoutParams labelParams =
                new LinearLayout.LayoutParams(-1, -2);

        labelParams.topMargin = dp(20);

        content.addView(nama, labelParams);

        TextView isi = text(
                value == null ? "-" : value,
                16, TEXT, false
        );

        LinearLayout.LayoutParams valueParams =
                new LinearLayout.LayoutParams(-1, -2);

        valueParams.topMargin = dp(5);

        content.addView(isi, valueParams);
    }

    // ==========================================
    // INPUT DAN TOMBOL UMUM
    // ==========================================

    private EditText input(String hint) {

        EditText edit = new EditText(this);

        edit.setHint(hint);
        edit.setTextSize(15);
        edit.setSingleLine(true);
        edit.setTextColor(TEXT);
        edit.setHintTextColor(MUTED);

        edit.setPadding(
                dp(16), dp(12), dp(16), dp(12)
        );

        edit.setBackground(shape(
                WHITE, 12, Color.rgb(235, 207, 222)
        ));

        return edit;
    }

    private Button tombol(String label, int color) {

        Button button = new Button(this);

        button.setText(label);
        button.setTextColor(WHITE);
        button.setTextSize(15);
        button.setAllCaps(false);
        button.setTypeface(null, Typeface.BOLD);
        button.setBackground(shape(color, 16, 0));

        return button;
    }
}
