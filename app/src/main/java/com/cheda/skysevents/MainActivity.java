package com.cheda.skysevents;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import com.cheda.skysevents.adapters.TransitionHelper;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.Visibility;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cheda.skysevents.adapters.SectionPagerAdapter;
import com.cheda.skysevents.fragments.DetailFragment;
import com.cheda.skysevents.fragments.PopularFragment;
import com.cheda.skysevents.fragments.UpComingFragment;
import com.cheda.skysevents.model.SkyEvent;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.appinvite.FirebaseAppInvite;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements UpComingFragment.SkyListCallback, PopularFragment.SkyListCallback{
    private ImageView profileImageView;
    private TextView greetingTextView;
    private boolean mTwoPane;
    ViewPager viewPager;
    static final int TYPE_PROGRAMMATICALLY = 0;
    static final String EXTRA_TYPE = "type";
    private int type;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupWindowAnimations();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setAllowReturnTransitionOverlap(false);
//            getWindow().setAllowEnterTransitionOverlap(false);

        }


//        if (AccessToken.getCurrentAccessToken() == null){
//            goLoginScreen();
//        }

        // Check for App Invite invitations and launch deep-link activity if possible.
        // Requires that an Activity is registered in AndroidManifest.xml to handle
        // deep-link URLs.
        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData data) {
                        if (data == null) {
                            Log.d(TAG, "getInvitation: no data");
                            return;
                        }

                        // Get the deep link
                        Uri deepLink = data.getLink();

                        // Extract invite
                        FirebaseAppInvite invite = FirebaseAppInvite.getInvitation(data);
                        if (invite != null) {
                            String invitationId = invite.getInvitationId();
                        }

                        // Handle the deep link
                        // ...
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "getDynamicLink:onFailure", e);
                    }
                });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        BottomNavigationView bottombar = (BottomNavigationView) findViewById(R.id.bottombarNavView_bar);
        Menu menu = bottombar.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
        bottombar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.tab_profile:
                        Intent intent0 = new Intent(MainActivity.this, ProfileActivity.class);
                        intent0.putExtra(EXTRA_TYPE, TYPE_PROGRAMMATICALLY);
                        transitionTo0(intent0);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            finishAfterTransition();
                        }

                        break;

//                    case R.id.tab_home:
////                        Intent intent1 = new Intent(MainActivity.this, MainActivity.class);
////                        intent1.putExtra(EXTRA_TYPE, TYPE_PROGRAMMATICALLY);
//////                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
////                        transitionTo1(intent1);
////                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
////                            getWindow().setAllowReturnTransitionOverlap(false);
////                            getWindow().setAllowEnterTransitionOverlap(false);
////                            finishAfterTransition();
////                        }
//                        break;

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

                        Intent intent2 = new Intent(MainActivity.this, FavoritesActivity.class);
                        intent2.putExtra(EXTRA_TYPE, TYPE_PROGRAMMATICALLY);

                        transitionTo2(intent2);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            finishAfterTransition();
                        }
                        break;


                    case R.id.tab_near_me:

                        Intent intent4 = new Intent(MainActivity.this, NearMeActivity.class);
//                        intent4.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent4.putExtra(EXTRA_TYPE, TYPE_PROGRAMMATICALLY);
                        transitionTo4(intent4);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            finishAfterTransition();
                        }
                        break;
                }
//                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                return true;
            }
        });

        profileImageView = (ImageView) findViewById(R.id.imageview_profile);
        greetingTextView = (TextView) findViewById(R.id.textview_greeting);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            greetingTextView.setText(String.format("%s", user.getDisplayName()));
            if (String.format("%s", user.getDisplayName()) == null){
                greetingTextView.setText(String.format("%s", user.getPhoneNumber()));
//            } else {
//                greetingTextView.setText(String.format("%s", user.getEmail()));
            }
            Picasso.with(getApplicationContext())
                    .load(user.getPhotoUrl())
                    .fit()
                    .placeholder(R.drawable.ic_avatr)
                    .into((ImageView)findViewById(R.id.imageview_profile));

            profileImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "You are already logged in " + user.getDisplayName(), Toast.LENGTH_SHORT).show();

                }
            });
        }

