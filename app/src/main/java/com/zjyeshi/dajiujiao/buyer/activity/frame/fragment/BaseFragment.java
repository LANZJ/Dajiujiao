package com.zjyeshi.dajiujiao.buyer.activity.frame.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xuan.bigapple.lib.ioc.ViewUtils;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigdog.lib.tabframe.mcall.CallFragment;
import com.zjyeshi.dajiujiao.buyer.receiver.LoadNewRemindReceiver;

/**
 * Fragment基类
 * Created by xuan on 15/9/16.
 */
public abstract class BaseFragment extends CallFragment {
    private View mContentView;//界面切换缓存

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null != mContentView) {
            ViewGroup parent = (ViewGroup) mContentView.getParent();
            if (null != parent) {
                // 父亲把我删除了把
                parent.removeView(mContentView);
            }
        } else {
            mContentView = LayoutInflater.from(getActivity()).inflate(initFragmentView(), null);
            ViewUtils.inject(this, mContentView);// 可以这样使用bigapple里面的注解注入
            initFragmentWidgets(inflater, container, savedInstanceState);
        }
        return mContentView;
    }

    /**
     * 子类实现，设置Fragment的View
     *
     * @return
     */
    protected abstract int initFragmentView();

    /**
     * 子类实现，在这里可以初始化数据
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     */
    protected abstract void initFragmentWidgets(LayoutInflater inflater,
                                                ViewGroup container, Bundle savedInstanceState);

    //    /**
//     * 可以自己设置默认图片
//     *
//     * @param imageView
//     * @param url
//     * @param defaultResId
//     */
//    public void initImageView(ImageView imageView, String url, int defaultResId) {
//        Bitmap defaultBitmap = BitmapFactory.decodeResource(Bigapple.getApplicationContext().getResources(), defaultResId);
//
//        if (!Validators.isEmpty(url)) {
//            imageView.setVisibility(View.VISIBLE);
//            BitmapDisplayConfig config = new BitmapDisplayConfig();
//            config.setLoadFailedBitmap(defaultBitmap);
//            config.setLoadingBitmap(defaultBitmap);
//
//            if (Validators.isNumber(url)) {
//                // 资源id
//                imageView.setImageResource(Integer.valueOf(url));
//            } else if (url.startsWith("http")) {
//                // 加载网络
//                BPBitmapLoader.getInstance().display(imageView, url, config);
//            } else {
//                // 加载本地
//                BPBitmapLoader.getInstance().display(imageView, url, config);
//            }
//        } else {
//            imageView.setImageBitmap(defaultBitmap);
//        }
//    }
    protected void initTextView(TextView textView, String text) {
        if (Validators.isEmpty(text)) {
            textView.setText("");
        } else {
            textView.setText(text);
        }
    }

}
