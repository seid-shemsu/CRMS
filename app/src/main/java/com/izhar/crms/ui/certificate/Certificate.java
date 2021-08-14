package com.izhar.crms.ui.certificate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class Certificate extends Fragment {

    View root;
    AutoCompleteTextView kebele;
    EditText name, f_name, phone, kebele_id_edit_text, house_no, kebele_id_photo_edit_text, photo_edit_text;
    Button send;

    private static File kebele_id_photo_file, photo_file;

    private static Uri photo_uri, kebele_id_photo_uri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setLanguage();
        root = inflater.inflate(R.layout.fragment_certificate, container, false);
        name = root.findViewById(R.id.name);
        f_name = root.findViewById(R.id.f_name);
        phone = root.findViewById(R.id.phone);
        kebele_id_edit_text = root.findViewById(R.id.kebele_id_number);
        kebele_id_photo_edit_text = root.findViewById(R.id.kebele_id_photo);
        photo_edit_text = root.findViewById(R.id.photo);
        house_no = root.findViewById(R.id.house_no);
        send = root.findViewById(R.id.send_request);
        kebele = root.findViewById(R.id.kebele);

        ArrayAdapter<CharSequence> kebele_adapter = ArrayAdapter.createFromResource(getContext(), R.array.kebele, R.layout.list_item);
        kebele.setAdapter(kebele_adapter);

        kebele_id_photo_edit_text.setOnClickListener(v -> {
            openFileChooser();
        });

        photo_edit_text.setOnClickListener(v -> {
            takePhoto();
        });

        send.setOnClickListener(v -> {
            if (is_valid()) {
                sendRequest();
            } else {
                Toast.makeText(getContext(), R.string.fill_form, Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }


    private boolean is_valid() {
        if (name.getText().toString().length() == 0) {
            name.setError(getString(R.string.insert_name));
            return false;
        }
        if (f_name.getText().toString().length() == 0) {
            f_name.setError(getString(R.string.insert_father));
            return false;
        }
        if (phone.getText().toString().length() == 0) {
            phone.setError(getString(R.string.insert_phone));
            return false;
        }
        if (kebele.getText().toString().length() == 0) {
            kebele.setError(getString(R.string.insert_kebele));
            return false;
        }
        if (house_no.getText().toString().length() == 0) {
            house_no.setError(getString(R.string.insert_house));
            return false;
        }
        if (kebele_id_edit_text.getText().toString().length() == 0) {
            kebele_id_edit_text.setError(getString(R.string.insert_kebele_id));
            return false;
        }
        if (kebele_id_photo_file == null) {
            Toast.makeText(getContext(), getContext().getResources().getString(R.string.insert_kebele_pic), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (photo_file == null) {
            Toast.makeText(getContext(), getContext().getResources().getString(R.string.insert_pic_you), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photo_file = new File(getContext().getExternalCacheDir(), "photo_" + (System.currentTimeMillis()) + ".jpg");
        photo_uri = Uri.fromFile(photo_file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photo_uri);
        startActivityForResult(intent, 1010);
    }

    private void takePhoto2() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        kebele_id_photo_file = new File(getContext().getExternalCacheDir(), "kebele_photo_" + (System.currentTimeMillis()) + ".jpg");
        kebele_id_photo_uri = Uri.fromFile(kebele_id_photo_file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, kebele_id_photo_uri);
        startActivityForResult(intent, 1012);
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "select image"),
                1001);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1010 && resultCode != 0) {
            photo_edit_text.setText("uploaded");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                photo_edit_text.setCompoundDrawablesRelative(null, null, getResources().getDrawable(R.drawable.check), null);
            }
        }
        if (requestCode == 1012 && resultCode != 0) {
            kebele_id_photo_edit_text.setText("uploaded");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                kebele_id_photo_edit_text.setCompoundDrawablesRelative(null, null, getResources().getDrawable(R.drawable.check), null);
            }
        }
        if (requestCode == 1001 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            kebele_id_photo_uri = data.getData();
            kebele_id_photo_file = new File(this.getRealPathFromURIForGallery(kebele_id_photo_uri));
            kebele_id_photo_edit_text.setText("uploaded");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                kebele_id_photo_edit_text.setCompoundDrawablesRelative(null, null, getResources().getDrawable(R.drawable.check), null);
            }
        }

    }

    public String getRealPathFromURIForGallery(Uri uri) {
        if (uri == null) {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContext().getContentResolver().query(uri, projection, null,
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

    private void sendRequest() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DjangoApi.host_ip)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DjangoApi getResponse = retrofit.create(DjangoApi.class);

        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String now = spf.format(new Date());

        RequestBody your_photo = RequestBody.create(MediaType.parse("image/*"), photo_file);
        RequestBody kebele_id_photo = RequestBody.create(MediaType.parse("image/*"), kebele_id_photo_file);

        MultipartBody.Part photo_ = MultipartBody.Part.createFormData("photo", photo_file.getName(), your_photo);
        MultipartBody.Part kebele_photo_ = MultipartBody.Part.createFormData("id_photo", kebele_id_photo_file.getName(), kebele_id_photo);
        RequestBody name_ = RequestBody.create(okhttp3.MultipartBody.FORM, name.getText().toString() + " " + f_name.getText().toString());
        RequestBody phone_ = RequestBody.create(okhttp3.MultipartBody.FORM, phone.getText().toString());
        RequestBody address_ = RequestBody.create(okhttp3.MultipartBody.FORM, kebele.getText().toString() + "/" + house_no.getText().toString());
        RequestBody kebele_id_ = RequestBody.create(okhttp3.MultipartBody.FORM, kebele_id_edit_text.getText().toString());
        RequestBody date = RequestBody.create(okhttp3.MultipartBody.FORM, now);

        Call<RequestBody> call = getResponse.requestCertificate(name_, phone_, address_, kebele_id_, date, kebele_photo_, photo_);
        //Call<RequestBody> call = getResponse.requestCertificate(name_, phone_, address_, kebele_id_, date, photo_);
        //Call<RequestBody> call = getResponse.requestCertificate(photo_);

        Log.d("status", "sending...");
        call.enqueue(new Callback<RequestBody>() {
            @Override
            public void onResponse(Call<RequestBody> call, Response<RequestBody> response) {
                Log.d("status", "success");
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d("status", "failed");
            }
        });
    }

    private void setLanguage() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("language", MODE_PRIVATE);
        Locale locale = new Locale(sharedPreferences.getString("language", "om"));
        Configuration configuration = new Configuration();
        Locale.setDefault(locale);
        configuration.locale = locale;
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
    }
}