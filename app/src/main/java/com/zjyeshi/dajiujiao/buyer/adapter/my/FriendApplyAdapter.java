package com.zjyeshi.dajiujiao.buyer.adapter.my;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.receiver.UpdateFriendReceiver;
import com.zjyeshi.dajiujiao.buyer.task.my.FriendsReviewTask;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.task.data.my.FriendApplyListData;

import java.util.List;

/**
 * Created by wuhk on 2016/8/16.
 */
public class FriendApplyAdapter extends MBaseAdapter {
    private Context context;
    private List<FriendApplyListData.FriendApply> dataList;

    public FriendApplyAdapter(Context context, List<FriendApplyListData.FriendApply> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (null == view){
            view = LayoutInflater.from(context).inflate(R.layout.listitem_friend , null);
        }

        ImageView avatarIv = (ImageView)view.findViewById(R.id.avatarIv);
        TextView nameTv = (TextView)view.findViewById(R.id.nameTv);
        TextView remarkTv = (TextView)view.findViewById(R.id.remarkTv);
        TextView waitAdoptTv = (TextView)view.findViewById(R.id.waitAdoptTv);
        LinearLayout opLayout = (LinearLayout)view.findViewById(R.id.opLayout);
        ImageView adoptIv = (ImageView) view.findViewById(R.id.adoptIv);
        ImageView refuseIv = (ImageView) view.findViewById(R.id.refuseIv);
        View topDivider = (View)view.findViewById(R.id.topDivider);


        final FriendApplyListData.FriendApply friendApply = dataList.get(position);

        if (position == 0){
            topDivider.setVisibility(View.VISIBLE);
        }else{
            topDivider.setVisibility(View.GONE);
        }
//        initImageView(avatarIv , friendApply.getAvatar() , R.drawable.default_tx);
        GlideImageUtil.glidImage(avatarIv , friendApply.getAvatar() , R.drawable.default_tx);
        initTextView(nameTv , friendApply.getName());

        if (friendApply.isApplicant()){
            opLayout.setVisibility(View.VISIBLE);
            remarkTv.setVisibility(View.VISIBLE);
            waitAdoptTv.setVisibility(View.GONE);
        }else{
            opLayout.setVisibility(View.GONE);
            remarkTv.setVisibility(View.GONE);
            waitAdoptTv.setVisibility(View.VISIBLE);
        }
        initTextView(remarkTv , friendApply.getRemark());

        //接受
        adoptIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opRequest(friendApply , true);
            }
        });
        //拒绝
        refuseIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opRequest(friendApply  , false);
            }
        });

        return view;
    }

    /**操作好友请求
     *
     */
    private void opRequest(final FriendApplyListData.FriendApply apply, boolean isAdopt){
        FriendsReviewTask reviewTask = new FriendsReviewTask(context);
        reviewTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        reviewTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                dataList.remove(apply);
                notifyDataSetChanged();
                UpdateFriendReceiver.notifyReceiver();
            }
        });

        reviewTask.execute(apply.getId() , String.valueOf(isAdopt));
    }
}
