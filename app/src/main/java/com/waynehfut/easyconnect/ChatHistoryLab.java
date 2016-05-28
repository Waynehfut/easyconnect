package com.waynehfut.easyconnect;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.waynehfut.database.ServerBaseHelper;
import com.waynehfut.database.ServerCursorWrapper;
import com.waynehfut.database.ServerDbSchema;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Wayne on 2016/5/14.
 * Site:www.waynehfut.com
 * Mail:waynehfut@gmail.com
 */
public class ChatHistoryLab {
    private static ChatHistoryLab sChatHistoryLab;
    private Context mContext;
    private SQLiteDatabase mDatabse;

    private ChatHistoryLab(Context appContext) {
        mContext = appContext.getApplicationContext();
        mDatabse = new ServerBaseHelper(mContext).getWritableDatabase();
    }

    public static ChatHistoryLab getsChatHistoryLab(Context context) {
        if (sChatHistoryLab == null) {
            sChatHistoryLab = new ChatHistoryLab(context.getApplicationContext());
        }
        return sChatHistoryLab;
    }

    private static ContentValues getContentValus(ChatHistory chatHistory) {
        ContentValues values = new ContentValues();
        values.put(ServerDbSchema.ChatTable.Cols.CHATCLIENTID, chatHistory.getChatClientId());
        values.put(ServerDbSchema.ChatTable.Cols.CHATCONTEXT, chatHistory.getChatContext());
        values.put(ServerDbSchema.ChatTable.Cols.CHATTYPE, chatHistory.getChatType());
        values.put(ServerDbSchema.ChatTable.Cols.DATE, chatHistory.getChatDate().toString());
        return values;
    }

    public void addChatHistory(ChatHistory chatHistory) {
        ContentValues contentValues = getContentValus(chatHistory);
        mDatabse.insert(ServerDbSchema.ChatTable.NAME, null, contentValues);

    }

    public List<ChatHistory> getChatHistories() {
        List<ChatHistory> chatHistories = new ArrayList<>();
        ServerCursorWrapper cursorWrapper = queryChatHistories(null, null);
        try {
            cursorWrapper.moveToLast();
            while (!cursorWrapper.isFirst()) {
                chatHistories.add(cursorWrapper.getChatHistory());
                cursorWrapper.moveToPrevious();
            }
        } finally {
            cursorWrapper.close();
        }
        return chatHistories;


    }


//    public ServerHistory getChatHistory(Date date) {
//        ServerCursorWrapper cursorWrapper = queryChatHistories(ServerDbSchema.ChatTable.Cols.DATE + " =?", new String[]{date.toString()});
//        try {
//            if (cursorWrapper.getCount() == 0) {
//                return null;
//            }
//            cursorWrapper.moveToFirst();
//            return cursorWrapper.getServer();
//        } finally {
//            cursorWrapper.close();
//        }
//
//    }

    private ServerCursorWrapper queryChatHistories(String whereClause, String[] whereArgs) {
        String querySQL = "SELECT  * FROM " + ServerDbSchema.ChatTable.NAME + " ASC";
        Cursor cursor = mDatabse.rawQuery(querySQL, null);
        return new ServerCursorWrapper(cursor);
    }
}
