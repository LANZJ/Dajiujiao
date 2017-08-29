package com.zjyeshi.dajiujiao.buyer.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.ioc.ViewUtils;
import com.zjyeshi.dajiujiao.buyer.views.other.MyGridView;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.circle.itemview.BaseView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuhk on 2016/12/14.
 */
public class PathWidget extends BaseView {
    @InjectView(R.id.gridView)
    private MyGridView gridView;
    @InjectView(R.id.pathDesTv)
    private TextView pathDesTv;

    private List<PathData> dataList = new ArrayList<PathData>();
    private PathAdapter pathAdapter;

    public PathWidget(Context context) {
        super(context);
    }

    public PathWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void dgInit() {
        inflate(getContext() , R.layout.widget_path , this);
        ViewUtils.inject(this , this);
    }


    public void bindData(List<PathData> pathDataList){
        dataList.clear();
        dataList.addAll(pathDataList);
        pathAdapter = new PathAdapter(getContext() , dataList);
        gridView.setAdapter(pathAdapter);

    }

    public TextView getPathDesTv() {
        return pathDesTv;
    }
}
