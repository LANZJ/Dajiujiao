package com.zjyeshi.dajiujiao.buyer.adapter.store;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.store.BalanceAccountsActivity;
import com.zjyeshi.dajiujiao.buyer.activity.store.onGroupExpandedListener;
import com.zjyeshi.dajiujiao.buyer.entity.enums.SalesFillTypeEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.SalesFormTypeEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.SalesGiveTypeEnum;
import com.zjyeshi.dajiujiao.buyer.task.sales.data.SalesListData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import static com.zjyeshi.dajiujiao.R.id.checkBox;

/**
 * Created by lan on 2017/8/23.
 */
public class MyExpandableListView extends BaseExpandableListAdapter {
    private  SparseArray<Boolean> checkStates;
    private SparseArray<Boolean> chengpo=new SparseArray<>();
    private List<Integer> chengpoi=new ArrayList<Integer>();
    private List<String> groupArray;
    private List<List<SalesListData.Sales>> childArray;
    private List<SalesListData.Sales> dataList = new ArrayList<SalesListData.Sales>();
    private int mm=0;
    List<String>muer=new ArrayList<String>();
    private IDialogControl iDialogControl;
    //  用于存放Indicator的集合
    private SparseArray<ImageView> mIndicators;
    private Context context;
    private onGroupExpandedListener mOnGroupExpandedListener;

    public MyExpandableListView(Context context,List<String> groupArray, List<List<SalesListData.Sales>> childArray,  List<SalesListData.Sales> dataList,SparseArray<Boolean> checkStates,IDialogControl iDialogControl) {
        this.context=context;
       this.groupArray=groupArray;
        this.childArray=childArray;
        this.dataList=dataList;
        this.checkStates=checkStates;
        this.iDialogControl=iDialogControl;
        initDate();
        mIndicators = new SparseArray<>();
    }


    public void setOnGroupExpandedListener(onGroupExpandedListener onGroupExpandedListener) {
        mOnGroupExpandedListener = onGroupExpandedListener;
    }

    //            根据分组的展开闭合状态设置指示器
    public void setIndicatorState(int groupPosition, boolean isExpanded) {
        if (isExpanded) {
            mIndicators.get(groupPosition).setImageResource(R.drawable.ic_expand_less);
        } else {
            mIndicators.get(groupPosition).setImageResource(R.drawable.ic_expand_more);
        }
    }
    private void initDate() {
//        if (dataList==null)
//            return;
        for (SalesListData.Sales sale : dataList) {
            chengpo.put(mm, sale.getSuperposition());
            mm++;
        }
    }
    @Override
    public int getGroupCount() {
        return groupArray.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
      //  return childData.length;
        return childArray.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupArray.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childArray.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group, parent, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.label_group_indicator);
            groupViewHolder.ivIndicator = (ImageView) convertView.findViewById(R.id.iv_indicator);
            groupViewHolder.optile=(TextView) convertView.findViewById(R.id.textView5);
            groupViewHolder.lompop= (LinearLayout) convertView.findViewById(R.id.lompop);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        groupViewHolder.tvTitle.setText(groupArray.get(groupPosition));
//        if (dataList==null){
//            groupViewHolder.lompop.setVisibility(View.GONE);
//        }else {
            groupViewHolder.optile.setText(dataList.size()+"个活动");


        //      把位置和图标添加到Map
        mIndicators.put(groupPosition, groupViewHolder.ivIndicator);
        //      根据分组状态设置Indicator
        setIndicatorState(groupPosition, isExpanded);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View
            convertView, ViewGroup parent) {
        final ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.hodolayout, parent, false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.tvTitle1 = (TextView) convertView.findViewById(R.id.fillDesTv);
            childViewHolder.tvTitle2 = (TextView)convertView.findViewById(R.id.fillTv);
            childViewHolder.tvTitle3 = (TextView)convertView.findViewById(R.id.cutDesTv);
            childViewHolder.tvTitle4 = (TextView)convertView.findViewById(R.id.cutTv);
            childViewHolder.imageview = (ImageView)convertView.findViewById(R.id.flagIv);
            childViewHolder.checkbox= (CheckBox) convertView.findViewById(checkBox);
            childViewHolder.rlut= (RelativeLayout) convertView.findViewById(R.id.ddd);
            childViewHolder.imgeop=(ImageView) convertView.findViewById(R.id.imgeop);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        //childViewHolder.checkbox.setId(childPosition);

        final SalesListData.Sales sales = dataList.get(childPosition);
        childViewHolder.checkbox.setChecked(checkStates.valueAt(childPosition));
        // 根据isSelected来设置checkbox的选中状况  

//        childViewHolder.checkbox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
                childViewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        View viewById = buttonView.findViewById(checkBox);
                        if (!viewById.isPressed()){
                            return;
                        }
                        //  int id = buttonView.getId();
                        checkStates.setValueAt(childPosition, isChecked);
                        checkStates= BalanceAccountsActivity.checkStates;
                        ntd(childPosition,isChecked,sales.getId());
                        MyExpandableListView.this.notifyDataSetChanged();
                    }
                });
