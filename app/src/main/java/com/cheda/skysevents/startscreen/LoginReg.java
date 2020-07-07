package com.cheda.skysevents.startscreen;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import com.cheda.skysevents.MainActivity;
import com.cheda.skysevents.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appinvite.FirebaseAppInvite;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginReg extends AppCompatActivity {
    private static final String UNCHANGED_CONFIG_VALUE = "CHANGE-ME";
    private static final String GOOGLE_TOS_URL = "https://www.google.com/policies/terms/";
    private static final String FIREBASE_TOS_URL = "https://firebase.google.com/terms/";
    private static final String GOOGLE_PRIVACY_POLICY_URL = "https://www.google.com/policies/privacy/";
    private static final String FIREBASE_PRIVACY_POLICY_URL = "https://firebase.google.com/terms/analytics/#7_privacy";
    private static final int RC_SIGN_IN = 100;
    private static final String TAG = LoginReg.class.getSimpleName();
    @BindView(R.id.email_provider)
    CheckBox mUseEmailProvider;

    @BindView(R.id.phone_provider)
    CheckBox mUsePhoneProvider;

    @BindView(R.id.google_provider)
    CheckBox mUseGoogleProvider;

    @BindView(R.id.facebook_provider)
    CheckBox mUseFacebookProvider;

    @BindView(R.id.sign_in)
    Button mSignIn;

    @BindView(R.id.root)
    View mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        FirebaseApp.initializeApp(this);
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        if (auth.getCurrentUser() != null) {
//            startSignedInActivity(null);
//            finish();
//        }
//        setContentView(R.layout.activity_login_reg);
//        if (FirebaseApp.initializeApp(this) != null){

            FirebaseAuth auth = FirebaseAuth.getInstance();
            if (auth.getCurrentUser() != null) {
                startSignedInActivity(null);
                finish();

            }
//            else {
//
//                startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
////                        .setTheme(getSelectedTheme())
//                                .setLogo(getSelectedLogo())
//                                .setAvailableProviders(getSelectedProviders())
//                                .setTosUrl(getSelectedTosUrl())
//                                .setPrivacyPolicyUrl(getSelectedPrivacyPolicyUrl())
//                                .setSmartLockEnabled(true)
//                                .setAllowNewEmailAccounts(true)
//                                .build(),
//                        RC_SIGN_IN);
//
//            }

        setContentView(R.layout.activity_login_reg);
        ButterKnife.bind(this);

        if (!isGoogleConfigured()) {
            mUseGoogleProvider.setChecked(false);
            mUseGoogleProvider.setEnabled(false);
            mUseGoogleProvider.setText(R.string.google_label_missing_config);

        } else {
//            setGoogleScopesEnabled(mUseGoogleProvider.isChecked());
            mUseGoogleProvider.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
//                    setGoogleScopesEnabled(checked);
                }
            });
        }

        if (!isFacebookConfigured()) {
            mUseFacebookProvider.setChecked(false);
            mUseFacebookProvider.setEnabled(false);
            mUseFacebookProvider.setText(R.string.facebook_label_missing_config);
//            setFacebookScopesEnabled(false);
        } else {
//            setFacebookScopesEnabled(mUseFacebookProvider.isChecked());
            mUseFacebookProvider.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
//                    setFacebookScopesEnabled(checked);
                }
            });
        }

        if (!isGoogleConfigured() || !isFacebookConfigured()) {
            showSnackbar(R.string.configuration_required);
        }



        }

    @OnClick(R.id.sign_in)
    public void signIn(View view) {

        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
//                        .setTheme(getSelectedTheme())
                        .setLogo(getSelectedLogo())
                        .setAvailableProviders(getSelectedProviders())
                        .setTosUrl(getSelectedTosUrl())
                        .setPrivacyPolicyUrl(getSelectedPrivacyPolicyUrl())
                        .setSmartLockEnabled(true)
                        .setAllowNewEmailAccounts(true)
                        .build(),
                RC_SIGN_IN);
        }

   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            handleSignInResponse(resultCode, data);
            return;
        }

        showSnackbar(R.string.unknown_response);
    }

    @MainThread
    private void handleSignInResponse(int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);

        // Successfully signed in
        if (resultCode == ResultCodes.OK) {

            startSignedInActivity(response);
            finish();
            return;
        } else {
            // Sign in failed
            if (response == null) {
                // User pressed back button
                showSnackbar(R.string.sign_in_cancelled);
                return;
            }

            if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                showSnackbar(R.string.no_internet_connection);
                return;
            }

            if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                showSnackbar(R.string.unknown_error);
                return;
            }
        }

        showSnackbar(R.string.unknown_sign_in_response);
    }


    private void startSignedInActivity(IdpResponse response) {

       Intent go = new Intent(this, MainActivity.class);
        startActivity(go);
//        startActivity(
//                SignedInActivity.createIntent(
//                        this,
//                        response,
//                        new SignedInActivity.SignedInConfig(
//                                getSelectedLogo(),
//                                getSelectedProviders(),
//                                getSelectedTosUrl())));
    }

    @MainThread
    @DrawableRes
    public int getSelectedLogo() {

        return R.drawable.sky_logo2;
//        return R.drawable.logo_googleg_color_144dp;

    }


    @MainThread
    public List<AuthUI.IdpConfig> getSelectedProviders() {
        List<AuthUI.IdpConfig> selectedProviders = new ArrayList<>();


        if (mUseGoogleProvider.isChecked()) {
            selectedProviders.add(
                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER)
                            .build());
        }

        if (mUseFacebookProvider.isChecked()) {
            selectedProviders.add(
                    new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER)
                            .build());
        }

        if (mUseEmailProvider.isChecked()) {
            selectedProviders.add(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build());
        }

        if (mUsePhoneProvider.isChecked()) {
            selectedProviders.add(
                    new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build());
        }

        return selectedProviders;
    }

    @MainThread
    public  String getSelectedTosUrl() {
       return GOOGLE_TOS_URL;
//       return FIREBASE_TOS_URL;
    }

    @MainThread
    private String getSelectedPrivacyPolicyUrl() {
            return GOOGLE_PRIVACY_POLICY_URL;
//          return FIREBASE_PRIVACY_POLICY_URL;
    }

    @MainThread
    private boolean isGoogleConfigured() {
        return !UNCHANGED_CONFIG_VALUE.equals(
                getString(R.string.default_web_client_id));
    }

    @MainThread
    private boolean isFacebookConfigured() {
        return !UNCHANGED_CONFIG_VALUE.equals(
                getString(R.string.facebook_app_id));
    }

    @MainThread
    private void showSnackbar(@StringRes int errorMessageRes) {
//        Toast.makeText(this, errorMessageRes, Toast.LENGTH_SHORT).show();
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_LONG).show();
    }



    public static Intent createIntent(Context context) {
        Intent in = new Intent();
        in.setClass(context, LoginReg.class);
        return in;
    }


}

















































