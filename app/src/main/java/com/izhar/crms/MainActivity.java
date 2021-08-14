package com.izhar.crms;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.izhar.crms.activities.IncidentReport;
import com.izhar.crms.activities.MissingPersonReport;
import com.izhar.crms.objects.Incident;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.lang.reflect.Method;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,
                R.id.missing_person,
                R.id.wanted_person,
                R.id.certificate)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        askPermission();
        if(Build.VERSION.SDK_INT>=24){
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> { startActivity(new Intent(this, IncidentReport.class)); });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1002 && permissions.length == 0){
            Toast.makeText(this, getResources().getString(R.string.grant_permission), Toast.LENGTH_SHORT).show();
            askPermission();
        }
    }

    private void askPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1002);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.setting){
            SharedPreferences lang = getSharedPreferences("language", MODE_PRIVATE);
            BottomSheetDialog dialog = new BottomSheetDialog(MainActivity.this);
            dialog.setContentView(R.layout.settings);
            TextView am, om, en;
            Button save = dialog.findViewById(R.id.save);
            am = dialog.findViewById(R.id.am);
            om = dialog.findViewById(R.id.om);
            en = dialog.findViewById(R.id.en);

            am.setOnClickListener(v -> {
                lang.edit().putString("language", "am").apply();
                am.setBackgroundResource(R.drawable.selected_lang_bg);
                en.setBackgroundResource(R.drawable.lang_bg);
                om.setBackgroundResource(R.drawable.lang_bg);
            });
            om.setOnClickListener(v -> {
                lang.edit().putString("language", "om").apply();
                om.setBackgroundResource(R.drawable.selected_lang_bg);
                en.setBackgroundResource(R.drawable.lang_bg);
                am.setBackgroundResource(R.drawable.lang_bg);
            });
            en.setOnClickListener(v -> {
                lang.edit().putString("language", "en").apply();
                en.setBackgroundResource(R.drawable.selected_lang_bg);
                am.setBackgroundResource(R.drawable.lang_bg);
                om.setBackgroundResource(R.drawable.lang_bg);
            });
            save.setOnClickListener(v -> {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                finish();
            });
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private void setLanguage() {
        SharedPreferences sharedPreferences = getSharedPreferences("language", MODE_PRIVATE);
        Locale locale = new Locale(sharedPreferences.getString("language", "om"));
        Configuration configuration = new Configuration();
        Locale.setDefault(locale);
        configuration.locale = locale;
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
    }
}