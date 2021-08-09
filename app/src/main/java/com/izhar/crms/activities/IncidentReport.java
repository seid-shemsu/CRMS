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
import java.util.Calendar;

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

    AutoCompleteTextView kebele, type;
    EditText place, description, image;
    Button send;
    static File file;
    static Uri uri;
    final int RC_TAKE_PHOTO = 1;
    private static final String url = "http://10.240.72.142:8000/upload/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_incident);
        kebele = findViewById(R.id.kebele);
        type = findViewById(R.id.type);
        place = findViewById(R.id.special_place);
        description = findViewById(R.id.description);
        image = findViewById(R.id.image);


        ArrayAdapter<CharSequence> types = ArrayAdapter.createFromResource(this, R.array.types, R.layout.list_item);
        type.setAdapter(types);

        ArrayAdapter<CharSequence> kebeles = ArrayAdapter.createFromResource(this, R.array.kebele, R.layout.list_item);
        kebele.setAdapter(kebeles);

        image.setOnClickListener(v -> {
            /*Intent intent = new Intent();
            startActivityForResult(intent, 1010);*/
            takePhoto();
        });

        send = findViewById(R.id.send_report);
        send.setOnClickListener(v -> {
            /*try {
                String url = "http://10.240.72.142:8000/reportIncident/";
                JSONObject object = new JSONObject();
                object.put("type", type.getText().toString());
                object.put("description", description.getText().toString());
                object.put("address", kebele.getText().toString() + " " + place.getText().toString());

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                        url,
                        object,
                        response -> {
                            Toast.makeText(this, "sending", Toast.LENGTH_SHORT).show();
                        },
                        error -> {
                            Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                RequestQueue queue = Volley.newRequestQueue(this);
                queue.add(request);

            }
            catch (Exception e){
                Toast.makeText(this, e.getMessage() + "\nerror 101", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }*/
            //upload(getPath());
            upload();
        });
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = new File(getExternalCacheDir(),
                "phone_" + (System.currentTimeMillis()) + ".jpg");
        uri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, RC_TAKE_PHOTO);

    }

    private String getPath() {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            File wallpaperDirectory = new File(
                    Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
            // have the object build the directory structure, if needed.
            if (!wallpaperDirectory.exists()) {
                wallpaperDirectory.mkdirs();
            }

            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1010 && resultCode != 0) {
            image.setText(uri.toString());
        }
    }


    private void upload() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DjangoApi.DJANGO_SITE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        /*DjangoApi postApi = retrofit.create(DjangoApi.class);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part multiPartBody = MultipartBody.Part
                .createFormData("image", file.getPath().toLowerCase(), requestBody);

        Call<RequestBody> call = postApi.uploadFile(multiPartBody);

        call.enqueue(new Callback<RequestBody>() {
            @Override
            public void onResponse(Call<RequestBody> call, Response<RequestBody> response) {
                Toast.makeText(IncidentReport.this, "good", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<RequestBody> call, Throwable t) {
                Toast.makeText(IncidentReport.this, "fail\n" + call.toString(), Toast.LENGTH_SHORT).show();

            }
        });*/
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
        JSONObject object = new JSONObject();
        try {
            object.put("type", type.getText().toString());
            object.put("description", description.getText().toString());
            object.put("address", kebele.getText().toString() + " " + place.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody json = RequestBody.create(MediaType.parse("application/json"), object.toString());

        MultipartBody.Part json_ = MultipartBody.Part.createFormData("json", "json", json);
        DjangoApi getResponse = retrofit.create(DjangoApi.class);
        Call<RequestBody> call = getResponse.uploadFile(fileToUpload);
        Log.d("assss", "asss");
        call.enqueue(new Callback<RequestBody>() {
            @Override
            public void onResponse(Call<RequestBody> call, Response<RequestBody> response) {
                Log.d("mullllll", "good");
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d("gttt", "bad");
            }
        });
    }

    private static final String IMAGE_DIRECTORY = "/demonuts_upload_gallery";

}
