package com.waynehfut.easyconnect;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Wayne on 2016/5/8.
 * Site:www.waynehfut.com
 * Mail:waynehfut@gmail.com
 */
public class EasyConnectFragment extends Fragment {
    private static final String TAG = "EasyConnectFragment";
    private static EasyConnectFragment sEasyConnectFragment;
    private RecyclerView mHistoryRecycleView;
    private HistoryAdapter historyAdapter;
    private EasyHistoryLab easyHistoryLab;

    public EasyConnectFragment() {

    }

    public static EasyConnectFragment newInstance() {
        if (sEasyConnectFragment == null) {
            sEasyConnectFragment = new EasyConnectFragment();
        }
        return sEasyConnectFragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.app_bar_easy_connect, container, false);
        easyHistoryLab = EasyHistoryLab.getEasyHistoryLab(getContext());
        mHistoryRecycleView = (RecyclerView) view.findViewById(R.id.index_list_iew);
        mHistoryRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    public void updateUI() {
        EasyHistoryLab easyHistoryLab = EasyHistoryLab.getEasyHistoryLab(getActivity());
        List<EasyConnectHistory> easyConnectHistories = easyHistoryLab.getEasyConnectHistories();
        if (historyAdapter == null) {
            historyAdapter = new HistoryAdapter(easyConnectHistories);
            mHistoryRecycleView.setAdapter(historyAdapter);
        } else {
            historyAdapter.setmEasyConnectHistories(easyConnectHistories);
            historyAdapter.notifyDataSetChanged();
        }
    }

    /*
    * 列表容器的数据单个条目信息
    * */
    private class HistoryAdapter extends RecyclerView.Adapter<HistoryHolder> {
        private List<EasyConnectHistory> mEasyConnectHistories;

        public HistoryAdapter(List<EasyConnectHistory> easyConnectHistories) {
            mEasyConnectHistories = easyConnectHistories;
        }

        public void setmEasyConnectHistories(List<EasyConnectHistory> mEasyConnectHistories) {
            this.mEasyConnectHistories = mEasyConnectHistories;
        }

        @Override
        public void onBindViewHolder(HistoryHolder holder, int position) {
            EasyConnectHistory easyConnectHistory = mEasyConnectHistories.get(position);
            /*
            * 此处绑定数据源
            * */
            holder.bindHistory(easyConnectHistory);
        }

        /*
        * 此处设置条目单独的数据信息
        * */
        @Override
        public HistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item, parent, false);
            return new HistoryHolder(view);
        }

        @Override
        public int getItemCount() {
            return mEasyConnectHistories.size();
        }
    }

    private class HistoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mHistoryTitle;
        private TextView mHistorySubtitle;
        private ImageView mHistoryStatus;
        private EasyConnectHistory mEasyConnectHistory;

        public HistoryHolder(View historyView) {
            super(historyView);
            /*
            * 长按操作
            * */
            itemView.setOnLongClickListener(new View.OnLongClickListener() { //长按监听
                @Override
                public boolean onLongClick(View v) {

                    return true;
                }
            });
            mHistoryTitle = (TextView) historyView.findViewById(R.id.list_History_Title);
            mHistorySubtitle = (TextView) historyView.findViewById(R.id.list_History_Subtitle);
            mHistoryStatus = (ImageView) historyView.findViewById(R.id.list_History_Type);

        }

        /*
        * 点击操作
        * */
        @Override
        public void onClick(View v) {

        }

        /*
        * 绑定数据信息
        * */
        public void bindHistory(EasyConnectHistory easyConnectHistory) {
            if (easyConnectHistory != null) {
                mEasyConnectHistory = easyConnectHistory;
                mHistoryTitle.setText(mEasyConnectHistory.getHistoryTitle());
                mHistorySubtitle.setText(mEasyConnectHistory.getHistorySubTitle());
                mHistoryStatus.setImageResource(findImageByType(mEasyConnectHistory.getHisType()));
            }
        }

        /*
        * 根据状态设置返回的图片资源
        * */
        public int findImageByType(Connection.ConnectionStatus history) {
            switch (history) {
                case CONNECTED:
                    return R.drawable.ic_connect;
                case DISCONNECTED:
                    return R.drawable.ic_disconnect;
                case NEWCONNECT:
                    return R.drawable.ic_charge;
                default:
                    return R.drawable.ic_notifications_black_24dp;
            }
        }
    }
}
