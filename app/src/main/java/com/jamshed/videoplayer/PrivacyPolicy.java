package com.jamshed.videoplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class PrivacyPolicy extends AppCompatActivity {

    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        textView = findViewById(R.id.textView);

        String html = "<h3>Privacy Policies are legally binding agreements you are required to post on your website if you’re collecting any sort of personal information from your site’s visitors or customers.\n" +
                "\n" +
                "A Privacy Policy is an important legal document that lets users understand the various ways a website might be collecting personal information. The purpose of a Privacy Policy is to inform users of your data collection practices in order to protect the customer’s privacy.\n" +
                "\n" +
                "Your Privacy Policy should disclose how the website/app collects information, how the information is used, whether or not it is shared with third parties and how it is protected and stored.</h3>";

        textView.setText(Html.fromHtml(html));
    }
}
