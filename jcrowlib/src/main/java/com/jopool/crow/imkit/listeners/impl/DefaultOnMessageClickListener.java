package com.jopool.crow.imkit.listeners.impl;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageView;

import com.jopool.crow.CWChat;
import com.jopool.crow.R;
import com.jopool.crow.imkit.activity.CWShowImageActivity;
import com.jopool.crow.imkit.activity.CWWebViewActivity;
import com.jopool.crow.imkit.listeners.OnMessageClickListener;
import com.jopool.crow.imkit.receiver.CWConversationMessageReceiver;
import com.jopool.crow.imkit.receiver.CWConversationUnreadNumReceiver;
import com.jopool.crow.imlib.db.dao.CWChatDaoFactory;
import com.jopool.crow.imlib.entity.CWConversationMessage;
import com.jopool.crow.imlib.enums.CWMessageContentType;
import com.jopool.crow.imlib.enums.CWMessageType;
import com.jopool.crow.imlib.enums.CWReadStatus;
import com.jopool.crow.imlib.enums.CWSendStatus;
import com.jopool.crow.imlib.soket.CWErrorCode;
import com.jopool.crow.imlib.soket.listeners.OnMessageSendListener;
import com.jopool.crow.imlib.task.DownloadTask;
import com.jopool.crow.imlib.utils.CWLogUtil;
import com.jopool.crow.imlib.utils.CWToastUtil;
import com.jopool.crow.imlib.utils.CWUrlUtils;
import com.jopool.crow.imlib.utils.CWValidator;
import com.jopool.crow.imlib.utils.ChatFileUtils;
import com.jopool.crow.imlib.utils.asynctask.callback.AsyncTaskFailCallback;
import com.jopool.crow.imlib.utils.asynctask.callback.AsyncTaskSuccessCallback;
import com.jopool.crow.imlib.utils.asynctask.helper.Result;
import com.jopool.crow.imlib.utils.media.MediaPlayerModel;
import com.jopool.crow.imlib.utils.media.helper.MediaConfig;

import java.io.File;

/**
 * 默认消息点击事件实现
 *
 * @author xuan
 */
public class DefaultOnMessageClickListener implements OnMessageClickListener {
    private final MediaPlayerModel mediaPlayerModel;

    public DefaultOnMessageClickListener() {
        mediaPlayerModel = new MediaPlayerModel(new MediaConfig(
                ChatFileUtils.getVoiceFilePath(), ChatFileUtils.VOICE_EXT));
    }

    @Override
    public void onMessageClick(Context context, View view,
                               CWConversationMessage message) {
        if (CWMessageContentType.TEXT.equals(message.getMessageContentType())) {
            dealTextClick(context, message);
        } else if (CWMessageContentType.IMAGE.equals(message
                .getMessageContentType())) {
            dealImageClick(context, message);
        } else if (CWMessageContentType.VOICE.equals(message
                .getMessageContentType())) {
            dealVoiceClick(context, view, message);
        } else if (CWMessageContentType.URL.equals(message.getMessageContentType())) {
            dealUrlClick(context, message);
        }
    }

    @Override
    public void onResendClick(final Context context, View view,
                              CWConversationMessage message) {
        // 调用接口重新发送
        CWChat.getInstance().getImClient()
                .sendMessage(context, message, new OnMessageSendListener() {
                    @Override
                    public void onSuccess(CWConversationMessage message) {
                        modifySendStatus(context, message, CWSendStatus.SUCCESS);
                    }

                    @Override
                    public void onError(CWConversationMessage message,
                                        CWErrorCode errorCode) {
                        modifySendStatus(context, message, CWSendStatus.FAIL);
                    }
                }, message.getConversationType());

        // 修改本地数据
        modifySendStatus(context, message, CWSendStatus.ING);
    }

    // 修改发送状态
    private void modifySendStatus(Context context,
                                  CWConversationMessage message, CWSendStatus sendStatus) {
        // 修改本地数据
        CWChatDaoFactory.getConversationMessageDao().updateSendStatusById(
                message.getId(), sendStatus.getValue());
        // 修改内存数据的状态
        message.setSendStatus(sendStatus);
        // 通知刷新列表
        CWConversationMessageReceiver.notifyRefreshList(context);
    }

