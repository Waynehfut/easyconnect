package com.waynehfut.easyconnect;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Wayne on 2016/5/12.
 * Site:www.waynehfut.com
 * Mail:waynehfut@gmail.com
 */
public class EasyHistoryLab {
    private static EasyHistoryLab sEasyHistoryLab;
    private ArrayList<EasyConnectHistory> mEasyConnectHistories;
    private Context mContext;

    private EasyHistoryLab(Context appContext) {
        mContext = appContext;
        mEasyConnectHistories = new ArrayList<EasyConnectHistory>();
    }

    public static EasyHistoryLab getEasyHistoryLab(Context context) {
        if (sEasyHistoryLab == null) {
            sEasyHistoryLab = new EasyHistoryLab(context);
        }
        return sEasyHistoryLab;
    }

    public void addHistory(EasyConnectHistory easyConnectHistory) {
        mEasyConnectHistories.add(easyConnectHistory);
    }

    public ArrayList<EasyConnectHistory> getEasyConnectHistories() {

        return mEasyConnectHistories;
    }

    public EasyConnectHistory getEasyHistory(UUID historyUID) {

        for (EasyConnectHistory easyConnectHistory : mEasyConnectHistories) {
            if (easyConnectHistory.getHistoryUID().equals(historyUID)) {
                return easyConnectHistory;
            }
        }
        return null;
    }
}
