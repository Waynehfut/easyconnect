package com.waynehfut.easyconnect;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Wayne on 2016/5/14.
 * Site:www.waynehfut.com
 * Mail:waynehfut@gmail.com
 */
public class ChatHistoryLab {
    private static ChatHistoryLab sChatHistoryLab;
    private ArrayList<ChatHistory> mChathistories;
    private Context mContext;
    private SQLiteDatabase mDatabse;

    private ChatHistoryLab(Context appContext) {
        mContext = appContext;
        mChathistories = new ArrayList<ChatHistory>();
    }

    public static ChatHistoryLab getsChatHistoryLab(Context context) {
        if (sChatHistoryLab == null) {
            sChatHistoryLab = new ChatHistoryLab(context);
        }
        return sChatHistoryLab;
    }

    public void addChatHistory(ChatHistory chatHistory) {
        mChathistories.add(chatHistory);
    }

    public ArrayList<ChatHistory> getmChathistories() {
        return mChathistories;
    }


    public ArrayList<ChatHistory> getChatHistories() {
        return mChathistories;
    }
}
