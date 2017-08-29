package com.zjyeshi.dajiujiao.buyer.activity.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.IntentUtils;
import com.xuan.bigdog.lib.dialog.DGPromptDialog;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.activity.my.personal.ShowImageActivity;
import com.zjyeshi.dajiujiao.buyer.task.my.ApplyFriendTask;
import com.zjyeshi.dajiujiao.buyer.task.my.RemoveFriendTask;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.chat.ChatManager;
import com.zjyeshi.dajiujiao.buyer.circle.UserCircleActivity;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.task.data.my.MemberMoreData;
import com.zjyeshi.dajiujiao.buyer.task.my.MemberMoreInfoTask;
import com.zjyeshi.dajiujiao.buyer.utils.DialogUtil;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

/**
 * 用户信息
 * Created by wuhk on 2016/8/15.
 */
public class UserInfoActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.headIv)
    private ImageView headIv;
    @InjectView(R.id.nameTv)
    private TextView nameTv;
    @InjectView(R.id.opBtn)
    private Button opBtn;
    @InjectView(R.id.messageBtn)
    private Button messageBtn;
    @InjectView(R.id.circleLayout)
    private RelativeLayout circleLayout;
    @InjectView(R.id.callLayout)
    private RelativeLayout callLayout;
    @InjectView(R.id.delFriendLayout)
    private RelativeLayout delFriendLayout;

    private MemberMoreData memberMoreData;
    private String memberId;
    public static String MEMBERID = "memberId";
    public static String MEMBERNAME = "member_name";
    public static String MEMBERPIC = "member_pic";
    public static String MEMBERBGPIC = "member_bg_pic";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_user_info);
        initWidgets();
    }

    private void initWidgets() {
        memberId = getIntent().getStringExtra(MEMBERID);
        callLayout.setVisibility(View.GONE);
        delFriendLayout.setVisibility(View.GONE);
        MemberMoreInfoTask memberMoreInfoTask = new MemberMoreInfoTask(UserInfoActivity.this);
        memberMoreInfoTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<MemberMoreData>() {
            @Override
            public void failCallback(Result<MemberMoreData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });
        memberMoreInfoTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<MemberMoreData>() {
            @Override
            public void successCallback(Result<MemberMoreData> result) {
                memberMoreData = result.getValue();

                configInfo();
            }
        });

        memberMoreInfoTask.execute(memberId);
    }

    /**获取信息后配置信息
     *
     */
    private void configInfo(){
        titleLayout.configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).configTitle(memberMoreData.getName());

//        initImageViewDefault(headIv, memberMoreData.getPic(), R.drawable.default_tx);
        GlideImageUtil.glidImage(headIv , memberMoreData.getPic() , R.drawable.default_tx);
        headIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowImageActivity.startActivity(UserInfoActivity.this , memberMoreData.getPic());
            }
        });
        initTextView(nameTv, memberMoreData.getName());
        if (memberId.equals(LoginedUser.getLoginedUser().getId())) {
            opBtn.setVisibility(View.GONE);
            messageBtn.setVisibility(View.GONE);
            callLayout.setVisibility(View.GONE);
            delFriendLayout.setVisibility(View.GONE);
        } else {
            if (memberMoreData.isFriend()) {
                opBtn.setVisibility(View.GONE);
                messageBtn.setVisibility(View.VISIBLE);
                delFriendLayout.setVisibility(View.VISIBLE);
            } else {
                opBtn.setVisibility(View.VISIBLE);
                messageBtn.setVisibility(View.GONE);
                delFriendLayout.setVisibility(View.GONE);
            }
        }
        //是否在花名册中
        if (memberMoreData.isCustomer()){
            callLayout.setVisibility(View.VISIBLE);
        }else{
            callLayout.setVisibility(View.GONE);
        }

        //加好友
        opBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DGPromptDialog dialog = new DGPromptDialog.Builder(
                        UserInfoActivity.this)
                        .setTitle("提示")
                        .setMessage("请输入请求备注")
                        .setLeftBtnText("取消")
                        .setDefaultContent("我是" + LoginedUser.getLoginedUser().getName())
                        .setOnLeftBtnListener(new DGPromptDialog.PromptDialogListener() {
                            @Override
                            public void onClick(View view, String inputText) {

                            }
                        }).setRightBtnText("确定")
                        .setOnRightBtnListener(new DGPromptDialog.PromptDialogListener() {
                            @Override
                            public void onClick(View view, String inputText) {
                                if (!TextUtils.isEmpty(inputText)) {
                                    applyFriend(inputText);
                                } else {
                                    ToastUtil.toast("请输入备注信息");
                                }
                            }
                        }).create();
                dialog.show();
            }
        });

        //发消息
        messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatManager.getInstance().startConversion(UserInfoActivity.this, memberId);
            }
        });

        //查看酒友圈
        circleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserCircleActivity.startActivity(UserInfoActivity.this, memberId, memberMoreData.getName()
                        , memberMoreData.getPic(), memberMoreData.getCircleBackgroundPic());
            }
        });

        //删除好友
        delFriendLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtil.confirm(UserInfoActivity.this, "是否确定删除好友", "取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                },"确定",  new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeFriend(memberId);
                    }
                });
            }
        });

        //打电话
        callLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtil.confirm(UserInfoActivity.this, "是否拨打电话" +  memberMoreData.getPhone(), "取消",  new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }, "确定",new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IntentUtils.callByPhone(UserInfoActivity.this ,  memberMoreData.getPhone());
                    }
                });
            }
        });
    }

    /**添加好友
     *
     * @param remark
     */
    private void applyFriend(final String remark) {
        ApplyFriendTask applyFriendTask = new ApplyFriendTask(UserInfoActivity.this);
        applyFriendTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        applyFriendTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                finish();
                ToastUtil.toast("请求发送成功，到通讯录查看");
            }
        });

        applyFriendTask.execute(memberId, remark);
    }

    /**删除好友
     *
     * @param id
     */
    private void removeFriend(String id){
        RemoveFriendTask removeFriendTask = new RemoveFriendTask(UserInfoActivity.this);
        removeFriendTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        removeFriendTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                ToastUtil.toast("删除成功");
                initWidgets();
            }
        });

        removeFriendTask.execute("",id);
    }

    /**启动该Activity
     *
     * @param context
     * @param id
     */
    public static void startActivity(Context context, String id) {
        Intent intent = new Intent();
        intent.setClass(context, UserInfoActivity.class);
        intent.putExtra(MEMBERID, id);
        context.startActivity(intent);
    }
}