    private void dealUrlClick(Context context, CWConversationMessage message) {
        CWWebViewActivity.startForUrl(context, CWUrlUtils.correctUrl(message.getContent()), message.getSenderUserId());
    }

    /**
     * 处理文字点击
     */
    private void dealTextClick(Context context, CWConversationMessage message) {
        if (CWUrlUtils.isUrl(message.getContent())) {
            CWWebViewActivity.startForUrl(context, CWUrlUtils.correctUrl(message.getContent()), message.getSenderUserId());
        }
    }

    /**
     * 处理图片点击
     */
    private void dealImageClick(Context context, CWConversationMessage message) {
        Intent intent = new Intent();
        intent.setClass(context, CWShowImageActivity.class);
        intent.putExtra(CWShowImageActivity.PARAM_SMALL_URL,
                message.getContent());
        intent.putExtra(CWShowImageActivity.PARAM_BIG_URL,
                message.getDownloadUrl());
        context.startActivity(intent);
    }

    /**
     * 处理语音点击
     */
    private void dealVoiceClick(Context context, View view,
                                CWConversationMessage message) {
        ImageView contentVoiceIv = (ImageView) view
                .findViewById(R.id.contentVoiceIv);

        if (CWMessageType.LEFT.equals(message.getMessageType())) {
            playVoiceIfNotExistFirstDownload(context, contentVoiceIv, message,
                    R.drawable.cw_chat_voice_playing_left4,
                    R.anim.cw_chat_voice_playing_left);
            // 播放过后，设置已听
            CWChatDaoFactory.getConversationMessageDao().updateReadStatusById(
                    message.getId(), CWReadStatus.LISTENED);
            // 未读消息数更改通知
            CWConversationUnreadNumReceiver.notifyReceiver(context,
                    message.getConversationToId());
            // 消息修改状态通知
            CWConversationMessageReceiver.notifyMessageReadStatusModify(
                    context, message.getId(), CWReadStatus.LISTENED.getValue());
        } else if (CWMessageType.RIGHT.equals(message.getMessageType())) {
            playVoiceIfNotExistFirstDownload(context, contentVoiceIv, message,
                    R.drawable.cw_chat_voice_playing_right4,
                    R.anim.cw_chat_voice_playing_right);
        }
    }

    /**
     * 语音不存在就先下载
     */
    private void playVoiceIfNotExistFirstDownload(final Context context,
                                                  final ImageView contentVoiceIv,
                                                  final CWConversationMessage message, final int normalResid,
                                                  final int playingResid) {

        if (!CWValidator.isEmpty(message.getContent())
                && new File(message.getContent()).exists()) {
            // 文件本地存在就直接播放
            playLocalVoice(contentVoiceIv, message.getContent(), normalResid,
                    playingResid);
        } else {
            // 先下载
            DownloadTask downloadTask = new DownloadTask(context);
            downloadTask
                    .setAsyncTaskFailCallback(new AsyncTaskFailCallback<Object>() {
                        @Override
                        public void failCallback(Result<Object> result) {
                            CWLogUtil.e(result.getMessage());
                            CWToastUtil.displayTextShort(
                                    result.getMessage());
                        }
                    });
            downloadTask
                    .setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<Object>() {
                        @Override
                        public void successCallback(Result<Object> result) {
                            String voiceFileName = result.getMessage();
                            // 更新数据库content值
                            CWChatDaoFactory.getConversationMessageDao()
                                    .updateContentById(message.getId(),
                                            voiceFileName);
                            // 更新内存content值
                            message.setContent(voiceFileName);
                            playLocalVoice(contentVoiceIv, voiceFileName,
                                    normalResid, playingResid);
                        }
                    });
            downloadTask.execute(message.getDownloadUrl(),
                    ChatFileUtils.getVoiceFileName(message.getId()));
        }
    }

    // 语音动画初始化
    private void playLocalVoice(final ImageView contentVoiceIv,
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
