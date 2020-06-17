//package com.biowiki.biowiki;
//
//import android.Manifest;
//import android.app.Dialog;
//import android.content.pm.PackageManager;
//import android.graphics.Color;
//import android.location.Address;
//import android.location.Geocoder;
//import android.location.Location;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.WindowManager;
//import android.view.inputmethod.EditorInfo;
//import android.widget.AdapterView;
//import android.widget.AutoCompleteTextView;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.common.api.PendingResult;
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.location.places.AutocompletePrediction;
//import com.google.android.gms.location.places.PlaceBuffer;
//import com.google.android.gms.location.places.Places;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.Circle;
//import com.google.android.gms.maps.model.CircleOptions;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.LatLngBounds;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.gms.tasks.OnSuccessListener;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class Mapsupload extends AppCompatActivity implements GoogleMap.OnMarkerClickListener,OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMapLongClickListener {
//    ImageView ic_gps, ic_uploca;
//    private GoogleMap mMap;
//    private static final String TAG = "Mapsupload";
//    private FusedLocationProviderClient mFusedLocationClient;
//    AutoCompleteTextView  mSearchText;
//    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
//    private GoogleApiClient mGoogleApiClient;
//    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
//            new LatLng(-40, -168), new LatLng(71, 136));
//    private static final float DEFAULT_ZOOM = 15f;
//    private Marker mMarker;
//    Circle circle;
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.mapupdate);
//        ic_gps = findViewById(R.id.ic_gpsup);
//        ic_uploca = findViewById(R.id.ic_uploca);
//        mSearchText = findViewById(R.id.searchloca);
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        ic_gps.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getlocation();
//            }
//        });
//        mGoogleApiClient = new GoogleApiClient
//                .Builder(this)
//                .addApi(Places.GEO_DATA_API)
//                .addApi(Places.PLACE_DETECTION_API)
//                .enableAutoManage(this, this)
//                .build();
//
//        ic_uploca.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient,
//                LAT_LNG_BOUNDS, null);
//        mSearchText.setAdapter(mPlaceAutocompleteAdapter);
//        mSearchText.setOnItemClickListener(mAutocompleteClickListener);
//        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
//                if(actionId == EditorInfo.IME_ACTION_SEARCH
//                        || actionId == EditorInfo.IME_ACTION_DONE
//                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
//                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){
//
//                    //execute our method for searching
//                    geoLocate();
//                }
//
//                return false;
//            }
//        });
//
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.Maps);
//        mapFragment.getMapAsync(this);
//    }
//    private void geoLocate(){
//        Log.d(TAG, "geoLocate: geolocating");
//
//        String searchString = mSearchText.getText().toString();
//        List<Address> list = new ArrayList<>();
//        if (mSearchText !=null || !mSearchText.equals("")){
//            Geocoder geocoder = new Geocoder(Mapsupload.this);
//            try{
//                list = geocoder.getFromLocationName(searchString, 1);
//            }catch (IOException e){
//                Log.e(TAG, "geoLocate: IOException: " + e.getMessage() );
//            }
//        }
//
//        if(list.size() > 0){
//            Address address = list.get(0);
//            Log.d(TAG, "geoLocate: found a location: " + address.toString());
//            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();
//            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM,
//                    address.getAddressLine(0));
//        }
//    }
//
//    private void getlocation() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        mFusedLocationClient.getLastLocation()
//                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        // Got last known location. In some rare situations this can be null.
//                        if (location != null) {
//                            // Logic to handle location object
//                            moveCamera(new LatLng(location.getLatitude(), location.getLongitude()), 15f, "My Location");
//                        } else {
//                            Toast.makeText(Mapsupload.this, "Please turn on your GPS", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
//
//    private void moveCamera(LatLng latLng, float zoom, String title) {
//        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
//
//        if (!title.equals("My Location")) {
//            MarkerOptions options = new MarkerOptions()
//                    .position(latLng)
//                    .title(title);
//            mMarker = mMap.addMarker(options);
//        }
//    }
//    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//            hideSoftKeyboard();
//
//            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(i);
//            final String placeId = item.getPlaceId();
//
//            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
//                    .getPlaceById(mGoogleApiClient, placeId);
//            //placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
//        }
//    };
//    private void hideSoftKeyboard(){
//        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//    }
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        mMap.setOnMapLongClickListener(this);
//        mMap.setMyLocationEnabled(true);
//        mMap.setOnMarkerClickListener(this);
//        mMap.getUiSettings().setMyLocationButtonEnabled(false);
//    }
//
//    @Override
//    public void onMapLongClick(LatLng latLng) {
////        MarkerOptions options = new MarkerOptions()
////                    .position(latLng)
////                    .title("Place you choose");
////            mMap.addMarker(options);
//        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
//        mMap.clear();
//        mMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("your choosen place"));
//    }
//
//    @Override
//    public boolean onMarkerClick(Marker marker) {
//        showdialog();
//        return false;
//    }
//
//    public void showdialog() {
//        final Dialog dialog= new Dialog(Mapsupload.this);
//        //dialog.setTitle("Enter radius");
//        dialog.setContentView(R.layout.circledialog);
//        Button okbtn = dialog.findViewById(R.id.okbtn);
//        final EditText radiustext = dialog.findViewById(R.id.radius);
//        okbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!TextUtils.isEmpty(radiustext.getText().toString())) {
//                        if ( circle != null) {
//                            circle.remove();
//                        }
//                        int norad= Integer.parseInt(radiustext.getText().toString());
//                        int fillcolorcircle = getResources().getColor(R.color.fillcircle);
//                        circle = mMap.addCircle(new CircleOptions().center(mMarker.getPosition()).radius(norad).strokeColor(Color.RED).fillColor(fillcolorcircle));
//                        dialog.cancel();
//                } else {
//                    Toast.makeText(Mapsupload.this, "Please enter radius", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        dialog.show();
//    }
//
//}
