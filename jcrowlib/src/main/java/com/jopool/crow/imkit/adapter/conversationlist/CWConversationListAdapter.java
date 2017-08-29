package com.jopool.crow.imkit.adapter.conversationlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jopool.crow.CWChat;
import com.jopool.crow.R;
import com.jopool.crow.imkit.adapter.CWBaseAdapter;
import com.jopool.crow.imkit.dialog.CWSingleSelectDialog;
import com.jopool.crow.imkit.listeners.GetConversationTitleRightViewProvider;
import com.jopool.crow.imkit.listeners.impl.DefaultGroupRightViewProvider;
import com.jopool.crow.imkit.receiver.CWTotalUnreadNumReceiver;
import com.jopool.crow.imkit.ui.uientity.CWUiConversation;
import com.jopool.crow.imkit.utils.ImageShowUtil;
import com.jopool.crow.imlib.enums.CWConversationType;
import com.jopool.crow.imlib.utils.CWFriendlyTimeUtil;

import java.util.List;

/**
 * JCrow消息列表适配器
 * Created by wuhk on 2016/3/23.
 */
public class CWConversationListAdapter extends CWBaseAdapter {
    private Context context;
    private List<CWUiConversation> dataList;

    public CWConversationListAdapter(Context context, List<CWUiConversation> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (null == view) {
            view = LayoutInflater.from(context).inflate(R.layout.cw_conversationlist_view_listitem, null);
        }
        ImageView photoIv = (ImageView) view.findViewById(R.id.photoIv);
        TextView numTv = (TextView) view.findViewById(R.id.numTv);
        TextView titleTv = (TextView) view.findViewById(R.id.titleTv);
        TextView timeTv = (TextView) view.findViewById(R.id.timeTv);
        TextView contentTv = (TextView) view.findViewById(R.id.contentTv);

        //消息会话数据
        final CWUiConversation conversation = dataList.get(position);
        if (conversation.getToType().equals(CWConversationType.GROUP)) {
            photoIv.setImageResource(R.drawable.cw_default_group);
        } else {
            ImageShowUtil.showHeadIcon(photoIv, conversation.getUrl());
        }
        initTextView(titleTv, conversation.getTitle());
        initTextView(contentTv, conversation.getDetail());
        initTextView(timeTv, CWFriendlyTimeUtil.friendlyTime2(conversation.getTime()));
        if (conversation.getUnreadNum() < 1) {
            numTv.setVisibility(View.GONE);
        } else {
            initTextView(numTv, String.valueOf(conversation.getUnreadNum()));
        }

        //点击事件
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (conversation.getToType().equals(CWConversationType.USER)) {
                    CWChat.getInstance().getConversationDelegate().startConversation(context, CWConversationType.USER, conversation.getToId(), conversation.getTitle());
                } else if (conversation.getToType().equals(CWConversationType.GROUP)) {
                    CWChat.getInstance().getConversationDelegate().startConversation(context,
                            CWConversationType.GROUP, conversation.getToId(), "", new DefaultGroupRightViewProvider(context));
                }
            }
        });
        //长按事件
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                CWSingleSelectDialog singleSelectDialog = new CWSingleSelectDialog.Builder(context).setItemTextAndOnClickListener(new String[]{"删除该聊天"},
                        new View.OnClickListener[]{new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CWChat.getInstance().removeConversationByToId(conversation.getToId());
                                dataList.clear();
                                List<CWUiConversation> newDataList = CWChat.getInstance().getUiModel().getUiConversationList();
                                dataList.addAll(newDataList);
                                notifyDataSetChanged();
                                CWTotalUnreadNumReceiver.notifyReceiver(context);
                            }
                        }}).create();
                singleSelectDialog.show();
                return true;
            }
        });
        return view;
    }

}
