package com.example.remidiv3.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

// Import Volley yang dibutuhkan
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest; // GANTI JsonObjectRequest JADI StringRequest
import com.example.remidiv3.R;
import com.example.remidiv3.network.ApiService;
import com.example.remidiv3.utils.UserSession;
import com.example.remidiv3.ui.dosen.ProfileActivity;

// Import Material Design
import com.google.android.material.textfield.TextInputEditText;

// Import JSON dan Map
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private UserSession session;
    private static final String TAG = "LoginActivity";

    private TextInputEditText etNidn, etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new UserSession(this);

        etNidn = findViewById(R.id.etNidn);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            String username = "";
            String password = "";

            if (etNidn.getText() != null) username = etNidn.getText().toString().trim();
            if (etPassword.getText() != null) password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "NIDN dan password wajib diisi!", Toast.LENGTH_SHORT).show();
            } else {
                loginToServer(username, password);
            }
        });
    }

    private void loginToServer(final String username, final String password) {
        String url = "https://sobatlab.pbltifnganjuk.com/login_api.php";

        // PERBAIKAN: Gunakan StringRequest, BUKAN JsonObjectRequest
        // Ini agar data dikirim sebagai "Form Data" yang bisa dibaca $_POST oleh PHP
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d(TAG, "Response Mentah: " + response); // Cek apa balasan server

                    try {
                        // Ubah String response menjadi JSON Object
                        JSONObject jsonResponse = new JSONObject(response);

                        // Cek status
                        if (jsonResponse.optString("status").equals("success")) {

                            // Ambil data dari JSON
                            int idUser = jsonResponse.optInt("id_user");
                            String namaLengkap = jsonResponse.optString("nama_lengkap");
                            String role = jsonResponse.optString("role");

                            // Simpan sesi
                            session.setLoggedIn(true);
                            session.setUsername(username);
                            session.setIdUser(idUser);
                            session.setRole(role);

                            Toast.makeText(LoginActivity.this, "Login Berhasil!", Toast.LENGTH_SHORT).show();

                            // Pindah halaman
                            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            String message = jsonResponse.optString("message", "Login gagal");
                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "Error format JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e(TAG, "Volley Error: ", error);
                    Toast.makeText(LoginActivity.this, "Gagal koneksi server. Cek internet.", Toast.LENGTH_SHORT).show();
                }
        ) {
            // BAGIA TERPENTING: Mengirim data sebagai $_POST
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // Pastikan key "username" dan "password" sama dengan yang ada di file PHP ($username = $_POST['username'])
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        // Masukkan ke antrian request
        ApiService.getInstance(this).addToRequestQueue(request);
    }
}