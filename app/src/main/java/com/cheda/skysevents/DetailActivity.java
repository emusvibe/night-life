package com.cheda.skysevents;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.cheda.skysevents.fragments.DetailFragment;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_NAME = "event_name";
    private ShareActionProvider mShareActionProvider;
    public static final String ARG_PARAM5 = "event_poster";
    View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setupWindowAnimations();

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();

            arguments.putString(DetailFragment.ARG_PARAM1,
                    getIntent().getStringExtra(DetailFragment.ARG_PARAM1));
            arguments.putString(DetailFragment.ARG_PARAM2,
                    getIntent().getStringExtra(DetailFragment.ARG_PARAM2));
            arguments.putString(DetailFragment.ARG_PARAM3,
                    getIntent().getStringExtra(DetailFragment.ARG_PARAM3));
            arguments.putString(DetailFragment.ARG_PARAM4,
                    getIntent().getStringExtra(DetailFragment.ARG_PARAM4));
//            arguments.putString(DetailFragment.ARG_PARAM5,
//                    getIntent().getStringExtra(DetailFragment.ARG_PARAM5));
            arguments.putString(DetailFragment.EXTRA_NAME,getIntent().getStringExtra(EXTRA_NAME));

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.event_detail_container, fragment)
                    .commit();
        }
        loadBackdrop();
    }
    private void loadBackdrop() {
        final String poster  = getIntent().getExtras().getString(ARG_PARAM5);
        final ImageView backdrop = (ImageView) findViewById(R.id.backdrop);
        Picasso.with(this)
                .load(poster)
                .placeholder(R.drawable.bckcover)
                .into(backdrop);
    }

    private void setupWindowAnimations() {
        // We are not interested in defining a new Enter Transition. Instead we change default transition duration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setAllowReturnTransitionOverlap(false);
            getWindow().setAllowEnterTransitionOverlap(false);
            getWindow().getEnterTransition().setDuration(getResources().getInteger(R.integer.anim_duration_long));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.middle_menu, menu);
        MenuItem shareItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.home:
                NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
                return true;

            case R.id.action_bookm:
                boolean checked = item.isChecked();
                if (checked == true){

                    item.setIcon(R.drawable.ic_bookmark);
                    item.setChecked(!checked);
                    Toast.makeText(this, getString(R.string.add_favr) , Toast.LENGTH_SHORT).show();
                }else {

                    item.setIcon(R.drawable.ic_bookmark_border);
                    item.setChecked(!checked);
                    Toast.makeText(this,getString(R.string.remove_favr),Toast.LENGTH_SHORT).show();
                }


                return true;

            case R.id.action_share:
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getResources().getResourcePackageName(item.getItemId())
                        + '/' + "drawable" + '/' + getIntent().getExtras().getString("poster"));
                shareIntent.setType("image/jpeg");
                shareIntent.putExtra(Intent.EXTRA_STREAM,imageUri );

                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.SkyEvents));
                shareIntent.putExtra(Intent.EXTRA_TEXT,getString(R.string.SkyEventslink));
                startActivity(Intent.createChooser(shareIntent,getResources().getText(R.string.send_to)));

                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
