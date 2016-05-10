package com.waynehfut.easyconnect;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * Created by Wayne on 2016/5/8.
 * Site:www.waynehfut.com
 * Mail:waynehfut@gmail.com
 */
public class MQTTConnectFragment extends Fragment {
    private static final String TAG = "MQTTConnectFragment";
    Connection connection = Connection.getConnection();
    private EditText mServerId;
    private EditText mPort;
    private EditText mClientId;
    private CheckBox mIsRemember;
    private String brokerURL = null;

    public MQTTConnectFragment() {

    }

    public static MQTTConnectFragment newInstance() {
        MQTTConnectFragment mqttConnecFragment = new MQTTConnectFragment();
        return mqttConnecFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    /*
    * 在创建View时设置监听，并获取填写数据
    * */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_connect, container, false);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.verifyYesButton);
        mServerId = (EditText) view.findViewById(R.id.connect_server);
        mPort = (EditText) view.findViewById(R.id.connect_port);
        mClientId = (EditText) view.findViewById(R.id.clientId);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    brokerURL = "tcp://" + mServerId.getText().toString() + ":" + mPort.getText().toString();
                    Snackbar.make(view, getString(R.string.con_success) + mServerId.getText().toString() + getString(R.string.to_string) + mClientId.getText().toString(), Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    connection.connectServer(brokerURL, mClientId.getText().toString());
                } catch (Exception e) {
                    Snackbar.make(view, getString(R.string.conn_fail) + mServerId.getText().toString(), Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                } finally {
                    connection.setServerId(mServerId.getText().toString());
                    connection.setClientId(mClientId.getText().toString());
                    connection.setPort(mPort.getText().toString());
                }
            }
        });
        return view;
    }
}
