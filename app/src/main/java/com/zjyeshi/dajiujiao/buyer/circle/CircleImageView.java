package com.zjyeshi.dajiujiao.buyer.circle;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zjyeshi.dajiujiao.R;

/**
 * 圈子里面的大图
 *
 * Created by xuan on 15/10/19.
 */
public class CircleImageView extends ImageView{
    public static final int IMAGE_MODE_NORMAL = 0;// 正常不做处理
    public static final int IMAGE_MODE_SINGLE = 1;// 单个图片处理
    public static final int IMAGE_MODE_WIDTH_EQUALS_HEIGHT = 2;// 图片宽高相等

    public int maxImageWidthOrHeight;//宽或者高的最大长度

    private int imageMode = IMAGE_MODE_NORMAL;

    public CircleImageView(Context context) {
        super(context);
        init(context);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        maxImageWidthOrHeight = (int) context.getResources().getDimension(R.dimen.circle_single_image_max_size);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        if (null == bm) {
            return;
        }

        float bitmapWidth = bm.getWidth();
        float bitmapHeight = bm.getHeight();
        if (IMAGE_MODE_SINGLE == imageMode) {
            fixImageModeMiddle(bitmapWidth, bitmapHeight);
        }else if(IMAGE_MODE_WIDTH_EQUALS_HEIGHT == imageMode){
            fixImageWidthEqualsHeight(bitmapWidth, bitmapHeight);
        }
        super.setImageBitmap(bm);
    }

    /**
     * 保持图片的宽高一致
     *
     * @param bitmapWidth
     * @param bitmapHeight
     */
    private void fixImageWidthEqualsHeight(float bitmapWidth, float bitmapHeight){
        setScaleType(ScaleType.CENTER_CROP);
        ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) getLayoutParams();
        lp.width = maxImageWidthOrHeight;
        lp.height = maxImageWidthOrHeight;
        setLayoutParams(lp);
    }

    // IMAGE_MODE_MIDDLE模式布局修改
    private void fixImageModeMiddle(float bitmapWidth, float bitmapHeight) {
        setScaleType(ScaleType.CENTER_CROP);
        ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) getLayoutParams();
        if (bitmapWidth > bitmapHeight) {
            if ((bitmapWidth / bitmapHeight) > 3) {
                // 变态比例宽大于高：宽=maxImageWidthOrHeight,高=maxImageWidthOrHeight/3
                lp.width = maxImageWidthOrHeight;
                lp.height = maxImageWidthOrHeight / 3;
                setLayoutParams(lp);
            }
            else {
                // 正常比例宽大于高：宽=maxImageWidthOrHeight,高按比例计算
                float radio = bitmapWidth / bitmapHeight;
                lp.width = maxImageWidthOrHeight;
                lp.height = (int) (maxImageWidthOrHeight / radio);
                setLayoutParams(lp);
            }
        }
        else {
            if ((bitmapHeight / bitmapWidth) > 3) {
                // 变态比例高大于宽：宽=maxImageWidthOrHeight/3,高=maxImageWidthOrHeight
                lp.width = maxImageWidthOrHeight / 3;
                lp.height = maxImageWidthOrHeight;
                setLayoutParams(lp);
            }
            else {
                // 正常比例高大于宽：宽按比例算,高=maxImageWidthOrHeight
                float radio = bitmapHeight / bitmapWidth;
                lp.width = (int) (maxImageWidthOrHeight / radio);
                lp.height = maxImageWidthOrHeight;
                setLayoutParams(lp);
            }
        }
    }

    public int getImageMode() {
        return imageMode;
    }

    public void setImageMode(int imageMode) {
        this.imageMode = imageMode;
    }

}
