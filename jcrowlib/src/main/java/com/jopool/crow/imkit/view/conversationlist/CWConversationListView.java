package com.jopool.crow.imkit.view.conversationlist;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.jopool.crow.CWChat;
import com.jopool.crow.R;
import com.jopool.crow.imkit.adapter.conversationlist.CWConversationListAdapter;
import com.jopool.crow.imkit.receiver.CWConversationUnreadNumReceiver;
import com.jopool.crow.imkit.ui.uientity.CWUiConversation;
import com.jopool.crow.imkit.view.CWEmptyView;
import com.jopool.crow.imlib.entity.CWConversationMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天界面
 * Created by wuhk on 2016/3/23.
 */
public class CWConversationListView extends LinearLayout {
    private ListView listView;
    private CWConversationListAdapter conversationListAdapter;
    private CWEmptyView emptyView;
    private List<CWUiConversation> dataList = new ArrayList<CWUiConversation>();
    private CWConversationUnreadNumReceiver conversationUnreadNumReceiver;

    public CWConversationListView(Context context) {
        super(context);
        init();
    }

    public CWConversationListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.cw_conversationlist_view, this);
        listView = (ListView) findViewById(R.id.listView);
        emptyView = (CWEmptyView) findViewById(R.id.emptyView);

        conversationListAdapter = new CWConversationListAdapter(getContext(), dataList);
        listView.setAdapter(conversationListAdapter);
    }

    /**
     * 重新刷新数据
     */
    public void refreshData() {
        dataList.clear();
        List<CWUiConversation> tempList = CWChat.getInstance().getUiModel().getUiConversationList();
        dataList.addAll(tempList);
        conversationListAdapter.notifyDataSetChanged();
        emptyView.showIfEmpty(dataList);
    }

    /**
     * 初始化注册未读消息监听
     */
    public void onCreate(){
        conversationUnreadNumReceiver = new CWConversationUnreadNumReceiver(){
            @Override
            public void conversationUnreadNum(String toId, int totalUnreadNum, CWConversationMessage lastMessage) {
                refreshData();
            }
        };
        conversationUnreadNumReceiver.register(getContext());
    }

    /**
     * 注销监听
     */
    public void onDestroy(){
        if(null != conversationUnreadNumReceiver){
            conversationUnreadNumReceiver.unregister(getContext());
        }
    }

}
