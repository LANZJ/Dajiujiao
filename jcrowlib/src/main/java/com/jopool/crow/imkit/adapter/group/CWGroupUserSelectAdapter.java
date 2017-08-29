package com.jopool.crow.imkit.adapter.group;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.jopool.crow.imkit.utils.lettersort.entity.ItemContent;
import com.jopool.crow.imkit.utils.lettersort.entity.ItemDivide;
import com.jopool.crow.imkit.utils.lettersort.entity.ItemTopContent;
import com.jopool.crow.imkit.utils.lettersort.view.LetterSortAdapter;
import com.jopool.crow.imkit.view.group.CWLetterContentView;
import com.jopool.crow.imkit.view.group.CWLetterDividerView;

import java.util.List;

/**
 * 字母排序列表适配器
 * Created by wuhk on 2016/11/7.
 */
public class CWGroupUserSelectAdapter extends LetterSortAdapter {
    private Context context;

    public CWGroupUserSelectAdapter(List<ItemContent> fromList, Context context) {
        super(fromList, context);
        this.context = context;
    }

    @Override
    public View drawItemContent(int position, View convertView, ViewGroup parent, ItemContent itemContent) {
        convertView = new CWLetterContentView(context);
        ((CWLetterContentView) convertView).bindData(itemContent);
        return convertView;
    }

    @Override
    public View drawItemDivide(int position, View convertView, ViewGroup parent, ItemDivide itemDivide) {
        convertView = new CWLetterDividerView(context);
        ((CWLetterDividerView) convertView).bindData(itemDivide);
        return convertView;
    }

    @Override
    public View drawItemTopContent(int position, View convertView, ViewGroup parent, ItemTopContent itemTopContent) {
        return null;
    }
}
