package com.jopool.crow.imkit.listeners;

/**
 * 录音监听
 * 
 * @author xuan
 */
public interface OnRecordVoiceListener {

	/**
	 * 按下开始录音
	 */
	void startRecord();

	/**
	 * 向上滑动取消录音
	 */
	void cancelRecord();

	/**
	 * up完成录音
	 */
	void finishRecord();

	/**
	 * 设置结果回调
	 * 
	 * @param l
	 */
	void setRecordResultListener(RecordResultListener l);

	/**
	 * 由于录音过程是个异步的过程，所以设置回调可以获取到相应的结果
	 * 
	 * @author xuan
	 */
	public static interface RecordResultListener {
		/**
		 * 录音结束，有3种可能：被取消，录音失败，录音成功
		 * 
		 * @param isCancel
		 * @param success
		 * @param fileName
		 * @param voiceLength
		 */
		void onRecordStoped(boolean isCancel, boolean success, String fileName,
							long voiceLength);

		/**
		 * 录音时间太短
		 */
		void onTooShort();

		/**
		 * 音量大小，应该要在[1,8之间]
		 * 
		 * @param v
		 */
		void onVolume(int v);
	}

}
