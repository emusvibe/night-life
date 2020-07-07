package com.cheda.skysevents;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cheda.skysevents.adapters.TransitionHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;

import static com.cheda.skysevents.MainActivity.TYPE_PROGRAMMATICALLY;

public class NearMeActivity extends FragmentActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private UiSettings mapSetting;
    private Marker marker;
    private Hashtable<String, String> markers;
    double latitude;
    double longitude;
    private int PROXIMITY_RADIUS = 10000;

    static final String EXTRA_TYPE = "type";
    private int type;
    static final int TYPE_PROGRAMMATICALLY = 0;

    GoogleApiClient mGoogleApiClient;
    android.location.Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setupWindowAnimations();
        setContentView(R.layout.activity_near_me);
        type = getIntent().getExtras().getInt(EXTRA_TYPE);

// Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }



//show error dialog if Google Play Services not available
        if (!isGooglePlayServicesAvailable()) {
            Log.d("onCreate", "Google Play Services not available. Ending Test case.");
            finish();
        }
        else {
            Log.d("onCreate", "Google Play Services available. Continuing.");
        }

        BottomNavigationView bottombar = (BottomNavigationView) findViewById(R.id.bottombarNavView_bar);
        Menu menu = bottombar.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        bottombar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.tab_profile:
                        Intent intent0 = new Intent(NearMeActivity.this, ProfileActivity.class);
                        intent0.putExtra(EXTRA_TYPE, TYPE_PROGRAMMATICALLY);
                        transitionTo0(intent0);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            finishAfterTransition();
                        }

                        break;

                    case R.id.tab_home:
                        Intent intent1 = new Intent(NearMeActivity.this, MainActivity.class);
                        intent1.putExtra(EXTRA_TYPE, TYPE_PROGRAMMATICALLY);
//                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        transitionTo1(intent1);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            finishAfterTransition();
                        }
                        break;

//                    case R.id.tab_search:
//                        Intent intent3 = new Intent(NearMeActivity.this, TicketsFragment.class);
//                        intent3.putExtra(EXTRA_TYPE, TYPE_PROGRAMMATICALLY);
//                        transitionTo3(intent3);
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                            getWindow().setAllowReturnTransitionOverlap(false);
//                            getWindow().setAllowEnterTransitionOverlap(false);
//                            finishAfterTransition();
//                        }
//                        break;

                    case R.id.tab_favorite:

                        Intent intent2 = new Intent(NearMeActivity.this, FavoritesActivity.class);
                        intent2.putExtra(EXTRA_TYPE, TYPE_PROGRAMMATICALLY);
                        transitionTo2(intent2);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            finishAfterTransition();
                        }
                        break;

//                    case R.id.tab_near_me:
//
//                        Intent intent4 = new Intent(NearMeActivity.this, NearMeActivity.class);
////                        intent4.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent4.putExtra(EXTRA_TYPE, TYPE_PROGRAMMATICALLY);
//                        transitionTo4(intent4);
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                            finishAfterTransition();
//                        }
//                        break;
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
        Fade enterTransition = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            enterTransition = new Fade();
//            enterTransition.setSlideEdge(Gravity.LEFT);
            enterTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
            getWindow().setEnterTransition(enterTransition);
            getWindow().setExitTransition(enterTransition);
//            getWindow().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//             getWindow().setReenterTransition(enterTransition);
//            getWindow().setAllowReturnTransitionOverlap(false);
            getWindow().setAllowEnterTransitionOverlap(false);

        }
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)
                    ) {
                googleAPI.getErrorDialog(this, result,0).show();
            }
            return false;
        }
        return true;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
                mapSetting = mMap.getUiSettings();
                mapSetting.setScrollGesturesEnabled(true);
                mapSetting.setZoomControlsEnabled(true);

            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
            mapSetting = mMap.getUiSettings();
            mapSetting.setScrollGesturesEnabled(true);
            mapSetting.setZoomControlsEnabled(true);
        }

        LatLng cheda = new LatLng(-26.20217, 28.2544);
        mMap.addMarker(new MarkerOptions()
                .title("Cheda's Place")
                .snippet("My World alone")
                // .icon(mIcon)
                .position(cheda));



        markers = new Hashtable<>();
