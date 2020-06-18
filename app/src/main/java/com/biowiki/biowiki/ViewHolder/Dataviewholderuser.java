package com.biowiki.biowiki.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.biowiki.biowiki.R;
import com.biowiki.biowiki.UserView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class RecyclerViewHolderuser extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener
{
    public ImageView profile_imageuser;
    public TextView profile_nameuser,profile_decuser,profile_coinuser;

    private ItemClickListener itemClickListener;

    public RecyclerViewHolderuser(View itemView) {
        super(itemView);
        profile_nameuser = itemView.findViewById(R.id.profile_nameuser);
        profile_decuser = itemView.findViewById(R.id.profile_decuser);
        profile_imageuser = itemView.findViewById(R.id.profile_imageuser);
        profile_coinuser= itemView.findViewById(R.id.profile_coinuser);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }
    public void setItemClickListener(ItemClickListener itemClickListener)
    {
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
public class Dataviewholderuser extends RecyclerView.Adapter<RecyclerViewHolderuser> {

    ArrayList<String> namelist;
    ArrayList<String> declist;
    ArrayList<String> linkimglist;
    ArrayList<String> useidlist;
    ArrayList<Long> biocoinlist;
    private Context context;
    public Dataviewholderuser( Context context,ArrayList<String> namelist, ArrayList<String> declist, ArrayList<String> linkimglist, ArrayList<Long> biocoinlist,ArrayList<String> useridlist) {
        this.namelist = namelist;
        this.declist = declist;
        this.linkimglist = linkimglist;
        this.context = context;
        this.biocoinlist = biocoinlist;
        this.useidlist = useridlist;
    }

    @NonNull
    @Override
    public RecyclerViewHolderuser onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.search_result_user,viewGroup,false);
        return new RecyclerViewHolderuser(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolderuser recyclerViewHolderuser, int i) {
        recyclerViewHolderuser.profile_nameuser.setText(namelist.get(i));
        Picasso.get().load(linkimglist.get(i).toString()).into(recyclerViewHolderuser.profile_imageuser);
        recyclerViewHolderuser.profile_decuser.setText(declist.get(i));
        recyclerViewHolderuser.profile_coinuser.setText(biocoinlist.get(i).toString());
        recyclerViewHolderuser.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {

                Intent intent = new Intent(context, UserView.class);
                intent.putExtra("name", namelist.get(position));
                intent.putExtra("dec", declist.get(position));
                intent.putExtra("image", linkimglist.get(position));
                intent.putExtra("coin" , biocoinlist.get(position).toString());
                intent.putExtra("userid",useidlist.get(position));

                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return namelist.size();
    }
}
