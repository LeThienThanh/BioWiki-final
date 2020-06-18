package com.biowiki.biowiki;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.biowiki.biowiki.Adapter.PlaceAutocompleteAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class BaseMaps extends Fragment implements GoogleMap.OnCameraMoveListener,OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener{
    private FusedLocationProviderClient mFusedLocationClient;
    GoogleMap mMap;
    private static final String TAG = "BaseMaps";

    AutoCompleteTextView  mSearchText;

    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;

    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    private static final float DEFAULT_ZOOM = 15f;
    Marker mMarker;
    boolean check = true;
    ImageView ic_add,ic_gps,ic_marker;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_maps, container, false);

        ic_gps = v.findViewById(R.id.ic_gpsshow);
        ic_marker = v.findViewById(R.id.ic_marker);
        ic_add = v.findViewById(R.id.ic_add);

        mSearchText = v.findViewById(R.id.searchlocainmaps);
        mSearchText.setOnItemClickListener(mAutocompleteClickListener);

        if ( mGoogleApiClient!= null && mGoogleApiClient.isConnected()) {
        mGoogleApiClient.disconnect();

        mGoogleApiClient = new GoogleApiClient
                    .Builder(Objects.requireNonNull(getActivity()))
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .enableAutoManage(getActivity(), this)
                    .build();

        }

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter( getActivity(), mGoogleApiClient,
                LAT_LNG_BOUNDS, null);

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){
                    geoLocate();
                }

                return false;
            }
        });

        mSearchText.setAdapter(mPlaceAutocompleteAdapter);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getActivity()));

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.Mapsfrag);

        if (mapFragment == null){
            FragmentManager fm= getFragmentManager();
            assert fm != null;
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.Mapsfrag,mapFragment).commit();
        }

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
            mMap= googleMap;

            mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),
                                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                        {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION_REQUEST_CODE);
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},LOCATION_PERMISSION_REQUEST_CODE);

                            return;

                        } else {
                            mMap.setMyLocationEnabled(true);
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                            startDemo();
                        }

                    }
                });
            }
        });

        ic_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] permissions = {android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION};
                if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()).getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                    getlocation();
                } else {
                    ActivityCompat.requestPermissions(getActivity(),
                            permissions,LOCATION_PERMISSION_REQUEST_CODE
                    );
                }

            }
        });
        ic_marker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMarker != null) {
                    moveCamera(mMarker.getPosition(),15f,"Your marker here");
                }
            }
        });

        ic_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check) {
                    // đổi từ Map hiển thị vị trí sang Map nhập vị trí
                    MapsAddFragment fragment=new MapsAddFragment();
                    assert getFragmentManager() != null;

                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.content_frame, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();

                } else {
                   ic_add.setImageResource(R.drawable.ic_add);
                }
            }
        });
        return v;
    }

    private void moveCamera(LatLng latLng, float zoom, String title) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if (!title.equals("My Location")) { MarkerOptions options = new MarkerOptions().position(latLng).title(title); }

    }
    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(i);
            assert item != null;
            final String placeId = item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);

        }
    };

    private void getlocation() {
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            moveCamera(new LatLng(location.getLatitude(), location.getLongitude()), 15f, "My Location");
                        } else {
                            Toast.makeText(getActivity(), "Please turn on your GPS", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void geoLocate(){
        Log.d(TAG, "geoLocate: geolocating");

        String searchString = mSearchText.getText().toString();
        List<Address> list = new ArrayList<>();
        if (mSearchText !=null || !mSearchText.equals("")){
            Geocoder geocoder = new Geocoder(getActivity());
            try{
                list = geocoder.getFromLocationName(searchString, 1);

            }catch (IOException e){
                Log.e(TAG, "geoLocate: IOException: " + e.getMessage() );

            }}

        if(list.size() > 0){
            Address address = list.get(0);
            Log.d(TAG, "geoLocate: found a location: " + address.toString());
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();

            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM,
                    address.getAddressLine(0));

        }
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        startDemo();

    }

    @Override
    public void onCameraMove() {
    }

    protected abstract void startDemo();
}