//        imageLoader = ImageLoader.getInstance();
//
//        options = new DisplayImageOptions.Builder()
//                .showStubImage(R.mipmap.ic_launcher)		//	Display Stub Image
//                .showImageForEmptyUri(R.mipmap.ic_launcher)	//	If Empty image found
//                .cacheInMemory()
//                .cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();

        if ( googleMap != null ) {
            new NearMeActivity.MarkerTask().execute();
            googleMap.setInfoWindowAdapter(new NearMeActivity.CustomInfoWindowAdapter());
        }

        googleMap.setOnInfoWindowClickListener(this);

    }

    public class MarkerTask extends AsyncTask<Void,Void,String> {

        private static final String LOG_TAG = "Example";
        private static final String SERVICE_URL = "http://10.0.0.6/cheda/4jb09.json";

        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection conn = null;
            final StringBuilder json = new StringBuilder();
            try {
                URL url = new URL(SERVICE_URL);
                conn = (HttpURLConnection)url.openConnection();
                InputStreamReader in = new InputStreamReader(conn.getInputStream());

                int read;
                char[]buff= new char[1024];
                while ((read = in.read(buff)) != -1){
                    json.append(buff,0,read);
                }

            }catch (IOException e){
                Log.e(LOG_TAG, "ERROR Connecting to server ",e);
            }finally {
                if (conn != null){conn.disconnect();}
            }

            return json.toString();
        }

        @Override
        protected void onPostExecute(String json) {
            try {
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObj = jsonArray.getJSONObject(i);
                    LatLng latLng = new LatLng(jsonObj.getJSONArray("latlng").getDouble(0),jsonObj.getJSONArray("latlng").getDouble(1));

                    if (i==0){
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(10).build();
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }
                    mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
                            .title(jsonObj.getString("name"))
                            .snippet(jsonObj.getString("date"))
//                            .snippet(Integer.toString(jsonObj.getInt("population")))
                            .position(latLng));
                    // markers.put(jsonObj.get(), "http://10.0.0.6/cheda/Images/album7.jpg");
                }
            } catch (JSONException e){
                Log.e(LOG_TAG,"Error procesing Json",e);
            }

        }
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 200, null);
        Toast.makeText(this, "Info window clicked", Toast.LENGTH_SHORT).show();
    }

    private class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        private View view;
        final String thumbnail  = getIntent().getStringExtra("thumbnail");

        public CustomInfoWindowAdapter() {
            view = getLayoutInflater().inflate(R.layout.custom_info_window, null);
        }
        @Override
        public View getInfoWindow(final Marker marker) {

            // here you set the name and date of the gig on markr



            NearMeActivity.this.marker = marker;

//            String thumbnail = null;

//            if (marker.getId() != null && markers != null && markers.size() > 0) {
//                if ( markers.get(marker.getId()) != null &&
//                        markers.get(marker.getId()) != null) {
//                    thumbnail = markers.get(marker.getId());
//                }
//            }


            final ImageView image = ((ImageView) view.findViewById(R.id.badge));
            Glide.with(getApplicationContext())
                    .load(thumbnail)
                    .placeholder(R.drawable.sky_logo)
                    .into(image);
//            if (url != null && !url.equalsIgnoreCase("null")
//                    && !url.equalsIgnoreCase("")) {
//
//            }
//                imageLoader.displayImage(url, image, options,
//                        new SimpleImageLoadingListener() {
//                            @Override
//                            public void onLoadingComplete(String imageUri,
//                                                          View view, Bitmap loadedImage) {
//                                super.onLoadingComplete(imageUri, view,
//                                        loadedImage);
//                                getInfoContents(marker);
//                            }
//                        });
//            } else {
//                image.setImageResource(R.mipmap.ic_launcher);
//            }
            String songname = getIntent().getStringExtra("name");
            final String title = marker.getTitle();
            final TextView titleUi = ((TextView) view.findViewById(R.id.mkr_title));
            if (title != null) {
                titleUi.setText(title);
            } else {
                titleUi.setText("");
            }
            String numofsongs = getIntent().getStringExtra("numOfSongs");
            final String snippet = marker.getSnippet();
            final TextView snippetUi = ((TextView) view
                    .findViewById(R.id.mkr_snippet));
            if (snippet != null) {
                snippetUi.setText(snippet);
            } else {
                snippetUi.setText("");
            }
            return view;

        }

        @Override
        public View getInfoContents(Marker marker) {
            //  view = getLayoutInflater(null).inflate(R.layout.custom_info_window, null);
//            View v = getLayoutInflater().inflate(R.layout.nav_header_main, null );
//            TextView mkr_name = (TextView) v.findViewById(R.id.mkr_name);
//            TextView songsdata = (TextView) v.findViewById(R.id.songsdata);
//            ImageView iv = (ImageView) v.findViewById(R.id.thumbnail_header);

//            mkr_name.setText(marker.getTitle());
//            songsdata.setText(marker.getSnippet());

//            return v;


            if (NearMeActivity.this.marker != null && NearMeActivity.this.marker.isInfoWindowShown()) {
                NearMeActivity.this.marker.hideInfoWindow();
                NearMeActivity.this.marker.showInfoWindow();
            }

            return null;
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

    }




    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,	android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest, this);

        }

    }

    @Override
    public void onConnectionSuspended(int i) {


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onLocationChanged(Location location) {
        Log.d("onLocationChanged", "entered");
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
//Place current location marker
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title( "You Are Here");
// Adding colour to the marker
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
// Adding Marker to the Map
        mCurrLocationMarker = mMap.addMarker(markerOptions);
//move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo( 11));
        Log.d("onLocationChanged", String.format("latitude:%.3f longitude:%.3f", latitude,longitude));
        Log.d("onLocationChanged", "Exit");
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
// Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.ACCESS_FINE_LOCATION)) {
// Show an explanation to the user *asynchronously* -- don't block this thread waiting for the user's response! After the user
// sees the explanation, try again to request the permission.
//Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions( this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
// No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions( this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
// If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
// permission was granted. Do the contacts-related task you need to do.
                    if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
//                        mapSetting = mMap.getUiSettings();
//                        mapSetting.setScrollGesturesEnabled(true);
//                        mapSetting.setZoomControlsEnabled(true);
                    }
                } else {
// Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
// other 'case' lines to check for other permissions this app might request.
// You can add here other case statements according to your requirement.
        }
    }







}
