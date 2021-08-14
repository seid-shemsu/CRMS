package com.izhar.crms.ui.missing;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.izhar.crms.R;
import com.izhar.crms.activities.MissingPersonReport;
import com.izhar.crms.adapters.MissingAdapter;
import com.izhar.crms.api.DjangoApi;
import com.izhar.crms.objects.MissingPerson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class Missing extends Fragment {

    private View root;
    private RecyclerView recyclerView;
    private MissingAdapter adapter;
    private List<MissingPerson> missingPeople;
    private TextView no;
    private Dialog dialog;
    private FloatingActionButton fab;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setLanguage();
        root = inflater.inflate(R.layout.fragment_missing, container, false);
        setHasOptionsMenu(true);
        initDialog();
        dialog.show();
        recyclerView = root.findViewById(R.id.recycler);
        no = root.findViewById(R.id.no);
        fab = root.findViewById(R.id.fab);
        missingPeople = new ArrayList<>();

        fab.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), MissingPersonReport.class));
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        String url = DjangoApi.host_ip + "missed_persons/";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String id, name, address, sex, date, color, description, status, parent_name, parent_phone;
                    String image;
                    int age;
                    double height;
                    id = jsonObject.getString("id");
                    name = (jsonObject.getString("name"));
                    age = (jsonObject.getInt("age"));
                    address = (jsonObject.getString("address"));
                    sex = (jsonObject.getString("sex"));
                    date = (jsonObject.getString("date"));
                    color = (jsonObject.getString("color"));
                    height = (jsonObject.getDouble("height"));
                    description = (jsonObject.getString("description"));
                    status = (jsonObject.getString("status"));
                    parent_name = (jsonObject.getString("reporter_name"));
                    parent_phone = (jsonObject.getString("reporter_phone"));

                    image = (jsonObject.getString("image"));
                    MissingPerson person = new MissingPerson(
                            id, name, age, address, sex, date, color, height, description, status, image, parent_name, parent_phone);
                    missingPeople.add(person);
                }
                adapter = new MissingAdapter(getContext(), missingPeople);
                recyclerView.setAdapter(adapter);
                dialog.dismiss();
            } catch (Exception e) {
                Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        }, error -> {
            Toast.makeText(getContext(), error.getMessage() + "", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
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