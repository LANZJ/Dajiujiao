package com.zjyeshi.dajiujiao.buyer.activity.my.work;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.bitmap.BPBitmapLoader;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.BitmapUtils;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigapple.lib.utils.sharepreference.BPPreferences;
import com.xuan.bigapple.lib.utils.uuid.UUIDUtils;
import com.xuan.bigappleui.lib.album.BUAlbum;
import com.xuan.bigappleui.lib.album.entity.ImageItem;
import com.xuan.bigdog.lib.dialog.DGProgressDialog;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.common.Constants;
import com.zjyeshi.dajiujiao.buyer.task.data.other.GetUpLoadFileData;
import com.zjyeshi.dajiujiao.buyer.widgets.timePicker.TimePickerWidget;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.my.work.LeaveConfirmImageAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.entity.enums.ApproveEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.work.LeaveTypeEnum;
import com.zjyeshi.dajiujiao.buyer.circle.task.UpLoadFileTask;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.task.data.my.WorkTimeData;
import com.zjyeshi.dajiujiao.buyer.task.work.LeaveTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.ApproverListData;
import com.zjyeshi.dajiujiao.buyer.utils.DialogUtil;
import com.zjyeshi.dajiujiao.buyer.utils.FileUtil;
import com.zjyeshi.dajiujiao.buyer.utils.LeaveDaysUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.buyer.widgets.timePicker.callback.EndTimeCallbck;
import com.zjyeshi.dajiujiao.buyer.widgets.dialog.LeaveTypeDialog;
import com.zjyeshi.dajiujiao.buyer.widgets.timePicker.callback.OpenTimeCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 我要请假界面
 *
 * Created by zhum on 2016/6/14.
 */
