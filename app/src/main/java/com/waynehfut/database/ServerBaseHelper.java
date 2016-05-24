package com.waynehfut.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Wayne on 2016/5/24.
 * Site:www.waynehfut.com
 * Mail:waynehfut@gmail.com
 */
public class ServerBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "mqttServer.db";

    public ServerBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(

                "create table " + ServerDbSchema.ServerTable.NAME + "(" +
                        "_id integer primary key autoincrement,"
                        + ServerDbSchema.ServerTable.Cols.UUID + ","
                        + ServerDbSchema.ServerTable.Cols.SERVER + ","
                        + ServerDbSchema.ServerTable.Cols.PORT +
                        ")"

        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
