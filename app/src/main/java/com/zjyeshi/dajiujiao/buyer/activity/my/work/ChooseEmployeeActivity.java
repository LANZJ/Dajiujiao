package com.zjyeshi.dajiujiao.buyer.activity.my.work;

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
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.my.work.ChooseEmployeeAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.Employee;
import com.zjyeshi.dajiujiao.buyer.task.work.StaffListTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.StaffListData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.buyer.widgets.search.SearchWidget;
import com.zjyeshi.dajiujiao.buyer.widgets.search.callback.SearchCallback;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;

/**
 * 选择员工
 *
 * Created by zhum on 2016/6/14.
 */
public class ChooseEmployeeActivity extends BaseActivity {

    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;//标题栏
    @InjectView(R.id.searchWidget)
    private SearchWidget searchWidget;

    @InjectView(R.id.listView)
    private ListView listView;//员工列表
    private List<Employee> dataLists;
    private List<Employee> dataBaseLists;
    private List<Employee> dataSearchLists;
    private ChooseEmployeeAdapter adapter;
    private boolean isKq;
    private boolean bossView;
    private boolean iswr;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_choose_employee);

        initWidgets();
    }

    private void initWidgets(){
        titleLayout.configTitle("选择人员").configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        isKq = getIntent().getBooleanExtra("isKq" , false);
        bossView = getIntent().getBooleanExtra("bossView" , false);
        iswr=getIntent().getBooleanExtra("iswr" , false);

        searchWidget.sethintDes("搜索");
        searchWidget.configSearchCallback(new SearchCallback() {
            @Override
            public void search(String content) {
                dataSearchLists.clear();
                if (content.length()>0){
                    for (Employee employee : dataBaseLists){
                        if (employee.getName().contains(content)){
                            dataSearchLists.add(employee);
                        }
                    }
                    dataLists.clear();
                    dataLists.addAll(dataSearchLists);
                    adapter.notifyDataSetChanged();
                }else {
                    dataLists.clear();
                    dataLists.addAll(dataBaseLists);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        dataLists = new ArrayList<>();
        dataBaseLists = new ArrayList<>();
        dataSearchLists = new ArrayList<>();
        adapter = new ChooseEmployeeAdapter(ChooseEmployeeActivity.this,dataLists,false);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Employee employee = dataLists.get(position);
                if (isKq){
                    //从考勤进来的选择员工
                    AttendanceRecordActivity.isReload = true;
                    AttendanceRecordActivity.memberId = employee.getId();
                    finish();
                }else if (bossView){
                    //boss查看的
                    Intent intent = new Intent(ChooseEmployeeActivity.this,AttendanceRecordActivity.class);
                    intent.putExtra("bossView" , true);
                    intent.putExtra("memberId" , employee.getId());
                    startActivity(intent);
                }else if (iswr){
//                    SharedPreferences preferences=getSharedPreferences("us", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor=preferences.edit();
//                    editor.putString("membername", employee.getName());
//                    editor.putString("memberId", employee.getId());
//                    editor.commit();
                    Intent intent = new Intent(ChooseEmployeeActivity.this,WriteDateReportActivity.class);
                    WriteDateReportActivity.mannan=employee.getName();
                    WriteDateReportActivity.manid=employee.getId();
                    startActivity(intent);
                    finish();
                }
                else {
                    //从联系进来的选择员工，点击聊天
                 //   ChatManager.getInstance().startConversion(ChooseEmployeeActivity.this, employee.getId());
                    RongIM.getInstance().startPrivateChat(ChooseEmployeeActivity.this,employee.getId(), employee.getName());
                }
            }
        });

        initDatas();
    }

    private void initDatas(){
        dataLists.clear();
        dataBaseLists.clear();
        dataSearchLists.clear();

        StaffListTask staffListTask = new StaffListTask(ChooseEmployeeActivity.this);
        staffListTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<StaffListData>() {
            @Override
            public void failCallback(Result<StaffListData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        staffListTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<StaffListData>() {
            @Override
            public void successCallback(Result<StaffListData> result) {
                for (StaffListData.Staff staff  : result.getValue().getList()){
                    Employee employee = new Employee();
                    employee.setId(staff.getId());
                    employee.setPic(staff.getPic());
                    employee.setName(staff.getName());
                    dataLists.add(employee);
                }

                dataBaseLists.addAll(dataLists);
                adapter.notifyDataSetChanged();
            }
        });

        staffListTask.execute();
    }
}