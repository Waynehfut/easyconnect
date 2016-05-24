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
 * Created by Wayne on 2016/5/22.
 * Site:www.waynehfut.com
 * Mail:waynehfut@gmail.com
 */
public class ServerHistoryLab {
    private static ServerHistoryLab serverHistoryLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private ServerHistoryLab(Context appContext) {
        mContext = appContext.getApplicationContext();
        mDatabase = new ServerBaseHelper(mContext).getWritableDatabase();
    }

    public static ServerHistoryLab get(Context context) {
        if (serverHistoryLab == null) {
            serverHistoryLab = new ServerHistoryLab(context.getApplicationContext());
        }
        return serverHistoryLab;
    }

    private static ContentValues getContentValus(ServerHistory serverHistory) {
        ContentValues values = new ContentValues();
        values.put(ServerDbSchema.ServerTable.Cols.UUID, serverHistory.getmUUID().toString());
        values.put(ServerDbSchema.ServerTable.Cols.SERVER, serverHistory.getmServer());
        values.put(ServerDbSchema.ServerTable.Cols.PORT, serverHistory.getmPort());
        return values;
    }

    public void addServer(ServerHistory serverHistory) {
        ContentValues contentValues = getContentValus(serverHistory);
        mDatabase.insert(ServerDbSchema.ServerTable.NAME,null,contentValues);

    }

    public void delServer(ServerHistory serverHistory) {
        mDatabase.delete(ServerDbSchema.ServerTable.NAME, ServerDbSchema.ServerTable.Cols.UUID + " =?", new String[]{serverHistory.getmUUID().toString()});

    }

    public List<ServerHistory> getServerHistories() {
        List<ServerHistory> serverHistories = new ArrayList<>();
        ServerCursorWrapper cursorWrapper = queryServerHistories(null, null);
        try {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()) {
                serverHistories.add(cursorWrapper.getServer());
                cursorWrapper.moveToNext();
            }
        } finally {
            cursorWrapper.close();
        }
        return serverHistories;
    }

    public ServerHistory getServerHistory(UUID id) {
        ServerCursorWrapper cursorWrapper = queryServerHistories(ServerDbSchema.ServerTable.Cols.UUID + " =?", new String[]{id.toString()});
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

    private ServerCursorWrapper queryServerHistories(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                ServerDbSchema.ServerTable.NAME,
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
