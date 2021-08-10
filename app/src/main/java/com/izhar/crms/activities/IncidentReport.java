package com.izhar.crms.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.izhar.crms.R;
import com.izhar.crms.api.DjangoApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Headers;


public class IncidentReport extends AppCompatActivity {

    private AutoCompleteTextView kebele, type;
    private EditText place, description, photo;
    private Button send;
    private static File file;
    private static Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_incident);
        kebele = findViewById(R.id.kebele);
        type = findViewById(R.id.type);
        place = findViewById(R.id.special_place);
        description = findViewById(R.id.description);
        photo = findViewById(R.id.image);


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
                Toast.makeText(this, "please fill the form correctly", Toast.LENGTH_SHORT).show();
        });
    }

    private boolean is_valid() {
        if (type.getText().toString().length() == 0){
            type.setError("select type");
            return false;
        }
        if (kebele.getText().toString().length() == 0){
            kebele.setError("select kebele");
            return false;
        }
        if (place.getText().toString().length() == 0){
            place.setError("please mention the special place");
            return false;
        }
        if (description.getText().toString().length() == 0){
            description.setError("please describe the situation");
            return false;
        }
        if (file == null){
            Toast.makeText(this, "please take a picture", Toast.LENGTH_SHORT).show();
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
                Log.d("status", "sent one");
            }
            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d("status", "sent One");
            }
        });
    }
}
