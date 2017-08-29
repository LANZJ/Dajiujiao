package com.jopool.crow.imkit.view.popview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jopool.crow.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xuan on 16/8/15.
 */
public class CWWebViewOpLayoutView extends CWPopViewLayout {
    private LinearLayout line1;
    private LinearLayout line2;

    public CWWebViewOpLayoutView(Context context) {
        super(context);
    }

    public CWWebViewOpLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onCreateView() {
        super.onCreateView();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.cw_chat_layout_webview_oplayout, null);
        setContentView(view).setPopMode(CWPopViewLayout.POPMODE_FROM_BOTTOM);
        setBackgroundResource(R.color.cw_color_translucent);
        //line1
        line1 = (LinearLayout)view.findViewById(R.id.line1);
        line2 = (LinearLayout)view.findViewById(R.id.line2);
        //
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
        //
        Button button = (Button)view.findViewById(R.id.cancelBtn);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
    }

    public void refreshData(OpItemAdapter line1Adapter, OpItemAdapter line2Adapter) {
        //set line1
        for (int i = 0, n = line1Adapter.getItemCount(); i < n; i++) {
            final View view = LayoutInflater.from(getContext()).inflate(R.layout.cw_chat_layout_webview_oplayout_item, null);
            ImageView iconIv = (ImageView) view.findViewById(R.id.iconIv);
            TextView textTv = (TextView)view.findViewById(R.id.textTv);
            //
            final OpItem item = line1Adapter.getItem(i, view);
            iconIv.setImageResource(item.getIcon());
            textTv.setText(item.getText());
            //
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggle();
                    if(null != item.getL()){
                        item.getL().onClick(view);
                    }
                }
            });
            line1.addView(view);
        }
        //set line2
        for (int i = 0, n = line2Adapter.getItemCount(); i < n; i++) {
            final View view = LayoutInflater.from(getContext()).inflate(R.layout.cw_chat_layout_webview_oplayout_item, null);
            ImageView iconIv = (ImageView) view.findViewById(R.id.iconIv);
            TextView textTv = (TextView)view.findViewById(R.id.textTv);
            //
            final OpItem item = line2Adapter.getItem(i, view);
            iconIv.setImageResource(item.getIcon());
            textTv.setText(item.getText());
            //
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggle();
                    if (null != item.getL()) {
                        item.getL().onClick(view);
                    }
                }
            });
            line2.addView(view);
        }
    }

    /**
     * data adapter
     */
    public interface OpItemAdapter{
        int getItemCount();
        OpItem getItem(int position, View view);
    }

    /**
     * data item
     */
    public static class OpItem{
        public static final String DATA_KEY_TITLE = "title";
        public static final String DATA_KEY_CURRENTURL = "currentUrl";
        public static final String DATA_KEY_OWNERUSERID = "ownerUserId";

        private int icon;
        private String text;
        private OnClickListener l;
        private Object tag;
        /**
         * key:
         * title  currentUrl  ownerUserId
         */
        private Map<String, Object> data = new HashMap<String, Object>();

        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }

        public OnClickListener getL() {
            return l;
        }

        public void setL(OnClickListener l) {
            this.l = l;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Object getTag() {
            return tag;
        }

        public void setTag(Object tag) {
            this.tag = tag;
        }

        public Map<String, Object> getData() {
            return data;
        }

        public void setData(Map<String, Object> data) {
            this.data = data;
        }
    }

}
