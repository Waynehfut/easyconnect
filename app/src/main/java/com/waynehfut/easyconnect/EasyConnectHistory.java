package com.waynehfut.easyconnect;

import java.util.UUID;

/**
 * Created by Wayne on 2016/5/10.
 * Site:www.waynehfut.com
 * Mail:waynehfut@gmail.com
 * This file will hold the info of the
 * # client connect status
 * # publish status
 * # subscribe status
 * # disconnect status.
 * # publish msg history
 * # Receive msg history
 * # -------------------
 * # 将保存链接状态，发布状态，订阅状态，断开状态，发布信息历史，订阅信息历史
 */
public class EasyConnectHistory {
    /*
    * @param historyTitle for xml layout's attr,History Title;
    * @param hisSubtitle for xml layout's attr,History Subtitle;
    * @param hisType for xml layout's attr,History Type;
    * */
    private String historyTitle;
    private String historySubTitle;
    private Connection.ConnectionStatus hisType;
    private UUID historyUID;

    public UUID getHistoryUID() {
        return historyUID;
    }

    public void setHistoryUID(UUID historyUID) {
        this.historyUID = historyUID;
    }

    public Connection.ConnectionStatus getHisType() {
        return hisType;
    }

    public void setHisType(Connection.ConnectionStatus hisType) {
        this.hisType = hisType;
    }

    public String getHistoryTitle() {
        return historyTitle;
    }

    public void setHistoryTitle(String historyTitle) {
        this.historyTitle = historyTitle;
    }

    public String getHistorySubTitle() {
        return historySubTitle;
    }

    public void setHistorySubTitle(String historySubTitle) {
        this.historySubTitle = historySubTitle;
    }


}
