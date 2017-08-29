package com.zjyeshi.dajiujiao.buyer.activity.my.work;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jopool.crow.imkit.utils.album.BUAlbum;
import com.jopool.crow.imkit.utils.album.entity.ImageItem;
import com.jopool.crow.imlib.utils.io.FileUtils;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.ContextUtils;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigappleui.lib.view.listview.BUHighHeightListView;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.my.work.CommentListAdapter;
import com.zjyeshi.dajiujiao.buyer.adapter.my.work.PaidListAdapter;
import com.zjyeshi.dajiujiao.buyer.common.Constants;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.entity.enums.ApproveEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.work.ReviewStatusEnum;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.ApproveShowParam;
import com.zjyeshi.dajiujiao.buyer.receiver.work.UpdateReceiver;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.task.data.order.PathResData;
import com.zjyeshi.dajiujiao.buyer.task.data.other.GetUpLoadFileData;
import com.zjyeshi.dajiujiao.buyer.task.my.UpLoadPhotoTask;
import com.zjyeshi.dajiujiao.buyer.task.work.AddPaidCommentTask;
import com.zjyeshi.dajiujiao.buyer.task.work.ApprovalApplyTask;
import com.zjyeshi.dajiujiao.buyer.task.work.CostPaidInfoTask;
import com.zjyeshi.dajiujiao.buyer.task.work.GetFeeApplyPathTask;
import com.zjyeshi.dajiujiao.buyer.task.work.RemindApproverTask;
import com.zjyeshi.dajiujiao.buyer.task.work.TransferReviewTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.ApproverListData;
import com.zjyeshi.dajiujiao.buyer.task.work.data.Comment;
import com.zjyeshi.dajiujiao.buyer.task.work.data.CostPaidInfoData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.buyer.views.comment.CommentBottomBarView;
import com.zjyeshi.dajiujiao.buyer.views.comment.CommentVolumeView;
import com.zjyeshi.dajiujiao.buyer.views.comment.listener.CommentListener;
import com.zjyeshi.dajiujiao.buyer.widgets.PathData;
import com.zjyeshi.dajiujiao.buyer.widgets.PathWidget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 费用报销详情
 * Created by wuhk on 2016/6/23.
 */
