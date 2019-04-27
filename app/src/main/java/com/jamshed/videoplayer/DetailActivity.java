package com.jamshed.videoplayer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSIONS = 100;
    ArrayList<Model_Video> al_video = new ArrayList();
    ArrayList<String> all_paths = new ArrayList();
    Adapter_VideoFolder obj_adapter;
    RecyclerView recyclerView;
    LayoutManager recyclerViewLayoutManager;

    protected void onCreate(Bundle bundle) {
        try {

            super.onCreate(bundle);
            setContentView(R.layout.activity_detail);

//        Toolbar toolbar = findViewById(R.id.my_toolbarD);
//        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
//        toolbar.getOverflowIcon().setColorFilter(ContextCompat.getColor(this, android.R.color.white), Mode.SRC_ATOP);
//        setSupportActionBar(toolbar);
            init();
            showAdd();
            fbAdd();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void showAdd() {
        ((AdView) findViewById(R.id.adView1)).loadAd(new AdRequest.Builder().build());
    }

    private void fbAdd() {
//        View adView = new com.facebook.ads.AdView((Context) this, "YOUR_PLACEMENT_ID", AdSize.BANNER_HEIGHT_50);
//        ((LinearLayout) findViewById(R.id.banner_container1)).addView(adView);
//        adView.loadAd();
    }

    private void init() {
        this.recyclerView = (RecyclerView) findViewById(R.id.recycler_viewD);
        this.recyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
        this.recyclerView.setLayoutManager(this.recyclerViewLayoutManager);
        fn_checkpermission();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() ==R.id.network) {
            showAlertDialog();
        }else if(menuItem.getItemId()==R.id.aboutUs){
            startActivity(new Intent(DetailActivity.this,AboutUs.class));
        }else if(menuItem.getItemId()==R.id.privacyPolicy){
            startActivity(new Intent(DetailActivity.this,PrivacyPolicy.class));
        }
        return true;
    }

    private void fn_checkpermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.WRITE_EXTERNAL_STORAGE") == 0 || ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.READ_EXTERNAL_STORAGE") == 0) {
            fn_video();
        } else if (!ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.WRITE_EXTERNAL_STORAGE") || !ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.READ_EXTERNAL_STORAGE")) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"}, 100);
        }
    }

    private void fn_video() {
        File[] listFiles = ((File) getIntent().getSerializableExtra("file")).listFiles();
        for (int i = 0; i < listFiles.length; i++) {
            if (isVideo(listFiles[i].getName())) {
                this.al_video.add(new Model_Video(listFiles[i].getName(), listFiles[i].getPath(), listFiles[i].getPath(), false));
                this.all_paths.add(listFiles[i].getPath());
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("size: ");
        stringBuilder.append(String.valueOf(this.al_video.size()));
        Log.v("arrayLength", stringBuilder.toString());
        this.obj_adapter = new Adapter_VideoFolder(getApplicationContext(), this.al_video, this, this.all_paths);
        this.recyclerView.setAdapter(this.obj_adapter);
    }

    public boolean isVideo(String str) {
        for (String endsWith : new String[]{"mp4", "3gp", "mkv", "webm", "ts"}) {
            if (str.toLowerCase().endsWith(endsWith)) {
                return true;
            }
        }
        return false;
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 100) {
            i = 0;
            while (i < iArr.length) {
                if (iArr[i]!=0) {
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
        AlertDialog.Builder positiveButton = new AlertDialog.Builder(this).setCancelable(true).setPositiveButton((CharSequence) "ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                String url = ((EditText) inflate.findViewById(R.id.et_url)).getText().toString();
                Intent intent = new Intent(DetailActivity.this, videoplayer.class);
                intent.putExtra("url", "live");
                intent.putExtra("address", url);
                dialogInterface.cancel();
                DetailActivity.this.startActivity(intent);
            }
        });
        positiveButton.setTitle((CharSequence) "Live Stream");
        positiveButton.setView(inflate);
        positiveButton.setCancelable(true);
        positiveButton.create().show();
    }
}
