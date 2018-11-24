package com.example.resan.downloadtask;

import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView tvName, tvStock, tvPrice;
    public ImageView ivThumbnail;
    public int circularProgressDrawable;
    private ConstraintLayout itemContainer;

    public ContactViewHolder(View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.tvName);
        tvStock = itemView.findViewById(R.id.tvStock);
        tvPrice = itemView.findViewById(R.id.tvPrice);
        ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
        this.itemContainer = itemView.findViewById(R.id.itemContainer);
        this.itemContainer.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

    }
}
