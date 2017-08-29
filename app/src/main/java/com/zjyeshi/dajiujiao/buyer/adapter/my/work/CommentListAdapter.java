package com.zjyeshi.dajiujiao.buyer.adapter.my.work;

import android.content.Context;
import android.content.Intent;
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
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.buyer.utils.FriendlyTimeUtil;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.my.personal.ShowImageActivity;
import com.zjyeshi.dajiujiao.buyer.common.PassConstans;
import com.zjyeshi.dajiujiao.buyer.task.work.data.Comment;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;
import com.zjyeshi.dajiujiao.buyer.utils.VoicePlayUtil;

import java.util.Date;
import java.util.List;

/**
 * 审批评论列表适配器
 *
 * Created by zhum on 2016/6/23.
 */
public class CommentListAdapter extends MBaseAdapter {

    private final Context context;
    private final List<Comment> dataList;
    private MediaPlayerModel mediaPlayerModel = new MediaPlayerModel(new MediaConfig(Constants.SDCARD_DJJBUYER_COMMENT, Constants.VOICE_EXT));

    public CommentListAdapter(Context context,
                           List<Comment> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (null != dataList) {
            count = dataList.size();
        }
        return count;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        if (null == convertView) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(
                    R.layout.listitem_comment_list, null);
        }
        TextView contentTv = (TextView)convertView.findViewById(R.id.contentTv);
        TextView fromTv = (TextView)convertView.findViewById(R.id.fromTv);
        TextView timeTv = (TextView)convertView.findViewById(R.id.timeTv);
        TextView voiceLengthTv = (TextView)convertView.findViewById(R.id.voiceLengthTv);
        ImageView imageIv = (ImageView)convertView.findViewById(R.id.imageIv);
        final ImageView voiceIv = (ImageView)convertView.findViewById(R.id.voiceIv);
        RelativeLayout voiceLayout  = (RelativeLayout)convertView.findViewById(R.id.voiceLayout);

        final Comment comment = dataList.get(position);

        initTextView(timeTv, FriendlyTimeUtil.friendlyTime(new Date(comment.getCreationTime())));
        initTextView(fromTv,"来自:"+comment.getMemberName());
        if (!Validators.isEmpty(comment.getContent())){
            contentTv.setVisibility(View.VISIBLE);
            initTextView(contentTv,comment.getContent());
        }else{
            contentTv.setVisibility(View.GONE);
        }

        if (!Validators.isEmpty(comment.getPics())){
            imageIv.setVisibility(View.VISIBLE);
            GlideImageUtil.glidImage(imageIv , ExtraUtil.getResizePic(comment.getPics() , 200 , 200) , R.drawable.default_img);
            imageIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(context , ShowImageActivity.class);
                    intent.putExtra(PassConstans.IMAGEURL , comment.getPics());
                    context.startActivity(intent);
                }
            });
        }else{
            imageIv.setVisibility(View.GONE);
        }

        if (!Validators.isEmpty(comment.getVoice())){
            voiceLayout.setVisibility(View.VISIBLE);
            voiceLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VoicePlayUtil.playVoice(context , mediaPlayerModel ,  comment.getVoice() , voiceIv);
                }
            });
            voiceLengthTv.setText(comment.getVoiceLength() + "\"");
        }else{
            voiceLayout.setVisibility(View.GONE);
        }

        return convertView;
    }
}
