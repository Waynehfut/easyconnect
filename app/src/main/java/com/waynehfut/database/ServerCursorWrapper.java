package com.waynehfut.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.waynehfut.easyconnect.ChatHistory;
import com.waynehfut.easyconnect.Connection;
import com.waynehfut.easyconnect.EasyHistory;
import com.waynehfut.easyconnect.ServerHistory;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Wayne on 2016/5/22.
 * Site:www.waynehfut.com
 * Mail:waynehfut@gmail.com
 */
public class ServerCursorWrapper extends CursorWrapper {
    public ServerCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public ServerHistory getServer() {
        String uuidString = getString(getColumnIndex(ServerDbSchema.ServerTable.Cols.UUID));
        String server = getString(getColumnIndex(ServerDbSchema.ServerTable.Cols.SERVER));
        String port = getString(getColumnIndex(ServerDbSchema.ServerTable.Cols.PORT));

        ServerHistory serverHistory = new ServerHistory(UUID.fromString(uuidString));
        serverHistory.setmServer(server);
        serverHistory.setmPort(port);
        return serverHistory;
    }

    public ChatHistory getChatHistory() {
        String chatClientId = getString(getColumnIndex(ServerDbSchema.ChatTable.Cols.CHATCLIENTID));
        String chatContext = getString(getColumnIndex(ServerDbSchema.ChatTable.Cols.CHATCONTEXT));
        String chatDate = getString(getColumnIndex(ServerDbSchema.ChatTable.Cols.DATE));
        String chatType = getString(getColumnIndex(ServerDbSchema.ChatTable.Cols.CHATTYPE));
        ChatHistory chatHistory = new ChatHistory();
        chatHistory.setChatClientId(chatClientId);
        chatHistory.setChatContext(chatContext);
        chatHistory.setChatDate(new Date(chatDate));
        chatHistory.setChatType(chatType);
        return chatHistory;
    }

    public EasyHistory getEasyHistory() {
        String historyTitle = getString(getColumnIndex(ServerDbSchema.ConnectTable.Cols.HISTORYTITLE));
        String historySubTitle = getString(getColumnIndex(ServerDbSchema.ConnectTable.Cols.HISTORYSUBTITLE));
        String hisType = getString(getColumnIndex(ServerDbSchema.ConnectTable.Cols.HISTYPE));
        String historyUID = getString(getColumnIndex(ServerDbSchema.ConnectTable.Cols.HSITORYUUID));

        EasyHistory easyHistory = new EasyHistory(UUID.randomUUID());
        easyHistory.setHistoryTitle(historyTitle);
        easyHistory.setHistorySubTitle(historySubTitle);
        Connection.ConnectionStatus status = Connection.ConnectionStatus.DISCONNECTED;
        switch (hisType) {
            case "CONNECTED":
                status = Connection.ConnectionStatus.CONNECTED;
                break;
            case "DISCONNECTED":
                status = Connection.ConnectionStatus.DISCONNECTED;
                break;
            case "NEWCONNECT":
                status = Connection.ConnectionStatus.NEWCONNECT;
                break;
            default:
                status = Connection.ConnectionStatus.DISCONNECTED;
        }
        easyHistory.setHisType(status);
        return easyHistory;
    }
}
