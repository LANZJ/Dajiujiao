package com.zjyeshi.dajiujiao.buyer.activity.order;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xuan.bigapple.lib.utils.Validators;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.common.PassConstans;
import com.zjyeshi.dajiujiao.buyer.entity.enums.OrderStatusEnum;
import com.zjyeshi.dajiujiao.buyer.receiver.order.ChangeOrderStatusReceiver;
import com.zjyeshi.dajiujiao.buyer.receiver.order.OrderCommentReceiver;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.task.order.CommentOrderTask;
import com.zjyeshi.dajiujiao.buyer.task.order.SellerSureTask;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.widgets.level.CommentLevelWidget;
import com.zjyeshi.dajiujiao.buyer.widgets.level.callback.LevelCallback;

/**
 * 订单评价
 * Created by wuhk on 2015/11/22.
 */
public class OrderCommentActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;

    @InjectView(R.id.contentEt)
    private EditText contentEt;

    @InjectView(R.id.sendBtn)
    private Button sendBtn;

    @InjectView(R.id.levelWidget)
    private CommentLevelWidget levelWidget;

    private String orderId;
    private String productId;
    private int position;
    private int levelStar = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_order_comment);
        initWidgets();
    }

    private void initWidgets(){

        orderId = getIntent().getStringExtra(PassConstans.ORDERID);
        productId = getIntent().getStringExtra(PassConstans.PRODUCTID);
        position = getIntent().getIntExtra(PassConstans.POSITION, -1);

        titleLayout.configReturn("返回", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).configTitle("商品评价").configRightText("发送", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBtn.performClick();
            }
        });

        levelWidget.setLevelCallback(new LevelCallback() {
            @Override
            public void dealWithLevel(int level) {
                levelStar = level;
            }
        });
        //发送评价
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = contentEt.getText().toString();
                if (Validators.isEmpty(content) || levelStar == 0){
                    ToastUtil.toast("请填写完整评价内容");
                }else{
                    CommentOrderTask commentOrderTask = new CommentOrderTask(OrderCommentActivity.this);
                    commentOrderTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
                        @Override
                        public void failCallback(Result<NoResultData> result) {
                            ToastUtil.toast(result.getMessage());
                        }
                    });

                    commentOrderTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
                        @Override
                        public void successCallback(Result<NoResultData> result) {
                            changeOrderStatus(orderId);
                        }
                    });
                    commentOrderTask.execute(orderId, productId ,String.valueOf(levelStar), content);
                }
            }
        });
    }

    private void changeOrderStatus(String orderId){
        SellerSureTask sellerSureTask = new SellerSureTask(OrderCommentActivity.this);
        sellerSureTask.setShowProgressDialog(false);
        sellerSureTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        sellerSureTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                ToastUtil.toast("评价成功");
                ChangeOrderStatusReceiver.notifyReceiver();
                OrderCommentReceiver.notifyReceiver(position);
                finish();
            }
        });
        sellerSureTask.execute(orderId ,String.valueOf(OrderStatusEnum.ALREADYCOMMENT.getValue()));
    }
}
