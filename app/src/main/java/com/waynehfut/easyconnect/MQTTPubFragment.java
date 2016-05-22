package com.waynehfut.easyconnect;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.WriterException;
import com.waynehfut.Lz77.MsgLzHelper;
import com.waynehfut.qrcode.QRCodeGenerator;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * Created by Wayne on 2016/5/8.
 * Site:www.waynehfut.com
 * Mail:waynehfut@gmail.com
 */
public class MQTTPubFragment extends Fragment {
    private static final String TAG = "MQTTPubFragment";
    private static MQTTPubFragment smqttPubFragment;
    private static MQTTSubFragment smqttSubFragment;
    MqttClient mqttClient;
    Gson gson;
    private EditText mTopicId;
    private EditText mMessage;
    private RadioGroup mQOSRadioGroup;
    private ChatHistoryLab chatHistoryLab;
    private TextView qrCodeTextView;
    private QRCodeGenerator qrCodeGenerator = QRCodeGenerator.newInstance();
    private int mQOS = 0;
    private Connection connection = Connection.getConnection();
    private MsgLzHelper msgLzHelper = new MsgLzHelper();
    private String compressTopic;
    private String compressContext;

    public static MQTTPubFragment newInstance() {
        if (smqttPubFragment == null)
            smqttPubFragment = new MQTTPubFragment();
        return smqttPubFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        chatHistoryLab = ChatHistoryLab.getsChatHistoryLab(getContext());
        final View view = inflater.inflate(R.layout.new_pub, container, false);
        mTopicId = (EditText) view.findViewById(R.id.pub_topic);
        mMessage = (EditText) view.findViewById(R.id.pub_context);
        mQOSRadioGroup = (RadioGroup) view.findViewById(R.id.qosRadio);
        qrCodeTextView = (TextView) view.findViewById(R.id.qr_code_view);
        smqttSubFragment = MQTTSubFragment.newInstance();
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
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.verifyYesButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    compressTopic = msgLzHelper.compressLZ77(mTopicId.getText().toString());
                    compressContext = msgLzHelper.compressLZ77(mMessage.getText().toString());
                    connection.publishMessage(compressTopic,compressContext, mQOS);
                    connection.setmTopic(mTopicId.getText().toString());
                    connection.setPubContext(mMessage.getText().toString());
                    Toast.makeText(getContext(), getString(R.string.toast_pub_success), Toast.LENGTH_SHORT).show();
                    mqttClient = connection.getMqttClient();

                    Gson gson = new Gson();
                    QRcodeInfo qRcodeInfo = new QRcodeInfo();
                    qRcodeInfo.setUrl(mqttClient.getServerURI());
                    qRcodeInfo.setTopic(connection.getmTopic());
                    String genereJson = gson.toJson(qRcodeInfo);

                    Drawable drawable = new BitmapDrawable(getResources(), qrCodeGenerator.GenerateQRCode(genereJson));
                    qrCodeTextView.setBackground(drawable);
                } catch (MqttException e) {
                    Snackbar.make(view, getString(R.string.toast_pub_failed, mMessage.getText().toString(), mTopicId.getText().toString()) + connection.getServerId(), Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                } catch (WriterException e) {
                    Snackbar.make(view, getString(R.string.genera_code_failed), Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
            }
        });
        return view;
    }

}
