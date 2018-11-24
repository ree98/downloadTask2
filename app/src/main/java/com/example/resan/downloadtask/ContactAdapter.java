package com.example.resan.downloadtask;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ContactAdapter extends RecyclerView.Adapter<ContactViewHolder> {
    Context mContext;
    ArrayList<Contact> mListContact;

    public ContactAdapter(ArrayList<Contact> contact, Context mContext){
        this.mContext = mContext;
        this.mListContact =contact;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.container_data,parent,false);
        return new ContactViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder viewHolder, int position) {
        Contact listContact =mListContact.get(position);

        viewHolder.tvName.setText(listContact.getName());
        viewHolder.tvStock.setText(listContact.getStock());
        viewHolder.tvPrice.setText(listContact.getPrice());
        Glide.with(viewHolder.ivThumbnail.getContext()).load(listContact.getImage()).apply(new RequestOptions().placeholder(viewHolder.circularProgressDrawable)).into(viewHolder.ivThumbnail);
    }

    @Override
    public int getItemCount() {
        return mListContact.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CircularProgressDrawable circularProgressDrawable;
        private ConstraintLayout itemContainer;
        private ImageView ivThumbnail;
        private TextView tvName, tvStock, tvPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
            this.tvName = itemView.findViewById(R.id.tvName);
            this.tvStock = itemView.findViewById(R.id.tvStock);
            this.tvPrice = itemView.findViewById(R.id.tvPrice);
            this.itemContainer = itemView.findViewById(R.id.itemContainer);
            this.itemContainer.setOnClickListener(this);

            circularProgressDrawable = new CircularProgressDrawable(itemView.getContext());
            circularProgressDrawable.setCenterRadius(10f);
            circularProgressDrawable.start();
        }
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            // ubah respon dari web service kedalam bentuk String
            String response = new String(responseBody);
            try {
                JSONArray aa = new JSONArray(response);
                for (int count = 0; count < aa.length(); count++) {
                    Contact bb = Contact.fromJson(aa.getJSONObject(count));
                    mListContact.add(bb);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onClick(View v) {

        }
    }
}
