package com.izhar.crms.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.izhar.crms.R;
import com.izhar.crms.api.DjangoApi;
import com.izhar.crms.objects.WantedPerson;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WantedAdapter extends RecyclerView.Adapter<WantedAdapter.Holder> {
    private Context context;
    private List<WantedPerson> wantedPeople;

    private Dialog load_dialog;
    public WantedAdapter(Context context, List<WantedPerson> wantedPeople) {
        this.context = context;
        this.wantedPeople = wantedPeople;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.single_wanted_person, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        WantedPerson person = wantedPeople.get(position);
        holder.name.setText(person.getName());
        holder.age.setText(Integer.toString(person.getAge()));
        holder.height.setText(person.getHeight().toString());
        holder.sex.setText(person.getSex());
        Picasso.with(context).load(DjangoApi.host_ip.substring(0, DjangoApi.host_ip.length() - 1)  + person.getImage()).placeholder(R.drawable.wanted_person).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return wantedPeople.size();
    }


    class Holder extends RecyclerView.ViewHolder {
        TextView name, age, height, sex;
        ImageView image;
        Button detail;
        public Holder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            age = itemView.findViewById(R.id.age);
            height = itemView.findViewById(R.id.height);
            sex = itemView.findViewById(R.id.sex);
            image = itemView.findViewById(R.id.image);
            detail = itemView.findViewById(R.id.view_detail);
            detail.setOnClickListener(v -> {
                try {
                    showDetail(getAdapterPosition());
                } catch (Exception e) {
                    Toast.makeText(context,e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void showDetail(int position) {
        WantedPerson _person = wantedPeople.get(position);
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.wanted_person_detail);
        TextView name, age, sex, color, height, description, status;
        ImageView image;
        Button report;
        name = dialog.findViewById(R.id.name);
        age = dialog.findViewById(R.id.age);
        sex = dialog.findViewById(R.id.sex);
        color = dialog.findViewById(R.id.color);
        height = dialog.findViewById(R.id.height);
        description = dialog.findViewById(R.id.description);
        status = dialog.findViewById(R.id.status);
        image = dialog.findViewById(R.id.image);
        report = dialog.findViewById(R.id.report);
        name.setText(_person.getName());
        sex.setText(_person.getSex());
        age.setText(_person.getAge() + "");
        color.setText(_person.getColor());
        height.setText(_person.getHeight() + "");
        description.setText(_person.getDescription());
        status.setText(_person.getStatus());
        Picasso.with(context).load(DjangoApi.host_ip.substring(0, DjangoApi.host_ip.length() - 1)  + _person.getImage()).placeholder(R.drawable.wanted_person).into(image);
        report.setOnClickListener(v1 -> {
            dialog.dismiss();
            dialog.setContentView(R.layout.report_criminal);
            dialog.show();
            EditText special_place = dialog.findViewById(R.id.special_place);
            AutoCompleteTextView kebele = dialog.findViewById(R.id.kebele);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.kebele, R.layout.list_item);
            kebele.setAdapter(adapter);
            Button report_btn = dialog.findViewById(R.id.report);
            report_btn.setOnClickListener(v -> {
                if (kebele.getText().toString().length() > 0 && special_place.getText().toString().length() > 0){
                    dialog.dismiss();
                    initDialog();
                    load_dialog.show();
                    Toast.makeText(context, "Reporting on progress", Toast.LENGTH_SHORT).show();
                    reportCriminal(position, kebele.getText().toString(), special_place.getText().toString());

                }
                else {
                    Toast.makeText(context, "please fill the required fields", Toast.LENGTH_SHORT).show();
                }
            });
        });
        dialog.show();

    }

    private void reportCriminal(int position, String kebele, String special) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DjangoApi.host_ip)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DjangoApi getResponse = retrofit.create(DjangoApi.class);

        RequestBody criminal_id = RequestBody.create(okhttp3.MultipartBody.FORM, wantedPeople.get(position).getId());
        RequestBody address_ = RequestBody.create(okhttp3.MultipartBody.FORM, kebele+ "/" + special);

        Call<RequestBody> call = getResponse.reportCriminal(criminal_id, address_);

        Log.d("status", "sending...");
        call.enqueue(new Callback<RequestBody>() {
            @Override
            public void onResponse(Call<RequestBody> call, Response<RequestBody> response) {
                Log.d("status", "success");
                load_dialog.dismiss();
            }
            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d("status", "failed");
                load_dialog.dismiss();
            }
        });
    }

    private void initDialog() {
        load_dialog = new Dialog(context);
        load_dialog.setCancelable(false);
        load_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        load_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        load_dialog.setContentView(R.layout.loading);
    }
}
