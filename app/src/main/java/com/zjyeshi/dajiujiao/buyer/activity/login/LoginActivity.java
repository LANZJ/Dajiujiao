package com.zjyeshi.dajiujiao.buyer.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.ContextUtils;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigdog.lib.location.DGLocationUtils;
import com.xuan.bigdog.lib.utils.BDActivityManager;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.activity.frame.fragment.MianFramActivity;
import com.zjyeshi.dajiujiao.buyer.activity.my.work.WorkActivity;
import com.zjyeshi.dajiujiao.buyer.common.Constants;
import com.zjyeshi.dajiujiao.buyer.dao.DaoFactory;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.entity.contact.AddressUser;
import com.zjyeshi.dajiujiao.buyer.entity.enums.LoginEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.LoginStatusEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.MarketCostEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.UserEnum;
import com.zjyeshi.dajiujiao.buyer.receiver.ToLoginReceiver;
import com.zjyeshi.dajiujiao.buyer.receiver.buy.GetFollowShopReceiver;
import com.zjyeshi.dajiujiao.buyer.receiver.info.ChangePersonalReceiver;
import com.zjyeshi.dajiujiao.buyer.task.data.login.LoginData;
import com.zjyeshi.dajiujiao.buyer.task.data.store.homepage.GetNearbyShopList;
import com.zjyeshi.dajiujiao.buyer.task.login.LoginTask;
import com.zjyeshi.dajiujiao.buyer.task.seller.GetProductBuyTask;
import com.zjyeshi.dajiujiao.buyer.utils.LogUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

/**
 * 登录
 * Created by wuhk on 2015/9/28.
 */
public class LoginActivity extends BaseActivity {
    @InjectView(R.id.registerTv)
    private TextView registerTv;
    @InjectView(R.id.loginEt)
    private EditText loginEt;
    @InjectView(R.id.lodinDel)
    private ImageView loginDel;
    @InjectView(R.id.pwdEt)
    private EditText pwdEt;
    @InjectView(R.id.forgetPwdTv)
    private TextView forgetPwdTv;
    @InjectView(R.id.loginBtn)
    private Button loginBtn;
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    //参数
    public static final String PARAM_PHONE = "param.phone";

    private String latitude;
    private String longitude;
    private String phone;
    private String password;

