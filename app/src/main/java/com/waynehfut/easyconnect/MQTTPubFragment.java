package com.waynehfut.easyconnect;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.util.UUID;

/**
 * Created by Wayne on 2016/5/8.
 * Site:www.waynehfut.com
 * Mail:waynehfut@gmail.com
 */
public class MQTTPubFragment extends Fragment {
    private static final String TAG = "MQTTPubFragment";
    private EditText mTopicId;
    private EditText mMessage;
    private RadioGroup mQOSRadioGroup;
    private int mQOS = 0;
    private CheckBox mIsHoldConn;
    private Connection connection=Connection.getConnection();

    public static MQTTPubFragment newInstance() {
        MQTTPubFragment mqttPubFragment = new MQTTPubFragment();
        return mqttPubFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_pub, container, false);
        mTopicId = (EditText) view.findViewById(R.id.pub_topic);
        mMessage = (EditText) view.findViewById(R.id.pub_context);
        mQOSRadioGroup = (RadioGroup) view.findViewById(R.id.qosRadio);
        mQOSRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int newCheckedId = group.getCheckedRadioButtonId();
                switch (newCheckedId) {
                    case R.id.qos0:
                        mQOS = 0;
                        break;
                    case R.id.qos1:
                        mQOS = 1;
                        break;
                    case R.id.qos2:
                        mQOS = 2;
                        break;
                }
            }
        });
        mIsHoldConn = (CheckBox) view.findViewById(R.id.isRetained);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.verifyYesButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    connection.publishMessage(mTopicId.getText().toString(), mMessage.getText().toString(), mQOS);
                    Snackbar.make(view, getString(R.string.toast_pub_success,mMessage.getText().toString(),mTopicId.getText().toString()), Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                } catch (Exception e) {
                    Snackbar.make(view, getString(R.string.toast_pub_failed,mMessage.getText().toString(),mTopicId.getText().toString())+connection.getServerId(), Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
            }
        });

        return view;
    }
}
