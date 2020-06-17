//package com.biowiki.biowiki;
//
//
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.google.firebase.database.core.Context;
//
//import java.util.ArrayList;
//
//public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewholder>{
//    Context mcontext;
//    ArrayList<String> namelist;
//    ArrayList<String> declist;
//    ArrayList<String> linkimglist;
//    ArrayList<String> adresslist;
//    static class SearchViewholder extends  RecyclerView.ViewHolder {
//        ImageView profile_image;
//        TextView profile_name, profile_dec,speadress;
//        LinearLayout itemSR;
//        public SearchViewholder(@NonNull View itemView) {
//            super(itemView);
//            profile_name = itemView.findViewById(R.id.profile_name);
//            profile_dec = itemView.findViewById(R.id.profile_dec);
//            profile_image = itemView.findViewById(R.id.profile_image);
//            itemSR = itemView.findViewById(R.id.itemSR);
//            //speadress= itemView.findViewById(R.id.speadress);
//        }
//    }
//
////
////    public SearchAdapter(Context context, ArrayList<String> namelist, ArrayList<String> declist, ArrayList<String> linkimglist) {
////        this.mcontext = context;
////        this.namelist = namelist;
////        this.declist = declist;
////        this.linkimglist = linkimglist;
////    }
//
//
//    public SearchAdapter(Context mcontext, ArrayList<String> namelist, ArrayList<String> declist, ArrayList<String> linkimglist, ArrayList<String> adresslist) {
//        this.mcontext = mcontext;
//        this.namelist = namelist;
//        this.declist = declist;
//        this.linkimglist = linkimglist;
//        this.adresslist = adresslist;
//    }
//
//    @NonNull
//    @Override
//    public SearchAdapter.SearchViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_layout,viewGroup,false);
//        return new SearchAdapter.SearchViewholder(view);
//    }
//
//
//    @Override
//    public void onBindViewHolder(@NonNull SearchViewholder searchViewholder, int i) {
//        searchViewholder.profile_name.setText(namelist.get(i));
//        searchViewholder.profile_dec.setText(declist.get(i));
//        //searchViewholder.speadress.setText(adresslist.get(i));
//
//    }
//    @Override
//    public int getItemCount() {
//        return namelist.size();
//    }
//}
//
