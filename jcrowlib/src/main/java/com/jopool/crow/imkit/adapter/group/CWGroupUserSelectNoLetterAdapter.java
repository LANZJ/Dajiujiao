package com.jopool.crow.imkit.adapter.group;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jopool.crow.R;
import com.jopool.crow.imkit.adapter.CWBaseAdapter;
import com.jopool.crow.imkit.utils.ImageShowUtil;
import com.jopool.crow.imkit.view.CWGroupUserSelectView;
import com.jopool.crow.imlib.entity.CWSelectUser;

import java.util.List;

/**
 * Created by wuhk on 2016/11/3.
 */
public class CWGroupUserSelectNoLetterAdapter extends CWBaseAdapter {
    private Context context;
    private List<CWSelectUser> dataList;

    public CWGroupUserSelectNoLetterAdapter(Context context, List<CWSelectUser> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (null == view) {
            view = LayoutInflater.from(context).inflate(R.layout.cw_group_view_letter_content, null);
        }
        ImageView userPhotoIv = (ImageView) view.findViewById(R.id.userPhotoIv);
        TextView userNameTv = (TextView) view.findViewById(R.id.userNameTv);
        final CWGroupUserSelectView userSelectView = (CWGroupUserSelectView) view.findViewById(R.id.userSelectView);

        final CWSelectUser user = dataList.get(position);

        ImageShowUtil.showHeadIcon(userPhotoIv, user.getUserLogo());
        initTextView(userNameTv, user.getUserName());
        //是否已经是群成员了，是的话就不进行选择操作了
        if (!user.isGroupMember()) {
            userSelectView.setEnabled(true);
            view.setEnabled(true);
            userSelectView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (userSelectView.isChecked()) {
                        user.setSelected(true);
                    } else {
                        user.setSelected(false);
                    }
                }
            });
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userSelectView.performClick();
                }
            });
        } else {

//            userSelectView.setEnabled(false);
//            view.setEnabled(false);
            userSelectView.setVisibility(View.GONE);
        }

        return view;
    }


}
