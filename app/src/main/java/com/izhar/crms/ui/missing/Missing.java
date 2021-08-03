package com.izhar.crms.ui.missing;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.izhar.crms.activities.MissingPersonReport;
import com.izhar.crms.adapters.MissingAdapter;
import com.izhar.crms.objects.MissingPerson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Missing extends Fragment {

    private View root;
    private RecyclerView recyclerView;
    private MissingAdapter adapter;
    private List<MissingPerson> missingPeople;
    private TextView no;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        root = inflater.inflate(R.layout.fragment_missing, container, false);
        setHasOptionsMenu(true);
        recyclerView = root.findViewById(R.id.recycler);
        progressBar = root.findViewById(R.id.progressbar);
        no = root.findViewById(R.id.no);
        missingPeople = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        progressBar.setVisibility(View.VISIBLE);
        String url = "";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.INVISIBLE);
                try {

                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0 ; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id, name, address, sex, date, color, description, status;
                        String image;
                        int age;
                        double height;
                        id = jsonObject.getString("id");
                        name =  (jsonObject. getString("name"));
                        age =  (jsonObject. getInt("age"));
                        address =  (jsonObject. getString("address"));
                        sex =  (jsonObject. getString("sex"));
                        date =  (jsonObject. getString("date"));
                        color =  (jsonObject. getString("color"));
                        height =  (jsonObject. getDouble("height"));
                        description =  (jsonObject. getString("description"));
                        status =  (jsonObject. getString("status"));
                        image =  (jsonObject. getString("image"));
                        MissingPerson person = new MissingPerson(
                                id, name, age, address, sex, date, color, height, description, status, image);
                        missingPeople.add(person);

                    }
                    adapter = new MissingAdapter(getContext(), missingPeople);
                    recyclerView.setAdapter(adapter);
                }
                catch (Exception e) {
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.add) {
            startActivity(new Intent(getContext(), MissingPersonReport.class));
        }
        return true;
    }
}