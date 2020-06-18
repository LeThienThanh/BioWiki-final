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

import com.biowiki.biowiki.AnimalView;
import com.biowiki.biowiki.Models.Animal;
import com.biowiki.biowiki.R;
import com.biowiki.biowiki.ViewHolder.ItemClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AnimalAdapter extends RecyclerView.Adapter <AnimalAdapter.animalViewHolder> {

    List<Animal> mAnimals;
    Context mContext;

    public AnimalAdapter(Context context, List<Animal> animals) {
        mContext = context;
        mAnimals = animals;
    }

    @NonNull
    @Override
    public animalViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.search_result_animal, viewGroup, false);
        return new animalViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull animalViewHolder animalViewHolder, int i) {
        Animal animalCurrent = mAnimals.get(i);

        animalViewHolder.txtName.setText(animalCurrent.getName());
        animalViewHolder.txtUername.setText("Source: " + animalCurrent.getSource());
        animalViewHolder.txtVote.setText(String.valueOf(animalCurrent.getVote()));
        Picasso.get().load(animalCurrent.getImage()).into(animalViewHolder.imgAvatar);

        animalViewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Animal animal = mAnimals.get(position);

                Intent intent = new Intent(mContext, AnimalView.class);

                intent.putExtra("Animal", animal);

                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAnimals.size();
    }

    public class animalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txtName, txtUername, txtVote;
        ImageView imgAvatar;

        private ItemClickListener itemClickListener;

        public animalViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.profile_name);
            txtUername = itemView.findViewById(R.id.profile_uid);
            txtVote = itemView.findViewById(R.id.voting);
            imgAvatar = itemView.findViewById(R.id.profile_image);

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

    public static class Location implements ClusterItem {
       Double lati,longti,radius;
       public String name, img;
       LatLng mposition;
        public Location() {
        }

        public Location(Double radius, String name, String img, LatLng mposition) {
            this.radius = radius;
            this.name = name;
            this.img = img;
            this.mposition = mposition;
        }
    //    public LatLng getMposition() {
    //        return mposition;
    //    }
    //
    //    public void setMposition(LatLng mposition) {
    //        this.mposition = mposition;
    //   }
        //
    //    public Location(Double lati, Double longti, String name, String img, Double radius) {
    //        this.lati = lati;
    //        this.longti = longti;
    //        this.name = name;
    //        this.img = img;
    //        this.radius = radius;
    //    }



    //    public LatLng getMlocation() {
    //        return mlocation;
    //    }

    //    public void setMlocation(LatLng mlocation) {
    //        this.mlocation = mlocation;
    //    }


    //    public Double getLati() {
    //        return lati;
    //    }
    //
    //    public void setLati(Double lati) {
    //        this.lati = lati;
    //    }
    //
    //    public Double getLongti() {
    //        return longti;
    //    }
    //
    //    public void setLongti(Double longti) {
    //        this.longti = longti;
    //    }

        public Double getRadius() {
            return radius;
        }

        public void setRadius(Double radius) {
            this.radius = radius;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        @Override
        public LatLng getPosition() {
            return mposition;
        }

        @Override
        public String getTitle() {
            return null;
        }

        @Override
        public String getSnippet() {
            return null;
        }
    }

    public static class Locationadd {
        Double lati,longti,radius;
        String name,img;

        public Locationadd(Double lati, Double longti, Double radius, String name, String img) {
            this.lati = lati;
            this.longti = longti;
            this.radius = radius;
            this.name = name;
            this.img = img;
        }
        public Locationadd() {
        }

        public Double getLati() {
            return lati;
        }

        public void setLati(Double lati) {
            this.lati = lati;
        }

        public Double getLongti() {
            return longti;
        }

        public void setLongti(Double longti) {
            this.longti = longti;
        }

        public Double getRadius() {
            return radius;
        }

        public void setRadius(Double radius) {
            this.radius = radius;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }
    }
}
