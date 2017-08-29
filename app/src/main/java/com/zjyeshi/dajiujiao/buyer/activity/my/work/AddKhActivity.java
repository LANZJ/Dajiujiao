package com.zjyeshi.dajiujiao.buyer.activity.my.work;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.zjyeshi.dajiujiao.buyer.activity.my.personal.SelectProvinceActivity;
import com.zjyeshi.dajiujiao.buyer.circle.task.UpLoadFileTask;
import com.zjyeshi.dajiujiao.buyer.common.Constants;
import com.zjyeshi.dajiujiao.buyer.receiver.info.GetAreaReceiver;
import com.zjyeshi.dajiujiao.buyer.receiver.work.UpdateReceiver;
import com.zjyeshi.dajiujiao.buyer.task.data.other.GetUpLoadFileData;
import com.zjyeshi.dajiujiao.buyer.task.work.AddCustomerTask;
import com.zjyeshi.dajiujiao.buyer.utils.FileUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;

import java.io.File;
import java.util.List;

/**
 * 添加客户
 *
 * Created by zhum on 2016/6/14.
 */
public class AddKhActivity extends BaseActivity {

    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;//标题栏
    @InjectView(R.id.avatarIv)
    private ImageView avatarIv;//头像
    @InjectView(R.id.nameEt)
    private EditText nameEt;//名字
    @InjectView(R.id.moneyEt)
    private EditText moneyEt;//年营业额
    @InjectView(R.id.frEt)
    private EditText frEt;//法人
    @InjectView(R.id.qlrwEt)
    private EditText qlrwEt;//权力人物
    @InjectView(R.id.telEt)
    private EditText telEt;//电话
    @InjectView(R.id.landTelEt)
    private EditText landTelEt;//固话
    @InjectView(R.id.addressTv)
    private TextView addressTv;//地址
    @InjectView(R.id.addressLayout)
    private RelativeLayout addressLayout;//地址
    @InjectView(R.id.addressDetailEt)
    private EditText addressDetailEt;//详细地址
    @InjectView(R.id.ywNumEt)
    private EditText ywNumEt;//业务员数量
    @InjectView(R.id.nqNumEt)
    private EditText nqNumEt;//内勤人数
    @InjectView(R.id.yyareaEt)
    private EditText yyareaEt;//营业面积
    @InjectView(R.id.ckareaEt)
    private EditText ckareaEt;//仓库面积
    @InjectView(R.id.payWayTv)
    private TextView payWayTv;//付款方式
    @InjectView(R.id.payWayLayout)
    private RelativeLayout payWayLayout;//付款方式
    @InjectView(R.id.productStatusLayout)
    private RelativeLayout productStatusLayout;//产品地位
    @InjectView(R.id.productStatusTv)
    private TextView productStatusTv;//产品地位
    @InjectView(R.id.competingProductsEt)
    private TextView competingProductsEt;//竞品信息
    @InjectView(R.id.bzEt)
    private EditText bzEt;//备注

    private GetAreaReceiver getAreaReceiver;//改变地区广播
    private String area = "";//地区代码
    private String payWayCode = "";//支付方式
    private String productStatusCode = "";//产品地位

    public static final String MEMBERID = "member_id";

