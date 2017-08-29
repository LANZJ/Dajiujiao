package com.zjyeshi.dajiujiao.buyer.activity.frame.fragment;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jopool.crow.CWChat;
import com.jopool.crow.imkit.receiver.CWTotalUnreadNumReceiver;
import com.jopool.crow.imkit.ui.uientity.CWUiConversation;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigdog.lib.widgets.adapter.DGNoDataView;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.adapter.message.MessageListAdapter;
import com.zjyeshi.dajiujiao.buyer.receiver.MessageSortReceiver;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息
 * Created by xuan on 15/9/16.
 */
public class Fragment2 extends BaseFragment {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;

    public DGTitleLayout getTitleLayout() {
        return titleLayout;
    }

    @InjectView(R.id.listView)
    private ListView listView;

    @InjectView(R.id.noDataView)
    private DGNoDataView noDataView;

    private MessageListAdapter messageListAdapter;
    private List<CWUiConversation> dataList = new ArrayList<CWUiConversation>();

    /**
     * 用来接收单条记录未读消息数量
     */
    private CWTotalUnreadNumReceiver totalUnreadNumReceiver;
    private MessageSortReceiver messageSortReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册单条记录未读消息数量广播接收器
        totalUnreadNumReceiver = new CWTotalUnreadNumReceiver() {
            @Override
            public void totalUnreadNum(int totalUnreadNum) {
                refreshData();
            }
        };
        totalUnreadNumReceiver.register(getActivity());

        messageSortReceiver = new MessageSortReceiver() {
            @Override
            public void reSortMessage() {
                refreshData();
            }
        };
        messageSortReceiver.register();

        NotificationManager nm=(NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancelAll();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        totalUnreadNumReceiver.unregister(getActivity());
        messageSortReceiver.unregister();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
        //连接聊天服务器方法里会判重
        ExtraUtil.connectChat(getActivity());
    }

    @Override
    protected int initFragmentView() {
        return R.layout.fragment2;
    }

    @Override
    protected void initFragmentWidgets(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        titleLayout.configTitle("聊天");
        titleLayout.configRightText("发起群聊", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CWChat.getInstance().getGroupDelegate().startSelectWhenCreate(getActivity());
            }
        });
        messageListAdapter = new MessageListAdapter(getActivity(), dataList);
        listView.setAdapter(messageListAdapter);
    }

    //刷新数据
    public void refreshData() {
        sort();
    }

    /**
     * 全国和地区客服置顶
     *
     */
    private void sort() {
        List<CWUiConversation> list = CWChat.getInstance().getUiModel().getUiConversationList();
        dataList.clear();
        dataList.addAll(list);
        noDataView.showIfEmpty(dataList);
        messageListAdapter.notifyDataSetChanged();
    }
}
