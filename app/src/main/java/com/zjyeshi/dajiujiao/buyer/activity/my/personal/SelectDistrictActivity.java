package com.zjyeshi.dajiujiao.buyer.activity.my.personal;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.my.AreaAdapter;
import com.zjyeshi.dajiujiao.buyer.common.PassConstans;
import com.zjyeshi.dajiujiao.buyer.dao.DaoFactory;
import com.zjyeshi.dajiujiao.buyer.entity.enums.ProvinceEnum;
import com.zjyeshi.dajiujiao.buyer.entity.my.BigArea;
import com.zjyeshi.dajiujiao.buyer.entity.my.DetailArea;
import com.zjyeshi.dajiujiao.buyer.receiver.info.SelectAreaReceive;
import com.zjyeshi.dajiujiao.R;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择区
 * Created by wuhk on 2015/11/13.
 */
public class SelectDistrictActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;

    @InjectView(R.id.listView)
    private ListView listView;

    private AreaAdapter areaAdapter;
    private List<DetailArea> districtList = new ArrayList<DetailArea>();
    private SelectAreaReceive selectAreaReceive;

    private String code;
    private String speName;
    private String speCode;

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
        code = getIntent().getStringExtra(PassConstans.CODE);
        speName = getIntent().getStringExtra(PassConstans.SPENAME);
        speCode = getIntent().getStringExtra(PassConstans.SPECODE);
        titleLayout.configReturn("返回", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).configTitle("我的地址");

        getDistrict();

        areaAdapter = new AreaAdapter(SelectDistrictActivity.this , districtList);
        listView.setAdapter(areaAdapter);
    }

    //获取区数据
    private void getDistrict(){
        districtList.clear();
        BigArea bigArea = DaoFactory.getAreaDao().findByCode("0"+","+speCode);
        List<DetailArea> detailAreaList = bigArea.getList();
        for (DetailArea detailArea : detailAreaList){
            detailArea.setProvinceEnum(ProvinceEnum.DISTRICT);
            detailArea.setSpeCode(speCode + "," + detailArea.getCode());
            detailArea.setSpeName(speName+ " "+ detailArea.getName());
        }
        districtList.addAll(detailAreaList);
    }
}
