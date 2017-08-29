package com.zjyeshi.dajiujiao.buyer.views.other;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.zjyeshi.dajiujiao.R;

/**
 * Created by wuhk on 2016/6/29.
 */
public class IVWalletCheck extends ImageView {
    private boolean isChecked;
    private OnClickListener externalOnClickListener;

    public IVWalletCheck(Context context) {
        super(context);
        init();
    }

    public IVWalletCheck(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IVWalletCheck(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isChecked) {
                    setImageResource(R.drawable.wallet_no);
                } else {
                    setImageResource(R.drawable.wallet_sel);
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
            setImageResource(R.drawable.wallet_sel);
        } else {
            setImageResource(R.drawable.wallet_no);
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
