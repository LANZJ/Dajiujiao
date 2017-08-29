package com.zjyeshi.dajiujiao.buyer.activity.my.work;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigapple.lib.utils.uuid.UUIDUtils;
import com.xuan.bigappleui.lib.album.BUAlbum;
import com.xuan.bigappleui.lib.album.entity.ImageItem;
import com.xuan.bigdog.lib.dialog.DGProgressDialog;
import com.xuan.bigdog.lib.location.DGLocationUtils;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.my.work.LeaveConfirmImageAdapter;
import com.zjyeshi.dajiujiao.buyer.circle.task.UpLoadFileTask;
import com.zjyeshi.dajiujiao.buyer.common.Constants;
import com.zjyeshi.dajiujiao.buyer.entity.enums.work.CheckCardEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.work.ClockPropertyEnum;
import com.zjyeshi.dajiujiao.buyer.task.data.other.GetUpLoadFileData;
import com.zjyeshi.dajiujiao.buyer.task.work.TimeCardSitutationTask;
import com.zjyeshi.dajiujiao.buyer.task.work.TimeCardTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.TimeCardData;
import com.zjyeshi.dajiujiao.buyer.task.work.data.TimeCardSitutaionData;
import com.zjyeshi.dajiujiao.buyer.utils.BitmapUtils;
import com.zjyeshi.dajiujiao.buyer.utils.DialogUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.buyer.utils.FileUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ImageUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.buyer.views.other.MyGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * 打卡
 * Created by zhum on 2016/6/13.
 */
public class CardActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;//标题栏

    @InjectView(R.id.timeTv)
    private TextView timeTv;//时间
