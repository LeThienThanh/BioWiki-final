/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.biowiki.biowiki;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.biowiki.biowiki.Adapter.AnimalAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.squareup.picasso.Picasso;

import java.util.Random;
/**
 * Demonstrates heavy customisation of the look of rendered clusters.
 */
public class MapsFragment extends BaseMaps implements ClusterManager.OnClusterClickListener<AnimalAdapter.Location>, ClusterManager.OnClusterInfoWindowClickListener<AnimalAdapter.Location>, ClusterManager.OnClusterItemClickListener<AnimalAdapter.Location>, ClusterManager.OnClusterItemInfoWindowClickListener<AnimalAdapter.Location> {
    private ClusterManager<AnimalAdapter.Location> mClusterManager;
    private Random mRandom = new Random(1984);
    DatabaseReference mreference;

    Circle mcircle;

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onCameraMove() {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }



    /**
     * Draws profile photos inside markers (using IconGenerator).
     * When there are multiple people in the cluster, draw multiple photos (using MultiDrawable).
     */
    private class PersonRenderer extends DefaultClusterRenderer<AnimalAdapter.Location> {
        private final IconGenerator mIconGenerator = new IconGenerator(ApplicationContextProvider.getContext());
        private final IconGenerator mClusterIconGenerator = new IconGenerator(ApplicationContextProvider.getContext());
        private  ImageView mImageView;
        private final ImageView mClusterImageView;
        private final int mDimension;

        public PersonRenderer() {
            super(ApplicationContextProvider.getContext(), mMap, mClusterManager);

            View multiProfile = getLayoutInflater().inflate(R.layout.multi_profile, null);
            mClusterIconGenerator.setContentView(multiProfile);
            mClusterImageView = (ImageView) multiProfile.findViewById(R.id.image);

            mImageView = new ImageView(ApplicationContextProvider.getContext());
            mDimension = (int) getResources().getDimension(R.dimen.custom_profile_image);
            mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
            int padding = (int) getResources().getDimension(R.dimen.custom_profile_padding);
            mImageView.setPadding(padding, padding, padding, padding);
            mIconGenerator.setContentView(mImageView);

        }

        @Override
        protected void onBeforeClusterItemRendered(AnimalAdapter.Location location, MarkerOptions markerOptions) {
            // Draw a single person.
            // Set the info window to show their name
            //mImageView.setImageResource(location.profi);
            Picasso.get().load(location.img.toString()).into(mImageView);
            Bitmap icon = mIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(location.name);

        }

//        @Override
//        protected void onBeforeClusterRendered(Cluster<Location> cluster, MarkerOptions markerOptions) {
//            // Draw multiple people.
//            // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).
//            List<Drawable> profilePhotos = new ArrayList<>(Math.min(4, cluster.getSize()));
//            int width = mDimension;
//            int height = mDimension;
//
//            for (Location p : cluster.getItems()) {
//                // Draw 4 at most.
//                if (profilePhotos.size() == 4) break;
//                Drawable drawable = getResources().getDrawable(p.profilePhoto);
//                Drawable drawable1 =;
//                drawable.setBounds(0, 0, width, height);
//                profilePhotos.add(drawable);
//            }
//            MultiDrawable multiDrawable = new MultiDrawable(profilePhotos);
//            multiDrawable.setBounds(0, 0, width, height);
//
//            mClusterImageView.setImageDrawable(multiDrawable);
//            Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
//            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
//        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            // Always render clusters.
            return cluster.getSize() > 1;
        }

    }

    @Override
    public boolean onClusterClick(Cluster<AnimalAdapter.Location> cluster) {
        // Show a toast with some info when the cluster is clicked.
        String firstName = cluster.getItems().iterator().next().name;
        Toast.makeText(getActivity(), cluster.getSize() + " (including " + firstName + ")", Toast.LENGTH_SHORT).show();


        // Zoom in the cluster. Need to create LatLngBounds and including all the cluster items
        // inside of bounds, then animate to center of the bounds.

        // Create the builder to collect all essential cluster items for the bounds.
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (ClusterItem item : cluster.getItems()) {
            builder.include(item.getPosition());

        }
        // Get the LatLngBounds
        final LatLngBounds bounds = builder.build();

        // Animate camera to the bounds
        try {
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<AnimalAdapter.Location> cluster) {
        // Does nothing, but you could go to a list of the users.
    }

    @Override
    public boolean onClusterItemClick(AnimalAdapter.Location item) {
        // Does nothing, but you could go into the user's profile page, for example.
        int fillcolorcircle = getResources().getColor(R.color.fillcircle);
        if (mcircle == null) {
            mcircle = mMap.addCircle(new CircleOptions().center(item.getPosition()).radius(item.getRadius()).strokeColor(Color.RED).fillColor(fillcolorcircle));

        } else {
            mcircle.remove();
            mcircle = null;

        }

        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(AnimalAdapter.Location item) {
        // Does nothing, but you could go into the user's profile page, for example.
    }

    @Override
    protected void startDemo() {
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 9.5f));
//        MarkerOptions options = new MarkerOptions()
//                .position(new LatLng(0,0));
        //mMarker = mMap.addMarker(options);

        mClusterManager = new ClusterManager<AnimalAdapter.Location>(getActivity(), mMap);
//        mMap.addMarker(options);
        mClusterManager.setRenderer(new PersonRenderer());

        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        mMap.setOnInfoWindowClickListener(mClusterManager);

        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);

        addItems();
        mClusterManager.cluster();
    }

    private void addItems() {
        // http://www.flickr.com/photos/sdasmarchives/5036248203/
        //mClusterManager.addItem(new Person(position(), "Walter", R.drawable.walter));
        mreference= FirebaseDatabase.getInstance().getReference();
        mreference.child("Location").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String name = snapshot.child("name").getValue(String.class);
                    String img = snapshot.child("img").getValue(String.class);
                    Double lati = snapshot.child("lati").getValue(Double.class);
                    Double longti= snapshot.child("longti").getValue(Double.class);
                    Double radius = snapshot.child("radius").getValue(Double.class);
                    LatLng location = new LatLng(lati,longti);

                    mClusterManager.addItem(new AnimalAdapter.Location(radius,name,img,location));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
