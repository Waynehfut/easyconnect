package com.waynehfut.easyconnect;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class AboutMeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, getString(R.string.ask_toast), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                sendMailByIntent();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public int sendMailByIntent() {
        Intent data = new Intent(Intent.ACTION_SENDTO);
        data.setData(Uri.parse("mailto:" + getString(R.string.mail_address)));
        data.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_subject));
        data.putExtra(Intent.EXTRA_TEXT, getString(R.string.mail_contect));
        startActivity(data);
        return 1;
    }
}
