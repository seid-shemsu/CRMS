package com.izhar.crms.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.izhar.crms.R;
import com.izhar.crms.api.Incident;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IncidentReport extends AppCompatActivity {

    AutoCompleteTextView kebele, type;
    EditText place, description, image;
    Button send;
    String imagePath;
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
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 1010);
        });

        send = findViewById(R.id.send_report);
        send.setOnClickListener(v -> {
            try {
                //String url = "http://10.240.72.142:8000/addincident/";
                JSONObject object = new JSONObject();
                object.put("type", type.getText().toString());
                object.put("description", description.getText().toString());
                object.put("address", kebele.getText().toString() + " " + place.getText().toString());
                /*JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                        url,
                        object,
                        response -> {
                            Toast.makeText(this, "sending", Toast.LENGTH_SHORT).show();
                        },
                        error -> {
                            Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                RequestQueue queue = Volley.newRequestQueue(this);
                queue.add(request);*/

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(com.izhar.crms.api.Incident.DJANGO_SITE)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();


                //DjangoApi postApi= retrofit.create(DjangoApi.class);
                Incident incident = retrofit.create(Incident.class);
                File imageFile = new File(imagePath);
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/data"), imageFile);
                MultipartBody.Part multiPartBody = MultipartBody.Part
                        .createFormData("model_pic", imageFile.getName(), requestBody);



                //Call<RequestBody> call = incident.uploadFile(multiPartBody, object);
                Call<RequestBody> call = incident.uploadFile(multiPartBody);
                call.enqueue(new Callback<RequestBody>() {


                    @Override
                    public void onResponse(Call<RequestBody> call, retrofit2.Response<RequestBody> response) {
                        Log.d("good", "good");
                    }

                    @Override
                    public void onFailure(Call<RequestBody> call, Throwable t) {
                        Log.d("fail", "fail");
                    }
                });
            }
            catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1010 && resultCode != 0){
            Bitmap bp = (Bitmap) data.getExtras().get( "data");
            //image.setText(bp.getGenerationId() + "");
            image.setText(data.getDataString());
            //imagePath = data.getData().getPath();
            imagePath = data.getDataString();
            if (data != null)
                Toast.makeText(this, data.getData().getPath(), Toast.LENGTH_SHORT).show();
        }
    }
}