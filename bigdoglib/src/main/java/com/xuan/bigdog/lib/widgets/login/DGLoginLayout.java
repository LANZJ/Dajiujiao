package com.xuan.bigdog.lib.widgets.login;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.xuan.bigapple.lib.utils.ContextUtils;
import com.xuan.bigapple.lib.utils.ToastUtils;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigdog.lib.R;
import com.xuan.bigdog.lib.widgets.DGBaseLayout;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;

/**
 * 登录界面布局
 * 
 * @author xuan
 */
public class DGLoginLayout extends DGBaseLayout {
	/** 输入用户名 */
	private EditText usernameEdit;
	/** 输入密码 */
	private EditText passwordEdit;
	/** 点击登录按钮 */
	private Button loginBtn;
	/** 通用title */
	private DGTitleLayout dgTitleLayout;

	/** 中间logo图标 */
	private ImageView logoIv;

	private boolean canCheckUsernameAndPassordEmpty = false;

	public DGLoginLayout(Context context) {
		super(context);
	}

	public DGLoginLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void dgInit() {
		inflate(getContext(), R.layout.dg_widgets_login_login, this);
		usernameEdit = (EditText) findViewById(R.id.usernameEdit);
		passwordEdit = (EditText) findViewById(R.id.passwordEdit);
		loginBtn = (Button) findViewById(R.id.loginBtn);
		dgTitleLayout = (DGTitleLayout) findViewById(R.id.dgTitleLayout);
		logoIv = (ImageView) findViewById(R.id.logoIv);
	}

	/**
	 * 配置登录事件
	 * 
	 * @param l
	 * @return
	 */
	public DGLoginLayout configOnLoginBtnClickListener(
			final OnLoginBtnClickListener l) {
		loginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (null != l) {
					String username = usernameEdit.getText().toString();
					String password = passwordEdit.getText().toString();
					if (canCheckUsernameAndPassordEmpty) {
						if (Validators.isEmpty(username)) {// 用户名校验
							ToastUtils.displayTextShort(
									usernameEdit.getHint() + "不能为空");
							usernameEdit.requestFocus();
							ContextUtils.showSoftInput(
									usernameEdit, true);
							return;
						}
						if (Validators.isEmpty(password)) {// 密码校验
							ToastUtils.displayTextShort(
									passwordEdit.getHint() + "不能为空");
							passwordEdit.requestFocus();
							ContextUtils.showSoftInput(
									passwordEdit, true);
							return;
						}
					}
					l.onClick(username, password);
				}
			}
		});
		return this;
	}

	/**
	 * 是否校验用户名密码输入空
	 * 
	 * @param isCheck
	 * @return
	 */
	public DGLoginLayout configCanCheckUsernameAndPassordEmpty(boolean isCheck) {
		canCheckUsernameAndPassordEmpty = isCheck;
		return this;
	}

	/**
	 * 设置logo
	 * 
	 * @param resid
	 * @return
	 */
	public DGLoginLayout configLogo(int resid) {
		logoIv.setImageResource(resid);
		return this;
	}

	public EditText getUsernameEdit() {
		return usernameEdit;
	}

	public EditText getPasswordEdit() {
		return passwordEdit;
	}

	public Button getLoginBtn() {
		return loginBtn;
	}

	public DGTitleLayout getDgTitleLayout() {
		return dgTitleLayout;
	}

	/**
	 * 点击登录事件
	 * 
	 * @author xuan
	 */
	public static interface OnLoginBtnClickListener {
		void onClick(String username, String password);
	}

}
