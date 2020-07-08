package project.thebest.sistemakademik;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import project.thebest.sistemakademik.app.AppController;
import project.thebest.sistemakademik.util.Server;

import org.rocko.bpb.BounceProgressBar;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    EditText etNISN, etPassword;
    BounceProgressBar progressBar;

    int success;
    ConnectivityManager conMgr;

    private String url = Server.URL + "login.php";

    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    public final static String TAG_NISN = "nisn";
    public final static String TAG_NAMA = "nama";
    public final static String TAG_KELAS = "kelas";

    String tag_json_obj = "json_obj_req";

    SharedPreferences sharedpreferences;
    Boolean session = false;
    String nisn, nama, kelas;
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getApplicationContext(), "Tidak Ada Koneksi Internet !",
                        Toast.LENGTH_LONG).show();
            }
        }

        btnLogin = findViewById(R.id.buttonLogin);
        etNISN = findViewById(R.id.et_nis);
        etPassword = findViewById(R.id.et_katasandi);
        progressBar = findViewById(R.id.progressBarLogin);

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        nisn = sharedpreferences.getString(TAG_NISN, null);
        nama = sharedpreferences.getString(TAG_NAMA, null);
        kelas = sharedpreferences.getString(TAG_KELAS, null);

        if (session) {
            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            intent.putExtra(TAG_NISN, nisn);
            intent.putExtra(TAG_NAMA, nama);
            intent.putExtra(TAG_KELAS, kelas);
            finish();
            startActivity(intent);
        }


        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String nisn = etNISN.getText().toString();
                String password = etPassword.getText().toString();

                if (nisn.trim().length() > 0 && password.trim().length() > 0) {
                    if (conMgr.getActiveNetworkInfo() != null
                            && conMgr.getActiveNetworkInfo().isAvailable()
                            && conMgr.getActiveNetworkInfo().isConnected()) {
                        checkLogin(nisn, password);
                    } else {
                        Toast.makeText(getApplicationContext(), "Tidak Ada Koneksi Internet", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "NISN dan Kata Sandi Harus Diisi", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void checkLogin(final String nisn, final String password) {
        showLoading(true);

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Login Response: " + response);
                showLoading(false);

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        String nisn = jObj.getString(TAG_NISN);
                        String nama = jObj.getString(TAG_NAMA);
                        String kelas = jObj.getString(TAG_KELAS);

                        Log.e("Berhasil Login!", jObj.toString());

                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean(session_status, true);
                        editor.putString(TAG_NISN, nisn);
                        editor.putString(TAG_NAMA, nama);
                        editor.putString(TAG_KELAS, kelas);
                        editor.apply();

                        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                        intent.putExtra(TAG_NISN, nisn);
                        intent.putExtra(TAG_NAMA, nama);
                        intent.putExtra(TAG_KELAS, kelas);
                        finish();
                        startActivity(intent);
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
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                showLoading(false);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nisn", nisn);
                params.put("password", password);
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
}