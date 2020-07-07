package com.cheda.skysevents;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.Visibility;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cheda.skysevents.adapters.ProfileSectionPagerAdapter;
import com.cheda.skysevents.adapters.TransitionHelper;
import com.cheda.skysevents.fragments.FeedBackFragment;
import com.cheda.skysevents.fragments.TicketsFragment;
import com.cheda.skysevents.service.UserService;
import com.cheda.skysevents.startscreen.AuthUI;
import com.cheda.skysevents.startscreen.IdpResponse;
import com.cheda.skysevents.startscreen.LoginReg;
import com.cheda.skysevents.startscreen.SignedInActivity;
import com.facebook.login.LoginManager;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.appinvite.FirebaseAppInvite;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cheda.skysevents.MainActivity.TYPE_PROGRAMMATICALLY;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getName();
    private static final String EXTRA_SIGNED_IN_CONFIG = "extra_signed_in_config";
//    private static final int REQUEST_INVITE = 101;
    private static final int REQUEST_INVITE = 100;

    static final String EXTRA_TYPE = "type";
    private int type;
    static final int TYPE_PROGRAMMATICALLY = 0;

    private TextView greetingTextView;
    private ProfileSectionPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private IdpResponse mIdpResponse;
    private SignedInActivity.SignedInConfig mSignedInConfig;
    View container;
    @BindView(R.id.imgBtn_Save)
    ImageView mUserProfilePicture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
       getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setupWindowAnimations();
        setContentView(R.layout.activity_profile);
//        type = getIntent().getExtras().getInt(EXTRA_TYPE);


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(LoginReg.createIntent(this));
            finish();
            return;
        }

        mIdpResponse = IdpResponse.fromResultIntent(getIntent());
        mSignedInConfig = getIntent().getParcelableExtra(EXTRA_SIGNED_IN_CONFIG);
//        profileImageView = (ImageView) findViewById(R.id.imageview_profile);
        ButterKnife.bind(this);
        populateProfile();




        mUserProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intn = new Intent(ProfileActivity.this,AccountEditActivity.class);
                startActivity(intn);
            }
        });

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar();

        try {
//            Glide.with(this).load(R.drawable.pum).into((ImageView)findViewById(R.id.imageview_profile));
            Glide.with(this).load(R.drawable.pum).into((ImageView)findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }

        mSectionsPagerAdapter = new ProfileSectionPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        //tabLayout.getTabAt(0).setIcon(R.drawable.ic_ticket);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_forum);
        tabLayout.getTabAt(1).setIcon(R.drawable.ticket_icon_hover);

        BottomNavigationView bottombar = (BottomNavigationView) findViewById(R.id.bottombarNavView_bar);
        Menu menu = bottombar.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);
        bottombar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

//                    case R.id.tab_profile:
//                        Intent intent0 = new Intent(ProfileActivity.this, ProfileActivity.class);
//                        intent0.putExtra(EXTRA_TYPE, TYPE_PROGRAMMATICALLY);
//                        transitionTo0(intent0);
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                            getWindow().setAllowReturnTransitionOverlap(false);
//                            getWindow().setAllowEnterTransitionOverlap(false);
//                            finishAfterTransition();
//                        }
//                        break;

                    case R.id.tab_home:
                        Intent intent1 = new Intent(ProfileActivity.this, MainActivity.class);
                        intent1.putExtra(EXTRA_TYPE, TYPE_PROGRAMMATICALLY);
//                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        transitionTo1(intent1);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            finishAfterTransition();
                        }
                        break;

//                    case R.id.tab_search:
//                        Intent intent3 = new Intent(ProfileActivity.this, TicketsFragment.class);
//                        intent3.putExtra(EXTRA_TYPE, TYPE_PROGRAMMATICALLY);
//                        transitionTo3(intent3);
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                            getWindow().setAllowReturnTransitionOverlap(false);
//                            getWindow().setAllowEnterTransitionOverlap(false);
//                            finishAfterTransition();
//                        }
//                        break;

                    case R.id.tab_favorite:

                        Intent intent2 = new Intent(ProfileActivity.this, FavoritesActivity.class);
                        intent2.putExtra(EXTRA_TYPE, TYPE_PROGRAMMATICALLY);
                        transitionTo2(intent2);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            finishAfterTransition();
                        }
                        break;


                    case R.id.tab_near_me:

                        Intent intent4 = new Intent(ProfileActivity.this, NearMeActivity.class);
