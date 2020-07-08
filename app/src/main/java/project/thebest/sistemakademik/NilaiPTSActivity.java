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

import project.thebest.sistemakademik.adapter.NilaiPTSAdapter;
import project.thebest.sistemakademik.data.NilaiPTS;
import project.thebest.sistemakademik.util.Server;

import static project.thebest.sistemakademik.DashboardActivity.TAG_KELAS;
import static project.thebest.sistemakademik.DashboardActivity.TAG_NISN;

public class NilaiPTSActivity extends AppCompatActivity implements NilaiPTSAdapter.OnItemClickListener {
    private String url = Server.URL + "readNilaiPTS.php";

    String nisn, kelas;

    TextView tvKosong, np1, np2, np3, np4, np5, nk1, nk2, nk3, nk4, nk5, pts;

    BounceProgressBar progressBar;

    Spinner spinnerPTS;

    Dialog dialog;

    LayoutInflater inflater;
    View view;

    RecyclerView rvNilai;
    NilaiPTSAdapter nilaiAdapter;
    ArrayList<NilaiPTS> nilaiList;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nilai_p_t_s);
        setTitle(Html.fromHtml("<font color='#e4f9ff'>Daftar Nilai Harian</font>"));
        progressBar = findViewById(R.id.progressBarNilaiPTS);
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
            view = inflater.inflate(R.layout.dialog_nilaipts, new LinearLayout(NilaiPTSActivity.this), false);
        } catch (InflateException e) {
            e.printStackTrace();
        }
        dialog = new Dialog(NilaiPTSActivity.this);
        dialog.setContentView(view);


        tvKosong = findViewById(R.id.text_nilaipts_kosong);
        np1 = view.findViewById(R.id.nilai_np1);
        np2 = view.findViewById(R.id.nilai_np2);
        np3 = view.findViewById(R.id.nilai_np3);
        np4 = view.findViewById(R.id.nilai_np4);
        np5 = view.findViewById(R.id.nilai_np5);
        nk1 = view.findViewById(R.id.nilai_nk1);
        nk2 = view.findViewById(R.id.nilai_nk2);
        nk3 = view.findViewById(R.id.nilai_nk3);
        nk4 = view.findViewById(R.id.nilai_nk4);
        nk5 = view.findViewById(R.id.nilai_nk5);
        pts = view.findViewById(R.id.nilai_pts);
        rvNilai = findViewById(R.id.rvNilaiPTS);
        spinnerPTS = findViewById(R.id.spinnerPTS);

        nisn = getIntent().getStringExtra(TAG_NISN);
        kelas = getIntent().getStringExtra(TAG_KELAS);

        nilaiList = new ArrayList<>();

        final String[] value = {"1", "2"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, value);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPTS.setAdapter(adapter);

        rvNilai.setHasFixedSize(true);
        rvNilai.setLayoutManager(new LinearLayoutManager(NilaiPTSActivity.this));

        mRequestQueue = Volley.newRequestQueue(this);

        spinnerPTS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                            int np1 = data.getInt("np1");
                            int np2 = data.getInt("np2");
                            int np3 = data.getInt("np3");
                            int np4 = data.getInt("np4");
                            int np5 = data.getInt("np5");
                            int nk1 = data.getInt("nk1");
                            int nk2 = data.getInt("nk2");
                            int nk3 = data.getInt("nk3");
                            int nk4 = data.getInt("nk4");
                            int nk5 = data.getInt("nk5");
                            int pts = data.getInt("pts");
                            String mapel = data.getString("mapel");

                            nilaiList.add(new NilaiPTS(id, np1, np2, np3, np4, np5, nk1, nk2, nk3, nk4, nk5, pts, mapel));
                        }
                        tvKosong.setVisibility(View.GONE);
                        rvNilai.setVisibility(View.VISIBLE);
                    } else {
                        tvKosong.setVisibility(View.VISIBLE);
                        rvNilai.setVisibility(View.GONE);
                    }
                    nilaiAdapter = new NilaiPTSAdapter(NilaiPTSActivity.this, nilaiList);
                    rvNilai.setAdapter(nilaiAdapter);
                    nilaiAdapter.setOnItemClickListener(NilaiPTSActivity.this);
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
        NilaiPTS clickedItem = nilaiList.get(position);
        np1.setText(String.valueOf(clickedItem.getNp1()));
        np2.setText(String.valueOf(clickedItem.getNp2()));
        np3.setText(String.valueOf(clickedItem.getNp3()));
        np4.setText(String.valueOf(clickedItem.getNp4()));
        np5.setText(String.valueOf(clickedItem.getNp5()));
        nk1.setText(String.valueOf(clickedItem.getNk1()));
        nk2.setText(String.valueOf(clickedItem.getNk2()));
        nk3.setText(String.valueOf(clickedItem.getNk3()));
        nk4.setText(String.valueOf(clickedItem.getNk4()));
        nk5.setText(String.valueOf(clickedItem.getNk5()));
        pts.setText(String.valueOf(clickedItem.getPts()));
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
                Intent intent = new Intent(NilaiPTSActivity.this, KelasActivity.class);
                intent.putExtra(TAG_NISN, nisn);
                intent.putExtra("tipe", "PTS");
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