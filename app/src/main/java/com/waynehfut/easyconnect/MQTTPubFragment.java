package com.waynehfut.easyconnect;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Date;

/**
 * Created by Wayne on 2016/5/8.
 * Site:www.waynehfut.com
 * Mail:waynehfut@gmail.com
 */
public class MQTTPubFragment extends Fragment {
    private static final String TAG = "MQTTPubFragment";
    private static MQTTPubFragment smqttPubFragment;
    private static MQTTSubFragment smqttSubFragment;
    MqttClient mqttClient;
    private EditText mTopicId;
    private EditText mMessage;
    private RadioGroup mQOSRadioGroup;
    private ChatHistoryLab chatHistoryLab;
    private PubCallBacks pubCallBacks;
    private int mQOS = 0;
    //    private CheckBox mIsHoldConn;
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        pubCallBacks = (PubCallBacks) activity;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        chatHistoryLab = ChatHistoryLab.getsChatHistoryLab(getContext());
        final View view = inflater.inflate(R.layout.new_pub, container, false);
        mTopicId = (EditText) view.findViewById(R.id.pub_topic);
        mMessage = (EditText) view.findViewById(R.id.pub_context);
        mQOSRadioGroup = (RadioGroup) view.findViewById(R.id.qosRadio);
        smqttSubFragment = MQTTSubFragment.newInstance();
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
//        mIsHoldConn = (CheckBox) view.findViewById(R.id.isRetained);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.verifyYesButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    connection.publishMessage(mTopicId.getText().toString(), mMessage.getText().toString(), mQOS);
                } catch (MqttException e) {
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
                    try {
                        mqttClient.connect();
                    } catch (Exception p) {
                        updateSubUIafterPub(mTopicId.getText().toString(), "LostConnect", new Date(), "Income", new ChatHistory());
                        p.printStackTrace();
                    }
                }

                @Override
                public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                    NewMessageNotification.notify(getContext(), getString(R.string.messageRecieved, mqttMessage.toString(), topic), 1);
                    updateSubUIafterPub(topic, mqttMessage.toString(), new Date(), "Income", new ChatHistory());
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                    connection.setmPubTopic(mTopicId.getText().toString());
                    Snackbar.make(view, getString(R.string.toast_pub_success, mMessage.getText().toString(), mTopicId.getText().toString()), Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    updateSubUIafterPub(mTopicId.getText().toString(), mMessage.getText().toString(), new Date(), "Outcome", new ChatHistory());

                }
            });
        }
        return view;
    }

    private void updateSubUIafterPub(String clientId, String chatContex, Date chatDate, String chatType, ChatHistory chatHistory) {
        chatHistory.setChatClientId(clientId);
        chatHistory.setChatContext(chatContex);
        chatHistory.setChatDate(chatDate);
        chatHistory.setChatType(chatType);
        chatHistoryLab.addChatHistory(chatHistory);
        pubCallBacks.updateSubUIAfterPub();

    }

    public interface PubCallBacks {
        void updateSubUIAfterPub();
    }
}
