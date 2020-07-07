package com.cheda.skysevents.utils.ui.provider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;


import com.cheda.skysevents.R;
import com.cheda.skysevents.utils.ui.AuthUI;
import com.cheda.skysevents.utils.ui.BaseHelper;
import com.cheda.skysevents.utils.ui.ResultCodes;
import com.cheda.skysevents.utils.ui.email.RegisterEmailActivity;
import com.google.firebase.auth.EmailAuthProvider;

public class EmailProvider implements Provider {
    private static final int RC_EMAIL_FLOW = 2;

    private Activity mActivity;
    private BaseHelper mHelper;

    public EmailProvider(Activity activity, BaseHelper helper) {
        mActivity = activity;
        mHelper = helper;
    }

    @Override
    public String getName(Context context) {
        return context.getString(R.string.provider_name_email);
    }

    @Override
    @AuthUI.SupportedProvider
    public String getProviderId() {
        return EmailAuthProvider.PROVIDER_ID;
    }

    @Override
    @LayoutRes
    public int getButtonLayout() {
        return R.layout.login_register;
    }

    @Override
    public void startLogin(Activity activity) {
        activity.startActivityForResult(
                RegisterEmailActivity.createIntent(activity, mHelper.getFlowParams()),
                RC_EMAIL_FLOW);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_EMAIL_FLOW && resultCode == ResultCodes.OK) {
            mHelper.finishActivity(mActivity, ResultCodes.OK, data);
        }
    }
}
