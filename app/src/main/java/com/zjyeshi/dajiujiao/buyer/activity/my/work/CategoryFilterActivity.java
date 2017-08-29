package com.zjyeshi.dajiujiao.buyer.activity.my.work;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.task.store.SortListTask;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.adapter.my.work.CategoryFilterAdapter;
import com.zjyeshi.dajiujiao.buyer.task.data.store.sort.Category;
import com.zjyeshi.dajiujiao.buyer.task.data.store.sort.SortData;
import com.zjyeshi.dajiujiao.buyer.task.data.store.sort.SortList;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 库存酒品类型筛选
 * Created by wuhk on 2016/6/23.
 */
public class CategoryFilterActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;

    @InjectView(R.id.listView)
    private ListView listView;

    private CategoryFilterAdapter categoryFilterAdapter;
    private List<Category> dataList = new ArrayList<Category>();

    public static final int REQUEST_CODE_FOR_FILTER = 111;
    public static final String PARAM_CATEID = "param.cate.id";
    public static final String PARAM_CATENAME = "param.cate.name";

    private String selectCategoryId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_category_filter);
        initWidgets();
    }

    private void initWidgets(){

        titleLayout.configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).configTitle("筛选");
        selectCategoryId = getIntent().getStringExtra(PARAM_CATEID);

        categoryFilterAdapter = new CategoryFilterAdapter(CategoryFilterActivity.this , dataList);
        listView.setAdapter(categoryFilterAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Category category = dataList.get(position);
                Intent intent = getIntent();
                intent.putExtra(PARAM_CATEID , category.getId());
                intent.putExtra(PARAM_CATENAME , category.getName());
                setResult(Activity.RESULT_OK , intent);
                finish();
            }
        });
        loadData();


    }

    private void loadData(){
        SortListTask sortListTask = new SortListTask(CategoryFilterActivity.this);
        sortListTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<SortList>() {
            @Override
            public void failCallback(Result<SortList> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        sortListTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<SortList>() {
            @Override
            public void successCallback(Result<SortList> result) {
                dataList.clear();
                Category category1 = new Category();
                category1.setName("全部");
                category1.setId("");
                dataList.add(category1);
                for(SortData sortData : result.getValue().getList()){
                    Category category = new Category();
                    category.setId(sortData.getCategory().getId());
                    category.setName(sortData.getCategory().getName());
                    dataList.add(category);
                }

                for (Category c : dataList){
                    if (c.getId().equals(selectCategoryId)){
                        c.setSelected(true);
                    }else{
                        c.setSelected(false);
                    }
                }
                categoryFilterAdapter.notifyDataSetChanged();

            }
        });

        sortListTask.execute();
    }


}