public class AskForLeaveActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;//标题栏

    @InjectView(R.id.typeLayout)
    private RelativeLayout typeLayout;
    @InjectView(R.id.typeTv)
    private TextView typeTv;//请假类型
    @InjectView(R.id.openTimeLayout)
    private RelativeLayout openTimeLayout;
    @InjectView(R.id.endTimeLayout)
    private RelativeLayout endTimeLayout;
    @InjectView(R.id.leaveDaysTv)
    private TextView leaveDaysTv;
    @InjectView(R.id.bzEt)
    private EditText bzEt;
    @InjectView(R.id.spLayout)
    private RelativeLayout spLayout;
    @InjectView(R.id.submitBtn)
    private Button submitBtn;
    @InjectView(R.id.openTimeTv)
    private TextView openTimeTv;
    @InjectView(R.id.endTimeTv)
    private TextView endTimeTv;
    @InjectView(R.id.spNameTv)
    private TextView spNameTv;
    @InjectView(R.id.spAvatarIv)
    private ImageView spAvatarIv;

    @InjectView(R.id.timePickerWidget)
    private TimePickerWidget timePickerWidget;

    @InjectView(R.id.gridView)
    private GridView gridView;

    private List<String> editImageList = new ArrayList<String>();
    private LeaveConfirmImageAdapter leaveConfirmImageAdapter;
    public static final int MAX_IMAGE = 5;

    //上传参数
    private String spId;//审批人ID
    private String type;
    private String startTimeReq;
    private String endTimeReq;
    private String leaveDaysReq;
    private String remark;
    private String files = "";


    private Handler handler = new Handler();

    /**
     * 去相册
     */
    public static final int REQUEST_CODE_ALBUM = 1;
    /**
     * 去拍照
     */
    public static final int REQUEST_CODE_CAMERA = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ask_for_leave);

        initWidgets();
    }

    private void initWidgets(){
        titleLayout.configTitle("我要请假").configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //请假类型
        typeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtil.selectLeaveType(AskForLeaveActivity.this, new LeaveTypeDialog.ItemClickListener() {
                    @Override
                    public void itemClick(String content) {
                        typeTv.setText(content);
                        type = String.valueOf(LeaveTypeEnum.stringOf(content).getValue());
                    }
                } , getLeaveTypeList());
            }
        });
        //设置时间选择确定点击
        timePickerWidget.setTimeClick(new OpenTimeCallback() {
            @Override
            public void openTime(String time) {
                openTimeTv.setText(time);
                startTimeReq = time;
                if (!Validators.isEmpty(endTimeReq)){
                    calLeaveDays();
                }
            }
        }, new EndTimeCallbck() {
            @Override
            public void endTime(String time) {
                endTimeTv.setText(time);
                endTimeReq = time;
                if (!Validators.isEmpty(startTimeReq)){
                    calLeaveDays();
                }
            }
        });
        //开始时间
        openTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               configTimePicker(openTimeTv.getText().toString() , true);
            }
        });
        //结束时间
        endTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configTimePicker(endTimeTv.getText().toString() , false);
            }
        });
        //审批人
        spLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(AskForLeaveActivity.this , ApproverListActivity.class);
                intent.putExtra("type" , ApproveEnum.LEAVE.getValue());
                startActivityForResult(intent , 333);
            }
        });
        //提交
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                judgetAndSend();
            }
        });
        leaveConfirmImageAdapter = new LeaveConfirmImageAdapter(AskForLeaveActivity.this , editImageList);
        gridView.setAdapter(leaveConfirmImageAdapter);

    }

    //设置时间选择
    private void configTimePicker(String content , boolean isOpen){
        if (content.equals("请选择")){
            Calendar calendar =  Calendar.getInstance();
            calendar.setTime(new Date());
            timePickerWidget.showSelect(calendar.get(Calendar.YEAR) , calendar.get(Calendar.MONTH) + 1 ,
                    calendar.get(Calendar.DAY_OF_MONTH) , calendar.get(Calendar.HOUR_OF_DAY) ,  calendar.get(Calendar.MINUTE) , isOpen);
        }else{
            String[] strs = content.split("-");
            String[] sxs = strs[2].split(" ");
            String[] times = sxs[1].split(":");
            timePickerWidget.showSelect(Integer.parseInt(strs[0]) ,Integer.parseInt(strs[1]) ,
                    Integer.parseInt(sxs[0]) , Integer.parseInt(times[0]) , Integer.parseInt(times[1])  , isOpen);
        }
    }

    //计算请假天数，去除双休日
    private void calLeaveDays(){
        WorkTimeData workTime = new WorkTimeData();
        String jsonStr = BPPreferences.instance().getString("workTime" + LoginedUser.getLoginedUser().getId() , "");
        if (!Validators.isEmpty(jsonStr)){
            workTime = JSON.parseObject(jsonStr , WorkTimeData.class);
            if (Validators.isEmpty(workTime.getStartTime()) || Validators.isEmpty(workTime.getAmEndTime())
                    || Validators.isEmpty(workTime.getPmStartTime()) || Validators.isEmpty(workTime.getEndTime())){
                workTime.setStartTime("09:00");
                workTime.setAmEndTime("12:30");
                workTime.setPmStartTime("13:30");
                workTime.setEndTime("17:30");
            }
        }else{
            workTime.setStartTime("09:00");
            workTime.setAmEndTime("12:30");
            workTime.setPmStartTime("13:30");
            workTime.setEndTime("17:30");
        }
        String startTime = openTimeTv.getText().toString();
        String endTime = endTimeTv.getText().toString();
        String times = "";
        if (!Validators.isEmpty(startTime) && !Validators.isEmpty(endTime)){
            times = LeaveDaysUtil.cal8HourDay(startTime , endTime , workTime.getStartTime() , workTime.getAmEndTime()
                    , workTime.getPmStartTime() , workTime.getEndTime());
        }
        String[] needs = times.split("\\+");
        leaveDaysTv.setText(needs[0]);
        leaveDaysReq = needs[1];
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK != resultCode) {
            return;
        }

        if (REQUEST_CODE_ALBUM == requestCode) {
            //图库选择处理
            List<ImageItem> selectedList = BUAlbum.getSelListAndClear();
            dealImageToEditForAlbum(selectedList);
        } else if (REQUEST_CODE_CAMERA == requestCode) {
            //拍照处理
            dealImageToEditForCamera();
        }else if(333 == requestCode){
            ApproverListData.Approver approver = JSON.parseObject(data.getStringExtra("approver") , ApproverListData.Approver.class);
            BPBitmapLoader.getInstance().display(spAvatarIv , approver.getPic());
            spNameTv.setText(approver.getName());
            spId = approver.getId();
        }
    }

    //刷新
    private void refreshData() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                leaveConfirmImageAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 把拍照的图片放大编辑文件夹下
     */
    private void dealImageToEditForCamera() {
        dealImageToEdit(Constants.SDCARD_DJJBUYER_WORK_TEMP_CAMREA);
    }

    /**
     * 把选择的图片压缩放倒编辑文件夹下
     */
    private void dealImageToEditForAlbum(final List<ImageItem> selectedList) {
        if (Validators.isEmpty(selectedList)) {
            return;
        }

        final DGProgressDialog d = new DGProgressDialog(this);
        d.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (ImageItem imageItem : selectedList) {
                    dealImageToEdit(imageItem.imagePath);
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        d.dismiss();
                    }
                });
            }
        }).start();
    }

    private void dealImageToEdit(String filePathName){
        int degree = BitmapUtils.getBitmapDegree(filePathName);//旋转角度
        String editFileName = Constants.SDCARD_DJJBUYER_WORK_EDIT + UUIDUtils.createId();
        FileUtil.checkParentFile(editFileName);//编辑时临时存放图片
        Bitmap b = BitmapUtils.changeOppositeSizeMayDegree(filePathName, editFileName, Constants.IMAGE_LIMIT_SIZE, Constants.IMAGE_LIMIT_SIZE, degree);
        if(null != b){
            editImageList.add(editFileName);
            refreshData();
        }
    }

    /**
     * 上传图片获取图片路径
     */
    private void judgetAndSend(){
        if (Validators.isEmpty(spId) || Validators.isEmpty(leaveDaysReq) || Validators.isEmpty(type)){
            ToastUtil.toast("请确认已填写审批人、请假天数、请假类型");
        }else{
            if (!Validators.isEmpty(editImageList)){
                StringBuilder fileName = new StringBuilder();
                for(String localUrl : editImageList){
                    fileName.append(localUrl);
                    fileName.append(",");

                }
                fileName.deleteCharAt(fileName.length() - 1);
                UpLoadFileTask upLoadFileTask = new UpLoadFileTask(AskForLeaveActivity.this);
                upLoadFileTask.setShowProgressDialog(true);
                upLoadFileTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<GetUpLoadFileData>() {
                    @Override
                    public void failCallback(Result<GetUpLoadFileData> result) {
                        ToastUtil.toast(result.getMessage());
                    }
                });

                upLoadFileTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<GetUpLoadFileData>() {
                    @Override
                    public void successCallback(Result<GetUpLoadFileData> result) {
//                        files = BPPreferences.instance().getString(PreferenceConstants.PIC_PATH, "{}");
                        files = result.getMessage();
                        sendAsk();
                    }
                });
                upLoadFileTask.execute(fileName.toString() , String.valueOf(editImageList.size()));
            }else{
                sendAsk();
            }
        }
    }

    private void sendAsk(){
        remark = bzEt.getText().toString();
        LeaveTask leaveTask = new LeaveTask(AskForLeaveActivity.this);
        leaveTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        leaveTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                ToastUtil.toast("请假成功");
                finish();
                LeaveActivity.isReload = true;
            }
        });

        leaveTask.execute(spId , type , startTimeReq , endTimeReq ,
                leaveDaysReq , remark , files);
    }


    private List<String> getLeaveTypeList(){
        List<String> resultList = new ArrayList<String>();
        resultList.add(LeaveTypeEnum.NIANJIA.toString());
        resultList.add(LeaveTypeEnum.SHIJIA.toString());
        resultList.add(LeaveTypeEnum.BINGJIA.toString());
        resultList.add(LeaveTypeEnum.TIAOXIUJIA.toString());
        resultList.add(LeaveTypeEnum.HUNJIA.toString());
        resultList.add(LeaveTypeEnum.CHANJIA.toString());
        resultList.add(LeaveTypeEnum.QITA.toString());
        return resultList;
    }
}