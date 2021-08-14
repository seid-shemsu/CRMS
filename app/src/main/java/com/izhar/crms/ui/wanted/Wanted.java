package com.izhar.crms.ui.wanted;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.izhar.crms.R;
import com.izhar.crms.adapters.MissingAdapter;
import com.izhar.crms.adapters.WantedAdapter;
import com.izhar.crms.api.DjangoApi;
import com.izhar.crms.objects.MissingPerson;
import com.izhar.crms.objects.WantedPerson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class Wanted extends Fragment {
    private View root;
    private RecyclerView recyclerView;
    private WantedAdapter adapter;
    private List<WantedPerson> wantedPeople;
    private TextView no;

    private Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setLanguage();
        root = inflater.inflate(R.layout.fragment_wanted, container, false);
        initDialog();
        dialog.show();
        recyclerView = root.findViewById(R.id.recycler);
        no = root.findViewById(R.id.no);
        wantedPeople = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        String url = DjangoApi.host_ip + "criminals/";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String id, name, image, sex, color, description, status;
                    int age;
                    double height;
                    id = jsonObject.getString("id");
                    name = (jsonObject.getString("name"));
                    age = (jsonObject.getInt("age"));
                    sex = (jsonObject.getString("sex"));
                    color = (jsonObject.getString("color"));
                    height = (jsonObject.getDouble("height"));
                    description = (jsonObject.getString("description"));
                    status = (jsonObject.getString("status"));
                    image = (jsonObject.getString("image"));
                    WantedPerson person = new WantedPerson(
                            id, name, age, sex, color, height, description, status, image);
                    wantedPeople.add(person);
                }
                adapter = new WantedAdapter(getContext(), wantedPeople);
                recyclerView.setAdapter(adapter);
            } catch (Exception e) {
                Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
            }
        }, error -> {
            Toast.makeText(getContext(), error.getMessage() + " ", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        requestQueue.add(stringRequest);
        return root;
    }

    private void initDialog() {
        dialog = new Dialog(getContext());
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading);
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