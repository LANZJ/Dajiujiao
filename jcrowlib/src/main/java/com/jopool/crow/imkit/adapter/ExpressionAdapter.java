package com.jopool.crow.imkit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jopool.crow.R;
import com.jopool.crow.imkit.expression.Expression;

import java.util.List;

/**
 * 表情适配器
 *
 * @author xuan
 */
public class ExpressionAdapter extends CWBaseAdapter {
    private final Context context;
    private final List<Expression> dataList;

    public ExpressionAdapter(Context context, List<Expression> dataList) {
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
            view = LayoutInflater.from(context).inflate(R.layout.cw_chat_view_conversaion_expression, null);
        }
        ImageView iconIv = (ImageView) view.findViewById(R.id.iconIv);
        Expression expression = dataList.get(position);
        iconIv.setImageResource(expression.getShowImage());
        return view;
    }

}
