package com.izhar.crms.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.izhar.crms.R;
import com.izhar.crms.objects.Notification;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.Holder> {
    private Context context;
    private List<Notification> notifications;

    public NotificationAdapter(Context context, List<Notification> notifications) {
        this.context = context;
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.single_notification, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Notification notification = notifications.get(position);
        if (!notification.getNew_().equalsIgnoreCase("new"))
            holder.new_.setVisibility(View.GONE);
        holder.new_.setText(notification.getNew_());
        holder.message.setText(notification.getMessage());
        holder.date.setText(notification.getDate().substring(0, notification.getDate().indexOf('T')));
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView new_, date, message;
        Button read_more;
        public Holder(@NonNull View itemView) {
            super(itemView);
            new_ = itemView.findViewById(R.id.new_);
            date = itemView.findViewById(R.id.date);
            message = itemView.findViewById(R.id.message);
            read_more = itemView.findViewById(R.id.read_more);

            read_more.setOnClickListener(v -> {
                readMore(notifications.get(getAdapterPosition()).getMessage());
            });
        }
    }

    private void readMore(String message) {
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.detail_message_view);
        TextView detail = dialog.findViewById(R.id.detail);
        detail.setText(message);
        dialog.show();
    }
}
