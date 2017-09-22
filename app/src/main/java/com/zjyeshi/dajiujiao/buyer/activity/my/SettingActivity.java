package com.zjyeshi.dajiujiao.buyer.activity.my;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigdog.lib.bservice.bversioncheck.BDVersionCheckModel;
import com.xuan.bigdog.lib.utils.BDActivityManager;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.activity.my.setting.AboutUsActivity;
import com.zjyeshi.dajiujiao.buyer.activity.store.BalanceAccountsActivity;
import com.zjyeshi.dajiujiao.buyer.common.Constants;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.my.setting.ChangePassActivity;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.task.my.CleanCachTask;
import com.zjyeshi.dajiujiao.buyer.utils.DataCleanUtil;
import com.zjyeshi.dajiujiao.buyer.utils.LogUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

/**
 * 设置
 * Created by whk on 2015/10/1.
 */
public class SettingActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;

    @InjectView(R.id.logoutBtn)
    private Button logoutBtn;//退出

    @InjectView(R.id.versionLayout)
    private RelativeLayout versionLayout;//版本

    @InjectView(R.id.versionTv)
    private TextView versionTv;

    @InjectView(R.id.aboutLayout)
    private RelativeLayout aboutLayout;//关于我们

    @InjectView(R.id.cleanLayout)
    private RelativeLayout cleanLayout;//清除缓存

    @InjectView(R.id.changePassLayout)
    private RelativeLayout changePassLayout;//修改密码

    @InjectView(R.id.cachTv)
    private TextView cachTv;
    private static boolean AER=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_setting);
        initWidgets();

    }

    private void initWidgets() {
        titleLayout.configReturn("返回", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleLayout.configTitle("设置");
        String version = getVersion();
        versionTv.setText(version);
        try {
            String cach = DataCleanUtil.getTotalCacheSize(SettingActivity.this);
            cachTv.setText(cach);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //检测新版本
        versionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = UrlConstants.WEB_SITE + UrlConstants.CHECKVERSION + "?os=android&version=" + getVersionCode() + "&appId=com.zjyeshi.dajiujiao";
                LogUtil.e(url);
                BDVersionCheckModel.getInstance().doCheck(SettingActivity.this, url, Constants.APK_FILENAME);
            }
        });
        //清除缓存
        cleanLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cachTv.getText().toString().equals("0K")) {
                    ToastUtil.toast("缓存已清理，暂无缓存");
                } else {
                    CleanCachTask cleanCachTask = new CleanCachTask(SettingActivity.this);
                    cleanCachTask.setShowProgressDialog(true);
                    cleanCachTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
                        @Override
                        public void successCallback(Result<NoResultData> result) {
                            try {
                                String cach = DataCleanUtil.getTotalCacheSize(SettingActivity.this);
                                cachTv.setText(cach);
                                ToastUtil.toast("缓存已清理");
                            } catch (Exception e) {
                                LogUtil.e(e);
                            }
                        }
                    });
                    cleanCachTask.execute();
                }

            }
        });

        //关于我们
        aboutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, AboutUsActivity.class));
            }
        });

        //没有登录就不显示退出按钮
        if (LoginedUser.getLoginedUser().isLogined()) {
            logoutBtn.setVisibility(View.VISIBLE);
        } else {
            logoutBtn.setVisibility(View.GONE);
        }

        //退出登录
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AER){
                     AER=false;
                  //  BDActivityManager.removeAndFinishIncludes(SettingActivity.class.getSimpleName());
                ExtraUtil.logoutAndUnBindPush(SettingActivity.this);
                   }
            }
        });

        //修改密码
        changePassLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, ChangePassActivity.class));
            }
        });
    }

    /**
     * 获取当前VersionName;
     *
     * @return
     */
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return "V" + version;
        } catch (Exception e) {
            e.printStackTrace();
            return "当前版本未知";
        }
    }

    /**
     * 获取当前VersionCode
     *
     * @return
     */
    public int getVersionCode() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            int versionCode = info.versionCode;
            return versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}