    private String registerPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        initWidgets();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DGLocationUtils.stop();
    }

    private void initWidgets() {

        registerPhone = getIntent().getStringExtra(PARAM_PHONE);
        if (!Validators.isEmpty(registerPhone)) {
            loginEt.setText(registerPhone);
        } else {
            loginEt.setText(LoginedUser.getLastLoginedUserInfo().getPhone());
            pwdEt.setText(LoginedUser.getLastLoginedUserInfo().getPassword());
        }
        titleLayout.configTitle("登录");
        registerTv.setVisibility(View.GONE);

        ContextUtils.showSoftInput(loginEt);
        //用户名有内容是显示清除按钮
        loginEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (loginEt.getText().length() > 0) {
                    loginDel.setVisibility(View.VISIBLE);
                } else {
                    loginDel.setVisibility(View.GONE);
                }
            }
        });
        //清除用户名
        loginDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginEt.setText("");
            }
        });
        //忘记密码
        forgetPwdTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, ForgetPwdActivity.class);
                startActivity(intent);
            }
        });

        //登录
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = loginEt.getText().toString();
                password = pwdEt.getText().toString();
                if (Validators.isEmpty(phone) || Validators.isEmpty(password)) {
                    ToastUtil.toast("用户名或密码不能为空");
                } else {
                    DGLocationUtils.init(LoginActivity.this, new BDLocationListener() {
                        @Override
                        public void onReceiveLocation(BDLocation bdLocation) {
                            DGLocationUtils.stop();
                            latitude = String.valueOf((int) (bdLocation.getLatitude() * 100000));
                            longitude = String.valueOf((int) (bdLocation.getLongitude() * 100000));
                            LogUtil.debug("定位结果:" + "lat" + latitude + "-----" + "lng" + longitude);
                            LoginTask loginTask = new LoginTask(LoginActivity.this);
                            loginTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<LoginData>() {
                                @Override
                                public void failCallback(Result<LoginData> result) {
                                    ToastUtil.toast(result.getMessage());
                                }
                            });
                            loginTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<LoginData>() {
                                @Override
                                public void successCallback(Result<LoginData> result) {

                                    //保存登录信息
                                    saveLoginInfo(result.getValue());
                                    //将自己插入联系人表
                                    AddressUser au = new AddressUser();
                                    au.setOwnerUserId(LoginedUser.getLoginedUser().getId());
                                    au.setUserId(LoginedUser.getLoginedUser().getId());
                                    au.setName(LoginedUser.getLoginedUser().getName());
                                    au.setAvatar(LoginedUser.getLoginedUser().getPic());
                                    DaoFactory.getAddressUserDao().replaceOrInsert(au);

                                    //登录类型卖家买家
                                    LoginEnum loginEnum;
                                    if (LoginedUser.getLoginedUser().getUserEnum() == UserEnum.CUSTOMER) {
                                        loginEnum = LoginEnum.BURER;
                                    } else {
                                        loginEnum = LoginEnum.SELLER;
                                    }
                                    //登录类型与保存类型相同
                                    if (loginEnum == Constants.loginEnum) {
                                        //此端为卖家
                                        if (Constants.loginEnum.equals(LoginEnum.SELLER)) {
                                            //连接微信服务器
                                           // ExtraUtil.connectChat(LoginActivity.this);

                                            if (LoginedUser.getLoginedUser().getUserEnum() == UserEnum.SALESMAN) {
                                                //业务员，直接进入工作台
                                                BDActivityManager.removeAndFinishAll();
                                                Intent intent = new Intent();
                                                intent.setClass(LoginActivity.this, WorkActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                //获取卖家上一级店铺信息
                                                if (Constants.loginEnum == LoginEnum.SELLER) {
                                                    GetProductBuyTask getProductBuyTask = new GetProductBuyTask(LoginActivity.this);
                                                    getProductBuyTask.setShowProgressDialog(false);
                                                    getProductBuyTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<GetNearbyShopList>() {
                                                        @Override
                                                        public void failCallback(Result<GetNearbyShopList> result) {
                                                            ToastUtil.toast(result.getMessage());
                                                        }
                                                    });

                                                    getProductBuyTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<GetNearbyShopList>() {
                                                        @Override
                                                        public void successCallback(Result<GetNearbyShopList> result) {

                                                        }
                                                    });
                                                    getProductBuyTask.execute();
                                                }
                                                //发起获取收藏店铺列表广播
                                                GetFollowShopReceiver.notifyReceiver(latitude, longitude);
                                                //改变头像和名字
                                                ChangePersonalReceiver.notifyReceiver(LoginStatusEnum.LOGIN);
                                                gotoFrame();
                                            }
                                        }
                                    } else {
                                        LoginedUser.getLoginedUser().setLogined(false);
                                        // 同步到本地文件
                                        LoginedUser.setLoginedUser(LoginedUser.getLoginedUser());
                                        ToastUtil.toast("请区分卖家买家账号");
                                    }
                                }
                            });
                            loginTask.execute(phone, password, String.valueOf(longitude), String.valueOf(latitude));
                            DGLocationUtils.stop();
                        }
                    });
                    DGLocationUtils.start();
                }
            }
        });
    }

    /**
     * 保存登录信息
     *
     * @param loginData
     */
    private void saveLoginInfo(LoginData loginData) {
        LoginedUser loginedUser = new LoginedUser();
        loginedUser.setPhone(phone);
        loginedUser.setPassword(password);
        loginedUser.setLogined(true);
        //返回的参数
        loginedUser.setToken(loginData.getToken());
        loginedUser.setId(loginData.getId());
        loginedUser.setName(loginData.getName());
        loginedUser.setPic(loginData.getPic());
        loginedUser.setCircleBackgroundPic(loginData.getCircleBackgroundPic());
        loginedUser.setShopName(loginData.getShopName());
        loginedUser.setShopId(loginData.getShopId());
        loginedUser.setjur(loginData.getJur());
        loginedUser.setMarketCostEnum(MarketCostEnum.valueOf(loginData.getMarketCostType()));
      //  LoginedUser.saveToFile();


        int type = loginData.getType();
        switch (type) {
            case 1:
                loginedUser.setUserEnum(UserEnum.CUSTOMER);
                break;
            case 2:
                //终端没有市场支持费用
                loginedUser.setUserEnum(UserEnum.TERMINAL);
                loginedUser.setMarketCostEnum(MarketCostEnum.NO_SUPPORT);
                break;
            case 3:
                loginedUser.setUserEnum(UserEnum.DEALER);
                break;
            case 4:
                loginedUser.setUserEnum(UserEnum.AGENT);
                break;
            case 5:
                loginedUser.setUserEnum(UserEnum.SALESMAN);
                break;
            case 99:
                loginedUser.setUserEnum(UserEnum.ADMINISTRATOR);
                break;
            default:
                loginedUser.setUserEnum(UserEnum.UNKNOW);
                break;
        }

        loginedUser.setShopType(loginData.getShopType());

        //操作权限
        String jur = loginData.getJur();
        if (!Validators.isEmpty(jur)) {
            if (jur.contains(",")) {
                String[] auths = jur.split(",");
                for (int i = 0; i < auths.length; i++) {
                    setAuth(auths[i], loginedUser);
                }
            } else {
                setAuth(jur, loginedUser);
            }
        }
        // 同步到本地文件
        LoginedUser.setLoginedUser(loginedUser);
        LoginedUser.getLoginedUser().displayLog();

//
//        //登录成功改变一下标志位
//        String loginCount = BPPreferences.instance().getString(PreferenceConstants.USER_LOGIN_COUNT + LoginedUser.getLoginedUser().getId(), "");
//        if (Validators.isEmpty(loginCount)) {
//            //是第一次登陆
//            BPPreferences.instance().putString(PreferenceConstants.USER_LOGIN_COUNT + LoginedUser.getLoginedUser().getId(), LoginedUser.FIRST_LOGIN);
//        } else {
//            BPPreferences.instance().putString(PreferenceConstants.USER_LOGIN_COUNT + LoginedUser.getLoginedUser().getId(), LoginedUser.ALREADY_LOGIN);
//        }
    }

    /**
     * 设置权限
     *
     * @param str
     * @param loginedUser
     */
    private void setAuth(String str, LoginedUser loginedUser) {
        if (str.equals("1")) {
            loginedUser.setLeaveAuth(true);
        } else if (str.equals("2")) {
            loginedUser.setMoneyAuth(true);
        } else if (str.equals("3")) {
            loginedUser.setBxAuth(true);
        } else if (str.equals("4")) {
            loginedUser.setMaxLeavel(true);
        } else if (str.equals("5")) {
            loginedUser.setDeliverAuth(true);
        }
    }

    /**
     * 跳转去主界面
     */
    private void gotoFrame() {
        startActivity(new Intent(LoginActivity.this, MianFramActivity.class));
        finish();
    }
}
