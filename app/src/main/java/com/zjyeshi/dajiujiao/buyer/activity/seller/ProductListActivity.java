package com.zjyeshi.dajiujiao.buyer.activity.seller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigdog.lib.dialog.DGSingleSelectDialog;
import com.xuan.bigdog.lib.widgets.adapter.DGNoDataView;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.CompanyStock;
import com.zjyeshi.dajiujiao.buyer.task.work.CompanyStockListTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.CompanyStockListData;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.my.work.CategoryFilterActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.seller.ProductListAdapter;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.buyer.widgets.store.ProductBottomLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * 商品管理
 * Created by xuan on 15/10/28.
 */
public class ProductListActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.listView)
    private ListView listView;
    @InjectView(R.id.sortTv)
    private TextView sortTv;
    @InjectView(R.id.countTv)
    private TextView countTv;
    @InjectView(R.id.noDataView)
    private DGNoDataView noDataView;
    @InjectView(R.id.productBottomLayout)
    private ProductBottomLayout productBottomLayout;

    public ProductBottomLayout getProductBottomLayout() {
        return productBottomLayout;
    }

    private ProductListAdapter productListAdapter;

    public ProductListAdapter getProductListAdapter() {
        return productListAdapter;
    }

    private List<CompanyStock> dataList = new ArrayList<CompanyStock>();
    private String memberId = "";
    private String categoryId = "";
    private String categoryTitle = "我的酒库";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_layout_product_list);
        initWidgets();
    }

    private void initWidgets() {

        titleLayout.configTitle(categoryTitle).configReturn("返回", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        noDataView.configMessage("该分类没有商品");
        titleLayout.configRightText("筛选", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductListActivity.this, CategoryFilterActivity.class);
                intent.putExtra(CategoryFilterActivity.PARAM_CATEID, categoryId);
                startActivityForResult(intent, CategoryFilterActivity.REQUEST_CODE_FOR_FILTER);
            }
        });

        sortTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DGSingleSelectDialog dialog = new DGSingleSelectDialog.Builder(ProductListActivity.this)
                        .setItemTextAndOnClickListener(new String[]{"由多至少", "由少至多"}, new View.OnClickListener[]{new View.OnClickListener() {
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
        productBottomLayout.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CompanyStock product = dataList.get(position);
                productBottomLayout.setVisibility(View.VISIBLE);
                productBottomLayout.bindData(product);
            }
        });
        productListAdapter = new ProductListAdapter(this, dataList);
        listView.setAdapter(productListAdapter);

        loadData();
    }

    /**
     * 加载数据
     */
    private void loadData() {
        CompanyStockListTask companyStockListTask = new CompanyStockListTask(ProductListActivity.this);
        companyStockListTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<CompanyStockListData>() {
            @Override
            public void failCallback(Result<CompanyStockListData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });
        companyStockListTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<CompanyStockListData>() {
            @Override
            public void successCallback(Result<CompanyStockListData> result) {
                dataList.clear();
                dataList.addAll(result.getValue().getList());
                for (CompanyStock companyStock : dataList) {
                    companyStock.setKc(Integer.parseInt(companyStock.getInventory()));
                }
                calCount();
                productListAdapter.notifyDataSetChanged();
                noDataView.showIfEmpty(dataList);
            }
        });

        companyStockListTask.execute(memberId, categoryId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == CategoryFilterActivity.REQUEST_CODE_FOR_FILTER) {
            categoryId = data.getStringExtra(CategoryFilterActivity.PARAM_CATEID);
            categoryTitle = data.getStringExtra(CategoryFilterActivity.PARAM_CATENAME);
            titleLayout.configTitle(categoryTitle);
            loadData();
        }
    }

    /**排序
     *
     * @param isMost
     */
    private void sortList(boolean isMost) {
        Collections.sort(dataList, new Comparator<CompanyStock>() {
            public int compare(CompanyStock arg0, CompanyStock arg1) {
                return arg0.getKc().compareTo(arg1.getKc());
            }
        });
        if (isMost) {
            List<CompanyStock> tempList = new ArrayList<CompanyStock>();
            for (int i = 0; i < dataList.size(); i++) {
                tempList.add(0, dataList.get(i));
            }
            dataList.clear();
            dataList.addAll(tempList);
        }
        productListAdapter.notifyDataSetChanged();
    }

    /**
     * 计算总库存
     */
    public void calCount() {
        HashMap<String, String> numMap = new HashMap<String, String>();
        String count = "";
        float totalPrice = 0.00f;
        for (CompanyStock stock : dataList) {
            //计算我的库存总计
            int myBoxNum = (Integer.parseInt(stock.getInventory())) / (Integer.parseInt(stock.getBottlesPerBox()));
            int myUnitNum = (Integer.parseInt(stock.getInventory())) % (Integer.parseInt(stock.getBottlesPerBox()));
            if (!Validators.isEmpty(numMap.get("箱"))) {
                numMap.put("箱", String.valueOf(Integer.parseInt(numMap.get("箱")) + myBoxNum));
            } else {
                numMap.put("箱", String.valueOf(myBoxNum));
            }
            if (!Validators.isEmpty(numMap.get(stock.getUnit()))) {
                numMap.put(stock.getUnit(), String.valueOf(Integer.parseInt(numMap.get(stock.getUnit())) + myUnitNum));
            } else {
                numMap.put(stock.getUnit(), String.valueOf(myUnitNum));
            }
            //我的价格总计
            totalPrice += Integer.parseInt(stock.getInventory()) * Float.parseFloat(stock.getPrice());
        }

        if (Validators.isEmpty(numMap.get("箱"))){
            count ="0箱";
        }else{
            count = numMap.get("箱") + "箱";
        }
        for (String key : numMap.keySet()) {
            if (!key.equals("箱")) {
                count += numMap.get(key) + key;
            }
        }
        countTv.setText("总库存:" + count + "  " + "总金额:¥" + ExtraUtil.format(totalPrice));
    }
}
