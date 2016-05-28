package com.waynehfut.easyconnect;

import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.waynehfut.Lz77.MsgLzHelper;
import com.waynehfut.services.BackGroundServices;
import com.waynehfut.zxing.android.CaptureActivity;

import org.eclipse.paho.client.mqttv3.MqttClient;

import java.util.Date;
import java.util.UUID;

public class EasyConnectActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MQTTConnectFragment.HistoryAddCallback{
    private static final String TAG = "EasyConnectActivity";
    private static final int REQUEST_CODE_SCAN = 0x0000;
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";
    MQTTConnectFragment netConnectFragment;
    EasyConnectFragment easyConnectFragment;
    MQTTPubFragment pubMessageFragment;
    MQTTSubFragment subTopicFragment;
    MqttClient mqttClient;
    Fragment currentFragement;
    Connection connection = Connection.getConnection();
    private long exitTime = 0;
    private ChatHistoryLab chatHistoryLab;

    private MsgLzHelper msgLzHelper = new MsgLzHelper();
    private String depressTopic;
    private String depressContext;
    private BackGroundServices backGroundServices;
    private BackGroundServices.MessageBinder messageBinder;


    private ServiceConnection messageNotification = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            messageBinder=(BackGroundServices.MessageBinder)service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    public void onHistoryAdd() {
        Intent testIntent = new Intent(this, BackGroundServices.class);
        easyConnectFragment.updateUI();
        bindService(testIntent, messageNotification, Context.BIND_AUTO_CREATE);
        startService(testIntent);
    }

    @Override
    protected void onRestart() {
        showOnlyOne(subTopicFragment);
        super.onRestart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        netConnectFragment = MQTTConnectFragment.newInstance();
        easyConnectFragment = EasyConnectFragment.newInstance();
        pubMessageFragment = MQTTPubFragment.newInstance();
        subTopicFragment = MQTTSubFragment.newInstance();
        chatHistoryLab = ChatHistoryLab.getsChatHistoryLab(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_connect);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.app_bar_easy_connect);

