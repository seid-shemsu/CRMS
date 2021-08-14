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
            AhoyOnboarderCard notify = new AhoyOnboarderCard("Notification", "this is description for notification", R.drawable.notify);
            AhoyOnboarderCard wanted = new AhoyOnboarderCard("Wanted Criminal", "this is description for Wanted Criminal", R.drawable.wanted);
            AhoyOnboarderCard missed = new AhoyOnboarderCard("Missed Person", "this is description for Missed Person", R.drawable.missed);
            AhoyOnboarderCard certify = new AhoyOnboarderCard("Certificate", "this is description for Certificate", R.drawable.certi);
            AhoyOnboarderCard incident = new AhoyOnboarderCard("Incident", "this is description for Incident", R.drawable.incidet);

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

            setFinishButtonTitle("Get Started");
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