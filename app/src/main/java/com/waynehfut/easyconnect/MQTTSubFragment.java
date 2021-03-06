package com.waynehfut.easyconnect;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.MqttClient;

import java.util.List;

/**
 * Created by Wayne on 2016/5/8.
 * Site:www.waynehfut.com
 * Mail:waynehfut@gmail.com
 */
public class MQTTSubFragment extends Fragment {
    private static final String TAG = "MQTTSubFragment";
    private static MQTTSubFragment smqttSubFragment;
    MqttClient mqttClient;
    private EditText mTopicId;
    private Connection connection = Connection.getConnection();

    private RecyclerView mChatRecycleView;
    private ChatHistoryLab chatHistoryLab;
    private ChatHistoryAdapter chatHistoryAdapter;
    private ChatHistory chatHistory;
    private CardView floatSendCard;
    private CardView chatHistoryHolderCard;
    /*
    * on sub page send msg float;
    * */
    private Button sendBtn;
    private EditText textOnSub;
    private Button extendMsgBtn;


    public static MQTTSubFragment newInstance() {
        if (smqttSubFragment == null)
            smqttSubFragment = new MQTTSubFragment();
        return smqttSubFragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.new_sub, container, false);
        /*
        * Animation
        * */
        final ScaleAnimation scaleAnim4 = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f);
        scaleAnim4.setDuration(500);
        scaleAnim4.setStartOffset(100);
        scaleAnim4.setFillAfter(true);

        mTopicId = (EditText) view.findViewById(R.id.sub_topic);
        chatHistoryLab = ChatHistoryLab.getsChatHistoryLab(getContext());
        mChatRecycleView = (RecyclerView) view.findViewById(R.id.recycle_view);
        mChatRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        chatHistoryHolderCard = (CardView) view.findViewById(R.id.chat_history_holder);

        floatSendCard = (CardView) view.findViewById(R.id.float_send_message);
        floatSendCard.setVisibility(View.INVISIBLE);

        sendBtn = (Button) view.findViewById(R.id.send_msg);
        textOnSub = (EditText) view.findViewById(R.id.onfloat_send_msg);
        extendMsgBtn = (Button) view.findViewById(R.id.send_extend);
        extendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2016/5/29 extend msg
                Toast.makeText(getContext(), "Extend click", Toast.LENGTH_SHORT).show();
            }
        });
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    connection.publishMessage(mTopicId.getText().toString(), textOnSub.getText().toString(), 2, false);
                    connection.setmTopic(mTopicId.getText().toString());
                    connection.setPubContext(textOnSub.getText().toString());
                    Toast.makeText(getContext(), getString(R.string.toast_pub_success), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Snackbar.make(view, getString(R.string.toast_sub_failed, mTopicId.getText().toString()) + e.toString(), Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
            }
        });

        final FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.verifyYesButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    connection.subMessage(mTopicId.getText().toString());
                    connection.setmTopic(mTopicId.getText().toString());
                    Toast.makeText(getContext(), getString(R.string.toast_sub_success, mTopicId.getText().toString()), Toast.LENGTH_SHORT).show();
                    floatSendCard.setVisibility(View.VISIBLE);
                    floatSendCard.setAnimation(scaleAnim4);
                    fab.hide();
                } catch (Exception e) {
                    Snackbar.make(view, getString(R.string.toast_sub_failed, mTopicId.getText().toString()) + e.toString(), Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
            }
        });
        mTopicId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                floatSendCard.setVisibility(View.INVISIBLE);
                fab.show();
            }
        });
        return view;
    }

    public void updateChatUI() {
        ChatHistoryLab chatHistoryLab = ChatHistoryLab.getsChatHistoryLab(getActivity());
        List<ChatHistory> chatHistories = chatHistoryLab.getChatHistories();
        if (chatHistoryAdapter == null) {
            chatHistoryAdapter = new ChatHistoryAdapter(chatHistories);
            mChatRecycleView.setAdapter(chatHistoryAdapter);
        } else {
            chatHistoryAdapter.setmChatHistories(chatHistories);
            chatHistoryAdapter.notifyDataSetChanged();
        }

    }

    private class ChatHistoryAdapter extends RecyclerView.Adapter<ChatHistoryHolder> {
        private List<ChatHistory> mChatHistories;

        public ChatHistoryAdapter(List<ChatHistory> chatHistories) {
            mChatHistories = chatHistories;
        }

        public void setmChatHistories(List<ChatHistory> mChatHistories) {
            this.mChatHistories = mChatHistories;
        }

        @Override
        public void onBindViewHolder(ChatHistoryHolder holder, int position) {
            ChatHistory chatHistory = mChatHistories.get(position);
            holder.bindChatHistory(chatHistory);
        }

        @Override
        public ChatHistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.chat_item, parent, false);
            return new ChatHistoryHolder(view);
        }

        @Override
        public int getItemCount() {
            return mChatHistories.size();
        }
    }

    private class ChatHistoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mChatClientId;
        private TextView mChatContext;
        private TextView mChatDate;
        private LinearLayout mChatItemBox;
        private ChatHistory mChatHistory;


        public ChatHistoryHolder(View chatHistoryView) {
            super(chatHistoryView);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return true;
                }
            });
            mChatClientId = (TextView) chatHistoryView.findViewById(R.id.chat_clientId);
            mChatContext = (TextView) chatHistoryView.findViewById(R.id.chat_context);
            mChatDate = (TextView) chatHistoryView.findViewById(R.id.chat_date);
            mChatItemBox = (LinearLayout) chatHistoryView.findViewById(R.id.chat_item_box);
        }

        @Override
        public void onClick(View v) {

        }

        public void bindChatHistory(ChatHistory chatHistory) {
            if (chatHistory != null) {
                mChatHistory = chatHistory;
                mChatClientId.setText(mChatHistory.getChatClientId());
                mChatContext.setText(mChatHistory.getChatContext());
                String datess = mChatHistory.getChatDate().toString();
                String formatDate = DateFormat.format("yyyy MMMM dd HH:MM:ss EEEE, ", mChatHistory.getChatDate()).toString();
                mChatDate.setText(formatDate);
                mChatItemBox.setBackground(getResources().getDrawable(setChatItemBoxByType(mChatHistory.getChatType())));
            }
        }

        public int setChatItemBoxByType(String chatType) {
            switch (chatType) {
                case "Income":
                    return R.drawable.msg_bubble_incoming;
                case "Outcome":
                    return R.drawable.msg_bubble_outgoing;
                default:
                    return R.drawable.msg_bubble_incoming;
            }
        }
    }
}
