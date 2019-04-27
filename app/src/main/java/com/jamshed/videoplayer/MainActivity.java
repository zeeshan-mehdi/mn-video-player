package com.jamshed.videoplayer;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore.Video.Media;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int REQUEST_PERMISSIONS = 100;
    ArrayList al_video = new ArrayList();
    private List<File> filtered;
    private HashSet<String> folderSet;
    adapter_directory obj_adapter;
    RecyclerView recyclerView;
    LayoutManager recyclerViewLayoutManager;
    private HashSet<String> storageSet;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private Activity mActivity;

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);

        mActivity = this;

        initDrawerMenu(true);

        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        this.storageSet = new HashSet();
        this.folderSet = new HashSet();
        this.filtered = new ArrayList();
        init();
        showAdd();
        fbAdd();
    }

    private void showAdd() {
        ((AdView) findViewById(R.id.adView)).loadAd(new AdRequest.Builder().build());
    }

    private void fbAdd() {
//        View adView = new com.facebook.ads.AdView((Context) this, "YOUR_PLACEMENT_ID", AdSize.BANNER_HEIGHT_50);
//        ((LinearLayout) findViewById(R.id.banner_container)).addView(adView);
//        adView.loadAd();
    }

    private void init() {
        this.recyclerView = (RecyclerView) findViewById(R.id.recycler_view1);
        this.recyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
        this.recyclerView.setLayoutManager(this.recyclerViewLayoutManager);
        fn_checkpermission();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.network) {
            showAlertDialog();
        }else if(menuItem.getItemId()==R.id.aboutUs){
            startActivity(new Intent(MainActivity.this,AboutUs.class));
        }else if(menuItem.getItemId()==R.id.privacyPolicy){
            startActivity(new Intent(MainActivity.this,PrivacyPolicy.class));
        }
        return true;
    }

    private void fn_checkpermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.WRITE_EXTERNAL_STORAGE") == 0 || ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.READ_EXTERNAL_STORAGE") == 0) {
            Log.e("Else", "Else");
            fn_video();
        } else if (!ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.WRITE_EXTERNAL_STORAGE") || !ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.READ_EXTERNAL_STORAGE")) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"}, 100);
        }
    }

    private void fn_video() {
        Cursor query = getApplicationContext().getContentResolver().query(Media.EXTERNAL_CONTENT_URI, new String[]{"_data", "bucket_display_name", "_id", "_data"}, null, null, "datetaken DESC");
        int columnIndexOrThrow = query.getColumnIndexOrThrow("_data");
        int columnIndexOrThrow2 = query.getColumnIndexOrThrow("bucket_display_name");
        int columnIndexOrThrow3 = query.getColumnIndexOrThrow("_id");
        int columnIndexOrThrow4 = query.getColumnIndexOrThrow("_data");
        while (query.moveToNext()) {
            String string = query.getString(columnIndexOrThrow);
            Log.e("Column", string);
            Log.e("Folder", query.getString(columnIndexOrThrow2));
            Log.e("column_id", query.getString(columnIndexOrThrow3));
            Log.e("thum", query.getString(columnIndexOrThrow4));
            this.al_video.add(new File(string).getParentFile());
        }
        checkDuplicate();
    }

    public void checkDuplicate() {
        Map hashMap = new HashMap();
        List<Directory> arrayList = new ArrayList<Directory>();
        Iterator it = this.al_video.iterator();

        while (it.hasNext()) {
            File file = (File) it.next();
            if (hashMap.containsKey(file)) {
                hashMap.put(file, Integer.valueOf(((Integer) hashMap.get(file)).intValue() + 1));
            } else {
                hashMap.put(file, Integer.valueOf(1));
            }
        }

        ArrayList list = removeDuplicates(this.al_video);


        for(int i =0;i<list.size();i++){
            File file = (File)list.get(i);
            int count =1;
            try{
               count =  ((Integer)hashMap.get(file)).intValue();
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                Directory d = new Directory(file,count);
                arrayList.add(d);
            }


        }

        if(arrayList.size()==0){
            Toast.makeText(this, "No videos Found", Toast.LENGTH_SHORT).show();
        }


        this.obj_adapter = new adapter_directory(getApplicationContext(),arrayList, this);
        this.recyclerView.setAdapter(this.obj_adapter);
    }

    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list)
    {

        // Create a new ArrayList
        ArrayList<T> newList = new ArrayList<T>();

        // Traverse through the first list
        for (T element : list) {

            // If this element is not present in newList
            // then add it
            if (!newList.contains(element)) {

                newList.add(element);
            }
        }

        // return the new list
        return newList;
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 100) {
            i = 0;
            while (i < iArr.length) {
                if (iArr[i] != 0) {
                    Toast.makeText(this, "The app was not allowed", Toast.LENGTH_SHORT).show();
                } else {
                    fn_video();
                }
                i++;
            }
        }
    }

    public void showAlertDialog() {
        final View inflate = getLayoutInflater().inflate(R.layout.dialog, null);
        AlertDialog.Builder positiveButton = new AlertDialog.Builder(this).setCancelable(true).setPositiveButton((CharSequence) "ok", new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                String url = ((EditText) inflate.findViewById(R.id.et_url)).getText().toString();
                Intent intent = new Intent(MainActivity.this, videoplayer.class);
                intent.putExtra("url", "live");
                intent.putExtra("address", url);
                dialogInterface.cancel();
                MainActivity.this.startActivity(intent);
            }
        });
        positiveButton.setTitle((CharSequence) "Live Stream");
        positiveButton.setView(inflate);
        positiveButton.setCancelable(true);
        positiveButton.create().show();
    }

    public void initDrawerMenu(boolean enable) {

        mToolbar = findViewById(R.id.toolbar);




        try {

            setSupportActionBar(mToolbar);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            mToolbar.setTitle(getString(R.string.app_name));
        }catch (Exception e){
            e.printStackTrace();
        }

        // Initialize drawer view
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        if(enable) {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle
                    (this, mDrawerLayout, mToolbar, R.string.openDrawer, R.string.closeDrawer) {
                public void onDrawerClosed(View view) {
                    super.onDrawerClosed(view);

                }

                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);

                }
            };

            mDrawerLayout.setDrawerListener(toggle);
            toggle.syncState();

            mNavigationView = (NavigationView) findViewById(R.id.navigationDrawer);
            mNavigationView.setItemIconTintList(null);
            getNavigationView().setNavigationItemSelectedListener(this);
        } else {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_youtube_fullscreen) {

            //youtube Intent

            startActivity(new Intent(MainActivity.this,WebActivity.class).putExtra("url",getString(R.string.site_youtube_url)));
            //showAdAndInvokeSinglePage(getString(R.string.site_youtube), getString(R.string.site_youtube_url));
        }

        // support
        else if (id == R.id.action_call) {
            AppUtils.makePhoneCall(mActivity, Constant.CALL_NUMBER);
        } else if (id == R.id.action_message) {
            AppUtils.sendSMS(mActivity, Constant.CALL_NUMBER, Constant.SMS_TEXT);
        } else if (id == R.id.action_messenger) {
            AppUtils.invokeMessengerBot(mActivity);
        } else if (id == R.id.action_email) {
            AppUtils.sendEmail(mActivity, Constant.EMAIL_ADDRESS, Constant.EMAIL_SUBJECT, Constant.EMAIL_BODY);
        }

        // others
        else if (id == R.id.action_share) {
            AppUtils.shareApp(mActivity);
        } else if (id == R.id.action_rate_app) {
            AppUtils.rateThisApp(mActivity); // this feature will only work after publish the app
        } else if (id == R.id.action_exit) {
            AppUtils.appClosePrompt(mActivity);
        }

        if (getDrawerLayout() != null && getDrawerLayout().isDrawerOpen(GravityCompat.START)) {
            getDrawerLayout().closeDrawer(GravityCompat.START);
        }

        return true;

    }


    @Override
    public void onBackPressed() {
        if (getDrawerLayout() != null && getDrawerLayout().isDrawerOpen(GravityCompat.START)) {
            getDrawerLayout().closeDrawer(GravityCompat.START);
        }

        super.onBackPressed();
    }

    public DrawerLayout getDrawerLayout(){
        return  mDrawerLayout;
    }

    public NavigationView getNavigationView(){
        return mNavigationView;
    }
}