//    private void goLoginScreen(){
//        Intent intentlog = new Intent(this, LoginRegister.class);
//        intentlog.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intentlog);
    }

    private void setupWindowAnimations() {
        // Re-enter transition is executed when returning to this activity
        Slide enterTransition = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            enterTransition = new Slide();
            enterTransition.setSlideEdge(Gravity.LEFT);
            enterTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
            getWindow().setEnterTransition(enterTransition);
            getWindow().setExitTransition(enterTransition);

            getWindow().setReenterTransition(enterTransition);
            getWindow().setAllowReturnTransitionOverlap(false);
            getWindow().setAllowEnterTransitionOverlap(false);

        }
    }

    private Visibility buildReturnTransition() {
        Visibility enterTransition = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            enterTransition = new Slide();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            enterTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        }
        return enterTransition;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
//        MenuItem search = menu.findItem(R.id.search);
//        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
//        searchView.setQueryHint("Search by event, venue or date");
//        search(searchView);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
//        switch (AppCompatDelegate.getDefaultNightMode()) {
//            case AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM:
//                menu.findItem(R.id.menu_night_mode_system).setChecked(true);
//                break;
//            case AppCompatDelegate.MODE_NIGHT_AUTO:
//                menu.findItem(R.id.menu_night_mode_auto).setChecked(true);
//                break;
//            case AppCompatDelegate.MODE_NIGHT_YES:
//                menu.findItem(R.id.menu_night_mode_night).setChecked(true);
//                break;
//            case AppCompatDelegate.MODE_NIGHT_NO:
//                menu.findItem(R.id.menu_night_mode_day).setChecked(true);
//                break;
//        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.menu_night_mode_system:
//                setNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
//                break;
//            case R.id.menu_night_mode_day:
//                setNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                break;
//            case R.id.menu_night_mode_night:
//                setNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                break;
//            case R.id.menu_night_mode_auto:
//                setNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
//                break;
//        }
        return super.onOptionsItemSelected(item);

    }
    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//
