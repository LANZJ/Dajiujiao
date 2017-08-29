package com.zjyeshi.dajiujiao.buyer.activity.my.work;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
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
import com.xuan.bigapple.lib.utils.uuid.UUIDUtils;
import com.xuan.bigappleui.lib.album.BUAlbum;
import com.xuan.bigappleui.lib.album.entity.ImageItem;
import com.xuan.bigdog.lib.dialog.DGProgressDialog;
import com.xuan.bigdog.lib.dialog.DGSingleSelectDialog;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.my.work.LeaveConfirmImageAdapter;
import com.zjyeshi.dajiujiao.buyer.circle.task.UpLoadFileTask;
import com.zjyeshi.dajiujiao.buyer.common.Constants;
import com.zjyeshi.dajiujiao.buyer.entity.enums.ApproveEnum;
import com.zjyeshi.dajiujiao.buyer.task.data.other.GetUpLoadFileData;
import com.zjyeshi.dajiujiao.buyer.task.work.CostApplicationTask;
import com.zjyeshi.dajiujiao.buyer.utils.FileUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.buyer.views.other.MyGridView;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.entity.enums.work.CostEnum;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.task.work.data.ApproverListData;

import java.util.ArrayList;
import java.util.List;

/**
 * 填写申请单
 * <p/>
 * Created by zhum on 2016/6/16.
 */
public class AskMoneyFormActivity extends BaseActivity {

    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;//标题栏

    @InjectView(R.id.moneyEt)
    private EditText moneyEt;//费用金额
    @InjectView(R.id.typeLayout)
    private RelativeLayout typeLayout;//费用类型
    @InjectView(R.id.typeTv)
    private TextView typeTv;//费用类型
    @InjectView(R.id.bzEt)
    private EditText bzEt;//费用说明
    @InjectView(R.id.addIv)
    private ImageView addIv;//添加照片

    @InjectView(R.id.spLayout)
    private RelativeLayout spLayout;//审批
    @InjectView(R.id.spAvatarIv)
    private ImageView spAvatarIv;//审批人头像
    @InjectView(R.id.spNameTv)
    private TextView spNameTv;//审批人姓名
    @InjectView(R.id.moneyLayout)
    private RelativeLayout moneyLayout;

    @InjectView(R.id.gridView)
    private MyGridView gridView;

    private List<String> editImageList = new ArrayList<String>();
    private LeaveConfirmImageAdapter leaveConfirmImageAdapter;
    private Handler handler = new Handler();

    /**
     * 去相册
     */
    public static final int REQUEST_CODE_ALBUM = 1;
    /**
     * 去拍照
     */
    public static final int REQUEST_CODE_CAMERA = 2;


    //请求参数
    private String approverId;//审批人
    private String applicatType;//费用申请类型
    private String applicationMoney;//费用金额
    private String remark;//备注
    private String pics = "";//图片

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ask_money_form);

        initWidgets();
    }

    private void initWidgets() {
        titleLayout.configTitle("填写申请单").configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        titleLayout.configRightText("提交", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                judgeEmptyAndSend();
            }
        });

        //审批
        spLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(AskMoneyFormActivity.this, ApproverListActivity.class);
                intent.putExtra("type", ApproveEnum.APPLYMONEY.getValue());
                startActivityForResult(intent, 333);
            }
        });

        //判断是否输入数字
        moneyEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String money = moneyEt.getText().toString();
                if (!Validators.isNumeric(money)) {
                    ToastUtil.toast("请输入正常的价格");
//                    moneyEt.setText("");
//                    moneyEt.setHint("点击输入");
                }
            }
        });
        //费用类型选择
        typeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DGSingleSelectDialog dialog = new DGSingleSelectDialog.Builder(AskMoneyFormActivity.this)
                        .setItemTextAndOnClickListener(new String[]{CostEnum.SHICHANG.toString(), CostEnum.YANGPING.toString() , CostEnum.QITA.toString()},
                                new View.OnClickListener[]{new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        typeTv.setText(CostEnum.SHICHANG.toString());
                                        moneyLayout.setVisibility(View.VISIBLE);
                                    }
                                }, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        typeTv.setText(CostEnum.YANGPING.toString());
                                        moneyLayout.setVisibility(View.GONE);
                                        moneyEt.setText("0");
                                    }
                                } , new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        typeTv.setText(CostEnum.QITA.toString());
                                        moneyLayout.setVisibility(View.VISIBLE);
                                    }
                                }}).create();
                dialog.show();
            }
        });

        leaveConfirmImageAdapter = new LeaveConfirmImageAdapter(AskMoneyFormActivity.this, editImageList);
        gridView.setAdapter(leaveConfirmImageAdapter);

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
        } else if (333 == requestCode) {
            ApproverListData.Approver approver = JSON.parseObject(data.getStringExtra("approver"), ApproverListData.Approver.class);
            BPBitmapLoader.getInstance().display(spAvatarIv, approver.getPic());
            spNameTv.setText(approver.getName());
            approverId = approver.getId();
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

    private void dealImageToEdit(String filePathName) {
        int degree = BitmapUtils.getBitmapDegree(filePathName);//旋转角度
        String editFileName = Constants.SDCARD_DJJBUYER_WORK_EDIT + UUIDUtils.createId();
        FileUtil.checkParentFile(editFileName);//编辑时临时存放图片
        Bitmap b = BitmapUtils.changeOppositeSizeMayDegree(filePathName, editFileName, Constants.IMAGE_LIMIT_SIZE, Constants.IMAGE_LIMIT_SIZE, degree);
        if (null != b) {
            editImageList.add(editFileName);
            refreshData();
        }
    }

    /**
     * 上传图片获取图片路径
     */
    private void getPicUrl() {
        if (!Validators.isEmpty(editImageList)) {
            StringBuilder fileName = new StringBuilder();
            for (String localUrl : editImageList) {
                fileName.append(localUrl);
                fileName.append(",");

            }
            fileName.deleteCharAt(fileName.length() - 1);
            UpLoadFileTask upLoadFileTask = new UpLoadFileTask(AskMoneyFormActivity.this);
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
                    pics =result.getMessage();
                    sendAsk();
                }
            });

            upLoadFileTask.execute(fileName.toString(), String.valueOf(editImageList.size()));
        } else {
            sendAsk();
        }
    }

    private void judgeEmptyAndSend() {
        applicationMoney = moneyEt.getText().toString();
        if (applicationMoney.equals("0")){
            applicationMoney = "";
        }
        applicatType = typeTv.getText().toString();
        remark = bzEt.getText().toString();
        if (applicationMoney.equals("点击输入") || applicatType.equals("请选择") || Validators.isEmpty(approverId)) {
            ToastUtil.toast("请将信息填写完整");
        } else {
            getPicUrl();
        }

    }

    private void sendAsk() {
        CostApplicationTask costApplicationTask = new CostApplicationTask(AskMoneyFormActivity.this);
        costApplicationTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        costApplicationTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                ToastUtil.toast("提交成功 ");
                finish();
                AskForMoneyActivity.isReload = true;
            }
        });

        costApplicationTask.execute(approverId, String.valueOf(CostEnum.stringOf(applicatType).getValue()), applicationMoney, remark, pics);
    }
}