    private String memberId;//替该ID添加花名册
    private String fileName;
    private String pic = "";
    private Handler handler = new Handler();
    /**
     * 去相册
     */
    public static final int REQUEST_CODE_ALBUM = 3;
    /**
     * 去拍照
     */
    public static final int REQUEST_CODE_CAMERA = 4;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_addkh);

        getAreaReceiver = new GetAreaReceiver() {
            @Override
            public void showArea(String speName, String speCode) {
                addressTv.setText(speName);
                area = speCode;
            }
        };
        getAreaReceiver.register();

        initWidgets();
    }

    private void initWidgets(){
        memberId = getIntent().getStringExtra(MEMBERID);

        titleLayout.configTitle("添加客户").configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        titleLayout.configRightText("完成", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Validators.isEmpty(text(telEt))){
                    ToastUtil.toast("手机号不能为空");
                    return;
                }else if (text(telEt).length() != 11){
                    ToastUtil.toast("请输入正确的11位手机号");
                    return;
                }

                if (Validators.isEmpty(text(nameEt))){
                    ToastUtil.toast("名称不能为空");
                    return;
                }

                if (Validators.isEmpty(text(frEt))){
                    ToastUtil.toast("法人不能为空");
                    return;
                }

                if (Validators.isEmpty(text(addressTv)) || Validators.isEmpty(text(addressDetailEt))){
                    ToastUtil.toast("地址不能为空");
                    return;
                }
                getPicAndSend();
            }
        });

        addressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddKhActivity.this, SelectProvinceActivity.class));
            }
        });

        payWayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AddKhActivity.this,ChosePayWayActivity.class),1);
            }
        });

        productStatusLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AddKhActivity.this,ChoseProductStatusActivity.class),2);
            }
        });

        avatarIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DGSingleSelectDialog d = new DGSingleSelectDialog.Builder(AddKhActivity.this).setItemTextAndOnClickListener(new String[]{"从手机相册选择", "拍照"}, new View.OnClickListener[]{new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //从相册中选择
                        BUAlbum.gotoAlbumForSingle(AddKhActivity.this, REQUEST_CODE_ALBUM);
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //拍照
                        gotoCamera();
                    }
                }}).create();
                d.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if (data!=null){
                    payWayCode = data.getExtras().getString("PayWay");//得到新Activity 关闭后返回的数据
                    if (!Validators.isEmpty(payWayCode)){
                        if (payWayCode.equals("1")){
                            payWayTv.setText("现金");
                        }else {
                            payWayTv.setText("打卡");
                        }
                    }
                }
                break;
            case 2:
                if (data!=null) {
                    productStatusCode = data.getExtras().getString("ProductStatus");//得到新Activity 关闭后返回的数据
                    if (!Validators.isEmpty(productStatusCode)) {
                        if (productStatusCode.equals("1")) {
                            productStatusTv.setText("一般");
                        } else if (productStatusCode.equals("2")) {
                            productStatusTv.setText("核心");
                        } else {
                            productStatusTv.setText("重点");
                        }
                    }
                }
                break;
            case REQUEST_CODE_ALBUM:
                //图库选择处理
                List<ImageItem> selectedList = BUAlbum.getSelListAndClear();
                dealImageToEditForAlbum(selectedList);
                break;
            case REQUEST_CODE_CAMERA:
                //拍照处理
                dealImageToEditForCamera();
                break;
        }
    }

    //刷新
    private void refreshData() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                BPBitmapLoader.getInstance().display(avatarIv , fileName);
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
            fileName = editFileName;
            refreshData();
        }
    }

    /**
     * 去拍照
     */
    public void gotoCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        FileUtil.checkParentFile(Constants.SDCARD_DJJBUYER_WORK_TEMP_CAMREA);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Constants.SDCARD_DJJBUYER_WORK_TEMP_CAMREA)));
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    /**
     * 上传图片获取图片路径
     */
    private void getPicAndSend(){
        if (!Validators.isEmpty(fileName)){
            UpLoadFileTask upLoadFileTask = new UpLoadFileTask(AddKhActivity.this);
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
                    pic = result.getMessage();
                    send();
                }
            });

            upLoadFileTask.execute(fileName.toString() , 1);
        }else{
            send();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getAreaReceiver.unRegister();
    }

    private String text(TextView view){
        return view.getText().toString();
    }

    private void send(){
        AddCustomerTask task = new AddCustomerTask(AddKhActivity.this);
        task.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                ToastUtil.toast("添加成功");
                UpdateReceiver.notifyReceiver();
                finish();
            }
        });

        task.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        task.execute(text(nameEt),text(telEt),text(frEt),text(moneyEt),
                text(qlrwEt),text(ywNumEt),text(nqNumEt),area,text(addressDetailEt),
                text(yyareaEt),text(ckareaEt),payWayCode,productStatusCode,
                text(competingProductsEt),text(bzEt),pic, "" , memberId , text(landTelEt));
    }
}