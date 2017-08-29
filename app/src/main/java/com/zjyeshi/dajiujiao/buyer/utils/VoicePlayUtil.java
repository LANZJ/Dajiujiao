package com.zjyeshi.dajiujiao.buyer.utils;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.widget.ImageView;

import com.jopool.crow.imlib.task.DownloadTask;
import com.jopool.crow.imlib.utils.asynctask.callback.AsyncTaskFailCallback;
import com.jopool.crow.imlib.utils.asynctask.callback.AsyncTaskSuccessCallback;
import com.jopool.crow.imlib.utils.asynctask.helper.Result;
import com.jopool.crow.imlib.utils.media.MediaPlayerModel;
import com.jopool.crow.imlib.utils.media.helper.MediaConfig;
import com.zjyeshi.dajiujiao.buyer.common.Constants;

import java.io.File;

/**
 * 录音播放工具类
 * Created by wuhk on 2016/10/18.
 */
public class VoicePlayUtil {
    public static void playVoice(Context context, final MediaPlayerModel mediaPlayerModel ,String voice, final ImageView voiceIv) {

        if (new File(ExtraUtil.voicefileKey(voice)).exists()) {
            LogUtil.e("===============================存在录音，直接播放=============================");

            playLocalVoice(mediaPlayerModel , voiceIv, ExtraUtil.voicefileKey(voice), com.jopool.crow.R.drawable.cw_chat_voice_playing_left4,
                    com.jopool.crow.R.anim.cw_chat_voice_playing_left);
        } else {
            LogUtil.e("===============================不存在录音，先去下载=============================");
            DownloadTask downloadTask = new DownloadTask(context);
            downloadTask
                    .setAsyncTaskFailCallback(new AsyncTaskFailCallback<Object>() {
                        @Override
                        public void failCallback(Result<Object> result) {
                            ToastUtil.toast(result.getMessage());
                        }
                    });
            downloadTask
                    .setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<Object>() {
                        @Override
                        public void successCallback(Result<Object> result) {
                            String voiceFileName = result.getMessage();
                            LogUtil.e("===============================下载完成，开始播放=============================");
                            playLocalVoice(mediaPlayerModel , voiceIv, voiceFileName,
                                    com.jopool.crow.R.drawable.cw_chat_voice_playing_left4, com.jopool.crow.R.anim.cw_chat_voice_playing_left);
                        }
                    });
            downloadTask.execute(voice, ExtraUtil.voicefileKey(voice));
        }
    }

    // 语音动画初始化
    private static void playLocalVoice(MediaPlayerModel mediaPlayerModel , final ImageView contentVoiceIv,
                                       String fileName, final int normalResid, int playingResid) {
        if (mediaPlayerModel.isPlaying()) {
            mediaPlayerModel.stopPlaying();
            contentVoiceIv.setImageResource(normalResid);
        } else {
            // 播放
            mediaPlayerModel.startPlaying(fileName, true,
                    new MediaPlayerModel.PlayingListener() {
                        @Override
                        public void startPlay(MediaPlayer mp, String fileName) {

                        }

                        @Override
                        public void endPlay() {
                            // 播放结束
                            contentVoiceIv.setImageResource(normalResid);
                        }
                    });
            contentVoiceIv.setImageResource(playingResid);
            AnimationDrawable animationDrawable = (AnimationDrawable) contentVoiceIv
                    .getDrawable();
            animationDrawable.start();
        }
    }
}
