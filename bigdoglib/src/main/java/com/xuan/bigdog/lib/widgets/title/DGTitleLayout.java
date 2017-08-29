package com.xuan.bigdog.lib.widgets.title;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigapple.lib.utils.display.DisplayUtils;
import com.xuan.bigdog.lib.R;
import com.xuan.bigdog.lib.widgets.DGBaseLayout;

/**
 * 公共标题栏
 *
 * @author xuan
 */
public class DGTitleLayout extends DGBaseLayout {
    private View frameHead;
    private TextView returnTv;//返回按钮
    private TextView rightTv;//右边文字
    private TextView titleTv;// 中间标题
    private LinearLayout rightLayout;//右边图标布局
    private View seperator;//分割线

    private static UIConfig uiConfig;

    public DGTitleLayout(Context context) {
        super(context);
    }

    public DGTitleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void dgInit() {
        inflate(getContext(), R.layout.dg_widgets_title_titlelayout, this);
        frameHead = findViewById(R.id.frameHead);

        returnTv = (TextView) findViewById(R.id.returnTv);

        rightTv = (TextView) findViewById(R.id.rightTv);
        titleTv = (TextView) findViewById(R.id.titleTv);
        rightLayout = (LinearLayout) findViewById(R.id.rightLayout);

        seperator = findViewById(R.id.seperator);
        goneView();
        paintUI();
    }

    private void goneView() {
        returnTv.setVisibility(View.GONE);
        rightTv.setVisibility(View.GONE);
        titleTv.setVisibility(View.GONE);
        rightLayout.setVisibility(View.GONE);
    }

