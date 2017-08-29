package com.xuan.bigappleui.lib.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ListView;

import com.xuan.bigappleui.lib.utils.ui.drawable.ArrowDrawable;
import com.xuan.bigappleui.lib.view.listview.pulltorefresh.BUHeaderView;
import com.xuan.bigappleui.lib.view.listview.pulltorefresh.UIHelper;

/**
 * 扩展了ListView控件，支持下拉刷新和上拉加载更多
 * 
 * @author xuan
 */
public class BURefreshListView extends ListView {
	/**
	 * 0:下拉刷新、1:松开刷新、2:正在刷新、3:刷新完成
	 */
	private static final int PULL_TO_REFRESH = 0;
	private static final int RELEASE_TO_REFRESH = 1;
	private static final int REFRESHING = 2;
	private static final int DONE = 3;

	/** 表示着列表正处于哪种状态 */
	private int state = DONE;

	/** 刷新提示布局 */
	private View mHeaderView;
	private View mFooterView;

	/** 提示动画 */
	private Animation mAnim;
	/** 动画复原 */
	private Animation mReverseAnim;

	// 记录状态切换时的一些标记
	private boolean mIsCanStartPull;// 记录是否可以开始拉动
	private int startY;// 移动开始时记录的Y高度
	private boolean isBack;// 标志着那个刷新箭头是否需要设置翻转回去

	// 头部的一些测量距离参数
	private int headContentHeight;
	private int headContentOriginalTopPadding;

	/** 触发刷新的接口 */
	public RefreshListener mRefreshListener;

	/** 是否可以下拉刷新 */
	private boolean mCanPullDown;
	/** 是否可以上拉刷新 */
	private boolean mCanPullUp;

	/** 阻尼系数 */
	private float damping = 0.3f;

	public BURefreshListView(Context context) {
		super(context);
		init(context);
	}

