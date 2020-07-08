package project.thebest.sistemakademik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    Context context;
    Intent intent;
    SharedPreferences sharedPreferences;
    String SHARED_PREF_NAME ="user_pref";
    SharedPreferences.Editor sharedPrefEditor;

    protected boolean isLoggedIn(){
        return sharedPreferences.getBoolean("login",false);
    }

    protected void logout(){
        sharedPrefEditor.putBoolean("login",false);
        sharedPrefEditor.apply();
        sharedPrefEditor.commit();
    }

    public void init() {
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        sharedPrefEditor = sharedPreferences.edit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}