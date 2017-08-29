package com.zjyeshi.dajiujiao.buyer.utils;

import android.content.Context;
import android.view.View;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.task.data.order.OrderProduct;
import com.zjyeshi.dajiujiao.buyer.task.order.ModifyOrderStatusTask;
import com.zjyeshi.dajiujiao.buyer.task.order.RemoveOrderTask;
import com.zjyeshi.dajiujiao.buyer.activity.order.OrderProductListActivity;
import com.zjyeshi.dajiujiao.buyer.entity.enums.LoginEnum;
import com.zjyeshi.dajiujiao.buyer.receiver.order.ChangeOrderStatusReceiver;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.task.order.SellerSureTask;

import java.util.List;

/**
 * Created by wuhk on 2016/9/14.
 */
public abstract class OrderUtil {

    /**
     * 上传修改状态
     *
     * @param context
     * @param productList
     * @param orderId
     * @param status
     * @param gotoComment
     */
    public static void changeOrderStatus(final Context context, final List<OrderProduct> productList, final List<OrderProduct> marketList , final String orderId, String status, final boolean gotoComment) {
        SellerSureTask sellerSureTask = new SellerSureTask(context);
        sellerSureTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        sellerSureTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                ChangeOrderStatusReceiver.notifyReceiver();
                if (gotoComment) {
                    OrderProductListActivity.startActivity(context, productList,marketList,orderId);
                }
            }
        });

        sellerSureTask.execute(orderId, status);

    }

    /**
     * 上传修改状态, 确认收货直接跳转评价，其余只改变状态
     *
     * @param context
     * @param orderId
     * @param status
     */
    public static void changeOrderStatus(Context context, String orderId, String status) {
        changeOrderStatus(context, null, null,orderId, status, false);
    }

    /**
     * 修改订单状态新
     * <p/>
     * 只允许 已付款-->已确认，已确认--> 已发货
     *
     * @param context
     * @param orderId
     * @param status
     */
    public static void modifyOrderStatus(Context context, String orderId, String status) {
        modifyOrderStatusForDeliver(context, orderId, status, "");
    }

    public static void modifyOrderStatusForDeliver(Context context, String orderId, String status, String remark) {
        ModifyOrderStatusTask modifyOrderStatusTask = new ModifyOrderStatusTask(context);
        modifyOrderStatusTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        modifyOrderStatusTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                ChangeOrderStatusReceiver.notifyReceiver();
            }
        });
        modifyOrderStatusTask.execute(orderId, status, remark);
    }


    /**
     * 删除订单
     *
     * @param view
     * @param orderId
     * @param type
     */
    public static void initDeleteOrder(final View view, final String orderId, final String type) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtil.confirm(view.getContext(), "是否确定删除订单", "取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }, "确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RemoveOrderTask removeOrderTask = new RemoveOrderTask(view.getContext());
                        removeOrderTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
                            @Override
                            public void failCallback(Result<NoResultData> result) {
                                ToastUtil.toast(result.getMessage());
                            }
                        });

                        removeOrderTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
                            @Override
                            public void successCallback(Result<NoResultData> result) {
                                ToastUtil.toast("订单删除成功");
                                ChangeOrderStatusReceiver.notifyReceiver();
                            }
                        });
                        if (type.equals(LoginEnum.SELLER.toString())) {
                            removeOrderTask.execute(orderId, RemoveOrderTask.SELLERDEL);
                        } else {
                            removeOrderTask.execute(orderId, RemoveOrderTask.BUYERDEL);
                        }
                    }
                });
            }
        });
    }
}
