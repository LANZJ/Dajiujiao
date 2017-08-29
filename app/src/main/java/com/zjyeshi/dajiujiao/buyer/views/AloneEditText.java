package com.zjyeshi.dajiujiao.buyer.views;

import android.content.Context;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import java.util.ArrayList;

/**
 * 独立EditText，避免多个EditTextView监听冲突
 * Created by wuhk on 2016/7/20.
 */
public class AloneEditText extends EditText {
    private ArrayList<TextWatcher> mListeners = null;

    public AloneEditText(Context context) {
        super(context);
    }

    public AloneEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void addTextChangedListener(TextWatcher watcher) {
        if (mListeners == null)

        {

            mListeners = new ArrayList<TextWatcher>();

        }

        mListeners.add(watcher);


        super.addTextChangedListener(watcher);
    }

    @Override
    public void removeTextChangedListener(TextWatcher watcher) {
        if (mListeners != null)

        {

            int i = mListeners.indexOf(watcher);

            if (i >= 0)

            {

                mListeners.remove(i);

            }

        }

        super.removeTextChangedListener(watcher);
    }


    public void clearTextChangedListeners()

    {

        if (mListeners != null)

        {

            for (TextWatcher watcher : mListeners)

            {

                super.removeTextChangedListener(watcher);

            }


            mListeners.clear();

            mListeners = null;

        }
    }
}
