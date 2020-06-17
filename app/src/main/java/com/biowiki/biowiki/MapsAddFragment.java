package com.biowiki.biowiki;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_OK;

public class MapsAddFragment extends BaseMaps implements GoogleMap.OnMarkerClickListener,GoogleMap.OnMapLongClickListener {
    DatabaseReference mreference;
    StorageReference mstorage;

    Circle circle;

    ImageView imgcircle;
    EditText namecircle;

    Uri mImageUri;

    String nameloca,uri;
    static final int GAlLERY_INTENT = 2;
    LatLng locationup;
    Integer radius;

    ProgressDialog progressDialog,mProgres;
    @Override
    protected void startDemo() {
        check=false;

        mstorage = FirebaseStorage.getInstance().getReference();
        mreference = FirebaseDatabase.getInstance().getReference();

        ic_add.setImageResource(R.drawable.ic_tick);
        ic_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (circle!= null && mImageUri!=null && check==false){
//                    Intent intent = new Intent(getActivity(),MainActivity.class);
//                    intent.putExtra("nameuploca", namecircle.getText().toString());
//                    intent.putExtra("uri", mImageUri.toString());
//                    intent.putExtra("locationup", mMarker.getPosition());
//                    intent.putExtra("radius", circle.getRadius());
                    nameloca = namecircle.getText().toString();
                    uri = mImageUri.toString();
                    locationup = mMarker.getPosition();

                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMax(100);
                    progressDialog.setMessage("Uploading...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.show();
                    progressDialog.setCancelable(false);

                    imgcircle.setDrawingCacheEnabled(true);
                    imgcircle.buildDrawingCache();

                    Bitmap bitmap = imgcircle.getDrawingCache();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                    byte[] data = byteArrayOutputStream.toByteArray();

                    final Double radius = Double.parseDouble(String.valueOf(circle.getRadius()));

                    final StorageReference filepath = mstorage.child("imgloca/").child(mImageUri.getLastPathSegment());
                    filepath.putBytes(data).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressDialog.incrementProgressBy((int) progress);

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                         filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                             @Override
                             public void onSuccess(Uri uri) {
                             String dwl =uri.toString();

                             AnimalAdapter.Locationadd location = new AnimalAdapter.Locationadd(locationup.latitude,locationup.longitude,radius,nameloca,dwl);
                             mreference.child("Location").push().setValue(location);

                             Toast.makeText(getActivity(),"Upload done", Toast.LENGTH_SHORT).show();
                             changefragment();
                             progressDialog.dismiss();

                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(),"Upload fail", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }
                    });

                } else {
                    Toast.makeText(getActivity(),"Null", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        mMap.clear();
        mMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("your choosen place"));

        showdialog();

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    public void changefragment(){
        MapsFragment fragment=new MapsFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
    public void showdialog() {
        final Dialog dialog= new Dialog(getActivity());

        dialog.setContentView(R.layout.circledialog);
        mProgres = new ProgressDialog(getActivity());
        Button okbtn = dialog.findViewById(R.id.okbtn);
        Button canclebtn = dialog.findViewById(R.id.cancel);

        imgcircle =dialog.findViewById(R.id.imgcircle);
        namecircle= dialog.findViewById(R.id.namecircle);

        final EditText radiustext = dialog.findViewById(R.id.radius);

        imgcircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                //intent.setAction(Intent.ACTION_GET_CONTENT);
                //startActivityForResult(intent, PICK_IMAGE_REQUEST);
                startActivityForResult(intent, GAlLERY_INTENT);

            }
        });

        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(radiustext.getText().toString())) {
                    if ( circle != null)  circle.remove();

                    int norad= Integer.parseInt(radiustext.getText().toString());
                    int fillcolorcircle = getResources().getColor(R.color.fillcircle);

                    circle = mMap.addCircle(new CircleOptions().center(mMarker.getPosition()).radius(norad).strokeColor(Color.RED).fillColor(fillcolorcircle));
                    dialog.cancel();

                } else {
                    Toast.makeText(getActivity(), "Please enter radius", Toast.LENGTH_SHORT).show();
                }
            }
        });

        canclebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { dialog.dismiss(); }
        });

        dialog.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GAlLERY_INTENT && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mProgres.setMessage("Uploadidng....");
            mImageUri = data.getData();

            Picasso.get().load(mImageUri.toString()).into(imgcircle);

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onCameraMove() {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
