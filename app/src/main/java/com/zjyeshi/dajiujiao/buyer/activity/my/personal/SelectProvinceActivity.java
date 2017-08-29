package com.zjyeshi.dajiujiao.buyer.activity.my.personal;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.dao.DaoFactory;
import com.zjyeshi.dajiujiao.buyer.entity.enums.ProvinceEnum;
import com.zjyeshi.dajiujiao.buyer.entity.my.BigArea;
import com.zjyeshi.dajiujiao.buyer.entity.my.DetailArea;
import com.zjyeshi.dajiujiao.buyer.receiver.info.SelectAreaReceive;
import com.zjyeshi.dajiujiao.buyer.task.my.GetAreaDataTask;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.adapter.my.AreaAdapter;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择省
 * Created by wuhk on 2015/11/13.
 */
public class SelectProvinceActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;

    @InjectView(R.id.listView)
    private ListView listView;

    private AreaAdapter areaAdapter;
    private List<DetailArea> provinceList = new ArrayList<DetailArea>();

    private SelectAreaReceive selectAreaReceive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_select_area);

        selectAreaReceive = new SelectAreaReceive() {
            @Override
            public void closeActivity() {
                finish();
            }
        };
        selectAreaReceive.register();
        initWidgets();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        selectAreaReceive.unRegister();
    }

    private void initWidgets(){
        titleLayout.configReturn("返回", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).configTitle("我的地址");
        BigArea bigArea = DaoFactory.getAreaDao().findByCode("0");
        if (null == bigArea){
            GetAreaDataTask getAreaDataTask = new GetAreaDataTask(SelectProvinceActivity.this);
            getAreaDataTask.setShowProgressDialog(true);
            getAreaDataTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
                @Override
                public void successCallback(Result<NoResultData> result) {
                    getProvince();
                    areaAdapter.notifyDataSetChanged();
                }
            });
            getAreaDataTask.execute();
        }else{
            getProvince();
        }
        areaAdapter = new AreaAdapter(SelectProvinceActivity.this , provinceList);
        listView.setAdapter(areaAdapter);
    }

    //获取数据
    private void getProvince(){
        provinceList.clear();
        BigArea bigArea = DaoFactory.getAreaDao().findByCode("0");
        List<DetailArea> detailAreaList = bigArea.getList();
        for (DetailArea detailArea : detailAreaList){
            detailArea.setProvinceEnum(ProvinceEnum.PROVINCE);
            detailArea.setSpeCode(detailArea.getCode());
            detailArea.setSpeName(detailArea.getName());
        }
        provinceList.addAll(detailAreaList);
    }
}
