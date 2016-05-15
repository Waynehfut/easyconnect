package com.waynehfut.easyconnect;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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
    private static MQTTConnectFragment smqttConnectFragment;
    Connection connection = Connection.getConnection();
    private EditText mServerId;
    private EditText mPort;
    private EditText mClientId;
    private CheckBox mIsRemember;
    private String brokerURL = null;
    private TextView mConnStatus;
    private MqttClient mqttClient;
    private Connection.ConnectionStatus connectStatus = Connection.ConnectionStatus.DISCONNECTED;
    private EasyHistory easyConnectHistory = new EasyHistory();
    private EasyHistoryLab easyHistoryLab;
    private EasyConnectFragment easyConnectFragment;
    private HistoryAddCallback historyAddCallback;
    private TextView connectText;


    public MQTTConnectFragment() {


    }

    public static MQTTConnectFragment newInstance() {
        if (smqttConnectFragment == null)
            smqttConnectFragment = new MQTTConnectFragment();
        return smqttConnectFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        historyAddCallback = (HistoryAddCallback) activity;
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
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
            connectText = (TextView) view.findViewById(R.id.connect_status_text);

            easyConnectFragment = EasyConnectFragment.newInstance();
            /*
            * 记住我
            * */
            mIsRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        connection.setRemember(true);
                    } else {
                        connection.setRemember(false);
                    }
                }
            });
            if (connection.getConnectionStatus() == Connection.ConnectionStatus.CONNECTED) {
                fab.hide();
                disFab.show();
                connection.setServerId(mServerId.getText().toString());
                connection.setPort(mPort.getText().toString());
                connection.setClientId(mClientId.getText().toString());
                mConnStatus.setBackground(getResources().getDrawable(R.drawable.ic_connect));
                connectText.setText(getString(R.string.connect));

            }
            if (connection.getConnectionStatus() == Connection.ConnectionStatus.DISCONNECTED) {
                mConnStatus.setBackground(getResources().getDrawable(R.drawable.ic_disconnect));
                connectText.setText(getString(R.string.disconnected));
            }

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {

                        brokerURL = "tcp://" + mServerId.getText().toString() + ":" + mPort.getText().toString();
                        connection.connectServer(brokerURL, mClientId.getText().toString());
                        updateHistorydateAndUI(getString(R.string.new_connection), " ", Connection.ConnectionStatus.NEWCONNECT, new EasyHistory());
                        Snackbar.make(view, getString(R.string.con_success) + mServerId.getText().toString() + getString(R.string.to_string) + mClientId.getText().toString(), Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();

                        connection.setConnectionStatus(Connection.ConnectionStatus.CONNECTED);

                        mConnStatus.setBackground(getResources().getDrawable(R.drawable.ic_connect));
                        connectText.setText(getString(R.string.connect));
                        fab.hide();
                        disFab.show();
                        updateHistorydateAndUI(getString(R.string.con_success) + mServerId.getText().toString() + ":" + mPort.getText().toString(), getString(R.string.to_string) + mClientId.getText().toString(), Connection.ConnectionStatus.CONNECTED, new EasyHistory());
                    } catch (Exception e) {
                        updateHistorydateAndUI(getString(R.string.failure_connect), e.toString().substring(0, 32), Connection.ConnectionStatus.DISCONNECTED, new EasyHistory());
                        connection.setConnectionStatus(Connection.ConnectionStatus.DISCONNECTED);

                        Snackbar.make(view, getString(R.string.conn_fail) + e.toString(), Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                        Log.i(TAG, e.toString());
                        connection.clear();

                        mConnStatus.setBackground(getResources().getDrawable(R.drawable.ic_disconnect));
                        connection.setConnectionStatus(Connection.ConnectionStatus.DISCONNECTED);
                        connectText.setText(getString(R.string.disconnected));
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
                                        mConnStatus.setBackground(getResources().getDrawable(R.drawable.ic_disconnect));
                                        updateHistorydateAndUI(getString(R.string.disconnected), " ", Connection.ConnectionStatus.DISCONNECTED, new EasyHistory());
                                        connectText.setText(getString(R.string.disconnected));
                                        connection.setConnectionStatus(Connection.ConnectionStatus.DISCONNECTED);
                                    } catch (MqttException e) {
                                        String errorInfo = e.toString();
                                        connectText.setText(getString(R.string.failure_disconnect) + errorInfo);
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
                        mConnStatus.setBackground(getResources().getDrawable(R.drawable.ic_connect));
                        connectText.setText(getString(R.string.connect));
                        break;
                    case DISCONNECTED:
                        mConnStatus.setBackground(getResources().getDrawable(R.drawable.ic_disconnect));
                        connectText.setText(getString(R.string.disconnected));
                        break;
                    default:
                        connectText.setText(getString(R.string.disconnected));
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
        } else {
            /*
            * 实现二维码页面
            * */
            View view = inflater.inflate(R.layout.new_connect, container, false);
            return view;
        }
    }

    private void updateHistorydateAndUI(String historyTitle, String historySubTitle, Connection.ConnectionStatus newconnect, EasyHistory easyConnectHistory) {
        easyConnectHistory.setHistoryTitle(historyTitle);
        easyConnectHistory.setHistorySubTitle(historySubTitle);
        easyConnectHistory.setHisType(newconnect);
        historyAddCallback.onHistoryAdd();
        easyHistoryLab.addHistory(easyConnectHistory);
    }

    public interface HistoryAddCallback {
        void onHistoryAdd();
    }
}
