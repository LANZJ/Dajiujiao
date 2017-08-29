package com.jopool.crow.imlib.utils;

import com.jopool.crow.CWChat;

/**
 * 文件工具类
 * 
 * @author xuan
 */
public abstract class ChatFileUtils {
	public static final String VOICE_EXT = "amr";

	/**
	 * 拍照返回的就这个图片地址
	 * 
	 * @return
	 */
	public static String getCameraRet() {
		return CWChat.getInstance().getConfig().getFileSavePath()
				+ "/temp/camera_ret.jpg";
	}

	/**
	 * 大图文件
	 * 
	 * @param uuid
	 * @return
	 */
	public static String getImageFileNameForBig(String uuid) {
		return CWChat.getInstance().getConfig().getFileSavePath()
				+ "/message/image/" + uuid + ".jpg";
	}

	/**
	 * 小图文件
	 * 
	 * @param uuid
	 * @return
	 */
	public static String getImageFileNameForSmall(String uuid) {
		return CWChat.getInstance().getConfig().getFileSavePath()
				+ "/message/image/" + uuid + "_small.jpg";
	}

	/**
	 * 语音文件
	 * 
	 * @param uuid
	 * @return
	 */
	public static String getVoiceFileName(String uuid) {
		return CWChat.getInstance().getConfig().getFileSavePath()
				+ "/message/voice/" + uuid + "." + VOICE_EXT;
	}

	/**
	 * 获取文件保存路径
	 * 
	 * @return
	 */
	public static String getVoiceFilePath() {
		return CWChat.getInstance().getConfig().getFileSavePath()
				+ "/message/voice";
	}

	/**
	 * copy图片到指定目录
	 * 
	 * @param fromFileName
	 * @param toFileName
	 */
	public static void copyImageForBig(String fromFileName, String toFileName) {
		int degree = CWBitmapUtil.getBitmapDegree(fromFileName);
		CWBitmapUtil.changeOppositeSizeMayDegree(fromFileName, toFileName, 900,
				900, degree);
	}

	/**
	 * copy图片到指定目录
	 * 
	 * @param fromFileName
	 * @param toFileName
	 */
	public static void copyImageForSmall(String fromFileName, String toFileName) {
		int degree = CWBitmapUtil.getBitmapDegree(fromFileName);
		CWBitmapUtil.changeOppositeSizeMayDegree(fromFileName, toFileName, 100,
				100, degree);
	}

}
