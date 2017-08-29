package com.zjyeshi.dajiujiao.buyer.views.comment.listener;

import com.jopool.crow.imkit.listeners.OnRecordVoiceListener;
import com.jopool.crow.imlib.utils.media.MediaRecorderModel;
import com.jopool.crow.imlib.utils.media.helper.MediaConfig;
import com.xuan.bigapple.lib.utils.uuid.UUIDUtils;
import com.zjyeshi.dajiujiao.buyer.common.Constants;

/**
 * 录音回调
 * Created by wuhk on 2016/8/10.
 */
public class MyOnRecordVioceListener implements OnRecordVoiceListener {
    private MediaRecorderModel mediaRecorderModel;
    private RecordResultListener l;

    public MyOnRecordVioceListener() {
        mediaRecorderModel = new MediaRecorderModel(new MediaConfig(Constants.SDCARD_DJJBUYER_COMMENT
                ,Constants.VOICE_EXT).configVolumeInteral(100));
        mediaRecorderModel.setOnRecordListener(new MediaRecorderModel.OnRecordListener() {
            @Override
            public void onRecordStarted() {

            }

            @Override
            public void onRecordStoped(boolean isCancel, boolean success, String fileName, long voiceLength) {
                if (null != l) {
                    l.onRecordStoped(isCancel, success, fileName, voiceLength);
                }
            }

            @Override
            public void onTooShort() {
                if (null != l) {
                    l.onTooShort();
                }
            }

            @Override
            public void onVolumeDb(double db) {
                // 这里db的范围是[1,90.3]
                double dealDb = Math.max(Math.min(db, 90), 40);
                // 这里dealDb的范围是[40,90]
                double v = (dealDb - 40) / (50 / 8);
                // 这里的v的范围是[0,7]

                if (null != l) {
                    l.onVolume((int) v);
                }
            }
        });
    }

    @Override
    public void setRecordResultListener(RecordResultListener l) {
        this.l = l;
    }

    @Override
    public void finishRecord() {
        mediaRecorderModel.stopRecording(false);
    }

    @Override
    public void cancelRecord() {
        mediaRecorderModel.stopRecording(true);
    }

    @Override
    public void startRecord() {
        mediaRecorderModel.startRecording(UUIDUtils.createId());
    }
}
