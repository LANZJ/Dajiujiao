package com.zjyeshi.dajiujiao.buyer.adapter.my;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigdog.lib.dialog.DGSingleSelectDialog;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.dao.DaoFactory;
import com.zjyeshi.dajiujiao.buyer.receiver.UpdateFriendReceiver;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.task.data.my.FriendListData;
import com.zjyeshi.dajiujiao.buyer.task.my.RemoveFriendTask;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.List;

import io.rong.imkit.RongIM;

/**
 * Created by wuhk on 2016/8/16.
 */
public class FriendAdapter extends MBaseAdapter {
    private Context context;
    private List<FriendListData.Friend> dataList;
    private boolean isSelect;

    public FriendAdapter(Context context, List<FriendListData.Friend> dataList , boolean isSelect) {
        this.context = context;
        this.dataList = dataList;
        this.isSelect = isSelect;
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
        View topDivider = (View)view.findViewById(R.id.topDivider);

        final FriendListData.Friend friend = dataList.get(position);

        if (position == 0){
            topDivider.setVisibility(View.VISIBLE);
        }else{
            topDivider.setVisibility(View.GONE);
        }

//        initImageView(avatarIv , friend.getAvatar() , R.drawable.default_tx);
        GlideImageUtil.glidImage(avatarIv , friend.getAvatar() , R.drawable.default_tx);
        initTextView(nameTv , friend.getName());
        if (!isSelect){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  //  ChatManager.getInstance().startConversion(context , friend.getMemberId());
                    RongIM.getInstance().startPrivateChat(context, friend.getMemberId(), friend.getName());
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    DGSingleSelectDialog dialog =
                            new DGSingleSelectDialog.Builder(context).setItemTextAndOnClickListener(new String[]{"删除好友"}
                                    , new View.OnClickListener[]{new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            removeFriend(friend);
                                        }
                                    }}).create();
                    ;

                    dialog.show();
                    return true;
                }
            });
        }
        return view;
    }

    private void removeFriend(final FriendListData.Friend friend){
        RemoveFriendTask removeFriendTask = new RemoveFriendTask(context);
        removeFriendTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        removeFriendTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                dataList.remove(friend);
                DaoFactory.getContactDao().deleteById(friend.getMemberId() , friend.getId());
                notifyDataSetChanged();
                UpdateFriendReceiver.notifyReceiver();
            }
        });

        removeFriendTask.execute(friend.getId() , "");
    }
}
