package project.thebest.sistemakademik;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.rocko.bpb.BounceProgressBar;

import java.util.ArrayList;

import project.thebest.sistemakademik.adapter.KelasAdapter;
import project.thebest.sistemakademik.data.Kelas;
import project.thebest.sistemakademik.util.Server;

import static project.thebest.sistemakademik.DashboardActivity.TAG_NISN;

public class KelasActivity extends AppCompatActivity implements KelasAdapter.OnItemClickListener{
    private String urlPAS = Server.URL + "readKelasPAS.php";
    private String urlPTS = Server.URL + "readKelasPTS.php";

    String nisn, tipe;

    TextView tvKosong;

    BounceProgressBar progressBar;

    RecyclerView rvKelas;
    KelasAdapter kelasAdapter;
    ArrayList<Kelas> kelasList;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelas);
        setTitle(Html.fromHtml("<font color='#e4f9ff'>Riwayat Nilai</font>"));
        progressBar = findViewById(R.id.progressBarKelas);
        showLoading(true);
        if (isNetworkAvailable(this)) {
            init();
        } else {
            showLoading(false);
            Toast.makeText(getApplicationContext(), "Tidak Ada Koneksi Internet !",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void init() {
        tvKosong = findViewById(R.id.text_kelas_kosong);
        rvKelas = findViewById(R.id.rvKelas);

        nisn = getIntent().getStringExtra(TAG_NISN);
        tipe = getIntent().getStringExtra("tipe");

        kelasList = new ArrayList<>();

        rvKelas.setHasFixedSize(true);
        rvKelas.setLayoutManager(new LinearLayoutManager(KelasActivity.this));

        mRequestQueue = Volley.newRequestQueue(this);

        if (tipe.equalsIgnoreCase("PTS")) {
            readDataPTS();
        } else if (tipe.equalsIgnoreCase("PAS")) {
            readDataPAS();
        } else {
            Toast.makeText(getApplicationContext(), "Error",
                    Toast.LENGTH_LONG).show();
        }

        showLoading(false);
    }

    private void readDataPTS() {
        Log.d("Tipe: ", "PTS");
        mRequestQueue = Volley.newRequestQueue(this);
        JSONObject params = new JSONObject();
        try {
            params.put("nisn", nisn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, urlPTS, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("result");
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.getJSONObject(i);
                            String kelas = data.getString("kelas");

                            kelasList.add(new Kelas(kelas));
                        }
                        tvKosong.setVisibility(View.GONE);
                        rvKelas.setVisibility(View.VISIBLE);
                    } else {
                        tvKosong.setVisibility(View.VISIBLE);
                        rvKelas.setVisibility(View.GONE);
                    }
                    kelasAdapter = new KelasAdapter(KelasActivity.this, kelasList);
                    rvKelas.setAdapter(kelasAdapter);
                    kelasAdapter.setOnItemClickListener(KelasActivity.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Ini Error ", error.getMessage());
                error.printStackTrace();
            }
        });
        mRequestQueue.add(request);
    }

    private void readDataPAS() {
        Log.d("Tipe: ", "PAS");
        mRequestQueue = Volley.newRequestQueue(this);
        JSONObject params = new JSONObject();
        try {
            params.put("nisn", nisn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, urlPAS, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("result");
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.getJSONObject(i);
                            String kelas = data.getString("kelas");

                            kelasList.add(new Kelas(kelas));
                        }
                        tvKosong.setVisibility(View.GONE);
                        rvKelas.setVisibility(View.VISIBLE);
                    } else {
                        tvKosong.setVisibility(View.VISIBLE);
                        rvKelas.setVisibility(View.GONE);
                    }
                    kelasAdapter = new KelasAdapter(KelasActivity.this, kelasList);
                    rvKelas.setAdapter(kelasAdapter);
                    kelasAdapter.setOnItemClickListener(KelasActivity.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Ini Error ", error.getMessage());
                error.printStackTrace();
            }
        });
        mRequestQueue.add(request);
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(int position) {
        Kelas clickedItem = kelasList.get(position);
        Intent intent = new Intent(KelasActivity.this, NilaiActivity.class);
        intent.putExtra(TAG_NISN, nisn);
        intent.putExtra("kode_kelas", clickedItem.getKelas());
        intent.putExtra("tipe", tipe);
        startActivity(intent);
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}