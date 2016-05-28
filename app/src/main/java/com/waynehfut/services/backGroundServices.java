package com.waynehfut.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.waynehfut.easyconnect.ChatHistory;
import com.waynehfut.easyconnect.ChatHistoryLab;
import com.waynehfut.easyconnect.Connection;
import com.waynehfut.easyconnect.MQTTSubFragment;
import com.waynehfut.easyconnect.NewMessageNotification;
import com.waynehfut.easyconnect.R;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Date;

/**
 * Created by Wayne on 2016/5/28.
 * Site:www.waynehfut.com
 * Mail:waynehfut@gmail.com
 */
public class BackGroundServices extends Service {

    private Connection connection = Connection.getConnection();
    private MqttClient mqttClient = connection.getMqttClient();
    private MQTTSubFragment subFragment = MQTTSubFragment.newInstance();
    private ChatHistoryLab chatHistoryLab;
    private OnMessageCallBack onMessageCallBack;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*
    * 服务第一次创建调用
    * */
    @Override
    public void onCreate() {
        super.onCreate();
    }

    /*
    * 每次启动都会调用
    * */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        chatHistoryLab = ChatHistoryLab.getsChatHistoryLab(getApplicationContext());
        mqttClient = connection.getMqttClient();
        mqttClient.setCallback(new MqttCallback() {
            int messageNumber = 0;
            @Override
            public void connectionLost(Throwable throwable) {
                try {
                    mqttClient.connect();
                } catch (Exception p) {
                    p.printStackTrace();
                }
            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
//                depressContext = msgLzHelper.DecompressLZ77(mqttMessage.toString());
                // TODO: 2016/5/24 lz77 depress
                NewMessageNotification.notify(getApplicationContext(), getString(R.string.messageRecieved, mqttMessage.toString(), topic), messageNumber++);
                ChatHistory chatHistory = new ChatHistory();
                chatHistory.setChatClientId(connection.getmTopic());
                chatHistory.setChatContext(mqttMessage.toString());
                chatHistory.setChatDate(new Date());
                chatHistory.setChatType("Income");
                chatHistoryLab.addChatHistory(chatHistory);
                subFragment.updateChatUI();
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                ChatHistory chatHistory = new ChatHistory();
                chatHistory.setChatClientId(connection.getmTopic());
                chatHistory.setChatContext(connection.getPubContext());
                chatHistory.setChatDate(new Date());
                chatHistory.setChatType("Outcome");
                chatHistoryLab.addChatHistory(chatHistory);
                subFragment.updateChatUI();
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public interface OnMessageCallBack {
        void updateByServices();
    }

    public class MessageBinder extends Binder {
        public BackGroundServices getService() {
            return BackGroundServices.this;
        }
    }
}
