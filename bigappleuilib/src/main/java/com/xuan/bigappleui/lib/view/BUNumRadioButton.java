package com.xuan.bigappleui.lib.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.xuan.bigappleui.lib.utils.BUDisplayUtil;

/**
 * 自定义RadioButton做tab按钮，可在上面绘制未读消息数
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-10-29 下午2:37:01 $
 */
public class BUNumRadioButton extends RadioButton {
	public static final int NOT_INIT_BITMAP = -1;// 不加载背景
	public static final int TRANSPARENT_BITMAP = 0;// 使用默认透明的背景

	/** 绘制画笔 */
	private Paint paint;

	/** 需要绘制的未读消息数 */
	private int num;
	/** 未读消息数的背景:数字小于10的 */
	private Bitmap drawBitmap;
	/**未读消息的背景:数字大于等于10小于100的*/
	private Bitmap drawBitmap2;

	/** 控件的宽高 */
	private int width;

	/** 未读提示默认是在图标的右上角，用下面两个参数可以进行偏移调整，右上角为（0，0）点，左方和下方为正方向 */
	private float offsetWidth = 0;
	private float offsetHeight = 0;

	/** 数字字体大小 */
	private float paintTextSize = 0;
	/** 数字字体颜色 */
	private int paintColor = Color.WHITE;

	/** 判断是否需要绘制数字：true绘制false不绘制 */
	private boolean canDrawNum = true;// 是否进行数字绘制

	/**
	 * 构造方法
	 * 
	 * @param context
	 */
	public BUNumRadioButton(Context context) {
		super(context);
		init();
	}

	/**
	 * 构造方法
	 * 
	 * @param context
	 * @param attrs
	 */
	public BUNumRadioButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	/**
	 * 构造方法
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public BUNumRadioButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	/**
	 * 初始化操作
	 */
	private void init() {
		paint = new Paint();
		paint.setTextAlign(Align.CENTER);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		this.width = w;
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (null == drawBitmap) {
			return;
		}

		if (!canDrawNum) {
			// 表示只画一个背景
			int left = width - drawBitmap.getWidth();
			canvas.drawBitmap(drawBitmap, left - offsetWidth, 0 + offsetHeight,
					paint);
		} else {
			if (num > 0) {
				// 设置画笔参数
				if (0 == paintTextSize) {
					paintTextSize = drawBitmap.getWidth() * 0.8f;// 字体的图片默认根据背景的大小来调整
				}
				paint.setTextSize(paintTextSize);
				paint.setColor(paintColor);

				// 画上红色的圈圈，如果没有初始化过背景就不绘制背景
				Bitmap wantToDrawBitmap = null;
				if(num < 10){
					wantToDrawBitmap = drawBitmap;
				}else{
					wantToDrawBitmap = drawBitmap2;
				}
				int left = width - wantToDrawBitmap.getWidth();
				canvas.drawBitmap(wantToDrawBitmap, left - offsetWidth,
						0 + offsetHeight, paint);

				//绘制未读消息的数字
				int x = (int) (width - wantToDrawBitmap.getWidth() / 2d);
				int y = (int) (wantToDrawBitmap.getHeight() * 0.8d);

				String text = num > 99 ? "99+" : String.valueOf(num);
				canvas.drawText(text, x - offsetWidth, y + offsetHeight, paint);
			} else {
				// num小于等于0不进行绘制
			}
		}
	}

	/**
	 * 获取绘制的未读消息数字
	 * 
	 * @return
	 */
	public int getNum() {
		return num;
	}

	/**
	 * 只设置未读消息的背景，不绘制数字，所以内部会把canDrawNum设置成false
	 * 
	 * @param resid
	 *            绘制消息提示红点
	 */
	public void setPoint(int resid) {
		canDrawNum = false;
		initDrawBitmap(resid, resid);
		invalidate();
	}

	/**
	 * 设置未读消息数，resid是需要绘制的背景
	 * 
	 * @param n
	 *            如果n小于等于0，不会绘制未读消息数
	 * @param resid
	 *            绘制未读消息数的背景
	 */
	public void setNum(int n, int resid) {
		setNum(n, resid, resid);
	}

	/**
	 * 设置未读消息数，resid是需要绘制的背景
	 *
	 * @param n 如果n小于等于0，不会绘制未读消息数
	 * @param resid 绘制未读消息数的背景:小于10的背景
	 * @param resid2 绘制未读消息数的背景:大于等于10,小于100的背景
	 */
	public void setNum(int n, int resid, int resid2){
		if (n != num) {
			canDrawNum = true;
			this.num = n;
			initDrawBitmap(resid, resid2);
			invalidate();
		}
	}

