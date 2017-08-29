package com.jopool.crow.imkit.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jopool.crow.R;

import java.util.List;

/**
 * Created by wuhk on 2016/4/13.
 */
public class CWEmptyView extends RelativeLayout {
    private ImageView iconIv;
    private TextView messageTv;

    public CWEmptyView(Context context) {
        super(context);
        init();
    }

    public CWEmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.cw_empty_layout, this);
        iconIv = (ImageView) findViewById(R.id.iconIv);
        messageTv = (TextView) findViewById(R.id.messageTv);
    }

    /**
     * 提示图片
     *
     * @param resid
     * @return
     */
    public CWEmptyView configImage(int resid) {
        iconIv.setImageResource(resid);
        return this;
    }

    /**
     * 提示消息
     *
     * @param message
     * @return
     */
    public CWEmptyView configMessage(String message) {
        messageTv.setText(message);
        return this;
    }

    /**
     * 显示
     */
    public void show() {
        setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏
     */
    public void hide() {
        setVisibility(View.GONE);
    }

    /**
     * list空就显示，非空就隐藏
     *
     * @param list
     */
    public void showIfEmpty(List<?> list) {
        if (null == list || list.isEmpty()) {
            show();
        } else {
            hide();
        }
    }

}