    /**
     * 配置中间标题
     *
     * @param title
     * @return
     */
    public DGTitleLayout configTitle(String title) {
        if (!Validators.isEmpty(title)) {
            titleTv.setText(title);
            titleTv.setVisibility(View.VISIBLE);
        } else {
            titleTv.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * 自定义图标,自定义文字
     *
     * @param returnResId
     * @param returnTxt
     * @param l
     * @return
     */
    public DGTitleLayout configReturn(int returnResId, String returnTxt, final View.OnClickListener l) {
        returnTv.setVisibility(View.VISIBLE);

        //返回图标
        if (-1 == returnResId) {
            returnTv.setCompoundDrawables(null, null, null, null);
        } else {
            Drawable drawable = getResources().getDrawable(returnResId);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            returnTv.setCompoundDrawables(drawable, null, null, null);
        }

        //返回文字
        if (Validators.isEmpty(returnTxt)) {
            returnTv.setText("");
        } else {
            returnTv.setText(returnTxt);
        }

        //返回事件
        if (null != l) {
            returnTv.setOnClickListener(l);
        }
        return this;
    }

    /**
     * 自定义图标
     *
     * @param returnResId
     * @param l
     * @return
     */
    public DGTitleLayout configReturn(int returnResId, View.OnClickListener l) {
        return configReturn(returnResId, null, l);
    }

    /**
     * 默认图标,自定义文字
     *
     * @param l
     * @return
     */
    public DGTitleLayout configReturn(String returnTxt, View.OnClickListener l) {
        return configReturn(uiConfig.DEFAULT_LEFT_ICON, returnTxt, l);
    }

    /**
     * 默认图标
     *66
     * @param l
     * @return
     */
    public DGTitleLayout configReturn(View.OnClickListener l) {
        return configReturn(uiConfig.DEFAULT_LEFT_ICON, null, l);
    }

    /**
     * 默认返回
     *
     * @param activity
     * @return
     */
    public DGTitleLayout configReturn(final Activity activity){
        return configReturn(uiConfig.DEFAULT_LEFT_ICON, "返回", new OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }

    /**
     * 配置右边文字按钮
     *
     * @param rightText
     * @param l
     */
    public DGTitleLayout configRightText(String rightText,
                                         View.OnClickListener l) {
        if (!Validators.isEmpty(rightText)) {
            rightTv.setText(rightText);
            rightTv.setVisibility(View.VISIBLE);
            if (null != l) {
                rightTv.setOnClickListener(l);
            }
        } else {
            rightTv.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * 配置右边图标
     *
     * @param resid
     * @param l
     * @return
     */
    public DGTitleLayout configRightIcon(int resid, View.OnClickListener l) {
        ImageView rightItemIv = (ImageView) LayoutInflater.from(getContext()).inflate(
                R.layout.dg_widgets_title_titlelayout_rightitem, null);

        if (-1 != resid) {
            rightItemIv.setImageResource(resid);
        } else {
            //默认的
            rightItemIv.setImageResource(R.drawable.dg_widgets_title_right_icon);
        }

        if (null != l) {
            rightItemIv.setOnClickListener(l);
        }

        //padding调整
        int left = (int)DisplayUtils.getPxByDp((Activity)getContext(), 8);
        int top = rightItemIv.getPaddingTop();
        int right = 0;
        int bottom = rightItemIv.getPaddingBottom();
        if(rightLayout.getChildCount() < 1){
            right = (int)DisplayUtils.getPxByDp((Activity)getContext(), 8);
        }else{
            right = (int)DisplayUtils.getPxByDp((Activity)getContext(), 8);
        }
        rightItemIv.setPadding(left, top, right, bottom);

        return configRightView(rightItemIv);
    }

    /**
     * 默认图标
     *
     * @param l
     * @return
     */
    public DGTitleLayout configRightIcon(View.OnClickListener l) {
        return configRightIcon(-1, l);
    }

    /**
     * 标题右边添加布局
     *
     * @param view
     * @return
     */
    public DGTitleLayout configRightView(View view) {
        rightLayout.setVisibility(View.VISIBLE);
        int widthAndHeight = (int) getResources().getDimension(
                R.dimen.dg_widgets_title_bg_height);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                widthAndHeight);
        rightLayout.addView(view, lp);
        return this;
    }

    public TextView getRightTv() {
        return rightTv;
    }

    public TextView getReturnTv(){
        return returnTv;
    }

    public TextView getTitleTv() {
        return titleTv;
    }

    public View getRightLayout() {
        return rightLayout;
    }

    private void paintUI() {
        if(null == uiConfig){
            uiConfig = new UIConfig();
        }

        //左边文字颜色
        returnTv.setTextColor(getResources().getColorStateList(uiConfig.DEFAULT_LEFT_TEXTCOLOR));
        //背景颜色
        frameHead.setBackgroundColor(getResources().getColor(uiConfig.DEFAULT_BG_COLOR));
        //标题文字颜色
        titleTv.setTextColor(getResources().getColor(uiConfig.DEFAULT_TITLE_TEXTCOLOR));
        //标题文字大小
        titleTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, uiConfig.DEFAULT_TITLE_TEXTSIZE);
        //分割线颜色
        if (-1 != uiConfig.DEFAULT_SEPERATOR_COLOR) {
            seperator.setBackgroundColor(getResources().getColor(uiConfig.DEFAULT_SEPERATOR_COLOR));
        }
        //右边文字颜色
        rightTv.setTextColor(getResources().getColorStateList(uiConfig.DEFAULT_RIGHT_TEXTCOLOR));
    }

    public static UIConfig getUiConfig() {
        return uiConfig;
    }

    public static void setUiConfig(UIConfig uiConfig) {
        DGTitleLayout.uiConfig = uiConfig;
    }

    public static class UIConfig {
        //返回图标
        public int DEFAULT_LEFT_ICON = R.drawable.dg_widgets_title_left_icon;
        //返回文字颜色
        public int DEFAULT_LEFT_TEXTCOLOR = R.color.dg_widgets_title_left_textcolor;
        //标题背景
        public int DEFAULT_BG_COLOR = R.color.dg_color_theme;
        //标题文字颜色
        public int DEFAULT_TITLE_TEXTCOLOR = R.color.dg_color_white;
        //标题文字大小
        public int DEFAULT_TITLE_TEXTSIZE = 20;
        //标题分割线
        public int DEFAULT_SEPERATOR_COLOR = -1;
        //右边文字颜色
        public int DEFAULT_RIGHT_TEXTCOLOR = R.color.dg_widgets_title_right_textcolor;
    }

}