        if (fragment == null) {
            fragment = easyConnectFragment;
            if (getSupportFragmentManager().findFragmentByTag("Index") == null)
                getSupportFragmentManager().beginTransaction().add(R.id.app_bar_easy_connect, fragment, "Index").commit();
            if (getSupportFragmentManager().findFragmentByTag("Connect") == null)
                getSupportFragmentManager().beginTransaction().add(R.id.app_bar_easy_connect, netConnectFragment, "Sub").commit();
            if (getSupportFragmentManager().findFragmentByTag("Sub") == null)
                getSupportFragmentManager().beginTransaction().add(R.id.app_bar_easy_connect, subTopicFragment, "Sub").commit();
            if (getSupportFragmentManager().findFragmentByTag("Pub") == null)
                getSupportFragmentManager().beginTransaction().add(R.id.app_bar_easy_connect, pubMessageFragment, "Sub").commit();
            showOnlyOne(easyConnectFragment);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
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
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(EasyConnectActivity.this,
                    CaptureActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SCAN);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*
        * 扫描结果
        * */
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(DECODED_CONTENT_KEY);
                Gson gson = new Gson();
                QRcodeInfo qRcodeInfo = new QRcodeInfo();
                qRcodeInfo = gson.fromJson(content, QRcodeInfo.class);
                try {
                    connection.connectServer(qRcodeInfo.getUrl(), qRcodeInfo.getTopic() + new Date());
                    connection.setmTopic(qRcodeInfo.getTopic());
                    connection.setServerId(qRcodeInfo.getUrl().substring(6, qRcodeInfo.getUrl().length() - 5));
                    connection.setPort(qRcodeInfo.getUrl().substring(qRcodeInfo.getUrl().length() - 4, qRcodeInfo.getUrl().length()));
                    connection.setConnectionStatus(Connection.ConnectionStatus.CONNECTED);
                    connection.setClientId(qRcodeInfo.getTopic() + new Date());
                    showOnlyOne(subTopicFragment);
                    onHistoryAdd();
                    subTopicFragment.updateChatUI();
                    netConnectFragment.updateDataOnConcStatus();

                    ServerHistory serverHistory = new ServerHistory(UUID.randomUUID());
                    serverHistory.setmServer(connection.getServerId());
                    serverHistory.setmPort(connection.getPort());
                    ServerHistoryLab.get(getApplicationContext()).addServer(serverHistory);

                    View view = getWindow().getDecorView();
                    TextView textView = (TextView) view.findViewById(R.id.sub_topic);
                    textView.setText(qRcodeInfo.getTopic());
                    Toast.makeText(this, getString(R.string.scan_success), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.i(TAG, e.toString());
                    Toast.makeText(this, getString(R.string.scan_fail), Toast.LENGTH_SHORT).show();
                }


            }
        } else if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_CANCELED) {
            Toast.makeText(this, getString(R.string.scan_not_url), Toast.LENGTH_SHORT).show();
        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    /*这个方法来实现导航栏*/
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_index) {
            setTitle(R.string.app_name);
            showOnlyOne(easyConnectFragment);
        } else if (id == R.id.nav_new_connect) {
            setTitle(R.string.new_connection);
            showOnlyOne(netConnectFragment);
        } else if (id == R.id.nav_pub_msg) {
            if (connection.getConnectionStatus() == Connection.ConnectionStatus.CONNECTED) {
                setTitle(R.string.publish_title);
                showOnlyOne(pubMessageFragment);
            } else {
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.share_alter_dialog))
                        .setMessage(getString(R.string.not_connect_yet))
                        .setPositiveButton(getString(R.string.yes_btn), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setTitle(R.string.new_connection);
                                showOnlyOne(netConnectFragment);

                            }
                        })
                        .setNegativeButton(getString(R.string.no_btn), null)
                        .show();
            }

        } else if (id == R.id.nav_sub_msg) {
            if (connection.getConnectionStatus() == Connection.ConnectionStatus.CONNECTED) {
                setTitle(R.string.subscribe_title);
                subTopicFragment.updateChatUI();
                showOnlyOne(subTopicFragment);
            } else {

                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.share_alter_dialog))
                        .setMessage(getString(R.string.not_connect_yet))
                        .setPositiveButton(getString(R.string.yes_btn), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setTitle(R.string.new_connection);
                                showOnlyOne(netConnectFragment);
                            }
                        })
                        .setNegativeButton(getString(R.string.no_btn), null)
                        .show();

            }
        } else if (id == R.id.nav_app_setting) {
            Intent intent = new Intent(this, SettingActivity.class);
            intent.putExtra("Ringtone", Notification.DEFAULT_SOUND);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            if (connection.getmPubTopic() != null) {
                shareTopic();
            } else if (Connection.getConnection().getConnectionStatus() == Connection.ConnectionStatus.DISCONNECTED) {
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.share_alter_dialog))
                        .setMessage(getString(R.string.not_connect_yet))
                        .setPositiveButton(getString(R.string.yes_btn), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setTitle(R.string.new_connection);
                                showOnlyOne(netConnectFragment);

                            }
                        })
                        .setNegativeButton(getString(R.string.no_btn), null)
                        .show();
            } else if (connection.getConnectionStatus() == Connection.ConnectionStatus.CONNECTED) {
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.share_alter_dialog))
                        .setMessage(getString(R.string.no_topic_to_share))
                        .setPositiveButton(getString(R.string.yes_btn), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setTitle(R.string.publish_title);
                                showOnlyOne(pubMessageFragment);

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

    public void switchContent(Fragment from, Fragment to) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().setCustomAnimations(
                android.R.anim.fade_in, android.R.anim.fade_out);
        if (!to.isAdded()) {    // 先判断是否被add过
            transaction.hide(from).add(R.id.app_bar_easy_connect, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
        } else {
            transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
        }

    }

    public void showOnlyOne(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().hide(netConnectFragment).commit();
        getSupportFragmentManager().beginTransaction().hide(subTopicFragment).commit();
        getSupportFragmentManager().beginTransaction().hide(pubMessageFragment).commit();
        getSupportFragmentManager().beginTransaction().hide(easyConnectFragment).commit();
        getSupportFragmentManager().beginTransaction().show(fragment).commit();
    }

    public void showIndex() {
        showOnlyOne(easyConnectFragment);
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

