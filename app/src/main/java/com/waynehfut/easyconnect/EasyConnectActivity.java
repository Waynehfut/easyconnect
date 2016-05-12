package com.waynehfut.easyconnect;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class EasyConnectActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "EasyConnectActivity";
    Fragment netConnectFragment;
    Fragment easyConnectFragment;
    Fragment pubMessageFragment;
    Fragment subTopicFragment;
    private long exitTime = 0;
//    Fragment softwareSettingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        netConnectFragment = MQTTConnectFragment.newInstance();
        easyConnectFragment = EasyConnectFragment.newInstance();
        pubMessageFragment = MQTTPubFragment.newInstance();
        subTopicFragment = MQTTSubFragment.newInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_connect);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                View view = getCurrentFocus();
//                Toast.makeText(getApplicationContext(), R.string.press_bck, Toast.LENGTH_SHORT).show();
                Snackbar.make(view, getString(R.string.press_bck), Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                onBackPressed();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    /*
    * 顶部按钮
    * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.easy_connect, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    /*这个方法来实现导航栏*/
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_index) {
            setTitle(R.string.app_name);
            getSupportFragmentManager().beginTransaction().replace(R.id.app_bar_easy_connect, easyConnectFragment).commit();
        } else if (id == R.id.nav_new_connect) {
            setTitle(R.string.new_connection);
            getSupportFragmentManager().beginTransaction().replace(R.id.app_bar_easy_connect, netConnectFragment).commit();
        } else if (id == R.id.nav_pub_msg) {
            setTitle(R.string.publish_title);
            getSupportFragmentManager().beginTransaction().replace(R.id.app_bar_easy_connect, pubMessageFragment).commit();
        } else if (id == R.id.nav_sub_msg) {
            setTitle(R.string.subscribe_title);
            getSupportFragmentManager().beginTransaction().replace(R.id.app_bar_easy_connect, subTopicFragment).commit();
        } else if (id == R.id.nav_app_setting) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            if (Connection.getConnection().getmPubTopic() != null) {
                shareTopic();
            } else if (Connection.getConnection().getConnectionStatus() == Connection.ConnectionStatus.DISCONNECTED) {
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.share_alter_dialog))
                        .setMessage(getString(R.string.not_connect_yet))
                        .setPositiveButton(getString(R.string.yes_btn), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setTitle(R.string.new_connection);
                                getSupportFragmentManager().beginTransaction().replace(R.id.app_bar_easy_connect, netConnectFragment).commit();

                            }
                        })
                        .setNegativeButton(getString(R.string.no_btn), null)
                        .show();
            } else {
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.share_alter_dialog))
                        .setMessage(getString(R.string.no_topic_to_share))
                        .setPositiveButton(getString(R.string.yes_btn), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setTitle(R.string.new_connection);
                                getSupportFragmentManager().beginTransaction().replace(R.id.app_bar_easy_connect, pubMessageFragment).commit();

                            }
                        })
                        .setNegativeButton(getString(R.string.no_btn), null)
                        .show();
            }
        } else if (id == R.id.about_me) {
            Intent intent = new Intent(this, AboutMeActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean shareTopic() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, Connection.getConnection().getmPubTopic() + getString(R.string.share_info_context));
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_info_context));
        intent = Intent.createChooser(intent, getString(R.string.share_topic_string));
        startActivity(intent);
        return true;
    }
}