//    @InjectView(R.id.timePTv)
//    private TextView timePTv;//时间段（AM,PM）
    @InjectView(R.id.locationTv)
    private TextView locationTv;//定位位置
    @InjectView(R.id.mapView)
    private MapView mapView;//地图
    @InjectView(R.id.cardBt)
    private TextView cardBt;//标题栏
    @InjectView(R.id.gridView)
    private MyGridView gridView;
    private LeaveConfirmImageAdapter adapter;
    private List<String> imageList = new ArrayList<String>();

    private BaiduMap baiduMap;
    private Marker marker;//覆盖物

    private TimeThread timeThread;
    private Handler handler = new Handler();

    //上传参数
    private int type = 1 ;//考勤类型，1、普通上下班 ， 2、外出考勤
    private int clockProperty; //打卡性质 ， 1、上班 ， 2、下班
    private String lngE5;
    private String latE5;
    private String address;//详细地址
    private String remark = "";//备注
    private String pics = "";
    private  String mdi="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_card);
        initWidgets();
    }

    private void initWidgets(){
        cardBt.setEnabled(false);
        checkStatus();

        titleLayout.configTitle("打卡").configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        titleLayout.configRightText("考勤记录", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CardActivity.this,AttendanceRecordActivity.class);
                startActivity(intent);
            }
        });

        //打卡
        cardBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPicsAndsend();
            }
        });

        adapter = new LeaveConfirmImageAdapter(CardActivity.this , imageList , true);
        gridView.setAdapter(adapter);

        timeThread = new TimeThread();
        timeThread.start();

        getCurrentTime();
        getLocation();

    }

    private void getCurrentTime(){
        long sysTime = System.currentTimeMillis();
        CharSequence sysTimeStr = DateFormat.format("HH:mm", sysTime);
        timeTv.setText(sysTimeStr); //更新时间
    }

    class TimeThread extends Thread{
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = 1;  //消息(一个整型值)
                    mHandler.sendMessage(msg);// 每隔1秒发送一个msg给mHandler
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    //在主线程里面处理消息并更新UI界面
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    long sysTime = System.currentTimeMillis();
                    CharSequence sysTimeStr = DateFormat.format("HH:mm", sysTime);
                    timeTv.setText(sysTimeStr); //更新时间
                    break;
                default:
                    break;

            }
        }
    };

    //定位
    private void getLocation() {
        DGLocationUtils.init(getApplicationContext(), new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                locationTv.setText(bdLocation.getCity()+bdLocation.getDistrict()
                        +bdLocation.getStreet()+bdLocation.getStreetNumber());
                mdi=bdLocation.getCity()+bdLocation.getDistrict()
                        +bdLocation.getStreet()+bdLocation.getStreetNumber();
                baiduMap = mapView.getMap();
                //设定中心点坐标
                LatLng cenpt =  new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
                //定义地图状态
                MapStatus mMapStatus = new MapStatus.Builder()
                        .target(cenpt)
                        .zoom(17)
                        .build();
                //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
                MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                //改变地图状态
                baiduMap.setMapStatus(mMapStatusUpdate);
                MarkerOptions ooA = new MarkerOptions().position(cenpt).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_address))
                        .zIndex(9).draggable(true);
                marker = (Marker) (baiduMap.addOverlay(ooA));
                DGLocationUtils.stop();

            }
        });
        DGLocationUtils.start();
    }


    //检测打卡状态
    private void checkStatus(){
        TimeCardSitutationTask timeCardSitutationTask = new TimeCardSitutationTask(CardActivity.this);
        timeCardSitutationTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<TimeCardSitutaionData>() {
            @Override
            public void failCallback(Result<TimeCardSitutaionData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        timeCardSitutationTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<TimeCardSitutaionData>() {
            @Override
            public void successCallback(Result<TimeCardSitutaionData> result) {
                cardBt.setEnabled(true);
                CheckCardEnum checkCardEnum = CheckCardEnum.valueOf(result.getValue().getStatus());
                if (checkCardEnum.equals(CheckCardEnum.NOTCARD)){
                    //未打卡
                    cardBt.setText("上班打卡");
                    clockProperty = ClockPropertyEnum.GO.getValue();
                }else if (checkCardEnum.equals(CheckCardEnum.UPCARD)){
                    //上班已打卡
                    cardBt.setText("下班打卡");
                    clockProperty = ClockPropertyEnum.OFF.getValue();

                }else if (checkCardEnum.equals(CheckCardEnum.DOWNCARD)){
                    //下班已打卡
                    cardBt.setText("下班已打卡");
                    cardBt.setEnabled(false);
                }
            }
        });

        timeCardSitutationTask.execute();
    }

    //打卡
    private void timeCard(){
        DGLocationUtils.init(CardActivity.this, new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                address = bdLocation.getCity()+bdLocation.getDistrict() +bdLocation.getStreet()+bdLocation.getStreetNumber();
                lngE5 = String.valueOf((int) (bdLocation.getLongitude() * 100000));
                latE5 = String.valueOf((int) (bdLocation.getLatitude() * 100000));
                TimeCardTask timeCardTask = new TimeCardTask(CardActivity.this);
                timeCardTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<TimeCardData>() {
                    @Override
                    public void failCallback(Result<TimeCardData> result) {
                        ToastUtil.toast(result.getMessage());
                    }
                });

                timeCardTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<TimeCardData>() {
                    @Override
                    public void successCallback(final Result<TimeCardData> result) {
                        cardBt.setEnabled(false);
                        cardBt.setBackgroundResource(R.color.color_9b9b9b);
                        cardBt.setTextColor(Color.parseColor("#353535"));
                        if (result.getValue().getStatus() == 1){
                            //正常
                            DialogUtil.normalClock(CardActivity.this,"打卡成功" , new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            },  new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent();
                                    intent.setClass(CardActivity.this , AddRemarkActivity.class);
                                    intent.putExtra("cardId" , result.getValue().getId());
                                    startActivity(intent);
                                }
                            });
                        }else if (result.getValue().getStatus() == 2){
                            DialogUtil.lateClock(CardActivity.this, "您已经迟到" , new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            }, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent();
                                    intent.setClass(CardActivity.this , AddRemarkActivity.class);
                                    intent.putExtra("cardId" , result.getValue().getId());
                                    startActivity(intent);
                                }
                            });
                        }else{
                            DialogUtil.lateClock(CardActivity.this, "您早退了" , new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            }, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent();
                                    intent.setClass(CardActivity.this , AddRemarkActivity.class);
                                    intent.putExtra("cardId" , result.getValue().getId());
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                });

                timeCardTask.execute(String.valueOf(type) , String.valueOf(clockProperty) , lngE5 ,
                        latE5 , address , remark , pics);

                DGLocationUtils.stop();
            }
        });

        DGLocationUtils.start();
    }

    private void uploadPicsAndsend(){
        String files = ExtraUtil.list2String(imageList);
        if (Validators.isEmpty(files)){
            timeCard();
        }else{
            UpLoadFileTask upLoadFileTask = new UpLoadFileTask(CardActivity.this);
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
                    pics = result.getMessage();
                    timeCard();
                }
            });

            upLoadFileTask.execute(files , String.valueOf(imageList.size()));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK != resultCode) {
            return;
        }

        if (AskForLeaveActivity.REQUEST_CODE_ALBUM == requestCode) {
            //图库选择处理
            List<ImageItem> selectedList = BUAlbum.getSelListAndClear();
            dealImageToEditForAlbum(selectedList);
        } else if (AskForLeaveActivity.REQUEST_CODE_CAMERA == requestCode) {
            //拍照处理
            dealImageToEditForCamera();
        }
    }

    //刷新
    private void refreshData() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 把拍照的图片放大编辑文件夹下
     */
    private void dealImageToEditForCamera() {
        long sysTime = System.currentTimeMillis();
        CharSequence sysTimeStr = DateFormat.format("HH:mm", sysTime);
        Bitmap r= ImageUtil.getBitmap(Constants.SDCARD_DJJBUYER_WORK_TEMP_CAMREA);
        Bitmap textBitmap = ImageUtil.drawTextToLeftTop(this, r, sysTimeStr.toString(),18, Color.WHITE, 8, 9);
        textBitmap = ImageUtil.drawTextToRightBottom(this, textBitmap, mdi, 10, Color.WHITE, 9, 9);
        String ee= ImageUtil.saveBitmap(CardActivity.this,textBitmap);
        if(null != ee){
            imageList.add(ee);
            refreshData();
        }
        //dealImageToEdit(Constants.SDCARD_DJJBUYER_WORK_TEMP_CAMREA);
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
//        Bitmap textBitmap = ImageUtil.drawTextToLeftTop(this, b, "左上角", 16, Color.RED, 0, 0);
//        textBitmap = ImageUtil.drawTextToRightBottom(this, textBitmap, "右下角", 16, Color.RED, 0, 0);
//        textBitmap = ImageUtil.drawTextToRightTop(this, textBitmap, "右上角", 16, Color.RED, 0, 0);
//        textBitmap = ImageUtil.drawTextToLeftBottom(this, textBitmap, "左下角", 16, Color.RED, 0, 0);
//        textBitmap = ImageUtil.drawTextToCenter(this, textBitmap, "中间", 16, Color.RED);

        if(null != b){
            imageList.add(editFileName);
            refreshData();
        }
    }
}