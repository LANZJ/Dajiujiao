package com.jopool.crow.imkit.view.group;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jopool.crow.R;
import com.jopool.crow.imkit.listeners.CWSelectedUserSearchListener;
import com.jopool.crow.imkit.receiver.CWGroupSelectUserClickReceiver;
import com.jopool.crow.imkit.utils.ImageShowUtil;
import com.jopool.crow.imkit.view.CWBaseLayout;
import com.jopool.crow.imkit.view.popview.CWWebViewOpLayoutView;
import com.jopool.crow.imlib.entity.CWSelectUser;
import com.jopool.crow.imlib.utils.CWLogUtil;

import java.util.List;

/**
 * 显示选择人员View
 * Created by wuhk on 2016/11/7.
 */
public class CWSelectedUserShowView extends CWBaseLayout {
    private LinearLayout userLayout;
    private CWLimitedWidthHorizontalScrollView scrollView;
    private EditText searchEt;
    private CWSelectedUserSearchListener selectedUserSearchListener;

    public CWSelectedUserShowView(Context context) {
        super(context);
    }

    public CWSelectedUserShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onViewInit() {
        inflate(getContext(), R.layout.cw_group_view_selected_user_show, this);
        userLayout = (LinearLayout) findViewById(R.id.userLayout);
        scrollView = (CWLimitedWidthHorizontalScrollView) findViewById(R.id.scrollView);
        searchEt = (EditText) findViewById(R.id.searchEt);

        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                selectedUserSearchListener.doSearch(searchEt.getText().toString());
            }
        });
    }

    /**
     * 刷新列表
     *
     * @param userShowAdapter
     */
    public void refreshData(UserShowAdapter userShowAdapter) {
        userLayout.removeAllViews();
        for (int i = 0; i < userShowAdapter.getItmeCount(); i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.cw_group_user_photo, null);
            ImageView avatarIv = (ImageView) view.findViewById(R.id.avatarIv);

            CWSelectUser user = userShowAdapter.getItem(i, view);
            ImageShowUtil.showHeadIcon(avatarIv, user.getUserLogo());

            userLayout.addView(view);
            scrollView.fullScroll(FOCUS_RIGHT);
        }

    }

    /**
     * 设置搜索监听
     *
     * @param searchListener
     */
    public void setSearchListener(CWSelectedUserSearchListener searchListener) {
        this.selectedUserSearchListener = searchListener;

    }


    /**
     * 选择人员适配器
     */
    public interface UserShowAdapter {
        int getItmeCount();

        CWSelectUser getItem(int position, View view);
    }
}
