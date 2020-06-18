package com.biowiki.biowiki.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.biowiki.biowiki.AnimalView;
import com.biowiki.biowiki.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
    public ImageView profile_image;
    public TextView profile_name,profile_dec,profile_adress,profile_uid;

    private ItemClickListener itemClickListener;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        profile_name = itemView.findViewById(R.id.profile_name);
        profile_image = itemView.findViewById(R.id.profile_image);
        profile_uid = itemView.findViewById(R.id.profile_uid);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }

    @Override
    public boolean onLongClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),true);
        return true;
    }
}
public class Dataviewholder extends RecyclerView.Adapter<RecyclerViewHolder> {

    ArrayList<String> namelist;
    ArrayList<String> declist;
    ArrayList<String> linkimglist;
    ArrayList<String> adresslist;
    ArrayList<String> uidlist;
    private Context context;
    public Dataviewholder( Context context,ArrayList<String> namelist, ArrayList<String> declist, ArrayList<String> linkimglist, ArrayList<String> adresslist,ArrayList uidlist) {
        this.namelist = namelist;
        this.declist = declist;
        this.linkimglist = linkimglist;
        this.adresslist = adresslist;
        this.context = context;
        this.uidlist = uidlist;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.search_result_animal,parent,false);

        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.profile_name.setText(namelist.get(position));
        Picasso.get().load(linkimglist.get(position)).into(holder.profile_image);
        holder.profile_uid.setText("Upload User:" +uidlist.get(position));

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent intent = new Intent(context, AnimalView.class);
                intent.putExtra("name", namelist.get(position));
                intent.putExtra("dec", declist.get(position));
                intent.putExtra("image", linkimglist.get(position));
                intent.putExtra("source", adresslist.get(position));

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return namelist.size();
    }
}
