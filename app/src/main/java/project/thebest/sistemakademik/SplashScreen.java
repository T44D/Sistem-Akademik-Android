package project.thebest.sistemakademik;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends MainActivity {
    public final static String TAG_NISN = "nisn";
    public final static String TAG_NAMA = "nama";
    public final static String TAG_KELAS = "kelas";

    SharedPreferences sharedpreferences;
    Boolean session = false;
    String nisn, nama, kelas;
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        nisn = sharedpreferences.getString(TAG_NISN, null);
        nama = sharedpreferences.getString(TAG_NAMA, null);
        kelas = sharedpreferences.getString(TAG_KELAS, null);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (session) {
                    Intent intent = new Intent(SplashScreen.this, DashboardActivity.class);
                    intent.putExtra(TAG_NISN, nisn);
                    intent.putExtra(TAG_NAMA, nama);
                    intent.putExtra(TAG_KELAS, kelas);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },3000);
    }

}