//                SkyEventsListAdapter adapter = new SkyEventsListAdapter(getApplicationContext(), mArrayList, listCallback);
//                adapter.getFilter().filter(newText);
//                mAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    private void setNightMode(@AppCompatDelegate.NightMode int nightMode) {
//        AppCompatDelegate.setDefaultNightMode(nightMode);
//
//        if (Build.VERSION.SDK_INT >= 11) {
//            recreate();
//        }
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionPagerAdapter adapter = new SectionPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new UpComingFragment(), "Up Coming Events");
        adapter.addFragment(new PopularFragment(), "Popular Events");
        viewPager.setAdapter(adapter);
    }

    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        if (exit){
            moveTaskToBack(true);
            // super.onBackPressed();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Visibility returnTransition = buildReturnTransition();
                getWindow().setReturnTransition(returnTransition);
                this.finishAfterTransition();
            }
        }else {
            Toast.makeText(this, "Press Back again to Exit", Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            },3 * 1000);
        }

    }


    @Override
    public void onSkyEventSelected(SkyEvent event) {

        if (findViewById(R.id.event_detail_container) != null) {
            mTwoPane = true;
        }


        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(DetailFragment.ARG_PARAM1, event.getName());
            arguments.putString(DetailFragment.ARG_PARAM2, event.getDate());
            arguments.putString(DetailFragment.ARG_PARAM3, event.getVenue());
            arguments.putString(DetailFragment.ARG_PARAM4, event.getAdmission());
            arguments.putString(DetailFragment.ARG_PARAM5, event.getPoster());

            DetailFragment fragment = DetailFragment.newInstance(event);
//            Toast.makeText(this, "Twopane is true", Toast.LENGTH_SHORT).show();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.event_detail_container, fragment)
                    .commit();

        } else {

     Intent intent = new Intent(this, DetailActivity.class);
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
          getWindow().setSharedElementEnterTransition(new Fade(Fade.IN));
          getWindow().setSharedElementExitTransition(new Fade(Fade.OUT));

            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
             this,
                  new Pair<>(findViewById(R.id.poster),
                          this.getString(R.string.transition_image)),
                  new Pair<>(findViewById(R.id.tv_name),
                      this.getString(R.string.transition_name)),
                  new Pair<>(findViewById(R.id.tv_venue),
                          this.getString(R.string.transition_venue)),
                  new Pair<>(findViewById(R.id.tv_date),
                          this.getString(R.string.transition_date)),
                    new Pair<>(findViewById(R.id.tv_admission),
                            this.getString(R.string.transition_fee))
                   );

            intent.putExtra(DetailFragment.ARG_PARAM1, event.getName());
            intent.putExtra(DetailFragment.ARG_PARAM2, event.getDate());
            intent.putExtra(DetailFragment.ARG_PARAM3, event.getVenue());
            intent.putExtra(DetailFragment.ARG_PARAM4, event.getAdmission());
            intent.putExtra(DetailFragment.ARG_PARAM5, event.getPoster());
            startActivity(intent,optionsCompat.toBundle());

        }else{
          // In single-pane mode, simply start the detail activity
          // for the selected item ID.
          intent.putExtra(DetailFragment.ARG_PARAM1, event.getName());
          intent.putExtra(DetailFragment.ARG_PARAM2, event.getDate());
          intent.putExtra(DetailFragment.ARG_PARAM3, event.getVenue());
          intent.putExtra(DetailFragment.ARG_PARAM4, event.getAdmission());
          intent.putExtra(DetailFragment.ARG_PARAM5, event.getPoster());
          startActivity(intent);
      }
    }
}


    @Override
    public void onSkyEvent2Selected(SkyEvent event) {

        if (findViewById(R.id.event_detail_container2) != null) {
            mTwoPane = true;
        }

        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(DetailFragment.ARG_PARAM1, event.getName());
            arguments.putString(DetailFragment.ARG_PARAM2, event.getDate());
            arguments.putString(DetailFragment.ARG_PARAM3, event.getVenue());
            arguments.putString(DetailFragment.ARG_PARAM4, event.getAdmission());
            arguments.putString(DetailFragment.ARG_PARAM5, event.getPoster());

            DetailFragment fragment = DetailFragment.newInstance(event);
//            Toast.makeText(this, "Twopane is true", Toast.LENGTH_SHORT).show();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.event_detail_container2, fragment)
                    .commit();

        } else {
            Intent intent = new Intent(this, DetailActivity.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setEnterTransition(new Fade(Fade.IN));
                getWindow().setExitTransition(new Fade(Fade.OUT));

                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        this,
                        new Pair<>(findViewById(R.id.poster),
                                this.getString(R.string.transition_image)),
                        new Pair<>(findViewById(R.id.tv_name),
                                this.getString(R.string.transition_name)),
                        new Pair<>(findViewById(R.id.tv_venue),
                                this.getString(R.string.transition_venue)),
                        new Pair<>(findViewById(R.id.tv_date),
                                this.getString(R.string.transition_date)),
                        new Pair<>(findViewById(R.id.tv_admission),
                                this.getString(R.string.transition_fee))
                );

                intent.putExtra(DetailFragment.ARG_PARAM1, event.getName());
                intent.putExtra(DetailFragment.ARG_PARAM2, event.getDate());
                intent.putExtra(DetailFragment.ARG_PARAM3, event.getVenue());
                intent.putExtra(DetailFragment.ARG_PARAM4, event.getAdmission());
                intent.putExtra(DetailFragment.ARG_PARAM5, event.getPoster());
                startActivity(intent,optionsCompat.toBundle());

            }else{
                // In single-pane mode, simply start the detail activity
                // for the selected item ID.
                intent.putExtra(DetailFragment.ARG_PARAM1, event.getName());
                intent.putExtra(DetailFragment.ARG_PARAM2, event.getDate());
                intent.putExtra(DetailFragment.ARG_PARAM3, event.getVenue());
                intent.putExtra(DetailFragment.ARG_PARAM4, event.getAdmission());
                intent.putExtra(DetailFragment.ARG_PARAM5, event.getPoster());
                startActivity(intent);
            }

        }
    }


    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }


}
