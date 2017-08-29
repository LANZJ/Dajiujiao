package com.xuan.bigdog.lib.model;

import android.widget.EditText;

import com.xuan.bigapple.lib.utils.ContextUtils;

/**
 * 对EditText方便操作进行封装
 *
 * Created by xuan on 15/9/23.
 */
public class DGEditTextModel {

    /**
     * 移动光标到末尾
     *
     * @param editText
     */
    public void moveCursorEnd(EditText editText){
        editText.requestFocus();
        String text = editText.getText().toString();
        editText.setSelection(text.length());
    }

    /**
     * 移动光标到末尾，并弹出软键盘
     *
     * @param editText
     */
    public void moveCursorEndAndPopSofteyboard(EditText editText){
        moveCursorEnd(editText);
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        ContextUtils.showSoftInput(editText, true);
    }

}
