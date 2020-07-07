package com.cheda.skysevents;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.transition.Explode;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.cheda.skysevents.adapters.SkyEventsListAdapter;
import com.cheda.skysevents.adapters.TransitionHelper;
import com.cheda.skysevents.fragments.DetailFragment;
import com.cheda.skysevents.fragments.UpComingFragment;
import com.cheda.skysevents.model.SkyEvent;
import com.cheda.skysevents.service.SharedPreference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.cheda.skysevents.MainActivity.TYPE_PROGRAMMATICALLY;

public class FavoritesActivity extends AppCompatActivity {
    public static final String ARG_ITEM_ID = "favorite_list";
    private RecyclerView mRecyclerView;
    private UpComingFragment.SkyListCallback listCallback;
    private ArrayList<SkyEvent> favorites ;
    private SkyEventsListAdapter mAdapter;
    SharedPreference sharedPreference;
    private ImageView profileImageView;
    private boolean mTwoPane;

    static final int TYPE_PROGRAMMATICALLY = 0;
    static final String EXTRA_TYPE = "type";
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        setupWindowAnimations();

        type = getIntent().getExtras().getInt(EXTRA_TYPE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        BottomNavigationView bottombar = (BottomNavigationView) findViewById(R.id.bottombarNavView_bar);
        Menu menu = bottombar.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        bottombar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.tab_profile:
                        Intent intent0 = new Intent(FavoritesActivity.this, ProfileActivity.class);
                        intent0.putExtra(EXTRA_TYPE, TYPE_PROGRAMMATICALLY);
                        transitionTo0(intent0);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            finishAfterTransition();
                        }

                        break;

                    case R.id.tab_home:
                        Intent intent1 = new Intent(FavoritesActivity.this, MainActivity.class);
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

//                    case R.id.tab_favorite:
//
//                        Intent intent2 = new Intent(FavoritesActivity.this, FavoritesActivity.class);
//                        intent2.putExtra(EXTRA_TYPE, TYPE_PROGRAMMATICALLY);
//                        transitionTo2(intent2);
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                            finishAfterTransition();
//                        }
//                        break;


                    case R.id.tab_near_me:
                        Intent intent4 = new Intent(FavoritesActivity.this, NearMeActivity.class);
//                        intent4.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent4.putExtra(EXTRA_TYPE, TYPE_PROGRAMMATICALLY);
                        transitionTo4(intent4);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                            getWindow().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            finishAfterTransition();
                        }
                        break;
                }

                return true;
            }
        });
        profileImageView = (ImageView) findViewById(R.id.imageview_profile);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            Picasso.with(getApplicationContext())
                    .load(user.getPhotoUrl())
                    .fit()
                    .into((ImageView) findViewById(R.id.imageview_profile));
        }
        initViews();

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
        Explode enterTransition = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            enterTransition = new Explode();
//            enterTransition.setSlideEdge(Gravity.RIGHT);
            enterTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
            getWindow().setEnterTransition(enterTransition);
