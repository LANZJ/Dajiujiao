package com.jopool.crow.imkit.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.jopool.crow.R;

/**
 * 人员选择view
 * Created by wuhk on 2016/6/29.
 */
public class CWGroupUserSelectView extends ImageView {
    private boolean isChecked;
    private OnClickListener externalOnClickListener;

    public CWGroupUserSelectView(Context context) {
        super(context);
        init();
    }

    public CWGroupUserSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CWGroupUserSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isChecked) {
                    setImageResource(R.drawable.cw_group_user_select_nor);
                } else {
                    setImageResource(R.drawable.cw_group_user_select_sel);
                }
                isChecked = !isChecked;

                if (null != externalOnClickListener) {
                    externalOnClickListener.onClick(view);
                }
            }
        });
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        if (checked) {
            setImageResource(R.drawable.cw_group_user_select_sel);
        } else {
            setImageResource(R.drawable.cw_group_user_select_nor);
        }

        this.isChecked = checked;
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        this.externalOnClickListener = l;
    }

    public void toggle() {
        setChecked(!isChecked);
    }
}
