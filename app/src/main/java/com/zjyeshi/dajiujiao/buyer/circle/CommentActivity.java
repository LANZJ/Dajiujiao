package com.zjyeshi.dajiujiao.buyer.circle;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.zjyeshi.dajiujiao.buyer.circle.receiver.OnlyNotifyReceiver;
import com.zjyeshi.dajiujiao.buyer.circle.task.data.CircleData;
import com.zjyeshi.dajiujiao.buyer.circle.task.data.Member;
import com.zjyeshi.dajiujiao.buyer.dao.DaoFactory;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.common.PassConstans;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.circle.task.AddEvaluateTask;
import com.zjyeshi.dajiujiao.buyer.circle.task.data.AddEvaluateData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.buyer.circle.itementity.CircleContentEntity;
import com.zjyeshi.dajiujiao.buyer.circle.task.data.Evaluate;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.Validators;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 评论Activity
 * Created by wuhk on 2015/11/19.
 */
public class CommentActivity extends BaseActivity {

    @InjectView(R.id.allLayout)
    private RelativeLayout allLayout;

    @InjectView(R.id.sendEt)
    private EditText sendEt;

    @InjectView(R.id.sendBtn)
    private Button sendBtn;

    private CircleContentEntity contentEntity;
    private Evaluate evaluate;
    private String content;
    private String position;
    private boolean isIncomment;
    private String circleEvaluateId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_comment);
        initWidgets();
    }

    private void initWidgets(){
        //判断是评论还是评论的评论
       if (getIntent().getStringExtra(PassConstans.ISINCOMMENT).equals("incomment")){
           isIncomment = true;
       }else{
           isIncomment = false;
       }
        position  = getIntent().getStringExtra(PassConstans.POSITION);
        contentEntity = (CircleContentEntity)getIntent().getSerializableExtra(PassConstans.CONTENTENTITY);
        evaluate = (Evaluate)getIntent().getSerializableExtra(PassConstans.EVALUATE);

        if (isIncomment){
            circleEvaluateId = evaluate.getId();
        }else{
            circleEvaluateId = "";
        }
        //设置回复提示
        if (isIncomment){
            sendEt.setHint("回复 " + evaluate.getMember().getName() + ":");
        }
        else{
            sendEt.setHint("回复 " + contentEntity.getMember().getName() + ":");
        }
        //点击外部消失
        allLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                finish();
                return true;
            }
        });
        //发送评论
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = sendEt.getText().toString();
                if (Validators.isEmpty(content)) {
                    ToastUtil.toast("写点什么吧");
                } else {
                    AddEvaluateTask addEvaluateTask = new AddEvaluateTask(CommentActivity.this);
                    addEvaluateTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<AddEvaluateData>() {
                        @Override
                        public void failCallback(Result<AddEvaluateData> result) {
                            ToastUtil.toast(result.getMessage());
                        }
                    });

                    addEvaluateTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<AddEvaluateData>() {
                        @Override
                        public void successCallback(Result<AddEvaluateData> result) {
                            ToastUtil.toast("评论成功");
                            String evaluateId = result.getValue().getEvaluateId();
                            //将数据组合成一条评论
                            Evaluate evaluate = getCommentData(evaluateId);
//                            //更新数据库
                            CircleData.Circle circle = DaoFactory.getCircleDao().findById(contentEntity.getId());
                            CircleData.Circle userCircle = DaoFactory.getUserCircleDao().findById(contentEntity.getId() , contentEntity.getMember().getId());

                            if (null == circle.getEvaluates()) {
                                List<Evaluate> evaluateList = new ArrayList<Evaluate>();
                                evaluateList.add(evaluate);
                                circle.setEvaluates(evaluateList);
                                if (null != userCircle){
                                    userCircle.setEvaluates(evaluateList);
                                }
                            } else {
                                circle.getEvaluates().add(evaluate);
                                if(null != userCircle){
                                    userCircle.getEvaluates().add(evaluate);
                                }
                            }
                            DaoFactory.getCircleDao().replace(circle);
                            if (null != userCircle){
                                DaoFactory.getUserCircleDao().replace(userCircle);
                            }
                            //发送广播刷新列表
                            OnlyNotifyReceiver.notifyReceiver();
                            finish();
                        }
                    });

                    addEvaluateTask.execute(contentEntity.getId(), circleEvaluateId, content);
                }
            }
        });
    }

    //组合成评论的数据
    private Evaluate getCommentData(String id) {
        if (isIncomment){
            Member member = new Member();
            member.setId(LoginedUser.getLoginedUser().getId());
            member.setPic(LoginedUser.getLoginedUser().getPic());
            member.setName(LoginedUser.getLoginedUser().getName());
            evaluate.setMemberup(evaluate.getMember());
            evaluate.setMember(member);
            evaluate.setContent(content);
            evaluate.setCreationTime(new Date());
            return evaluate;
        }else{
            Evaluate evaluate = new Evaluate();
            evaluate.setId(id);
            evaluate.setContent(content);
            evaluate.setCreationTime(new Date());
            Member member = new Member();
            member.setId(LoginedUser.getLoginedUser().getId());
            member.setPic(LoginedUser.getLoginedUser().getPic());
            member.setName(LoginedUser.getLoginedUser().getName());
            evaluate.setMember(member);
            return evaluate;
        }
    }
}