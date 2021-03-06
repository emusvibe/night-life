package com.cheda.skysevents.startscreen.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;

import com.cheda.skysevents.startscreen.IdpResponse;
import com.cheda.skysevents.startscreen.ResultCodes;
import com.cheda.skysevents.startscreen.util.signincontainer.SaveSmartLock;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.CredentialsApi;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;

import static com.cheda.skysevents.startscreen.util.Preconditions.checkNotNull;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class BaseHelper {
    private final FlowParameters mFlowParams;
    protected Context mContext;
    protected ProgressDialog mProgressDialog;

    public BaseHelper(Context context, FlowParameters parameters) {
        mContext = context;
        mFlowParams = parameters;
    }

    public static Intent createBaseIntent(
            @NonNull Context context,
            @NonNull Class<? extends Activity> target,
            @NonNull FlowParameters flowParams) {
        return new Intent(
                checkNotNull(context, "context cannot be null"),
                checkNotNull(target, "target activity cannot be null"))
                .putExtra(ExtraConstants.EXTRA_FLOW_PARAMS,
                          checkNotNull(flowParams, "flowParams cannot be null"));
    }

    public FlowParameters getFlowParams() {
        return mFlowParams;
    }

    public void finishActivity(Activity activity, int resultCode, Intent intent) {
        activity.setResult(resultCode, intent);
        activity.finish();
    }

    public void showLoadingDialog(String message) {
        dismissDialog();

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setTitle("");
        }

        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    public void showLoadingDialog(@StringRes int stringResource) {
        showLoadingDialog(mContext.getString(stringResource));
    }

    public void dismissDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    public boolean isProgressDialogShowing() {
        return mProgressDialog != null && mProgressDialog.isShowing();
    }

    public FirebaseAuth getFirebaseAuth() {
        return FirebaseAuth.getInstance(FirebaseApp.getInstance(mFlowParams.appName));
    }

    public CredentialsApi getCredentialsApi() {
        return Auth.CredentialsApi;
    }

    public FirebaseUser getCurrentUser() {
        return getFirebaseAuth().getCurrentUser();
    }

    public SaveSmartLock getSaveSmartLockInstance(FragmentActivity activity) {
        return SaveSmartLock.getInstance(activity, getFlowParams());
    }

    public PhoneAuthProvider getPhoneAuthProviderInstance() {
        return PhoneAuthProvider.getInstance();
    }

    public void saveCredentialsOrFinish(
            @Nullable SaveSmartLock saveSmartLock,
            Activity activity,
            FirebaseUser firebaseUser,
            @Nullable String password,
            IdpResponse response) {
        if (saveSmartLock == null) {
            finishActivity(activity, ResultCodes.OK, response.toIntent());
        } else {
            saveSmartLock.saveCredentialsOrFinish(
                    firebaseUser,
                    password,
                    response);
        }
    }
}
