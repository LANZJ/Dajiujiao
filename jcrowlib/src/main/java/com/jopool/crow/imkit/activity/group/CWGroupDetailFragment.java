package com.jopool.crow.imkit.activity.group;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jopool.crow.CWChat;
import com.jopool.crow.R;
import com.jopool.crow.imkit.adapter.group.CWGroupInfoUserAdapter;
import com.jopool.crow.imkit.dialog.CWConfirmDialog;
import com.jopool.crow.imkit.view.CWGroupUserGridView;
import com.jopool.crow.imlib.entity.CWSelectUser;
import com.jopool.crow.imlib.entity.CWUser;
import com.jopool.crow.imlib.enums.CWConversationType;
import com.jopool.crow.imlib.model.CWConversationModel;
import com.jopool.crow.imlib.model.CWGroupModel;
import com.jopool.crow.imlib.model.CWUserModel;
import com.jopool.crow.imlib.task.CWGroupGetGroupTask;
import com.jopool.crow.imlib.task.CWGroupRemoveUserTask;
import com.jopool.crow.imlib.utils.asynctask.callback.AsyncTaskSuccessCallback;
import com.jopool.crow.imlib.utils.asynctask.helper.Result;

import java.util.ArrayList;
import java.util.List;

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

    private List<CWSelectUser> dataList = new ArrayList<>();
    private CWGroupInfoUserAdapter groupInfoUserAdapter;

    public static final String PARAM_GROUP_ID = "param_group_id";
    private String groupId = "";

    public static boolean reload = false;//是否刷新页面

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cw_group_fragment_detail, container, false);
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
                CWChat.getInstance().getGroupDelegate().startModifyGroupNameActivity(getActivity(), groupNameTv.getText().toString(), groupId);
            }
        });

        groupInfoUserAdapter = new CWGroupInfoUserAdapter(getActivity(), dataList, groupId);
        gridView.setAdapter(groupInfoUserAdapter);

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CWConfirmDialog dialog = new CWConfirmDialog(getActivity(), "退出群聊", "删除并退出后，将不再接收此群聊信息", null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CWConversationModel.getInstance().deleteConversation(getActivity() , groupId , CWConversationType.GROUP);
                        CWGroupRemoveUserTask.removeUserFromGroup(getActivity(), groupId, CWUser.getConnectUserId(), new AsyncTaskSuccessCallback<String>() {
                            @Override
                            public void successCallback(Result<String> result) {
                                //将该聊天消息从会话列表中移除,并关闭相应界面
//                                CWConversationModel.getInstance().deleteConversation(getActivity(), groupId , CWConversationType.GROUP);
//                                CWChatDaoFactory.getConversationDao().deleteByToId(groupId);
//                                CWActivityManager.removeAndFinishIncludes(CWGroupDetailActivity.class.getSimpleName(), CWConversationActivity.class.getSimpleName());
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
        if (reload) {
            getGroupInfo();
        }
    }

    /**
     * 获取群组信息
     */
    private void getGroupInfo() {
        CWGroupGetGroupTask.getGroupInfo(getActivity(), groupId, new AsyncTaskSuccessCallback<CWGroupGetGroupTask.GetGroupData>() {
            @Override
            public void successCallback(Result<CWGroupGetGroupTask.GetGroupData> result) {

                CWUserModel.getInstance().cacheUserByGroup(result.getValue());//缓存用户
                CWGroupModel.getInstance().cacheGroupByGroup(result.getValue());//缓存群组

                reload = false;
                refreshData(result.getValue());
            }
        });
    }

    /**
     * 刷新数据
     *
     * @param groupData
     */
    private void refreshData(CWGroupGetGroupTask.GetGroupData groupData) {
        groupNameTv.setText(groupData.getName());
        dataList.clear();
        for (CWGroupGetGroupTask.GetGroupUserData data : groupData.getGroupUserList()) {
            CWSelectUser user = new CWSelectUser();
            user.setUserId(data.getUserId());
            user.setUserName(data.getUserName());
            user.setUserLogo(data.getUserLogo());
            dataList.add(user);
        }
        CWSelectUser creatorUser = groupData.getCreatorUser();
        if (creatorUser.getUserId().equals(CWUser.getConnectUserId())) {
            groupInfoUserAdapter.notifyDataSetChanged(true);
        } else {
            groupInfoUserAdapter.notifyDataSetChanged(false);
        }
    }
}
