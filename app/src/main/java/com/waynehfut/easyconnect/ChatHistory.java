package com.waynehfut.easyconnect;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Wayne on 2016/5/14.
 * Site:www.waynehfut.com
 * Mail:waynehfut@gmail.com
 */

/*
* 聊天记录
* @param chat_topic topic
* @param chat_context context
* @param chat_date date
* @param chat_uid chatUID
* */
public class ChatHistory {
    private String chatTopic;
    private String chatContext;
    private Date chatDate;
    private UUID chatUUID;

    public String getChatTopic() {
        return chatTopic;
    }

    public void setChatTopic(String chatTopic) {
        this.chatTopic = chatTopic;
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

    public UUID getChatUUID() {
        return chatUUID;
    }

    public void setChatUUID(UUID chatUUID) {
        this.chatUUID = chatUUID;
    }

}
