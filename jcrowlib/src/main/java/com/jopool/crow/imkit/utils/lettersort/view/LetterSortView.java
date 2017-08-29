package com.jopool.crow.imkit.utils.lettersort.view;

import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;


/**
 * 按字母排序分类，并用字母做检索
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-7-15 下午7:12:47 $
 */
public class LetterSortView extends ViewGroup {
    private final Context context;

    private LetterSortBar letterSortBar;// 字母条控件
    private TextView mLetterShow;// 字母提示控件
    private ListView listView;// 数据显示列表控件

    private int mLetterSortBarWidth = 40;// 字母条的宽度，单位px
    private int mLetterShowWidth = 100;// 显示字母提示的宽度，单位px

    public LetterSortView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LetterSortView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    // 添加一些子控件初始化
    private void initView() {
        // 添加ListView
        if (null == listView) {
            listView = new ListView(context);
            listView.setVerticalScrollBarEnabled(false);
            listView.setCacheColorHint(Color.parseColor("#00000000"));
            listView.setDivider(null);
        }
        addView(listView);

        // 添加字母条
        if (null == letterSortBar) {
            letterSortBar = new LetterSortBar(context);

            letterSortBar.setOnLetterChange(new LetterSortBar.OnLetterChange() {
                @Override
                public void letterChange(String letter) {
                    mLetterShow.setText(letter);
                    if (mLetterShow.getVisibility() != View.VISIBLE) {
                        mLetterShow.setVisibility(View.VISIBLE);
                    }

                    // 定位ListView的显示区域
                    LetterSortAdapter lsa = (LetterSortAdapter) listView
                            .getAdapter();
                    Integer indexInteger = lsa.getIndexMap().get(letter);
                    final int index = (null == indexInteger) ? -1
                            : indexInteger;

                    listView.setSelection(index);
                    listView.requestFocusFromTouch();
                }
            });

            letterSortBar.setOutLetterSeacherBar(new LetterSortBar.OutLetterSeacherBar() {
                @Override
                public void outBar(String lastLetter) {
                    mLetterShow.setVisibility(View.GONE);
                }
            });
        }
        addView(letterSortBar);

        // 添加字母提示
        if (null == mLetterShow) {
            mLetterShow = new TextView(context);
            mLetterShow.setTextSize(50);
            mLetterShow.setTextColor(Color.WHITE);
            mLetterShow.setBackgroundColor(Color.BLACK);
            mLetterShow.setGravity(Gravity.CENTER);
            mLetterShow.setVisibility(View.GONE);
            TextPaint tp = mLetterShow.getPaint();
            tp.setFakeBoldText(true);
        }
        addView(mLetterShow);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (null != listView) {
            listView.measure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int childCount = getChildCount();
        final int width = getWidth();
        final int height = getHeight();
        for (int i = 0; i < childCount; i++) {
            final View childView = getChildAt(i);

            if (childView instanceof ListView) {
                childView.layout(0, 0, width, height);
            } else if (childView instanceof LetterSortBar) {
                childView.layout(width - mLetterSortBarWidth, 0, width, height);
            } else if (childView instanceof TextView) {
                childView.layout((width - mLetterShowWidth) / 2,
                        (height - mLetterShowWidth) / 2,
                        (width + mLetterShowWidth) / 2,
                        (height + mLetterShowWidth) / 2);
            }
        }
    }

    public LetterSortBar getLetterSortBar() {
        return letterSortBar;
    }

    public void setLetterSortBar(LetterSortBar letterSortBar) {
        removeView(this.letterSortBar);
        this.letterSortBar = letterSortBar;
        addView(letterSortBar, 1);
    }

    public int getLetterSortBarWidth() {
        return mLetterSortBarWidth;
    }

    public void setLetterSortBarWidth(int letterSortBarWidth) {
        this.mLetterSortBarWidth = letterSortBarWidth;
    }

    /**
     * 获取中间字母提示框
     *
     * @return
     */
    public TextView getLetterShow() {
        return mLetterShow;
    }

    /**
     * 设置中间字母提示框
     *
     * @param letterShow
     */
    public void setLetterShow(TextView letterShow) {
        removeView(this.mLetterShow);
        this.mLetterShow = letterShow;
        addView(letterShow, 2);
        letterShow.setVisibility(View.GONE);
    }

    /**
     * 获取提示字母显示宽度
     *
     * @return
     */
    public int getLetterShowWidth() {
        return mLetterShowWidth;
    }

    /**
     * 设置提示字母显示宽度，单位px
     *
     * @param letterShowWidth
     */
    public void setLetterShowWidth(int letterShowWidth) {
        this.mLetterShowWidth = letterShowWidth;
    }

    /**
     * 获取ListView对象
     *
     * @return
     */
    public ListView getListView() {
        return listView;
    }

    /**
     * 设置数据显示的ListView
     *
     * @param listView
     */
    public void setListView(ListView listView) {
        removeView(this.listView);
        this.listView = listView;
        addView(listView, 0);
    }

}
