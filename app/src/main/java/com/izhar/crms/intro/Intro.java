package com.izhar.crms.intro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;

import com.codemybrainsout.onboarder.AhoyOnboarderActivity;
import com.codemybrainsout.onboarder.AhoyOnboarderCard;
import com.izhar.crms.MainActivity;
import com.izhar.crms.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Intro extends AhoyOnboarderActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLanguage();
        if (!getSharedPreferences("status", MODE_PRIVATE).getBoolean("started", false)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            AhoyOnboarderCard notify = new AhoyOnboarderCard(getString(R.string.notification), getString(R.string.desc_notification), R.drawable.notify);
            AhoyOnboarderCard wanted = new AhoyOnboarderCard(getString(R.string.wanted_criminal), getString(R.string.desc_wanted), R.drawable.wanted);
            AhoyOnboarderCard missed = new AhoyOnboarderCard(getString(R.string.missed_person), getString(R.string.desc_missed), R.drawable.missed);
            AhoyOnboarderCard certify = new AhoyOnboarderCard(getString(R.string.certificate), getString(R.string.desc_certificate), R.drawable.certi);
            AhoyOnboarderCard incident = new AhoyOnboarderCard( getString(R.string.incident), getString(R.string.desc_incident), R.drawable.incidet);

            notify.setBackgroundColor(R.color.black_transparent);
            wanted.setBackgroundColor(R.color.black_transparent);
            missed.setBackgroundColor(R.color.black_transparent);
            certify.setBackgroundColor(R.color.black_transparent);
            incident.setBackgroundColor(R.color.black_transparent);

            List<AhoyOnboarderCard> pages = new ArrayList<>();

            pages.add(notify);
            pages.add(wanted);
            pages.add(missed);
            pages.add(certify);
            pages.add(incident);

            for (AhoyOnboarderCard page : pages) {
                page.setTitleColor(R.color.white);
                page.setDescriptionColor(R.color.grey_200);
                page.setTitleTextSize(dpToPixels(18, this));
                page.setDescriptionTextSize(dpToPixels(12, this));
                page.setIconLayoutParams(700, 700, 150, 25, 25, 25);
            }

            setFinishButtonTitle(R.string.get_started);
            showNavigationControls(true);
            //setGradientBackground();
            setColorBackground(R.color.purple_500);

            //set the button style you created
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                setFinishButtonDrawableStyle(ContextCompat.getDrawable(this, R.drawable.rounded_button));
            }

            //Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
            //setFont(face);

            setOnboardPages(pages);
        }

    }

    @Override
    public void onFinishButtonPressed() {
        getSharedPreferences("status", MODE_PRIVATE).edit().putBoolean("started", true).apply();
        startActivity(new Intent(this, MainActivity.class));
        finish();
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