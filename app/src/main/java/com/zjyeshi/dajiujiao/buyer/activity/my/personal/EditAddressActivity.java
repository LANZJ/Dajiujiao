package com.zjyeshi.dajiujiao.buyer.activity.my.personal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.xuan.bigdog.lib.location.DGLocationUtils;
import com.zjyeshi.dajiujiao.buyer.dao.DaoFactory;
import com.zjyeshi.dajiujiao.buyer.entity.my.Area;
import com.zjyeshi.dajiujiao.buyer.entity.my.BigArea;
import com.zjyeshi.dajiujiao.buyer.entity.my.CodeArea;
import com.zjyeshi.dajiujiao.buyer.entity.my.DetailArea;
import com.zjyeshi.dajiujiao.buyer.receiver.info.GetAreaReceiver;
import com.zjyeshi.dajiujiao.buyer.task.data.my.AddData;
import com.zjyeshi.dajiujiao.buyer.task.my.AddAddressTask;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.entity.my.Address;
import com.zjyeshi.dajiujiao.buyer.receiver.info.RefreshAddressListReceiver;
import com.zjyeshi.dajiujiao.buyer.receiver.order.ChangeOrderStatusReceiver;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.task.my.ChangeAddressTask;
import com.zjyeshi.dajiujiao.buyer.task.my.GetAreaDataTask;
import com.zjyeshi.dajiujiao.buyer.task.order.ModifyOrderAddressTask;
import com.zjyeshi.dajiujiao.buyer.utils.LogUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;

import java.util.List;

/**
 * 修改地址
 * Created by wuhk on 2015/11/12.
 */
