//package com.biowiki.biowiki;
//
//import android.content.Context;
//import android.content.Intent;
//import android.support.annotation.NonNull;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.squareup.picasso.Picasso;
//
//import java.util.ArrayList;
//
//public class Dulieu extends RecyclerView.Adapter<SearchAdapter.SearchViewholder> {
//    Context acontext;
//    ArrayList<String> namelist;
//    ArrayList<String> declist;
//    ArrayList<String> linkimglist;
//    ArrayList<String> adresslist;
//    private ItemClickListener itemClickListener;
//    public void xuli (int position){
//
//    }
//    class SearchViewholder extends RecyclerView.ViewHolder {
//        ImageView profile_image;
//        TextView profile_name,profile_dec,profile_adress;
//        public SearchViewholder(@NonNull View itemView) {
//            super(itemView);
//            //profile_adress = itemView.findViewById(R.id.pro);
//            profile_name = itemView.findViewById(R.id.profile_name);
//            profile_dec = itemView.findViewById(R.id.profile_dec);
//            profile_image = itemView.findViewById(R.id.profile_image);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(new MainActivity(), profile_name.getText(), Toast.LENGTH_SHORT).show();
//                }
//            });
//
//        }
////        public interface OnItemClickedListener {
////            void onItemClick(String username);
////        }
////
////        private OnItemClickedListener onItemClickedListener;
////
////        public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
////            this.onItemClickedListener = onItemClickedListener;
////        }
//    }
//
//    public Dulieu(Context acontext, ArrayList<String> namelist, ArrayList<String> declist, ArrayList<String> linkimglist, ArrayList<String> adresslist) {
//        this.acontext = acontext;
//        this.namelist = namelist;
//        this.declist = declist;
//        this.linkimglist = linkimglist;
//        this.adresslist = adresslist;
//    }
//
//    @NonNull
//    @Override
//    public SearchAdapter.SearchViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_result_animal,viewGroup,false);
//        return new SearchAdapter.SearchViewholder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull SearchAdapter.SearchViewholder searchViewholder, int i) {
//        searchViewholder.profile_name.setText(namelist.get(i));
//        searchViewholder.profile_dec.setText(declist.get(i));
//        Picasso.get().load(linkimglist.get(i).toString()).into(searchViewholder.profile_image);
//        //searchViewholder.speadress.setText(adresslist.get(i));
//    }
//
//    @Override
//    public int getItemCount() {
//        return namelist.size();
//    }
//}