	/**
	 * 设置未读消息数，背景为透明，即是显示数字
	 * 
	 * @param n
	 *            如果n小于等于0，不会绘制未读消息数
	 */
	public void setNum(int n) {
		setNum(n, BUNumRadioButton.TRANSPARENT_BITMAP);
	}

	/**
	 * 去掉未读消息数，会去掉背景和数字
	 */
	public void clearNum() {
		initDrawBitmap(NOT_INIT_BITMAP, NOT_INIT_BITMAP);
		invalidate();
	}

	// /////////////////////////////////////////////设置数字体颜色////////////////////////////////////////////////
	/**
	 * 设置数字体颜色
	 * 
	 * @param paintColor
	 *            color的颜色值
	 */
	public void setPaintColor(int paintColor) {
		this.paintColor = paintColor;
	}

	// /////////////////////////////////////////////设置数字体大小///////////////////////////////////////////////
	/**
	 * 设置数字大小
	 * 
	 * @param paintTextSizePx
	 *            px为单位
	 */
	public void setPaintTextSizeByPx(float paintTextSizePx) {
		this.paintTextSize = paintTextSizePx;
	}

	/**
	 * 设置数字大小
	 * 
	 * @param paintTextSizeSp
	 *            dp为单位
	 */
	public void setPaintTextSizeBySp(float paintTextSizeSp) {
		this.paintTextSize = getPxBySp(paintTextSizeSp);
	}

	/**
	 * 设置数字大小
	 * 
	 * @param resId
	 *            dimens文件中的值设置
	 */
	public void setPaintTextSizeByRes(int resId) {
		this.paintTextSize = getResources().getDimension(resId);
	}

	// //////////////////////////////////////////设置未读块的偏移////////////////////////////////////////////////
	/**
	 * 设置提示点距离右边的距离
	 * 
	 * @param offsetWidthPx
	 *            px为单位
	 */
	public void setOffsetWidthByPx(float offsetWidthPx) {
		this.offsetWidth = offsetWidthPx;
	}

	/**
	 * 设置提示点距离右边的距离
	 * 
	 * @param offsetWidthDp
	 *            dp为单位
	 */
	public void setOffsetWidthByDp(float offsetWidthDp) {
		this.offsetWidth = getPxByDp(offsetWidthDp);
	}

	/**
	 * 设置提示点距离右边的距离
	 * 
	 * @param resId
	 *            dimens中的值为单位
	 */
	public void setOffsetWidthByRes(int resId) {
		this.offsetWidth = getResources().getDimension(resId);
	}

	/**
	 * 设置提示点距离上边的距离
	 * 
	 * @param offsetHeightPx
	 *            px为单位
	 */
	public void setOffsetHeightByPx(float offsetHeightPx) {
		this.offsetHeight = offsetHeightPx;
	}

	/**
	 * 设置提示点距离上边的距离
	 * 
	 * @param offsetHeightDp
	 *            dp为单位
	 */
	public void setOffsetHeightByDp(float offsetHeightDp) {
		this.offsetHeight = getPxByDp(offsetHeightDp);
	}

	/**
	 * 设置提示点距离上边的距离
	 * 
	 * @param resId
	 *            dimens文件中的值
	 */
	public void setOffsetHeightByRes(int resId) {
		this.offsetHeight = getResources().getDimension(resId);
	}

	// //////////////////////////////////////////内置工具方法////////////////////////////////////////////////
	/**
	 * sp单位转换成px
	 * 
	 * @param sp
	 * @return
	 */
	private float getPxBySp(float sp) {
		Context context = getContext();
		if (context instanceof Activity) {
			return BUDisplayUtil.getPxBySp((Activity) context, sp);
		} else {
			return sp;
		}
	}

	/**
	 * dp单位转换成px
	 * 
	 * @param dp
	 * @return
	 */
	private float getPxByDp(float dp) {
		Context context = getContext();
		if (context instanceof Activity) {
			return BUDisplayUtil.getPxByDp((Activity) context, dp);
		} else {
			return dp;
		}
	}

	/**
	 * 设置背景图资源
	 *
	 * @param resid
	 * @param resid2
	 */
	private void initDrawBitmap(int resid, int resid2) {
		if (BUNumRadioButton.NOT_INIT_BITMAP == resid) {
			drawBitmap = null;
			return;
		}

		if (BUNumRadioButton.TRANSPARENT_BITMAP == resid) {
			// 无背景透明，在只需要绘制数字不需要背景的时候有用到
			drawBitmap = Bitmap.createBitmap(22, 22, Bitmap.Config.ALPHA_8);
		} else {
			// 自定义背景
			drawBitmap = BitmapFactory.decodeResource(getResources(), resid);
			if(-1 != resid2){
				drawBitmap2 = BitmapFactory.decodeResource(getResources(), resid2);
			}
		}
	}

}
