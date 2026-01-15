package com.example.remidiv3.ui.dosen;

import android.content.Intent;
import android.os.Bundle;
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

public class ProfileActivity extends AppCompatActivity {

    private UserSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        session = new UserSession(this);

        findViewById(R.id.btnEdit).setOnClickListener(v -> {
            startActivity(new Intent(this, EditProfileActivity.class));
        });

        findViewById(R.id.btnPeminjaman).setOnClickListener(v -> {
            startActivity(new Intent(this, RiwayatPeminjamanActivity.class));
        });

        muatProfil();
    }

    private void muatProfil() {
        String url = "https://sobatlab.pbltifnganjuk.com/api/profil.php";
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", session.getUsername());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                response -> {
                    try {
                        if (response.getString("status").equals("success")) {
                            JSONObject data = response.getJSONObject("data");
                            // Set text view sesuai data
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Gagal memuat profil", Toast.LENGTH_SHORT).show()
        );

        ApiService.getInstance(this).addToRequestQueue(request);
    }
}
