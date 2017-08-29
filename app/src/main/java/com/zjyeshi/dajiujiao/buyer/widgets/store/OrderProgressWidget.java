package com.zjyeshi.dajiujiao.buyer.widgets.store;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigdog.lib.dialog.DGPromptDialog;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.entity.enums.LoginEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.OrderStatusEnum;
import com.zjyeshi.dajiujiao.buyer.receiver.order.ChangeOrderStatusReceiver;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.task.data.order.OrderDetailData;
import com.zjyeshi.dajiujiao.buyer.task.order.ModifyDellivertRemarkTask;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

/**
 * 订单流程图
 * Created by wuhk on 2016/6/29.
 */
public class OrderProgressWidget extends LinearLayout {
    private View buyerFirstCircle;
    private View buyerSecondCircle;
    private View buyerThirdCircle;
    private View buyerFirstDivider;
    private View buyerSecondDivider;
    private TextView buyerFirstTv;
    private TextView buyerSecondTv;
    private TextView buyerThirdTv;
    private RelativeLayout buyerStatusLayout;


    private View sellerFirstCircle;
    private View sellerSecondCircle;
    private View sellerExtCircle;
    private View sellerThirdCircle;
    private View sellerFirstDivider;
    private View sellerExtDivider;
    private View sellerSecondDivider;
    private TextView sellerFirstTv;
    private TextView sellerSecondTv;
    private TextView sellerExtTv;
    private TextView sellerThirdTv;
    private RelativeLayout sellerStatusLayout;


    private LinearLayout remarkLayout;
    private ImageView modifyRemarkIv;
    private TextView sendRemarkTv;
    private TextView closeTv;

    public OrderProgressWidget(Context context) {
        super(context);
        init();
    }

    public OrderProgressWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void init() {
        inflate(getContext(), R.layout.widget_order_progress, this);
        buyerFirstCircle = (View) findViewById(R.id.buyerFirstCircle);
        buyerSecondCircle = (View) findViewById(R.id.buyerSecondCircle);
        buyerThirdCircle = (View) findViewById(R.id.buyerThirdCircle);
        buyerFirstDivider = (View) findViewById(R.id.buyerFirstDivider);
        buyerSecondDivider = (View) findViewById(R.id.buyerSecondDivider);
        buyerFirstTv = (TextView) findViewById(R.id.buyerFirstTv);
        buyerSecondTv = (TextView) findViewById(R.id.buyerSecondTv);
        buyerThirdTv = (TextView) findViewById(R.id.buyerThirdTv);
        buyerStatusLayout = (RelativeLayout) findViewById(R.id.buyerStatusLayout);

        sellerFirstCircle = (View) findViewById(R.id.sellerFirstCircle);
        sellerSecondCircle = (View) findViewById(R.id.sellerSecondCircle);
        sellerExtCircle = (View) findViewById(R.id.sellerExtCircle);
        sellerThirdCircle = (View) findViewById(R.id.sellerThirdCircle);
        sellerFirstDivider = (View) findViewById(R.id.sellerFirstDivider);
        sellerExtDivider = (View) findViewById(R.id.sellerExtDivider);
        sellerSecondDivider = (View) findViewById(R.id.sellerSecondDivider);
        sellerFirstTv = (TextView) findViewById(R.id.sellerFirstTv);
        sellerSecondTv = (TextView) findViewById(R.id.sellerSecondTv);
        sellerExtTv = (TextView) findViewById(R.id.sellerExtTv);
        sellerThirdTv = (TextView) findViewById(R.id.sellerThirdTv);
        sellerStatusLayout = (RelativeLayout) findViewById(R.id.sellerStatusLayout);

        remarkLayout = (LinearLayout)findViewById(R.id.remarkLayout);
        modifyRemarkIv = (ImageView)findViewById(R.id.modifyRemarkIv);
        sendRemarkTv = (TextView) findViewById(R.id.sendRemarkTv);
        closeTv = (TextView) findViewById(R.id.closeTv);

        remarkLayout.setVisibility(GONE);
        closeTv.setVisibility(GONE);
    }