//            }
//        });
        // backLayout.setBackgroundColor(backgroundColor);
        //满足条件
        SalesFillTypeEnum salesFillTypeEnum = SalesFillTypeEnum.valueOf(sales.getSatisfyType());
        String fillDes = SalesFormTypeEnum.valueOf(sales.getFormType()).getName();
        if (salesFillTypeEnum.equals(SalesFillTypeEnum.FILL_MONEY)){
            childViewHolder.tvTitle1.setText(fillDes);
            childViewHolder.tvTitle2.setText(sales.getSatisfyCondition());
        }else if (salesFillTypeEnum.equals(SalesFillTypeEnum.FILL_BOX)){
            childViewHolder.tvTitle1.setText(fillDes);
            childViewHolder.tvTitle2.setText(sales.getSatisfyCondition() + "箱");
        }else if (salesFillTypeEnum.equals(SalesFillTypeEnum.MIX_BOX)){
            childViewHolder.tvTitle1.setText("合购" + fillDes);
            childViewHolder.tvTitle2.setText(sales.getSatisfyCondition() + "箱");
        }

        //优惠政策
        SalesGiveTypeEnum salesGiveTypeEnum = SalesGiveTypeEnum.valueOf(sales.getFavouredType());
        if (salesGiveTypeEnum.equals(SalesGiveTypeEnum.GIVE_GIFT)){
            childViewHolder.tvTitle3.setText("送");
            if (sales.getSuperposition()){
               childViewHolder.imageview.setImageResource(R.drawable.icon_xiangsss);
            }else {
                childViewHolder.imageview.setImageResource(R.drawable.icon_send);
            }
            childViewHolder.tvTitle4.setText(sales.getGiftName());
        }else if (salesGiveTypeEnum.equals(SalesGiveTypeEnum.BACK_MONEY)){
            childViewHolder.tvTitle3.setText("返");
            if (sales.getSuperposition()){
                childViewHolder.imageview.setImageResource(R.drawable.icon_xiangsss);
            }else {
                childViewHolder.imageview.setImageResource(R.drawable.icon_back);
               }
            childViewHolder.tvTitle4.setText("¥ "+sales.getFavouredPolicy()+"元");
        }else if (salesGiveTypeEnum.equals(SalesGiveTypeEnum.CUT_MONEY)){
            childViewHolder.tvTitle3.setText("立减");
            if (sales.getSuperposition()){
                childViewHolder.imageview.setImageResource(R.drawable.icon_xiangsss);
            }else {
                childViewHolder.imageview.setImageResource(R.drawable.icon_cut);
            }
            childViewHolder.tvTitle4.setText("¥ "+sales.getFavouredPolicy()+"元");
        }else if (salesGiveTypeEnum.equals(SalesGiveTypeEnum.GIVE_WINE)){
            if (sales.getSuperposition()){
               childViewHolder.imageview.setImageResource(R.drawable.icon_xiangsss);
            }else {
                childViewHolder.imageview.setImageResource(R.drawable.icon_send);
               }
            childViewHolder.tvTitle3.setText("送");
            childViewHolder.tvTitle4.setText(sales.getGiveProductName() + sales.getFavouredPolicy() + "箱");
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
       // Log.d(TAG, "onGroupExpanded() called with: groupPosition = [" + groupPosition + "]");
        if (mOnGroupExpandedListener != null) {
            mOnGroupExpandedListener.onGroupExpandedListener(groupPosition);
        }
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }



    private static class GroupViewHolder {
        TextView tvTitle;
        TextView optile;
        ImageView ivIndicator;
        LinearLayout lompop;
    }

    public static class ChildViewHolder {
       TextView tvTitle1;
        TextView tvTitle2;
        TextView tvTitle3;
        TextView tvTitle4;
        ImageView imageview;
        ImageView imgeop;
         CheckBox checkbox;
        RelativeLayout rlut;
    }
      public void ntd(int che,Boolean tm,String activirid){
          //tm选中状态
          //chengpo.get(che) t是共享活动 F不共享活动
          if (tm){
              if(chengpo.get(che)){
                  chengpoi.add(che);
                  for (int i = 0; i < checkStates.size(); i++) {
                      checkStates.setValueAt(i, false);}
                  muer.add(activirid);
                  for (int op:chengpoi){
                      checkStates.setValueAt(op,true);
                  }
                  //选中共享活动
                  checkStates= BalanceAccountsActivity.checkStates;
                  MyExpandableListView.this.notifyDataSetChanged();
                  iDialogControl.onShowDialog(true,muer);
              }else {//选中不共享活动
                  for (int i = 0; i < checkStates.size(); i++) {
                      checkStates.setValueAt(i, false);
                  }
                 checkStates.setValueAt(che,true);
                  muer.clear();
                  chengpoi.clear();
                  checkStates= BalanceAccountsActivity.checkStates;
                  MyExpandableListView.this.notifyDataSetChanged();
                  iDialogControl.onshio(activirid);
              }
          }else {
              if (chengpo.get(che)){
                  muer.remove(activirid);
                  iDialogControl.onShowDialog(tm,muer);
                  //ToastUtil.toast(muer.size()+"");
              }else {
                  iDialogControl.onshio("");
              }
          }

      }
    public interface IDialogControl {
        public void onShowDialog(boolean kl,List<String> maru);
        public void onshio(String activityee);
    }


}



