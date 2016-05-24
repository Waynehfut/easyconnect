package com.waynehfut.easyconnect;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.waynehfut.database.ServerBaseHelper;
import com.waynehfut.database.ServerCursorWrapper;
import com.waynehfut.database.ServerDbSchema;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Wayne on 2016/5/12.
 * Site:www.waynehfut.com
 * Mail:waynehfut@gmail.com
 */
public class EasyHistoryLab {
    private static EasyHistoryLab sEasyHistoryLab;
//    private ArrayList<EasyHistory> mEasyConnectHistories;// TODO: 2016/5/24 to delete
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private EasyHistoryLab(Context appContext) {
        mContext = appContext.getApplicationContext();
//        mEasyConnectHistories = new ArrayList<EasyHistory>();
        mDatabase  = new ServerBaseHelper(mContext).getWritableDatabase();
    }

    public static EasyHistoryLab getEasyHistoryLab(Context context) {
        if (sEasyHistoryLab == null) {
            sEasyHistoryLab = new EasyHistoryLab(context);
        }
        return sEasyHistoryLab;
    }
    private static ContentValues getContentValus(EasyHistory easyHistory){
        ContentValues values = new ContentValues();
        values.put(ServerDbSchema.ConnectTable.Cols.HISTORYTITLE,easyHistory.getHistoryTitle());
        values.put(ServerDbSchema.ConnectTable.Cols.HISTORYSUBTITLE,easyHistory.getHistorySubTitle());
        values.put(ServerDbSchema.ConnectTable.Cols.HISTYPE,easyHistory.getHisType().toString());
        values.put(ServerDbSchema.ConnectTable.Cols.HSITORYUUID,easyHistory.getHistoryUID().toString());
        return values;
    }

    public void addHistory(EasyHistory easyConnectHistory) {
        ContentValues contentValues = getContentValus(easyConnectHistory);
        mDatabase.insert(ServerDbSchema.ConnectTable.NAME,null,contentValues);
        // TODO: 2016/5/24 to delete
//        mEasyConnectHistories.add(easyConnectHistory);
    }

    public List<EasyHistory> getEasyConnectHistories() {
        List<EasyHistory> easyHistories = new ArrayList<>();
        ServerCursorWrapper cursorWrapper = queryConnHistories(null,null);
        try {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()){
                easyHistories.add(cursorWrapper.getEasyHistory());
                cursorWrapper.moveToNext();
            }
        }finally {
            cursorWrapper.close();
        }

        return easyHistories;
        // TODO: 2016/5/24 to del
//        return mEasyConnectHistories;
    }

    public EasyHistory getEasyHistory(UUID historyUID) {

        ServerCursorWrapper cursorWrapper = queryConnHistories(ServerDbSchema.ConnectTable.Cols.HSITORYUUID + " =?", new String[]{historyUID.toString()});
        try {
            if (cursorWrapper.getCount()==0){
                return null;
            }
            cursorWrapper.moveToFirst();
            return cursorWrapper.getEasyHistory();
        }finally {
            cursorWrapper.close();
        }
    }

    private ServerCursorWrapper queryConnHistories(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                ServerDbSchema.ConnectTable.NAME,
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
