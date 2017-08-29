package com.jopool.crow.imkit.adapter.itemview;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jopool.crow.CWChat;
import com.jopool.crow.R;
import com.jopool.crow.imkit.expression.ExpressionUtils;
import com.jopool.crow.imkit.utils.ImageShowUtil;
import com.jopool.crow.imkit.view.CWBaseLayout;
import com.jopool.crow.imlib.entity.CWConversationMessage;
import com.jopool.crow.imlib.entity.CWUser;
import com.jopool.crow.imlib.enums.CWConversationType;
import com.jopool.crow.imlib.enums.CWMessageContentType;
import com.jopool.crow.imlib.enums.CWReadStatus;
import com.jopool.crow.imlib.enums.CWSendStatus;
import com.jopool.crow.imlib.utils.CWValidator;
import com.jopool.crow.imlib.utils.CWFriendlyTimeUtil;
import com.jopool.crow.imlib.utils.bitmap.BPBitmapLoader;

/**
 * 聊天左边布局
 *
 * @author xuan
 */
public class CWChatMsgLeftView extends CWBaseLayout {
    private ImageView headIv;
    private TextView contentTextIv;
    private ImageView contentVoiceIv;
    private ImageView contentImageIv;
    private TextView vioceLengthTv;
    private ImageView newTipIv;
    private ProgressBar progressBar;
    private ImageView resendIv;
    private TextView tipTv;
    private TextView nameTv;

    private View contentLayout;

    //
    private View contentUrlLayout;//网页链接布局

    public CWChatMsgLeftView(Context context) {
        super(context);
    }

    public CWChatMsgLeftView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onViewInit() {
        inflateView();
        headIv = (ImageView) findViewById(R.id.headIv);
        contentTextIv = (TextView) findViewById(R.id.contentTextIv);
        contentVoiceIv = (ImageView) findViewById(R.id.contentVoiceIv);
        contentImageIv = (ImageView) findViewById(R.id.contentImageIv);
        vioceLengthTv = (TextView) findViewById(R.id.vioceLengthTv);
        newTipIv = (ImageView) findViewById(R.id.newTipIv);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        resendIv = (ImageView) findViewById(R.id.resendIv);
        contentLayout = findViewById(R.id.contentLayout);
        tipTv = (TextView) findViewById(R.id.tipTv);
        nameTv = (TextView) findViewById(R.id.nameTv);
        //
        contentUrlLayout = findViewById(R.id.contentUrlLayout);
    }

    protected void inflateView() {
        inflate(getContext(), R.layout.cw_chat_listitem_msg_left, this);
    }