//            getWindow().setExitTransition(enterTransition);
//            getWindow().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            getWindow().setReenterTransition(enterTransition);
//            getWindow().setAllowReturnTransitionOverlap(false);
            getWindow().setAllowEnterTransitionOverlap(false);

        }
    }



    //here we setup our dialog box to be shown when favorites is empty
    public void showNoFavoritesSetDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("No Favorites");
        builder1.setIcon(R.drawable.ic_favorite_selected);
        builder1.setMessage(R.string.dialog_no_favorites_set);
        builder1.setCancelable(true);
        builder1.setNeutralButton(R.string.dialog_no_favorites_ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));

                        dialog.cancel();
                    }
                });

        AlertDialog alert1 = builder1.create();
        alert1.show();
    }
    // here we initialize views
    private void initViews() {

        // Get favorite items from SharedPreferences.
        sharedPreference = new SharedPreference();
        favorites = sharedPreference.getFavorites(this);

        if (favorites == null) {
            showNoFavoritesSetDialog();
        }
          else {

            if (favorites.size() == 0){
            showNoFavoritesSetDialog();

        }
          mRecyclerView = (RecyclerView) findViewById(R.id.favorite_list);
          if (favorites != null) {
             mRecyclerView.setHasFixedSize(true);
             mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
             favorites = new ArrayList<>();

             favorites = sharedPreference.getFavorites(this);
             mAdapter = new SkyEventsListAdapter(this, favorites, new UpComingFragment.SkyListCallback() {
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
//                         Toast.makeText(getApplicationContext(), "Twopane is true", Toast.LENGTH_SHORT).show();
                         getSupportFragmentManager().beginTransaction()
                                 .replace(R.id.event_detail_container, fragment)
                                 .commit();

                     } else {


//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                          getWindow().setEnterTransition(new Fade(Fade.IN));
//                            getWindow().setExitTransition(new Fade(Fade.OUT));
//
//                            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                                    this,
//                                    new Pair<>(v.findViewById(R.id.poster),
//                                            context.getString(R.string.transition_image)),
//                                    new Pair<>(v.findViewById(R.id.tv_name),
//                                            context.getString(R.string.transition_name)),
//                                    new Pair<>(v.findViewById(R.id.tv_venue),
//                                            context.getString(R.string.transition_venue)),
//                                    new Pair<>(v.findViewById(R.id.tv_date),
//                                            context.getString(R.string.transition_date)),
//                                    new Pair<View, String>(v.findViewById(R.id.tv_admission),
//                                            context.getString(R.string.transition_fee))
//                            );
//                            ActivityCompat.startActivity(intent,optionsCompat.toBundle());
//
//                        }


                         // In single-pane mode, simply start the detail activity
                         // for the selected item ID.
                         Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                         intent.putExtra(DetailFragment.ARG_PARAM1, event.getName());
                         intent.putExtra(DetailFragment.ARG_PARAM2, event.getDate());
                         intent.putExtra(DetailFragment.ARG_PARAM3, event.getVenue());
                         intent.putExtra(DetailFragment.ARG_PARAM4, event.getAdmission());
                         intent.putExtra(DetailFragment.ARG_PARAM5, event.getPoster());
                         startActivity(intent);
//                         Toast.makeText(getApplicationContext(), "Twopane is false", Toast.LENGTH_SHORT).show();
                     }

//                     Toast.makeText(getApplicationContext(), "New call back works", Toast.LENGTH_SHORT).show();

                 }
             }, new SkyEventsListAdapter.FavListener() {
                 @Override
                 public void onClickFavItem(SkyEvent event) {
//                     if (findViewById(R.id.event_detail_container) != null) {
//                         mTwoPane = true;
//                     }
//
//                     if (mTwoPane) {
//                         // In two-pane mode, show the detail view in this activity by
//                         // adding or replacing the detail fragment using fragment transaction.
//                         Bundle arguments = new Bundle();
//                         arguments.putString(DetailFragment.ARG_PARAM1, event.getName());
//                         arguments.putString(DetailFragment.ARG_PARAM2, event.getDate());
//                         arguments.putString(DetailFragment.ARG_PARAM3, event.getVenue());
//                         arguments.putString(DetailFragment.ARG_PARAM4, event.getAdmission());
//                         arguments.putString(DetailFragment.ARG_PARAM5, event.getPoster());
//
//                         DetailFragment fragment = DetailFragment.newInstance(event);
//                         Toast.makeText(getApplicationContext(), "Twopane is true", Toast.LENGTH_SHORT).show();
//                         getSupportFragmentManager().beginTransaction()
//                                 .replace(R.id.event_detail_container, fragment)
//                                 .commit();
//
//                     } else {
//
//
////                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
////                          getWindow().setEnterTransition(new Fade(Fade.IN));
////                            getWindow().setExitTransition(new Fade(Fade.OUT));
////
////                            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
////                                    this,
////                                    new Pair<>(v.findViewById(R.id.poster),
////                                            context.getString(R.string.transition_image)),
////                                    new Pair<>(v.findViewById(R.id.tv_name),
////                                            context.getString(R.string.transition_name)),
////                                    new Pair<>(v.findViewById(R.id.tv_venue),
////                                            context.getString(R.string.transition_venue)),
////                                    new Pair<>(v.findViewById(R.id.tv_date),
////                                            context.getString(R.string.transition_date)),
////                                    new Pair<View, String>(v.findViewById(R.id.tv_admission),
////                                            context.getString(R.string.transition_fee))
////                            );
////                            ActivityCompat.startActivity(intent,optionsCompat.toBundle());
////
////                        }
//
//
//                         // In single-pane mode, simply start the detail activity
//                         // for the selected item ID.
//                         Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
//                         intent.putExtra(DetailFragment.ARG_PARAM1, event.getName());
//                         intent.putExtra(DetailFragment.ARG_PARAM2, event.getDate());
//                         intent.putExtra(DetailFragment.ARG_PARAM3, event.getVenue());
//                         intent.putExtra(DetailFragment.ARG_PARAM4, event.getAdmission());
//                         intent.putExtra(DetailFragment.ARG_PARAM5, event.getPoster());
//                         startActivity(intent);
//                         Toast.makeText(getApplicationContext(), "Twopane is false", Toast.LENGTH_SHORT).show();
//                     }
//
//                     Toast.makeText(getApplicationContext(), "New call back works", Toast.LENGTH_SHORT).show();

                 }
             });
             mRecyclerView.smoothScrollToPosition(0);
             mRecyclerView.setAdapter(mAdapter);

             ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallback());
             itemTouchHelper.attachToRecyclerView(mRecyclerView);
           }
        }
    }
    private ItemTouchHelper.Callback createHelperCallback(){

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT |ItemTouchHelper.ANIMATION_TYPE_SWIPE_SUCCESS | ItemTouchHelper.ACTION_STATE_SWIPE) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                moveItem(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                deleteItem(viewHolder.getAdapterPosition());

            }
        };

        return simpleCallback;
    }

    private void moveItem(int oldPos, int newPos){

        mAdapter.notifyItemMoved(oldPos, newPos);
    }

    private void deleteItem(final int position){

        sharedPreference.removeFavorite(this,favorites.remove(position));
//        favorites.remove(position);
        mAdapter.notifyItemRemoved(position);
//        mAdapter.notifyItemChanged(position);

        Toast.makeText(this,this.getString(R.string.remove_favr), Toast.LENGTH_SHORT).show();
        sharedPreference.saveFavorites(this,favorites);
        mAdapter.notifyDataSetChanged();
    }

}
