package com.xuan.bigdog.lib.widgets.about;

import android.content.Context;
import android.content.Intent;

import com.xuan.bigdog.lib.R;

/**
 * 配置参数
 *
 * Created by xuan on 16/3/7.
 */
public class DGAboutConfig {
    private int icon;
    private String nameVersion;
    private String copyrightTitle;
    private String copyrightDetail;

    private DGAboutConfig.UIConfig uiConfig = new DGAboutConfig.UIConfig();//aboutUI配置

    public UIConfig getUiConfig() {
        return uiConfig;
    }

    public DGAboutConfig setUiConfig(UIConfig uiConfig) {
        this.uiConfig = uiConfig;
        return this;
    }

    public String getCopyrightDetail() {
        return copyrightDetail;
    }

    public DGAboutConfig setCopyrightDetail(String copyrightDetail) {
        this.copyrightDetail = copyrightDetail;
        return this;
    }

    public String getCopyrightTitle() {
        return copyrightTitle;
    }

    public DGAboutConfig setCopyrightTitle(String copyrightTitle) {
        this.copyrightTitle = copyrightTitle;
        return this;
    }

    public int getIcon() {
        return icon;
    }

    public DGAboutConfig setIcon(int icon) {
        this.icon = icon;
        return this;
    }

    public String getNameVersion() {
        return nameVersion;
    }

    public DGAboutConfig setNameVersion(String nameVersion) {
        this.nameVersion = nameVersion;
        return this;
    }

    public class UIConfig {
        private int bodybgColor = R.color.dg_color_white;
        private int versionNameColor = R.color.dg_color_444444;
        private int copyRightTitleColor = R.color.dg_color_444444;
        private int copyRightDetailColor = R.color.dg_color_444444;

        public int getBodybgColor() {
            return bodybgColor;
        }

        public void setBodybgColor(int bodybgColor) {
            this.bodybgColor = bodybgColor;
        }

        public int getCopyRightDetailColor() {
            return copyRightDetailColor;
        }

        public void setCopyRightDetailColor(int copyRightDetailColor) {
            this.copyRightDetailColor = copyRightDetailColor;
        }

        public int getCopyRightTitleColor() {
            return copyRightTitleColor;
        }

        public void setCopyRightTitleColor(int copyRightTitleColor) {
            this.copyRightTitleColor = copyRightTitleColor;
        }

        public int getVersionNameColor() {
            return versionNameColor;
        }

        public void setVersionNameColor(int versionNameColor) {
            this.versionNameColor = versionNameColor;
        }
    }

    /**
     * 启动界面
     *
     * @param context
     */
    public void startWork(Context context){
        Intent intent = new Intent();
        DGAboutActivity.configTemp = this;
        intent.setClass(context, DGAboutActivity.class);
        context.startActivity(intent);
    }

}
