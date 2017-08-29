package com.jopool.crow.imkit.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * 该布局能监听到软键盘的变化
 * 
 * @author xuan
 */
public class CWConversationRelativeLayout extends RelativeLayout {
	private OnKeyBoardStateChangListener onKeyBoardStateChangListener;

	public CWConversationRelativeLayout(Context context) {
		super(context);
	}

	public CWConversationRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CWConversationRelativeLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (null != onKeyBoardStateChangListener && h != oldh) {
			boolean isShow = false;
			if (h < oldh) {
				isShow = true;
			}

			onKeyBoardStateChangListener.onKeyBoardStateChanged(isShow);
		}
	}

	/**
	 * 设置键盘变化监听
	 * 
	 * @param onKeyBoardStateChangListener
	 */
	public void setOnKeyBoardStateChangListener(
			OnKeyBoardStateChangListener onKeyBoardStateChangListener) {
		this.onKeyBoardStateChangListener = onKeyBoardStateChangListener;
	}

	/**
	 * 布局中键盘是否显示事件
	 * 
	 * @author xuan
	 * @version $Revision: 1.0 $, $Date: 2012-11-1 下午6:10:00 $
	 */
	public interface OnKeyBoardStateChangListener {
		void onKeyBoardStateChanged(boolean isShow);
	}

}
