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

package com.cheda.skysevents.startscreen.util.signincontainer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;


import com.cheda.skysevents.startscreen.AuthUI;
import com.cheda.skysevents.startscreen.ErrorCodes;
import com.cheda.skysevents.startscreen.IdpResponse;
import com.cheda.skysevents.startscreen.ResultCodes;
import com.cheda.skysevents.startscreen.provider.FacebookProvider;
import com.cheda.skysevents.startscreen.provider.GoogleProvider;
import com.cheda.skysevents.startscreen.provider.IdpProvider;
import com.cheda.skysevents.startscreen.provider.ProviderUtils;
import com.cheda.skysevents.startscreen.ui.ExtraConstants;
import com.cheda.skysevents.startscreen.ui.FlowParameters;
import com.cheda.skysevents.startscreen.ui.FragmentBase;
import com.cheda.skysevents.startscreen.ui.FragmentHelper;
import com.cheda.skysevents.startscreen.ui.TaskFailureLogger;
import com.cheda.skysevents.startscreen.ui.User;
import com.cheda.skysevents.startscreen.ui.idp.CredentialSignInHandler;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class IdpSignInContainer extends FragmentBase implements IdpProvider.IdpCallback {
    private static final String TAG = "IDPSignInContainer";
    private static final int RC_WELCOME_BACK_IDP = 4;

    private IdpProvider mIdpProvider;
    @Nullable
    private SaveSmartLock mSaveSmartLock;

    public static void signIn(FragmentActivity activity, FlowParameters parameters, User user) {
        FragmentManager fm = activity.getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(TAG);
        if (!(fragment instanceof IdpSignInContainer)) {
            IdpSignInContainer result = new IdpSignInContainer();

            Bundle bundle = FragmentHelper.getFlowParamsBundle(parameters);
            bundle.putParcelable(ExtraConstants.EXTRA_USER, user);
            result.setArguments(bundle);

            try {
                fm.beginTransaction().add(result, TAG).disallowAddToBackStack().commit();
            } catch (IllegalStateException e) {
                Log.e(TAG, "Cannot add fragment", e);
            }
        }
    }

    public static IdpSignInContainer getInstance(FragmentActivity activity) {
        Fragment fragment = activity.getSupportFragmentManager().findFragmentByTag(TAG);
        if (fragment instanceof IdpSignInContainer) {
            return (IdpSignInContainer) fragment;
        } else {
            return null;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSaveSmartLock = mHelper.getSaveSmartLockInstance(getActivity());

        User user = User.getUser(getArguments());
        String provider = user.getProvider();

        AuthUI.IdpConfig providerConfig = null;
        for (AuthUI.IdpConfig config : mHelper.getFlowParams().providerInfo) {
            if (config.getProviderId().equalsIgnoreCase(provider)) {
                providerConfig = config;
                break;
            }
        }

        if (providerConfig == null) {
            // we don't have a provider to handle this
            finish(ResultCodes.CANCELED, IdpResponse.getErrorCodeIntent(ErrorCodes.UNKNOWN_ERROR));
            return;
        }

        if (provider.equalsIgnoreCase(GoogleAuthProvider.PROVIDER_ID)) {
            mIdpProvider = new GoogleProvider(
                    getActivity(),
                    providerConfig,
                    user.getEmail());
        } else if (provider.equalsIgnoreCase(FacebookAuthProvider.PROVIDER_ID)) {
            mIdpProvider = new FacebookProvider(providerConfig, mHelper.getFlowParams().themeId);
        }

        mIdpProvider.setAuthenticationCallback(this);



        if (savedInstanceState == null) {
            mIdpProvider.startLogin(getActivity());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(ExtraConstants.HAS_EXISTING_INSTANCE, true);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSuccess(final IdpResponse response) {
        AuthCredential credential = ProviderUtils.getAuthCredential(response);
        mHelper.getFirebaseAuth()
                .signInWithCredential(credential)
                .addOnFailureListener(
                        new TaskFailureLogger(TAG, "Failure authenticating with credential " +
                                credential.getProvider()))
                .addOnCompleteListener(new CredentialSignInHandler(
                        getActivity(),
                        mHelper,
                        mSaveSmartLock,
                        RC_WELCOME_BACK_IDP,
                        response));
    }

    @Override
    public void onFailure(Bundle extra) {
        finish(ResultCodes.CANCELED, IdpResponse.getErrorCodeIntent(ErrorCodes.UNKNOWN_ERROR));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_WELCOME_BACK_IDP) {
            finish(resultCode, data);
        } else {
            mIdpProvider.onActivityResult(requestCode, resultCode, data);
        }
    }
}