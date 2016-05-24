package com.waynehfut.database;

/**
 * Created by Wayne on 2016/5/24.
 * Site:www.waynehfut.com
 * Mail:waynehfut@gmail.com
 */
public class ServerDbSchema {
    public static final class ServerTable{
        public static final String NAME = "Servers";
        public static final class Cols{
            public static final String UUID="uuid";
            public static final String SERVER = "server";
            public static final String PORT = "port";
        }
    }
}
