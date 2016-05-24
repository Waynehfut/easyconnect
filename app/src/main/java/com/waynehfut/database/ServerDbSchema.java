package com.waynehfut.database;

/**
 * Created by Wayne on 2016/5/22.
 * Site:www.waynehfut.com
 * Mail:waynehfut@gmail.com
 */
public class ServerDbSchema {
    public static final class ServerTable {
        public static final String NAME = "Servers";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String SERVER = "server";
            public static final String PORT = "port";
        }
    }

    public static final class ChatTable {
        public static final String NAME = "ChatHistories";

        public static final class Cols {
            public static final String CHATCLIENTID = "chatClientId";
            public static final String CHATCONTEXT = "chatcontext";
            public static final String DATE = "date";
            public static final String CHATTYPE = "chattype";
        }
    }

    public static final class ConnectTable {
        public static final String NAME = "ConnectHistories";

        public static final class Cols {
            public static final String HISTORYTITLE = "historyTitle";
            public static final String HISTORYSUBTITLE = "historySubTitle";
            public static final String HISTYPE = "hisType";
            public static final String HSITORYUUID = "historyUID";
        }
    }
}
