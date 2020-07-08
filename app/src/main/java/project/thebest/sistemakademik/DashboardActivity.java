package project.thebest.sistemakademik;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import project.thebest.sistemakademik.adapter.InformationAdapter;
import project.thebest.sistemakademik.data.Information;
import project.thebest.sistemakademik.util.Server;

public class DashboardActivity extends MainActivity implements InformationAdapter.OnItemClickListener {
    private String url = Server.URL + "readBerita.php";

    public final static String TAG_NISN = "nisn";
    public final static String TAG_NAMA = "nama";
    public final static String TAG_KELAS = "kelas";

    public static final String EXTRA_JUDUL = "judul";
    public static final String EXTRA_DESKRIPSI = "deskripsi";
    public static final String EXTRA_MEDIA = "media";
    public static final String EXTRA_MEDIA_TIPE = "tipe";
    public static final String EXTRA_TANGGAL = "tanggal";

    ConstraintLayout containerProfile, containerMenu, containerPesan;
    CardView menuNilaiPTS, menuNilaiPAS, menuPresensi;
    Button btnLogout;
    TextView tvNISN, tvNama, tvKelas, tvPesan, tvInformasi, tvInformasiKosong;
    ImageView imageSetting, imageInfo;
    BounceProgressBar progressBar;
    String nama, nisn, kelas;
    SharedPreferences sharedpreferences;

    RecyclerView rvInfo;
    RecyclerView.LayoutManager rvLayoutManager;
    LinearLayoutManager horizontalLayout;
    InformationAdapter mInfoAdapter;
    ArrayList<Information> informationList;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        progressBar = findViewById(R.id.progressBarDashboard);
        showLoading(true);
        if (isNetworkAvailable(this)) {
            init();
            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean(LoginActivity.session_status, false);
                    editor.putString(TAG_NISN, null);
                    editor.putString(TAG_NAMA, null);
                    editor.putString(TAG_KELAS, null);
                    editor.apply();

                    Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                    finish();
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Logout Berhasil!",
                            Toast.LENGTH_LONG).show();
                }
            });

            menuNilaiPAS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(DashboardActivity.this, NilaiPASActivity.class);
                    intent.putExtra(TAG_NISN, nisn);
                    intent.putExtra(TAG_KELAS, kelas);
                    startActivity(intent);
                }
            });

            menuNilaiPTS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(DashboardActivity.this, NilaiPTSActivity.class);
                    intent.putExtra(TAG_NISN, nisn);
                    intent.putExtra(TAG_KELAS, kelas);
                    startActivity(intent);
                }
            });

            menuPresensi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(DashboardActivity.this, PresensiActivity.class);
                    intent.putExtra(TAG_NISN, nisn);
                    startActivity(intent);
                }
            });
            readBerita();
        } else {
            Toast.makeText(getApplicationContext(), "Tidak Ada Koneksi Internet !",
                    Toast.LENGTH_LONG).show();
            showLoading(false);
        }
    }

    public void init() {
        tvNISN = findViewById(R.id.text_nisn);
        tvNama = findViewById(R.id.text_nama);
        tvKelas = findViewById(R.id.text_kelas);
        tvPesan = findViewById(R.id.text_pesan);
        tvInformasi = findViewById(R.id.text_informasi);
        tvInformasiKosong = findViewById(R.id.text_informasi_kosong);
        containerMenu = findViewById(R.id.containerMenu);
        containerPesan = findViewById(R.id.containerPesan);
        containerProfile = findViewById(R.id.containerProfile);
        btnLogout = findViewById(R.id.buttonLogout);
        menuNilaiPTS = findViewById(R.id.menuNilaiPTS);
        menuNilaiPAS = findViewById(R.id.menuNilaiPAS);
        menuPresensi = findViewById(R.id.menuAbsen);
        imageSetting = findViewById(R.id.image_setting);
        imageInfo = findViewById(R.id.image_info);
        rvInfo = findViewById(R.id.rvInformasi);

        sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);

        informationList = new ArrayList<>();

        nisn = getIntent().getStringExtra(TAG_NISN);
        nama = getIntent().getStringExtra(TAG_NAMA);
        kelas = getIntent().getStringExtra(TAG_KELAS);

        tvNISN.setText(nisn);
        tvNama.setText(nama);
        tvKelas.setText(kelas);

        containerProfile.setVisibility(View.VISIBLE);
        containerMenu.setVisibility(View.VISIBLE);
        tvInformasi.setVisibility(View.VISIBLE);
        imageInfo.setVisibility(View.VISIBLE);
        imageSetting.setVisibility(View.VISIBLE);
//        tvPesan.setVisibility(View.VISIBLE);
//        containerPesan.setVisibility(View.VISIBLE);
        btnLogout.setVisibility(View.VISIBLE);

        rvLayoutManager = new LinearLayoutManager(this);
        rvInfo.setHasFixedSize(true);
        rvInfo.setLayoutManager(rvLayoutManager);
        horizontalLayout = new LinearLayoutManager(DashboardActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rvInfo.setLayoutManager(horizontalLayout);

        imageSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, SettingsActivity.class);
                intent.putExtra(TAG_NISN, nisn);
                startActivity(intent);
            }
        });

        imageInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, InfoActivity.class);
                startActivity(intent);
            }
        });

        showLoading(false);
    }

    private void readBerita() {
        mRequestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("result");
                    if (jsonArray.length() > 0) {
                        tvInformasiKosong.setVisibility(View.GONE);
                        rvInfo.setVisibility(View.VISIBLE);
                    } else {
                        tvInformasiKosong.setVisibility(View.VISIBLE);
                        rvInfo.setVisibility(View.GONE);
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject data = jsonArray.getJSONObject(i);
                        int id = data.getInt("no_info");
                        String judul = data.getString("judul_info");
                        String deskripsi = data.getString("deskripsi_info");
                        String media = data.getString("media_info");
                        String tipe = data.getString("media_info_tipe");
                        String tanggal = data.getString("tanggal_info");

                        informationList.add(new Information(id, judul, deskripsi, media, tipe, tanggal));
                    }
                    mInfoAdapter = new InformationAdapter(DashboardActivity.this, informationList);
                    rvInfo.setAdapter(mInfoAdapter);
                    mInfoAdapter.setOnItemClickListener(DashboardActivity.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
        Intent detailIntent = new Intent(this, BeritaActivity.class);
        Information clickedItem = informationList.get(position);
        detailIntent.putExtra(EXTRA_JUDUL, clickedItem.getJudul_info());
        detailIntent.putExtra(EXTRA_DESKRIPSI, clickedItem.getDeskripsi_info());
        detailIntent.putExtra(EXTRA_MEDIA, clickedItem.getMedia_info());
        detailIntent.putExtra(EXTRA_MEDIA_TIPE, clickedItem.getMedia_tipe_info());
        detailIntent.putExtra(EXTRA_TANGGAL, clickedItem.getTanggal_info());
        startActivity(detailIntent);
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}