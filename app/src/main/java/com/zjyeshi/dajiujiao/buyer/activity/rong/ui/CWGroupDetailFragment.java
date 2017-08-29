package com.zjyeshi.dajiujiao.buyer.activity.rong.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jopool.crow.R;
import com.jopool.crow.imkit.dialog.CWConfirmDialog;
import com.jopool.crow.imkit.view.CWGroupUserGridView;
import com.jopool.crow.imlib.entity.CWSelectUser;
import com.jopool.crow.imlib.task.CWGroupGetGroupTask;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.zjyeshi.dajiujiao.buyer.App;
import com.zjyeshi.dajiujiao.buyer.activity.frame.fragment.MianFramActivity;
import com.zjyeshi.dajiujiao.buyer.task.GetUserInfoTask;
import com.zjyeshi.dajiujiao.buyer.task.data.UserData;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Discussion;

/**
 * 群组详情页面(这里是添加人员到群组和移除人员,修改群组的入口界面)
 * <p/>
 * Created by xuan on 16/11/2.
 */
public class CWGroupDetailFragment extends android.support.v4.app.Fragment {
    private RelativeLayout groupNameLayout;
    private TextView groupNameTv;
    private CWGroupUserGridView gridView;
    private Button exitBtn;
    private Discussion mDiscussion;
    private List<CWSelectUser> dataList = new ArrayList<>();
    private CWGroupInfoUserAdapter groupInfoUserAdapter;
    private List<String> ids;
    public static final String PARAM_GROUP_ID = "param_group_id";
    private String groupId = "";
   // private String createIds="";
    private String name;
    private String comdid="";
    public static boolean reload = false;//是否刷新页面
    private TextView memberSize;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cw_group_fragment_detail, container, false);
        memberSize = (TextView) view. findViewById(R.id.discu_member_size);
        groupNameLayout = (RelativeLayout) view.findViewById(R.id.groupNameLayout);
        groupNameTv = (TextView) view.findViewById(R.id.groupNameTv);
        gridView = (CWGroupUserGridView) view.findViewById(R.id.groupUserGridView);
        exitBtn = (Button) view.findViewById(R.id.exitBtn);

        initWidgets();
        return view;
    }

    private void initWidgets() {
        groupId = getActivity().getIntent().getStringExtra(PARAM_GROUP_ID);

        groupNameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //改讨论组名字
              // CWChat.getInstance().getGroupDelegate().startModifyGroupNameActivity(getActivity(), groupNameTv.getText().toString(), groupId);
               // RongIM.getInstance().setDiscussionName(groupId, name,Parcelable );
                Intent intent = new Intent();
                intent.setClass(getActivity(), CWGroupModifyNameActivity.class);
                intent.putExtra(CWGroupModifyNameFragment.PARAM_GROUP_NAME, name);
                intent.putExtra(CWGroupModifyNameFragment.PARAM_GROUP_ID, comdid);
                getActivity().startActivity(intent);
                }
        });


        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CWConfirmDialog dialog = new CWConfirmDialog(getActivity(), "退出群聊", "删除并退出后，将不再接收此群聊信息", null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RongIM.getInstance().quitDiscussion(groupId, new RongIMClient.OperationCallback() {
                            @Override
                            public void onSuccess() {
                                RongIM.getInstance().removeConversation(Conversation.ConversationType.DISCUSSION, groupId);
                                Intent i = new Intent();
                                i.setClass(getActivity(), MianFramActivity.class);
                                getActivity().startActivity(i);
                                getActivity().finish();
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {

                            }
                        });
                    }
                });
                dialog.show();
            }
        });

        getGroupInfo();


    }

    @Override
    public void onResume() {
        super.onResume();
        App.ods(mDiscussion);
     if (reload) {
        dataList.clear();
            getGroupInfo();


      }
    }

    /**
     * 获取群组信息
     */
    private void getGroupInfo() {
        RongIM.getInstance().getDiscussion(groupId, new RongIMClient.ResultCallback<Discussion>() {
                @Override
                public void onSuccess(Discussion discussion) {
                    mDiscussion = discussion;
                    if (mDiscussion != null) {
                        initData(mDiscussion);
                        name=discussion.getName();
                        groupNameTv.setText(name);
                        comdid=discussion.getId();
                    //    createIds=discussion.getId();
                    }
                    App.ods(discussion);

                }

            private void initData(Discussion mDiscussion) {

                memberSize.setText("讨论组成员(" + mDiscussion.getMemberIdList().size() + ")");
               // createIds = mDiscussion.getId();
                ids = mDiscussion.getMemberIdList();
                if (ids != null) {
                  for (int w=0;w<ids.size();w++){
                      final String ipw=ids.get(w);
                      GetUserInfoTask.getUserTodo(getActivity(), ids.get(w), new AsyncTaskSuccessCallback<UserData>() {
                          @Override
                          public void successCallback(com.xuan.bigapple.lib.asynctask.helper.Result<UserData> result) {
                              //Uri pic =  Uri.parse((String)result.getValue().getPic());
                              //String usersw=ids.get(w);
                              CWSelectUser user = new CWSelectUser();
                              user.setUserId(ipw);
                              user.setUserName(result.getValue().getName());
                              user.setUserLogo(result.getValue().getPic());
                              dataList.add(user);
//                              ToastUtil.toast(dataList.size()+"");


                              groupInfoUserAdapter = new CWGroupInfoUserAdapter(getActivity(), dataList, groupId,comdid);
                              gridView.setAdapter(groupInfoUserAdapter);
                              groupInfoUserAdapter.notifyDataSetChanged(true);

                          }
                      });
                    }



                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode e) {

            }
        });

    }

    /**
     * 刷新数据
     *
     * @param groupData
     */
    private void refreshData(CWGroupGetGroupTask.GetGroupData groupData) {
//        groupNameTv.setText(name);
//        dataList.clear();
//        for (CWGroupGetGroupTask.GetGroupUserData data : groupData.getGroupUserList()) {
//            CWSelectUser user = new CWSelectUser();
//            user.setUserId(data.getUserId());
//            user.setUserName(data.getUserName());
//            user.setUserLogo(data.getUserLogo());
//            dataList.add(user);
//        }
//        CWSelectUser creatorUser = groupData.getCreatorUser();
//        if (creatorUser.getUserId().equals(CWUser.getConnectUserId())) {
//            groupInfoUserAdapter.notifyDataSetChanged(true);
//        } else {
//            groupInfoUserAdapter.notifyDataSetChanged(false);
//        }
    }
}
