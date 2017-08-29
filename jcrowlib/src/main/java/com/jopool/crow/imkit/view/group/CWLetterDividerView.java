package com.jopool.crow.imkit.view.group;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.jopool.crow.R;
import com.jopool.crow.imkit.utils.lettersort.entity.ItemDivide;
import com.jopool.crow.imkit.view.CWBaseLayout;

/**
 * 字母排序分割线
 * <p/>
 * Created by wuhk on 2016/11/7.
 */
public class CWLetterDividerView extends CWBaseLayout {
    private TextView letterTv;

    public CWLetterDividerView(Context context) {
        super(context);
    }

    public CWLetterDividerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onViewInit() {
        inflate(getContext(), R.layout.cw_group_view_letter_divider, this);
        letterTv = (TextView) findViewById(R.id.letterTv);
    }

    /**
     * 设置字母
     *
     * @param itemDivide
     */
    public void bindData(ItemDivide itemDivide) {
        letterTv.setText(itemDivide.getLetter());
    }
}
