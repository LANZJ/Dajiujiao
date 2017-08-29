package com.jopool.crow.imkit.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Selection;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.jopool.crow.R;
import com.jopool.crow.imkit.activity.CWConversationActivity;
import com.jopool.crow.imkit.adapter.ExpressionAdapter;
import com.jopool.crow.imkit.adapter.ToolsImageAdapter;
import com.jopool.crow.imkit.expression.ChatImagesResource;
import com.jopool.crow.imkit.expression.Expression;
import com.jopool.crow.imkit.utils.album.BUAlbum;
import com.jopool.crow.imlib.utils.CWContextUtil;
import com.jopool.crow.imlib.utils.CWToastUtil;
import com.jopool.crow.imlib.utils.ChatFileUtils;

import java.io.File;

/**
 * 会话聊天底部操作栏
 * 
 * @author xuan
 */
public class CWConversationBottomBarView extends CWBaseLayout {
	private final Activity mActivity;

	private Button selectVoiceBtn;// 语音图标
	private Button selectTextBtn;// 文本图标
	private Button voiceBtn;// 按住说话按钮
	private EditText inputEt;// 输入框
	private Button sendBtn;// 发送按钮
	private Button selectToolsBtn;// 选择拍照或者表情图标

	private View toolsRl;// 选图拍照+表情
	private GridView toolsGv;// 选图拍照
	private GridView expressionGv;// 表情

	public CWConversationBottomBarView(Context context) {
		super(context);
		mActivity = (Activity) context;
	}

	public CWConversationBottomBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mActivity = (Activity) context;
	}

	@Override
	protected void onViewInit() {
		loadView();
		initWidgits();
	}

	/** 加载布局 */
	private void loadView() {
		inflate(getContext(), R.layout.cw_chat_view_conversation_bottom_bar,
				this);
		selectVoiceBtn = (Button) findViewById(R.id.selectVoiceBtn);
		selectTextBtn = (Button) findViewById(R.id.selectTextBtn);
		voiceBtn = (Button) findViewById(R.id.voiceBtn);
		inputEt = (EditText) findViewById(R.id.inputEt);
		sendBtn = (Button) findViewById(R.id.sendBtn);
		selectToolsBtn = (Button) findViewById(R.id.selectToolsBtn);
		toolsRl = findViewById(R.id.toolsRl);
		toolsGv = (GridView) findViewById(R.id.toolsGv);
		expressionGv = (GridView) findViewById(R.id.expressionGv);
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

				resetTools();
				CWContextUtil.showSoftInput(inputEt, false);
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

				resetTools();
				toolsRl.setVisibility(View.GONE);
				CWContextUtil.showSoftInput(inputEt, true);
			}
		});

		// 加号图标—点击弹出选择工具或者隐藏
		selectToolsBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!CWContextUtil.hasSdCard()) {
					CWToastUtil.displayTextShort(
							"没有安装SD卡时不能使用图片、语音等功能，请安装SD卡");
					return;
				}

				if (toolsRl.getVisibility() == View.GONE) {
					showTools();
					CWContextUtil.hideSoftInput(inputEt);
				} else {
					resetTools();
				}
			}
		});

		// 初始化工具数据
		toolsGv.setAdapter(new ToolsImageAdapter(getContext(),
				ChatImagesResource.getToolimagelist()));
		toolsGv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int postion, long arg3) {
				switch (postion) {
				case 0:
					CWContextUtil.showSoftInput(inputEt, false);
					toolsRl.setVisibility(View.GONE);
					BUAlbum.gotoAlbumForSingle(
							mActivity,
							CWConversationActivity.CONVERSATION_REQUEST_CODE_FOR_ALBUM);
					break;
				case 1:
					CWContextUtil.showSoftInput(inputEt, false);
					toolsRl.setVisibility(View.GONE);
					startCamera();
					break;
				case 2:
					toolsGv.setVisibility(View.GONE);
					expressionGv.setVisibility(View.VISIBLE);
					// weixinBgGridView.setAdapter(new
					// ImageAdapter(ChatActivity.this,
					// ImagesResource.getBqImages()));
					toolsRl.setBackgroundColor(Color.WHITE);

					selectVoiceBtn.setVisibility(View.VISIBLE);
					selectTextBtn.setVisibility(View.INVISIBLE);
					inputEt.setVisibility(View.VISIBLE);
					voiceBtn.setVisibility(View.INVISIBLE);
					sendBtn.setVisibility(View.VISIBLE);
					inputEt.requestFocus();
					break;
				}
			}
		});

		// 初始化表情
		expressionGv.setAdapter(new ExpressionAdapter(getContext(),
				ChatImagesResource.getExpressionlist()));
		expressionGv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
									int position, long arg3) {
				Expression expression = ChatImagesResource.getExpressionlist()
						.get(position);
				String temp = inputEt.getText().toString()
						+ expression.getReplaceText();
				inputEt.setText(temp);
				// 设置光标到最后
				Editable etext = inputEt.getText();
				Selection.setSelection(etext, etext.length());
				CWContextUtil.hideSoftInput(inputEt);
			}
		});
	}

	/** 重置工具布局 */
	private void resetTools() {
		toolsRl.setVisibility(View.GONE);
		toolsGv.setVisibility(View.VISIBLE);
		expressionGv.setVisibility(View.GONE);
		toolsRl.setBackgroundResource(R.color.cw_color_chat_conversation_bottom_bar_select_bg);
	}

	/** 显示工具布局 */
	private void showTools() {
		toolsRl.setVisibility(View.VISIBLE);
	}

	/** 去相机拍照返回图片 */
	private Uri startCamera() {
		String filePathName = ChatFileUtils.getCameraRet();

		File file = new File(filePathName);
		File parentFile = file.getParentFile();
		if (!parentFile.exists()) {
			parentFile.mkdirs();
		}

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
		Uri uri = Uri.fromFile(new File(filePathName));
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		mActivity.startActivityForResult(intent,
				CWConversationActivity.CONVERSATION_REQUEST_CODE_FOR_CAMERA);
		return uri;
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
}
