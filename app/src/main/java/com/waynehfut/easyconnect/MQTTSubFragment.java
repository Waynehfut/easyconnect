package com.waynehfut.easyconnect;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by Wayne on 2016/5/8.
 * Site:www.waynehfut.com
 * Mail:waynehfut@gmail.com
 */
public class MQTTSubFragment extends Fragment {
    MqttClient mqttClient;
    private EditText mTopicId;
    private Connection connection = Connection.getConnection();

    public static MQTTSubFragment newInstance() {
        MQTTSubFragment mqttSubFragment = new MQTTSubFragment();
        return mqttSubFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.new_sub, container, false);
        mTopicId = (EditText) view.findViewById(R.id.sub_topic);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.verifyYesButton);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    connection.subMessage(mTopicId.getText().toString());
                    Snackbar.make(view, getString(R.string.toast_sub_success,mTopicId.getText().toString()), Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                } catch (Exception e) {
                    Snackbar.make(view, getString(R.string.toast_sub_failed,mTopicId.getText().toString()), Snackbar.LENGTH_SHORT)
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
                    Snackbar.make(view, getString(R.string.messageRecieved,mqttMessage.toString(),topic)+ mqttMessage.toString(), Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

                }
            });
        } else {
            Snackbar.make(view, getString(R.string.not_connect_yet), Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }
        return view;
    }

}
