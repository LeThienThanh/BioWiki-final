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

import com.biowiki.biowiki.Models.User;
import com.biowiki.biowiki.R;
import com.biowiki.biowiki.UserView;
import com.biowiki.biowiki.ViewHolder.ItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter <UserAdapter.userViewHolder> {

    Context mContext;
    ArrayList<User> mUsers;

    public UserAdapter(Context context, ArrayList<User> users) {
        this.mContext = context;
        this.mUsers = users;
    }

    @NonNull
    @Override
    public userViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.search_result_user, viewGroup, false);
        return new userViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull userViewHolder userViewHolder, int i) {
        User user = mUsers.get(i);

        userViewHolder.txtName.setText(user.getName());
        userViewHolder.txtDec.setText(user.getDec());
        userViewHolder.txtCoin.setText(String.valueOf(user.getCoin()));
        Picasso.get().load(user.getImage()).into(userViewHolder.avatar);

        userViewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                User user = mUsers.get(position);

                Intent intent = new Intent(mContext, UserView.class);

                intent.putExtra("User", user);

                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class userViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtName,txtDec, txtCoin;
        ImageView avatar;

        private ItemClickListener itemClickListener;

        public userViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.profile_nameuser);
            txtDec = itemView.findViewById(R.id.profile_decuser);
            txtCoin = itemView.findViewById(R.id.profile_coinuser);
            avatar = itemView.findViewById(R.id.profile_imageuser);

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
