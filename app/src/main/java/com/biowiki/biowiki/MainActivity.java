package com.biowiki.biowiki;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.biowiki.biowiki.Fragment.NewsFragment;
import com.biowiki.biowiki.Fragment.SearchARFragment;
import com.biowiki.biowiki.Fragment.UploadFragment;
import com.biowiki.biowiki.Models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

import static com.biowiki.biowiki.ApplicationContextProvider.getContext;

public class MainActivity extends AppCompatActivity {

    private static final int AVATAR_GALLERY = 3;

    FirebaseAuth mAuth;
    DatabaseReference mref;
    StorageReference msto;

    private DrawerLayout drawerLayout;

    public String userid;
    ImageView avatar;
    TextView txtCoin, txtName, txtEmail;
    ProgressDialog progressDialog;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mref = FirebaseDatabase.getInstance().getReference();
        msto = FirebaseStorage.getInstance().getReference();

        if (!mAuth.getCurrentUser().isEmailVerified()) {
            Toast.makeText(MainActivity.this, "Your Email not verified", Toast.LENGTH_SHORT).show();
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, login.class));
            finish();
        }

        userid = mAuth.getCurrentUser().getUid();

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.openDrawer(GravityCompat.START);

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headView = navigationView.getHeaderView(0);

        avatar = headView.findViewById(R.id.avatar);
        txtCoin =  headView.findViewById(R.id.bioCoin);
        txtName = headView.findViewById(R.id.userName);
        txtEmail = headView.findViewById(R.id.userEmail);

        getdata();

        loadFragment(new NewsFragment());

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            menuItem.setChecked(true);

            Fragment fragment = null;
            switch (menuItem.getItemId()) {
                case R.id.navigation_news:
                    fragment = new NewsFragment();
                    break;
                case R.id.navigation_info:
                    fragment = new Data();
                    break;
                case R.id.navigation_Map:
                    fragment = new MapsFragment();
                    break;
                case R.id.navigation_forum:
                    fragment = new UploadFragment();
                    break;
                case R.id.navigation_ar:
                    fragment = new SearchARFragment();
                    break;
                case R.id.logout:
                    signout();
                    break;

            }
            drawerLayout.closeDrawers();

            return loadFragment(fragment);
        });

        avatar.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, AVATAR_GALLERY);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AVATAR_GALLERY && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {

                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMax(100);
                progressDialog.setMessage("Uploading...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.show();
                progressDialog.setCancelable(false);

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(MainActivity.this).getContentResolver(), imageUri);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imagedata = baos.toByteArray();
                final StorageReference filepath = msto.child("Avatar").child(imageUri.getLastPathSegment());

                filepath.putBytes(imagedata).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
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
                                String dwl = uri.toString();
                                mref.child("Users").child(userid).child("image").setValue(dwl);
                                Picasso.get().load(dwl).into(avatar);
                                Toast.makeText(MainActivity.this,"Upload done", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,"Upload fail", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });

            } catch (IOException e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setTitle("Exit Application?");
        alertDialogBuilder
                .setMessage("Click yes to exit!")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void getdata(){
        mref.child("Users").child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);

                    txtCoin.setText(String.valueOf(user.getCoin()));
                    txtEmail.setText(user.getEmail());
                    txtName.setText(user.getName());
                    Picasso.get().load(user.getImage()).into(avatar);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void signout(){
        Toast.makeText(MainActivity.this, "Sign out", Toast.LENGTH_LONG).show();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setTitle("Do you want to Sign Out ?");
        alertDialogBuilder
                .setMessage("Click yes to Sign Out!")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mAuth.signOut();
                                startActivity(new Intent(MainActivity.this, login.class));
                                MainActivity.this.finish();
                            }
                        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
