package com.izhar.crms.intro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import com.izhar.crms.R;

import java.util.Locale;

public class Welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    public void en(View view) {
        setLanguage("en");
    }

    public void am(View view) {
        setLanguage("am");
    }

    public void om(View view) {
        setLanguage("om");
    }

    private void setLanguage(String language) {
            SharedPreferences sharedPreferences = getSharedPreferences("language", MODE_PRIVATE);
            sharedPreferences.edit().putString("language", language).apply();
            Locale locale = new Locale(language);
            Configuration configuration = new Configuration();
            Locale.setDefault(locale);
            configuration.locale = locale;
            getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
            startActivity(new Intent(this, Intro.class));
            finish();
        }
}