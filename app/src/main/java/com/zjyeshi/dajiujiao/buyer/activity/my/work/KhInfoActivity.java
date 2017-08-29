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
import com.xuan.bigapple.lib.utils.IntentUtils;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigapple.lib.utils.uuid.UUIDUtils;
import com.xuan.bigappleui.lib.album.BUAlbum;
import com.xuan.bigappleui.lib.album.entity.ImageItem;
import com.xuan.bigdog.lib.dialog.DGProgressDialog;
import com.xuan.bigdog.lib.dialog.DGSingleSelectDialog;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.activity.my.personal.SelectProvinceActivity;
import com.zjyeshi.dajiujiao.buyer.chat.ChatManager;
import com.zjyeshi.dajiujiao.buyer.circle.task.UpLoadFileTask;
import com.zjyeshi.dajiujiao.buyer.common.Constants;
import com.zjyeshi.dajiujiao.buyer.dao.DaoFactory;
import com.zjyeshi.dajiujiao.buyer.entity.my.BigArea;
import com.zjyeshi.dajiujiao.buyer.entity.my.DetailArea;
import com.zjyeshi.dajiujiao.buyer.receiver.info.GetAreaReceiver;
import com.zjyeshi.dajiujiao.buyer.receiver.work.UpdateReceiver;
import com.zjyeshi.dajiujiao.buyer.task.data.other.GetUpLoadFileData;
import com.zjyeshi.dajiujiao.buyer.task.work.AddCustomerTask;
import com.zjyeshi.dajiujiao.buyer.task.work.GetCustomerInfoTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.CustomerInfoData;
import com.zjyeshi.dajiujiao.buyer.utils.FileUtil;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.utils.DialogUtil;

import java.io.File;
import java.util.List;

/**
 * 客户信息
 *
 * Created by zhum on 2016/6/15.
 */
public class KhInfoActivity extends BaseActivity {

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
    private EditText landTelEt;
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
    @InjectView(R.id.chatIv)
    private ImageView chatIv;
    @InjectView(R.id.telV)
    private View telV;

    @InjectView(R.id.sendBtn)
    private RelativeLayout sendBtn;//发送
    private String fileName;

    private String pic = "";
    private String id;//客户id
    private GetAreaReceiver getAreaReceiver;//改变地区广播
    private String area = "";//地区代码
    private String payWayCode = "";//支付方式
    private String phone;
    private String productStatusCode = "";//产品地位
    private boolean b =false;

    private Handler handler = new Handler();

    private String memberId;

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
        setContentView(R.layout.layout_kh_info);

        id = getIntent().getStringExtra("id");
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

