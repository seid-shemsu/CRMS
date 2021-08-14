package com.izhar.crms.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.izhar.crms.R;
import com.izhar.crms.api.DjangoApi;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class IncidentReport extends AppCompatActivity {

    private AutoCompleteTextView kebele, type;
    private EditText place, description, photo;
    private Button send;
    private static File file;
    private static Uri uri;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLanguage();

        setContentView(R.layout.fragment_incident);
        kebele = findViewById(R.id.kebele);
        type = findViewById(R.id.type);
        place = findViewById(R.id.special_place);
        description = findViewById(R.id.description);
        photo = findViewById(R.id.image);
        initDialog();
        ArrayAdapter<CharSequence> types = ArrayAdapter.createFromResource(this, R.array.types, R.layout.list_item);
        type.setAdapter(types);

        ArrayAdapter<CharSequence> kebeles = ArrayAdapter.createFromResource(this, R.array.kebele, R.layout.list_item);
        kebele.setAdapter(kebeles);

        photo.setOnClickListener(v -> {
            takePhoto();
        });

        send = findViewById(R.id.send_report);
        send.setOnClickListener(v -> {
            if (is_valid())
                upload();
            else
                Toast.makeText(this, getString(R.string.fill_form), Toast.LENGTH_SHORT).show();
        });
    }

    private void initDialog() {
        dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading);
    }

    private boolean is_valid() {
        if (type.getText().toString().length() == 0){
            type.setError(getString(R.string.select_type));
            return false;
        }
        if (kebele.getText().toString().length() == 0){
            kebele.setError(getString(R.string.select_kebele));
            return false;
        }
        if (place.getText().toString().length() == 0){
            place.setError(getString(R.string.mention_special_place));
            return false;
        }
        if (description.getText().toString().length() == 0){
            description.setError(getString(R.string.describe_situation));
            return false;
        }
        if (file == null){
            Toast.makeText(this, getString(R.string.take_picture), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = new File(getExternalCacheDir(),
                "phone_" + (System.currentTimeMillis()) + ".jpg");
        uri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, 1010);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1010 && resultCode != 0) {
            photo.setText(uri.toString());
        }
    }

    private void upload() {
        dialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DjangoApi.host_ip)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DjangoApi getResponse = retrofit.create(DjangoApi.class);

        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String now = spf.format(new Date());

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);

        MultipartBody.Part image = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
        RequestBody desc = RequestBody.create(okhttp3.MultipartBody.FORM, description.getText().toString());
        RequestBody typ = RequestBody.create(okhttp3.MultipartBody.FORM, type.getText().toString());
        RequestBody add = RequestBody.create(okhttp3.MultipartBody.FORM, kebele.getText().toString() + " " + place.getText().toString());
        RequestBody date = RequestBody.create(okhttp3.MultipartBody.FORM, now);

        Call<RequestBody> call = getResponse.reportIncident(typ, desc, add, date, image);
        Log.d("status", "sending...");
        call.enqueue(new Callback<RequestBody>() {
            @Override
            public void onResponse(Call<RequestBody> call, Response<RequestBody> response) {
                Log.d("status", "sent___");
                dialog.dismiss();
                onBackPressed();
            }
            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d("status", "___sent");
                dialog.dismiss();
                onBackPressed();
            }
        });
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
