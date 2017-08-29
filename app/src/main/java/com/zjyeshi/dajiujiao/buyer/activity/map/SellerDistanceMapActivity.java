package com.zjyeshi.dajiujiao.buyer.activity.map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.xuan.bigapple.lib.utils.ToastUtils;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.utils.SingleSelectDialogUtil;
import com.xuan.bigdog.lib.location.DGPoint;
import com.xuan.bigdog.lib.location.activity.interfaces.DGRoutePlanInterface;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 自己与商家的距离地图
 * Created by wuhk on 2015/11/20.
 */
public class SellerDistanceMapActivity extends BaseMapActivity{
    private Double sellerLat;
    private Double sellerLng;

    private Double myLat;
    private Double myLng;

    private String shopName;
    private String address;

    public static final String PARAM_MAP_LAT = "param.map.lat";
    public static final String PARAM_MAP_LNG = "param.map.lng";
    public static final String PARAM_MAP_MY_LAT = "param.map.my.lat";
    public static final String PARAM_MAP_MY_LNG = "param.map.my.lng";
    public static final String PARAM_MAP_SHOP_NAME = "param.map.shop.name";
    public static final String PARAM_MAP_ADDRESS = "param.map.address";

    private DGRoutePlanInterface routePlanInterface;//路线
    @Override
    public void onCreate(Bundle savedInstanceState) {
        sellerLat = Double.parseDouble(getIntent().getStringExtra(PARAM_MAP_LAT))/100000;
        sellerLng = Double.parseDouble(getIntent().getStringExtra(PARAM_MAP_LNG))/100000;

        myLat = Double.parseDouble(getIntent().getStringExtra(PARAM_MAP_MY_LAT))/100000;
        myLng = Double.parseDouble(getIntent().getStringExtra(PARAM_MAP_MY_LNG))/100000;

        shopName = getIntent().getStringExtra(PARAM_MAP_SHOP_NAME);
        address = getIntent().getStringExtra(PARAM_MAP_ADDRESS);

        super.onCreate(savedInstanceState);

        routePlanInterface = this ;

        //设置步行导航路线
        routePlanInterface.doRoutePlanWalking(new DGPoint(myLat, myLng), new DGPoint(sellerLat, sellerLng));
    }

    @Override
    protected View configTitleView() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_map_title, null);
        LinearLayout backLayout = (LinearLayout)view.findViewById(R.id.backLayout);
        TextView titleTv = (TextView)view.findViewById(R.id.titleTv);
        titleTv.setText(shopName);
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        return view;
    }

    @Override
    protected View configBottomView() {
        View view  = LayoutInflater.from(this).inflate(R.layout.layout_map_bottom , null);
        TextView shopNameTv = (TextView)view.findViewById(R.id.shopNameTv);
        TextView addressTv = (TextView)view.findViewById(R.id.addressTv);
        RelativeLayout leadLayout = (RelativeLayout)view.findViewById(R.id.leadLayout);

        shopNameTv.setText(shopName);
        addressTv.setText(address);

        leadLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<String> itemList = new ArrayList<String>();
                List<View.OnClickListener> onClickListenerList = new ArrayList<View.OnClickListener>();
                itemList.add("百度地图");
                onClickListenerList.add(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LatLng pt1 = new LatLng(myLat, myLng);
                        LatLng pt2 = new LatLng(sellerLat, sellerLng);

                        // 构建 导航参数
                        NaviParaOption para = new NaviParaOption()
                                .startPoint(pt1).endPoint(pt2);

                        try {
                            BaiduMapNavigation.openBaiduMapNavi(para,getApplicationContext());
                        } catch (BaiduMapAppNotSupportNaviException e) {
                            e.printStackTrace();
                            showDialog();
                        }
                    }
                });
                itemList.add("高德地图");
                onClickListenerList.add(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String packageName = "com.autonavi.minimap";
                        if (isInstallByread(packageName)) {
                            try {
                                DGPoint gdPoint = changeBdToGc(new DGPoint(sellerLat , sellerLng));
                                String url = "androidamap://navi?sourceApplication=王朝大酒窖&lat=" + gdPoint.lat + "&lon=" + gdPoint.lng + "&dev=1&style=2";
                                Intent intent = Intent.getIntent(url);
                                intent.addCategory("android.intent.category.DEFAULT");
                                startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            ToastUtils.displayTextShort("请先安装高德地图");
                        }
                    }
                });
                itemList.add("腾讯地图");
                onClickListenerList.add(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String packageName = "com.tencent.map";
                        if (isInstallByread(packageName)) {
                            try {
                                DGPoint gdMyPoint = changeBdToGc(new DGPoint(myLat , myLng));
                                DGPoint gdSellerPoint = changeBdToGc(new DGPoint(sellerLat , sellerLng));
                                String url = "qqmap://map/routeplan?type=bus&from=我的位置&fromcoord=" + gdMyPoint.lat + "," + gdMyPoint.lng + "&to=" + address + "&tocoord=" + gdSellerPoint.lat + "," + gdSellerPoint.lng + "&policy=1&referer=王朝大酒窖";
                                Intent intent = Intent.getIntent(url);
                                intent.addCategory("android.intent.category.DEFAULT");
                                startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            ToastUtils.displayTextShort("请先安装腾讯地图");
                        }
                    }
                });
                SingleSelectDialogUtil dialog = new SingleSelectDialogUtil.Builder(SellerDistanceMapActivity.this)
                        .setItemTextAndOnClickListener(
                                itemList.toArray(new String[itemList.size()]),
                                onClickListenerList
                                        .toArray(new View.OnClickListener[onClickListenerList
                                                .size()])).createInstance();
                dialog.show();
            }
        });
        return view;
    }

    @Override
    protected DGPoint configCenterPoint() {
        return new DGPoint(myLat ,myLng);
    }

    @Override
    public void markLocation(List<DGPoint> locationPointList) {
        super.markLocation(locationPointList);
    }
    /**
     * 提示未安装百度地图app或app版本过低
     */
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                OpenClientUtil.getLatestBaiduMapApp(SellerDistanceMapActivity.this);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();

    }

    /**
     * 判断是否安装目标应用
     * @param packageName 目标应用安装后的包名
     * @return 是否已安装目标应用
     */
    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    /**
     * 将百度坐标转换成国测局坐标
     * @param point
     * @return
     */
    private DGPoint changeBdToGc(DGPoint point){
        double pi = 3.14159265358979324 * 3000.0 / 180.0;
        double x = point.lng - 0.0065 , y = point.lat - 0.006;
        double z = Math.sqrt(x*x + y*y) - 0.00002 * Math.sin(y * pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * pi);
        DGPoint gcPoint = new DGPoint(z*Math.sin(theta) , z*Math.cos(theta));
        return gcPoint;
    }
}
