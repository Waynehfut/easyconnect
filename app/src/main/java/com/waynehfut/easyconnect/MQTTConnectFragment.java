package com.waynehfut.easyconnect;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

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
    private Connection.ConnectionStatus connectStatus = Connection.ConnectionStatus.DISCONNECTED;
    private EasyConnectHistory easyConnectHistory = new EasyConnectHistory();
    private EasyHistoryLab easyHistoryLab;
    private EasyConnectFragment easyConnectFragment;

    public MQTTConnectFragment() {


    }

    public static MQTTConnectFragment newInstance() {
        MQTTConnectFragment mqttConnecFragment = new MQTTConnectFragment();
        return mqttConnecFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        * 怎么初始化数据
        * */
        /*UUID easyConnectHistoryId=(UUID)getArguments().getSerializable("history_id");
        easyConnectHistory = EasyHistoryLab.getEasyHistoryLab(getActivity()).getEasyHistory(easyConnectHistoryId);*/
    }

    /*
    * 在创建View时设置监听，并获取填写数据
    * */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (connectStatus == Connection.ConnectionStatus.DISCONNECTED) {
            View view = inflater.inflate(R.layout.new_connect, container, false);
            final FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.verifyYesButton);
            final FloatingActionButton disFab = (FloatingActionButton) view.findViewById(R.id.disConnectBtn);
            mServerId = (EditText) view.findViewById(R.id.connect_server);
            mPort = (EditText) view.findViewById(R.id.connect_port);
            mClientId = (EditText) view.findViewById(R.id.clientId);
            mConnStatus = (TextView) view.findViewById(R.id.connect_status);
            mIsRemember = (CheckBox) view.findViewById(R.id.isRemember);
            easyHistoryLab = EasyHistoryLab.getEasyHistoryLab(getContext());
            easyConnectFragment = EasyConnectFragment.newInstance();
            mIsRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        connection.setServerId(mServerId.getText().toString());
                        connection.setPort(mPort.getText().toString());
                        connection.setClientId(mClientId.getText().toString());
                        connection.setRemember(true);
                    } else {
                        connection.setRemember(false);
                    }
                }
            });
//        updateHistorydateAndUI("New Connect ", " ", Connection.ConnectionStatus.NEWCONNECT, easyConnectHistory);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        brokerURL = "tcp://" + mServerId.getText().toString() + ":" + mPort.getText().toString();
                        connection.connectServer(brokerURL, mClientId.getText().toString());

                        Snackbar.make(view, getString(R.string.con_success) + mServerId.getText().toString() + getString(R.string.to_string) + mClientId.getText().toString(), Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();

                        connection.setConnectionStatus(Connection.ConnectionStatus.CONNECTED);
                        mConnStatus.setText(getString(R.string.connect));
                        mConnStatus.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                        fab.hide();
                        disFab.show();
                        updateHistorydateAndUI(getString(R.string.con_success) + mServerId.getText().toString() + ":" + mPort.getText().toString(), "Client ID is " + mClientId.getText().toString(), Connection.ConnectionStatus.CONNECTED, easyConnectHistory);
                    } catch (Exception e) {
                        updateHistorydateAndUI(getString(R.string.failure_disconnect), e.toString(), Connection.ConnectionStatus.DISCONNECTED, easyConnectHistory);
                        connection.setConnectionStatus(Connection.ConnectionStatus.DISCONNECTED);

                        Snackbar.make(view, getString(R.string.conn_fail) + mServerId.getText().toString(), Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();

                        connection.clear();
                        mConnStatus.setText(getString(R.string.Disconnect));
                        mConnStatus.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                    }
                }
            });

            disFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(getContext())
                            .setTitle(getString(R.string.disconnectClient))
                            .setMessage(getString(R.string.disconnectClient))
                            .setPositiveButton(getString(R.string.yes_btn), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        connection.disConnectServer();
                                        mConnStatus.setText(getString(R.string.disconnected));
                                        mConnStatus.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                                        updateHistorydateAndUI(getString(R.string.disconnected), " ", Connection.ConnectionStatus.DISCONNECTED, easyConnectHistory);
                                    } catch (MqttException e) {
                                        String errorInfo = e.toString();
                                        mConnStatus.setText(getString(R.string.failure_disconnect) + errorInfo);
                                    } finally {
                                        disFab.hide();
                                        fab.show();
                                    }

                                }
                            })
                            .setNegativeButton(getString(R.string.no_btn), null)
                            .show();
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
            } else {
                mServerId.setText("");
                mClientId.setText("");
                mPort.setText("");
                mIsRemember.setChecked(false);
            }
            if (!mIsRemember.isChecked() || connection.getConnectionStatus() == Connection.ConnectionStatus.DISCONNECTED) {
                mServerId.clearComposingText();
                mClientId.clearComposingText();
                mPort.clearComposingText();
                mIsRemember.setChecked(false);
            }

            return view;
        }
        else {
            /*
            * 实现二维码页面
            * */
            View view = inflater.inflate(R.layout.new_connect, container, false);
            return view;
        }
    }

    private void updateHistorydateAndUI(String historyTitle, String historySubTitle, Connection.ConnectionStatus newconnect, EasyConnectHistory easyConnectHistory) {
        easyConnectHistory.setHistoryTitle(historyTitle);
        easyConnectHistory.setHistorySubTitle(historySubTitle);
        easyConnectHistory.setHisType(newconnect);
        easyHistoryLab.addHistory(easyConnectHistory);
    }
}
