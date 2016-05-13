package com.waynehfut.easyconnect;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by Wayne on 2016/5/8.
 * Site:www.waynehfut.com
 * Mail:waynehfut@gmail.com
 */
public class MQTTPubFragment extends Fragment {
    private static final String TAG = "MQTTPubFragment";
    private static MQTTPubFragment smqttPubFragment;
    MqttClient mqttClient;
    private EditText mTopicId;
    private EditText mMessage;
    private RadioGroup mQOSRadioGroup;
    private int mQOS = 0;
    private CheckBox mIsHoldConn;
    private Connection connection = Connection.getConnection();

    public static MQTTPubFragment newInstance() {
        if (smqttPubFragment == null)
            smqttPubFragment = new MQTTPubFragment();
        return smqttPubFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.new_pub, container, false);
        mTopicId = (EditText) view.findViewById(R.id.pub_topic);
        mMessage = (EditText) view.findViewById(R.id.pub_context);
        mQOSRadioGroup = (RadioGroup) view.findViewById(R.id.qosRadio);
        mQOSRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int newCheckedId = group.getCheckedRadioButtonId();
                switch (newCheckedId) {
                    case R.id.qos0:
                        mQOS = 0;
                        break;
                    case R.id.qos1:
                        mQOS = 1;
                        break;
                    case R.id.qos2:
                        mQOS = 2;
                        break;
                }
            }
        });
        mIsHoldConn = (CheckBox) view.findViewById(R.id.isRetained);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.verifyYesButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    connection.publishMessage(mTopicId.getText().toString(), mMessage.getText().toString(), mQOS);
                } catch (Exception e) {
                    Snackbar.make(view, getString(R.string.toast_pub_failed, mMessage.getText().toString(), mTopicId.getText().toString()) + connection.getServerId(), Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
            }
        });
        if (connection.getMqttClient() != null) {
            mqttClient = connection.getMqttClient();
            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {

                }

                @Override
                public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                    Snackbar.make(view, getString(R.string.messageRecieved, mqttMessage.toString(), topic) + mqttMessage.toString(), Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                    connection.setmPubTopic(mTopicId.getText().toString());
                    Snackbar.make(view, getString(R.string.toast_pub_success, mMessage.getText().toString(), mTopicId.getText().toString()), Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
            });
        } else {
            new AlertDialog.Builder(getContext())
                    .setTitle(getString(R.string.share_alter_dialog))
                    .setMessage(getString(R.string.not_connect_yet))
                    .setPositiveButton(getString(R.string.yes_btn), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().setTitle(R.string.new_connection);
                            getFragmentManager().beginTransaction().replace(R.id.app_bar_easy_connect, MQTTConnectFragment.newInstance()).commit();

                        }
                    })
                    .setNegativeButton(getString(R.string.no_btn), null)
                    .show();
        }
        return view;
    }

    public void pubSnakBarMessage(String tosat) {
        Toast.makeText(getContext(), tosat, Toast.LENGTH_SHORT);
    }
}
