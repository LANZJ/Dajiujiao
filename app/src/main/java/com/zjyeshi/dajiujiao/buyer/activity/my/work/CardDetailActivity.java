package com.zjyeshi.dajiujiao.buyer.activity.my.work;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.activity.my.Baiduy;
import com.zjyeshi.dajiujiao.buyer.adapter.my.work.LeaveDetailImageAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.AttendanceRecord;
import com.zjyeshi.dajiujiao.buyer.views.other.MyGridView;

/**
 * 打卡详情
 *
 * Created by zhum on 2016/6/14.
 */
public class CardDetailActivity extends BaseActivity {

    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;//标题栏

    @InjectView(R.id.dateTv)
    private TextView dateTv;//日期
    @InjectView(R.id.upTimeTv)
    private TextView upTimeTv;//上班时间
    @InjectView(R.id.upAreaTv)
    private TextView upAreaTv;//上班地点
    @InjectView(R.id.downTimeTv)
    private TextView downTimeTv;//下班时间
    @InjectView(R.id.downAreaTv)
    private TextView downAreaTv;//下班地点
    @InjectView(R.id.bzTv)
    private TextView bzTv;//下班备注
    @InjectView(R.id.remarkTv)
    private TextView remarkTv;//上班备注
    @InjectView(R.id.onGridView)
    private MyGridView onGridView;
    @InjectView(R.id.offGridView)
    private MyGridView offGridView;

    private LeaveDetailImageAdapter onAdapter;
    private LeaveDetailImageAdapter offAdapter;

    private AttendanceRecord attendanceRecord;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_card_detail);

        initWidgets();
    }

    private void initWidgets(){
        attendanceRecord = (AttendanceRecord) getIntent().getSerializableExtra("record");

        titleLayout.configTitle("打卡详情").configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setTextJudgeEmpty(dateTv , attendanceRecord.getSpeDate());
        setTextJudgeEmpty(upTimeTv , attendanceRecord.getUpTime());
        setTextJudgeEmpty(upAreaTv , attendanceRecord.getUpArea());
        setTextJudgeEmpty(downTimeTv , attendanceRecord.getDownTime());
        setTextJudgeEmpty(downAreaTv , attendanceRecord.getDownArea());
        setTextJudgeEmpty(bzTv , attendanceRecord.getDownmRemark());
        setTextJudgeEmpty(remarkTv , attendanceRecord.getUpRemark());

        onAdapter = new LeaveDetailImageAdapter(CardDetailActivity.this , attendanceRecord.getOnList());
        onGridView.setAdapter(onAdapter);

        offAdapter = new LeaveDetailImageAdapter(CardDetailActivity.this , attendanceRecord.getOffList());
        offGridView.setAdapter(offAdapter);

        upAreaTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connt(attendanceRecord.getLng(),attendanceRecord.getLat());
            }
        });
        downAreaTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connt(attendanceRecord.getLngs(),attendanceRecord.getLats());
            }
        });
    }


    private void setTextJudgeEmpty(TextView textView , String content){
        if (Validators.isEmpty(content)){
            textView.setVisibility(View.GONE);
        }else{
            textView.setVisibility(View.VISIBLE);
            textView.setText(content);
        }
    }
  private void connt(String log,String lat){

      Intent intent=new Intent(CardDetailActivity.this, Baiduy.class);
      Bundle bundle=new Bundle();
     bundle.putString("log", log);
      bundle.putString("lat",lat);
      intent.putExtras(bundle);
      startActivity(intent);

  }


}