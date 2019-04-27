package com.jamshed.videoplayer;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.khizar1556.mkvideoplayer.MKPlayer;
import com.khizar1556.mkvideoplayer.MKPlayer.PlayerGestureListener;

import java.util.ArrayList;

public class videoplayer extends AppCompatActivity {
    private int Index;
    private ArrayList<String> al_paths;
    private MKPlayer player;

    private InterstitialAd mInterstitialAd;

    /* renamed from: com.nx.videoplayer.videoplayer$3 */
    class C06793 implements Runnable {


        C06793() {
        }

        public void run() {
            //Toast.makeText(videoplayer.this.getApplicationContext(), "video play completed", Toast.LENGTH_SHORT).show();
            int size = videoplayer.this.al_paths.size();
            if (size > 0 && videoplayer.this.Index < size - 1) {
                videoplayer.this.Index = videoplayer.this.Index + 1;
                String str = (String) videoplayer.this.al_paths.get(videoplayer.this.Index);
                videoplayer.this.player.play(str);
                videoplayer.this.player.setTitle(videoplayer.this.getName(str));
            }else {
                    videoplayer.this.Index = 0;
                    String str = (String) videoplayer.this.al_paths.get(videoplayer.this.Index);
                    videoplayer.this.player.play(str);
                    videoplayer.this.player.setTitle(videoplayer.this.getName(str));
                }

        }


    }


    class C09395 extends AdListener {
        public void onAdClosed() {
        }

        public void onAdFailedToLoad(int i) {
        }

        public void onAdLeftApplication() {
        }

        public void onAdOpened() {
        }

        C09395() {
        }

        public void onAdLoaded() {
            videoplayer.this.mInterstitialAd.show();
        }
    }


    /* renamed from: com.nx.videoplayer.videoplayer$1 */
    class C09361 implements MKPlayer.OnErrorListener {
        C09361() {
        }

        public void onError(int i, int i2) {
            AlertDialog alertDialog = new AlertDialog.Builder(videoplayer.this).create();
            alertDialog.setTitle("Playback Error ");
            alertDialog.setMessage("Error while playing this video ...");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });
            alertDialog.show();

            //Toast.makeText(videoplayer.this.getApplicationContext(), "video play error",  Toast.LENGTH_SHORT).show();
        }
    }

    /* renamed from: com.nx.videoplayer.videoplayer$2 */
    class C09372 implements MKPlayer.OnInfoListener {
        public void onInfo(int i, int i2) {
            switch (i) {
            }
        }

        C09372() {
        }
    }



    /* renamed from: com.nx.videoplayer.videoplayer$4 */
    class C09384 implements MKPlayer.playerCallbacks {
        C09384() {
        }

        public void onNextClick() {
            int size = videoplayer.this.al_paths.size();
            if (size > 0 && videoplayer.this.Index < size - 1) {
                videoplayer.this.Index = videoplayer.this.Index + 1;
                String str = (String) videoplayer.this.al_paths.get(videoplayer.this.Index);
                videoplayer.this.player.play(str);
                videoplayer.this.player.setTitle(videoplayer.this.getName(str));
            }
        }

        public void onPreviousClick() {
            if (videoplayer.this.al_paths.size() > 0 && videoplayer.this.Index > 0) {
                videoplayer.this.Index = videoplayer.this.Index - 1;
                String str = (String) videoplayer.this.al_paths.get(videoplayer.this.Index);
                videoplayer.this.player.play(str);
                videoplayer.this.player.setTitle(videoplayer.this.getName(str));
            }
        }
    }



    protected void onCreate(Bundle bundle) {
        try {
            super.onCreate(bundle);
            setContentView(R.layout.videoplayer);
            // MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
            this.al_paths = new ArrayList();
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            this.Index = 0;
            this.player = new MKPlayer(this);
            this.player.onComplete(new C06793()).onInfo(new C09372()).onError(new C09361());
            this.player.setPlayerCallbacks(new C09384());


            getIntentData();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void getIntentData() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        String stringExtra;

        if ("android.intent.action.SEND".equals(action)) {
            stringExtra = intent.getStringExtra("android.intent.extra.TEXT");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("rl: ");
            stringBuilder.append(stringExtra);
            Log.v("url gotten", stringBuilder.toString());
            this.player.play(stringExtra);
            this.player.setTitle(getName(stringExtra));
            return;
        }else if(action.equals("android.intent.action.VIEW")&&type !=null){
            if(type.startsWith("video/")){
                Uri uri = intent.getData();
                this.player.play(String.valueOf(uri));
                this.player.setTitle(getName(String.valueOf(uri)));
            }
        }
        if (getIntent().getStringExtra("url").equals("offline")) {
            this.Index = getIntent().getIntExtra("index", 0);
            this.al_paths = getIntent().getStringArrayListExtra("urlarray");
            stringExtra = (String) this.al_paths.get(this.Index);
        } else {
            stringExtra = getIntent().getStringExtra("address");
        }
        this.player.play(stringExtra);
        this.player.setTitle(getName(stringExtra));



    }

    private void showAdd() {
        this.mInterstitialAd = new InterstitialAd(this);
        this.mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        this.mInterstitialAd.loadAd(new AdRequest.Builder().build());
        this.mInterstitialAd.setAdListener(new C09395());
    }

    public String getName(String str) {
        str = str.substring(str.lastIndexOf("/") + 1);
        return str.indexOf(".") > 0 ? str.substring(0, str.lastIndexOf(".")) : str;
    }

    protected void onPause() {
        super.onPause();
        MKPlayer mKPlayer = this.player;
        if (mKPlayer != null) {
            mKPlayer.onPause();
        }
    }

    protected void onResume() {
        super.onResume();
        MKPlayer mKPlayer = this.player;
        if (mKPlayer != null) {
            mKPlayer.onResume();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        MKPlayer mKPlayer = this.player;
        if (mKPlayer != null) {
            mKPlayer.onDestroy();
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        MKPlayer mKPlayer = this.player;
        if (mKPlayer != null) {
            mKPlayer.onConfigurationChanged(configuration);
        }
    }

    public void onBackPressed() {
        showAdd();
        MKPlayer mKPlayer = this.player;
        if (mKPlayer == null || !mKPlayer.onBackPressed()) {
            super.onBackPressed();
        }
    }
}
