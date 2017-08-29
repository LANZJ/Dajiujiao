package com.xuan.bigdog.lib.tabframe.mcall;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost.TabSpec;

import com.xuan.bigappleui.lib.utils.ui.M;
import com.xuan.bigappleui.lib.view.BUNumRadioButton;
import com.xuan.bigdog.lib.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 框架主界面
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-10-25 下午6:55:29 $
 */
public class DGFrameActivity extends CallFragmentActivity implements
        CallByFragmentListener {
    protected FragmentTabHost tabhost;

    protected RadioGroup tab;

    /**
     * 下面tab按钮数组
     */
    protected RadioButton[] tabBtns;

    /**
     * 主界面所需要管理的Fragment片段
     */
   private Class<?>[] fragments;
  //  private ArrayList<Fragment>fragments;


    /**
     * tab的resid对应position
     */
    private final Map<Integer, Integer> resid2postionMap = new HashMap<Integer, Integer>();

    /**
     * 子界面个数
     */
    private int tabNum;

    protected RadioButton lastCheckedBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dg_tabframe_mcall_frame);
        fragments = initFragment();
        tabNum=fragments.length;
       // tabNum = fragments.size();
        initWidgets();


    }

    // Init view
    private void initWidgets() {
        tabhost = (FragmentTabHost) findViewById(M.id(this, "tabhost"));
        tab = (RadioGroup) findViewById(M.id(this, "tab"));

        tabBtns = new RadioButton[tabNum];

        for (int i = 0; i < tabNum; i++) {
            // 循环获取tabBtns
            int tabResId = M.id(this, "tabbtn" + i);
            tabBtns[i] = (RadioButton) findViewById(tabResId);
            resid2postionMap.put(tabResId, i);
        }

        tabhost.setup(this, getSupportFragmentManager(),
                M.id(this, "realtabcontent"));

        // 设置子界面
        for (int i = 0; i < tabNum; i++) {
           TabSpec tabSpec = tabhost.newTabSpec(String.valueOf(i))
                  .setIndicator(String.valueOf(i));
           tabhost.addTab(tabSpec, fragments[i], null);
        }

        tab.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(!onTabChecked(group, checkedId)){
                    tabhost.setCurrentTab(resid2postionMap.get(checkedId));
                    lastCheckedBtn = tabBtns[resid2postionMap.get(checkedId)];//记录上一次选中的
                }
            }
        });

        // 默认选中第一个
        tabBtns[0].setChecked(true);
    }

    /**
     * 设置选中某个tab页面
     *
     * @param index
     */
    public void setTab(int index) {
        index = Math.min(Math.max(index, (tabBtns.length - 1)), 0);
        tabhost.setCurrentTab(index);
    }

    /**
     * 设置角标
     *
     * @param tabIndex
     */
    public void setUnreadNum(int tabIndex, int unreadNum){
        setUnreadNum(tabIndex, unreadNum, R.drawable.dg_icon_red, R.drawable.dg_icon_red2);
    }

    public void setUnreadNum(int tabIndex, int unreadNum, int iconResId, int iconResId2){
        ((BUNumRadioButton)tabBtns[tabIndex]).setNum(unreadNum, iconResId, iconResId2);
    }

    /**
     * 子类自己实现Fragment
     */
   protected Class<?>[] initFragment() {
       return new Class<?>[]{DGFrameFragment.class, DGFrameFragment.class,
                DGFrameFragment.class, DGFrameFragment.class};
   }

// protected ArrayList<Fragment>initFragment() {
//
//       fragments =new  ArrayList<Fragment>();
//        fragments.add(new DGFrameFragment());
//        fragments.add(new DGFrameFragment());
//        fragments.add(new DGFrameFragment());
//        fragments.add(new DGFrameFragment());
////        return new ArrayList<Fragment>{DGFrameFragment.class, DGFrameFragment.class,
////                DGFrameFragment.class, DGFrameFragment.class};
//        return fragments;
//    }

    /**
     * tab选中事件
     *
     * @param group
     * @param checkedId
     */
    protected boolean onTabChecked(RadioGroup group, int checkedId) {
        return false;
        //可以复写，返回false表示没有消耗该方法，程序会往下走
        //返回true，表示消耗了该方法，不会在走框架里面的逻辑了
    }


}
