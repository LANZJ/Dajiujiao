package com.xuan.bigdog.lib.tabframe.mcall;

import android.app.Activity;
import android.support.v4.app.Fragment;

/**
 * 所有Fragment的基类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-7-10 下午2:59:27 $
 */
public class CallFragment extends Fragment implements CallByActivityListener {
    private CallByFragmentListener callByFragmentListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof CallByFragmentListener){
            callByFragmentListener = (CallByFragmentListener) activity;
        }
    }

    /**
     * 调用Activity
     * 
     * @param command
     * @param data
     */
    protected void callActivity(int command, Object... data) {
        if(null != callByFragmentListener){
            callByFragmentListener.callByFragment(command, data);
        }
    }

    @Override
    public void callByActivity(int command, Object... data) {
        // 子类实现这个方法可以实现被activity调用
    }

}
