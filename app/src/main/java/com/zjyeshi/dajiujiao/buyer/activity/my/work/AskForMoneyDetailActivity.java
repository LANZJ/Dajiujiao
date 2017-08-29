package com.zjyeshi.dajiujiao.buyer.activity.my.work;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
import com.zjyeshi.dajiujiao.buyer.adapter.my.work.LeaveDetailImageAdapter;
import com.zjyeshi.dajiujiao.buyer.common.Constants;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.entity.enums.ApproveEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.work.CostEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.work.ReviewStatusEnum;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.ApproveShowParam;
import com.zjyeshi.dajiujiao.buyer.receiver.work.UpdateReceiver;
import com.zjyeshi.dajiujiao.buyer.task.GetUserInfoTask;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.task.data.UserData;
import com.zjyeshi.dajiujiao.buyer.task.data.order.PathResData;
import com.zjyeshi.dajiujiao.buyer.task.data.other.GetUpLoadFileData;
import com.zjyeshi.dajiujiao.buyer.task.my.UpLoadPhotoTask;
import com.zjyeshi.dajiujiao.buyer.task.work.AddPaidCommentTask;
import com.zjyeshi.dajiujiao.buyer.task.work.ApprovalApplyTask;
import com.zjyeshi.dajiujiao.buyer.task.work.AskMoneyDetailTask;
import com.zjyeshi.dajiujiao.buyer.task.work.GetFeeApplyPathTask;
import com.zjyeshi.dajiujiao.buyer.task.work.RemindApproverTask;
import com.zjyeshi.dajiujiao.buyer.task.work.TransferReviewTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.ApproverListData;
import com.zjyeshi.dajiujiao.buyer.task.work.data.AskMoneyDetailData;
import com.zjyeshi.dajiujiao.buyer.task.work.data.Comment;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.buyer.views.comment.CommentBottomBarView;
import com.zjyeshi.dajiujiao.buyer.views.comment.CommentVolumeView;
import com.zjyeshi.dajiujiao.buyer.views.comment.listener.CommentListener;
import com.zjyeshi.dajiujiao.buyer.views.other.MyGridView;
import com.zjyeshi.dajiujiao.buyer.widgets.PathData;
import com.zjyeshi.dajiujiao.buyer.widgets.PathWidget;

import java.util.ArrayList;
import java.util.List;

/**
 * 费用申请详情
 * Created by wuhk on 2016/6/22.
 */
