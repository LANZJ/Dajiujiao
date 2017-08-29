package com.zjyeshi.dajiujiao.buyer.views.order;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.ioc.ViewUtils;
import com.xuan.bigapple.lib.utils.DateUtils;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigappleui.lib.view.listview.BUHighHeightListView;
import com.xuan.bigdog.lib.dialog.DGPromptDialog;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.my.MaxLevelManActivity;
import com.zjyeshi.dajiujiao.buyer.activity.order.OrderDetailEditActivity;
import com.zjyeshi.dajiujiao.buyer.activity.order.OrderDetailNewActivity;
import com.zjyeshi.dajiujiao.buyer.activity.order.OrderProductListActivity;
import com.zjyeshi.dajiujiao.buyer.activity.sales.ApplyRebackActivity;
import com.zjyeshi.dajiujiao.buyer.activity.store.BalanceAccountsActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.order.OrderGoodsListAdapter;
import com.zjyeshi.dajiujiao.buyer.circle.itemview.BaseView;
import com.zjyeshi.dajiujiao.buyer.entity.GoodsCar;
import com.zjyeshi.dajiujiao.buyer.entity.enums.GoodTypeEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.LoginEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.OrderStatusEnum;
import com.zjyeshi.dajiujiao.buyer.receiver.order.ChangeOrderStatusReceiver;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.task.data.order.OrderProduct;
import com.zjyeshi.dajiujiao.buyer.task.data.order.PerOrder;
import com.zjyeshi.dajiujiao.buyer.task.order.DrawBackOrderReviewTask;
import com.zjyeshi.dajiujiao.buyer.task.order.RemoverOrderMistakeTask;
import com.zjyeshi.dajiujiao.buyer.utils.AuthUtil;
import com.zjyeshi.dajiujiao.buyer.utils.DialogUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.buyer.utils.OrderUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 订单Item View
 * Created by wuhk on 2016/9/14.
 */
public class OrderView extends BaseView {
    @InjectView(R.id.shopNameTv)
    private TextView shopNameTv;
    @InjectView(R.id.timeTv)
    private TextView timeTv;
    @InjectView(R.id.deleteIv)
    private ImageView deleteIv;
    @InjectView(R.id.stateTv)
    private TextView stateTv;
    @InjectView(R.id.goodListView)
    private BUHighHeightListView goodListView;
    @InjectView(R.id.marketGoodListView)
    private BUHighHeightListView marketGoodListView;
    @InjectView(R.id.payMethodTv)
    private TextView payMethodTv;
    @InjectView(R.id.doLayout)
    private RelativeLayout doLayout;
    @InjectView(R.id.leftTv)
    private TextView leftTv;
    @InjectView(R.id.rightTv)
    private TextView rightTv;
    @InjectView(R.id.extTv)
    private TextView extTv;
    @InjectView(R.id.finalPriceTv)
    private TextView finalPriceTv;
    @InjectView(R.id.backLayout)
    private LinearLayout backLayout;
    @InjectView(R.id.spView)
    private View spView;

    public OrderView(Context context) {
        super(context);
    }

    public OrderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void dgInit() {
        inflate(getContext(), R.layout.view_order, this);
        ViewUtils.inject(this, this);
    }

