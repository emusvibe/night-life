package com.cheda.skysevents;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.cheda.skysevents.model.User;
import com.cheda.skysevents.service.UserService;

public class AccountActivity extends AppCompatActivity {

    private ImageView profileImageView;
    private TextView usernameTextView;
    private TextView emailTextVIew;
    private TextView genderTextView;
    private CompoundButton newsletterSubscriptionCompoundButton;
    private CompoundButton allowOtherEmailCompoundButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        User user = (User) getIntent().getExtras().getSerializable("user");

        if(user==null) {
            finish();
            return;
        }

        profileImageView = (ImageView) findViewById(R.id.imageview_profile);
        usernameTextView = (TextView) findViewById(R.id.textview_username);
        emailTextVIew = (TextView) findViewById(R.id.textview_email);
        genderTextView = (TextView) findViewById(R.id.textview_gender);
        newsletterSubscriptionCompoundButton = (Switch) findViewById(R.id.switch_subscription);
        if(newsletterSubscriptionCompoundButton==null)
        {
//            newsletterSubscriptionCompoundButton = (CheckBox) findViewById(R.id.checkbox_subscription);
        }
        allowOtherEmailCompoundButton = (Switch) findViewById(R.id.switch_allow_email);
        if(allowOtherEmailCompoundButton==null)
        {
//            allowOtherEmailCompoundButton = (CheckBox) findViewById(R.id.checkbox_allow_email);
        }

        profileImageView.setImageBitmap(UserService.getInstance(this).getProfileImage(user));
        usernameTextView.setText(String.format("Username: %s",user.getUsername()));
        emailTextVIew.setText(String.format("Email: %s",user.getEmail()));
        genderTextView.setText(String.format("Gender: %s",user.getGender()== User.Gender.MALE?"Male":"Female"));
        newsletterSubscriptionCompoundButton.setChecked(user.isNewsletterSubscribed());
        allowOtherEmailCompoundButton.setChecked(user.isAllowedOtherEmail());

        if(!allowOtherEmailCompoundButton.isChecked())
        {
            emailTextVIew.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intnt = new Intent(this,ProfileActivity.class);
        startActivity(intnt);
        finish();
        super.onBackPressed();
    }
}