    public void bindData(String type, OrderDetailData orderDetailData) {
        OrderStatusEnum orderStatusEnum = OrderStatusEnum.valueOf(orderDetailData.getStatus());
        if (type.equals(LoginEnum.BURER.toString())) {
            buyerStatusLayout.setVisibility(VISIBLE);
            sellerStatusLayout.setVisibility(GONE);
            setBuyerStatus(orderStatusEnum);
        } else {
            buyerStatusLayout.setVisibility(GONE);
            sellerStatusLayout.setVisibility(VISIBLE);
            setSellerStatus(orderStatusEnum, orderDetailData);
        }
    }


    /**
     * 买家状态
     *
     * @param orderStatusEnum
     */
    private void setBuyerStatus(OrderStatusEnum orderStatusEnum) {

        if (orderStatusEnum.equals(OrderStatusEnum.WAITPAY)) {
            //待付款(1)
            buyerFirstCircle.setBackgroundResource(R.drawable.shape_apply_ok);
            buyerSecondCircle.setBackgroundResource(R.drawable.shape_apply_not);
            buyerThirdCircle.setBackgroundResource(R.drawable.shape_apply_not);
            buyerFirstDivider.setBackgroundResource(R.drawable.shape_apply_not);
            buyerSecondDivider.setBackgroundResource(R.drawable.shape_apply_not);
            buyerFirstTv.setTextColor(getResources().getColor(R.color.color_theme));
            buyerSecondTv.setTextColor(getResources().getColor(R.color.color_divider));
            buyerThirdTv.setTextColor(getResources().getColor(R.color.color_divider));

        } else if (orderStatusEnum.equals(OrderStatusEnum.PAYED) || orderStatusEnum.equals(OrderStatusEnum.SURED)
                || orderStatusEnum.equals(OrderStatusEnum.SENDED)) {
            //已付款(2)
            //已确认(3)
            //已发货(5)
            buyerFirstCircle.setBackgroundResource(R.drawable.shape_apply_ok);
            buyerSecondCircle.setBackgroundResource(R.drawable.shape_apply_ok);
            buyerThirdCircle.setBackgroundResource(R.drawable.shape_apply_not);
            buyerFirstDivider.setBackgroundResource(R.drawable.shape_apply_ok);
            buyerSecondDivider.setBackgroundResource(R.drawable.shape_apply_not);
            buyerFirstTv.setTextColor(getResources().getColor(R.color.color_theme));
            buyerSecondTv.setTextColor(getResources().getColor(R.color.color_theme));
            buyerThirdTv.setTextColor(getResources().getColor(R.color.color_divider));

        } else if (orderStatusEnum.equals(OrderStatusEnum.ALREADYGET) || orderStatusEnum.equals(OrderStatusEnum.ALREADYCOMMENT)) {
            //已收，未评价(7)
            //已收，已评价(8)
            buyerFirstCircle.setBackgroundResource(R.drawable.shape_apply_ok);
            buyerSecondCircle.setBackgroundResource(R.drawable.shape_apply_ok);
            buyerThirdCircle.setBackgroundResource(R.drawable.shape_apply_ok);
            buyerFirstDivider.setBackgroundResource(R.drawable.shape_apply_ok);
            buyerSecondDivider.setBackgroundResource(R.drawable.shape_apply_ok);
            buyerFirstTv.setTextColor(getResources().getColor(R.color.color_theme));
            buyerSecondTv.setTextColor(getResources().getColor(R.color.color_theme));
            buyerThirdTv.setTextColor(getResources().getColor(R.color.color_theme));

        } else if (orderStatusEnum.equals(OrderStatusEnum.CLOSED)) {
            //已关闭，已取消(9)
            buyerStatusLayout.setVisibility(GONE);
            closeTv.setVisibility(VISIBLE);
            closeTv.setText("订单已取消");
        } else if (orderStatusEnum.equals(OrderStatusEnum.WAITRETURN) || orderStatusEnum.equals(OrderStatusEnum.NEW_TOBEREBACKED)) {
            //待退货(12)
            buyerStatusLayout.setVisibility(GONE);
            closeTv.setVisibility(VISIBLE);
            closeTv.setText("订单待退货");

        } else if (orderStatusEnum.equals(OrderStatusEnum.RETURNSUCCESS) || orderStatusEnum.equals(OrderStatusEnum.NEW_REBACK_SUCCESS)) {
            //退货成功(13)
            buyerStatusLayout.setVisibility(GONE);
            closeTv.setVisibility(VISIBLE);
            closeTv.setText("退货成功");

        } else if (orderStatusEnum.equals(OrderStatusEnum.RETURNFAILED) || orderStatusEnum.equals(OrderStatusEnum.NEW_REBACK_FAIL)) {
            //退货失败(14)
            buyerStatusLayout.setVisibility(GONE);
            closeTv.setVisibility(VISIBLE);
            closeTv.setText("退货失败");
        } else if (orderStatusEnum.equals(OrderStatusEnum.NEW_REBACK_CLOSE)){
            buyerStatusLayout.setVisibility(GONE);
            closeTv.setVisibility(VISIBLE);
            closeTv.setText("退货关闭");
        }
    }

