package com.xuan.bigdog.lib.model;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.View;

import com.xuan.bigapple.lib.io.FileUtils;
import com.xuan.bigapple.lib.utils.ToastUtils;
import com.xuan.bigapple.lib.utils.log.LogUtils;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 上传图片逻辑块
 *
 * Created by xuan on 15/9/23.
 */
public abstract class DGUploadHeadModel {
    /**去相册*/
    public static final int REQUEST_CODE_ALBUM = 1;
    /**去拍照*/
    public static final int REQUEST_CODE_CAMERA = 2;
    /**相册返回裁剪*/
    public static final int REQUEST_CODE_CUT_4_AlBUM = 3;
    /**拍照返回裁剪*/
    public static final int REQUEST_CODE_CUT_4_CAMERA = 4;

    protected Activity mActivity;
    protected Fragment mFragment;

    public Uri cameraSaveUri;

    /**
     * 初始化头像上传
     *
     * @param headLayout
     */
    public void initForActivity(View headLayout, final Activity activity) {
        this.mActivity = activity;
        this.mFragment = null;
        createDir();

        if (null != headLayout) {
            headLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onShowDialog();
                }
            });
        }
    }

    /**
     * 初始化头像上传
     *
     * @param headLayout
     */
    public void initForFragment(View headLayout, final Fragment fragment) {
        this.mActivity = null;
        this.mFragment = fragment;
        createDir();

        if (null != headLayout) {
            headLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    extraJudge();
                    onShowDialog();
                }
            });
        }
    }

    /**
     * 处理返回结果
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        // 从相册获取照片，保存大图，并走去截图
        if (REQUEST_CODE_ALBUM == requestCode) {
            gotoCut(data.getData(), REQUEST_CODE_CUT_4_AlBUM, getBigHeadFilename());
            return;
        }

        // 从拍照获取图片，保存大图，并走去截图
        if (REQUEST_CODE_CAMERA == requestCode) {
            gotoCut(cameraSaveUri, REQUEST_CODE_CUT_4_CAMERA, getBigHeadFilename());
            return;
        }

        // 截图后处理小图
        if (requestCode == REQUEST_CODE_CUT_4_AlBUM
                || requestCode == REQUEST_CODE_CUT_4_CAMERA) {

            Bitmap bigBitmap = null;
            Bitmap smallBitmap = null;
            try {
                // 生成小图保存
                bigBitmap = BitmapFactory.decodeFile(getBigHeadFilename());
                if (null != bigBitmap) {
                    smallBitmap = saveBitmap2FileName(bigBitmap, getSmallHeadFilename(), 100, 100);
                }
            } catch (Exception e) {
                LogUtils.e(e.getMessage(), e);
            }

            if (null != bigBitmap && null != smallBitmap) {
                // 上传头像
                onUploadHead2Server(bigBitmap, smallBitmap);
            }else{
                ToastUtils.displayTextShort("截图失败，请重试.");
            }

            if (requestCode == REQUEST_CODE_CUT_4_CAMERA) {
                // 如果是拍照来着就删除临时文件
                FileUtils.deleteFileOrDirectoryQuietly(cameraSaveUri.getPath());
            }
        }
    }

    protected String getSmallHeadFilename() {
        return onSaveFilePath() + "small.jpg";
    }

    protected String getBigHeadFilename() {
        return onSaveFilePath() + "big.jpg";
    }

    protected String getTempFilename() {
        return onSaveFilePath() + "temp.jpg";
    }

    private void createDir() {
        File saveDirFile = new File(onSaveFilePath());
        if (!saveDirFile.exists()) {
            saveDirFile.mkdirs();
        }
    }

    /**
     * 保存图片到本地
     *
     * @param fromBitmap
     * @param dest
     * @param newWidth
     * @param newHeight
     */
    private Bitmap saveBitmap2FileName(Bitmap fromBitmap, String dest, int newWidth, int newHeight) {
        Bitmap bitmapSrc = fromBitmap;

        int w = bitmapSrc.getWidth();
        int h = bitmapSrc.getHeight();

        // 若宽高小于指定最大值，不需重新绘制
        Bitmap bitmap = null;
        if (w <= newWidth && h <= newHeight) {
            bitmap = bitmapSrc;
        } else {
            float scale = ((float) newWidth / w) > ((float) newHeight / h) ? ((float) newHeight / h)
                    : ((float) newWidth / w);
            newWidth = (int) (w * scale);
            newHeight = (int) (h * scale);
            if (newWidth <= 0) {
                newWidth = 1;
            }
            if (newHeight <= 0) {
                newHeight = 1;
            }

            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            bitmap = Bitmap.createBitmap(bitmapSrc, 0, 0, w, h, matrix, true);
        }

        OutputStream out = null;
        try {
            File file = new File(dest);
            createParentDirs(file);
            out = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, out);
        } catch (Exception e) {
            LogUtils.e(e.getMessage(), e);
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                // Ignore
            }
        }

        return bitmap;
    }

    private static void createParentDirs(File file) {
        File parentPath = file.getParentFile();
        if (!parentPath.exists() || !parentPath.isDirectory()) {
            parentPath.mkdirs();
        }
    }

    /**
     * 去相册
     */
    public void gotoAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        startActivityForResult(intent, REQUEST_CODE_ALBUM);
    }

    /**
     * 去相机拍照返回图片
     */
    public void gotoCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);

        File file = new File(getTempFilename());
        File parentFile = file.getParentFile();
        if(!parentFile.exists()){
            parentFile.mkdirs();
        }

        cameraSaveUri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraSaveUri);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    /**
     * 去截图
     *
     * @param uri
     * @param requestCode
     * @param output
     */
    private void gotoCut(Uri uri, int requestCode, String output) {
        if (null == uri) {
            return;
        }

        // 如果没有文件夹，先创建之
        File file = new File(output);
        File fileDir = file.getParentFile();
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        setIntentExtra(intent);
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        intent.putExtra("return-data", false);

        startActivityForResult(intent, requestCode);
    }

    /**设置裁剪框的比例
     *
     * @param intent
     */
    protected void setIntentExtra(Intent intent){

    }

    private void startActivityForResult(Intent intent, int requestCode){
        if(null != mActivity){
            mActivity.startActivityForResult(intent, requestCode);
        }else{
            mFragment.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 选中图片后上传
     *
     * @param bigBitmap
     * @param smallBitmap
     */
    protected abstract void onUploadHead2Server(Bitmap bigBitmap, Bitmap smallBitmap);

    /**
     * 显示对话框
     */
    protected abstract void onShowDialog();

    /**
     * 拍照选择图片的保存路径
     *
     * @return
     */
    protected abstract String onSaveFilePath();

    /**
     * 额外操作，区别两个model中的activityforresult
     * @return
     */
    protected void extraJudge(){

    }
}
