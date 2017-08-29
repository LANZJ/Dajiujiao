package com.zjyeshi.dajiujiao.buyer.activity.my;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xuan.bigdog.lib.zxing.DGScanActivity;
import com.zjyeshi.dajiujiao.R;

/**
 * 扫描二维码界面
 * Created by wuhk on 2016/4/8.
 */
public class ScanMaActivity extends DGScanActivity {
    /**
     * 请求码
     */
    public static final int SCAN_REQUEST_CODE = 222;

    public static final String PARAM_SCAN_TEXT = "param.scan.text";

    @Override
    protected void onScanFinish(String text) {
        setResult(Activity.RESULT_OK, getIntent().putExtra(PARAM_SCAN_TEXT, text));
        finish();
    }

    @Override
    protected int onScanViewLayout() {
        return R.layout.layout_qrcode_scan;
    }

    @Override
    protected void extendOperate() {
        Button sureBtn = (Button) findViewById(R.id.sureBtn);
        final EditText contentEt = (EditText) findViewById(R.id.contentEt);

        sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = contentEt.getText().toString();
                setResult(Activity.RESULT_OK, getIntent().putExtra(PARAM_SCAN_TEXT, content));
                finish();
            }
        });
    }
}
