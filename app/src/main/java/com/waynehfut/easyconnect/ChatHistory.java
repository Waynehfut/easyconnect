package com.waynehfut.easyconnect;

import java.util.Date;

/**
 * Created by Wayne on 2016/5/14.
 * Site:www.waynehfut.com
 * Mail:waynehfut@gmail.com
 */

/*
* 聊天记录
* @param chat_clientId clientId:
* @param chat_context context:chat info
* @param chat_date date:chat date
* @param chat_uid chatUID:random Id
* @param chat_type chatType:income or outcome
* */
public class ChatHistory {
    private String chatClientId;
    private String chatContext;
    private Date chatDate;
    private String chatType;


    public String getChatType() {
        return chatType;
    }

    public void setChatType(String chatType) {
        this.chatType = chatType;
    }

    /*
    * TODO ClientId is to be verified to store here?
    * */

    public String getChatClientId() {
        return chatClientId;
    }

    public void setChatClientId(String chatClientId) {
        this.chatClientId = chatClientId;
    }

    public String getChatContext() {
        return chatContext;
    }

    public void setChatContext(String chatContext) {
        this.chatContext = chatContext;
    }

    public Date getChatDate() {
        return chatDate;
    }

    public void setChatDate(Date chatDate) {
        this.chatDate = chatDate;
    }


}
