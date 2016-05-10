package com.waynehfut.easyconnect;

/**
 * Created by Wayne on 2016/5/8.
 * Site:www.waynehfut.com
 * Mail:waynehfut@gmail.com
 */
public class ManageMQTTServer {
    public boolean onConnect() {
        return false;
    }

    public boolean onDisConnect() {
        return false;
    }

    public boolean onPublishMsg() {
        return false;
    }

    public boolean onSubTopic() {
        return false;
    }
}
