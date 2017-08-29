package com.xuan.bigappleui.lib.view.popview;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.xuan.bigappleui.lib.utils.BULogUtil;

/**
 * 从底部弹出View
 * 
 * @author xuan
 */
public class BUPopViewLayout extends RelativeLayout {
	private Activity mActivity;

	public static final int DURATION = 300;
	public static final int POPMODE_FROM_LEFT = 1;// 从左部出现
	public static final int POPMODE_FROM_TOP = 2;// 从上部出现
	public static final int POPMODE_FROM_RIGHT = 3;// 从右部出现
	public static final int POPMODE_FROM_BOTTOM = 4;// 从底部出现
	/** 显示方式 */
	private int mPopMode = POPMODE_FROM_BOTTOM;

	/** 内容View对象 */
	private View mPopView;

	/** 是否显示状态 */
	private boolean mIsShow;
	/** 是否在滑动状态 */
	private boolean mIsSliding;

	private TranslateAnimation mShowAnim;
	private TranslateAnimation mDismissAnim;

	/**
	 * 构造方法
	 * 
	 * @param context
	 */
	public BUPopViewLayout(Context context) {
		super(context);
		init();
	}

	/**
	 * 构造方法
	 * 
	 * @param context
	 * @param attrs
	 */
	public BUPopViewLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	/**
	 * 构造方法
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public BUPopViewLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		setVisibility(View.INVISIBLE);
		mActivity = (Activity) getContext();
		mActivity.addContentView(this, new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.MATCH_PARENT));
		onCreateView();
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (!isInContentView(ev)) {
			// 我要处理
			return true;
		}
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (isInContentView(event)) {
			BULogUtil.d("In");
			return super.onInterceptTouchEvent(event);
		} else {
			BULogUtil.d("Out");
			switch (event.getAction()) {
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				dismiss();
				break;
			}

			return true;
		}
	}

	/**
	 * 显示
	 */
	public void show() {
		if (null == mPopView || mIsShow || mIsSliding) {
			return;
		}

		mIsSliding = true;
		mIsShow = true;
		mShowAnim = buildShowAnim();
		mPopView.setAnimation(mShowAnim);
		setVisibility(View.VISIBLE);
		mShowAnim.start();
	}

	/**
	 * 隐藏
	 */
	public void dismiss() {
		if (null == mPopView || !mIsShow || mIsSliding) {
			return;
		}

		mIsShow = false;
		mIsSliding = true;
		mDismissAnim = buildDismissAnim();
		mPopView.setAnimation(mDismissAnim);
		mDismissAnim.start();
		invalidate();// 必须，通知他的父亲需要重新绘制
	}

	/**
	 * 显示或者隐藏
	 */
	public void toggle() {
		if (mIsShow) {
			dismiss();
		} else {
			show();
		}
	}

	/**
	 * 设置View
	 * 
	 * @param resId
	 */
	public BUPopViewLayout setContentView(int resId) {
		return setContentView(LayoutInflater.from(getContext()).inflate(resId,
				null));
	}

	/**
	 * 设置View
	 * 
	 * @return
	 */
	public BUPopViewLayout setContentView(View view) {
		this.mPopView = view;
		resetView();
		resetAnima();
		this.mPopView.setClickable(true);
		return this;
	}

	/**
	 * 获取弹出方式
	 * 
	 * @return
	 */
	public int getPopMode() {
		return this.mPopMode;
	}

	/**
	 * 设置弹出方式
	 * 
	 * @param popMode
	 * @return
	 */
	public BUPopViewLayout setPopMode(int popMode) {
		if (popMode == this.mPopMode) {
			return this;
		}

		this.mPopMode = popMode;
		resetView();
		resetAnima();
		return this;
	}

	/**
	 * 是否显示状态
	 * 
	 * @return
	 */
	public boolean isShow() {
		return mIsShow;
	}