public class AskForMoneyDetailActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;//标题栏

    @InjectView(R.id.moneyTv)
    private TextView moneyTv;//费用金额
    @InjectView(R.id.typeTv)
    private TextView typeTv;//费用类型
    @InjectView(R.id.bzTv)
    private TextView bzTv;//费用说明

    @InjectView(R.id.spAvatarIv)
    private ImageView spAvatarIv;//审批人头像
    @InjectView(R.id.spNameTv)
    private TextView spNameTv;//审批人姓名
    @InjectView(R.id.listView)
    private BUHighHeightListView listView;//评论列表


    @InjectView(R.id.remindLayout)
    private RelativeLayout remindLayout;//提醒审批人
    @InjectView(R.id.passLayout)
    private RelativeLayout passLayout;//通过
    @InjectView(R.id.refuseLayout)
    private RelativeLayout refuseLayout;//拒绝
    @InjectView(R.id.nextLayout)
    private RelativeLayout nextLayout;//下一级审批
    @InjectView(R.id.commentLayout)
    private RelativeLayout commentLayout;//评论
    @InjectView(R.id.commentBottomBarView)
    private CommentBottomBarView commentBottomBarView;
    @InjectView(R.id.volumeView)
    private CommentVolumeView volumeView;
    @InjectView(R.id.moneyLayout)
    private RelativeLayout moneyLayout;
    @InjectView(R.id.pathWidget)
    private PathWidget pathWidget;
    @InjectView(R.id.gridView)
    private MyGridView gridView;
    @InjectView(R.id.plcte)
    private LinearLayout plcte;

    //请求参数
    private String id;
    private ApproverListData.Approver approver;
    private String remindMemberId;

    private LeaveDetailImageAdapter leaveDetailImageAdapter;
    private List<String> dataList = new ArrayList<String>();

    private CommentListAdapter commentListAdapter;
    private List<Comment> commentList = new ArrayList<Comment>();

    private String url;

    public static final String DETAIL_ID = "id";
    public static final String SHOW_PARAM = "show_param";
    public static final String REVIEW_STATUS = "review_status";
    public static final String MAIN_ID = "main_id";//流水Id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ask_money_detail);
        initWidgets();
    }

    private void  initWidgets(){
        id = getIntent().getStringExtra(DETAIL_ID);
        String showParam = getIntent().getStringExtra(SHOW_PARAM);
        int reviewStatus = getIntent().getIntExtra(REVIEW_STATUS , 0);
        String mainId = getIntent().getStringExtra(MAIN_ID);

        titleLayout.configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).configTitle("申请详情");

        leaveDetailImageAdapter = new LeaveDetailImageAdapter(AskForMoneyDetailActivity.this , dataList);
        gridView.setAdapter(leaveDetailImageAdapter);

        commentListAdapter = new CommentListAdapter(AskForMoneyDetailActivity.this , commentList);
        listView.setAdapter(commentListAdapter);

        loadData();
        configPath(mainId , reviewStatus);

        if (showParam.equals(ApproveShowParam.SHOW_ALL)){
            remindLayout.setVisibility(View.GONE);
        }else if(showParam.equals(ApproveShowParam.SHOW_ONLY_COMMENT)){
            remindLayout.setVisibility(View.GONE);
            passLayout.setVisibility(View.GONE);
            refuseLayout.setVisibility(View.GONE);
            nextLayout.setVisibility(View.GONE);
        }else if (showParam.equals(ApproveShowParam.SHOW_REMIND_AND_COMMENT)){
            passLayout.setVisibility(View.GONE);
            refuseLayout.setVisibility(View.GONE);
            nextLayout.setVisibility(View.GONE);
        }

        //提醒审批人
        remindLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemindApproverTask remindApproverTask = new RemindApproverTask(AskForMoneyDetailActivity.this);
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

                remindApproverTask.execute(remindMemberId , id, "1");
            }
        });

        //通过
        passLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = UrlConstants.APPROVALAPPLY;
                approve();
            }
        });
        //拒绝
        refuseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = UrlConstants.REFUSEAPPLY;
                approve();
            }
        });
        //下一级
        nextLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(AskForMoneyDetailActivity.this , ApproverListActivity.class);
                intent.putExtra(ApproverListActivity.TYPE , ApproveEnum.APPLYMONEY.getValue());
                startActivityForResult(intent , 111);
            }
        });
        //评论
        commentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (commentBottomBarView.getVisibility() == View.VISIBLE){
                    commentBottomBarView.setVisibility(View.GONE);
                }else{
                    commentBottomBarView.setVisibility(View.VISIBLE);
                }
            }
        });

        commentBottomBarView.setCommentListener(volumeView, new CommentListener() {
            @Override
            public void send(String text, String pics, Voice voice) {
                addComment(text , pics , voice);
            }
        });

        if (LoginedUser.getLoginedUser().isMaxLeavel()){
        }else {
            plcte.setVisibility(View.GONE);
            commentLayout.setVisibility(View.GONE);
        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(RESULT_OK != resultCode){
            return;
        }

        switch (requestCode){
            case 111:
                approver = JSON.parseObject(data.getStringExtra("approver") , ApproverListData.Approver.class);
                TransferReviewTask transferReviewTask = new TransferReviewTask(AskForMoneyDetailActivity.this);
                transferReviewTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
                    @Override
                    public void failCallback(Result<NoResultData> result) {
                        ToastUtil.toast(result.getMessage());
                    }
                });

                transferReviewTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
                    @Override
                    public void successCallback(Result<NoResultData> result) {
                        ToastUtil.toast("提交下一级成功");
                        UpdateReceiver.notifyReceiver();
                        finish();
                    }
                });
                transferReviewTask.execute(id , approver.getId() , "1");
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
            default:
                break;
        }
    }

    private void loadData(){
        AskMoneyDetailTask askMoneyDetailTask = new AskMoneyDetailTask(AskForMoneyDetailActivity.this);
        askMoneyDetailTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<AskMoneyDetailData>() {
            @Override
            public void failCallback(Result<AskMoneyDetailData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        askMoneyDetailTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<AskMoneyDetailData>() {
            @Override
            public void successCallback(Result<AskMoneyDetailData> result) {
                AskMoneyDetailData data = result.getValue();
                confidApprover(result.getValue().getApprover());
                remindMemberId = result.getValue().getApprover();
                moneyTv.setText(data.getApplicationMoney());
                if (Validators.isEmpty(data.getApplicationMoney())){
                    moneyLayout.setVisibility(View.GONE);
                }else{
                    moneyLayout.setVisibility(View.VISIBLE);
                }
                typeTv.setText(CostEnum.valueOf(data.getApplicantType()).toString());
                if (!Validators.isEmpty(data.getRemark())){
                    bzTv.setText(data.getRemark());
                }
                dataList.clear();
                if (!Validators.isEmpty(data.getPics())){
                    String[] pics = data.getPics().split(",");
                    for (int i = 0 ; i < pics.length ; i++){
                        dataList.add(pics[i]);
                    }
                    leaveDetailImageAdapter.notifyDataSetChanged();
                }
                commentList.clear();
                commentList.addAll(result.getValue().getComments());
                commentListAdapter.notifyDataSetChanged();
            }
        });

        askMoneyDetailTask.execute(id);
    }


    private void configPath(String id , final int reviewStatus){
        GetFeeApplyPathTask.FeeApplyReq req = new GetFeeApplyPathTask.FeeApplyReq(id , "");
        GetFeeApplyPathTask.getFeePath(AskForMoneyDetailActivity.this, req, new AsyncTaskSuccessCallback<PathResData>() {
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
                        } else if (reviewStatus == ReviewStatusEnum.REFUSE.getValue()) {
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

    //设置审批人信息
    private void confidApprover(String id){
        GetUserInfoTask getUserInfoTask = new GetUserInfoTask(AskForMoneyDetailActivity.this);
        getUserInfoTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<UserData>() {
            @Override
            public void failCallback(Result<UserData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        getUserInfoTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<UserData>() {
            @Override
            public void successCallback(Result<UserData> result) {
                spNameTv.setText(result.getValue().getName());
                GlideImageUtil.glidImage(spAvatarIv , result.getValue().getPic() , R.drawable.default_img);
            }
        });

        getUserInfoTask.execute(id);
    }

    private void approve(){
        ApprovalApplyTask approvalApplyTask = new ApprovalApplyTask(AskForMoneyDetailActivity.this , url);
        approvalApplyTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });
        approvalApplyTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                ToastUtil.toast("成功");
                UpdateReceiver.notifyReceiver();
                finish();
            }
        });
        approvalApplyTask.execute(id , "1");
    }

    //添加评论
    private void addComment(String text , String pics , CommentListener.Voice voice){
        AddPaidCommentTask task = new AddPaidCommentTask(AskForMoneyDetailActivity.this);
        task.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                loadData();
                commentBottomBarView.getInputEt().setText("");
                commentBottomBarView.getInputEt().requestFocus();
                ContextUtils.hideSoftInput(commentBottomBarView.getInputEt());
            }
        });

        task.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        task.execute(id, "", text ,pics , voice);
    }


    /**使用图片评论
     *
     * @param fileName
     * @param isCamera
     */
    private void addCommentWithPic(String fileName , final boolean isCamera){
        UpLoadPhotoTask upLoadPhotoTask = new UpLoadPhotoTask(AskForMoneyDetailActivity.this);
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

    /**启动该Activity
     *
     * @param context
     * @param param
     */
    public static void startActivity(Context context , String id , String param , int reviewStatus , String mainId){
        Intent intent = new Intent();
        intent.setClass(context , AskForMoneyDetailActivity.class);
        intent.putExtra(DETAIL_ID , id);
        intent.putExtra(SHOW_PARAM , param);
        intent.putExtra(REVIEW_STATUS , reviewStatus);
        intent.putExtra(MAIN_ID , mainId);
        context.startActivity(intent);

    }
}