public class EditAddressActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.selectAreaLayout)
    private RelativeLayout selectAreaLayout;
    @InjectView(R.id.receiverEt)
    private EditText receiverEt;
    @InjectView(R.id.phoneEt)
    private EditText phoneEt;
    @InjectView(R.id.areaTv)
    private TextView areaTv;
    @InjectView(R.id.detailEt)
    private EditText detailEt;

    private Address address;
    private boolean change;

    private String EditAddress;//编辑是的地区名称

    private String area = "";//地区代码

    private String orderId = "";//是否是帮忙修改的标记 , 不为空就是帮助修改
    public static final String ORDERID = "order_id";
    public static final String PARAM_ADDRESS = "param.address";

    private GetAreaReceiver getAreaReceiver;//改变地区广播
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_edit_address);
        getAreaReceiver = new GetAreaReceiver() {
            @Override
            public void showArea(String speName, String speCode) {
                areaTv.setText(speName);
                area = speCode;
                if (null != address){
                    address.setArea(area);
                }
            }
        };
        getAreaReceiver.register();
        initWidgets();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getAreaReceiver.unRegister();
    }

    private void initWidgets(){
        address = (Address)getIntent().getSerializableExtra(PARAM_ADDRESS);
        orderId = getIntent().getStringExtra(ORDERID);

        //地址不为空，表示是修改进入的
        if (null != address){
            receiverEt.setText(address.getName());
            phoneEt.setText(address.getPhone());
            if (!Validators.isEmpty(address.getArea())){
                area = address.getArea();
                getAreaByCode(address.getArea());
            }
            detailEt.setText(address.getAddress());
            change = true;
        }else{
            //地址为空，表示新增地址，需要填写默认省市区
            DGLocationUtils.init(EditAddressActivity.this, new BDLocationListener() {
                @Override
                public void onReceiveLocation(BDLocation bdLocation) {
                    String province = bdLocation.getProvince();
                    String city = bdLocation.getCity();
                    String district  = bdLocation.getDistrict();
                    String  street =bdLocation.getStreet();
                    String streetN=bdLocation.getStreetNumber();
                    String phone=  LoginedUser.getLastLoginedUserInfo().getPhone();
                    String name=LoginedUser.getLoginedUser().getName();
                    areaTv.setText(province + city + district);
                    detailEt.setText(street+streetN);
                    phoneEt.setText(phone);
                    receiverEt.setText(name);
                    LogUtil.e("地区信息：=======" + province + city + district);
                    //省代码
                    area = getCodeByStr(province);
                    BigArea provinceArea = DaoFactory.getAreaDao().findByCode("0," + area);
                    for(DetailArea detailArea : provinceArea.getList()){
                        if (detailArea.getName().equals(city)){
                            area = area + ","+ detailArea.getCode();
                            break;
                        }
                    }
                    BigArea cityArea = DaoFactory.getAreaDao().findByCode("0," + area);
                    for(DetailArea detailArea : cityArea.getList()){
                        if (detailArea.getName().equals(district)){
                            area = area + "," + detailArea.getCode();
                            break;
                        }
                    }
                    LogUtil.e("地区代码 " + area);
                    DGLocationUtils.stop();
                }
            });
            DGLocationUtils.start();

        }

        titleLayout.configReturn("返回", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).configTitle("修改地址").configRightText("保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = receiverEt.getText().toString();//姓名
                String phone = phoneEt.getText().toString();//手机号
                String address = detailEt.getText().toString();//详细地址
                //判空 ， 并根据是否是改变还是增加进行相应操作
                judgeEmpty(name , phone ,address);
            }
        });

        selectAreaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditAddressActivity.this, SelectProvinceActivity.class));
            }
        });
    }

    //增加收货地址
    private void addAddress(final String name , final String phone , final String address){

        AddAddressTask addAddressTask = new AddAddressTask(EditAddressActivity.this);
        addAddressTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<AddData>() {
            @Override
            public void failCallback(Result<AddData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        addAddressTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<AddData>() {
            @Override
            public void successCallback(Result<AddData> result) {
                ToastUtil.toast("添加加地址成功");
                Address ar = new Address();
                ar.setOwnerUserId(LoginedUser.getLoginedUser().getId());
                ar.setId(result.getValue().getId());
                ar.setName(name);
                ar.setPhone(phone);
                ar.setArea(area);
                ar.setSelected(2);
                ar.setAddress(address);

                DaoFactory.getAddressDao().replace(ar);
                finish();
                RefreshAddressListReceiver.notifyReceiver();

            }
        });

        addAddressTask.execute(name, phone, area, address);
    }
    //修改地址
    private void changeAddress(final String name  , final String phone ,final  String addressDetail){
        final String id = address.getId();
        ChangeAddressTask changeAddressTask = new ChangeAddressTask(EditAddressActivity.this);
        changeAddressTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        changeAddressTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                ToastUtil.toast("修改地址成功");
                address.setName(name);
                address.setPhone(phone);
                address.setAddress(addressDetail);

                DaoFactory.getAddressDao().replace(address);
                finish();
                RefreshAddressListReceiver.notifyReceiver();
            }
        });
        changeAddressTask.execute(id ,name , phone , area , addressDetail);
    }

    //帮助修改地址
    private void modifyOrderAddress(String name , String phone , String address){
        ModifyOrderAddressTask modifyOrderAddressTask = new ModifyOrderAddressTask(EditAddressActivity.this);
        modifyOrderAddressTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        modifyOrderAddressTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                finish();
                ChangeOrderStatusReceiver.notifyReceiver();
            }
        });

        modifyOrderAddressTask.execute(orderId , name , phone , area , address);
    }

    //根据区号代码获得地区
    private void  getAreaByCode(String code){
        String temp[] = code.split(",");
        final String provinceCode = temp[0];
        final String cityCode =temp[1];
        final String districtCode = temp[2];


        final BigArea bigArea = DaoFactory.getAreaDao().findByCode("0");
        if (null == bigArea){
            GetAreaDataTask getAreaDataTask = new GetAreaDataTask(EditAddressActivity.this);
            getAreaDataTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
                @Override
                public void failCallback(Result<NoResultData> result) {

                }
            });
            getAreaDataTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
                @Override
                public void successCallback(Result<NoResultData> result) {
                    BigArea newBigArea = DaoFactory.getAreaDao().findByCode("0");
                    EditAddress = change(newBigArea , provinceCode , cityCode , districtCode);
                    areaTv.setText(EditAddress);
                }
            });
            getAreaDataTask.execute();
        }else{
            EditAddress = change(bigArea , provinceCode , cityCode , districtCode);
            areaTv.setText(EditAddress);
        }
    }

    private String change(BigArea bigArea , String provinceCode ,String cityCode ,String districtCode){
        String result= "";
        List<DetailArea> detailAreaList = bigArea.getList();
        for (DetailArea detailArea : detailAreaList){
            if (detailArea.getCode().equals(provinceCode)){
                result = detailArea.getName();
            }
        }
        BigArea bigArea1 = DaoFactory.getAreaDao().findByCode("0"+"," +  provinceCode);
        List<DetailArea> detailAreaList1 = bigArea1.getList();
        for (DetailArea detailArea : detailAreaList1){
            if (detailArea.getCode().equals(cityCode)){
                result = result + " " + detailArea.getName();
            }
        }

        BigArea bigArea2 = DaoFactory.getAreaDao().findByCode("0" + "," + provinceCode + "," +cityCode);
        List<DetailArea> detailAreaList2 = bigArea2.getList();
        for (DetailArea detailArea : detailAreaList2){
            if (detailArea.getCode().equals(districtCode)){
                result = result + " " + detailArea.getName();
            }
        }
        return  result;
    }

    //地址判空
    private void judgeEmpty(String name , String phone , String address){
        if (Validators.isEmpty(name)){
            ToastUtil.toast("请填写姓名");
        }else if(Validators.isEmpty(phone)){
            ToastUtil.toast("请填写手机号");
        }else if(Validators.isEmpty(area)){
            ToastUtil.toast("请选择地区");
        }else if (Validators.isEmpty(address)){
            ToastUtil.toast("请填写详细地址");
        }else{
            if (!Validators.isEmpty(orderId)){
                //帮助修改地址
                modifyOrderAddress(name , phone , address);
            }else{
                if (change){
                    changeAddress(name , phone , address);
                }else{
                    addAddress(name , phone , address);
                }
            }
        }
    }


    public static void startActivity(Context context , Address address , String orderId){
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PARAM_ADDRESS, address);
        intent.putExtras(bundle);
        if (!Validators.isEmpty(orderId)){
            intent.putExtra(ORDERID , orderId);
        }
        intent.setClass(context, EditAddressActivity.class);
        context.startActivity(intent);
    }


    private String getCodeByStr(String str){
        //市代码
        String result = "";
        BigArea bigArea = DaoFactory.getAreaDao().findByAreaLike("%" + str + "%");
        if (null != bigArea){
            LogUtil.e("不为空" + bigArea.getCode());
            for (DetailArea detailArea : bigArea.getList()){
                if (detailArea.getName().equals(str)){
                    result = detailArea.getCode();
                    break;
                }
            }
        }
        return result;
    }
}
