//package com.cheda.skysevents;
//
//
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.design.widget.BottomNavigationView;
//import android.support.design.widget.CollapsingToolbarLayout;
//import android.support.design.widget.TabLayout;
//import android.support.v4.app.Fragment;
//import android.support.v4.view.ViewPager;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.animation.Animation;
//import android.widget.EditText;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.cheda.skysevents.adapters.ProfileSectionPagerAdapter;
//import com.cheda.skysevents.fragments.FeedBackFragment;
//import com.cheda.skysevents.fragments.TicketsFragment;
//import com.cheda.skysevents.model.User;
//import com.cheda.skysevents.service.UserService;
//import com.cheda.skysevents.startscreen.LoginReg;
//import com.cheda.skysevents.startscreen.SignedInActivity;
//import com.facebook.login.LoginManager;
//import com.kosalgeek.android.photoutil.CameraPhoto;
//import com.kosalgeek.android.photoutil.GalleryPhoto;
//
//
///**
// * A simple {@link Fragment} subclass.
// */
//public class ProfileActivity33 extends AppCompatActivity {
//
//    private final String TAG = this.getClass().getName();
//
//
//    private ImageView profileImageView;
//    private TextView greetingTextView;
//
//    // image importing Declaration =============================================
//    ImageView ivGallery, ivUpload;
//    CameraPhoto cameraPhoto;
//    GalleryPhoto galleryPhoto;
//    final int GALLAREY_REQUEST = 22131;
//    final int CAMERA_REQUEST = 13323;
//    //=======================================================================
//
//    EditText nameTxt, phoneTxt;
//    View v;
//
//    GridView gridView;
//    //ImageView gridView;
////    ArrayList<User> list;
////    UserListAdapter adapter = null;
//
////    public ProfileFragment() {
////        Required empty public constructor
////    }
//
//    private ProfileSectionPagerAdapter mSectionsPagerAdapter;
//    private ViewPager mViewPager;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//        super.onCreate(savedInstanceState);
//        getWindow().setWindowAnimations(Animation.ZORDER_BOTTOM);
//        setContentView(R.layout.activity_profile);
//
////        if (AccessToken.getCurrentAccessToken() == null){
////            goLoginScreen();
////        }
//
//        profileImageView = (ImageView) findViewById(R.id.imageview_profile);
////        greetingTextView = (TextView) findViewById(R.id.textview_greeting)
//        User user = UserService.getInstance(this).getCurrentUser();
//
//        if(user!=null) {
////            greetingTextView.setText(String.format(user.getUsername()));
//            profileImageView.setImageBitmap(UserService.getInstance(this).getProfileImage(user));
//            CollapsingToolbarLayout collapsingToolbar =
//                    (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_profile);
//            collapsingToolbar.setTitle(String.format(user.getUsername()));
//
//        } else{
//            CollapsingToolbarLayout collapsingToolbar =
//                    (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_profile);
//            collapsingToolbar.setTitle("User Name");
//        }
//
//
////        userSqLiteHelper = new UserSQLiteHelper(this, "UserDB.sqlite", null, 1);
////        userSqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS USER(Id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, price VARCHAR, image BLOB)");
//
//        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar();
//
//
//
//
//        try {
////            Glide.with(this).load(R.drawable.pum).into((ImageView)findViewById(R.id.imageview_profile));
//            Glide.with(this).load(R.drawable.pum).into((ImageView)findViewById(R.id.backdrop));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//
//
////        gridView = (GridView) findViewById(R.id.gridView);
////        list = new ArrayList<>();
////        adapter = new UserListAdapter(this, R.layout.food_items, list);
////        gridView.setAdapter(adapter);
//
//        // get all data from sqlite
////        Cursor cursor = EditProfileFragment.userSqLiteHelper.getData("SELECT * FROM USER");
////        list.clear();
////        if ( cursor.getCount() > 0 ){
////            cursor.moveToLast();
////            int id = cursor.getInt(0);
////            String name = cursor.getString(1);
////            String price = cursor.getString(2);
////            byte[] image = cursor.getBlob(3);
////
////            list.add(new User(null, null, image, id));
////
////        }else {
////            cursor.moveToPosition(0);
////
////        }
////        adapter.notifyDataSetChanged();
////
//
//        galleryPhoto = new GalleryPhoto(getApplicationContext());
//        ivUpload = (ImageView) findViewById(R.id.imgBtn_Save);
////        ivGallery = (ImageView) findViewById(R.id.img_profile);
////        ivGallery.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////
////                startActivityForResult(galleryPhoto.openGalleryIntent(), GALLAREY_REQUEST);
////            }
////        });
//
//        ivUpload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intn = new Intent(ProfileActivity33.this,AccountEditActivity.class);
//                startActivity(intn);
//            }
//        });
//
//        mSectionsPagerAdapter = new ProfileSectionPagerAdapter(getSupportFragmentManager());
//        mViewPager = (ViewPager) findViewById(R.id.container);
//        setupViewPager(mViewPager);
//
//        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(mViewPager);
//
//
//        //tabLayout.getTabAt(0).setIcon(R.drawable.ic_ticket);
//        tabLayout.getTabAt(0).setIcon(R.drawable.ic_forum);
//        tabLayout.getTabAt(1).setIcon(R.drawable.ticket_icon_hover);
//
//
//
//        BottomNavigationView bottombar = (BottomNavigationView) findViewById(R.id.bottombarNavView_bar);
//        Menu menu = bottombar.getMenu();
//        MenuItem menuItem = menu.getItem(4);
//        menuItem.setChecked(true);
//        bottombar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()){
//
////                    case R.id.tab_profile:
////
//////
////                        break;
//
//
//                    case R.id.tab_home:
//                        Intent intent1 = new Intent(ProfileActivity33.this, MainActivity.class);
//                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent1);
//                        finish();
//                        break;
//
//                    case R.id.tab_search:
//
////                        Intent intent3 = new Intent(MainActivity.this, TicketsFragment.class);
//                        //          intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
////                        startActivity(intent3);
////                        finish();
//                        break;
//
//                    case R.id.tab_favorite:
//
//                        Intent intent2 = new Intent(ProfileActivity33.this, FavoritesActivity.class);
//                        // intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent2);
//                        finish();
//                        break;
//
//
//                    case R.id.tab_near_me:
//
//                        Intent intent4 = new Intent(ProfileActivity33.this, NearMeActivity.class);
//                        intent4.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent4);
//                        finish();
//                        break;
//
//
//                }
//
//
//                return true;
//            }
//        });
//
//
//
//    }
//
//    private void goLoginScreen(){
//        Intent intentlog = new Intent(this, LoginReg.class);
//        intentlog.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intentlog);
//    }
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_items_profile, menu);
////        MenuItem menuItem = menu.findItem(R.id.action_search);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()){
//            case R.id.action_account:
//                goToAccountPage();
//                Intent signedin = new Intent(this, SignedInActivity.class);
//                signedin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK  | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(signedin);
//
//                break;
//
//            case  R.id.action_Invite:
//                Intent shareIntent = new Intent();
//                shareIntent.setAction(Intent.ACTION_SEND);
//                shareIntent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.SkyEvents));
//                shareIntent.putExtra(Intent.EXTRA_TEXT,getString(R.string.SkyEventslink));
//                shareIntent.setType("text/plain");
//                startActivity(Intent.createChooser(shareIntent,getResources().getText(R.string.send_to)));
//                break;
//
//            case  R.id.action_settings:
//                Intent intent6 = new Intent(ProfileActivity33.this,SettingsActivity.class);
////                intent6.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK  | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent6);
//                break;
//
//            case  R.id.action_About:
//                Intent intent7 = new Intent(ProfileActivity33.this,AboutActivity.class);
////                intent7.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK  | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent7);
//                break;
//
//            case  R.id.menu_logout:
//                logout();
//                LoginManager.getInstance().logOut();
//                goLoginScreen();
//                break;
//        }
//
//        return true;
//    }
//    private void goToAccountPage() {
//
////        User user = UserService.getInstance(getApplicationContext()).getCurrentUser();
////        if(user!=null) {
////            Intent intent = new Intent(this,AccountActivity.class);
////            intent.putExtra("user",UserService.getInstance(this).getCurrentUser());
////            startActivity(intent);
////
////        } else{
////            Intent intent = new Intent(this,LoginActivity.class);
////            startActivity(intent);
////            Toast.makeText(this, "Please Login First", Toast.LENGTH_SHORT).show();
////        }
//
//    }
//
//    private void logout()
//    {
//        UserService.getInstance(this).logout();
//        SharedPreferences sharedPreferences = getSharedPreferences("user_data",MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putBoolean("remembered",false);
//        editor.remove("username");
//        editor.remove("password");
//        editor.commit();
//        finish();
//    }
//
//
//
////    @Override
////    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
////        if (resultCode == RESULT_OK){
////            if (requestCode == 77777){
////                String photoPath = cameraPhoto.getPhotoPath();
////                try {
////                    Bitmap bitmap = ImageLoader.init().from(photoPath).requestSize(512, 512).getBitmap();
////
////                    ivGallery.setImageBitmap(bitmap);
////                } catch (FileNotFoundException e) {
////                    Toast.makeText(getApplicationContext(),"Error While loading Photos", Toast.LENGTH_SHORT).show();
////                }
////
////                Log.d(TAG, photoPath);
////
////            }else if (requestCode == GALLAREY_REQUEST){
////                Uri uri = data.getData();
////                galleryPhoto.setPhotoUri(uri);
////                String photoPath = galleryPhoto.getPath();
////                try {
////                    Bitmap bitmap = ImageLoader.init().from(photoPath).requestSize(512, 512).getBitmap();
////
////                    ivGallery.setImageBitmap(bitmap);
////                } catch (FileNotFoundException e) {
////                    Toast.makeText(getApplicationContext(),"Error While choosing Photos", Toast.LENGTH_SHORT).show();
////                }
////
////            }
////        }
////
////    }
//
//
//    private void setupViewPager(ViewPager viewPager) {
//        ProfileSectionPagerAdapter adapter = new ProfileSectionPagerAdapter(getSupportFragmentManager());
//        //adapter.addFragment(new HomeFragment());
//        adapter.addFragment(new FeedBackFragment());
//        adapter.addFragment(new TicketsFragment());
//        viewPager.setAdapter(adapter);
//
//    }
//
////    public void picview(){
////
////        gridView = (GridView) findViewById(R.id.gridView);
////        adapter = new UserListAdapter(this, R.layout.food_items, list);
////        gridView.setAdapter(adapter);
////
////        // get all data from sqlite
////        Cursor cursor = EditProfileFragment.userSqLiteHelper.getData("SELECT * FROM USER");
////        list.clear();
////        if (cursor != null){
////            cursor.moveToLast();
////            int id = cursor.getInt(0);
////            String name = cursor.getString(1);
////            String price = cursor.getString(2);
////            byte[] image = cursor.getBlob(3);
////
////            list.add(new User(name, price, image, id));
////
////        }else {
////            cursor.moveToPosition(1);
////
////        }
////        adapter.notifyDataSetChanged();
////
////    }
//
//}
