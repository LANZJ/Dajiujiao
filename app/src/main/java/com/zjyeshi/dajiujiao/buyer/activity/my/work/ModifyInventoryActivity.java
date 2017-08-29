package com.zjyeshi.dajiujiao.buyer.activity.my.work;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.bitmap.BPBitmapLoader;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.CompanyStock;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.task.work.ModifyInventoryTask;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

/**
 * 修改客户库存
 * Created by wuhk on 2016/6/23.
 */
public class ModifyInventoryActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.avatarIv)
    private ImageView avatarIv;
    @InjectView(R.id.nameTv)
    private TextView nameTv;
    @InjectView(R.id.kcTv)
    private TextView kcTv;
    @InjectView(R.id.desTv)
    private TextView desTv;
    @InjectView(R.id.xiangEt)
    private EditText xiangEt;
    @InjectView(R.id.pingET)
    private EditText pingEt;
    @InjectView(R.id.sureBtn)
    private Button sureBtn;
    @InjectView(R.id.unitDesTv)
    private TextView unitDesTv;
    @InjectView(R.id.unitTv)
    private TextView unitTv;

    private CompanyStock companyStock;

    //请求参数
    private String id;
    private String boxNum;
    private String bottleNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_inventory);

        initWidgets();
    }

    private void initWidgets(){
        String jsonStr = getIntent().getStringExtra("companyStock");
        companyStock = JSON.parseObject(jsonStr , CompanyStock.class);
        id = companyStock.getId();
        BPBitmapLoader.getInstance().display(avatarIv , companyStock.getPic());
        nameTv.setText(companyStock.getName());
        desTv.setText(companyStock.getSpecifications() + "/" +companyStock.getUnit() + "  " + companyStock.getBottlesPerBox()
                + companyStock.getUnit() + "/箱");
        unitDesTv.setText("单位:" + companyStock.getUnit() + "/" + "箱");
        unitTv.setText(companyStock.getUnit());

        int box = (Integer.parseInt(companyStock.getInventory()) / (Integer.parseInt(companyStock.getBottlesPerBox())));
        int bottle = (Integer.parseInt(companyStock.getInventory()) % (Integer.parseInt(companyStock.getBottlesPerBox())));
        kcTv.setText("库存剩余:" + box + "箱" + bottle + companyStock.getUnit());
        xiangEt.setText(String.valueOf(box));
        pingEt.setText(String.valueOf(bottle));
        titleLayout.configTitle("修改库存").configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyInventory();
            }
        });
    }

    private void modifyInventory(){
        boxNum = xiangEt.getText().toString();
        bottleNum = pingEt.getText().toString();

        ModifyInventoryTask modifyInventoryTask = new ModifyInventoryTask(ModifyInventoryActivity.this);
        modifyInventoryTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        modifyInventoryTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                ToastUtil.toast("修改成功");
                finish();
            }
        });

        modifyInventoryTask.execute(id , boxNum , bottleNum);
    }
}
