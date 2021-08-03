package com.izhar.crms.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.izhar.crms.R;
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
        Picasso.with(context).load(person.getImage()).placeholder(R.drawable.missing_person).into(holder.image);
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

            });
        }
    }
}
