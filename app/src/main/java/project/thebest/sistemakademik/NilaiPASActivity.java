package project.thebest.sistemakademik;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

import project.thebest.sistemakademik.adapter.NilaiPASAdapter;
import project.thebest.sistemakademik.data.NilaiPAS;
import project.thebest.sistemakademik.util.Server;

import static project.thebest.sistemakademik.DashboardActivity.TAG_KELAS;
import static project.thebest.sistemakademik.DashboardActivity.TAG_NISN;

public class NilaiPASActivity extends AppCompatActivity implements NilaiPASAdapter.OnItemClickListener {
    private String url = Server.URL + "readNilaiPAS.php";

    String nisn, kelas;

    TextView tvKosong, np, npt, prednp, prednpt;

    Spinner spinnerPAS;

    BounceProgressBar progressBar;

    Dialog dialog;

    LayoutInflater inflater;
    View view;

    RecyclerView rvNilai;
    NilaiPASAdapter nilaiAdapter;
    ArrayList<NilaiPAS> nilaiList;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nilai_p_a_s);
        setTitle(Html.fromHtml("<font color='#e4f9ff'>Daftar Nilai Akhir</font>"));
        progressBar = findViewById(R.id.progressBarNilaiPAS);
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
        inflater = this.getLayoutInflater();
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        try {
            view = inflater.inflate(R.layout.dialog_nilaipas, new LinearLayout(NilaiPASActivity.this), false);
        } catch (InflateException e) {
            e.printStackTrace();
        }
        dialog = new Dialog(NilaiPASActivity.this);
        dialog.setContentView(view);

        np = view.findViewById(R.id.nilai_np);
        prednp = view.findViewById(R.id.pred_nilai_np);
        npt = view.findViewById(R.id.nilai_npt);
        prednpt = view.findViewById(R.id.pred_nilai_npt);
        tvKosong = findViewById(R.id.text_nilaipas_kosong);
        rvNilai = findViewById(R.id.rvNilaiPAS);
        spinnerPAS = findViewById(R.id.spinnerPAS);

        nisn = getIntent().getStringExtra(TAG_NISN);
        kelas = getIntent().getStringExtra(TAG_KELAS);

        nilaiList = new ArrayList<>();

        final String[] value = {"1", "2"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, value);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPAS.setAdapter(adapter);

        rvNilai.setHasFixedSize(true);
        rvNilai.setLayoutManager(new LinearLayoutManager(NilaiPASActivity.this));

        mRequestQueue = Volley.newRequestQueue(this);

        spinnerPAS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                readData(value[i]);
                nilaiList.clear();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        showLoading(false);
    }

//    private void readData() {
//        mRequestQueue = Volley.newRequestQueue(this);
//        JSONObject params = new JSONObject();
//        try {
//            params.put("nisn", nisn);
//            params.put("kode_kelas", kelas);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    JSONArray jsonArray = response.getJSONArray("result");
//                    if (jsonArray.length() > 0) {
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject data = jsonArray.getJSONObject(i);
//                            int id = data.getInt("id");
//                            int np = data.getInt("np");
//                            String pred_np = data.getString("predikat_np");
//                            int npt = data.getInt("npt");
//                            String pred_npt = data.getString("predikat_npt");
//                            String mapel = data.getString("mapel");
//
//                            nilaiList.add(new NilaiPAS(id, np, pred_np, npt, pred_npt, mapel));
//                        }
//                        tvKosong.setVisibility(View.GONE);
//                        rvNilai.setVisibility(View.VISIBLE);
//                    } else {
//                        tvKosong.setVisibility(View.VISIBLE);
//                        rvNilai.setVisibility(View.GONE);
//                    }
//                    nilaiAdapter = new NilaiPASAdapter(NilaiPASActivity.this, nilaiList);
//                    rvNilai.setAdapter(nilaiAdapter);
//                    nilaiAdapter.setOnItemClickListener(NilaiPASActivity.this);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("Ini Error ", error.getMessage());
//                error.printStackTrace();
//            }
//        });
//        mRequestQueue.add(request);
//    }

    private void readData(String semester) {
        int semesterInt = Integer.parseInt(semester);
        mRequestQueue = Volley.newRequestQueue(this);
        JSONObject params = new JSONObject();
        try {
            params.put("nisn", nisn);
            params.put("kode_kelas", kelas);
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
                            int np = data.getInt("np");
                            String pred_np = data.getString("predikat_np");
                            int npt = data.getInt("npt");
                            String pred_npt = data.getString("predikat_npt");
                            String mapel = data.getString("mapel");

                            nilaiList.add(new NilaiPAS(id, np, pred_np, npt, pred_npt, mapel));
                        }
                        tvKosong.setVisibility(View.GONE);
                        rvNilai.setVisibility(View.VISIBLE);
                    } else {
                        tvKosong.setVisibility(View.VISIBLE);
                        rvNilai.setVisibility(View.GONE);
                    }
                    nilaiAdapter = new NilaiPASAdapter(NilaiPASActivity.this, nilaiList);
                    rvNilai.setAdapter(nilaiAdapter);
                    nilaiAdapter.setOnItemClickListener(NilaiPASActivity.this);
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
        NilaiPAS clickedItem = nilaiList.get(position);
        np.setText(String.valueOf(clickedItem.getNp()));
        prednp.setText(clickedItem.getNp_predikat());
        npt.setText(String.valueOf(clickedItem.getNpt()));
        prednpt.setText(clickedItem.getNpt_predikat());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
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
                Intent intent = new Intent(NilaiPASActivity.this, KelasActivity.class);
                intent.putExtra(TAG_NISN, nisn);
                intent.putExtra("tipe", "PAS");
                startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}