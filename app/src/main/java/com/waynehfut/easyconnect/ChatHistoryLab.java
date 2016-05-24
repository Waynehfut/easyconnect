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
import java.util.UUID;

/**
 * Created by Wayne on 2016/5/14.
 * Site:www.waynehfut.com
 * Mail:waynehfut@gmail.com
 */
public class ChatHistoryLab {
    private static ChatHistoryLab sChatHistoryLab;
//    private ArrayList<ChatHistory> mChathistories;// TODO: 2016/5/24 to delete
    private Context mContext;
    private SQLiteDatabase mDatabse;

    private ChatHistoryLab(Context appContext) {
        mContext = appContext.getApplicationContext();
//        mChathistories = new ArrayList<ChatHistory>();
        mDatabse=new ServerBaseHelper(mContext).getWritableDatabase();
    }

    public static ChatHistoryLab getsChatHistoryLab(Context context) {
        if (sChatHistoryLab == null) {
            sChatHistoryLab = new ChatHistoryLab(context.getApplicationContext());
        }
        return sChatHistoryLab;
    }
    private static ContentValues getContentValus(ChatHistory chatHistory){
        ContentValues values = new ContentValues();
        values.put(ServerDbSchema.ChatTable.Cols.CHATCLIENTID,chatHistory.getChatClientId());
        values.put(ServerDbSchema.ChatTable.Cols.CHATCONTEXT,chatHistory.getChatContext());
        values.put(ServerDbSchema.ChatTable.Cols.CHATTYPE,chatHistory.getChatType());
        values.put(ServerDbSchema.ChatTable.Cols.DATE,chatHistory.getChatDate().toString());
        return values;
    }

    public void addChatHistory(ChatHistory chatHistory) {
        ContentValues contentValues= getContentValus(chatHistory);
        mDatabse.insert(ServerDbSchema.ChatTable.NAME,null,contentValues);
        // TODO: 2016/5/24 update view 
//        mChathistories.add(chatHistory);

    }

    public List<ChatHistory> getChatHistories() {
        List<ChatHistory> chatHistories = new ArrayList<>();
        ServerCursorWrapper cursorWrapper = queryChatHistories(null,null);
        try {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()){
                chatHistories.add(cursorWrapper.getChatHistory());
                cursorWrapper.moveToNext();
            }
        }finally {
            cursorWrapper.close();
        }
        return chatHistories;
        //// TODO: 2016/5/24 update view 
//        return mChathistories;

    }

    // TODO: 2016/5/24 should del
//    public ArrayList<ChatHistory> getChatHistories() {
//        return mChathistories;
//    }

    public ServerHistory getChatHistory(Date date) {
        ServerCursorWrapper cursorWrapper = queryChatHistories(ServerDbSchema.ChatTable.Cols.DATE + " =?", new String[]{date.toString()});
        try {
            if (cursorWrapper.getCount()==0){
                return null;
            }
            cursorWrapper.moveToFirst();
            return cursorWrapper.getServer();
        }finally {
            cursorWrapper.close();
        }

    }

    private ServerCursorWrapper queryChatHistories(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabse.query(
                ServerDbSchema.ChatTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new ServerCursorWrapper(cursor);
    }
}
