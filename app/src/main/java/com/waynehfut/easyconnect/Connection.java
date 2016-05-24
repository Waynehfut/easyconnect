package com.waynehfut.easyconnect;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Wayne on 2016/5/8.
 * Site:www.waynehfut.com
 * Mail:waynehfut@gmail.com
 */

public class Connection {
    private final static String TAG = "Connect";
    /*
            * 基础信息
            * */
    private static Connection connection;
    private String clientHandle = null;
    private String clientId = null;
    private String serverId = null;
    private String port = null;
    private String mPubTopic = null;
    private String mSubTopic = null;
    private String mTopic = null;
    private boolean isRemember = false;
    private ConnectionStatus connectionStatus = ConnectionStatus.DISCONNECTED;
    private ConnectionStatus status = ConnectionStatus.NONE;
    private ArrayList<String> history = null;
    private MqttClient mqttClient = null;
    private String pubContext;
    private MqttConnectOptions mqttConnectOptions;
    private ArrayList<PropertyChangeListener> listeners = new ArrayList<PropertyChangeListener>();
    private boolean sslConnection = false;
    private long persistenceId = -1;
    private Connection() {
    }

    public static Connection getConnection() {
        if (connection == null) {
            connection = new Connection();
        }
        return connection;

    }

    public String getmTopic() {
        return mTopic;
    }

    public void setmTopic(String mTopic) {
        this.mTopic = mTopic;
    }

    public String getPubContext() {
        return pubContext;
    }

    public void setPubContext(String pubContext) {
        this.pubContext = pubContext;
    }

    public String getmPubTopic() {
        return mPubTopic;
    }

    public void setmPubTopic(String mPubTopic) {
        this.mPubTopic = mPubTopic;
    }

    public String getmSubTopic() {
        return mSubTopic;
    }

    public void setmSubTopic(String mSubTopic) {
        this.mSubTopic = mSubTopic;
    }

    public ConnectionStatus getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(ConnectionStatus connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public boolean isRemember() {
        return isRemember;
    }

    public void setRemember(boolean remember) {
        isRemember = remember;
    }

    public MqttClient getMqttClient() {
        return mqttClient;
    }


    public void clear() {
        this.serverId = null;
        this.port = null;
        this.clientId = null;
        this.isRemember = false;
    }


    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }


    /*
    * 是否相等
    * */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Connection)) {
            return false;
        }

        Connection c = (Connection) o;

        return clientHandle.equals(c.clientHandle);

    }

    public String getId() {
        return clientId;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    /*
    * 连接到服务
    * */
    public void connectServer(final String brokerURL, final String clientId) throws MqttException {
        mqttClient = new MqttClient(brokerURL, clientId, new MemoryPersistence());
        mqttConnectOptions = new MqttConnectOptions();
        mqttClient.connect(mqttConnectOptions);

    }

    public void disConnectServer() throws MqttException {
        if (mqttClient.isConnected())
            mqttClient.disconnect();

    }

    public void publishMessage(String topic, String content, int qos) throws MqttException {
        MqttMessage newMessage = new MqttMessage(content.getBytes());
        newMessage.setQos(qos);
        if (mqttClient != null) {
            mqttClient.publish(topic, newMessage);
            mTopic = topic;
        } else {
            throw new MqttPersistenceException(200);
        }

    }

    public void subMessage(String topic) throws Exception {
        if (mqttClient != null) {
            mqttClient.subscribe(topic);
            mTopic = topic;
        } else {
            throw new MqttException(100);
        }
    }

    /*
    * 链接状态
    * */
   public enum ConnectionStatus {
        /*
        * 客户端已经链接
        * */
        CONNECTED,

        /*
        * 客户端已经断开
        * */
        DISCONNECTED,
        /*
        * 出现了错误
        * */
        ERROR,
        /*
        * 状态未知
        * */
        NONE,
        NEWCONNECT

    }
}
