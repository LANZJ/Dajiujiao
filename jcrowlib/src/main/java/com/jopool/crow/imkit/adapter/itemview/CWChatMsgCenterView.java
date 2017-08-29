package com.jopool.crow.imkit.adapter.itemview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.jopool.crow.CWChat;
import com.jopool.crow.R;
import com.jopool.crow.imkit.view.CWBaseLayout;
import com.jopool.crow.imlib.entity.CWConversationMessage;
import com.jopool.crow.imlib.utils.CWValidator;

/**
 * 聊天中间布局
 * 
 * @author xuan
 */
public class CWChatMsgCenterView extends CWBaseLayout {
	private TextView tipTv;

	public CWChatMsgCenterView(Context context) {
		super(context);
	}

	public CWChatMsgCenterView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onViewInit() {
		inflate(getContext(), R.layout.cw_chat_listitem_msg_center, this);
		tipTv = (TextView) findViewById(R.id.tipTv);
	}

	public CWChatMsgCenterView loadData(final CWConversationMessage message) {
		if (CWValidator.isEmpty(message.getContent())) {
			tipTv.setVisibility(View.GONE);
		} else {
			tipTv.setVisibility(View.VISIBLE);
			tipTv.setText(message.getContent());
		}

		// 消息点击事件
		setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				CWChat.getInstance().getOnMessageClickListener()
						.onMessageClick(getContext(), view, message);
			}
		});

		return this;
	}

}
