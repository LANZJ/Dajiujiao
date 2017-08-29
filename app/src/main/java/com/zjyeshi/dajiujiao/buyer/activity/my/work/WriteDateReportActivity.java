package com.zjyeshi.dajiujiao.buyer.activity.my.work;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import com.xuan.bigapple.lib.utils.DateUtils;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigappleui.lib.view.listview.BUHighHeightListView;
import com.xuan.bigappleui.lib.view.scrollview.BUVerticalScrollView;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.my.work.CommentListAdapter;
import com.zjyeshi.dajiujiao.buyer.adapter.my.work.DateRemarkListAdapter;
import com.zjyeshi.dajiujiao.buyer.common.Constants;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.DateRemark;
import com.zjyeshi.dajiujiao.buyer.receiver.LoadNewRemindReceiver;
import com.zjyeshi.dajiujiao.buyer.receiver.work.UpdateReceiver;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.task.data.other.GetUpLoadFileData;
import com.zjyeshi.dajiujiao.buyer.task.my.UpLoadPhotoTask;
import com.zjyeshi.dajiujiao.buyer.task.work.AddDailyCommentTask;
import com.zjyeshi.dajiujiao.buyer.task.work.AddDailyTask;
import com.zjyeshi.dajiujiao.buyer.task.work.DailyCommentTask;
import com.zjyeshi.dajiujiao.buyer.task.work.DailyInfoTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.Comment;
import com.zjyeshi.dajiujiao.buyer.task.work.data.DailyCommentInfo;
import com.zjyeshi.dajiujiao.buyer.task.work.data.DailyInfoData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.buyer.views.comment.CommentBottomBarView;
import com.zjyeshi.dajiujiao.buyer.views.comment.CommentVolumeView;
import com.zjyeshi.dajiujiao.buyer.views.comment.listener.CommentListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 填写/查看日报
 *
 * Created by zhum on 2016/6/17.
 */
public class WriteDateReportActivity extends BaseActivity {

    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;//标题栏

    @InjectView(R.id.timeTv)
    private TextView timeTv;//时间

    @InjectView(R.id.xcEt)
    private EditText xcEt;//今日行程
    @InjectView(R.id.xjEt)
    private EditText xjEt;//今日小结
    @InjectView(R.id.fyEt)
    private EditText fyEt;//今日费用
    @InjectView(R.id.jhEt)
    private EditText jhEt;//明日计划

    @InjectView(R.id.remarkListView)
    private BUHighHeightListView remarkListView;//备注列表
    @InjectView(R.id.listView)
    private BUHighHeightListView listView;//评论列表

    @InjectView(R.id.scrollView)
    private BUVerticalScrollView scrollView;
    //评论
    @InjectView(R.id.listLayout)
    private LinearLayout listLayout;
    @InjectView(R.id.commentBottomView)
    private CommentBottomBarView commentBottomView;
    @InjectView(R.id.volumeView)
    private CommentVolumeView volumeView;

    public static final String WRITE = "1";
    public static final String READ = "2";
    //备注列表
    private DateRemarkListAdapter dateRemarkListAdapter;
    private List<DateRemark> dateRemarkList = new ArrayList<DateRemark>();
    //抄送
  @InjectView(R.id.cse)
  private RelativeLayout cse;
    @InjectView(R.id.csman)
    private TextView csman;
    //评论列表
    private CommentListAdapter dailyCommentAdapter;
    private List<Comment> dataList = new ArrayList<Comment>();

