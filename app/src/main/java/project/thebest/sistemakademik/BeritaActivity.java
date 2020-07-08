package project.thebest.sistemakademik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import static project.thebest.sistemakademik.DashboardActivity.EXTRA_DESKRIPSI;
import static project.thebest.sistemakademik.DashboardActivity.EXTRA_JUDUL;
import static project.thebest.sistemakademik.DashboardActivity.EXTRA_TANGGAL;

public class BeritaActivity extends AppCompatActivity {
    TextView tvJudul, tvDeskripsi, tvTanggal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(Html.fromHtml("<font color='#e4f9ff'>Informasi Akademik</font>"));
        setContentView(R.layout.activity_berita);
        init();
    }

    private void init() {
        Intent intent = getIntent();
        String judul = intent.getStringExtra(EXTRA_JUDUL);
        String deskripsi = intent.getStringExtra(EXTRA_DESKRIPSI);
        String tanggal = intent.getStringExtra(EXTRA_TANGGAL);

        tvJudul = findViewById(R.id.text_judul);
        tvDeskripsi = findViewById(R.id.text_deskripsi);
        tvTanggal = findViewById(R.id.text_tanggal);

        tvJudul.setText(judul);
        tvDeskripsi.setText(deskripsi);
        tvTanggal.setText(tanggal);
    }
}