package com.waynehfut.easyconnect;

import java.util.UUID;

/**
 * Created by Wayne on 2016/5/24.
 * Site:www.waynehfut.com
 * Mail:waynehfut@gmail.com
 */
public class ServerHistory {
    private UUID mUUID;
    private String mServer;
    private String mPort;

    public ServerHistory(UUID id) {
        mUUID = id;
    }

    public String getmServer() {
        return mServer;
    }

    public void setmServer(String mServer) {
        this.mServer = mServer;
    }

    public UUID getmUUID() {
        return mUUID;
    }

    public String getmPort() {
        return mPort;
    }

    public void setmPort(String mPort) {
        this.mPort = mPort;
    }
}
