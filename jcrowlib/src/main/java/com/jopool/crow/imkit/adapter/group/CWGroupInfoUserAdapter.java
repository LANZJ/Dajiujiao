package com.jopool.crow.imkit.adapter.group;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jopool.crow.CWChat;
import com.jopool.crow.R;
import com.jopool.crow.imkit.adapter.CWBaseAdapter;
import com.jopool.crow.imkit.utils.ImageShowUtil;
import com.jopool.crow.imkit.view.CWSquareImageView;
import com.jopool.crow.imlib.entity.CWSelectUser;
import com.jopool.crow.imlib.entity.CWUser;

import java.util.List;

/**
 * Created by wuhk on 2016/11/3.
 */
public class CWGroupInfoUserAdapter extends CWBaseAdapter {
    private Context context;
    private List<CWSelectUser> dataList;
    private String groupId;
    private boolean isCreator;


    public CWGroupInfoUserAdapter(Context context, List<CWSelectUser> dataList, String groupId) {
        this.context = context;
        this.dataList = dataList;
        this.groupId = groupId;
    }

    @Override
    public int getCount() {
        if (isCreator) {
            return dataList.size() + 2;
        } else {
            return dataList.size() + 1;
        }

    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (null == view) {
            view = LayoutInflater.from(context).inflate(R.layout.cw_group_griditem_user, null);
        }

        CWSquareImageView avatarIv = (CWSquareImageView) view.findViewById(R.id.avatarIv);
        TextView nameTv = (TextView) view.findViewById(R.id.nameTv);

        if (position == dataList.size()) {
            avatarIv.setImageResource(R.drawable.cw_group_add_member);
            nameTv.setVisibility(View.INVISIBLE);

            avatarIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
             //       CWChat.getInstance().getGroupDelegate().startSelectWhenAdd(context, groupId, dataList);
                }
            });
        } else if (position == (dataList.size() + 1)) {
            avatarIv.setImageResource(R.drawable.cw_group_remove_user);
            nameTv.setVisibility(View.INVISIBLE);

            avatarIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 //   CWChat.getInstance().getGroupDelegate().startSelectWhenRemove(context, groupId, dataList);
                }
            });
        } else {
            final CWSelectUser selectUser = dataList.get(position);
            if (null != CWChat.getInstance().getGetUserInfoProvider()) {
                CWUser user = CWChat.getInstance().getGetUserInfoProvider().getUserById(selectUser.getUserId());
                selectUser.transFromCWUser(user);
            }

            ImageShowUtil.showHeadIcon(avatarIv, selectUser.getUserLogo());
            initTextView(nameTv, selectUser.getUserName());

            avatarIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CWChat.getInstance().getGroupDelegate().getOnGroupUserClickListener().onGroupUserClick(context , selectUser.getUserId());
                }
            });
        }
        return view;
    }

    public void notifyDataSetChanged(boolean isCreator) {
        this.isCreator = isCreator;
        notifyDataSetChanged();
    }
}
