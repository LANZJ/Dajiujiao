package com.jopool.crow.imkit.utils.lettersort.utils;

import android.content.Context;

import com.jopool.crow.imkit.utils.lettersort.entity.BaseItem;
import com.jopool.crow.imkit.utils.lettersort.entity.ItemContent;
import com.jopool.crow.imkit.utils.lettersort.entity.ItemDivide;
import com.jopool.crow.imkit.utils.lettersort.entity.ItemTopContent;
import com.jopool.crow.imkit.utils.pinyin.CWPinyinUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * 给列表数据排序用
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-7-16 下午3:30:18 $
 */
public abstract class LetterSortUtils {

	/**
	 * 过去出ItemTopContent的项目
	 * 
	 * @param fromItemList
	 * @return
	 */
	private static List<ItemTopContent> filterTopContent(
			List<ItemContent> fromItemList) {
		List<ItemTopContent> topContentList = new ArrayList<ItemTopContent>();
		Iterator<ItemContent> iter = fromItemList.iterator();
		while (iter.hasNext()) {
			ItemContent ic = iter.next();
			if (ic instanceof ItemTopContent) {
				topContentList.add((ItemTopContent) ic);
				iter.remove();
			}
		}

		return topContentList;
	}

	/**
	 * 放回去ItemTopContent
	 * 
	 * @param topContentList
	 * @param fromItemList
	 */
	private static void addBackTopContent(List<ItemTopContent> topContentList,
			List<BaseItem> fromItemList) {
		// 逆序排
		Collections.sort(topContentList, new Comparator<ItemTopContent>() {
			@Override
			public int compare(ItemTopContent left, ItemTopContent right) {
				if (left.getLevel() > right.getLevel()) {
					return -1;
				} else if (left.getLevel() < right.getLevel()) {
					return 1;
				} else {
					return 0;
				}
			}
		});
		for (ItemTopContent itc : topContentList) {
			fromItemList.add(0, itc);
		}
	}

	/**
	 * 根据字母排序，并加入了字母分割线哦
	 *
	 * @param fromItemList
	 * @param context
	 * @return
	 */
	public static List<BaseItem> sortOrderLetter(
			List<ItemContent> fromItemList, Context context) {
		if (null == fromItemList || fromItemList.isEmpty()) {
			return Collections.emptyList();
		}

		// 过滤出topContent的项目
		List<ItemTopContent> topContentList = filterTopContent(fromItemList);

		List<BaseItem> ret = new ArrayList<BaseItem>();

		Map<String, List<BaseItem>> letter2ItemListMap = groupByFirstLetter(
				fromItemList, context);

		// 拎出无法识别的name，删除之，因为要放在最后
		List<BaseItem> unknowNameList = letter2ItemListMap.get("#");
		if (null != unknowNameList) {
			letter2ItemListMap.remove("#");
		}
		// 通过key排序map
		Map<String, List<BaseItem>> tempMap = new TreeMap<>(
				new Comparator<Object>() {
					public int compare(Object obj1, Object obj2) {
						String v1 = (String) obj1;
						String v2 = (String) obj2;
						int s = v1.compareTo(v2);
						return s;
					}
				}
		);

		Set col = letter2ItemListMap.keySet();
		Iterator iter = col.iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			List<BaseItem> value = letter2ItemListMap.get(key);
			tempMap.put(key, value);
		}

		for (Map.Entry<String, List<BaseItem>> entry : tempMap
				.entrySet()) {
			String key = entry.getKey();
			List<BaseItem> itemList = entry.getValue();
			ItemDivide itemDivide = new ItemDivide(key);
			ret.add(itemDivide);
			for (BaseItem item : itemList) {
				ret.add(item);
			}
		}

		// 在后面添加上无法识别的名字
		if (null != unknowNameList) {
			ret.add(new ItemDivide("#"));
			for (BaseItem item : unknowNameList) {
				ret.add(item);
			}
		}

		addBackTopContent(topContentList, ret);

		return ret;
	}

	/**
	 * 按首字母分类,Map返回
	 *
	 * @param fromItemList
	 * @param context
	 * @return
	 */
	private static Map<String, List<BaseItem>> groupByFirstLetter(
			List<ItemContent> fromItemList, Context context) {
		Map<String, List<BaseItem>> ret = new HashMap<String, List<BaseItem>>();

		for (ItemContent item : fromItemList) {
			String firstLetter = CWPinyinUtil.toPinyinUpperF(item.getName());

			List<BaseItem> itemList = ret.get(firstLetter);
			if (null == itemList) {
				itemList = new ArrayList<BaseItem>();
				ret.put(firstLetter, itemList);
			}

			itemList.add(item);
		}

		return ret;
	}

}
