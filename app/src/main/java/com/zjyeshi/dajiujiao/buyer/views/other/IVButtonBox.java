package com.zjyeshi.dajiujiao.buyer.views.other;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.zjyeshi.dajiujiao.R;

/**
 * 开关ImageView
 * Created by wuhk on 2015/10/19.
 */
public class IVButtonBox extends ImageView {
    private boolean isChecked;
    private OnClickListener externalOnClickListener;
    public IVButtonBox(Context context) {
        super(context);
        init();
    }

    public IVButtonBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IVButtonBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isChecked) {
                    setImageResource(R.drawable.close);
                } else {
                    setImageResource(R.drawable.open);
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
            setImageResource(R.drawable.open);
        } else {
            setImageResource(R.drawable.close);
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
