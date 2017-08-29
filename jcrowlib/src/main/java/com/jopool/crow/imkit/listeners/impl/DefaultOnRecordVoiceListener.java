package com.jopool.crow.imkit.listeners.impl;

import com.jopool.crow.imkit.listeners.OnRecordVoiceListener;
import com.jopool.crow.imlib.utils.ChatFileUtils;
import com.jopool.crow.imlib.utils.media.MediaRecorderModel;
import com.jopool.crow.imlib.utils.media.helper.MediaConfig;
import com.jopool.crow.imlib.utils.uuid.CWUUIDUtils;

/**
 * 设置默认录音监听
 * 
 * @author xuan
 */
public class DefaultOnRecordVoiceListener implements OnRecordVoiceListener {
	private final MediaRecorderModel mediaRecorderModel;
	private RecordResultListener l;

	public DefaultOnRecordVoiceListener() {
		mediaRecorderModel = new MediaRecorderModel(
				new MediaConfig(ChatFileUtils.getVoiceFilePath(),
						ChatFileUtils.VOICE_EXT).configVolumeInteral(100));
		mediaRecorderModel.setOnRecordListener(new MediaRecorderModel.OnRecordListener() {
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

			@Override
			public void onTooShort() {
				if (null != l) {
					l.onTooShort();
				}
			}

			@Override
			public void onRecordStoped(boolean isCancel, boolean success,
					String fileName, long voiceLength) {
				if (null != l) {
					l.onRecordStoped(isCancel, success, fileName, voiceLength);
				}
			}

			@Override
			public void onRecordStarted() {
			}
		});
	}

	@Override
	public void startRecord() {
		mediaRecorderModel.startRecording(CWUUIDUtils.createId());
	}

	@Override
	public void cancelRecord() {
		mediaRecorderModel.stopRecording(true);

	}

	@Override
	public void finishRecord() {
		mediaRecorderModel.stopRecording(false);
	}

	@Override
	public void setRecordResultListener(RecordResultListener l) {
		this.l = l;
	}

}
