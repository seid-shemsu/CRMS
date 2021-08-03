package com.izhar.crms.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.izhar.crms.R;
import com.izhar.crms.adapters.NotificationAdapter;
import com.izhar.crms.adapters.WantedAdapter;
import com.izhar.crms.objects.Notification;
import com.izhar.crms.objects.WantedPerson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    List<Notification> notifications;
    NotificationAdapter adapter;
    RecyclerView recycler;
    ProgressBar progressBar;
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_notifications, container, false);
        notifications = new ArrayList<>();
        recycler = root.findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        progressBar = root.findViewById(R.id.progressBar);
        String url = "http://192.168.137.252:8000/notifications/";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.INVISIBLE);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0 ; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id, date, message, new_;
                        id = jsonObject.getString("id");
                        date = jsonObject.getString("date");
                        message = jsonObject.getString("message");
                        new_ = jsonObject.getString("_new");
                        notifications.add(
                                new Notification(id, date, message, new_)
                        );
                    }
                    adapter = new NotificationAdapter(getContext(), notifications);
                    recycler.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
        requestQueue.add(stringRequest);
        return root;
    }
}