    public void bindData(final String userType, final PerOrder order , int position) {
        //一些操作界面先隐藏，后续需要再显示
        deleteIv.setVisibility(View.GONE);//删除
        doLayout.setVisibility(View.GONE);//底部操作按钮
        rightTv.setVisibility(View.GONE);
        leftTv.setVisibility(View.GONE);
        extTv.setVisibility(GONE);

        if (position % 2 == 0){
            backLayout.setBackgroundColor(Color.parseColor("#f6f6f6"));
        }else{
            backLayout.setBackgroundColor(Color.parseColor("#ffffff"));
        }


        if (Validators.isEmpty(order.getMarketCostProductResp())){
            spView.setVisibility(GONE);
        }else{
            spView.setVisibility(VISIBLE);
        }
        OrderUtil.initDeleteOrder(deleteIv, order.getId(), userType);

        payMethodTv.setVisibility(VISIBLE);
        if (order.getType() == 0) {
            payMethodTv.setText("线下支付");
        } else {
            payMethodTv.setText("线上支付");
        }

        //下单时间
        initTextView(timeTv, DateUtils.date2StringBySecond(new Date(order.getCreateTime())));

        //获取订单状态
        final OrderStatusEnum orderStatusEnum = OrderStatusEnum.valueOf(Integer.parseInt(order.getStatus()));
        ////////////////////////////////////////如果是买家//////////////////////////////////////////
        if (userType.equals(LoginEnum.BURER.toString())) {

            //买家显示卖家店名
            initTextView(shopNameTv, order.getShopName());

            if (orderStatusEnum.equals(OrderStatusEnum.WAITPAY)) {
                //待付款(1)
                initTextView(stateTv, "待付款");
                payMethodTv.setVisibility(GONE);
                doLayout.setVisibility(View.VISIBLE);
                rightTv.setVisibility(View.VISIBLE);
                rightTv.setText("去付款");
                rightTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BalanceAccountsActivity.startBalanceActivity(getContext(), getGoodList(order.getProductResp()), getGoodList(order.getMarketCostProductResp())
                                , order.getId(), BalanceAccountsActivity.FROM_ORDER, String.valueOf(Integer.parseInt(order.getAmount()) + Integer.parseInt(order.getMarketCostAmount())) , false , "");
                    }
                });
                leftTv.setVisibility(View.VISIBLE);
                leftTv.setText("取消订单");
                leftTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtil.confirmSure(getContext(), "是否取消订单", "确定", new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String status = String.valueOf(OrderStatusEnum.CLOSED.getValue());
                                OrderUtil.changeOrderStatus(getContext(), order.getId(), status);
                            }
                        });

                    }
                });
            } else if (orderStatusEnum.equals(OrderStatusEnum.PAYED)) {
                //已付款(2)
                initTextView(stateTv, "等待商家确认");
                doLayout.setVisibility(VISIBLE);
                rightTv.setVisibility(VISIBLE);
                rightTv.setText("申请退货");
                rightTv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ApplyRebackActivity.startApplyRebackActivity(getContext() , order.getId());

                    }
                });
            } else if (orderStatusEnum.equals(OrderStatusEnum.SURED)) {
                //已确认(3)
                initTextView(stateTv, "商家已接单");

                doLayout.setVisibility(VISIBLE);
                rightTv.setVisibility(VISIBLE);
                rightTv.setText("申请退货");
                rightTv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ApplyRebackActivity.startApplyRebackActivity(getContext() , order.getId());

                    }
                });
                leftTv.setVisibility(GONE);
            } else if (orderStatusEnum.equals(OrderStatusEnum.SENDED)) {
                //已发货(5)
                initTextView(stateTv, "商家已发货");
                doLayout.setVisibility(VISIBLE);
                leftTv.setVisibility(VISIBLE);
                leftTv.setText("申请退货");
                leftTv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ApplyRebackActivity.startApplyRebackActivity(getContext() , order.getId());
                    }
                });
                rightTv.setVisibility(VISIBLE);
                rightTv.setText("确认收货");
                rightTv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtil.confirmSure(getContext(), "是否确认收货", "确定", new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String status = String.valueOf(OrderStatusEnum.ALREADYGET.getValue());
                                OrderUtil.changeOrderStatus(getContext(), order.getProductResp(), order.getMarketCostProductResp(), order.getId(), status, true);
                            }
                        });
                    }
                });
            } else if (orderStatusEnum.equals(OrderStatusEnum.ALREADYGET)) {
                //已收，未评价(7)
                deleteIv.setVisibility(VISIBLE);

                initTextView(stateTv, "已收货");
                doLayout.setVisibility(VISIBLE);
                leftTv.setVisibility(GONE);
                rightTv.setVisibility(VISIBLE);
                rightTv.setText("去评价");
                rightTv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OrderProductListActivity.startActivity(getContext(), order.getProductResp(), order.getMarketCostProductResp(), order.getId());
                    }
                });
            } else if (orderStatusEnum.equals(OrderStatusEnum.ALREADYCOMMENT)) {
                //已收，已评价(8)
                deleteIv.setVisibility(VISIBLE);

                initTextView(stateTv, "已收货");
                doLayout.setVisibility(VISIBLE);
                leftTv.setVisibility(GONE);
                rightTv.setVisibility(VISIBLE);
                rightTv.setText("追加评价");
                rightTv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OrderProductListActivity.startActivity(getContext(), order.getProductResp(), order.getMarketCostProductResp(), order.getId());
                    }
                });
            } else if (orderStatusEnum.equals(OrderStatusEnum.CLOSED)) {
                //已关闭，已取消(9)
                deleteIv.setVisibility(VISIBLE);
                initTextView(stateTv, "已取消");
                doLayout.setVisibility(GONE);
            } else if (orderStatusEnum.equals(OrderStatusEnum.WAITRETURN)) {
                //待退货(12)
                initTextView(stateTv, "待退货");
                doLayout.setVisibility(GONE);
            } else if (orderStatusEnum.equals(OrderStatusEnum.RETURNSUCCESS)) {
                //退货成功(13)
                deleteIv.setVisibility(VISIBLE);
                initTextView(stateTv, "退货成功");
                doLayout.setVisibility(GONE);
            } else if (orderStatusEnum.equals(OrderStatusEnum.RETURNFAILED)) {
                //退货失败(14)
                deleteIv.setVisibility(VISIBLE);
                initTextView(stateTv, "退货失败");
                doLayout.setVisibility(GONE);
            } else {
                initTextView(stateTv, orderStatusEnum.toString());
                doLayout.setVisibility(GONE);
            }
        }
        /////////////////////////////////////////如果是卖家//////////////////////////////////////////
        else {
            //卖家显示消费者名
            if (!Validators.isEmpty(order.getBuyerShopName())) {
                if (!Validators.isEmpty(order.getSalesman())){
                    initTextView(shopNameTv, order.getBuyerShopName() + "(" + order.getSalesman() + ")");
                }else{
                    initTextView(shopNameTv, order.getBuyerShopName());
                }
            } else {
                initTextView(shopNameTv, order.getBuyer());
            }

            if (orderStatusEnum.equals(OrderStatusEnum.WAITPAY)) {
                //待付款(1)
                payMethodTv.setVisibility(GONE);
                initTextView(stateTv, "待付款");
                doLayout.setVisibility(GONE);
            } else if (orderStatusEnum.equals(OrderStatusEnum.PAYED)) {
                //已付款(2)
                initTextView(stateTv, "待确认");
                //业务员不能确认订单，但是可以转订单，
                //Boss一开始没有操作，但是能看到订单，只有转到的订单才能转和确认，
                //转过的就没有转操作了
                if (AuthUtil.isSalesEdit()) {
                    //撤销和转审只会出现一个
                    if (order.isDealWith()) {
                        doLayout.setVisibility(VISIBLE);
                        //当前审核人 订单如果是业务员待下单的，可以删除
                        if (order.isReplaceOrder()) {
                            leftTv.setVisibility(VISIBLE);
                            leftTv.setText("删除订单");
                            leftTv.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DialogUtil.confirmSure(getContext(), "是否删除订单", "确定", new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            RemoverOrderMistakeTask.removeOrderWhenMistake(getContext(), order.getId(), new AsyncTaskSuccessCallback<NoResultData>() {
                                                @Override
                                                public void successCallback(Result<NoResultData> result) {
                                                    ToastUtil.toast("删除成功");
                                                    ChangeOrderStatusReceiver.notifyReceiver();
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        } else {
                            leftTv.setVisibility(GONE);
                        }

                        rightTv.setVisibility(VISIBLE);
                        rightTv.setText("订单转审");
                        rightTv.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MaxLevelManActivity.startMaxLevelManActivity(getContext(), order.getId() , MaxLevelManActivity.TYPE_ORDER);

                            }
                        });
                    } else {
                        if (order.isCanRevoke()) {
                            doLayout.setVisibility(VISIBLE);
                            leftTv.setVisibility(GONE);
                            rightTv.setVisibility(VISIBLE);
                            rightTv.setText("撤销转审");
                            rightTv.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DialogUtil.confirmSure(getContext(), "是否撤销转审", "确定", new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            DrawBackOrderReviewTask.drawbackOrderReview(getContext(), order.getId(), new AsyncTaskSuccessCallback<NoResultData>() {
                                                @Override
                                                public void successCallback(Result<NoResultData> result) {
                                                    ToastUtil.toast("撤销转审成功");
                                                    ChangeOrderStatusReceiver.notifyReceiver();
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        } else {
                            doLayout.setVisibility(GONE);
                        }
                    }
                } else {
                    //boss
                    if (order.isDealWith()) {
                        doLayout.setVisibility(VISIBLE);
                        leftTv.setVisibility(VISIBLE);
                        leftTv.setText("订单转审");
                        leftTv.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MaxLevelManActivity.startMaxLevelManActivity(getContext(), order.getId() , MaxLevelManActivity.TYPE_ORDER);
                            }
                        });
                        rightTv.setVisibility(VISIBLE);
                        rightTv.setText("确认订单");
                        rightTv.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DialogUtil.confirmSure(getContext(), "是否确认订单", "确定", new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String status = String.valueOf(OrderStatusEnum.SURED.getValue());
                                        OrderUtil.modifyOrderStatus(getContext(), order.getId(), status);
                                    }
                                });
                            }
                        });
                        //当前审核人 订单如果是业务员待下单的，可以删除
                        if (order.isReplaceOrder()) {
                            extTv.setVisibility(VISIBLE);
                            extTv.setText("删除订单");
                            extTv.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DialogUtil.confirmSure(getContext(), "是否删除订单", "确定", new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            RemoverOrderMistakeTask.removeOrderWhenMistake(getContext(), order.getId(), new AsyncTaskSuccessCallback<NoResultData>() {
                                                @Override
                                                public void successCallback(Result<NoResultData> result) {
                                                    ToastUtil.toast("删除成功");
                                                    ChangeOrderStatusReceiver.notifyReceiver();
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        } else {
                            extTv.setVisibility(GONE);
                        }
                    } else {
                        if (order.isCanRevoke()) {
                            doLayout.setVisibility(VISIBLE);
                            leftTv.setVisibility(GONE);
                            rightTv.setVisibility(VISIBLE);
                            rightTv.setText("撤销转审");
                            rightTv.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DialogUtil.confirmSure(getContext(), "是否撤销转审", "确定", new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            DrawBackOrderReviewTask.drawbackOrderReview(getContext(), order.getId(), new AsyncTaskSuccessCallback<NoResultData>() {
                                                @Override
                                                public void successCallback(Result<NoResultData> result) {
                                                    ToastUtil.toast("撤销转审成功");
                                                    ChangeOrderStatusReceiver.notifyReceiver();
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        } else {
                            doLayout.setVisibility(GONE);
                        }
                    }
                }

            } else if (orderStatusEnum.equals(OrderStatusEnum.SURED)) {
                //已确认(3)
                initTextView(stateTv, "商家已确认");
          //  stateTv.setTextColor(Color.parseColor("#0d7f04"));
                doLayout.setVisibility(VISIBLE);
                leftTv.setVisibility(GONE);
                //有发货权限的显示确认发货
                if (AuthUtil.canDeliver()) {
                    rightTv.setVisibility(VISIBLE);
                    rightTv.setText("确认发货");
                    rightTv.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogUtil.editDialog(getContext(), "提示", "添加发货备注，没有可不填", "取消", "确定", new DGPromptDialog.PromptDialogListener() {
                                @Override
                                public void onClick(View view, String inputText) {
                                    //取消，不做操作
                                }
                            }, new DGPromptDialog.PromptDialogListener() {
                                @Override
                                public void onClick(View view, String inputText) {
                                    String status = String.valueOf(OrderStatusEnum.SENDED.getValue());
                                    OrderUtil.modifyOrderStatusForDeliver(getContext(), order.getId(), status, inputText);
                                }
                            });
                        }
                    });
                } else {
                    //没有发货权限的就没有操作了
                    doLayout.setVisibility(GONE);
                }

            } else if (orderStatusEnum.equals(OrderStatusEnum.SENDED)) {
                //已发货(5)
                initTextView(stateTv, "已发货");
                doLayout.setVisibility(GONE);

            } else if (orderStatusEnum.equals(OrderStatusEnum.ALREADYGET)) {
                //已收，未评价(7)
                deleteIv.setVisibility(VISIBLE);
                initTextView(stateTv, "已收货");
                doLayout.setVisibility(GONE);
            } else if (orderStatusEnum.equals(OrderStatusEnum.ALREADYCOMMENT)) {
                //已收，已评价(8)
                deleteIv.setVisibility(VISIBLE);
                initTextView(stateTv, "已收货");
                doLayout.setVisibility(GONE);
            } else if (orderStatusEnum.equals(OrderStatusEnum.CLOSED)) {
                //已关闭，已取消(9)
                deleteIv.setVisibility(VISIBLE);
                initTextView(stateTv, "已取消");
                doLayout.setVisibility(GONE);
            } else if (orderStatusEnum.equals(OrderStatusEnum.WAITRETURN)) {
                //待退货(12)
                initTextView(stateTv, "待退货");
                doLayout.setVisibility(GONE);
            } else if (orderStatusEnum.equals(OrderStatusEnum.RETURNSUCCESS)) {
                //退货成功(13)
                deleteIv.setVisibility(VISIBLE);
                initTextView(stateTv, "退货成功");
                doLayout.setVisibility(GONE);
            } else if (orderStatusEnum.equals(OrderStatusEnum.RETURNFAILED)) {
                //退货失败(14)
                deleteIv.setVisibility(VISIBLE);
                initTextView(stateTv, "退货失败");
                doLayout.setVisibility(GONE);
            } else {
                initTextView(stateTv, orderStatusEnum.toString());
                doLayout.setVisibility(GONE);
            }
        }

        /////////////////////////////////////////////订单的商品列表////////////////////////////////////
        List<OrderProduct> goodList = order.getProductResp();
        List<OrderProduct> marketGoodList = order.getMarketCostProductResp();

        OnClickListener salesOnClickListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                orderProductItemClick(userType, order);
            }
        };

        if (!Validators.isEmpty(goodList) || !Validators.isEmpty(marketGoodList)) {

            //已取消或者用户是发货人不显示价格
            if (orderStatusEnum.equals(OrderStatusEnum.CLOSED) || AuthUtil.canDeliver()) {
                OrderGoodsListAdapter orderGoodsListAdapter = new OrderGoodsListAdapter(getContext(), goodList, false, GoodTypeEnum.NORMAL_BUY.getValue(), order.getAmount() , position , salesOnClickListener);
                goodListView.setAdapter(orderGoodsListAdapter);
                OrderGoodsListAdapter marketOrderGoodsListAdapter = new OrderGoodsListAdapter(getContext(), marketGoodList, false, GoodTypeEnum.MARKET_SUPPORT.getValue(), order.getMarketCostAmount() , position , salesOnClickListener);
                marketGoodListView.setAdapter(marketOrderGoodsListAdapter);
                finalPriceTv.setVisibility(GONE);
            } else {
                OrderGoodsListAdapter orderGoodsListAdapter = new OrderGoodsListAdapter(getContext(), goodList, true, GoodTypeEnum.NORMAL_BUY.getValue(), order.getAmount() , position , salesOnClickListener);
                goodListView.setAdapter(orderGoodsListAdapter);
                OrderGoodsListAdapter marketOrderGoodsListAdapter = new OrderGoodsListAdapter(getContext(), marketGoodList, true, GoodTypeEnum.MARKET_SUPPORT.getValue(), order.getMarketCostAmount() , position , salesOnClickListener);
                marketGoodListView.setAdapter(marketOrderGoodsListAdapter);
                finalPriceTv.setVisibility(VISIBLE);
                float finalPrice =  Float.parseFloat(order.getAmount()) + Float.parseFloat(order.getMarketCostAmount());
                finalPriceTv.setText("共计:¥" + ExtraUtil.format(finalPrice/100));
            }

            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    orderProductItemClick(userType , order);
                }
            });
        }

    }


    /**
     * 订单商品列表点击事件
     *
     * @param userType 用户类型，卖家||买家
     * @param order    订单
     */
    public void orderProductItemClick(String userType, PerOrder order) {
        OrderStatusEnum orderStatusEnum = OrderStatusEnum.valueOf(Integer.parseInt(order.getStatus()));
        if (userType.equals(LoginEnum.SELLER.toString())) {
            if (orderStatusEnum.equals(OrderStatusEnum.WAITPAY) || (orderStatusEnum.equals(OrderStatusEnum.PAYED) && order.getType() == 0)) {
                //待付款订单和已付款中的线下支付可以进行修改,只有dealWith才能修改
                if (order.isDealWith()) {
                    //业务员和内勤 页面 需要参数订单ID 订单状态
                    OrderDetailEditActivity.startActivity(getContext(), order.getId(),order.isCanRevoke(),order.isDealWith(), order.isReplaceOrder());
                } else {
                    OrderDetailNewActivity.startOrderDetailNewActivity(getContext(), order.getId(), userType);
                }
            } else {
                //其余状态不能修改，直接进入详情
                OrderDetailNewActivity.startOrderDetailNewActivity(getContext(), order.getId(), userType);
            }
        } else {
            //买家直接进入详情
            OrderDetailNewActivity.startOrderDetailNewActivity(getContext(), order.getId(), userType);
        }
    }

    /**
     * 将订单商品列表转换成结算界面需要显示的列表
     *
     * @param productList
     * @return
     */
    private List<GoodsCar> getGoodList(List<OrderProduct> productList) {
        List<GoodsCar> retList = new ArrayList<GoodsCar>();
        for (OrderProduct orderProduct : productList) {
            GoodsCar goodsCar = new GoodsCar();
            goodsCar.setGoodId(orderProduct.getId());
            goodsCar.setGoodName(orderProduct.getName());
            goodsCar.setGoodIcon(orderProduct.getPic());
            goodsCar.setGoodCount(orderProduct.getCount());
            goodsCar.setGoodPrice(ExtraUtil.format(Float.parseFloat(orderProduct.getPrice()) / 100));
            retList.add(goodsCar);
        }
        return retList;
    }
}