	public BURefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public BURefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		mAnim = buildAnim();
		mReverseAnim = buildReverseAnim();
		setCanPullDown(true);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!mCanPullDown && !mCanPullUp) {
			// 不需要下拉刷新啥都不处理
			return super.onTouchEvent(event);
		}

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			checkStartRecord(event);
			break;
		case MotionEvent.ACTION_MOVE:
			checkStartRecord(event);

			// 如果不是正在刷新，并检测到可以下拉或者上拉时，就要执行下面一系列的变化
			if (state != REFRESHING && mIsCanStartPull) {
				int tempY = (int) event.getY();
				// 滑动时RELEASE_TO_REFRESH状态下，转变有3种情况：1.上推又到PULL_TO_REFRESH（下拉刷新）状态
				// 2.上推回到done状态
				// 3.一直往下拉不用管
				if (state == RELEASE_TO_REFRESH) {
					if ((filterDamping(tempY - startY) < headContentHeight + 20)
							&& (filterDamping(tempY - startY)) > 0) {
						state = PULL_TO_REFRESH;
						changeHeaderViewByState();
					} else if (filterDamping(tempY - startY) <= 0) {
						state = DONE;
						changeHeaderViewByState();
					} else {
					}
				}

				// 滑动时PULL_TO_REFRESH状态下，转变有两种情况：1.下拉到RELEASE_TO_REFRESH（松开刷新）状态
				// 2.上推回到done状态
				else if (state == PULL_TO_REFRESH) {
					if (filterDamping(tempY - startY) >= headContentHeight + 20) {
						state = RELEASE_TO_REFRESH;
						isBack = true;
						changeHeaderViewByState();
					} else if (filterDamping(tempY - startY) <= 0) {
						state = DONE;
						changeHeaderViewByState();
					}
				}

				// 滑动时DONE状态下，转变只有一种情况：1.下拉到PULL_TO_REFRESH（下拉刷新）状态
				else if (state == DONE) {
					if (filterDamping(tempY - startY) > 0) {
						state = PULL_TO_REFRESH;
						changeHeaderViewByState();
					}
				}

				// PULL_TO_REFRESH状态下,需要根据下拉距离实时更新头部的Padding距离
				if (state == PULL_TO_REFRESH) {
					int topPadding = ((-1 * headContentHeight + filterDamping(tempY
							- startY)));
					mHeaderView.setPadding(mHeaderView.getPaddingLeft(),
							topPadding, mHeaderView.getPaddingRight(),
							mHeaderView.getPaddingBottom());
					mHeaderView.invalidate();
				}

				// RELEASE_TO_REFRESH状态下，需要根据下拉距离实时更新头部的Padding距离
				if (state == RELEASE_TO_REFRESH) {
					int topPadding = ((filterDamping(tempY - startY) - headContentHeight));
					mHeaderView.setPadding(mHeaderView.getPaddingLeft(),
							topPadding, mHeaderView.getPaddingRight(),
							mHeaderView.getPaddingBottom());
					mHeaderView.invalidate();
				}
			}
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			// 当抬起时，如果是正在刷新就不管
			if (state != REFRESHING) {
				if (state == DONE) {
					// 当抬起时，如果是DONE(已经完成状态)，就什么都不做
				} else if (state == PULL_TO_REFRESH) {
					// 当抬起时，如果是PULL_To_REFRESH(下拉刷新提示)，就直接到DONE状态，只改变一下头就可以，不用去加载数据
					state = DONE;
					changeHeaderViewByState();
				} else if (state == RELEASE_TO_REFRESH) {
					// 当抬起时，如果是RELEASE_To_REFRESH(松开刷新提示)，就到REFRESHING(刷新中)状态，改变头，并触发刷新事件
					state = REFRESHING;
					changeHeaderViewByState();
					if (null != mRefreshListener) {
						mRefreshListener.onPullDownRefresh(false);
					}
				}
			}

			mIsCanStartPull = false;
			isBack = false;
			break;

		}
		return super.onTouchEvent(event);
	}

	/** 是否开始记录下拉或者上拉 */
	private void checkStartRecord(MotionEvent event) {
		if ((isFirstViewTop() || isLastViewBottom()) && !mIsCanStartPull) {
			// 第一个item显示在第一个位置时，开始记录下拉刷新动作
			startY = (int) event.getY();
			mIsCanStartPull = true;
		}
	}

	/** 手动测量指定View */
	private void measureView(View child) {
		ViewGroup.LayoutParams lp = child.getLayoutParams();
		if (null == lp) {
			lp = new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, lp.width);
		int lpHeight = lp.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}

		child.measure(childWidthSpec, childHeightSpec);
	}

	/** 当状态改变时候，调用该方法，以更新头部的显示界面 */
	private void changeHeaderViewByState() {
		switch (state) {
		case RELEASE_TO_REFRESH:
			releaseToRefresh(mHeaderView);
			break;
		case PULL_TO_REFRESH:
			pullToRefresh(mHeaderView);
			break;
		case REFRESHING:
			refreshing(mHeaderView);
			break;
		case DONE:
			done(mHeaderView);
			break;
		}
	}

	/** 进过阻尼处理 */
	private int filterDamping(float original) {
		return (int) (original * damping);
	}

	/**
	 * 点击刷新，这种情况就是供，列表外部有个按钮，然后点击就可以刷新列表
	 */
	public void clickRefresh() {
		setSelection(0);
		state = REFRESHING;
		changeHeaderViewByState();
		if (null != mRefreshListener) {
			mRefreshListener.onPullDownRefresh(true);
		}
	}

	/**
	 * 设置刷新监听器
	 * 
	 * @param l
	 */
	public void setRefreshListener(RefreshListener l) {
		this.mRefreshListener = l;
	}

	/**
	 * 当数据加载完成后，调用该方法可以手动设置列表为done状态
	 * 
	 * @param update
	 */
	public void onRefreshComplete(String update) {
		setLastUpdateHint(mHeaderView, update);
		state = DONE;
		changeHeaderViewByState();
	}

	/**
	 * 设置是否需要下拉刷新
	 * 
	 * @param canPullDown
	 */
	public void setCanPullDown(boolean canPullDown) {
		this.mCanPullDown = canPullDown;
		if (canPullDown) {
			// 创建头尾布局
			mHeaderView = createHeaderView((Activity) getContext());

			// 头部的原始paddingTop值
			headContentOriginalTopPadding = mHeaderView.getPaddingTop();
			measureView(mHeaderView);

			// 隐藏头部
			headContentHeight = mHeaderView.getMeasuredHeight();
			mHeaderView.setPadding(mHeaderView.getPaddingLeft(), -1
					* headContentHeight, mHeaderView.getPaddingRight(),
					mHeaderView.getPaddingBottom());
			mHeaderView.invalidate();

			addHeaderView(mHeaderView);
		} else {
			if (null != mHeaderView) {
				removeHeaderView(mHeaderView);
			}
		}
	}

	/**
	 * 设置是否需要下拉刷新
	 * 
	 * @param canPullUp
	 */
	public void setCanPullUp(boolean canPullUp) {
		this.mCanPullUp = canPullUp;
		if (canPullUp) {
			// 创建尾部
			mFooterView = createFooterView((Activity) getContext());

			// 头部的原始paddingTop值
			headContentOriginalTopPadding = mFooterView.getPaddingTop();
			measureView(mFooterView);

			// 隐藏头部
			headContentHeight = mFooterView.getMeasuredHeight();
			mFooterView.setPadding(mFooterView.getPaddingLeft(), -1
					* headContentHeight, mFooterView.getPaddingRight(),
					mFooterView.getPaddingBottom());
			mFooterView.invalidate();

			addFooterView(mFooterView);
		} else {
			if (null != mFooterView) {
				removeFooterView(mFooterView);
			}
		}
	}

	/**
	 * 设置最后一次更新时间
	 * 
	 * @param view
	 * @param update
	 */
	protected void setLastUpdateHint(View view, String update) {
		if (view instanceof BUHeaderView) {
			BUHeaderView headerView = (BUHeaderView) view;
			headerView.getLastUpdateHint().setText(update);
		}
	}

	/**
	 * 设置下拉刷新时的阻尼系数
	 * 
	 * @param damping
	 */
	protected void setDamping(float damping) {
		this.damping = damping;
	}

	/** 是否第一个View到顶了 */
	protected boolean isFirstViewTop() {
		final int count = getChildCount();
		if (0 == count) {
			return true;
		}
		final int firstVisiblePosition = getFirstVisiblePosition();
		final View firstChildView = getChildAt(0);
		return firstChildView.getTop() == 0 && (firstVisiblePosition == 0);
	}

	/** 是否最后一个View到底了 */
	protected boolean isLastViewBottom() {
		final int count = getChildCount();
		if (0 == count) {
			return false;//
		}

		final int lastVisiblePosition = getLastVisiblePosition();
		return (lastVisiblePosition == (getAdapter().getCount() - getHeaderViewsCount()))
				&& (getChildAt(getChildCount() - 1).getBottom() == (getBottom() - getTop()));
	}

	/** 创建复原动画 */
	protected RotateAnimation buildReverseAnim() {
		RotateAnimation reverseA = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseA.setInterpolator(new LinearInterpolator());
		reverseA.setDuration(100);
		reverseA.setFillAfter(true);
		return reverseA;
	}

	/** 创建动画 */
	protected RotateAnimation buildAnim() {
		RotateAnimation a = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		a.setInterpolator(new LinearInterpolator());
		a.setDuration(100);
		a.setFillAfter(true);
		return a;
	}

	/**
	 * 创建头部布局
	 * 
	 * @param activity
	 * @return
	 */
	protected BUHeaderView createHeaderView(Activity activity) {
		return UIHelper.getHeaderView(activity);
	}

	/**
	 * 创建尾部布局
	 * 
	 * @param activity
	 * @return
	 */
	protected BUHeaderView createFooterView(Activity activity) {
		return UIHelper.getHeaderView(activity);
	}

	// 完成状态，隐藏头部
	protected void done(View view) {
		if (view instanceof BUHeaderView) {
			BUHeaderView headerView = (BUHeaderView) view;
			headerView.setPadding(mHeaderView.getPaddingLeft(), -1
					* headContentHeight, mHeaderView.getPaddingRight(),
					mHeaderView.getPaddingBottom());
			headerView.invalidate();
			headerView.getProgressBar().setVisibility(View.GONE);
			headerView.getArrow().clearAnimation();
			headerView.getArrow().setImageDrawable(new ArrowDrawable());
			headerView.getPullToRefreshHint().setText("下拉可以刷新");
			headerView.getLastUpdateHint().setVisibility(View.VISIBLE);
		}
	}

	// 下拉刷新状态
	protected void pullToRefresh(View view) {
		if (view instanceof BUHeaderView) {
			BUHeaderView headerView = (BUHeaderView) view;
			headerView.getProgressBar().setVisibility(View.GONE);
			headerView.getPullToRefreshHint().setVisibility(View.VISIBLE);
			headerView.getLastUpdateHint().setVisibility(View.VISIBLE);
			headerView.getArrow().clearAnimation();
			headerView.getArrow().setVisibility(View.VISIBLE);
			if (isBack) {
				// 把箭头设置回去
				isBack = false;
				headerView.getArrow().clearAnimation();
				headerView.getArrow().startAnimation(mReverseAnim);
			}
			headerView.getPullToRefreshHint().setText("下拉可以刷新");
		}
	}

	// 正在刷新状态
	protected void refreshing(View view) {
		if (view instanceof BUHeaderView) {
			BUHeaderView headerView = (BUHeaderView) view;
			headerView.setPadding(mHeaderView.getPaddingLeft(),
					headContentOriginalTopPadding,
					mHeaderView.getPaddingRight(),
					mHeaderView.getPaddingBottom());
			headerView.invalidate();
			headerView.getProgressBar().setVisibility(View.VISIBLE);
			headerView.getArrow().clearAnimation();
			headerView.getArrow().setVisibility(View.GONE);
			headerView.getPullToRefreshHint().setText("加载中...");
			headerView.getLastUpdateHint().setVisibility(View.GONE);
		}
	}

	// 松开可以刷新状态
	protected void releaseToRefresh(View view) {
		if (view instanceof BUHeaderView) {
			BUHeaderView headerView = (BUHeaderView) view;
			headerView.getArrow().setVisibility(View.VISIBLE);
			headerView.getProgressBar().setVisibility(View.GONE);
			headerView.getPullToRefreshHint().setVisibility(View.VISIBLE);
			headerView.getLastUpdateHint().setVisibility(View.VISIBLE);
			headerView.getArrow().clearAnimation();
			headerView.getArrow().startAnimation(mAnim);
			headerView.getPullToRefreshHint().setText("松开可以刷新");
		}
	}

	/**
	 * 刷新监听事件
	 * 
	 * @author xuan
	 */
	public static interface RefreshListener {
		/**
		 * 下拉刷新
		 * 
		 * @param isManual
		 *            是否是手动调用方法触发
		 */
		void onPullDownRefresh(boolean isManual);

		/**
		 * 上拉加载更多
		 * 
		 * @param isManual
		 *            是否是手动调用方法触发
		 */
		void onPullUpRefresh(boolean isManual);
	}

}
