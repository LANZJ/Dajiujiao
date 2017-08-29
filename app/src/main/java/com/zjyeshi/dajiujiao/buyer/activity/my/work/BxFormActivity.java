package com.zjyeshi.dajiujiao.buyer.activity.my.work;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.xuan.bigapple.lib.utils.uuid.UUIDUtils;
import com.xuan.bigappleui.lib.album.BUAlbum;
import com.xuan.bigappleui.lib.album.entity.ImageItem;
import com.xuan.bigdog.lib.dialog.DGProgressDialog;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.circle.task.UpLoadFileTask;
import com.zjyeshi.dajiujiao.buyer.common.Constants;
import com.zjyeshi.dajiujiao.buyer.entity.enums.ApproveEnum;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.BxFormItem;
import com.zjyeshi.dajiujiao.buyer.task.data.other.GetUpLoadFileData;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.my.work.BxFormListAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.enums.work.CostEnum;
import com.zjyeshi.dajiujiao.buyer.receiver.work.GetPicUrlReceiver;
import com.zjyeshi.dajiujiao.buyer.receiver.work.UpdatePriceReceiver;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.task.work.CostPaidTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.ApproverListData;
import com.zjyeshi.dajiujiao.buyer.task.work.data.Paids;
import com.zjyeshi.dajiujiao.buyer.utils.FileUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 填写报销单
 *
 * Created by zhum on 2016/6/16.
 */
public class BxFormActivity extends BaseActivity {

    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;//标题栏

    @InjectView(R.id.titleEt)
    private EditText titleEt;//报销主题

    @InjectView(R.id.listView)
    private ListView listView;//报销明细列表
    @InjectView(R.id.addTv)
    private TextView addTv;//添加明细
    @InjectView(R.id.spLayout)
    private RelativeLayout spLayout;//审批
    @InjectView(R.id.spAvatarIv)
    private ImageView spAvatarIv;//审批人头像
    @InjectView(R.id.spNameTv)
    private TextView spNameTv;//审批人姓名

    @InjectView(R.id.totalPriceLayout)
    private RelativeLayout totalPriceLayout;//合计金额
    @InjectView(R.id.totalMoneyTv)
    private TextView totalMoneyTv;//合计金额

    private BxFormListAdapter adapter;
    private List<BxFormItem> dataLists;

    private Handler handler = new Handler();

    private GetPicUrlReceiver getPicUrlReceiver; // 上传图片成功
    private UpdatePriceReceiver updatePriceReceiver; //更新总价
    //请求参数
    private String name; //主题
    private String approverId; //审批人Id
    private float paidMoney = 0.00f;//总价
    private String paids;//明细

    public static int position = 0;