        titleLayout.configTitle("客户信息").configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        titleLayout.configRightText("编辑", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!b){
                    titleLayout.configRightText("完成",null);
                    b=true;
                    edit();
                }else {
                    if (text(telEt).length() != 11){
                        ToastUtil.toast("请输入正确的11位手机号");
                        return;
                    }
                    titleLayout.configRightText("编辑",null);
                    b=false;
                    getPicAndSend();
                }
            }
        });
        memberId = getIntent().getStringExtra("memberId");
        if (!Validators.isEmpty(memberId)){
            chatIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChatManager.getInstance().startConversion(KhInfoActivity.this , memberId );
                }
            });
        }

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KhInfoActivity.this,SendKhInfoActivity.class);
                intent.putExtra("phone",phone);
                startActivity(intent);
            }
        });

        telV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtil.confirm(KhInfoActivity.this, "是否拨打电话" + telEt.getText().toString(), "取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }, "确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IntentUtils.callByPhone(KhInfoActivity.this , telEt.getText().toString());
                    }
                });
            }
        });

        avatarIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DGSingleSelectDialog d = new DGSingleSelectDialog.Builder(KhInfoActivity.this).setItemTextAndOnClickListener(new String[]{"从手机相册选择", "拍照"}, new View.OnClickListener[]{new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //从相册中选择
                        BUAlbum.gotoAlbumForSingle(KhInfoActivity.this, REQUEST_CODE_ALBUM);
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
        getData();
    }

    private void getData(){
        GetCustomerInfoTask task = new GetCustomerInfoTask(KhInfoActivity.this);
        task.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<CustomerInfoData>() {
            @Override
            public void successCallback(Result<CustomerInfoData> result) {
                CustomerInfoData data =result.getValue();
                data.setArea(data.getProvince()+","+data.getCity()+","+data.getTown());
                area = data.getArea();
                payWayCode = data.getPaymentMethod();
                productStatusCode = data.getProductStatus();
                String addressName =  getAddressName(data.getArea());
                phone = data.getPhone();

//                initImageViewDefault(avatarIv,data.getPic(),R.drawable.default_img);
                GlideImageUtil.glidImage(avatarIv , data.getPic() , R.drawable.default_img);
                nameEt.setText(data.getName());
                moneyEt.setText(data.getTurnover());
                frEt.setText(data.getLegalPerson());
                qlrwEt.setText(data.getRightPerson());
                telEt.setText(data.getPhone());
                landTelEt.setText(data.getLandlinePhone());
                addressTv.setText(addressName);
                addressDetailEt.setText(data.getAddress());
                ywNumEt.setText(data.getSalemensQuantity());
                nqNumEt.setText(data.getPersonQuantity());
                yyareaEt.setText(data.getBusinessArea());
                ckareaEt.setText(data.getWarehouseArea());
                pic = data.getPic();

                if (!Validators.isEmpty(data.getPaymentMethod())){
                    if (data.getPaymentMethod().equals("1")){
                        initTextView(payWayTv,"现金");
                    }else if (data.getPaymentMethod().equals("2")){
                        initTextView(payWayTv,"打卡");
                    }else{
                        payWayTv.setText("");
                    }
                }
                if (!Validators.isEmpty(data.getProductStatus())){
                    if (data.getProductStatus().equals("1")){
                        initTextView(productStatusTv,"一般");
                    }else if ((data.getProductStatus().equals("2"))){
                        initTextView(productStatusTv,"核心");
                    }else if (data.getProductStatus().equals("3")) {
                        initTextView(productStatusTv,"重点");
                    }else{
                        productStatusTv.setText("");
                    }
                }
                competingProductsEt.setText(data.getComptingProducts());
                bzEt.setText(data.getRamark());
            }
        });

        task.setAsyncTaskFailCallback(new AsyncTaskFailCallback<CustomerInfoData>() {
            @Override
            public void failCallback(Result<CustomerInfoData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        task.execute(id);
    }

    private String getAddressName(String code){
        String addressName = "";

        String[] codeArray = code.split(",");
        BigArea bigArea = DaoFactory.getAreaDao().findByCode("0");
        for (DetailArea area : bigArea.getList()){
            if (codeArray[0].equals(area.getCode())){
                addressName += area.getName();
                break;
            }
        }

        BigArea bigArea1 = DaoFactory.getAreaDao().findByCode("0,"+codeArray[0]);
        for (DetailArea area : bigArea1.getList()){
            if (codeArray[1].equals(area.getCode())){
                addressName += area.getName();
                break;
            }
        }

        BigArea bigArea2 = DaoFactory.getAreaDao().findByCode("0,"+codeArray[0]+","+codeArray[1]);
        for (DetailArea area : bigArea2.getList()){
            if (codeArray[2].equals(area.getCode())){
                addressName += area.getName();
                break;
            }
        }

        return addressName;
    }

    private void show(){
        chatIv.setVisibility(View.VISIBLE);
        telV.setVisibility(View.VISIBLE);

        nameEt.setEnabled(false);
        nameEt.setHint("");
        moneyEt.setEnabled(false);
        moneyEt.setHint("");
        frEt.setEnabled(false);
        frEt.setHint("");
        qlrwEt.setEnabled(false);
        qlrwEt.setHint("");
        telEt.setEnabled(false);
        telEt.setHint("");
        landTelEt.setEnabled(false);
        landTelEt.setHint("");
        addressTv.setEnabled(false);
        addressTv.setHint("");
        addressDetailEt.setEnabled(false);
        addressDetailEt.setHint("");
        ywNumEt.setEnabled(false);
        ywNumEt.setHint("");
        nqNumEt.setEnabled(false);
        nqNumEt.setHint("");
        yyareaEt.setEnabled(false);
        yyareaEt.setHint("");
        ckareaEt.setEnabled(false);
        ckareaEt.setHint("");
        competingProductsEt.setEnabled(false);
        competingProductsEt.setHint("");
        bzEt.setEnabled(false);
        bzEt.setHint("");

        avatarIv.setEnabled(false);

        addressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        payWayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        productStatusLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        AddCustomerTask task = new AddCustomerTask(KhInfoActivity.this);
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
                ToastUtil.toast(result.getMessage());
            }
        });

        task.execute(text(nameEt),text(telEt),text(frEt),text(moneyEt),
                text(qlrwEt),text(ywNumEt),text(nqNumEt),area,text(addressDetailEt),
                text(yyareaEt),text(ckareaEt),payWayCode,productStatusCode,
                text(competingProductsEt),text(bzEt),pic,id , "" , text(landTelEt));
    }

    private void edit(){
        chatIv.setVisibility(View.GONE);
        telV.setVisibility(View.GONE);
        nameEt.setEnabled(true);
        nameEt.setHint("请输入名称");
        moneyEt.setEnabled(true);
        moneyEt.setHint("请输入年营业额");
        frEt.setEnabled(true);
        frEt.setHint("请输入法人");
        qlrwEt.setEnabled(true);
        qlrwEt.setHint("请输入权力人物");
        telEt.setEnabled(true);
        telEt.setHint("请输入手机号");
        landTelEt.setEnabled(true);
        landTelEt.setHint("请输入固话");
        addressTv.setEnabled(true);
        addressTv.setHint("请选择地区");
        addressDetailEt.setEnabled(true);
        addressDetailEt.setHint("请输入详细地址");
        ywNumEt.setEnabled(true);
        ywNumEt.setHint("请输入业务员人数");
        nqNumEt.setEnabled(true);
        nqNumEt.setHint("请输入内勤人数");
        yyareaEt.setEnabled(true);
        yyareaEt.setHint("请输入营业面积");
        ckareaEt.setEnabled(true);
        ckareaEt.setHint("请输入仓库面积");
        competingProductsEt.setEnabled(true);
        competingProductsEt.setHint("请输入竞品信息");
        bzEt.setEnabled(true);
        bzEt.setHint("请输入备注");
        avatarIv.setEnabled(true);

        addressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(KhInfoActivity.this, SelectProvinceActivity.class));
            }
        });

        payWayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(KhInfoActivity.this,ChosePayWayActivity.class),1);
            }
        });

        productStatusLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(KhInfoActivity.this,ChoseProductStatusActivity.class),2);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getAreaReceiver.unRegister();
    }

    private String text(TextView view){
        return view.getText().toString();
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
            UpLoadFileTask upLoadFileTask = new UpLoadFileTask(KhInfoActivity.this);
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
                    show();
                }
            });

            upLoadFileTask.execute(fileName.toString() , 1);
        }else{
            show();
        }
    }

}