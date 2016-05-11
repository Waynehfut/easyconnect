package com.waynehfut.easyconnect;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.MqttClient;

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
    private TextView mConnStatus;
    private MqttClient mqttClient;
    private Connection.ConnectionStatus connectStatus;

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
        mConnStatus = (TextView) view.findViewById(R.id.connect_status);
        mIsRemember = (CheckBox) view.findViewById(R.id.isRemember);
        mIsRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    connection.setServerId(mServerId.getText().toString());
                    connection.setPort(mPort.getText().toString());
                    connection.setClientId(mClientId.getText().toString());
                    connection.setRemember(true);
                }
                else {
                    connection.setRemember(false);
                }
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    brokerURL = "tcp://" + mServerId.getText().toString() + ":" + mPort.getText().toString();
                    Snackbar.make(view, getString(R.string.con_success) + mServerId.getText().toString() + getString(R.string.to_string) + mClientId.getText().toString(), Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    connection.connectServer(brokerURL, mClientId.getText().toString());
                    connection.setConnectionStatus(Connection.ConnectionStatus.CONNECTED);
                    mConnStatus.setText(getString(R.string.connect));
                    mConnStatus.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));

                } catch (Exception e) {
                    connection.setConnectionStatus(Connection.ConnectionStatus.DISCONNECTED);
                    Snackbar.make(view, getString(R.string.conn_fail) + mServerId.getText().toString(), Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    connection.clear();
                    mConnStatus.setText(getString(R.string.Disconnect));
                    mConnStatus.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                }
            }
        });
        if (connection.isRemember()) {
            mServerId.setText(connection.getServerId());
            mClientId.setText(connection.getClientId());
            mPort.setText(connection.getPort());
            mIsRemember.setChecked(true);
            switch (connection.getConnectionStatus()) {
                case CONNECTED:
                    mConnStatus.setText(getString(R.string.connect));
                    mConnStatus.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                    break;
                case DISCONNECTED:
                    mConnStatus.setText(getString(R.string.Disconnect));
                    mConnStatus.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                    break;
                default:
                    mConnStatus.setText(getString(R.string.no_status));
            }
        }else {
            mServerId.setText("");
            mClientId.setText("");
            mPort.setText("");
            mIsRemember.setChecked(false);
        }
        if (!mIsRemember.isChecked()||connection.getConnectionStatus()== Connection.ConnectionStatus.DISCONNECTED){
            mServerId.clearComposingText();
            mClientId.clearComposingText();
            mPort.clearComposingText();
            mIsRemember.setChecked(false);
        }

        return view;
    }
}