    private int totalCount = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_bx_form);

        initWidgets();

        register();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getPicUrlReceiver.unRegister();
        updatePriceReceiver.unRegister();
    }

    private void initWidgets(){

        titleLayout.configTitle("填写报销单").configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        titleLayout.configRightText("提交", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = titleEt.getText().toString();
                if (Validators.isEmpty(name)){
                    ToastUtil.toast("请填写报销主题");
                }else if (Validators.isEmpty(approverId)){
                    ToastUtil.toast("请选择审批人");
                }else{
                    for (int i = 0 ; i < dataLists.size() ; i++){
                        BxFormItem bxFormItem = dataLists.get(i);
                        if (bxFormItem.getApplicantType() == 0){
                            ToastUtil.toast("请填写完类型");
                            break;
                        }else if ((bxFormItem.getApplicantType() != CostEnum.YANGPING.getValue()) && Validators.isEmpty(bxFormItem.getApplcationMoney())){
                            ToastUtil.toast("请填写完金额");
                            break;
                        }else{
                            getPicUrl(bxFormItem.getImageList() , i);
                        }
                    }
                }
            }
        });

        dataLists = new ArrayList<>();
        BxFormItem bxFormItem = new BxFormItem();
        dataLists.add(bxFormItem);
        adapter = new BxFormListAdapter(BxFormActivity.this,dataLists);
        listView.setAdapter(adapter);
        addTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BxFormItem bxFormItem = new BxFormItem();
                dataLists.add(bxFormItem);
                adapter.notifyDataSetChanged();
            }
        });

        spLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(BxFormActivity.this , ApproverListActivity.class);
                intent.putExtra("type" , ApproveEnum.BXMONEY.getValue());
                startActivityForResult(intent , 333);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK != resultCode) {
            return;
        }
        if (AskForLeaveActivity.REQUEST_CODE_ALBUM == requestCode) {
            //图库选择处理
            List<ImageItem> selectedList = BUAlbum.getSelListAndClear();
            dealImageToEditForAlbum(selectedList);
        } else if (AskForLeaveActivity.REQUEST_CODE_CAMERA == requestCode) {
            //拍照处理
            dealImageToEditForCamera();
        }else if(333 == requestCode){
            ApproverListData.Approver approver = JSON.parseObject(data.getStringExtra("approver") , ApproverListData.Approver.class);
            BPBitmapLoader.getInstance().display(spAvatarIv , approver.getPic());
            spNameTv.setText(approver.getName());
            approverId = approver.getId();
        }
    }

    //刷新
    private void refreshData() {
        handler.post(new Runnable() {
            @Override
            public void run() {
//                leaveConfirmImageAdapter.notifyDataSetChanged();
                adapter.notifyDataSetChanged();
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
            dataLists.get(position).getImageList().add(editFileName);
            refreshData();
        }
    }

    //提交
    private void send(){
        CostPaidTask costPaidTask = new CostPaidTask(BxFormActivity.this);
        costPaidTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        costPaidTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                ToastUtil.toast("提交成功");
                finish();
                BxMoneyActivity.isReload = true;
            }
        });

        costPaidTask.execute(name , approverId , ExtraUtil.format(paidMoney), paids);
    }

    /**
     * 上传图片获取图片路径
     */
    private void getPicUrl(List<String> editImageList ,final int p){

        if (!Validators.isEmpty(editImageList)){
            StringBuilder fileName = new StringBuilder();
            for(String localUrl : editImageList){
                fileName.append(localUrl);
                fileName.append(",");

            }
            fileName.deleteCharAt(fileName.length() - 1);
            UpLoadFileTask upLoadFileTask = new UpLoadFileTask(BxFormActivity.this);
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
                    dataLists.get(p).setPics(result.getMessage());
                    GetPicUrlReceiver.notifyReceiver();
                }
            });

            upLoadFileTask.execute(fileName.toString() , String.valueOf(editImageList.size()));
        }else{
            GetPicUrlReceiver.notifyReceiver();
        }
    }

    //广播
    private void register(){
        //上传图片完成监听
        getPicUrlReceiver = new GetPicUrlReceiver() {
            @Override
            public void calCount() {
                totalCount ++;
                if (totalCount == dataLists.size()){
                    List<Paids> paidsList = new ArrayList<Paids>();
                    for (BxFormItem bxFormItem : dataLists){
                        Paids paids = new Paids();
                        paids.setPics(bxFormItem.getPics());
                        paids.setApplicantType(bxFormItem.getApplicantType());
                        if (bxFormItem.getApplcationMoney().equals("0")){
                            paids.setApplicationMoney("");
                        }else{
                            paids.setApplicationMoney(bxFormItem.getApplcationMoney());
                        }
                        paids.setRemark(bxFormItem.getRemark());
                        paidsList.add(paids);
                    }
                    paids = JSON.toJSONString(paidsList);

                    send();
                }
            }
        };
        getPicUrlReceiver.register();

        //更新价格
        updatePriceReceiver = new UpdatePriceReceiver() {
            @Override
            public void calPrice() {
                float countMoney = 0.00f;
                for (BxFormItem bxFormItem : dataLists){
                    if (!Validators.isEmpty(bxFormItem.getApplcationMoney())){
                        countMoney += Float.parseFloat(bxFormItem.getApplcationMoney());
                    }
                }
                paidMoney = countMoney;
                totalMoneyTv.setText(ExtraUtil.format(paidMoney));
            }
        };
        updatePriceReceiver.register();
    }

}