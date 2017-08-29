package com.zjyeshi.dajiujiao.buyer.circle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.circle.task.CircleAddTask;
import com.zjyeshi.dajiujiao.buyer.circle.task.UpLoadFileTask;
import com.zjyeshi.dajiujiao.buyer.circle.task.data.CircleAddData;
import com.zjyeshi.dajiujiao.buyer.common.Constants;
import com.zjyeshi.dajiujiao.buyer.task.data.other.GetUpLoadFileData;
import com.zjyeshi.dajiujiao.buyer.utils.FileUtil;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.circle.receiver.OnlyRefrshReceiver;
import com.zjyeshi.dajiujiao.buyer.utils.LogUtil;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.BitmapUtils;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigapple.lib.utils.uuid.UUIDUtils;
import com.xuan.bigappleui.lib.album.BUAlbum;
import com.xuan.bigappleui.lib.album.entity.ImageItem;
import com.xuan.bigdog.lib.dialog.DGProgressDialog;
import com.xuan.bigdog.lib.dialog.DGSingleSelectDialog;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 发布圈子
 * <p/>
 * Created by xuan on 15/11/5.
 */
public class CircleAddActivity extends BaseActivity {
    /**
     * 去相册
     */
    public static final int REQUEST_CODE_ALBUM = 1;
    /**
     * 去拍照
     */
    public static final int REQUEST_CODE_CAMERA = 2;
    public static final int MAX_IMAGE = 9;

    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;

    @InjectView(R.id.editText)
    private EditText editText;

    @InjectView(R.id.limitTv)
    private TextView limitTv;

    @InjectView(R.id.gridView)
    private GridView gridView;

    private List<String> editImageList = new ArrayList<String>();
    private EditImageAdapter editImageAdapter;

    private Handler handler = new Handler();

    private String files = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_circle_add);
        initWidgets();
    }

    private void initWidgets() {

        //标题
        titleLayout.configTitle("发动态").configReturn("返回", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).configRightText("发布", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                sendCircle();
                getPicUrl();
            }
        });

        //输入监听
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int size = s.toString().length();
                limitTv.setText(size + "/150");
            }
        });

        //图片适配器
        editImageAdapter = new EditImageAdapter();
        gridView.setAdapter(editImageAdapter);
    }

    public class EditImageAdapter extends MBaseAdapter {
        @Override
        public int getCount() {
            if (editImageList.size() == MAX_IMAGE) {
                return MAX_IMAGE;
            } else {
                return editImageList.size() + 1;
            }
        }

        @Override
        public View getView(int position, View view, ViewGroup arg2) {
//            if (null == view) {
//                view = LayoutInflater.from(CircleAddActivity.this).inflate(R.layout.view_image, null);
//            }
            view = LayoutInflater.from(CircleAddActivity.this).inflate(R.layout.view_image, null);

            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);

            if (editImageList.size() == position) {
                //最后一个+
                imageView.setImageResource(R.drawable.icon_add_pic);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DGSingleSelectDialog d = new DGSingleSelectDialog.Builder(CircleAddActivity.this).setItemTextAndOnClickListener(new String[]{"从手机相册选择", "拍照"}, new View.OnClickListener[]{new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //从相册中选择
                                BUAlbum.gotoAlbumForMulti(CircleAddActivity.this, getRestNum(), REQUEST_CODE_ALBUM);
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
            } else {
//                initImageViewDefault(imageView, editImageList.get(position));
                GlideImageUtil.glidImage(imageView , editImageList.get(position) , R.drawable.default_img);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }

            return view;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK != resultCode) {
            return;
        }

        if (REQUEST_CODE_ALBUM == requestCode) {
            //图库选择处理
            List<ImageItem> selectedList = BUAlbum.getSelListAndClear();
            dealImageToEditForAlbum(selectedList);
        } else if (REQUEST_CODE_CAMERA == requestCode) {
            //拍照处理
            dealImageToEditForCamera();
        }
    }

    /**
     * 获取剩下可以选图片数量
     * @return
     */
    private int getRestNum() {
        return (MAX_IMAGE - editImageList.size());
    }

    private void refreshData() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                editImageAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 把拍照的图片放大编辑文件夹下
     */
    private void dealImageToEditForCamera() {
        dealImageToEdit(Constants.SDCARD_DJJBUYER_CIRCLE_TEMP_CAMREA);
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
        String editFileName = Constants.SDCARD_DJJBUYER_CIRCLE_EDIT + UUIDUtils.createId();
        FileUtil.checkParentFile(editFileName);//编辑时临时存放图片
        Bitmap b = BitmapUtils.changeOppositeSizeMayDegree(filePathName, editFileName, Constants.IMAGE_LIMIT_SIZE, Constants.IMAGE_LIMIT_SIZE, degree);
        if(null != b){
            editImageList.add(editFileName);
            refreshData();
        }
    }

    /**
     * 去拍照
     */
    public void gotoCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        FileUtil.checkParentFile(Constants.SDCARD_DJJBUYER_CIRCLE_TEMP_CAMREA);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Constants.SDCARD_DJJBUYER_CIRCLE_TEMP_CAMREA)));
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    /**
     * 发布
     */
    private void sendCircle(){
        String text = editText.getText().toString();
//        CircleUtil.circleAdd(CircleAddActivity.this , text , files);
        if(Validators.isEmpty(text) && Validators.isEmpty(files)){
            ToastUtil.toast("说点什么呗~");
            return;
        }
        CircleAddTask circleAddTask = new CircleAddTask(this);
        circleAddTask.setShowProgressDialog(false);
        circleAddTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<CircleAddData>() {
            @Override
            public void successCallback(Result<CircleAddData> result) {
                LogUtil.e(result.getMessage());
            }
        });
        circleAddTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<CircleAddData>() {
            @Override
            public void successCallback(Result<CircleAddData> result) {
                OnlyRefrshReceiver.notifyReceiver();
                finish();
            }
        });
        circleAddTask.execute(text, files);
    }

    /**
     * 上传图片获取图片路径
     */
    private void getPicUrl(){
        if (!Validators.isEmpty(editImageList)){
            StringBuilder fileName = new StringBuilder();
            for(String localUrl : editImageList){
                fileName.append(localUrl);
                fileName.append(",");

            }
            fileName.deleteCharAt(fileName.length() - 1);
            UpLoadFileTask upLoadFileTask = new UpLoadFileTask(CircleAddActivity.this);
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
//                    files = BPPreferences.instance().getString(PreferenceConstants.PIC_PATH, "{}");
                    files = result.getMessage();
                    sendCircle();
                }
            });

            upLoadFileTask.execute(fileName.toString() , String.valueOf(editImageList.size()));
        }else{
            sendCircle();
        }

    }


}
