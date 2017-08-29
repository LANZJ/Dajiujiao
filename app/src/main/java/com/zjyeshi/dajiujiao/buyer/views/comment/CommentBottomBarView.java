package com.zjyeshi.dajiujiao.buyer.views.comment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jopool.crow.imkit.listeners.OnRecordVoiceListener;
import com.jopool.crow.imkit.utils.album.BUAlbum;
import com.jopool.crow.imkit.view.CWBaseLayout;
import com.jopool.crow.imlib.utils.io.FileUtils;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.utils.ContextUtils;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigdog.lib.dialog.DGSingleSelectDialog;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.common.Constants;
import com.zjyeshi.dajiujiao.buyer.task.data.other.GetUpLoadFileData;
import com.zjyeshi.dajiujiao.buyer.task.my.UpLoadPhotoTask;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.buyer.utils.FileUtil;
import com.zjyeshi.dajiujiao.buyer.utils.LogUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.buyer.views.comment.listener.CommentListener;
import com.zjyeshi.dajiujiao.buyer.views.comment.listener.MyOnRecordVioceListener;

import java.io.File;
import java.io.IOException;

/**
 * 会话聊天底部操作栏
 * 
 * @author xuan
 */
public class CommentBottomBarView extends CWBaseLayout {
	private final Activity mActivity;
	private CommentListener commentListener;//评论回调
	public static final int COMMENT_BOTTOM_BAR_CAMERA = 1;
	public static final int COMMENT_BOTTOM_BAR_BUALUM = 2;

	private Button selectVoiceBtn;// 语音图标
	private Button selectTextBtn;// 文本图标
	private Button voiceBtn;// 按住说话按钮
	private EditText inputEt;// 输入框
	private Button sendBtn;// 发送按钮
	private Button selectToolsBtn;// 选择拍照图标
	private MyOnRecordVioceListener myOnRecordVioceListener;
	private CommentVolumeView volumeView;

	public CommentBottomBarView(Context context) {
		super(context);
		mActivity = (Activity) context;
	}

	public CommentBottomBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mActivity = (Activity) context;
	}

	@Override
	protected void onViewInit() {
		myOnRecordVioceListener = new MyOnRecordVioceListener();
		loadView();
		initWidgits();
	}

	/**
	 * 加载布局
	 */
	private void loadView() {
		inflate(getContext(), R.layout.layout_comment_bottom_bar,
				this);
		selectVoiceBtn = (Button) findViewById(R.id.selectVoiceBtn);
		selectTextBtn = (Button) findViewById(R.id.selectTextBtn);
		voiceBtn = (Button) findViewById(R.id.voiceBtn);
		inputEt = (EditText) findViewById(R.id.inputEt);
		sendBtn = (Button) findViewById(R.id.sendBtn);
		selectToolsBtn = (Button) findViewById(R.id.selectToolsBtn);
	}

	// 初始化控件
	private void initWidgits() {
		// 喇叭图标-点击进入喇叭输入
		selectVoiceBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				selectVoiceBtn.setVisibility(View.INVISIBLE);
				inputEt.setVisibility(View.INVISIBLE);
				sendBtn.setVisibility(View.INVISIBLE);
				selectTextBtn.setVisibility(View.VISIBLE);
				voiceBtn.setVisibility(View.VISIBLE);
				inputEt.requestFocus();
				ContextUtils.showSoftInput(inputEt, false);
			}
		});

		// 文本图标-点击进入文本输入
		selectTextBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectVoiceBtn.setVisibility(View.VISIBLE);
				inputEt.setVisibility(View.VISIBLE);
				sendBtn.setVisibility(View.VISIBLE);
				selectTextBtn.setVisibility(View.INVISIBLE);
				voiceBtn.setVisibility(View.INVISIBLE);

				inputEt.requestFocus();
				ContextUtils.showSoftInput(inputEt, true);
			}
		});

		//文字发送
		sendBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String text = inputEt.getText().toString();
				if (Validators.isEmpty(text)) {
					ToastUtil.toast("请先输入评论内容");
				} else {
					commentListener.send(text, "", new CommentListener.Voice());
				}
			}
		});

		//选择照片
		selectToolsBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!ContextUtils.hasSdCard()) {
					ToastUtil.toast(
							"没有安装SD卡时不能使用图片、语音等功能，请安装SD卡");
					return;
				}

				DGSingleSelectDialog d = new DGSingleSelectDialog.Builder(mActivity).setItemTextAndOnClickListener(new String[]{"从手机相册选择", "拍照"}, new View.OnClickListener[]{new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						//从相册中选择
						BUAlbum.gotoAlbumForSingle(mActivity, COMMENT_BOTTOM_BAR_BUALUM);
					}
				}, new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						//拍照
						startCamera();
					}
				}}).create();
				d.show();
			}
		});

		//录音按钮
		voiceBtn.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						volumeView.showPrePare();
