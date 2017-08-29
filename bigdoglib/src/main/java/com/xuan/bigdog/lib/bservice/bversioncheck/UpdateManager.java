package com.xuan.bigdog.lib.bservice.bversioncheck;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.xuan.bigdog.lib.R;
import com.xuan.bigdog.lib.update.entity.UpdateInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lan on 2017/7/20.
 */
public class UpdateManager {
    private Context mContext; //上下文
    private final UpdateInfo updateInfo;
    private static final String savePath = "/sdcard/updateAPK/"; //apk保存到SD卡的路径
    private static final String saveFileName = savePath + "apkName.apk"; //完整路径名

    private ProgressBar mProgress; //下载进度条控件
    private static final int DOWNLOADING = 1; //表示正在下载
    private static final int DOWNLOADED = 2; //下载完毕
    private static final int DOWNLOAD_FAILED = 3; //下载失败
    private int progress; //下载进度
    private boolean cancelFlag = false; //取消下载标志位

//    private double serverVersion = 2.0; //从服务器获取的版本号
    private double clientVersion = 26; //客户端当前的版本号
//    private String updateDescription = "更新描述"; //更新内容描述信息
//    private boolean forceUpdate = false; //是否强制更新

    private AlertDialog alertDialog1, alertDialog2; //表示提示对话框、进度条对话框

    /** 构造函数 */
    public UpdateManager(Context context,UpdateInfo updateInfo) {
        this.mContext = context;
        if (null == updateInfo) {
            updateInfo = new UpdateInfo();
        }
        this.updateInfo = updateInfo;

//        if (null == updateInfo.getHandler()) {
//            mUpdateconfig.setHandler(new UpdateHandler(context, mUpdateconfig));
//        }

    }

    /** 显示更新对话框 */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void showNoticeDialog() {
        //如果版本最新，则不需要更新
        if (updateInfo.getLastVersion() <= clientVersion)
            return;
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);//使用默认设备 浅色主题;
        dialog.setTitle("发现新版本 ：" + updateInfo.getLastVersion());
        dialog.setMessage(updateInfo.getDescription());
        dialog.setPositiveButton("现在更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                arg0.dismiss();
                showDownloadDialog();
            }
        });
        //是否强制更新
        if (updateInfo.isForceUpdate() == false) {
            dialog.setNegativeButton("待会更新", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    arg0.dismiss();
                }
            });
        }
        alertDialog1  = dialog.create();
        alertDialog1.setCancelable(false);
        alertDialog1.show();
    }

    /** 显示进度条对话框 */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void showDownloadDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        dialog.setTitle("正在更新");
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.softupdate_progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.update_progress);

        dialog.setView(v);
        //如果是强制更新，则不显示取消按钮
        if (updateInfo.isForceUpdate() == false) {
            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    arg0.dismiss();
                    cancelFlag = false;
                }
            });
        }
        alertDialog2  = dialog.create();
        alertDialog2.setCancelable(false);
        alertDialog2.show();

        //下载apk
        downloadAPK();
    }

    /** 下载apk的线程 */
    public void downloadAPK() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                   URL url = new URL(updateInfo.getUrl());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();

                    int length = conn.getContentLength();
                    InputStream is = conn.getInputStream();

                    File file = new File(savePath);
                    if(!file.exists()){
                        file.mkdir();
                    }
                    String apkFile = saveFileName;
                    File ApkFile = new File(apkFile);
                    FileOutputStream fos = new FileOutputStream(ApkFile);

                    int count = 0;
                    byte buf[] = new byte[1024];

                    do{
                        int numread = is.read(buf);
                        count += numread;
                        progress = (int)(((float)count / length) * 100);
                        //更新进度
                        mHandler.sendEmptyMessage(DOWNLOADING);
                        if(numread <= 0){
                            //下载完成通知安装
                            mHandler.sendEmptyMessage(DOWNLOADED);
                            break;
                        }
                        fos.write(buf, 0, numread);
                    }while(!cancelFlag); //点击取消就停止下载.

                    fos.close();
                    is.close();
                } catch(Exception e) {
                    mHandler.sendEmptyMessage(DOWNLOAD_FAILED);
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /** 更新UI的handler */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case DOWNLOADING:
                    mProgress.setProgress(progress);
                    break;
                case DOWNLOADED:
                    if (alertDialog2 != null)
                        alertDialog2.dismiss();
                    installAPK();
                    break;
                case DOWNLOAD_FAILED:
                    Toast.makeText(mContext, "网络断开，请稍候再试", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    };

    /** 下载完成后自动安装apk */
    public void installAPK() {
        File apkFile = new File(saveFileName);
        if (!apkFile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + apkFile.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(intent);
    }

}
