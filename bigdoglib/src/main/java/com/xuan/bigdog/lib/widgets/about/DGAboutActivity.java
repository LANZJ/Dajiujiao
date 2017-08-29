package com.xuan.bigdog.lib.widgets.about;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xuan.bigdog.lib.R;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;

/**
 * 关于界面
 *
 * Created by xuan on 16/3/7.
 */
public class DGAboutActivity extends Activity {
    public static DGAboutConfig configTemp;

    private DGTitleLayout dgAboutTitleLayout;
    private ImageView dgAboutIcon;
    private TextView dgAboutNameVersion;
    private TextView dgAboutCopyTitle;
    private TextView dgAboutCopyDetail;
    private View dgAboutBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dg_widgets_about);
        initView();
        DGAboutConfig config = configTemp;
        configTemp = null;

        //标题栏初始化
        dgAboutTitleLayout.configTitle("关于我们").configReturn(this);

        //设置内容
        dgAboutIcon.setImageResource(config.getIcon());
        dgAboutNameVersion.setText(config.getNameVersion());
        dgAboutCopyTitle.setText(config.getCopyrightTitle());
        dgAboutCopyDetail.setText(config.getCopyrightDetail());

        //设置UI
        paintUI(config);
    }

    private void paintUI(DGAboutConfig config){
        DGAboutConfig.UIConfig uiConfig = config.getUiConfig();
        dgAboutBg.setBackgroundResource(uiConfig.getBodybgColor());
        dgAboutNameVersion.setTextColor(uiConfig.getVersionNameColor());
        dgAboutCopyTitle.setTextColor(uiConfig.getCopyRightTitleColor());
        dgAboutCopyDetail.setTextColor(uiConfig.getCopyRightDetailColor());
    }

    private void initView(){
        dgAboutTitleLayout = (DGTitleLayout)findViewById(R.id.dgAboutTitleLayout);
        dgAboutIcon = (ImageView)findViewById(R.id.dgAboutIcon);
        dgAboutNameVersion = (TextView)findViewById(R.id.dgAboutNameVersion);
        dgAboutCopyTitle = (TextView)findViewById(R.id.dgAboutCopyTitle);
        dgAboutCopyDetail = (TextView)findViewById(R.id.dgAboutCopyDetail);
        dgAboutBg = findViewById(R.id.dgAboutBg);
    }

}
