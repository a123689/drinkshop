package com.example.dat.drinkshopapp.Adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dat.drinkshopapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatTextHolder extends RecyclerView.ViewHolder {

    public TextView tvTime;
    public TextView tvEmail;
    public TextView tvMessage;
    public CircleImageView imProfile;
    public ChatTextHolder(@NonNull View itemView) {
        super(itemView);
        tvTime = itemView.findViewById(R.id.tvTime);
        tvEmail = itemView.findViewById(R.id.tvEmail);
        tvMessage = itemView.findViewById(R.id.tvChatMessage);
        imProfile = itemView.findViewById(R.id.imProfile);

    }
}
