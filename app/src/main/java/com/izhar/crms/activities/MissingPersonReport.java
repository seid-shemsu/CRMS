package com.izhar.crms.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MissingPersonReport extends AppCompatActivity {
    EditText name, age, height, description, photo, parent_name, parent_phone, house_no, lost_date;
    AutoCompleteTextView sex, color, kebele;
    Button send;
    private static File file;
    private static Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missing_person_report);
        init();
        setAdapters();
        photo.setOnClickListener(v -> {
            openFileChooser();
        });

        send.setOnClickListener(v -> {
            if (is_valid()){
                report();
            }
        });
    }

    private void init(){
        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        height = findViewById(R.id.height);
        description = findViewById(R.id.description);
        house_no = findViewById(R.id.house_no);
        photo = findViewById(R.id.photo);
        parent_name = findViewById(R.id.parent_name);
        lost_date = findViewById(R.id.lost_date);
        parent_phone = findViewById(R.id.parent_phone);
        sex = findViewById(R.id.sex);
        color = findViewById(R.id.color);
        kebele = findViewById(R.id.kebele);
        send = findViewById(R.id.send);
    }

    private void setAdapters(){
        ArrayAdapter<CharSequence> sexes = ArrayAdapter.createFromResource(this, R.array.sex, R.layout.list_item);
        sex.setAdapter(sexes);


        ArrayAdapter<CharSequence> colors = ArrayAdapter.createFromResource(this, R.array.color, R.layout.list_item);
        color.setAdapter(colors);


        ArrayAdapter<CharSequence> kebeles = ArrayAdapter.createFromResource(this, R.array.kebele, R.layout.list_item);
        kebele.setAdapter(kebeles);
    }

    private void openFileChooser(){
        /*Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1001);*/
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "select image"),
                1001);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            /*uri = data.getData();
            file = new File(uri.getPath());
            photo.setText(file.getAbsolutePath());*/
            uri = data.getData();
            file = new File(this.getRealPathFromURIForGallery(uri));
            photo.setText(file.getAbsolutePath());
        }
    }
    public String getRealPathFromURIForGallery(Uri uri) {
        if (uri == null) {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = this.getContentResolver().query(uri, projection, null,
                null, null);
        if (cursor != null) {
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        assert false;
        cursor.close();
        return uri.getPath();
    }

    private boolean is_valid() {
        if (name.getText().toString().length() == 0){
            name.setError("");
            return false;
        }
        if (age.getText().toString().length() == 0){
            age.setError("");
            return false;
        }
        if (height.getText().toString().length() == 0){
            height.setError("");
            return false;
        }
        if (description.getText().toString().length() == 0){
            description.setError("");
            return false;
        }
        if (parent_phone.getText().toString().length() == 0){
            parent_phone.setError("");
            return false;
        }
        if (parent_name.getText().toString().length() == 0){
            parent_name.setError("");
            return false;
        }
        if (sex.getText().toString().length() == 0){
            sex.setError("");
            return false;
        }
        if (lost_date.getText().toString().length() == 0){
            lost_date.setError("");
            return false;
        }
        if (house_no.getText().toString().length() == 0){
            house_no.setError("");
            return false;
        }
        if (color.getText().toString().length() == 0){
            color.setError("");
            return false;
        }
        if (kebele.getText().toString().length() == 0){
            kebele.setError("");
            return false;
        }
        if (file == null){
            photo.setError("");
            return false;
        }
        return true;
    }

    private void report() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DjangoApi.host_ip)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DjangoApi getResponse = retrofit.create(DjangoApi.class);

        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String now = spf.format(new Date());

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);

        MultipartBody.Part image = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
        RequestBody name_ = RequestBody.create(okhttp3.MultipartBody.FORM, name.getText().toString());
        RequestBody sex_ = RequestBody.create(okhttp3.MultipartBody.FORM, sex.getText().toString());
        RequestBody age_ = RequestBody.create(okhttp3.MultipartBody.FORM, age.getText().toString());
        RequestBody color_ = RequestBody.create(okhttp3.MultipartBody.FORM, color.getText().toString());
        RequestBody height_ = RequestBody.create(okhttp3.MultipartBody.FORM, height.getText().toString());
        RequestBody description_ = RequestBody.create(okhttp3.MultipartBody.FORM, description.getText().toString());
        RequestBody address_ = RequestBody.create(okhttp3.MultipartBody.FORM, kebele.getText().toString() + "-->" + house_no.getText().toString());
        RequestBody lost_date_ = RequestBody.create(okhttp3.MultipartBody.FORM, lost_date.getText().toString());
        RequestBody status_ = RequestBody.create(okhttp3.MultipartBody.FORM, "pending");
        RequestBody date = RequestBody.create(okhttp3.MultipartBody.FORM, now);

        Call<RequestBody> call = getResponse.reportMissing(name_, sex_, age_, color_, height_, description_, address_, date, lost_date_, status_, image);
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