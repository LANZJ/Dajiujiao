package com.jopool.crow.imkit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jopool.crow.R;

import java.util.List;

/**
 * 聊天工具图片适配器
 *
 * @author xuan
 */
public class ToolsImageAdapter extends CWBaseAdapter {
    private final Context context;
    private final List<ToolItem> dataList;

    public ToolsImageAdapter(Context context, List<ToolItem> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (null == view) {
            view = LayoutInflater.from(context).inflate(R.layout.cw_chat_view_conversation_tool, null);
        }
        //
        ImageView iconIv = (ImageView) view.findViewById(R.id.iconIv);
        TextView nameTv = (TextView) view.findViewById(R.id.nameTv);
        //
        ToolItem toolItem = dataList.get(position);
        iconIv.setImageResource(toolItem.getIconResid());
        nameTv.setText(context.getResources().getString(toolItem.getNameResid()));
        return view;
    }

    public static class ToolItem {
        private int iconResid;
        private int nameResid;

        public int getIconResid() {
            return iconResid;
        }

        public void setIconResid(int iconResid) {
            this.iconResid = iconResid;
        }

        public int getNameResid() {
            return nameResid;
        }

        public void setNameResid(int nameResid) {
            this.nameResid = nameResid;
        }
    }

}