//						voiceBtn.setBackgroundResource(com.jopool.crow.R.drawable.cw_chat_conversation_bottom_bar_voice_btn_pressed);
						myOnRecordVioceListener.startRecord();
						break;
					case MotionEvent.ACTION_MOVE:
						if (moveIsOutVoiceBtn(event)) {
							volumeView.showReleaseUpCancel();
						} else {
							volumeView.showSlideUpCancel();
						}
						break;
					case MotionEvent.ACTION_CANCEL:
					case MotionEvent.ACTION_UP:
						volumeView.hide();
//						voiceBtn.setBackgroundResource(com.jopool.crow.R.drawable.cw_chat_conversation_bottom_bar_voice_btn_normal);
						if (moveIsOutVoiceBtn(event)) {
							myOnRecordVioceListener.cancelRecord();
						} else {
							myOnRecordVioceListener.finishRecord();
						}
						break;
					default:
						break;
				}
				return false;
			}
		});


		//录音结果监听
		myOnRecordVioceListener.setRecordResultListener(new OnRecordVoiceListener.RecordResultListener() {
			@Override
			public void onRecordStoped(boolean isCancel, boolean success, final String fileName, final long voiceLength) {
				if (!isCancel && success) {
					// 非取消，并且是录音成功的
					final String srcFileName = FileUtil.getLocalVoiceFileName(fileName);
					LogUtil.e("===============================录音成功=============================");
					UpLoadPhotoTask upLoadPhotoTask = new UpLoadPhotoTask(getContext());
					upLoadPhotoTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<GetUpLoadFileData>() {
						@Override
						public void failCallback(Result<GetUpLoadFileData> result) {
							ToastUtil.toast(result.getMessage());
						}
					});
					upLoadPhotoTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<GetUpLoadFileData>() {
						@Override
						public void successCallback(Result<GetUpLoadFileData> result) {
							try {
								//已返回Url作为文件名，复制一份原来的文件
								String copyFilename = ExtraUtil.voicefileKey(result.getValue().getPath());
								com.xuan.bigapple.lib.io.FileUtils.copyFile(new File(srcFileName)
										, new File(copyFilename));
								LogUtil.e("===============================录音上传成功=============================\n" +
										"保存路径" + copyFilename);
								//完成之后将原来的文件删掉
								FileUtils.deleteFileOrDirectory(srcFileName, true);
								int voiceSecond = (int) (voiceLength / 1000);
								if (voiceSecond < 1) {
									voiceSecond = 1;
								}
								commentListener.send("", "", new CommentListener.Voice(result.getValue().getPath(), String.valueOf(voiceSecond)));
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					});

					upLoadPhotoTask.execute(srcFileName);
				}
			}

			@Override
			public void onTooShort() {
				volumeView.interruptPrepare();
				volumeView.toastTooShot();
			}

			@Override
			public void onVolume(int v) {
				volumeView.setVolume(v);
			}
		});
	}


	/**
	 * 去相机拍照返回图片
	 */
	private Uri startCamera() {

		File file = new File(Constants.SDCARD_DJJBUYER_COMMENT_TEMP_CAMREA);
		File parentFile = file.getParentFile();
		if (!parentFile.exists()) {
			parentFile.mkdirs();
		}
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
		Uri uri = Uri.fromFile(new File(Constants.SDCARD_DJJBUYER_COMMENT_TEMP_CAMREA));
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		mActivity.startActivityForResult(intent, COMMENT_BOTTOM_BAR_CAMERA);
		return uri;
	}


	public void setCommentListener(CommentVolumeView volumeView, CommentListener commentListener) {
		this.volumeView = volumeView;
		this.commentListener = commentListener;
	}

	public Button getSendBtn() {
		return sendBtn;
	}

	public EditText getInputEt() {
		return inputEt;
	}

	public Button getVoiceBtn() {
		return voiceBtn;
	}


	/**
	 * 是否移出了
	 */
	private boolean moveIsOutVoiceBtn(MotionEvent event) {
		if (event.getY() < -50) {
			return true;
		} else {
			return false;
		}
	}


}