    /**
     * 卖家状态
     *
     * @param orderStatusEnum
     */
    private void setSellerStatus(OrderStatusEnum orderStatusEnum, final OrderDetailData orderDetailData) {
        final String deliverRemark = orderDetailData.getDeliveryRemark();
        boolean canEdit = orderDetailData.isModifyDeliveryRemark();

        if (orderStatusEnum.equals(OrderStatusEnum.WAITPAY)) {
            //待付款(1)
            sellerFirstCircle.setBackgroundResource(R.drawable.shape_apply_ok);
            sellerSecondCircle.setBackgroundResource(R.drawable.shape_apply_not);
            sellerExtCircle.setBackgroundResource(R.drawable.shape_apply_not);
            sellerThirdCircle.setBackgroundResource(R.drawable.shape_apply_not);
            sellerFirstDivider.setBackgroundResource(R.drawable.shape_apply_not);
            sellerExtDivider.setBackgroundResource(R.drawable.shape_apply_not);
            sellerSecondDivider.setBackgroundResource(R.drawable.shape_apply_not);
            sellerFirstTv.setTextColor(getResources().getColor(R.color.color_theme));
            sellerSecondTv.setTextColor(getResources().getColor(R.color.color_divider));
            sellerExtTv.setTextColor(getResources().getColor(R.color.color_divider));
            sellerThirdTv.setTextColor(getResources().getColor(R.color.color_divider));

        } else if (orderStatusEnum.equals(OrderStatusEnum.PAYED) || orderStatusEnum.equals(OrderStatusEnum.SURED)) {
            //已付款(2)
            //已确认(3)
            sellerFirstCircle.setBackgroundResource(R.drawable.shape_apply_ok);
            sellerSecondCircle.setBackgroundResource(R.drawable.shape_apply_ok);
            sellerExtCircle.setBackgroundResource(R.drawable.shape_apply_not);
            sellerThirdCircle.setBackgroundResource(R.drawable.shape_apply_not);
            sellerFirstDivider.setBackgroundResource(R.drawable.shape_apply_ok);
            sellerExtDivider.setBackgroundResource(R.drawable.shape_apply_not);
            sellerSecondDivider.setBackgroundResource(R.drawable.shape_apply_not);
            sellerFirstTv.setTextColor(getResources().getColor(R.color.color_theme));
            sellerSecondTv.setTextColor(getResources().getColor(R.color.color_theme));
            sellerExtTv.setTextColor(getResources().getColor(R.color.color_divider));
            sellerThirdTv.setTextColor(getResources().getColor(R.color.color_divider));

        } else if (orderStatusEnum.equals(OrderStatusEnum.SENDED)) {
            //已发货(5)
            sellerFirstCircle.setBackgroundResource(R.drawable.shape_apply_ok);
            sellerSecondCircle.setBackgroundResource(R.drawable.shape_apply_ok);
            sellerExtCircle.setBackgroundResource(R.drawable.shape_apply_ok);
            sellerThirdCircle.setBackgroundResource(R.drawable.shape_apply_not);
            sellerFirstDivider.setBackgroundResource(R.drawable.shape_apply_ok);
            sellerExtDivider.setBackgroundResource(R.drawable.shape_apply_not);
            sellerSecondDivider.setBackgroundResource(R.drawable.shape_apply_ok);
            sellerFirstTv.setTextColor(getResources().getColor(R.color.color_theme));
            sellerSecondTv.setTextColor(getResources().getColor(R.color.color_theme));
            sellerExtTv.setTextColor(getResources().getColor(R.color.color_theme));
            sellerThirdTv.setTextColor(getResources().getColor(R.color.color_divider));

            remarkLayout.setVisibility(VISIBLE);
            if (Validators.isEmpty(deliverRemark)) {
                sendRemarkTv.setText("发货备注:" + "暂无");
            } else {
                sendRemarkTv.setText("发货备注:" + deliverRemark);
            }

            if (canEdit){
                modifyRemarkIv.setVisibility(VISIBLE);
            }else{
                modifyRemarkIv.setVisibility(GONE);
            }
            modifyRemarkIv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    DGPromptDialog dialog = new DGPromptDialog.Builder(
                            getContext())
                            .setTitle("提示")
                            .setMessage("请输入请求备注")
                            .setLeftBtnText("取消")
                            .setDefaultContent(deliverRemark)
                            .setOnLeftBtnListener(new DGPromptDialog.PromptDialogListener() {
                                @Override
                                public void onClick(View view, String inputText) {

                                }
                            }).setRightBtnText("确定")
                            .setOnRightBtnListener(new DGPromptDialog.PromptDialogListener() {
                                @Override
                                public void onClick(View view, String inputText) {
                                    if (!TextUtils.isEmpty(inputText)) {
                                        modifyDeliverRemark(orderDetailData.getId() , inputText);
                                    } else {
                                        ToastUtil.toast("请输入备注信息");
                                    }
                                }
                            }).create();
                    dialog.show();
                }
            });


        } else if (orderStatusEnum.equals(OrderStatusEnum.ALREADYGET) || orderStatusEnum.equals(OrderStatusEnum.ALREADYCOMMENT)) {
            //已收，未评价(7)
            //已收，已评价(8)
            sellerFirstCircle.setBackgroundResource(R.drawable.shape_apply_ok);
            sellerSecondCircle.setBackgroundResource(R.drawable.shape_apply_ok);
            sellerExtCircle.setBackgroundResource(R.drawable.shape_apply_ok);
            sellerThirdCircle.setBackgroundResource(R.drawable.shape_apply_ok);
            sellerFirstDivider.setBackgroundResource(R.drawable.shape_apply_ok);
            sellerExtDivider.setBackgroundResource(R.drawable.shape_apply_ok);
            sellerSecondDivider.setBackgroundResource(R.drawable.shape_apply_ok);
            sellerFirstTv.setTextColor(getResources().getColor(R.color.color_theme));
            sellerSecondTv.setTextColor(getResources().getColor(R.color.color_theme));
            sellerExtTv.setTextColor(getResources().getColor(R.color.color_theme));
            sellerThirdTv.setTextColor(getResources().getColor(R.color.color_theme));

        } else if (orderStatusEnum.equals(OrderStatusEnum.CLOSED)) {
            //已关闭，已取消(9)
            sellerStatusLayout.setVisibility(GONE);
            closeTv.setVisibility(VISIBLE);
            closeTv.setText("订单已取消");
        } else if (orderStatusEnum.equals(OrderStatusEnum.WAITRETURN) || orderStatusEnum.equals(OrderStatusEnum.NEW_TOBEREBACKED)) {
            //待退货(12)
            sellerStatusLayout.setVisibility(GONE);
            closeTv.setVisibility(VISIBLE);
            closeTv.setText("订单待退货");

        } else if (orderStatusEnum.equals(OrderStatusEnum.RETURNSUCCESS) || orderStatusEnum.equals(OrderStatusEnum.NEW_REBACK_SUCCESS)) {
            //退货成功(13)
            sellerStatusLayout.setVisibility(GONE);
            closeTv.setVisibility(VISIBLE);
            closeTv.setText("退货成功");

        } else if (orderStatusEnum.equals(OrderStatusEnum.RETURNFAILED) || orderStatusEnum.equals(OrderStatusEnum.NEW_REBACK_FAIL)) {
            //退货失败(14)
            sellerStatusLayout.setVisibility(GONE);
            closeTv.setVisibility(VISIBLE);
            closeTv.setText("退货失败");
        } else if (orderStatusEnum.equals(OrderStatusEnum.NEW_REBACK_CLOSE)){
            sellerStatusLayout.setVisibility(GONE);
            closeTv.setVisibility(VISIBLE);
            closeTv.setText("退货关闭");
        }
    }


    private void modifyDeliverRemark(String orderId , String remark){
        ModifyDellivertRemarkTask task = new ModifyDellivertRemarkTask(getContext());
        task.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        task.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                ToastUtil.toast("修改成功");
                ChangeOrderStatusReceiver.notifyReceiver();
            }
        });

        task.execute(orderId , remark);
    }

}