    public CWChatMsgLeftView loadData(final CWConversationMessage preMessage,
                                      final CWConversationMessage message) {
        goneAllView();
        // 头像
        final CWUser user = CWChat.getInstance().getProviderDelegate().getGetUserInfoProvider().getUserById(message.getSenderUserId());
        if (null != user) {
            ImageShowUtil.showHeadIcon(headIv, user.getUrl());
        } else {
            ImageShowUtil.showHeadIcon(headIv, null);
        }

        //群聊需要显示对方的名字
        if (CWConversationType.GROUP.equals(message.getConversationType())) {
            if (null != user) {
                nameTv.setVisibility(View.VISIBLE);
                nameTv.setText(user.getName());
            } else {
                nameTv.setVisibility(View.VISIBLE);
                nameTv.setText("未知");
            }
        } else {
            nameTv.setVisibility(View.GONE);
        }
        // 消息内容
        CWMessageContentType messageContentType = message
                .getMessageContentType();
        if (CWSendStatus.FAIL.equals(message.getSendStatus())) {
            resendIv.setVisibility(View.VISIBLE);
        } else if (CWSendStatus.ING.equals(message.getSendStatus())) {
            progressBar.setVisibility(View.VISIBLE);
        }

        if (CWMessageContentType.TEXT.equals(messageContentType)) {
            // 文本
            showText(message);
        } else if (CWMessageContentType.IMAGE.equals(messageContentType)) {
            // 图片
            showImage(message);
        } else if (CWMessageContentType.VOICE.equals(messageContentType)) {
            // 语音
            showVoice(message);
        } else if (CWMessageContentType.URL.equals(messageContentType)) {
            //网页链接
            showUrl(message);
        } else {
            showDefault();
        }
        // 消息点击事件
        contentLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                CWChat.getInstance().getOnMessageClickListener()
                        .onMessageClick(getContext(), view, message);
            }
        });

        // 消息长按事件
        contentLayout.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return CWChat.getInstance().getOnMessageLongClickListener()
                        .onMessageLongClick(getContext(), view, message);
            }
        });

        // 重发按钮点击事件
        resendIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                CWChat.getInstance().getOnMessageClickListener()
                        .onResendClick(getContext(), view, message);
            }
        });

        // 头像点击事件
        headIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                CWChat.getInstance().getPortraitClickListener()
                        .onPortaitClick(getContext(), view, user.getUserId());
            }
        });

        // 60秒内的连续消息不显示时间
        if (null != preMessage
                && (message.getCreationTime().getTime()
                - preMessage.getCreationTime().getTime() < 60000)) {
            tipTv.setVisibility(View.GONE);
        } else {
            tipTv.setText(CWFriendlyTimeUtil.friendlyTime(message.getCreationTime()));
            tipTv.setVisibility(View.VISIBLE);
        }

        return this;
    }

    /**
     * 文本
     */
    private void showText(CWConversationMessage message) {
        contentTextIv.setVisibility(View.VISIBLE);
        ExpressionUtils.setText((Activity) getContext(), contentTextIv,
                message.getContent());
    }

    /**
     * 图片
     */
    private void showImage(final CWConversationMessage message) {
        contentImageIv.setVisibility(View.VISIBLE);
        BPBitmapLoader.getInstance().display(contentImageIv,
                message.getContent());
    }

    private void showUrl(final CWConversationMessage message) {
        TextView contentUrlLayoutDetailTv = (TextView)contentUrlLayout.findViewById(R.id.contentUrlLayoutDetailTv);
        TextView contentUrlLayoutTitleTv = (TextView)contentUrlLayout.findViewById(R.id.contentUrlLayoutTitleTv);
        contentUrlLayout.setVisibility(View.VISIBLE);
        contentUrlLayoutDetailTv.setText(message.getContent());
        if(!CWValidator.isEmpty(message.getExt())){
            contentUrlLayoutTitleTv.setText(message.getExt());
        }else{
            contentUrlLayoutTitleTv.setText("网页链接");
        }
    }

    /**
     * 语音
     */
    private void showVoice(CWConversationMessage message) {
        contentVoiceIv.setVisibility(View.VISIBLE);

        // if (null != contentVoiceIv.getDrawable()
        // && contentVoiceIv.getDrawable() instanceof AnimationDrawable) {
        // AnimationDrawable ad = (AnimationDrawable) contentVoiceIv
        // .getDrawable();
        // if (ad.isRunning()) {
        //
        // }
        // }

        if (CWReadStatus.UNREAD.equals(message.getReadStatus()) || CWReadStatus.READED.equals(message.getReadStatus())) {
            newTipIv.setVisibility(View.VISIBLE);// 未听
        }
        vioceLengthTv.setVisibility(View.VISIBLE);
        vioceLengthTv.setText(String.valueOf(message.getVoiceLength()) + "''");
    }

    /**
     * 默认
     */
    private void showDefault() {
        contentTextIv.setVisibility(View.VISIBLE);
        contentTextIv.setText("不支持显示该消息内容，请升级到最新版本。");
    }

    /**
     * 先隐藏所有布局
     */
    private void goneAllView() {
        headIv.setVisibility(View.GONE);
        contentTextIv.setVisibility(View.GONE);
        contentVoiceIv.setVisibility(View.GONE);
        contentImageIv.setVisibility(View.GONE);
        vioceLengthTv.setVisibility(View.GONE);
        newTipIv.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        resendIv.setVisibility(View.GONE);
        contentUrlLayout.setVisibility(View.GONE);
    }

}
