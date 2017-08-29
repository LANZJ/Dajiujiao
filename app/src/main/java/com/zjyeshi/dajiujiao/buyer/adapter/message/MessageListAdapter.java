package com.zjyeshi.dajiujiao.buyer.adapter.message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jopool.crow.CWChat;
import com.jopool.crow.imkit.listeners.impl.DefaultGroupRightViewProvider;
import com.jopool.crow.imkit.receiver.CWTotalUnreadNumReceiver;
import com.jopool.crow.imkit.ui.uientity.CWUiConversation;
import com.jopool.crow.imlib.db.dao.CWChatDaoFactory;
import com.jopool.crow.imlib.enums.CWConversationType;
import com.jopool.crow.imlib.model.CWConversationModel;
import com.jopool.crow.imlib.task.CWGroupRemoveUserTask;
import com.jopool.crow.imlib.utils.CWFriendlyTimeUtil;
import com.jopool.crow.imlib.utils.asynctask.callback.AsyncTaskSuccessCallback;
import com.jopool.crow.imlib.utils.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.activity.frame.fragment.Fragment2;
import com.zjyeshi.dajiujiao.buyer.activity.my.personal.ShowImageActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.chat.ChatManager;
import com.zjyeshi.dajiujiao.buyer.dao.DaoFactory;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.entity.contact.AddressUser;
import com.zjyeshi.dajiujiao.buyer.receiver.MessageSortReceiver;
import com.zjyeshi.dajiujiao.buyer.task.data.UserData;
import com.zjyeshi.dajiujiao.buyer.utils.DialogUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;
import com.zjyeshi.dajiujiao.buyer.utils.SingleSelectDialogUtil;
import com.zjyeshi.dajiujiao.buyer.views.UnReadNumView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 消息适配器
 * Created by wuhk on 2015/9/8.
 */
public class MessageListAdapter extends MBaseAdapter {
    private final Context context;
    private final List<CWUiConversation> dataList;

    public MessageListAdapter(Context context, List<CWUiConversation> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public View getView(final int position, View view, ViewGroup arg2) {
        if (null == view) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            view = mInflater.inflate(R.layout.listitem_message,
                    null);
        }
        ImageView photoIv = (ImageView) view.findViewById(R.id.photoIv);
        TextView titleTv = (TextView) view.findViewById(R.id.titleTv);
        final TextView contentTv = (TextView) view.findViewById(R.id.contentTv);
        UnReadNumView unreadView = (UnReadNumView) view.findViewById(R.id.unreadView);
        TextView timeTv = (TextView) view.findViewById(R.id.timeTv);
        RelativeLayout backLayout = (RelativeLayout) view.findViewById(R.id.backLayout);

        //消息会话数据
        final CWUiConversation conversation = dataList.get(position);

        //置顶显示不同的背景，自身排序优先级是20以内，20以上就是置顶
        if (conversation.getPriority() > 20) {
            backLayout.setBackgroundResource(R.drawable.priority_item_selector);
        } else {
            backLayout.setBackgroundResource(R.drawable.item_selector);
        }
        //单聊和群聊的头像
        if (conversation.getToType().equals(CWConversationType.GROUP)) {
            photoIv.setImageResource(R.drawable.default_group);
        } else if (conversation.getToType().equals(CWConversationType.USER)) {
            GlideImageUtil.glidImage(photoIv, ExtraUtil.getResizePic(conversation.getUrl(), 150, 150), R.drawable.default_tx);
        }

        initTextView(titleTv, conversation.getTitle());
        initTextView(contentTv, conversation.getDetail());
        if (null == conversation.getTime()) {
            timeTv.setVisibility(View.GONE);
        } else {
            timeTv.setVisibility(View.VISIBLE);
            initTextView(timeTv, CWFriendlyTimeUtil.friendlyTime2(conversation.getTime()));
        }

        unreadView.setNum(conversation.getUnreadNum());

        //点击事件
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (conversation.getToType().equals(CWConversationType.USER)) {
                    ChatManager.getInstance().startConversion(context, conversation.getToId());
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

                List<String> itemList = new ArrayList<String>();
                List<View.OnClickListener> onClickListenerList = new ArrayList<View.OnClickListener>();
                if (conversation.getToType().equals(CWConversationType.USER)) {
                    itemList.add("删除该会话");
                } else {
                    itemList.add("删除该会话并退出群聊");
                }
                //0 - 20之间才有置顶不置顶的操作
                if (conversation.getPriority() > 20) {
                    itemList.add("取消置顶");
                } else {
                    itemList.add("置顶聊天");
                }


                onClickListenerList.add(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtil.confirmSure(context, "删除会话同时会删除该会话聊天记录", "确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (conversation.getToType().equals(CWConversationType.GROUP)) {
                                    //在调一次退出群聊的接口
                                    CWGroupRemoveUserTask.removeUserFromGroup(context, conversation.getToId(), LoginedUser.getLoginedUser().getId(), new AsyncTaskSuccessCallback<String>() {
                                        @Override
                                        public void successCallback(Result<String> result) {
                                            CWConversationModel.getInstance().deleteConversation(context, conversation.getToId(), conversation.getToType());
                                        }
                                    });
                                } else {
                                    CWConversationModel.getInstance().deleteConversation(context, conversation.getToId(), conversation.getToType());
                                }
                                CWChat.getInstance().removeConversationByToId(
                                        conversation.getToId());
                                dataList.clear();
                                List<CWUiConversation> newDataList = CWChat.getInstance()
                                        .getUiModel().getUiConversationList();
                                CWTotalUnreadNumReceiver.notifyReceiver(context);
                                dataList.addAll(newDataList);
                                notifyDataSetChanged();
                            }
                        });
                    }
                });

                onClickListenerList.add(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (conversation.getPriority() > 20) {
                            //取消置顶
                            int priority = CWChat.getInstance().getConversationDelegate().getConversationPriorityProvider()
                                    .getConversationPriority(conversation.getToType(), conversation.getToId());
                            conversation.setPriority(priority);
                        } else {
                            //置顶
                            conversation.setPriority(30);
                        }
                        CWChatDaoFactory.getConversationDao().updatePriority(conversation.getToId(), conversation.getPriority());
                        notifyDataSetChanged();
                        MessageSortReceiver.notifyReceiver();
                    }
                });

                SingleSelectDialogUtil dialog = new SingleSelectDialogUtil.Builder(context)
                        .setItemTextAndOnClickListener(
                                itemList.toArray(new String[itemList.size()]),
                                onClickListenerList
                                        .toArray(new View.OnClickListener[onClickListenerList
                                                .size()])).createInstance();
                dialog.show();
                return true;
            }
        });
        return view;
    }
}
