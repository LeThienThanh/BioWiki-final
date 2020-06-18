package com.biowiki.biowiki.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.biowiki.biowiki.Ar;
import com.biowiki.biowiki.Models.Model;
import com.biowiki.biowiki.R;
import com.biowiki.biowiki.ViewHolder.ItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ModelAdapter extends RecyclerView.Adapter <ModelAdapter.modelViewHolder> {

    Context mContext;
    ArrayList<Model> mModels;

    public ModelAdapter(Context context, ArrayList<Model> models) {
        this.mContext = context;
        this.mModels = models;
    }

    @NonNull
    @Override
    public modelViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.search_result_ar, viewGroup, false);
        return new modelViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull modelViewHolder modelViewHolder, int i) {
        Model modelCurrent = mModels.get(i);

        modelViewHolder.txtName.setText(modelCurrent.getName());
        Picasso.get().load(modelCurrent.getImage()).into(modelViewHolder.avatar);

        modelViewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent intent = new Intent(mContext, Ar.class);

                intent.putExtra("gltf", modelCurrent.getGltf());

                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mModels.size();
    }

    public class modelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtName;
        ImageView avatar;

        private ItemClickListener itemClickListener;

        public modelViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.arName);
            avatar = itemView.findViewById(R.id.arImage);

            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }
    }
}