//                        intent4.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent4.putExtra(EXTRA_TYPE, TYPE_PROGRAMMATICALLY);
                        transitionTo4(intent4);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            finishAfterTransition();
                        }
                        break;
                }

                return true;
            }
        });

    }

    @SuppressWarnings("unchecked") void transitionTo0(Intent intent0) {
        final Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants(this, true);
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pairs);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivity(intent0, transitionActivityOptions.toBundle());
        }
    }
    @SuppressWarnings("unchecked") void transitionTo1(Intent intent1) {
        final Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants(this, true);
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pairs);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivity(intent1, transitionActivityOptions.toBundle());
        }
    }
    @SuppressWarnings("unchecked") void transitionTo2(Intent intent2) {
        final Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants(this, true);
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pairs);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivity(intent2, transitionActivityOptions.toBundle());

        }
    }

    @SuppressWarnings("unchecked") void transitionTo3(Intent intent3) {
        final Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants(this, true);
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pairs);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivity(intent3, transitionActivityOptions.toBundle());

        }
    }

    @SuppressWarnings("unchecked") void transitionTo4(Intent intent4) {
        final Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants(this, true);
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pairs);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivity(intent4, transitionActivityOptions.toBundle());

        }
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

    private Visibility buildReturnTransition() {
        Visibility enterTransition = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            enterTransition = new Explode();
            getWindow().setExitTransition(enterTransition);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            enterTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        }
        return enterTransition;
    }




    @MainThread
    private void populateProfile() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Picasso.with(getApplicationContext())
                    .load(user.getPhotoUrl())
                    .fit()
                    .placeholder(R.drawable.ic_avatr)
                    .into((ImageView)findViewById(R.id.imageview_profile));
//            Glide.with(this).load(R.drawable.pum).into((ImageView)findViewById(R.id.backdrop));
            CollapsingToolbarLayout collapsingToolbar =
                    (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_profile);
            collapsingToolbar.setTitle(user.getDisplayName());

        }else{
            CollapsingToolbarLayout collapsingToolbar =
                    (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_profile);
            collapsingToolbar.setTitle(user.getPhoneNumber());
        }

    }


    private void goLoginScreen(){
        Intent intentlog = new Intent(this, LoginReg.class);
//        intentlog.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentlog);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items_profile, menu);
//        MenuItem menuItem = menu.findItem(R.id.action_search);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_account:
                goToAccountPage();
                Intent signedin = new Intent(this, SignedInActivity.class);
                Slide slideTransition = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    slideTransition = new Slide();
                    slideTransition.setSlideEdge(Gravity.RIGHT);
                    slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                    getWindow().setExitTransition(slideTransition);

                }
//                signedin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK  | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(signedin);

                break;

            case  R.id.action_Invite:
                Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                        .setMessage(getString(R.string.invitation_message))
                        .setDeepLink(Uri.parse(getString(R.string.SkyEventslink)))
                        .setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
                        .setCallToActionText(getString(R.string.invitation_cta))
                        .build();
                startActivityForResult(intent, REQUEST_INVITE);
//                Intent shareIntent = new Intent();
//                shareIntent.setAction(Intent.ACTION_SEND);
//                shareIntent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.SkyEvents));
//                shareIntent.putExtra(Intent.EXTRA_TEXT,getString(R.string.SkyEventslink));
//                shareIntent.setType("text/plain");
//                startActivity(Intent.createChooser(shareIntent,getResources().getText(R.string.send_to)));
                break;

            case  R.id.action_settings:
                Intent intent6 = new Intent(ProfileActivity.this,SettingsActivity.class);
//                intent6.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK  | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent6);
                break;

            case  R.id.action_About:
                Intent intent7 = new Intent(ProfileActivity.this,AboutActivity.class);
//                intent7.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK  | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent7);
                break;

            case  R.id.menu_logout:
                logout();
                LoginManager.getInstance().logOut();
                goLoginScreen();
                break;
        }

        return true;
    }
    private void goToAccountPage() {

//        User user = UserService.getInstance(getApplicationContext()).getCurrentUser();
//        if(user!=null) {
//            Intent intent = new Intent(this,AccountActivity.class);
//            intent.putExtra("user",UserService.getInstance(this).getCurrentUser());
//            startActivity(intent);
//
//        } else{
//            Intent intent = new Intent(this,LoginActivity.class);
//            startActivity(intent);
//            Toast.makeText(this, "Please Login First", Toast.LENGTH_SHORT).show();
//        }

    }

    private void logout()
    {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            startActivity(LoginReg.createIntent(ProfileActivity.this));
                            finish();
                        } else {
                            Toast.makeText(ProfileActivity.this, getString(R.string.sign_out_failed), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

//        UserService.getInstance(this).logout();
//        SharedPreferences sharedPreferences = getSharedPreferences("user_data",MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putBoolean("remembered",false);
//        editor.remove("username");
//        editor.remove("password");
//        editor.commit();
//        finish();
    }


    private void setupViewPager(ViewPager viewPager) {
        ProfileSectionPagerAdapter adapter = new ProfileSectionPagerAdapter(getSupportFragmentManager());
        //adapter.addFragment(new HomeFragment());
        adapter.addFragment(new FeedBackFragment());
        adapter.addFragment(new TicketsFragment());
        viewPager.setAdapter(adapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Get the invitation IDs of all sent messages
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                for (String id : ids) {
                    Log.d(TAG, "onActivityResult: sent invitation " + id);
                }
            } else {
                // Sending failed or it was canceled, show failure message to the user
                // [START_EXCLUDE]
                showMessage(getString(R.string.send_failed));
                // [END_EXCLUDE]
            }
        }
    }
    // [END on_activity_result]

    private void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        //Snackbar.make(container, msg, Snackbar.LENGTH_SHORT).show();
    }


//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent bck = new Intent(this, MainActivity.class);
//        Visibility returnTransition = buildReturnTransition();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setExitTransition(returnTransition);
//            getWindow().setAllowReturnTransitionOverlap(false);
//        }
//        startActivity(bck);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            finishAfterTransition();
//        }
//    }
}
