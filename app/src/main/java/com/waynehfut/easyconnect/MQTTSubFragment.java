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
import android.widget.RadioGroup;

/**
 * Created by Wayne on 2016/5/8.
 * Site:www.waynehfut.com
 * Mail:waynehfut@gmail.com
 */
public class MQTTSubFragment extends Fragment {
    private EditText mTopicId;
    private RadioGroup mQOSRadioGroup;
    private int mQOS = 0;
    private Connection connection=Connection.getConnection();
    public static MQTTSubFragment newInstance() {
        MQTTSubFragment mqttSubFragment = new MQTTSubFragment();
        return mqttSubFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_sub, container, false);
        mTopicId = (EditText) view.findViewById(R.id.sub_topic);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.verifyYesButton);
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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    connection.subMessage(mTopicId.getText().toString(),  mQOS);
                    Snackbar.make(view, "Sub Success", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                } catch (Exception e) {
                    Snackbar.make(view, "Sub Fail", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
            }
        });
        return view;
    }
}
