package project.thebest.sistemakademik;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import project.thebest.sistemakademik.adapter.InformationAdapter;
import project.thebest.sistemakademik.adapter.NilaiPASAdapter;
import project.thebest.sistemakademik.adapter.PresensiAdapter;
import project.thebest.sistemakademik.data.Information;
import project.thebest.sistemakademik.data.NilaiPAS;
import project.thebest.sistemakademik.data.Presensi;
import project.thebest.sistemakademik.data.Tahun;
import project.thebest.sistemakademik.util.Server;

import static project.thebest.sistemakademik.DashboardActivity.TAG_KELAS;
import static project.thebest.sistemakademik.DashboardActivity.TAG_NISN;

public class PresensiActivity extends AppCompatActivity {
    private String url = Server.URL + "readPresensi.php";

    String nisn;

    TextView tvKosong;

    BounceProgressBar progressBar;

    Spinner spinnerPresensi;

    RecyclerView rvPresensi;
    PresensiAdapter presensiAdapter;
    ArrayList<Presensi> presensiList;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presensi);
        setTitle(Html.fromHtml("<font color='#e4f9ff'>Daftar Presensi</font>"));
        progressBar = findViewById(R.id.progressBarPresensi);
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
        tvKosong = findViewById(R.id.text_presensi_kosong);
        rvPresensi = findViewById(R.id.rvPresensi);
        spinnerPresensi = findViewById(R.id.spinnerPresensi);

        nisn = getIntent().getStringExtra(TAG_NISN);

        presensiList = new ArrayList<>();

        final String[] value = {"1", "2"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, value);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPresensi.setAdapter(adapter);

        rvPresensi.setHasFixedSize(true);
        rvPresensi.setLayoutManager(new LinearLayoutManager(PresensiActivity.this));

        mRequestQueue = Volley.newRequestQueue(this);

        spinnerPresensi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                readData(value[i]);
                presensiList.clear();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        showLoading(false);
    }

    private void readData(String semester) {
        int semesterInt = Integer.parseInt(semester);
        mRequestQueue = Volley.newRequestQueue(this);
        JSONObject params = new JSONObject();
        try {
            params.put("nisn", nisn);
            params.put("semester", semesterInt);
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
                            int id = data.getInt("id");
                            String nama_hari = data.getString("nama_hari");
                            String jam = data.getString("jam");
                            String mapel = data.getString("mapel");
                            String kelas = data.getString("kelas");
                            String keterangan = data.getString("keterangan");
                            String tanggal = data.getString("tanggal");

                            presensiList.add(new Presensi(id,nama_hari, jam, mapel, kelas, keterangan, tanggal));
                        }
                        tvKosong.setVisibility(View.GONE);
                        rvPresensi.setVisibility(View.VISIBLE);
                    } else {
                        tvKosong.setVisibility(View.VISIBLE);
                        rvPresensi.setVisibility(View.GONE);
                    }
                    presensiAdapter = new PresensiAdapter(PresensiActivity.this, presensiList);
                    rvPresensi.setAdapter(presensiAdapter);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.history:
                Intent intent = new Intent(PresensiActivity.this, TahunActivity.class);
                intent.putExtra(TAG_NISN, nisn);
                startActivity(intent);
                break;
            default:
                break;
        }
        return true;
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