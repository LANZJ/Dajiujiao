package com.zjyeshi.dajiujiao.buyer.views.comment;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jopool.crow.imkit.expression.ChatImagesResource;
import com.jopool.crow.imkit.view.CWBaseLayout;
import com.jopool.crow.imlib.utils.CWLogUtil;

/**
 * 播放语音布局
 * 
 * @author xuan
 */
public class CommentVolumeView extends CWBaseLayout {
	public static final String MESSAGE_SLIDE_CANCEL = "手指上滑，取消发送";
	public static final String MESSAGE_REALSE_CANCEL = "松开手指，取消发送";
	public static final String MESSAGE_TOO_SHOT = "说话时间太短";
	public static final String MESSAGE_PREPARE = "正在准备录音";

	public static final int STATE_HIDE = 1;// 不显示状态
	public static final int STATE_PREPARE = 2;// 录音准备状态
	public static final int STATE_SLIDE_UP_CANCEL = 3;// 正在录音,上滑取消
	public static final int STATE_RELEASE_UP_CALCEL = 4;// 正在录音，松开取消
	public static final int STATE_TOAST_TOO_SHOT = 5;// 录音太短

	private int currentState = STATE_HIDE;// 当前状态

	private ImageView imageIv;
	private TextView messageTv;

	private final Handler handler = new Handler();

	private final Runnable hideRunbale = new Runnable() {
		@Override
		public void run() {
			hide();
		}
	};

	private final Runnable showSlideUpCancelRunnable = new Runnable() {
		@Override
		public void run() {
			interruptPrepare();
			showSlideUpCancel();
		}
	};

	public CommentVolumeView(Context context) {
		super(context);
	}

	public CommentVolumeView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onViewInit() {
		loadView();
		initWidgits();
		setVisibility(View.GONE);
	}

	/** Load view */
	private void loadView() {
		inflate(getContext(), com.jopool.crow.R.layout.cw_chat_view_conversation_volume, this);
		imageIv = (ImageView) findViewById(com.jopool.crow.R.id.imageIv);
		messageTv = (TextView) findViewById(com.jopool.crow.R.id.messageTv);
	}

	/** Init view */
	private void initWidgits() {

	}

	/**
	 * 设置显示音量大小
	 * 
	 * @param volume
	 */
	public void setVolume(int volume) {
		if (STATE_SLIDE_UP_CANCEL == currentState
				|| STATE_RELEASE_UP_CALCEL == currentState) {
			// 正在录音时才能设置音量
			CWLogUtil.d("Input volume is:" + volume);
			int v = Math.max(Math.min(volume, 7), 0);
			CWLogUtil.d("Result volume is:" + v);
			imageIv.setImageResource(ChatImagesResource.getVolumes()[v]);
		}
	}

	/**
	 * 改变显示状态
	 * 
	 * @param state
	 */
	public void changeState(int state) {
		switch (state) {
		case STATE_PREPARE:
			messageTv.setText(MESSAGE_PREPARE);
			messageTv.setBackgroundColor(getResources().getColor(
					com.jopool.crow.R.color.cw_color_transparent));
			imageIv.setImageResource(com.jopool.crow.R.drawable.cw_chat_volume_prepare);
			setVisibility(View.VISIBLE);

			handler.postDelayed(showSlideUpCancelRunnable, 1000);
			break;
		case STATE_SLIDE_UP_CANCEL:
			if (STATE_PREPARE == currentState) {
				return;
			}

			messageTv.setText(MESSAGE_SLIDE_CANCEL);
			messageTv.setBackgroundColor(getResources().getColor(
					com.jopool.crow.R.color.cw_color_transparent));
			imageIv.setImageResource(com.jopool.crow.R.drawable.cw_chat_volume_1);
			setVisibility(View.VISIBLE);
			break;
		case STATE_RELEASE_UP_CALCEL:
			if (STATE_PREPARE == currentState) {
				return;
			}

			messageTv.setText(MESSAGE_REALSE_CANCEL);
			messageTv.setBackgroundColor(getResources().getColor(
					com.jopool.crow.R.color.cw_color_chat_volume_message_bg));
			imageIv.setImageResource(com.jopool.crow.R.drawable.cw_chat_volume_cancel);
			setVisibility(View.VISIBLE);
			break;
		case STATE_TOAST_TOO_SHOT:
			messageTv.setText(MESSAGE_TOO_SHOT);
			messageTv.setBackgroundColor(getResources().getColor(
					com.jopool.crow.R.color.cw_color_transparent));
			imageIv.setImageResource(com.jopool.crow.R.drawable.cw_chat_volume_tooshot);
			setVisibility(View.VISIBLE);

			handler.postDelayed(hideRunbale, 2000);
			break;
		case STATE_HIDE:
			setVisibility(View.GONE);
		default:
			break;
		}

		this.currentState = state;
	}

	/**
	 * 显示正在准备录音
	 */
	public void showPrePare() {
		changeState(STATE_PREPARE);
	}

	/**
	 * 显示上滑取消
	 */
	public void showSlideUpCancel() {
		changeState(STATE_SLIDE_UP_CANCEL);
	}

	/**
	 * 显示松开取消
	 */
	public void showReleaseUpCancel() {
		changeState(STATE_RELEASE_UP_CALCEL);
	}

	/**
	 * 显示录音太短
	 */
	public void toastTooShot() {
		changeState(STATE_TOAST_TOO_SHOT);
	}

	/**
	 * 隐藏
	 */
	public void hide() {
		changeState(STATE_HIDE);
	}

	/**
	 * 是否是隐藏状态
	 * 
	 * @return
	 */
	public boolean isHide() {
		return currentState == STATE_HIDE;
	}

	/**
	 * 中断准备状态，主要是要移除1S后显示的上滑取消状态
	 */
	public void interruptPrepare() {
		hide();
		handler.removeCallbacks(showSlideUpCancelRunnable);
	}

}
