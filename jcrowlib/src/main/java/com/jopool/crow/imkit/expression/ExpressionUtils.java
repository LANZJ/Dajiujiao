package com.jopool.crow.imkit.expression;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.jopool.crow.imlib.utils.CWDisplayUtil;
import com.jopool.crow.imlib.utils.CWLogUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 表情文本工具
 * 
 * @author xuan
 */
public abstract class ExpressionUtils {

	/**
	 * 设置带有表情的文本
	 * 
	 * @param activity
	 * @param tv
	 * @param content
	 */
	public static void setText(Activity activity, TextView tv, String content) {
		int bitmapSize = (int) CWDisplayUtil.getPxByDp(activity, 25);
		tv.setText(getExpressionString(activity, content, bitmapSize));
	}

	private static SpannableString getExpressionString(Context context,
			String str, int bitmapSize) {
		SpannableString spannableString = new SpannableString(str);
		// 正则表达式比配字符串里是否含有表情，如： 我好[开心]啊
		String zhengze = "\\[[^\\]]+\\]";
		// 通过传入的正则表达式来生成一个pattern
		Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
		try {
			dealExpression(context, spannableString, sinaPatten, 0, bitmapSize);
		} catch (Exception e) {
			CWLogUtil.e(e.getMessage(), e);
		}
		return spannableString;
	}

	/** 处理表情 */
	private static void dealExpression(Context context,
			SpannableString spannableString, Pattern patten, int start,
			int bitmapSize) throws Exception {
		Matcher matcher = patten.matcher(spannableString);
		while (matcher.find()) {
			String key = matcher.group();
			// 从start开始，但是匹配到的还没超过start的位置
			if (matcher.start() < start) {
				continue;
			}
			Expression expression = ChatImagesResource.getExpressionmap().get(
					key);
			if (null == expression) {
				// 虽然符合[xxx]这种格式，但是没有对应的表情
				continue;
			}

			Bitmap bitmap = BitmapFactory.decodeResource(
					context.getResources(), expression.getShowImage());
			bitmap = Bitmap.createScaledBitmap(bitmap, bitmapSize, bitmapSize,
					true);
			ImageSpan imageSpan = new ImageSpan(context, bitmap);
			// 计算该图片名字的长度，也就是要替换的字符串的长度
			int end = matcher.start() + key.length();
			// 将该图片替换字符串中规定的位置中
			spannableString.setSpan(imageSpan, matcher.start(), end,
					Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			if (end < spannableString.length()) {
				// 如果整个字符串还未验证完，则继续。。
				dealExpression(context, spannableString, patten, end,
						bitmapSize);
			}
			break;
		}
	}

}
