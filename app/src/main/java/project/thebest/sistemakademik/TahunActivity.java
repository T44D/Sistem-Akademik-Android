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

import project.thebest.sistemakademik.adapter.TahunAdapter;
import project.thebest.sistemakademik.data.Tahun;
import project.thebest.sistemakademik.util.Server;

import static project.thebest.sistemakademik.DashboardActivity.TAG_NISN;

public class TahunActivity extends AppCompatActivity implements TahunAdapter.OnItemClickListener {
    private String url = Server.URL + "readTahun.php";

    String nisn;

    TextView tvKosong;

    BounceProgressBar progressBar;

    RecyclerView rvTahun;
    TahunAdapter tahunAdapter;
    ArrayList<Tahun> tahunList;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tahun);
        setTitle(Html.fromHtml("<font color='#e4f9ff'>Riwayat Presensi</font>"));
        progressBar = findViewById(R.id.progressBarTahun);
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
        tvKosong = findViewById(R.id.text_tahun_kosong);
        rvTahun = findViewById(R.id.rvTahun);

        nisn = getIntent().getStringExtra(TAG_NISN);

        tahunList = new ArrayList<>();

        rvTahun.setHasFixedSize(true);
        rvTahun.setLayoutManager(new LinearLayoutManager(TahunActivity.this));

        mRequestQueue = Volley.newRequestQueue(this);

        readData();

        showLoading(false);
    }

    private void readData() {
        mRequestQueue = Volley.newRequestQueue(this);
        JSONObject params = new JSONObject();
        try {
            params.put("nisn", nisn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("result");
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.getJSONObject(i);
                            String tahun = data.getString("tahun");

                            tahunList.add(new Tahun(tahun));
                        }
                        tvKosong.setVisibility(View.GONE);
                        rvTahun.setVisibility(View.VISIBLE);
                    } else {
                        tvKosong.setVisibility(View.VISIBLE);
                        rvTahun.setVisibility(View.GONE);
                    }
                    tahunAdapter = new TahunAdapter(TahunActivity.this, tahunList);
                    rvTahun.setAdapter(tahunAdapter);
                    tahunAdapter.setOnItemClickListener(TahunActivity.this);
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

    @Override
    public void onItemClick(int position) {
        Tahun clickedItem = tahunList.get(position);
        Intent intent = new Intent(TahunActivity.this, RiwayatPresensiActivity.class);
        intent.putExtra(TAG_NISN, nisn);
        intent.putExtra("tahun", clickedItem.getTahun());
        startActivity(intent);
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