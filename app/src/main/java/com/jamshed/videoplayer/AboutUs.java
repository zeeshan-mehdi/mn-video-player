package com.jamshed.videoplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class AboutUs extends AppCompatActivity {

    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        textView = findViewById(R.id.aboutUsText);

        String html = "<h3>This app is developed by Zeeshan Mehdi and The Copy rights of this App are Owned by Jamshed 2019 - onWards </h3>";

        textView.setText(Html.fromHtml(html));
    }
}

