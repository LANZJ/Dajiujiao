package com.zjyeshi.dajiujiao.buyer.activity.my.work;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.bitmap.BPBitmapLoader;
import com.xuan.bigapple.lib.bitmap.BitmapDisplayConfig;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigdog.lib.dialog.DGSingleSelectDialog;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.CompanyStock;
import com.zjyeshi.dajiujiao.buyer.task.work.CompanyStockListTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.CompanyStockListData;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.adapter.my.work.CompanyStockListAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.Stock;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * 公司库存
 *
 * Created by zhum on 2016/6/15.
 */
public class CompanyStockActivity extends BaseActivity {

    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;//标题栏
    @InjectView(R.id.totalkcTv)
    private TextView totalkcTv;
    @InjectView(R.id.allMoneyTv)
    private TextView allMoneyTv;
    @InjectView(R.id.listView)
    private ListView listView;
    @InjectView(R.id.sortLayout)
    private RelativeLayout sortLayout;
    @InjectView(R.id.sortNameTv)
    private TextView sortNameTv;
    @InjectView(R.id.headLayout)
    private RelativeLayout headLayout;
    @InjectView(R.id.avatarIv)
    private ImageView avatarIv;
    @InjectView(R.id.shopNameTv)
    private TextView shopNameTv;
    @InjectView(R.id.sortTv)
    private TextView sortTv;

    private CompanyStockListAdapter adapter;
    private List<CompanyStock> dataLists = new ArrayList<CompanyStock>();

    private String memberId;
    private String categoryId = "";
    private Stock stock;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_company_kc);

        initWidgets();
    }

    private void initWidgets(){
        String stockstr = getIntent().getStringExtra("memberInfo");
        if (Validators.isEmpty(stockstr)){
            headLayout.setVisibility(View.GONE);
            sortTv.setVisibility(View.VISIBLE);
        }else{
            headLayout.setVisibility(View.VISIBLE);
            sortTv.setVisibility(View.GONE);
            stock = JSON.parseObject(stockstr , Stock.class);
            memberId = stock.getId();
            shopNameTv.setText(stock.getShopName());
            BitmapDisplayConfig config = new BitmapDisplayConfig();
            config.setLoadFailedBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.default_img));
            config.setLoadingBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.default_img));

            BPBitmapLoader.getInstance().display(avatarIv , stock.getShopPic() , config);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CompanyStock companyStock  = dataLists.get(position);
                    Intent intent = new Intent();
                    intent.setClass(CompanyStockActivity.this , ModifyInventoryActivity.class);
                    intent.putExtra("companyStock" , JSON.toJSONString(companyStock));
                    startActivity(intent);
                }
            });

        }

        titleLayout.configTitle("库存详情").configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        titleLayout.configRightText("筛选", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompanyStockActivity.this,CategoryFilterActivity.class);
                intent.putExtra(CategoryFilterActivity.PARAM_CATEID , categoryId);
                startActivityForResult(intent , CategoryFilterActivity.REQUEST_CODE_FOR_FILTER);
            }
        });

        adapter = new CompanyStockListAdapter(CompanyStockActivity.this,dataLists);
        listView.setAdapter(adapter);
        sortLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DGSingleSelectDialog dialog = new DGSingleSelectDialog.Builder(CompanyStockActivity.this)
                        .setItemTextAndOnClickListener(new String[]{"由多至少" , "由少至多"} , new View.OnClickListener[]{new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sortNameTv.setText("由多至少");
                                sortList(true);
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sortNameTv.setText("由少至多");
                                sortList(false);

                            }
                        }}).create();
                dialog.show();
            }
        });

        sortTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DGSingleSelectDialog dialog = new DGSingleSelectDialog.Builder(CompanyStockActivity.this)
                        .setItemTextAndOnClickListener(new String[]{"由多至少" , "由少至多"} , new View.OnClickListener[]{new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sortList(true);
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sortList(false);

                            }
                        }}).create();
                dialog.show();
            }
        });
        initDatas();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == CategoryFilterActivity.REQUEST_CODE_FOR_FILTER){
            categoryId = data.getStringExtra(CategoryFilterActivity.PARAM_CATEID);
            initDatas();
        }
    }

    private void initDatas(){
        CompanyStockListTask companyStockListTask = new CompanyStockListTask(CompanyStockActivity.this);
        companyStockListTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<CompanyStockListData>() {
            @Override
            public void failCallback(Result<CompanyStockListData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        companyStockListTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<CompanyStockListData>() {
            @Override
            public void successCallback(Result<CompanyStockListData> result) {
                dataLists.clear();
                dataLists.addAll(result.getValue().getList());
                String myKc = "";
                float allMoney = 0.00f;
                HashMap<String , String> numMap = new HashMap<String, String>();
                for (CompanyStock companyStock : dataLists){
                    if (Validators.isEmpty(companyStock.getUnit())){
                        companyStock.setUnit("瓶");
                    }
                    companyStock.setKc(Integer.parseInt(companyStock.getInventory()));
                    allMoney += (Float.parseFloat(companyStock.getInventory())) * (Float.parseFloat(companyStock.getPrice()));
                    int myBoxNum = (Integer.parseInt(companyStock.getInventory())) / (Integer.parseInt(companyStock.getBottlesPerBox()));
                    int myUnitNum = (Integer.parseInt(companyStock.getInventory())) % (Integer.parseInt(companyStock.getBottlesPerBox()));
                    if (myBoxNum != 0){
                        if (!Validators.isEmpty(numMap.get("箱"))){
                            numMap.put("箱" , String.valueOf(Integer.parseInt(numMap.get("箱")) + myBoxNum));
                        }else{
                            numMap.put("箱" , String.valueOf(myBoxNum));
                        }
                    }
                    if (myUnitNum != 0){
                        if (!Validators.isEmpty(numMap.get(companyStock.getUnit()))){
                            numMap.put(companyStock.getUnit() , String.valueOf(Integer.parseInt(numMap.get(companyStock.getUnit())) + myUnitNum));
                        }else{
                            numMap.put(companyStock.getUnit() , String.valueOf(myUnitNum));
                        }
                    }
                }
                for (String key : numMap.keySet()) {
                    myKc += numMap.get(key) + key;
                }
                if (Validators.isEmpty(myKc)){
                    myKc = "0";
                }
                totalkcTv.setText("库存:" + myKc);
                allMoneyTv.setText("¥" + ExtraUtil.format(allMoney/100));
//                sortList(true);
                adapter.notifyDataSetChanged();
            }
        });

        if (Validators.isEmpty(memberId)){
            memberId = "";
        }
        if (Validators.isEmpty(categoryId)){
            categoryId = "";
        }
        companyStockListTask.execute(memberId  , categoryId);
    }

    private void sortList(boolean isMost){
        Collections.sort(dataLists,new Comparator<CompanyStock>(){
            public int compare(CompanyStock arg0, CompanyStock arg1) {
                return arg0.getKc().compareTo(arg1.getKc());
            }
        });
        if (isMost){
            List<CompanyStock> tempList = new ArrayList<CompanyStock>();
            for (int i = 0 ; i < dataLists.size() ; i ++){
                tempList.add(0 , dataLists.get(i));
            }
            dataLists.clear();
            dataLists.addAll(tempList);
        }
        adapter.notifyDataSetChanged();
    }
}