public class ApproveDetailActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;

    @InjectView(R.id.bxTitleTv)
    private TextView bxTitleTv;
    @InjectView(R.id.spPersonTv)
    private TextView spPersonTv;
    @InjectView(R.id.timeTv)
    private TextView timeTv;
    @InjectView(R.id.priceTv)
    private TextView priceTv;
    @InjectView(R.id.pathWidget)
    private PathWidget pathWidget;
    @InjectView(R.id.contentListView)
    private BUHighHeightListView contentListView;
    @InjectView(R.id.commentListView)
    private BUHighHeightListView commentListView;
    @InjectView(R.id.passLayout)
    private RelativeLayout passLayout;
    @InjectView(R.id.refuseLayout)
    private RelativeLayout refuseLayout;
    @InjectView(R.id.nextLayout)
    private RelativeLayout nextLayout;
    @InjectView(R.id.commentLayout)
    private RelativeLayout commentLayout;
    @InjectView(R.id.noticeLayout)
    private RelativeLayout noticeLayout;
    @InjectView(R.id.commentBottomView)
    private CommentBottomBarView commentBottomView;
    @InjectView(R.id.volumeView)
    private CommentVolumeView volumeView;
    @InjectView(R.id.linedet)
    private LinearLayout linedet;
    @InjectView(R.id.fourLayout)
    private LinearLayout fourLayout;

    private String id;
    private List<CostPaidInfoData.Paid> paidList;
    private PaidListAdapter paidAdapter;
    private List<Comment> commentList;
    private CommentListAdapter commentListAdapter;
    private String paidCommentId=null;

    private String remindMemberId;

    public static final String DETAIL_ID = "id";
    public static final String SHOW_PARAM = "show_param";
    public static final String REVIEW_STATUS = "review_status";
    public static final String MAIN_ID = "main_id";

    private ApproverListData.Approver approver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_approve_detail);

        initWidgets();
    }

    private void initWidgets() {
        id = getIntent().getStringExtra(DETAIL_ID);
        int reviewStatus = getIntent().getIntExtra(REVIEW_STATUS, 0);
        String mainId = getIntent().getStringExtra(MAIN_ID);

        titleLayout.configTitle("报销审批").configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        paidList = new ArrayList<>();
        paidAdapter = new PaidListAdapter(ApproveDetailActivity.this, paidList);
        contentListView.setAdapter(paidAdapter);

        commentList = new ArrayList<>();
        commentListAdapter = new CommentListAdapter(ApproveDetailActivity.this, commentList);
        commentListView.setAdapter(commentListAdapter);

        commentBottomView.setCommentListener(volumeView, new CommentListener() {
            @Override
            public void send(String text, String pics, Voice voice) {
                addComment(text, pics, voice);
            }
        });


        String showParam = getIntent().getStringExtra(SHOW_PARAM);
        if (showParam.equals(ApproveShowParam.SHOW_ONLY_COMMENT)) {
            noticeLayout.setVisibility(View.GONE);
            passLayout.setVisibility(View.GONE);
            refuseLayout.setVisibility(View.GONE);
            nextLayout.setVisibility(View.GONE);
        } else if (showParam.equals(ApproveShowParam.SHOW_REMIND_AND_COMMENT)) {
            passLayout.setVisibility(View.GONE);
            refuseLayout.setVisibility(View.GONE);
            nextLayout.setVisibility(View.GONE);
        } else if (showParam.equals(ApproveShowParam.SHOW_ALL)) {
            noticeLayout.setVisibility(View.GONE);
        }

        //通过
        passLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApprovalApplyTask task = new ApprovalApplyTask(ApproveDetailActivity.this, UrlConstants.APPROVALAPPLY);
                task.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
                    @Override
                    public void successCallback(Result<NoResultData> result) {
                        UpdateReceiver.notifyReceiver();
                        finish();
                    }
                });

                task.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
                    @Override
                    public void failCallback(Result<NoResultData> result) {
                        UpdateReceiver.notifyReceiver();
                        ToastUtil.toast(result.getMessage());
                    }
                });

                //费用报销
                task.execute(id, "2");
            }
        });
        //拒绝
        refuseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApprovalApplyTask task = new ApprovalApplyTask(ApproveDetailActivity.this, UrlConstants.REFUSEAPPLY);
                task.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
                    @Override
                    public void successCallback(Result<NoResultData> result) {
                        UpdateReceiver.notifyReceiver();
                        finish();
                    }
                });

                task.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
                    @Override
                    public void failCallback(Result<NoResultData> result) {
                        UpdateReceiver.notifyReceiver();
                        ToastUtil.toast(result.getMessage());
                    }
                });

                //费用报销
                task.execute(id, "2");
            }
        });
        //下一级审批
        nextLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ApproveDetailActivity.this, ApproverListActivity.class);
                intent.putExtra(ApproverListActivity.TYPE, ApproveEnum.BXMONEY.getValue());
                startActivityForResult(intent, 111);
            }
        });
        //评论
        commentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commentBottomView.getVisibility() == View.VISIBLE) {
                    commentBottomView.setVisibility(View.GONE);
                } else {
                    commentBottomView.setVisibility(View.VISIBLE);
                }
            }
        });
        //自己查看时的提醒审批人
        noticeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemindApproverTask remindApproverTask = new RemindApproverTask(ApproveDetailActivity.this);
                remindApproverTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
                    @Override
                    public void failCallback(Result<NoResultData> result) {
                        ToastUtil.toast(result.getMessage());
                    }
                });

                remindApproverTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
                    @Override
                    public void successCallback(Result<NoResultData> result) {
                        ToastUtil.toast("提醒成功");
                    }
                });

                remindApproverTask.execute(remindMemberId, id, "2");
            }
        });

        if (LoginedUser.getLoginedUser().isMaxLeavel()){
        }else {
                fourLayout.setVisibility(View.GONE);
                linedet.setVisibility(View.GONE);
    }

            getData();
            configFeePath(mainId, reviewStatus);
        }

    private void getData(){
        paidList.clear();
        commentList.clear();

        CostPaidInfoTask task = new CostPaidInfoTask(ApproveDetailActivity.this);
        task.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<CostPaidInfoData>() {
            @Override
            public void successCallback(Result<CostPaidInfoData> result) {
                CostPaidInfoData data = result.getValue();
                initTextView(bxTitleTv,data.getName());
                initTextView(spPersonTv,"申请人："+data.getApplication());
                if (Validators.isEmpty(data.getPaidMoney())){
                    priceTv.setVisibility(View.GONE);
                }else{
                    priceTv.setVisibility(View.VISIBLE);
                    initTextView(priceTv,"¥"+data.getPaidMoney());
                }
                remindMemberId = data.getApprover();
                paidList.addAll(data.getPaids());
                paidAdapter.notifyDataSetChanged();

                commentList.addAll(data.getComments());
                Collections.sort(commentList, new Comparator<Comment>() {
                    @Override
                    public int compare(Comment lhs, Comment rhs) {
                        if(lhs.getCreationTime()>rhs.getCreationTime()){
                            return -1;
                        }else {
                            return 1;
                        }
                    }
                });
                commentListAdapter.notifyDataSetChanged();
            }
        });

        task.setAsyncTaskFailCallback(new AsyncTaskFailCallback<CostPaidInfoData>() {
            @Override
            public void failCallback(Result<CostPaidInfoData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        task.execute(id);
    }

    private void configFeePath(String id , final int reviewStatus){
        GetFeeApplyPathTask.FeeApplyReq req = new GetFeeApplyPathTask.FeeApplyReq("" , id);
        GetFeeApplyPathTask.getFeePath(ApproveDetailActivity.this, req, new AsyncTaskSuccessCallback<PathResData>() {
            @Override
            public void successCallback(Result<PathResData> result) {
                List<PathResData.Path> tempList = result.getValue().getList();
                List<PathData> pathDataList = new ArrayList<PathData>();
                for (int i = 0; i < tempList.size(); i++) {
                    PathResData.Path orderPath = tempList.get(i);
                    PathData pathData = new PathData();
                    pathData.setAvatar(orderPath.getPic());
                    pathData.setName(orderPath.getApplicate());
                    pathData.setCreationiTime(orderPath.getCreationTime());
                    if (i == tempList.size() - 1) {
                        if (reviewStatus == ReviewStatusEnum.PASS.getValue()) {
                            pathData.setShowCheck(true);
                        } else if (reviewStatus == ReviewStatusEnum.REFUSE.getValue()){
                            pathData.setShowCheck(true);
                            pathData.setRefused(true);
                        }else{
                            pathData.setShowCheck(false);
                        }
                    } else {
                        pathData.setShowCheck(true);
                    }
                    pathDataList.add(pathData);
                }
                pathWidget.bindData(pathDataList);
                pathWidget.getPathDesTv().setText("审批流程");
                pathWidget.getPathDesTv().setBackgroundResource(R.color.color_divider);
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK != resultCode){
            return;
        }
        switch (requestCode){
            case 111:
                approver = JSON.parseObject(data.getStringExtra("approver") , ApproverListData.Approver.class);
                TransferReviewTask transferReviewTask = new TransferReviewTask(ApproveDetailActivity.this);
                transferReviewTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
                    @Override
                    public void failCallback(Result<NoResultData> result) {
                        ToastUtil.toast(result.getMessage());
                    }
                });

                transferReviewTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
                    @Override
                    public void successCallback(Result<NoResultData> result) {
                        UpdateReceiver.notifyReceiver();
                        ToastUtil.toast("提交下一级成功");
                        finish();
                    }
                });
                transferReviewTask.execute(id , approver.getId() , "2");
                break;
            case CommentBottomBarView.COMMENT_BOTTOM_BAR_BUALUM:
                List<ImageItem> imageItemList = BUAlbum.getSelListAndClear();
                if (Validators.isEmpty(imageItemList)) {
                    ToastUtil.toast("图片选择出现问题，请重新选择");
                }else{
                    ImageItem selectedItem = imageItemList.get(0);

                    addCommentWithPic(selectedItem.imagePath , false);
                }
                break;
            case CommentBottomBarView.COMMENT_BOTTOM_BAR_CAMERA:
                addCommentWithPic(Constants.SDCARD_DJJBUYER_COMMENT_TEMP_CAMREA , true);
                break;
        }
    }


    /**添加评论
     *
     * @param text
     * @param pics
     * @param voice
     */
    private void addComment(String text , String pics , CommentListener.Voice voice){
        AddPaidCommentTask task = new AddPaidCommentTask(ApproveDetailActivity.this);
        task.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                getData();
                commentBottomView.getInputEt().setText("");
                commentBottomView.getInputEt().requestFocus();
                ContextUtils.hideSoftInput(commentBottomView.getInputEt());
            }
        });

        task.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        task.execute(id,paidCommentId,text , pics ,voice);
    }

    /**使用图片评论
     *
     * @param fileName
     * @param isCamera
     */
    private void addCommentWithPic(String fileName , final boolean isCamera){
        UpLoadPhotoTask upLoadPhotoTask = new UpLoadPhotoTask(ApproveDetailActivity.this);
        upLoadPhotoTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<GetUpLoadFileData>() {
            @Override
            public void failCallback(Result<GetUpLoadFileData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        upLoadPhotoTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<GetUpLoadFileData>() {
            @Override
            public void successCallback(Result<GetUpLoadFileData> result) {
                String pics = result.getValue().getPath();
                if (isCamera){
                    FileUtils.deleteFileOrDirectoryQuietly(Constants.SDCARD_DJJBUYER_COMMENT_TEMP_CAMREA);// 处理完了了就删除
                }
                addComment("" , pics , new CommentListener.Voice());
            }
        });

        upLoadPhotoTask.execute(fileName);
    }

    public static void startActivity(Context context , String id , String param , int reviewStatus , String mainId){

        Intent intent = new Intent();
        intent.putExtra(DETAIL_ID , id);
        intent.putExtra(SHOW_PARAM , param);
        intent.putExtra(REVIEW_STATUS , reviewStatus);
        intent.putExtra(MAIN_ID , mainId);
        intent.setClass(context , ApproveDetailActivity.class);
        context.startActivity(intent);
    }
}
