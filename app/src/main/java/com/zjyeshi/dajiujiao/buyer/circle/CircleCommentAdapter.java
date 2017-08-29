package com.zjyeshi.dajiujiao.buyer.circle;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.circle.itementity.CircleContentEntity;
import com.zjyeshi.dajiujiao.buyer.circle.receiver.OnlyNotifyReceiver;
import com.zjyeshi.dajiujiao.buyer.circle.receiver.SetListViewReceiver;
import com.zjyeshi.dajiujiao.buyer.circle.task.DeleteEvaluateTask;
import com.zjyeshi.dajiujiao.buyer.circle.task.data.CircleData;
import com.zjyeshi.dajiujiao.buyer.circle.task.data.Evaluate;
import com.zjyeshi.dajiujiao.buyer.dao.DaoFactory;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.common.PassConstans;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.utils.SingleSelectDialogUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * 酒友圈评论列表适配器
 * Created by wuhk on 2015/11/17.
 */
public class CircleCommentAdapter extends MBaseAdapter {
    private Context context;
    private List<Evaluate> dataList = new ArrayList<Evaluate>();
    private CircleContentEntity contentEntity;
    private String pos;

    public CircleCommentAdapter(Context context, List<Evaluate> dataList, CircleContentEntity contentEntity, String pos) {
        this.context = context;
        this.dataList = dataList;
        this.contentEntity = contentEntity;
        this.pos = pos;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        if (null == view) {
            view = LayoutInflater.from(context).inflate(R.layout.listitem_circle_comment, null);
        }
        final Evaluate evaluate = dataList.get(position);

        TextView commentNameTv = (TextView) view.findViewById(R.id.commentNameTv);
        LinearLayout replyLayout = (LinearLayout) view.findViewById(R.id.replyLayout);
        TextView replyNameTv = (TextView) view.findViewById(R.id.replyNameTv);
        final TextView contentTv = (TextView) view.findViewById(R.id.contentTv);
        //显示评论人和被评论人的名字
        if (null == evaluate.getMemberup()) {
            replyLayout.setVisibility(View.GONE);
            initTextView(commentNameTv, evaluate.getMember().getName());
        } else {
            replyLayout.setVisibility(View.VISIBLE);
            initTextView(commentNameTv, evaluate.getMember().getName());
            initTextView(replyNameTv, evaluate.getMemberup().getName());
        }
        initTextView(contentTv, evaluate.getContent());
        //点击事件,是自己的显示删除，是别人就评论
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (evaluate.getMember().getId().equals(LoginedUser.getLoginedUser().getId())){
                    List<String> itemList = new ArrayList<String>();
                    List<View.OnClickListener> onClickListenerList = new ArrayList<View.OnClickListener>();
                    itemList.add("删除评价");
                    onClickListenerList.add(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteEvaluate(contentEntity ,evaluate.getId() , position);
                        }
                    });

                    SingleSelectDialogUtil dialog = new SingleSelectDialogUtil.Builder(context)
                            .setItemTextAndOnClickListener(
                                    itemList.toArray(new String[itemList.size()]),
                                    onClickListenerList
                                            .toArray(new View.OnClickListener[onClickListenerList
                                                    .size()])).createInstance();
                    dialog.show();
                }else{
                    SetListViewReceiver.notifyReceiver(String.valueOf(Integer.parseInt(pos) + 2));
                    Intent intent = new Intent();
                    intent.putExtra(PassConstans.EVALUATE, evaluate);
                    intent.putExtra(PassConstans.POSITION, pos);
                    intent.putExtra(PassConstans.CONTENTENTITY, contentEntity);
                    intent.putExtra(PassConstans.ISINCOMMENT, "incomment");
                    intent.setClass(context, CommentActivity.class);
                    context.startActivity(intent);
                }
            }
        });
        return view;
    }

    private void deleteEvaluate(final CircleContentEntity contentEntity ,final String id , final int position){
        DeleteEvaluateTask deleteEvaluateTask = new DeleteEvaluateTask(context);

        deleteEvaluateTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        deleteEvaluateTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                ToastUtil.toast("删除成功");
                dataList.remove(position);
                notifyDataSetChanged();
                CircleData.Circle circle = DaoFactory.getCircleDao().findById(contentEntity.getId());
                CircleData.Circle userCircle = DaoFactory.getUserCircleDao().findById(contentEntity.getId() , contentEntity.getMember().getId());
                circle.getEvaluates().remove(position);
                DaoFactory.getCircleDao().replace(circle);
                if (null != userCircle){
                    userCircle.getEvaluates().remove(position);
                    DaoFactory.getUserCircleDao().replace(userCircle);
                }
                OnlyNotifyReceiver.notifyReceiver();

            }
        });
        deleteEvaluateTask.execute(id);
    }
}
