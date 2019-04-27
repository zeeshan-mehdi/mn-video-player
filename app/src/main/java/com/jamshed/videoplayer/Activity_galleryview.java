package com.jamshed.videoplayer;

import android.app.Activity;
import android.os.Bundle;
import android.widget.VideoView;

public class Activity_galleryview extends Activity {
    String str_video;
    VideoView vv_video;

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_galleryview);
        init();
    }

    private void init() {
        this.vv_video = (VideoView) findViewById(R.id.vv_video);
        this.str_video = getIntent().getStringExtra("video");
        this.vv_video.setVideoPath(this.str_video);
        this.vv_video.start();
    }
}