    private String type;
    //上传参数
    private String dailyId = "";
    private String dailyCommentId = "";
    public static String manid="";
    public static String mannan="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_write_date_report);
        type = getIntent().getStringExtra("type");
        dailyId = getIntent().getStringExtra("id");
        if (!type.equals("")){
            cse.setVisibility(View.GONE);
        }
        initWidgets();
    }

    private void initWidgets(){
       // cse.setVisibility(View.GONE);
        dateRemarkListAdapter = new DateRemarkListAdapter(WriteDateReportActivity.this , dateRemarkList);
        remarkListView.setAdapter(dateRemarkListAdapter);

        if (type.equals(WRITE)){

            //隐藏评论列表和回复控件
//            commentBottomView.setVisibility(View.GONE);
            listLayout.setVisibility(View.GONE);

            titleLayout.configTitle("填写日报").configReturn(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            commentBottomView.setCommentListener(volumeView, new CommentListener() {
                @Override
                public void send(String text, String pics, Voice voice) {
                    DateRemark dateRemark = new DateRemark();
                    dateRemark.setPics(pics);
                    dateRemark.setContent(text);
                    dateRemark.setVoice(voice.getVoiceUrl());
                    dateRemark.setVoiceLength(voice.getVoiceLength());
                    dateRemarkList.add(dateRemark);
                    dateRemarkListAdapter.notifyDataSetChanged();
                    commentBottomView.getInputEt().setText("");
                    commentBottomView.getInputEt().requestFocus();
                    ContextUtils.hideSoftInput(commentBottomView.getInputEt());
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });

            timeTv.setText(DateUtils.currentDate2StringByDay());
            titleLayout.configRightText("完成", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String recordTime = DateUtils.currentDate2StringByDay();
                    String trip = xcEt.getText().toString();
                    String summary = xjEt.getText().toString();
                    String todayCost = fyEt.getText().toString();
                    String tomorrowPlan = jhEt.getText().toString();

                    if (Validators.isEmpty(trip)){
                        ToastUtil.toast("今日行程不能为空");
                        return;
                    }

                    if (Validators.isEmpty(summary)){
                        ToastUtil.toast("今日小结不能为空");
                        return;
                    }

                    if (Validators.isEmpty(todayCost)){
                        ToastUtil.toast("今日费用不能为空");
                        return;
                    }

                    if (Validators.isEmpty(tomorrowPlan)){
                        ToastUtil.toast("明日计划不能为空");
                        return;
                    }
                    String supInfos = JSON.toJSONString(dateRemarkList);
                    AddDailyTask addDailyTask = new AddDailyTask(WriteDateReportActivity.this);
                    addDailyTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
                        @Override
                        public void successCallback(Result<NoResultData> result) {
                            UpdateReceiver.notifyReceiver();
                            manid="";
                            mannan="";
                            finish();
                        }
                    });

                    addDailyTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
                        @Override
                        public void failCallback(Result<NoResultData> result) {
                            ToastUtil.toast(result.getMessage());
                        }
                    });

                    addDailyTask.execute(recordTime,trip,summary,todayCost,tomorrowPlan,supInfos,manid);
                }
            });
        }else {
            xcEt.setEnabled(false);
            xjEt.setEnabled(false);
            fyEt.setEnabled(false);
            jhEt.setEnabled(false);

            //显示评论列表和回复控件
            commentBottomView.setVisibility(View.VISIBLE);
            listLayout.setVisibility(View.VISIBLE);

            titleLayout.configTitle("工作日报").configReturn(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            dailyCommentAdapter = new CommentListAdapter(WriteDateReportActivity.this , dataList);
            listView.setAdapter(dailyCommentAdapter);

            initDiary();
            initComment(false);

            commentBottomView.setCommentListener(volumeView , new CommentListener(){
                @Override
                public void send(String text, String pics, Voice voice) {
                    addComment(text , pics , voice);
                }
            });
        }

        //抄送
        cse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WriteDateReportActivity.this,ChooseEmployeeActivity.class);
                intent.putExtra("iswr" , true);
                startActivity(intent);
            }
        });

    }

    /**日报详情
     *
     */
    private void initDiary(){
        DailyInfoTask task = new DailyInfoTask(WriteDateReportActivity.this);
        task.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<DailyInfoData>() {
            @Override
            public void successCallback(Result<DailyInfoData> result) {
                LoadNewRemindReceiver.notifyReceiver();
                DailyInfoData dailyInfoData = result.getValue();
                initTextView(timeTv,dailyInfoData.getRecordTime());
                initTextView(xcEt,dailyInfoData.getTrip());
                initTextView(xjEt,dailyInfoData.getSummary());
                initTextView(fyEt,dailyInfoData.getTodayCost());
                initTextView(jhEt,dailyInfoData.getTomorrowPlan());
                dateRemarkList.clear();
                dateRemarkList.addAll(dailyInfoData.getSupInfos());
                dateRemarkListAdapter.notifyDataSetChanged();
            }
        });

        task.setAsyncTaskFailCallback(new AsyncTaskFailCallback<DailyInfoData>() {
            @Override
            public void failCallback(Result<DailyInfoData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        task.execute(dailyId);
    }

    /**日报列表
     *
     * @param bottom
     */
    private void initComment(final boolean bottom){
        DailyCommentTask dailyCommentTask = new DailyCommentTask(WriteDateReportActivity.this);
        dailyCommentTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<DailyCommentInfo>() {
            @Override
            public void failCallback(Result<DailyCommentInfo> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        dailyCommentTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<DailyCommentInfo>() {
            @Override
            public void successCallback(Result<DailyCommentInfo> result) {
                dataList.clear();
                dataList.addAll(transformData(result.getValue().getList()));
                dailyCommentAdapter.notifyDataSetChanged();
                if (bottom){
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                }
            }
        });

        dailyCommentTask.execute(dailyId);
    }

    /**添加评论
     *
     * @param text
     * @param pic
     * @param voice
     */
    private void addComment(String text , String pic , CommentListener.Voice voice){
        AddDailyCommentTask addDailyCommentTask = new AddDailyCommentTask(WriteDateReportActivity.this);
        addDailyCommentTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        addDailyCommentTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                ToastUtil.toast("评论成功");
                initComment(true);
                commentBottomView.getInputEt().setText("");
                commentBottomView.getInputEt().requestFocus();
                ContextUtils.hideSoftInput(commentBottomView.getInputEt());
            }
        });

        addDailyCommentTask.execute(dailyId , dailyCommentId , text , pic , voice);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK != resultCode){
            return;
        }

        switch (requestCode){
            case CommentBottomBarView.COMMENT_BOTTOM_BAR_BUALUM:
                List<ImageItem> imageItemList = BUAlbum.getSelListAndClear();
                if (Validators.isEmpty(imageItemList)) {
                   ToastUtil.toast("图片选择出现问题，请重新选择");
                }else{
                    ImageItem selectedItem = imageItemList.get(0);

                    if (type.equals(WRITE)){
                        final DateRemark dateRemark = new DateRemark();
                        UpLoadPhotoTask.getUrlPath(WriteDateReportActivity.this, selectedItem.imagePath, new AsyncTaskSuccessCallback<GetUpLoadFileData>() {
                            @Override
                            public void successCallback(Result<GetUpLoadFileData> result) {
                                dateRemark.setPics(result.getValue().getPath());
                                dateRemarkList.add(dateRemark);
                                dateRemarkListAdapter.notifyDataSetChanged();
                                commentBottomView.getInputEt().setText("");
                                commentBottomView.getInputEt().requestFocus();
                                ContextUtils.hideSoftInput(commentBottomView.getInputEt());
                                scrollView.fullScroll(ScrollView.FOCUS_DOWN);

                            }
                        });
                    }else{
                        addCommentWithPic(selectedItem.imagePath , false);
                    }
                }
                break;
            case CommentBottomBarView.COMMENT_BOTTOM_BAR_CAMERA:
                if (type.equals(WRITE)){
                    final DateRemark dateRemark = new DateRemark();
                    UpLoadPhotoTask.getUrlPath(WriteDateReportActivity.this, Constants.SDCARD_DJJBUYER_COMMENT_TEMP_CAMREA, new AsyncTaskSuccessCallback<GetUpLoadFileData>() {
                        @Override
                        public void successCallback(Result<GetUpLoadFileData> result) {
                            dateRemark.setPics(result.getValue().getPath());
                            dateRemarkList.add(dateRemark);
                            dateRemarkListAdapter.notifyDataSetChanged();
                            commentBottomView.getInputEt().setText("");
                            commentBottomView.getInputEt().requestFocus();
                            ContextUtils.hideSoftInput(commentBottomView.getInputEt());
                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                }else{
                    addCommentWithPic(Constants.SDCARD_DJJBUYER_COMMENT_TEMP_CAMREA , true);
                }
                break;
            default:
                break;
        }

    }

    /**使用图片评论
     *
     * @param fileName
     * @param isCamera
     */
    private void addCommentWithPic(String fileName , final boolean isCamera){
        UpLoadPhotoTask upLoadPhotoTask = new UpLoadPhotoTask(WriteDateReportActivity.this);
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

    /**数据转换
     *
     * @param transList
     * @return
     */
    private List<Comment> transformData(List<DailyCommentInfo.CommentInfo> transList){
        List<Comment> tempList = new ArrayList<Comment>();
        for (DailyCommentInfo.CommentInfo commentInfo : transList){
            Comment comment = new Comment();
            comment.setContent(commentInfo.getContent());
            comment.setCreationTime(commentInfo.getCreationTime());
            comment.setId(commentInfo.getId());
            comment.setMemberId(commentInfo.getMemberId());
            comment.setMemberName(commentInfo.getMemberName());
            comment.setPics(commentInfo.getPics());
            comment.setVoice(commentInfo.getVoice());
            comment.setVoiceLength(commentInfo.getVoiceLength());
            tempList.add(comment);
        }
        return tempList;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!mannan.equals(""))
        csman.setText(mannan);
     //   ToastUtil.toast(mannan+1);
    }
}