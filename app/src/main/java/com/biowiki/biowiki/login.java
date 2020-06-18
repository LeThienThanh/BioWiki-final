package com.biowiki.biowiki;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class login extends AppCompatActivity implements View.OnClickListener {

    EditText mEmail, mPassword,emailResettxt;
    TextView mForgotPassword;
    Button mLogin, mRegister,resetPassbtn;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListner;
    public static final String TAG = "login";

    String  email, pass;

    ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmail = (EditText) findViewById(R.id.edtEmail);
        mPassword = (EditText) findViewById(R.id.edtPass);
        mForgotPassword = (TextView) findViewById(R.id.txtForgot);
        mLogin = (Button) findViewById(R.id.btnLogin);
        mRegister = (Button) findViewById(R.id.btnReg);

        mDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    Intent intent = new Intent(login.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    Log.d(TAG, "AuthStateChange:LogOut");
                }
            }
        };

        mLogin.setOnClickListener(this);
        mRegister.setOnClickListener(this);
        mForgotPassword.setOnClickListener(this);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(login.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            Toast.makeText(login.this, "Name: " + personName + " ID: " + personId, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListner != null) mAuth.removeAuthStateListener(mAuthListner);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View view) {
        if (view == mLogin) {
            userSign();
        } else if (view == mRegister) {
            startActivity(new Intent(login.this, register.class));
        } else if (view == mForgotPassword) {
            //TODO: LCT make forgot password =)) oke nhows =))
            showDialogResetPass();
        }
    }

    private void showDialogResetPass() {
        Context context;
        final Dialog dialog = new Dialog(login.this);
        dialog.setContentView(R.layout.restpassdialog);

        emailResettxt = dialog.findViewById(R.id.emailResetPass);
        resetPassbtn  = dialog.findViewById(R.id.btnResetPass);

        dialog.show();

        resetPassbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.sendPasswordResetEmail(emailResettxt.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Email sent.");
                                    Toast.makeText(login.this,"Check your email to  reset password !", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                dialog.dismiss();
            }
        });



    }

    private void userSign() {
        email = mEmail.getText().toString().trim();
        pass = mPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(login.this, "Enter Email", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(pass)) {
            Toast.makeText(login.this, "Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }

        mDialog.setMessage("Login please wait...");
        mDialog.setIndeterminate(true);
        mDialog.show();
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    mDialog.dismiss();
                    Toast.makeText(login.this, "Login not Successful", Toast.LENGTH_SHORT).show();
                } else {
                    mDialog.dismiss();
                }
            }
        });
    }
}
