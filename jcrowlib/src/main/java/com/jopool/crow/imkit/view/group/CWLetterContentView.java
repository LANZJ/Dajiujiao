package com.jopool.crow.imkit.view.group;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jopool.crow.R;
import com.jopool.crow.imkit.receiver.CWGroupSelectUserClickReceiver;
import com.jopool.crow.imkit.utils.ImageShowUtil;
import com.jopool.crow.imkit.utils.lettersort.entity.ItemContent;
import com.jopool.crow.imkit.view.CWBaseLayout;
import com.jopool.crow.imkit.view.CWGroupUserSelectView;
import com.jopool.crow.imlib.entity.CWSelectUser;

/**
 * Created by wuhk on 2016/11/7.
 */
public class CWLetterContentView extends CWBaseLayout {
    private ImageView userPhotoIv;
    private TextView userNameTv;
    private CWGroupUserSelectView userSelectView;


    public CWLetterContentView(Context context) {
        super(context);
    }

    public CWLetterContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onViewInit() {
        inflate(getContext(), R.layout.cw_group_view_letter_content, this);

        userPhotoIv = (ImageView) findViewById(R.id.userPhotoIv);
        userNameTv = (TextView) findViewById(R.id.userNameTv);
        userSelectView = (CWGroupUserSelectView) findViewById(R.id.userSelectView);
    }

    /**
     * 绑定数据
     *
     * @param itemContent
     */
    public void bindData(ItemContent itemContent) {
        final CWSelectUser user = (CWSelectUser) itemContent.getValue();

        ImageShowUtil.showHeadIcon(userPhotoIv, user.getUserLogo());
        userNameTv.setText(user.getUserName());
        if (user.isSelected()){
            userSelectView.setChecked(true);
        }else{
            userSelectView.setChecked(false);
        }
        //是否已经是群成员了，是的话就不进行选择操作了
        if (!user.isGroupMember()) {
            userSelectView.setEnabled(true);
            userSelectView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (userSelectView.isChecked()) {
                        user.setSelected(true);
                    } else {
                        user.setSelected(false);
                    }
                    CWGroupSelectUserClickReceiver.notifyReceiver(getContext());
                }
            });
            this.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userSelectView.performClick();
                }
            });
        } else {
            userSelectView.setImageResource(R.drawable.cw_group_user_can_not_sel);
            userSelectView.setEnabled(false);
        }

    }
}
