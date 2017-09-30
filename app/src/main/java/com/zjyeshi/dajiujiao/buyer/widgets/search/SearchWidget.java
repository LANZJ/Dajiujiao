package com.zjyeshi.dajiujiao.buyer.widgets.search;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.ioc.ViewUtils;
import com.xuan.bigapple.lib.utils.ContextUtils;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.circle.itemview.BaseView;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.buyer.widgets.search.callback.SearchCallback;

/**
 * 搜索框
 * Created by wuhk on 2016/9/18.
 */
public class SearchWidget extends BaseView {
    @InjectView(R.id.searchEt)
    private EditText searchEt;
    @InjectView(R.id.hintLayout)
    private RelativeLayout hintLayout;
    @InjectView(R.id.desTv)
    private TextView desTv;
    @InjectView(R.id.searchLayout)
    private RelativeLayout searchLayout;
    private SearchCallback searchCallback;
    public SearchWidget(Context context) {
        super(context);
    }

    public SearchWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void dgInit() {
        inflate(getContext(), R.layout.widget_search, this);
        ViewUtils.inject(this, this);

        searchEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    hintLayout.setVisibility(View.GONE);
                    searchLayout.setVisibility(VISIBLE);
                }else {
                    hintLayout.setVisibility(View.VISIBLE);
                    searchLayout.setVisibility(GONE);
                }
            }
        });

        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 0){
                    hintLayout.setVisibility(VISIBLE);
                    searchLayout.setVisibility(GONE);
                    searchCallback.search(s.toString());
                    ContextUtils.showSoftInput(searchEt , false);
                }else{
                    hintLayout.setVisibility(GONE);
                    searchLayout.setVisibility(VISIBLE);
                }
            }
        });
        searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if ((i == 0 || i == 3) && keyEvent != null) {
                    searchCallback.search(searchEt.getText().toString());
                    ContextUtils.showSoftInput(searchEt , false);
                        return true;
                }
                return false;
            }
        });

        searchLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                searchCallback.search(searchEt.getText().toString());
                ContextUtils.showSoftInput(searchEt , false);
            }
        });
    }

    public void configSearchCallback(SearchCallback searchCallback){
        this.searchCallback = searchCallback;
    }

    public void sethintDes(String des){
        desTv.setText(des);
    }
}
