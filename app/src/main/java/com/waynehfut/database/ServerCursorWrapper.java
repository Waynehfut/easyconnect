package com.waynehfut.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.waynehfut.easyconnect.Connection;
import com.waynehfut.easyconnect.ServerHistory;

import java.util.UUID;

/**
 * Created by Wayne on 2016/5/24.
 * Site:www.waynehfut.com
 * Mail:waynehfut@gmail.com
 */
public class ServerCursorWrapper extends CursorWrapper {
    public ServerCursorWrapper(Cursor cursor) {
        super(cursor);
    }
    public ServerHistory getServer(){
        String uuidString = getString(getColumnIndex(ServerDbSchema.ServerTable.Cols.UUID));
        String server = getString(getColumnIndex(ServerDbSchema.ServerTable.Cols.SERVER));
        String port = getString(getColumnIndex(ServerDbSchema.ServerTable.Cols.PORT));

        ServerHistory serverHistory = new ServerHistory(UUID.fromString(uuidString));
        serverHistory.setmServer(server);
        serverHistory.setmPort(port);
        return serverHistory;
    }
}
