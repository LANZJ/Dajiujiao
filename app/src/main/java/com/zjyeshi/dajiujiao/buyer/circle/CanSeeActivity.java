package com.zjyeshi.dajiujiao.buyer.circle;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.sharepreference.BPPreferences;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.my.work.CategoryFilterAdapter;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.circle.receiver.OnlyRefrshReceiver;
import com.zjyeshi.dajiujiao.buyer.circle.task.ChangeDynamicJurTask;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.task.data.store.sort.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * 可见设置
 * Created by wuhk on 2016/8/15.
 */
public class CanSeeActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.listView)
    private ListView listView;
    private CategoryFilterAdapter adapter;
    private List<Category> strList = new ArrayList<Category>();
    private String type;
    public static String CIRCLETYPE = "circle_type";
    private String SEE_ALL = "1";
    private String SEE_FRIEND = "2";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_can_see);
        initWidgets();
    }

    private void initWidgets(){
        //默认显示全部
        type = BPPreferences.instance().getString(CIRCLETYPE , "1");
        titleLayout.configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).configTitle("设置可见").configRightText("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Category category : strList){
                    if (category.isSelected()){
                        type = category.getId();
                        break;
                    }
                }
                changeShow();

            }
        });

        strList.clear();
        Category category1 = new Category();
        category1.setId(SEE_ALL);
        category1.setName("显示所有人动态");
        if (type.equals(SEE_ALL)){
            category1.setSelected(true);
        }else{
            category1.setSelected(false);
        }
        strList.add(category1);

        Category category2 = new Category();
        category2.setId(SEE_FRIEND);
        category2.setName("只看酒友的动态");
        if (type.equals(SEE_FRIEND)){
            category2.setSelected(true);
        }else{
            category2.setSelected(false);
        }
        strList.add(category2);

        adapter = new CategoryFilterAdapter(CanSeeActivity.this , strList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Category category = strList.get(position);
                category.setSelected(true);
                for (Category data : strList){
                    if (!data.getId().equals(category.getId())){
                        data.setSelected(false);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    /**改变酒友圈显示权限并刷新酒友圈
     *
     */
    private void changeShow(){
        ChangeDynamicJurTask changeDynamicJurTask = new ChangeDynamicJurTask(CanSeeActivity.this);
        changeDynamicJurTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        changeDynamicJurTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                BPPreferences.instance().putString(CIRCLETYPE , type);
                OnlyRefrshReceiver.notifyReceiver();
                finish();
            }
        });

        changeDynamicJurTask.execute(type);
    }
}
