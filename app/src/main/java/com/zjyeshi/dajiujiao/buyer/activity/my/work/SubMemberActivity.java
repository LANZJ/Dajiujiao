package com.zjyeshi.dajiujiao.buyer.activity.my.work;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.my.work.SubMemberAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.enums.UserEnum;
import com.zjyeshi.dajiujiao.buyer.task.work.SubSetListTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.SubSetListData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 筛选下级界面
 * Created by wuhk on 2016/8/24.
 */
public class SubMemberActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.listView)
    private ListView listView;

    private List<SubSetListData.SubSet> dataList = new ArrayList<SubSetListData.SubSet>();
    private SubMemberAdapter adapter;

    public static final String SUB_SEL_MEMBER = "sub_sel_member";
    public static final String SUB_SEL_MEMBER_ID = "sub_sel_member_id";
    public static final String SUB_SEL_TYPE = "sub_sel_type";
    public static final String SUB_FROM = "sub_from";

    private String name;//这个界面的人员的上级姓名
    private String memberId;//这个界面的人员的上级Id
    private String type;//当前是选择什么级别的，若是选择经销商的，点击item之后进入的选择终端，若是选择终端，点击之后是返回选择终端业务员
    private boolean isOrderFilter;//从前一个界面传过来的，表示从哪里进入的，一次筛选，这个值始终不变

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_subset);
        initWidgets();
    }

    private void initWidgets(){
        name = getIntent().getStringExtra(SUB_SEL_MEMBER);
        memberId = getIntent().getStringExtra(SUB_SEL_MEMBER_ID);
        type = getIntent().getStringExtra(SUB_SEL_TYPE);
        isOrderFilter = getIntent().getBooleanExtra(SUB_FROM , false);

        titleLayout.configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).configTitle(name);


        adapter = new SubMemberAdapter(SubMemberActivity.this , dataList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SubSetListData.SubSet subSet = dataList.get(position);
                if (!Validators.isEmpty(type)){
                    if (type.equals(MemberFilterActivity.SELECT_DEALER)){
                        MemberFilterActivity.startActivity(SubMemberActivity.this , UserEnum.DEALER.getValue()
                                , subSet.getShopName() , subSet.getId() ,  isOrderFilter);
                    }else if(type.equals(MemberFilterActivity.SELECT_TERMINAL)){
                        MemberFilterActivity.startActivity(SubMemberActivity.this , UserEnum.TERMINAL.getValue()
                                , subSet.getShopName() , subSet.getId() ,  isOrderFilter);
                    }else{

                    }
                }
            }
        });
        loadData();

    }

    private void loadData(){
        final SubSetListTask subSetListTask = new SubSetListTask(SubMemberActivity.this);
        subSetListTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<SubSetListData>() {
            @Override
            public void failCallback(Result<SubSetListData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        subSetListTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<SubSetListData>() {
            @Override
            public void successCallback(Result<SubSetListData> result) {
                dataList.clear();
                dataList.addAll(result.getValue().getList());
                adapter.notifyDataSetChanged();
            }
        });

        subSetListTask.execute(memberId);
    }

    public static void startActivity(Context context , String name , String id , String type , boolean isOrderFilter){
        Intent intent = new Intent();
        intent.putExtra(SUB_SEL_MEMBER , name);
        intent.putExtra(SUB_SEL_MEMBER_ID , id);
        intent.putExtra(SUB_SEL_TYPE , type);
        intent.putExtra(SUB_FROM , isOrderFilter);
        intent.setClass(context , SubMemberActivity.class);
        context.startActivity(intent);
    }
}
