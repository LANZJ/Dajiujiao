package com.xuan.bigdog.lib.zxing;

/**
 * 配置文件
 *
 * Created by xuan on 15/9/23.
 */
public class ZxConfig {
    private int bitmapWidth = 300;
    private int bitmapHeight = 300;

    private String saveFileName;// 存储二维码文件地址

    private int bgColor = 0xFFFFFFFF;// 白色
    private int color = 0xFF000000;// 黑色

    private String encoding;

    public ZxConfig() {
    }

    public ZxConfig(String saveFileName) {
        this.saveFileName = saveFileName;
    }

    public int getBitmapWidth() {
        return bitmapWidth;
    }

    public void setBitmapWidth(int bitmapWidth) {
        this.bitmapWidth = bitmapWidth;
    }

    public int getBitmapHeight() {
        return bitmapHeight;
    }

    public void setBitmapHeight(int bitmapHeight) {
        this.bitmapHeight = bitmapHeight;
    }

    public String getSaveFileName() {
        return saveFileName;
    }

    public void setSaveFileName(String saveFileName) {
        this.saveFileName = saveFileName;
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

}
