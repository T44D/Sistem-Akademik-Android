package project.thebest.sistemakademik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.rocko.bpb.BounceProgressBar;

import java.util.HashMap;
import java.util.Map;

import project.thebest.sistemakademik.app.AppController;
import project.thebest.sistemakademik.util.Server;

import static project.thebest.sistemakademik.DashboardActivity.TAG_NISN;

public class SettingsActivity extends AppCompatActivity {
    Button btnChange;
    EditText etLama, etBaru;
    BounceProgressBar progressBar;

    String nisn;
    int success;

    private String url = Server.URL + "change_password.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";

    private static final String TAG = SettingsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(Html.fromHtml("<font color='#e4f9ff'>Ganti Kata Sandi</font>"));
        progressBar = findViewById(R.id.progressBarSetting);
        etLama = findViewById(R.id.et_sandi_lama);
        etBaru = findViewById(R.id.et_sandi_baru);
        btnChange = findViewById(R.id.buttonGanti);

        nisn = getIntent().getStringExtra(TAG_NISN);

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lama = etLama.getText().toString();
                String baru = etBaru.getText().toString();

                if (lama.trim().length() > 0 && baru.trim().length() > 0) {
                    if (isNetworkAvailable(SettingsActivity.this)) {
                        check(nisn, lama, baru);
                    } else {
                        Toast.makeText(getApplicationContext(), "Tidak Ada Koneksi Internet", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "NISN dan Kata Sandi Harus Diisi", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void check(final String nisn, final String lama, final String baru) {
        showLoading(true);
        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Response: " + response);
                showLoading(false);
                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        Log.e("Berhasil Diganti!", jObj.toString());

                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(SettingsActivity.this, DashboardActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                showLoading(false);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nisn", nisn);
                params.put("kata_sandi_lama", lama);
                params.put("kata_sandi_baru", baru);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}