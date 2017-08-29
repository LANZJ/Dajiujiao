package com.jopool.crow.imkit.expression;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html.ImageGetter;

/**
 * 表情标签图片显示
 * 
 * @author xuan
 */
public class ExpressionImageGetter implements ImageGetter {
	private final Context context;
	private final int width;
	private final int height;

	public ExpressionImageGetter(Context context, int width, int height) {
		this.context = context;
		this.width = width;
		this.height = height;
	}

	@Override
	public Drawable getDrawable(String source) {
		Expression expression = ChatImagesResource.getExpressionmap().get(
				source);
		Drawable drawable = context.getResources().getDrawable(
				expression.getShowImage());
		drawable.setBounds(0, 0, width, height);
		return drawable;
	}

}
