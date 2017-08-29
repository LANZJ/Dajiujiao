package com.zjyeshi.dajiujiao.buyer.activity.store;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.DateUtils;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigappleui.lib.view.listview.BUPullToRefreshListView;
import com.xuan.bigappleui.lib.view.listview.BUPullToRefreshListViewListener;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.store.GoodsDetailCommentAdapter;
import com.zjyeshi.dajiujiao.buyer.circle.itementity.BaseEntity;
import com.zjyeshi.dajiujiao.buyer.entity.enums.LtGtEnum;
import com.zjyeshi.dajiujiao.buyer.entity.good.AllGoodInfo;
import com.zjyeshi.dajiujiao.buyer.entity.good.GoodCommentEntity;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.entity.good.GoodInfoEntity;
import com.zjyeshi.dajiujiao.buyer.task.data.store.goods.GoodCommentList;
import com.zjyeshi.dajiujiao.buyer.task.store.ListEvaluateTask;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wuhk on 2016/4/7.
 */
public class GoodsDetailCommentActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.listView)
    private BUPullToRefreshListView listView;

    private AllGoodInfo allGoodInfo;
    public static final String ALLGOODINFO = "allGoodInfo";

    private List<BaseEntity> dataList = new ArrayList<BaseEntity>();
    private GoodsDetailCommentAdapter goodsDetailCommentAdapter;

    private String lastTime = "0";
    private String mode = LtGtEnum.LT.getValueStr();

    private boolean isPullDownRefresh = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_goods_detail);
        allGoodInfo = (AllGoodInfo)getIntent().getSerializableExtra(ALLGOODINFO);
        //添加第一个Item
        addFirstItem(allGoodInfo);

        titleLayout.configReturn("返回", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).configTitle("商品详情");

        //初始化listView
        listView.setCanScrollUp(true);
        listView.setCanPullDown(true);
        listView.setPullToRefreshListViewListener(new BUPullToRefreshListViewListener() {
            @Override
            public void onPullDownRefresh() {
                //下拉
                isPullDownRefresh = true;

                if (dataList.size() == 1){
                    lastTime = "0";
                }else{
                    lastTime =  ((GoodCommentEntity)dataList.get(1)).getCreateTime();
                }
                mode = LtGtEnum.GT.getValueStr();
                loadCommmentList();
            }

            @Override
            public void onScrollUpRefresh() {
                //上滑
                isPullDownRefresh = false;
                if (dataList.size() == 1){
                    lastTime = "0";
                }else{
                    lastTime =  ((GoodCommentEntity)dataList.get(dataList.size()-1)).getCreateTime();
                }
                mode = LtGtEnum.LT.getValueStr();
                loadCommmentList();
            }
        });

        goodsDetailCommentAdapter = new GoodsDetailCommentAdapter(GoodsDetailCommentActivity.this , dataList);
        listView.setAdapter(goodsDetailCommentAdapter);

        loadCommmentList();
    }

    /**获取商品评价列表
     *
     */
    private void loadCommmentList(){
        ListEvaluateTask listEvaluateTask = new ListEvaluateTask(GoodsDetailCommentActivity.this);
        listEvaluateTask.setShowProgressDialog(false);
        listEvaluateTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<GoodCommentList>() {
            @Override
            public void failCallback(Result<GoodCommentList> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        listEvaluateTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<GoodCommentList>() {
            @Override
            public void successCallback(Result<GoodCommentList> result) {
                if (isPullDownRefresh){
                    dataList.addAll(1 , getCommentList(result.getValue().getList()));
                    listView.onPullDownRefreshComplete("最新更新" + DateUtils.date2StringBySecond(new Date()));
                    //重新开启上拉更多
                    listView.setCanScrollUp(false);
                    listView.setCanScrollUp(true);
                }else {
                    dataList.addAll(getCommentList(result.getValue().getList()));
                    listView.onScrollUpRefreshComplete("上滑更多");
                    if (Validators.isEmpty(result.getValue().getList())) {
                        listView.onScrollUpRefreshComplete("");
                        listView.onScrollUpNoMoreData("没有更多数据了");
                    }
                }

                goodsDetailCommentAdapter.notifyDataSetChanged();
            }
        });
        listEvaluateTask.execute(allGoodInfo.getGoodId(), lastTime, mode);
    }


    private void addFirstItem(AllGoodInfo allGoodInfo){
        GoodInfoEntity goodInfoEntity = new GoodInfoEntity();
        goodInfoEntity.setBottlesPerBox(allGoodInfo.getBottlesPerBox());
        goodInfoEntity.setDescription(allGoodInfo.getDescription());
        goodInfoEntity.setFormat(allGoodInfo.getFormat());
        goodInfoEntity.setGoodCount(allGoodInfo.getGoodCount());
        goodInfoEntity.setGoodIcon(allGoodInfo.getGoodIcon());
        goodInfoEntity.setGoodId(allGoodInfo.getGoodId());
        goodInfoEntity.setGoodName(allGoodInfo.getGoodName());
        goodInfoEntity.setGoodPrice(allGoodInfo.getGoodPrice());
        goodInfoEntity.setGoodType(allGoodInfo.getGoodType());
        goodInfoEntity.setInvertory(allGoodInfo.getInvertory());
        goodInfoEntity.setShopId(allGoodInfo.getShopId());
        goodInfoEntity.setShopName(allGoodInfo.getShopName());
        goodInfoEntity.setUnit(allGoodInfo.getUnit());
        goodInfoEntity.setUpPrice(allGoodInfo.getUpPrice());
        dataList.add(goodInfoEntity);
    }

    private List<GoodCommentEntity> getCommentList(List<GoodCommentList.GoodComment> commentList){
        List<GoodCommentEntity> resultList = new ArrayList<GoodCommentEntity>();
        if (!Validators.isEmpty(commentList)){
            for (GoodCommentList.GoodComment comment : commentList) {
                GoodCommentEntity goodComment = new GoodCommentEntity();
                goodComment.setContent(comment.getContent());
                goodComment.setCreateTime(String.valueOf(comment.getCreateTime()));
                goodComment.setId(comment.getId());
                goodComment.setLevel(comment.getLevel());
                goodComment.setMemberName(comment.getMemberName());
                goodComment.setOrderNum(comment.getOrderNum());
                goodComment.setPic(comment.getPic());
                resultList.add(goodComment);
            }
        }

        return resultList;
    }

    public static void startActivity(Context context , AllGoodInfo allGoodInfo){
        Intent intent = new Intent();
        intent.setClass(context , GoodsDetailCommentActivity.class);
        intent.putExtra(ALLGOODINFO , (Serializable)allGoodInfo);
        context.startActivity(intent);

    }
}
