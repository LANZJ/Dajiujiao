package com.zjyeshi.dajiujiao.buyer.utils;

import android.content.Context;
import android.view.View;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigdog.lib.dialog.DGSingleSelectDialog;
import com.zjyeshi.dajiujiao.buyer.circle.itemview.CircleCollectView;
import com.zjyeshi.dajiujiao.buyer.circle.task.CancelCollectionTask;
import com.zjyeshi.dajiujiao.buyer.circle.task.CircleAddTask;
import com.zjyeshi.dajiujiao.buyer.circle.task.CircleCollectTask;
import com.zjyeshi.dajiujiao.buyer.circle.task.data.CircleCollectionData;
import com.zjyeshi.dajiujiao.buyer.entity.CircleCollect;
import com.zjyeshi.dajiujiao.buyer.circle.receiver.OnlyRefrshReceiver;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.circle.task.data.CircleAddData;

/**
 * 圈子工具类
 * Created by wuhk on 2016/8/15.
 */
public abstract class CircleUtil {

    /**收藏
     *
     * @param view
     * @param collect
     */
    public static void circleCollect(final View view , final CircleCollect collect){
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DGSingleSelectDialog dialog =
                        new DGSingleSelectDialog.Builder(view.getContext()).setItemTextAndOnClickListener(new String[]{"收藏"}
                                , new View.OnClickListener[]{new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        doCollect(view.getContext(), collect);
                                    }
                                }}).create();

                dialog.show();
                return true;
            }
        });
    }

    public static void circleCollectOnChat(final View view , final CircleCollect collect){
        doCollect(view.getContext(), collect);
    }

    /**取消收藏
     *
     * @param view
     * @param collection
     * @param cancelCallback
     */
    public static void cancelCollect(final  View view , final CircleCollectionData.Collection collection , final CircleCollectView.CancelCallback cancelCallback ){
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DGSingleSelectDialog dialog =
                        new DGSingleSelectDialog.Builder(view.getContext()).setItemTextAndOnClickListener(new String[]{"取消收藏"}
                                , new View.OnClickListener[]{new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        doCancel(view.getContext() , collection , cancelCallback );
                                    }
                                }}).create();

                dialog.show();
                return true;
            }
        });
    }

    /**获取收藏数据
     *
     * @param fromMemberId
     * @param pic
     * @param content
     * @param link_type
     * @param link_pic
     * @param link_title
     * @param link_content
     * @return
     */
    public static CircleCollect getCollect(String fromMemberId , String pic , String content , int link_type
            ,String link_pic, String link_title , String link_content){
        CircleCollect collect = new CircleCollect();
        collect.setFromMemberId(fromMemberId);
        collect.setPic(pic);
        collect.setContent(content);
        collect.setLink_type(link_type);
        collect.setLink_pic(link_pic);
        collect.setLink_title(link_title);
        collect.setLink_content(link_content);

        return collect;
    }

    /**图片收藏数据
     *
     * @param fromMemberId
     * @param pic
     * @return
     */
    public static CircleCollect getCollectWithPic(String fromMemberId , String pic){
        return getCollect(fromMemberId , pic , "" , 1 , "" , "" , "");
    }

    /**文字收藏数据
     *
     * @param fromMemberId
     * @param content
     * @return
     */
    public static CircleCollect getCollectWithContent(String fromMemberId , String content){
        return getCollect(fromMemberId , "" , content , 1 , "" , "" , "");
    }

    /**连接收藏数据
     *
     * @param fromMemberId
     * @param link_type
     * @param link_pic
     * @param link_title
     * @param link_content
     * @return
     */
    public static CircleCollect getCollectWithLink(String fromMemberId , int link_type , String link_pic
            ,String link_title, String link_content){
        return getCollect(fromMemberId , "" , "" , link_type , link_pic , link_title , link_content);
    }

    /** 收藏
     *
     * @param context
     * @param collect
     */
    public static void doCollect(Context context , CircleCollect collect){
        CircleCollectTask collectTask = new CircleCollectTask(context);
        collectTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        collectTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                ToastUtil.toast("收藏成功");
            }
        });

        collectTask.execute(collect);
    }

    /**执行取消收藏操作
     *
     * @param context
     * @param collection
     * @param cancelCallback
     */
    private static void doCancel(Context  context , CircleCollectionData.Collection  collection , final CircleCollectView.CancelCallback cancelCallback){
        CancelCollectionTask cancelCollectionTask = new CancelCollectionTask(context);
        cancelCollectionTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        cancelCollectionTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                cancelCallback.callback();
            }
        });
        cancelCollectionTask.execute(collection.getId());
    }

    /**朋友圈中增加链接
     *
     * @param context
     * @param linkType
     * @param linkPic
     * @param linkTitle
     * @param linkContent
     */
    public static void circleAddUrl(final Context context , int linkType , String linkPic
            , String linkTitle , String linkContent){
        CircleAddTask circleAddTask = new CircleAddTask(context);
        circleAddTask.setShowProgressDialog(true);
        circleAddTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<CircleAddData>() {
            @Override
            public void successCallback(Result<CircleAddData> result) {
                LogUtil.e(result.getMessage());
            }
        });
        circleAddTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<CircleAddData>() {
            @Override
            public void successCallback(Result<CircleAddData> result) {
                OnlyRefrshReceiver.notifyReceiver();
                ToastUtil.toast("转发成功");
            }
        });
        circleAddTask.execute("", "" , String.valueOf(linkType) , linkPic , linkTitle , linkContent);
    }
}
