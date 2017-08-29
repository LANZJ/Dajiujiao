package com.zjyeshi.dajiujiao.buyer.activity.my.work;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.task.work.ModifyTimeCardRemarkTask;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

/**
 * Created by wuhk on 2016/6/20.
 */
public class AddRemarkActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;

    @InjectView(R.id.remarkEt)
    private EditText remarkEt;

    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_remark);
        initWidgets();
    }

    private void initWidgets(){
        id  = getIntent().getStringExtra("cardId");
        titleLayout.configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).configTitle("备注").configRightText("提交", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String remark = remarkEt.getText().toString();
                if (Validators.isEmpty(remark)){
                    ToastUtil.toast("请添加备注");
                }else{
                    ModifyTimeCardRemarkTask modifyTimeCardRemarkTask = new ModifyTimeCardRemarkTask(AddRemarkActivity.this);
                    modifyTimeCardRemarkTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
                        @Override
                        public void failCallback(Result<NoResultData> result) {
                            ToastUtil.toast(result.getMessage());
                        }
                    });

                    modifyTimeCardRemarkTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
                        @Override
                        public void successCallback(Result<NoResultData> result) {
                            ToastUtil.toast("备注添加成功");
                            finish();
                        }
                    });

                    modifyTimeCardRemarkTask.execute(id , remark);
                }

            }
        });

    }
}
