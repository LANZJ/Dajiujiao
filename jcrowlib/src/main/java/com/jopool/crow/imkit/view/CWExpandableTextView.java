package com.jopool.crow.imkit.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextView;

/**
 * 可伸缩TextView
 *
 * @author xuan
 */
public class CWExpandableTextView extends TextView {

    private boolean mExpanded = false;//标记是否处于打开状态
    private int mCollapseLines = 1;//收起显示的行数
    private int mExpandedLines = 5;//展开显示的行数

    private int mOriginalWidth;
    private int mCollapseHeight;
    private int mExpandedHeight;

    private boolean mAnimating;//标记是否正在动画中
    private long mAnimationDuration = 200;//动画持续时间

    private OnExpandListener mOnExpandListener;
    private OnCollapseListener mOnCollapseListener;
    private OnTapListener onTapListener;

    private float mStartY;

    public CWExpandableTextView(Context context) {
        super(context);
        init(context, null);
    }

    public CWExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CWExpandableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        if (!mInitialized) {
//            mOriginalWidth = getMeasuredWidth();
//
//            setLines(mExpandedLines);
//            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//            mExpandedHeight = getMeasuredHeight();
//
//            setLines(mCollapseLines);
//            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//            mCollapseHeight = getMeasuredHeight();
//
//            mInitialized = true;
//            setMeasuredDimension(mOriginalWidth, mExpanded ? mExpandedHeight : mCollapseHeight);
//        } else {
//            if (!mAnimating) {
//                mOriginalWidth = getMeasuredWidth();
//                final int lineHeight = getLineHeight();
//                mExpandedHeight = lineHeight * mExpandedLines + 1;
//                mCollapseHeight = lineHeight * mCollapseLines + 1;
//                setMeasuredDimension(mOriginalWidth, mExpanded ? mExpandedHeight : mCollapseHeight);
//            }
//        }

        if (!mAnimating) {
            mOriginalWidth = getMeasuredWidth();
            final int lineHeight = getLineHeight();
            int padding = getPaddingBottom() + getPaddingTop();
            mExpandedHeight = lineHeight * mExpandedLines + 1 + padding;
            mCollapseHeight = lineHeight * mCollapseLines + 1 + padding;
            //setMeasuredDimension(mOriginalWidth, mExpanded ? mExpandedHeight : mCollapseHeight);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(mStartY - event.getY()) < 5.0f) {
                    if (null != onTapListener) {
                        onTapListener.onTap(this);
                    }
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    public void toggle() {
        toggle(true);
    }

    public void toggle(boolean animated) {
        if (mExpanded) {
            collapse(animated);
        } else {
            expand(animated);
        }
    }

    public void expand() {
        expand(true);
    }

    public void expand(boolean animated) {
        if (mAnimating) {
            return;
        }

        if (animated) {
            mAnimating = true;
            startAnimation(new ExpandOrCollapseAnimation());
        } else {
            setLines(mExpandedLines);
            fireOnExpandListener();
        }
        mExpanded = true;
    }

    public void collapse() {
        collapse(true);
    }

    public void collapse(boolean animated) {
        if (mAnimating) {
            return;
        }

        if (animated) {
            mAnimating = true;
            startAnimation(new ExpandOrCollapseAnimation());
        } else {
            setLines(mCollapseLines);
            fireOnCollapseListener();
        }
        mExpanded = false;
    }

    public void setCollapseLines(int collapseLines) {
        this.mCollapseLines = collapseLines;
    }

    public void setExpandedLines(int expandedLines) {
        this.mExpandedLines = expandedLines;
    }

    public CWExpandableTextView setOnTapListener(OnTapListener onTapListener) {
        this.onTapListener = onTapListener;
        return this;
    }

    public CWExpandableTextView setOnExpandListener(OnExpandListener onExpandListener) {
        mOnExpandListener = onExpandListener;
        return this;
    }

    public CWExpandableTextView setOnCollapseListener(OnCollapseListener onCollapseListener) {
        mOnCollapseListener = onCollapseListener;
        return this;
    }

    private void fireOnExpandListener() {
        if (null != mOnExpandListener) {
            mOnExpandListener.onExpand(this);
        }
    }

    private void fireOnCollapseListener() {
        if (null != mOnCollapseListener) {
            mOnCollapseListener.onCollapse(this);
        }
    }

    /**
     * 如果正在动画切换中,那么这个值的之后的状态,也就是说先改状态,再慢慢执行动画的
     */
    public boolean isExpanded() {
        return mExpanded;
    }

    public void setExpanded(boolean expanded) {
        mExpanded = expanded;
        mAnimating = false;
        if (expanded) {
            setLines(mExpandedLines);
        } else {
            setLines(mCollapseLines);
        }
    }

    public boolean isAnimating() {
        return mAnimating;
    }

    public void setAnimationDuration(long animationDuration) {
        mAnimationDuration = animationDuration;
    }

    public void changeExpanderHeight(int height) {
        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = mOriginalWidth;
        params.height = height;
        setLayoutParams(params);
    }

    private class ExpandOrCollapseAnimation extends Animation {
        private final int mStartHeight;
        private final int mDistance;

        public ExpandOrCollapseAnimation() {
            super();
            int endHeight;
            if (mExpanded) {
                mStartHeight = mExpandedHeight;
                endHeight = mCollapseHeight;
                //setLines(mCollapseLines);
            } else {
                mStartHeight = mCollapseHeight;
                endHeight = mExpandedHeight;
                //setLines(mExpandedLines);
            }
            mDistance = endHeight - mStartHeight;
            setDuration(mAnimationDuration);
            setAnimationListener(new ExpandAnimationListener());
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            int height = mStartHeight + Math.round(mDistance * interpolatedTime);
            changeExpanderHeight(height);
        }

    }

    private class ExpandAnimationListener implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {
            mAnimating = true;
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            mAnimating = false;
            if (mExpanded) {
                setLines(mExpandedLines);
                fireOnExpandListener();
            } else {
                setLines(mCollapseLines);
                fireOnCollapseListener();
            }
        }
    }

    public interface OnExpandListener {
        void onExpand(CWExpandableTextView view);
    }

    public interface OnCollapseListener {
        void onCollapse(CWExpandableTextView view);
    }

    public interface OnTapListener {
        void onTap(CWExpandableTextView view);
    }

}
