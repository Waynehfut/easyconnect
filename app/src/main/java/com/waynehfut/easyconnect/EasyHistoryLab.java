package com.waynehfut.easyconnect;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wayne on 2016/5/12.
 * Site:www.waynehfut.com
 * Mail:waynehfut@gmail.com
 */
public class EasyHistoryLab {
    private static EasyHistoryLab sEayHistoryLab;
    private ArrayList<EasyConnectHistory> mEasyConnectHistories;
    private Context mContext;

    private EasyHistoryLab(Context appContext) {
        mContext = appContext;
        mEasyConnectHistories = new ArrayList<EasyConnectHistory>();
    }

    public static EasyHistoryLab get(Context context) {
        if (sEayHistoryLab == null) {
            sEayHistoryLab = new EasyHistoryLab(context.getApplicationContext());
        }
        return sEayHistoryLab;
    }

    public void addHistory(EasyConnectHistory easyConnectHistory) {
        mEasyConnectHistories.add(easyConnectHistory);
    }
    public List<EasyConnectHistory> getEasyConnectHistories(){
        List<EasyConnectHistory> easyConnectHistories = new ArrayList<>();
        return easyConnectHistories;
    }
}
