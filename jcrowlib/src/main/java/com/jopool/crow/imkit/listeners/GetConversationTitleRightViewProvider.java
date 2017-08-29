package com.jopool.crow.imkit.listeners;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.jopool.crow.R;

/**
 * 会话界面右边的标题栏
 * <p/>
 * Created by xuan on 15/10/28.
 */
public interface GetConversationTitleRightViewProvider {
    /**
     * View数组
     *
     * @return
     */
    View[] getRightViews();

    /**
     * 点击事件
     *
     * @return
     */
    OnRightViewClickListener[] getRightViewClickListener();

    /**
     * 传递参数封装
     */
    public static class Params {
        private String toId;

        public String getToId() {
            return toId;
        }

        public void setToId(String toId) {
            this.toId = toId;
        }
    }

    /**
     * 点击事件接口
     */
    public static interface OnRightViewClickListener {
        void onClick(View view, Params params);
    }

    public static class Factory {
        public static GetConversationTitleRightViewProvider getSimpleProvider(final Context context, final int resid, final OnRightViewClickListener l) {
            return getSimpleProvider(context, new int[]{resid}, new OnRightViewClickListener[]{l});
        }

        public static GetConversationTitleRightViewProvider getSimpleProvider(final Context context, final int[] resids, final OnRightViewClickListener[] ls) {
            if (null == resids) {
                return null;
            }

            return new GetConversationTitleRightViewProvider() {
                @Override
                public View[] getRightViews() {
                    ImageView[] ivs = new ImageView[resids.length];
                    for (int i = 0; i < resids.length; i++) {
                        ivs[i] = (ImageView) LayoutInflater.from(context).inflate(R.layout.cw_widgets_title_titlelayout_rightimageview, null);
                        ivs[i].setImageResource(resids[i]);
                    }
                    return ivs;
                }

                @Override
                public OnRightViewClickListener[] getRightViewClickListener() {
                    return ls;
                }
            };
        }
    }

}
