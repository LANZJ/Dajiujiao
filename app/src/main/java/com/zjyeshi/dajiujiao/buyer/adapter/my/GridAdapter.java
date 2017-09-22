package com.zjyeshi.dajiujiao.buyer.adapter.my;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.xuan.bigappleui.lib.view.photoview.app.BUViewImageUtils;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.my.work.Rceyzs;
import com.zjyeshi.dajiujiao.buyer.activity.store.ShowGoodsMultiActivity;
import com.zjyeshi.dajiujiao.buyer.circle.ShowMultiImageActivity;
import com.zjyeshi.dajiujiao.buyer.entity.good.AllGoodInfo;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangjian on 2017/9/15.
 */

public class GridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener, View.OnLongClickListener {
    private Context mcontext;
    private List<AllGoodInfo> dataList;
    private boolean isMarketGoods;
    private String memberId;
    private AllGoodInfo allGoodInfo;
    private List<AllGoodInfo> allGoodList = new ArrayList<>();
    private boolean glps=true;
    //自定义监听事件
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view);
        void onItemLongClick(View view);
    }
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnItemClickListener = listener;
    }
    //适配器初始化
    public GridAdapter(Context context,List<AllGoodInfo> datas, boolean glps) {
        mcontext=context;
        this.dataList=datas;
        this.glps=glps;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (glps){
        View view = LayoutInflater.from(mcontext).inflate(R.layout.grid_item,parent, false);
        MyViewHolder holder= new MyViewHolder(view);
        //给布局设置点击和长点击监听
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return holder;

    }else {
       Thong holder2=new Thong(LayoutInflater.from(mcontext).inflate(R.layout.grid_items, parent, false));//这个布局就是一个textview用来显示页数
        LayoutInflater.from(mcontext).inflate(R.layout.grid_items, parent, false).setOnClickListener(this);
        LayoutInflater.from(mcontext).inflate(R.layout.grid_items, parent, false).setOnLongClickListener(this);
        return holder2;
    }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final AllGoodInfo data = dataList.get(position);
        if(glps){
            GlideImageUtil.glidImage(  ((MyViewHolder) holder).pic , ExtraUtil.getResizePic(data.getGoodIcon() , 200 , 200), R.drawable.default_img);
            ((MyViewHolder) holder).namep.setText(data.getGoodName());
            ((MyViewHolder) holder).gage.setText(data.getBottlesPerBox()+"瓶／箱");
            ((MyViewHolder) holder).geige.setText(data.getGoodPrice()+"／瓶");
        }else {
            ((com.zjyeshi.dajiujiao.buyer.adapter.my.GridAdapter.Thong) holder).gages.setText(data.getGoodName());
            GlideImageUtil.glidImage( ((com.zjyeshi.dajiujiao.buyer.adapter.my.GridAdapter.Thong) holder).pics , ExtraUtil.getResizePic(data.getGoodIcon() , 200 , 200), R.drawable.default_img);
        }


    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(view);
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if (mOnItemClickListener!= null) {
            mOnItemClickListener.onItemLongClick(view);
        }
        return false;
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView namep;
        private TextView geige;
        private TextView gage;
        private ImageView pic;

        public MyViewHolder(View itemView) {
            super(itemView);
            namep = (TextView) itemView.findViewById(R.id.nameTv);
            gage = (TextView) itemView.findViewById(R.id.desTv);
            geige = (TextView) itemView.findViewById(R.id.bigPriceTv);
            pic = (ImageView) itemView.findViewById(R.id.photoIv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getPosition();

                    List<AllGoodInfo> tempList = JSONArray.parseArray(JSONArray.toJSONString(dataList), AllGoodInfo.class);
                    allGoodList.clear();
                    allGoodList.addAll(tempList);
                    for (int i = 0; i < allGoodList.size(); i++) {
                        AllGoodInfo good = allGoodList.get(i);
                        good.setGoodIcon(good.getGoodIcon() + "," + i);
                    }

                    allGoodInfo = allGoodList.get(position);
                    String[] imageArray = new String[allGoodList.size()];
                    for (int i = 0; i < allGoodList.size(); i++) {
                        AllGoodInfo info = allGoodList.get(i);
                        imageArray[i] = info.getGoodIcon();
                    }
                    BUViewImageUtils.gotoViewImageActivity(mcontext, imageArray, position, ShowMultiImageActivity.LOADTYPE_CIRCLE, new Object[]{JSONArray.toJSONString(allGoodList)}, ShowGoodsMultiActivity.class);

                }
            });

        }



//    //添加一个item
//    public void addItem(AllGoodInfo all, int position) {
//        all.add(position, all);
//        notifyItemInserted(position);
//        recyclerview.scrollToPosition(position);//recyclerview滚动到新加item处
//    }
//
//    //删除一个item
//    public void removeItem(final int position) {
//        meizis.remove(position);
//        notifyItemRemoved(position);
//    }

    }
    class Thong extends RecyclerView.ViewHolder {
        private TextView gages;
        private ImageView pics;

        public Thong(View itemView) {
            super(itemView);
            gages = (TextView) itemView.findViewById(R.id.nameTv);
            pics = (ImageView) itemView.findViewById(R.id.photoIv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getPosition();
                    List<AllGoodInfo> tempList = JSONArray.parseArray(JSONArray.toJSONString(dataList), AllGoodInfo.class);
                    allGoodList.clear();
                    allGoodList.addAll(tempList);
                    for (int i = 0; i < allGoodList.size(); i++) {
                        AllGoodInfo good = allGoodList.get(i);
                        good.setGoodIcon(good.getGoodIcon() + "," + i);}
                    allGoodInfo = allGoodList.get(position);
                    String[] imageArray = new String[allGoodList.size()];
                    for (int i = 0; i < allGoodList.size(); i++) {
                        AllGoodInfo info = allGoodList.get(i);
                        imageArray[i] = info.getGoodIcon();}
                    BUViewImageUtils.gotoViewImageActivity(mcontext, imageArray, position, ShowMultiImageActivity.LOADTYPE_CIRCLE, new Object[]{JSONArray.toJSONString(allGoodList)}, ShowGoodsMultiActivity.class);

                }
            });
        }
    }
}
