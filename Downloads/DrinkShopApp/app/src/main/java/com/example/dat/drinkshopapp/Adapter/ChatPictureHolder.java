package com.example.dat.drinkshopapp.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dat.drinkshopapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatPictureHolder extends RecyclerView.ViewHolder {
    public TextView tvTime,tvMessage,tvEmail;
    public CircleImageView imProfile;
    public ImageView imPreview;
    public ChatPictureHolder(@NonNull View itemView) {
        super(itemView);
        tvTime = itemView.findViewById(R.id.tvTime);
        tvMessage = itemView.findViewById(R.id.tvChatMessage);
        tvEmail = itemView.findViewById(R.id.tvEmail);
        imProfile = itemView.findViewById(R.id.imProfile);
        imPreview = itemView.findViewById(R.id.imPreview);
    }
}
