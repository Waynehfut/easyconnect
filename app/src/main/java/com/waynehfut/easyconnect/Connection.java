package com.waynehfut.easyconnect;

import android.content.Context;

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
    MQTTSubFragment mqttSubFragment;
    MQTTPubFragment mqttPubFragment;
    private UUID mUid;
    private String clientHandle = null;
    private String clientId = null;
    private String serverId = null;
    private String port = null;
    private String mPubTopic = null;
    private String mSubTopic = null;
    private boolean isRemember = false;
    private ConnectionStatus connectionStatus = ConnectionStatus.DISCONNECTED;
    private ConnectionStatus status = ConnectionStatus.NONE;
    private ArrayList<String> history = null;
    private MqttClient mqttClient = null;
    private ArrayList<PropertyChangeListener> propertyChangeListeners = new ArrayList<PropertyChangeListener>();
    private Context context = null;
    private MqttConnectOptions mqttConnectOptions;
    private ArrayList<PropertyChangeListener> listeners = new ArrayList<PropertyChangeListener>();
    private boolean sslConnection = false;
    private long persistenceId = -1;

    private Connection() {
    }

    public static synchronized Connection getConnection() {
        if (connection == null) {
            connection = new Connection();
        }
        return connection;

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

    public void setMqttClient(MqttClient mqttClient) {
        this.mqttClient = mqttClient;
    }

    public UUID getmUid() {
        return mUid;
    }

    public void setmUid(UUID mUid) {
        this.mUid = mUid;
    }

    public boolean isSslConnection() {
        return sslConnection;
    }

    public void setSslConnection(boolean sslConnection) {
        this.sslConnection = sslConnection;
    }

    public MqttConnectOptions getMqttConnectOptions() {
        return mqttConnectOptions;
    }

    public void setMqttConnectOptions(MqttConnectOptions mqttConnectOptions) {
        this.mqttConnectOptions = mqttConnectOptions;
    }

    public void clear() {
        this.serverId = null;
        this.port = null;
        this.clientId = null;
        this.isRemember = false;
    }

    public ArrayList<String> getHistory() {
        return history;
    }

    public void setHistory(ArrayList<String> history) {
        this.history = history;
    }

    public ArrayList<PropertyChangeListener> getPropertyChangeListeners() {
        return propertyChangeListeners;
    }

    public void setPropertyChangeListeners(ArrayList<PropertyChangeListener> propertyChangeListeners) {
        this.propertyChangeListeners = propertyChangeListeners;
    }

    public String getClientHandle() {
        return clientHandle;
    }

    public void setClientHandle(String clientHandle) {
        this.clientHandle = clientHandle;
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

    public ConnectionStatus getStatus() {
        return status;
    }

    public void setStatus(ConnectionStatus status) {
        this.status = status;
    }

    public boolean isConnected() {
        return status == ConnectionStatus.CONNECTED;
    }


    /*
    * 将数据转换为字符串
    * */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(clientId);
        sb.append("\n ");
        switch (status) {

            case CONNECTED:
                sb.append(context.getString(R.string.connectedto));
                break;
            case DISCONNECTED:
                sb.append(context.getString(R.string.disconnected));
                break;
            case NONE:
                sb.append(context.getString(R.string.no_status));
                break;
            case CONNECTING:
                sb.append(context.getString(R.string.connecting));
                break;
            case DISCONNECTING:
                sb.append(context.getString(R.string.disconnecting));
                break;
            case ERROR:
                sb.append(context.getString(R.string.connectionError));
                break;
            case NEWCONNECT:
                sb.append("New Connection");
        }
        sb.append(" ");
        sb.append(serverId);
        return sb.toString();
    }

    /*
    * 识别客户端
    * */
    public boolean isHandle(String handle) {
        return clientHandle.equals(handle);
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

    public String getHostName() {

        return serverId;
    }

    /*
    * 是否链接中
    * */
    public boolean isConnectedOrConnecting() {
        return (status == ConnectionStatus.CONNECTED) || (status == ConnectionStatus.CONNECTING);
    }

    /*
    * 验证状态是否有错
    * */
    public boolean noError() {
        return status != ConnectionStatus.ERROR;
    }

    /*
    * 获取客户端信息
    * */


    /*
    * mqtt链接选项
    * */
    public void addConnectionOptions(MqttConnectOptions connectOptions) {
        mqttConnectOptions = connectOptions;

    }

    /*
    * 获取链接选项
    * */
    public MqttConnectOptions getConnectionOptions() {
        return mqttConnectOptions;
    }

    /*
    * 注册属性变化监听器
    * */
    public void registerChangeListener(PropertyChangeListener listener) {
        listeners.add(listener);
    }

    /*
    * 删除监听*/
    public void removeChangeListener(PropertyChangeListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public int isSSL() {
        return sslConnection ? 1 : 0;
    }

    public void assignPersistenceId(long id) {
        persistenceId = id;
    }

    public long getPersistenceId() {
        return persistenceId;
    }

    public void setPersistenceId(long persistenceId) {
        this.persistenceId = persistenceId;
    }

    /*
    * 连接到服务
    * */
    public void connectServer(final String brokerURL, final String clientId) throws MqttException {
        mqttClient = new MqttClient(brokerURL, clientId, new MemoryPersistence());
        mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setCleanSession(true);
        mqttConnectOptions.setConnectionTimeout(60);
        mqttConnectOptions.setKeepAliveInterval(60);
        mqttClient.connect(mqttConnectOptions);

    }

    public void disConnectServer() throws MqttException {
        if (mqttClient.isConnected())
            mqttClient.disconnect();

    }

    public void publishMessage(String topic, String content, int qos) throws Exception {
        MqttMessage newMessage = new MqttMessage(content.getBytes());
        newMessage.setQos(qos);
        if (mqttClient != null) {
            mqttClient.publish(topic, newMessage);
            mPubTopic = topic;
        } else {
            throw new MqttPersistenceException(200);
        }

    }

    public void subMessage(String topic) throws Exception {
        if (mqttClient != null) {
            mqttClient.subscribe(topic);
            mSubTopic = topic;
        } else {
            throw new MqttException(100);
        }
    }

    /*
    * 链接状态
    * */
    enum ConnectionStatus {
        /*
        * 客户端正在链接
        * */
        CONNECTING,
        /*
        * 客户端已经链接
        * */
        CONNECTED,
        /*
        * 客户端正在断开
        * */
        DISCONNECTING,
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
        /*
        * 新链接
        * */
        NEWCONNECT
    }
}
