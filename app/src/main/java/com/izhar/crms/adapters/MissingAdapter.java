package com.izhar.crms.adapters;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.izhar.crms.R;
import com.izhar.crms.api.DjangoApi;
import com.izhar.crms.objects.MissingPerson;
import com.izhar.crms.ui.missing.Missing;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MissingAdapter extends RecyclerView.Adapter<MissingAdapter.Holder> {

    private Context context;
    private List<MissingPerson> missingPeople;

    public MissingAdapter(Context context, List<MissingPerson> missingPeople) {
        this.context = context;
        this.missingPeople = missingPeople;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.single_missing_person, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        MissingPerson person = missingPeople.get(position);
        holder.name.setText(person.getName());
        holder.age.setText(Integer.toString(person.getAge()));
        holder.height.setText(Double.toString(person.getHeight()));
        holder.sex.setText(person.getSex());
        Picasso.with(context).load(DjangoApi.host_ip.substring(0, DjangoApi.host_ip.length() - 1)  +  person.getImage()).placeholder(R.drawable.missing_person).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return missingPeople.size();
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
                showDetail(getAdapterPosition());
            });
        }
    }

    private void showDetail(int position) {
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.missing_person_detail);
        TextView age, height, name, address, sex, date, color, description, status, reporter_name;
        ImageView image;
        Button call;
        age = dialog.findViewById(R.id.age);
        height = dialog.findViewById(R.id.height);
        name = dialog.findViewById(R.id.name);
        address = dialog.findViewById(R.id.address);
        sex = dialog.findViewById(R.id.sex);
        date = dialog.findViewById(R.id.date);
        color = dialog.findViewById(R.id.color);
        description = dialog.findViewById(R.id.description);
        status = dialog.findViewById(R.id.status);
        reporter_name = dialog.findViewById(R.id.parent_name);
        image = dialog.findViewById(R.id.image);
        call = dialog.findViewById(R.id.call);
        age.setText(missingPeople.get(position).getAge() + "");
        height.setText(missingPeople.get(position).getHeight() + "");
        name.setText(missingPeople.get(position).getName());
        address.setText(missingPeople.get(position).getAddress());
        sex.setText(missingPeople.get(position).getSex());
        date.setText(missingPeople.get(position).getDate());
        color.setText(missingPeople.get(position).getColor());
        description.setText(missingPeople.get(position).getDescription());
        status.setText(missingPeople.get(position).getStatus());
        reporter_name.setText(missingPeople.get(position).getReporter_name());
        Picasso.with(context).load(DjangoApi.host_ip.substring(0, DjangoApi.host_ip.length() - 1)  +  missingPeople.get(position).getImage()).placeholder(R.drawable.missing_person).into(image);
        call.setOnClickListener(v -> {
            makeCall(missingPeople.get(position).getReporter_phone());
        });
        dialog.show();
    }

    private void makeCall(String reporter_phone) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + reporter_phone));
            context.startActivity(callIntent);
        }
        else {
            ActivityCompat.requestPermissions((Activity) context, new String[] {Manifest.permission.CALL_PHONE}, 1001);
        }
    }

}
