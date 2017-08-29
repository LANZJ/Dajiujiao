package com.zjyeshi.dajiujiao.buyer.activity.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.pinyin.PinyinUtil;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.activity.sales.RebackDetailActivity;
import com.zjyeshi.dajiujiao.buyer.receiver.sales.RefreshRebackManagerReceiver;
import com.zjyeshi.dajiujiao.buyer.task.order.OrderNextTask;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.my.work.YwyAdapter;
import com.zjyeshi.dajiujiao.buyer.receiver.order.ChangeOrderStatusReceiver;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.task.sales.RebackChangeReviewMemberTask;
import com.zjyeshi.dajiujiao.buyer.task.work.MaxLevelListTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.SalemanListData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 超管列表
 * Created by wuhk on 2016/9/30.
 */
public class MaxLevelManActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;//标题栏
    @InjectView(R.id.searchEt)
    private EditText searchEt;//搜索
    @InjectView(R.id.hintLayout)
    private RelativeLayout hintLayout;//搜索默认

    @InjectView(R.id.listView)
    private ListView listView;//员工列表
    private List<SalemanListData.Saleman> dataLists;
    private List<SalemanListData.Saleman> dataBaseLists;
    private List<SalemanListData.Saleman> dataSearchLists;
    private YwyAdapter adapter;

    private static final String PARAM_CHANGE_REVIEW_ID = "param.change_review_id";
    private static final String PARAM_TYPE = "param.type";

    public static final String TYPE_ORDER = "type.order";
    public static final String TYPE_REBACK = "type.reback";

    private String changeReviewId= "";
    private String type = "";

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

        changeReviewId = getIntent().getStringExtra(PARAM_CHANGE_REVIEW_ID);
        type = getIntent().getStringExtra(PARAM_TYPE);

        searchEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    hintLayout.setVisibility(View.GONE);
                }else {
                    hintLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                dataSearchLists.clear();
                if (s.length()>0){
                    for (SalemanListData.Saleman saleman : dataBaseLists){
                        if (PinyinUtil.toPinyin(saleman.getName()).startsWith(PinyinUtil.toPinyin(s.toString()))){
                            dataSearchLists.add(saleman);
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
        adapter = new YwyAdapter(MaxLevelManActivity.this , dataLists);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SalemanListData.Saleman saleman = dataLists.get(position);
                if (type.equals(TYPE_ORDER)){
                    orderNext(saleman.getMemberId());
                }else if (type.equals(TYPE_REBACK)){
                    rebackNext(saleman.getMemberId());
                }
            }
        });

        initDatas();
    }

    private void initDatas(){
        dataLists.clear();
        dataBaseLists.clear();
        dataSearchLists.clear();

        MaxLevelListTask maxLevelListTask = new MaxLevelListTask(MaxLevelManActivity.this);
        maxLevelListTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<SalemanListData>() {
            @Override
            public void failCallback(Result<SalemanListData> result) {

            }
        });

        maxLevelListTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<SalemanListData>() {
            @Override
            public void successCallback(Result<SalemanListData> result) {
                for (SalemanListData.Saleman saleman  : result.getValue().getList()){
                    dataLists.add(saleman);
                }

                dataBaseLists.addAll(dataLists);
                adapter.notifyDataSetChanged();
            }
        });

        maxLevelListTask.execute();
    }

    private void orderNext(String memberId){
        OrderNextTask orderNextTask = new OrderNextTask(MaxLevelManActivity.this);
        orderNextTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        orderNextTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                ToastUtil.toast("转审成功");
                ChangeOrderStatusReceiver.notifyReceiver();
                finish();
            }
        });

        orderNextTask.execute(changeReviewId , memberId);
    }

    private void rebackNext(String memberId){
        RebackChangeReviewMemberTask.changeRebackReview(MaxLevelManActivity.this, changeReviewId, memberId, new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                ToastUtil.toast("转审成功");
                RebackDetailActivity.reload = true;
                RefreshRebackManagerReceiver.notifyReceiver();
                finish();
            }
        });
    }


    public static void startMaxLevelManActivity(Context context , String changeReviewId , String type){
        Intent intent = new Intent();
        intent.setClass(context , MaxLevelManActivity.class);
        intent.putExtra(PARAM_CHANGE_REVIEW_ID , changeReviewId);
        intent.putExtra(PARAM_TYPE , type);
        context.startActivity(intent);
    }
}
