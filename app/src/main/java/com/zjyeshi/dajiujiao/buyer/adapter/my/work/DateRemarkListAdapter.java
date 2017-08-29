package com.zjyeshi.dajiujiao.buyer.adapter.my.work;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jopool.crow.imlib.utils.media.MediaPlayerModel;
import com.jopool.crow.imlib.utils.media.helper.MediaConfig;
import com.xuan.bigapple.lib.utils.Validators;
import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.common.Constants;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.DateRemark;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;
import com.zjyeshi.dajiujiao.buyer.utils.VoicePlayUtil;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.my.personal.ShowImageActivity;

import java.util.List;

/**
 * 日报备注列表适配器
 * Created by wuhk on 2016/10/17.
 */
public class DateRemarkListAdapter extends MBaseAdapter {
    private Context context;
    private List<DateRemark> dataList;

    private MediaPlayerModel mediaPlayerModel = new MediaPlayerModel(new MediaConfig(Constants.SDCARD_DJJBUYER_COMMENT, Constants.VOICE_EXT));

    public DateRemarkListAdapter(Context context, List<DateRemark> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (null == view) {
            view = LayoutInflater.from(context).inflate(R.layout.listitem_date_remark, null);
        }

        ImageView remarkIv = (ImageView) view.findViewById(R.id.remarkIv);
        TextView remarkTv = (TextView) view.findViewById(R.id.remarkTv);
        final ImageView voiceIv = (ImageView) view.findViewById(R.id.voiceIv);
        TextView voiceLengthTv = (TextView)view.findViewById(R.id.voiceLengthTv);
        RelativeLayout remarkVoice = (RelativeLayout) view.findViewById(R.id.remarkVoice);

        final DateRemark dateRemark = dataList.get(position);
        remarkIv.setVisibility(View.GONE);
        remarkTv.setVisibility(View.GONE);
        remarkVoice.setVisibility(View.GONE);

        if (!Validators.isEmpty(dateRemark.getPics())) {
            remarkIv.setVisibility(View.VISIBLE);
//            initImageView(remarkIv, dateRemark.getPics());
            GlideImageUtil.glidImage(remarkIv , dateRemark.getPics() , R.drawable.default_img);

            remarkIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowImageActivity.startActivity(context , dateRemark.getPics());
                }
            });
        } else if (!Validators.isEmpty(dateRemark.getContent())) {
            remarkTv.setVisibility(View.VISIBLE);
            initTextView(remarkTv, dateRemark.getContent());
        } else if (!Validators.isEmpty(dateRemark.getVoice())) {
            remarkVoice.setVisibility(View.VISIBLE);
            remarkVoice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VoicePlayUtil.playVoice(context, mediaPlayerModel, dateRemark.getVoice(), voiceIv);
                }
            });
            voiceLengthTv.setText(dateRemark.getVoiceLength() + "\"");
        }
        return view;
    }
}