	// 判断按下是否在ContentView区域内
	private boolean isInContentView(MotionEvent event) {
		boolean isIn = false;
		switch (mPopMode) {
		case POPMODE_FROM_LEFT:
			isIn = (int) event.getX() <= mPopView.getWidth();
			break;
		case POPMODE_FROM_TOP:
			isIn = (int) event.getY() <= mPopView.getHeight();
			break;
		case POPMODE_FROM_RIGHT:
			isIn = (int) event.getX() >= getMeasuredWidth()
					- mPopView.getWidth();
			break;
		case POPMODE_FROM_BOTTOM:
			isIn = (int) event.getY() >= getMeasuredHeight()
					- mPopView.getHeight();
		default:
			break;
		}

		BULogUtil.d("isInContentView:" + isIn);
		return isIn;
	}

	// 重置contentView的位置
	private void resetView() {
		if (null == mPopView) {
			return;
		}

		removeAllViews();
		RelativeLayout.LayoutParams lp = null;
		switch (mPopMode) {
		case POPMODE_FROM_LEFT:
			lp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.MATCH_PARENT);
			lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			mPopView.setLayoutParams(lp);
			break;
		case POPMODE_FROM_TOP:
			lp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			mPopView.setLayoutParams(lp);
			break;
		case POPMODE_FROM_RIGHT:
			lp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.MATCH_PARENT);
			lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			mPopView.setLayoutParams(lp);
			break;
		case POPMODE_FROM_BOTTOM:
		default:
			lp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			mPopView.setLayoutParams(lp);
			break;
		}

		addView(mPopView, lp);
	}

	// 重置动画
	private void resetAnima() {
		mShowAnim = null;
		mDismissAnim = null;
	}

	protected TranslateAnimation buildShowAnim() {
		if (null == mShowAnim) {
			switch (mPopMode) {
			case POPMODE_FROM_LEFT:
				mShowAnim = new TranslateAnimation(-mPopView.getWidth(), 0, 0,
						0);
				break;
			case POPMODE_FROM_TOP:
				mShowAnim = new TranslateAnimation(0, 0, -mPopView.getHeight(),
						0);
				break;
			case POPMODE_FROM_RIGHT:
				mShowAnim = new TranslateAnimation(mPopView.getWidth(), 0, 0, 0);
				break;
			case POPMODE_FROM_BOTTOM:
			default:
				mShowAnim = new TranslateAnimation(0, 0, mPopView.getHeight(),
						0);
				break;
			}

			mShowAnim.setInterpolator(new AccelerateInterpolator());
			mShowAnim.setDuration(DURATION);
			mShowAnim.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					mIsSliding = false;
				}
			});
		}

		return mShowAnim;
	}

	protected TranslateAnimation buildDismissAnim() {
		if (null == mDismissAnim) {
			switch (mPopMode) {
			case POPMODE_FROM_LEFT:
				mDismissAnim = new TranslateAnimation(0, -mPopView.getWidth(),
						0, 0);
				break;
			case POPMODE_FROM_TOP:
				mDismissAnim = new TranslateAnimation(0, 0, 0,
						-mPopView.getHeight());
				break;
			case POPMODE_FROM_RIGHT:
				mDismissAnim = new TranslateAnimation(0, mPopView.getWidth(),
						0, 0);
				break;
			case POPMODE_FROM_BOTTOM:
			default:
				mDismissAnim = new TranslateAnimation(0, 0, 0,
						mPopView.getHeight());
				break;
			}

			mDismissAnim.setInterpolator(new AccelerateInterpolator());
			mDismissAnim.setDuration(DURATION);
			mDismissAnim.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					setVisibility(View.INVISIBLE);
					mIsSliding = false;
				}
			});
		}

		return mDismissAnim;
	}

	/**
	 * 子类自己实现
	 */
	protected void onCreateView() {
		// 子类实现后需要调用setContentView
	}

}
