/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cheda.skysevents.startscreen;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.cheda.skysevents.ProfileActivity;
import com.cheda.skysevents.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignedInActivity extends AppCompatActivity {

    private static final String EXTRA_SIGNED_IN_CONFIG = "extra_signed_in_config";
    private int type;
    static final int TYPE_PROGRAMMATICALLY = 0;
    @BindView(android.R.id.content)
    View mRootView;

    @BindView(R.id.user_profile_picture)
    ImageView mUserProfilePicture;

    @BindView(R.id.user_email)
    TextView mUserEmail;

    @BindView(R.id.user_display_name)
    TextView mUserDisplayName;


    @BindView(R.id.user_phone_number)
    TextView mUserPhoneNumber;

    @BindView(R.id.user_enabled_providers)
    TextView mEnabledProviders;

    private IdpResponse mIdpResponse;

    private SignedInConfig mSignedInConfig;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setupWindowAnimations();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(LoginReg.createIntent(this));
            finish();
            return;
        }

        mIdpResponse = IdpResponse.fromResultIntent(getIntent());
        mSignedInConfig = getIntent().getParcelableExtra(EXTRA_SIGNED_IN_CONFIG);

        setContentView(R.layout.signed_in_layout);
        ButterKnife.bind(this);
        populateProfile();
        populateIdpToken();

    }


    private void setupWindowAnimations() {
        Slide enterTransition = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            enterTransition = new Slide();
            enterTransition.setSlideEdge(Gravity.LEFT);
            enterTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
            getWindow().setEnterTransition(enterTransition);
            getWindow().setExitTransition(enterTransition);
//            getWindow().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            getWindow().setReenterTransition(enterTransition);

            getWindow().setAllowEnterTransitionOverlap(false);
      }

    }



    @OnClick(R.id.sign_out)
    public void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            startActivity(LoginReg.createIntent(SignedInActivity.this));
                            finish();
                        } else {
                            showSnackbar(R.string.sign_out_failed);
                        }
                    }
                });
    }

    @OnClick(R.id.delete_account)
    public void deleteAccountClicked() {

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to delete this account?")
                .setPositiveButton("Yes, nuke it!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteAccount();
                    }
                })
                .setNegativeButton("No", null)
                .create();

        dialog.show();
    }

    private void deleteAccount() {
        AuthUI.getInstance()
                .delete(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            startActivity(LoginReg.createIntent(SignedInActivity.this));
                            finish();
                        } else {
                            showSnackbar(R.string.delete_account_failed);
                        }
                    }
                });
    }

    @MainThread
    private void populateProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user.getPhotoUrl() != null) {
            Picasso.with(this)
                    .load(user.getPhotoUrl())
                    .fit()
                    .placeholder(R.drawable.ic_avatr)
                    .into(mUserProfilePicture);
        }

        mUserEmail.setText(
                TextUtils.isEmpty(user.getEmail()) ? "No email" : user.getEmail());
        mUserPhoneNumber.setText(
                TextUtils.isEmpty(user.getPhoneNumber()) ? "No phone number" : user.getPhoneNumber());
        mUserDisplayName.setText(
                TextUtils.isEmpty(user.getDisplayName()) ? "No display name" : user.getDisplayName());

        StringBuilder providerList = new StringBuilder(100);

        providerList.append("Providers used: ");

        if (user.getProviders() == null || user.getProviders().isEmpty()) {
            providerList.append("none");
        } else {
            Iterator<String> providerIter = user.getProviders().iterator();
            while (providerIter.hasNext()) {
                String provider = providerIter.next();
                if (GoogleAuthProvider.PROVIDER_ID.equals(provider)) {
                    providerList.append("Google");
                } else if (FacebookAuthProvider.PROVIDER_ID.equals(provider)) {
                    providerList.append("Facebook");
                } else if (EmailAuthProvider.PROVIDER_ID.equals(provider)) {
                    providerList.append("Company Email");
                } else {
                    providerList.append(provider);
                }

                if (providerIter.hasNext()) {
                    providerList.append(", ");
                }
            }
        }

        mEnabledProviders.setText(providerList);
    }

    private void populateIdpToken() {
        String token = null;
        String secret = null;
        if (mIdpResponse != null) {
            token = mIdpResponse.getIdpToken();
            secret = mIdpResponse.getIdpSecret();
        }
        View idpTokenLayout = findViewById(R.id.idp_token_layout);
        if (token == null) {
            idpTokenLayout.setVisibility(View.GONE);
        } else {
            idpTokenLayout.setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.idp_token)).setText(token);
        }
        View idpSecretLayout = findViewById(R.id.idp_secret_layout);
        if (secret == null) {
            idpSecretLayout.setVisibility(View.GONE);
        } else {
            idpSecretLayout.setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.idp_secret)).setText(secret);
        }
    }

    @MainThread
    private void showSnackbar(@StringRes int errorMessageRes) {

//        Toast.makeText(this, errorMessageRes, Toast.LENGTH_SHORT).show();
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_LONG).show();
    }

    public static final class SignedInConfig implements Parcelable {
        int logo;
//        int theme;
        List<AuthUI.IdpConfig> providerInfo;
        String tosUrl;
//        boolean isCredentialSelectorEnabled;
//        boolean isHintSelectorEnabled;

        SignedInConfig(int logo,
                       List<AuthUI.IdpConfig> providerInfo,
                       String tosUrl) {
            this.logo = logo;
            this.providerInfo = providerInfo;
            this.tosUrl = tosUrl;
        }

        SignedInConfig(Parcel in) {
            logo = in.readInt();
//            theme = in.readInt();
            providerInfo = new ArrayList<>();
            in.readList(providerInfo, AuthUI.IdpConfig.class.getClassLoader());
            tosUrl = in.readString();
//            isCredentialSelectorEnabled = in.readInt() != 0;
//            isHintSelectorEnabled = in.readInt() != 0;
        }

        public static final Creator<SignedInConfig> CREATOR = new Creator<SignedInConfig>() {
            @Override
            public SignedInConfig createFromParcel(Parcel in) {
                return new SignedInConfig(in);
            }

            @Override
            public SignedInConfig[] newArray(int size) {
                return new SignedInConfig[size];
            }
        };


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(logo);
//            dest.writeInt(theme);
            dest.writeList(providerInfo);
            dest.writeString(tosUrl);
//            dest.writeInt(isCredentialSelectorEnabled ? 1 : 0);
//            dest.writeInt(isHintSelectorEnabled ? 1 : 0);
        }
    }

    public static Intent createIntent(
            Context context,
            IdpResponse idpResponse,
            SignedInConfig signedInConfig) {
        Intent startIntent = idpResponse == null ? new Intent() : idpResponse.toIntent();

        return startIntent.setClass(context, SignedInActivity.class)
                .putExtra(EXTRA_SIGNED_IN_CONFIG, signedInConfig);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent bck = new Intent(this, ProfileActivity.class);
        Slide slideTransition = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            slideTransition = new Slide();
            slideTransition.setSlideEdge(Gravity.RIGHT);
            slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
            getWindow().setReenterTransition(slideTransition);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                finishAfterTransition();
//            }
        }

        startActivity(bck);


    }
}
