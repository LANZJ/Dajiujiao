package com.zjyeshi.dajiujiao.buyer.widgets.uploadMultImage;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.zjyeshi.dajiujiao.buyer.views.other.MyGridView;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.model.UploadGridViewImageModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuhk on 2016/8/23.
 */
public class GridViewImageWidget extends LinearLayout {
    public static int MAX_IMAGE_SIZE = 5;
    private UploadGridViewImageModel uploadGridViewImageModel = new UploadGridViewImageModel();

    public UploadGridViewImageModel getUploadGridViewImageModel() {
        return uploadGridViewImageModel;
    }

    private MyGridView gridView;
    private GridViewImageAdapter adapter;
    private List<String> dataList = new ArrayList<String>();

    public List<String> getDataList() {
        return dataList;
    }

    public GridViewImageWidget(Context context) {
        super(context);
        init();
    }

    public GridViewImageWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        inflate(getContext() , R.layout.widget_gridview_image , this);
        gridView = (MyGridView)findViewById(R.id.gridView);
        adapter = new GridViewImageAdapter(getContext() , dataList , uploadGridViewImageModel);
        gridView.setAdapter(adapter);
    }
}
