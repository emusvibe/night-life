//package com.cheda.skysevents.startscreen;
//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.cheda.skysevents.MainActivity;
//import com.cheda.skysevents.R;
//import com.facebook.AccessToken;
//import com.facebook.CallbackManager;
//import com.facebook.FacebookCallback;
//import com.facebook.FacebookException;
//import com.facebook.FacebookSdk;
//import com.facebook.appevents.AppEventsLogger;
//import com.facebook.login.LoginManager;
//import com.facebook.login.LoginResult;
//import com.facebook.login.widget.LoginButton;
//import com.google.android.gms.auth.api.Auth;
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.android.gms.auth.api.signin.GoogleSignInResult;
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.common.api.ResultCallback;
//import com.google.android.gms.common.api.Status;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthCredential;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FacebookAuthProvider;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.GoogleAuthProvider;
//
//
///**
// * Created by Cheda on 2017-04-17.
// */
//
//public class LoginRegister extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,View.OnClickListener {
//    public static String PREFS_NAME ;
//    private static final String TAG = "GoogleActivity";
//    private static final int RC_SIGN_IN = 9010;
//    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
//    private FirebaseAuth mAuth;
//    private FirebaseAuth.AuthStateListener mAuthListener;
//
//    private GoogleApiClient googleApiClient;
//    private ImageView profileImageView;
//    private TextView greetingTextView;
//    private ProgressDialog mProgressDialog;
//    CallbackManager mCallbackManager;
//    private LoginButton fb_btn;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.login_register);
//
//// Initialize Phone and Email Login
////        findViewById(R.id.btn_newProfile).setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Intent signup = new Intent(LoginRegister.this, EmailPhoneSignUp.class);
////                startActivity(signup);
////            }
////        });
//
//        findViewById(R.id.btn_Google).setOnClickListener(this);
//
//        profileImageView = (ImageView) findViewById(R.id.imageview_profile);
//        greetingTextView = (TextView) findViewById(R.id.textview_greeting);
//// Initialize Facebook Login button
//        mCallbackManager = CallbackManager.Factory.create();
//        fb_btn = (LoginButton) findViewById(R.id.btn_facebook);
//        fb_btn.setReadPermissions("email", "public_profile");
//
//        GoogleSignInOptions gSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//
//        googleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this, this)
//                .addApi(Auth.GOOGLE_SIGN_IN_API,gSignInOptions)
//                .build();
//
//        mAuth = FirebaseAuth.getInstance();
//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null){
//
//
//
//
//                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
//                }else {
//
//                    Log.d(TAG, "onAuthStateChanged:signed _out");
//                }
//
//                updateUI(user);
//
//            }
//        };
//
//        fb_btn.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                handleSignInResult(loginResult.getAccessToken());
////
//            }
//
//            @Override
//            public void onCancel() {
//                Toast.makeText(LoginRegister.this, "Login Cancelled" ,Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//                Toast.makeText(LoginRegister.this, "Login Error:" + error.getMessage() ,Toast.LENGTH_SHORT).show();
//            }
//        });
//        mAuth = FirebaseAuth.getInstance();
//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//              FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null){
//
//                    Toast.makeText(LoginRegister.this, "Login Success" , Toast.LENGTH_SHORT).show();  //+result.getStatus()
//                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                }
//            }
//        };
//    }
//
//
//    private void handleSignInResult(AccessToken accessToken) {
//        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
//      mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//          @Override
//          public void onComplete(@NonNull Task<AuthResult> task) {
//              if (task.isSuccessful()) {
//                 FirebaseUser user = mAuth.getCurrentUser();
//                  updateUI(user);
//
//              }else {
//
//                  Log.d(TAG, "signInWithCredential", task.getException());
//                  Toast.makeText(LoginRegister.this, "Facebbok Authantication failed.", Toast.LENGTH_SHORT).show();
//
//              }
//          }
//      });
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        mAuth.addAuthStateListener(mAuthListener);
////        FirebaseUser currentUser = mAuth.getCurrentUser();
////        updateUI(currentUser);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (mAuthListener != null) {
//            mAuth.removeAuthStateListener(mAuthListener);
//
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        mCallbackManager.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RC_SIGN_IN){
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            if (result.isSuccess()){
//                GoogleSignInAccount account = result.getSignInAccount();
//                firebaseAuthwithGoogle(account);
//                Toast.makeText(LoginRegister.this, "Login Success" , Toast.LENGTH_SHORT).show();  //+result.getStatus()
//                startActivity(new Intent(getApplicationContext(), MainActivity.class));
//            }else {
//
//                updateUI(null);
//            }
//        }
//
//    }
//
//    private void firebaseAuthwithGoogle(GoogleSignInAccount acct){
//        Log.d(TAG,"firebaseAuthGoogle:" +acct.getId());
//        showProgressDialog();
//
//        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//
//                        Log.d(TAG, "signInwithCredential:onComplete:" + task.isSuccessful());
//
//                    if (!task.isSuccessful()){
//                        Log.d(TAG, "signInWithCredential", task.getException());
//                        Toast.makeText(LoginRegister.this, "Authantication failed.", Toast.LENGTH_SHORT).show();
//
//                    }
//                    hideProgressDialog();
//
//                    }
//                });
//
//    }
//
//    private void signIn(){
//        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }
//
//    public void signOut(){
//        mAuth.signOut();
//        mAuth.getInstance().signOut();
//        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
//                new ResultCallback<Status>() {
//                    @Override
//                    public void onResult(@NonNull Status status) {
//                        updateUI(null);
//                    }
//                }
//        );
//    }
//
//    private void revokeAccess(){
//        mAuth.signOut();
//
//        Auth.GoogleSignInApi.revokeAccess(googleApiClient).setResultCallback(new ResultCallback<Status>() {
//            @Override
//            public void onResult(@NonNull Status status) {
//                updateUI(null);
//            }
//        });
//
//    }
//
//    private void updateUI(FirebaseUser user){
//        hideProgressDialog();
//        if (user != null){
////            greetingTextView.setText(String.format("Welcome, %s", user.getUid()));
////            profileImageView.setImageResource(user.getPhotoUrl(),null);
//        }
//
//    }
//
//
//    @Override
//    public void onClick(View v) {
//        signIn();
//
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//        Log.d(TAG, "onConnection" + connectionResult);
//        Toast.makeText(this, "Google Play Services error", Toast.LENGTH_SHORT).show();
//    }
//
//    private void showProgressDialog(){
//        if (mProgressDialog == null){
//
//            mProgressDialog = new ProgressDialog(this);
//            mProgressDialog.setMessage("loading ");  //getString(R.string.)
//            mProgressDialog.setIndeterminate(true);
//        }
//
//        mProgressDialog.show();
//    }
//
//    private void hideProgressDialog(){
//        if (mProgressDialog != null && mProgressDialog.isShowing()){
//            mProgressDialog.hide();
//        }
//    }
//
//
//
//
//}
//
