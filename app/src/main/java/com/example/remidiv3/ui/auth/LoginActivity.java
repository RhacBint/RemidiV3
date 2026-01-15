package com.example.remidiv3.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.remidiv3.R;
import com.example.remidiv3.network.ApiService;
import com.example.remidiv3.utils.UserSession;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private UserSession session;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new UserSession(this);

        findViewById(R.id.btnLogin).setOnClickListener(v -> {
            String username = ((android.widget.EditText) findViewById(R.id.etNidn)).getText().toString().trim();
            String password = ((android.widget.EditText) findViewById(R.id.etPassword)).getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "NIDN dan password wajib diisi!", Toast.LENGTH_SHORT).show();
                return;
            }

            loginToServer(username, password);
        });
    }

    private void loginToServer(String username, String password) {
        String url = "https://sobatlab.pbltifnganjuk.com/login_api.php"; // GANTI DENGAN DOMAIN ANDA

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", username);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                response -> {
                    try {
                        if (response.getString("status").equals("success")) {
                            int idUser = response.getInt("id_user");
                            String namaLengkap = response.getString("nama_lengkap");
                            String role = response.getString("role");

                            session.setLoggedIn(true);
                            session.setUsername(username);
                            session.setIdUser(idUser);
                            session.setRole(role);

                            if ("dosen".equals(role)) {
                                startActivity(new Intent(LoginActivity.this, com.example.remidiv3.ui.dosen.ProfileActivity.class));
                            } else if ("admin".equals(role)) {
                                startActivity(new Intent(LoginActivity.this, com.example.remidiv3.ui.dosen.ProfileActivity.class));
                            }
                            finish();
                        } else {
                            Toast.makeText(this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e(TAG, "Login Error: ", error);
                    Toast.makeText(this, "Gagal koneksi ke server", Toast.LENGTH_SHORT).show();
                }
        );

        ApiService.getInstance(this).addToRequestQueue(request);
    }
}
