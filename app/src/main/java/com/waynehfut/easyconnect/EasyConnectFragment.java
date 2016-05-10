package com.waynehfut.easyconnect;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.UUID;

/**
 * Created by Wayne on 2016/5/8.
 * Site:www.waynehfut.com
 * Mail:waynehfut@gmail.com
 */
public class EasyConnectFragment extends Fragment {
    private static final String TAG = "EasyConnectFragment";
    Connection connection = Connection.getConnection();
    private EditText mServerId;
    private EditText mPort;
    private EditText mClientId;

    public EasyConnectFragment() {

    }

    public static EasyConnectFragment newInstance() {
        EasyConnectFragment easyConnectFragment = new EasyConnectFragment();
        return easyConnectFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.app_bar_easy_connect, container, false);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }
}
