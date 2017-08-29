package com.jopool.crow.imkit.activity.group;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.jopool.crow.CWChat;
import com.jopool.crow.R;
import com.jopool.crow.imkit.adapter.group.CWGroupUserSelectAdapter;
import com.jopool.crow.imkit.listeners.CWGroupUserSelectTitleListener;
import com.jopool.crow.imkit.listeners.CWSelectedUserSearchListener;
import com.jopool.crow.imkit.listeners.GetGroupUserSelectListProvider;
import com.jopool.crow.imkit.receiver.CWGroupSelectUserClickReceiver;
import com.jopool.crow.imkit.utils.lettersort.entity.ItemContent;
import com.jopool.crow.imkit.utils.lettersort.view.LetterSortView;
import com.jopool.crow.imkit.view.group.CWSelectedUserShowView;
import com.jopool.crow.imlib.entity.CWSelectUser;
import com.jopool.crow.imlib.entity.CWUser;
import com.jopool.crow.imlib.enums.CWGroupSelectUserType;
import com.jopool.crow.imlib.task.CWGroupCreateTask;
import com.jopool.crow.imlib.utils.CWValidator;
import com.jopool.crow.imlib.utils.asynctask.callback.AsyncTaskSuccessCallback;
import com.jopool.crow.imlib.utils.asynctask.helper.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * 创建群组选择人员的页面
 * <p/>
 * Created by xuan on 16/11/2.
 */
public class CWGroupUserSelectFragment extends android.support.v4.app.Fragment {
    private CWSelectedUserShowView selectedUserShowView;
    private LetterSortView letterSortView;
    List<CWSelectUser> inGroupMemberList;
   List<String>muer=new ArrayList<String>();
    List<String> ary = new ArrayList<String>();//保存结果的list
    private CWGroupSelectUserClickReceiver groupSelectUserClickReceiver;

    private List<ItemContent> dataList = new ArrayList<ItemContent>();
    private CWGroupUserSelectAdapter letterSelectAdapter;


    public static final String PARAM_GROUP_ID = "param_group_id";//群组Id
    public static final String PARAM_IN_GROUP_MEMBER = "param_in_group_member";//群成员
    public static final String PARAM_SELECT_USER_TYPE = "param_select_user_type";//从哪里进入
    public static final String PARAM_SELECT_USEID = "param_select_urs";//ID

    private String groupId = "";
    private String ty="";
    private CWGroupSelectUserType selectUserType;//区别启动方式，创建选择,添加选择,删除选择

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        groupSelectUserClickReceiver = new CWGroupSelectUserClickReceiver() {
            @Override
            public void refreshSelectedUser() {
                selectedUserShowView.refreshData(new DefaultUserShowAdapter());
            }
        };
        groupSelectUserClickReceiver.register(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        groupSelectUserClickReceiver.unregister(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cw_group_fragment_user_select, container, false);
        groupId = getActivity().getIntent().getStringExtra(PARAM_GROUP_ID);
        selectUserType = CWGroupSelectUserType.valueOf(getActivity().getIntent().getIntExtra(PARAM_SELECT_USER_TYPE, 0));
        ty=getActivity().getIntent().getStringExtra(PARAM_SELECT_USEID);
        String inGroupMemberJsonStr = getActivity().getIntent().getStringExtra(PARAM_IN_GROUP_MEMBER);
        inGroupMemberList = JSON.parseArray(inGroupMemberJsonStr, CWSelectUser.class);
        selectedUserShowView = (CWSelectedUserShowView) view.findViewById(R.id.selectedUserShowView);
        letterSortView = (LetterSortView) view.findViewById(R.id.letterSortView);
       // RongIMClient.getInstance().
        letterSelectAdapter = new CWGroupUserSelectAdapter(dataList, getActivity());
        letterSortView.getListView().setAdapter(letterSelectAdapter);

        refreshData();

        selectedUserShowView.setSearchListener(new CWSelectedUserSearchListener() {
            @Override
            public void doSearch(String keywords) {
                if (CWValidator.isEmpty(keywords)) {
                    letterSelectAdapter.notifyDataSetChanged(dataList);
                } else {
                    List<ItemContent> searchList = filterList(keywords);
                    letterSelectAdapter.notifyDataSetChanged(searchList);
                }
            }
        });

        return view;
    }

    //////////////////////////////////外部可调用方法///////////////////////////////////////////////

    /**
     * 根据不同情况配置Activity的Title
     */
    public void configActivityTitle() {
        if (getActivity() instanceof CWGroupUserSelectTitleListener) {
            ((CWGroupUserSelectTitleListener) getActivity()).configTitle(selectUserType.getValue());
        }
    }

    /**
     * 新建群组
     */
    public void createGroup() {
        final List<CWSelectUser> selectUserList = getSelectedUsers();
        //判断自己又没有，没有添加
        addSelfToList(selectUserList);

        CWGroupCreateTask.creatGroup(getActivity(), getGroupName(selectUserList), new AsyncTaskSuccessCallback<String>() {
            @Override
            public void successCallback(Result<String> result) {
                String groupId = result.getValue();
                addUserToGroup(groupId, true);
            }
        });
    }

    /**
     * 用户添加到群组
     *
     * @param groupId  群组Id
     * @param isCreate 是否是创建时添加
     */
    public void addUserToGroup(final String groupId, boolean isCreate) {
        if (selectUserType.equals(CWGroupSelectUserType.ADD)) {
            Set set = new HashSet();
            for (String cd : muer) {
                if (set.add(cd)) {
                    ary.add(cd);
                }
            }
            RongIM.getInstance().addMemberToDiscussion(ty, ary, new RongIMClient.OperationCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(getActivity(),"添加成功",Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
              Toast.makeText(getActivity(),"添加失败",Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Set set = new HashSet();
            for (String cd : muer) {
                if (set.add(cd)) {
                    ary.add(cd);
                }
            }

            if (RongIM.getInstance() != null && ary.size() > 0) {
                if (ary.size() == 1) {
                    RongIM.getInstance().startPrivateChat(getActivity(), ary.get(0), "单聊");
                    getActivity().finish();
                } else {

                    RongIM.getInstance().getRongIMClient().createDiscussion("讨论组", ary, new RongIMClient.CreateDiscussionCallback() {
                        @Override
                        public void onSuccess(String s) {
                            RongIM.getInstance().startDiscussionChat(getActivity(), s, "讨论组X");
                            getActivity().finish();

                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {
                            Toast.makeText(getActivity(), "创建失败，请重新创建!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } else {
                Toast.makeText(getActivity(), "至少选择一位", Toast.LENGTH_LONG).show();
            }

        }
    }
    /**
     * 在已有群组上添加
     */
    public void addUserToGroupNotCreate() {
        addUserToGroup(groupId, false);
    }

    /**
     * 从群组中移除
     */
    public void removeUserFromGroup() {
//        CWGroupRemoveUserTask.removeUserFromGroup(getActivity(), groupId, getSelectedUserIds(getSelectedUsers()), new AsyncTaskSuccessCallback<String>() {
//            @Override
//            public void successCallback(Result<String> result) {
//                CWGroupDetailFragment.reload = true;
        String op=getSelectedUserIds(getSelectedUsers());
          if (op!=null){

            RongIM.getInstance().removeMemberFromDiscussion(ty, op, new RongIMClient.OperationCallback() {

                @Override
                public void onSuccess() {
                    Toast.makeText(getActivity(), "移除成功", Toast.LENGTH_LONG).show();
                    getActivity().finish();

                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Toast.makeText(getActivity(), "移除失败", Toast.LENGTH_LONG).show();

                }
            });

}else{
    Toast.makeText(getActivity(), "请选择移除的人", Toast.LENGTH_LONG).show();
}
    }

    /**
     * 获取选中的用户
     *
     * @return
     */
    public List<CWSelectUser> getSelectedUsers() {
        List<CWSelectUser> selectUserList = new ArrayList<CWSelectUser>();

        for (ItemContent itemContent : dataList) {
           CWSelectUser user = (CWSelectUser) itemContent.getValue();

            if (user.isSelected()) {
                selectUserList.add(user);
               // muer.add(staff.getId());
                muer.add(user.getUserId());
            }
//            for(int i = 0;i < selectUserList.size(); i ++){
//             mLists.add(user.getUserId());
//
//            }
        }

       return  selectUserList;


    }

    //////////////////////////////////内部调用方法///////////////////////////////////////////////

    /**
     * 刷新数据
     */
    private void refreshData() {
        CWChat.getInstance().getGroupDelegate().getGetGroupUserSelectListProvider().getSelectUserList(new GetGroupUserSelectListProvider.GetListCallback() {
            @Override
            public void callBack(List<CWSelectUser> list) {
                if (selectUserType.equals(CWGroupSelectUserType.REMOVE)) {
                    transMembersToLetterNeedList(inGroupMemberList);
                } else {
                    transSelectUsersToLetterNeedList(list);
                    markMemberAlreadyInGroup(inGroupMemberList);
                }

                letterSelectAdapter.notifyDataSetChanged(dataList);
            }
        });

    }

    /**
     * 获取搜索结果
     *
     * @param keywords
     * @return
     */
    private List<ItemContent> filterList(String keywords) {
        List<ItemContent> searchList = new ArrayList<ItemContent>();
        for (ItemContent itemContent : dataList) {
            if (itemContent.getName().contains(keywords)) {
                searchList.add(itemContent);
            }
        }
        return searchList;
    }

    /**
     * 将已在群里的成员转换成所需格式
     *
     * @param inGroupMemberList
     */
    private void transMembersToLetterNeedList(List<CWSelectUser> inGroupMemberList) {
        dataList.clear();
        for (CWSelectUser user : inGroupMemberList) {
            if (!user.getUserId().equals(CWUser.getConnectUserId())) {
                ItemContent itemContent = new ItemContent(user.getUserName(), user);
                dataList.add(itemContent);
            }
        }

    }

    /**
     * 转换成字母排序所需格式
     *
     * @return
     */
    private void transSelectUsersToLetterNeedList(List<CWSelectUser> selectUserList) {
        dataList.clear();
        for (CWSelectUser user : selectUserList) {
            ItemContent itemContent = new ItemContent(user.getUserName(), user);
            dataList.add(itemContent);
        }
    }

    /**
     * 添加群成员时标记列表中已经在群里的成员
     *
     * @param inGroupMemberList
     */
    private void markMemberAlreadyInGroup(List<CWSelectUser> inGroupMemberList) {
        HashMap<String, Boolean> inMemberGroupMap = new HashMap<String, Boolean>();
        for (CWSelectUser user : inGroupMemberList) {
            inMemberGroupMap.put(user.getUserId(), true);
        }
        for (ItemContent itemContent : dataList) {
            CWSelectUser user = (CWSelectUser) itemContent.getValue();
            Boolean inGroup = inMemberGroupMap.get(user.getUserId());
            if (null != inGroup) {
                user.setGroupMember(inGroup);
            } else {
                user.setGroupMember(false);
            }
        }
    }

    /**
     * 判断自己在不在群组里面,没有添加
     *
     * @param selectUserList
     */
    private void addSelfToList(List<CWSelectUser> selectUserList) {
        //创建群组的时候,如果选中的人中没有自己，把自己添加进去
        boolean inclueSelf = false;
        for (CWSelectUser user : selectUserList) {
            if (user.getUserId().equals(CWUser.getConnectUserId())) {
                inclueSelf = true;
            }
        }
        if (!inclueSelf) {
            CWSelectUser selectUser = new CWSelectUser();
            selectUser.setUserId(CWUser.getUser().getUserId());
            selectUser.setUserName(CWUser.getUser().getName());
            selectUser.setUserLogo(CWUser.getUser().getUrl());
            selectUserList.add(selectUser);
        }
    }

    /**
     * 获取群名
     *
     * @param selectUserList
     * @return
     */
    private String getGroupName(List<CWSelectUser> selectUserList) {
        if (selectUserList.size() > 3) {
            selectUserList = selectUserList.subList(0, 3);
        }
        StringBuilder name = new StringBuilder();
        for (CWSelectUser user : selectUserList) {
            name.append(user.getUserName());
            name.append("、");
        }

        name.deleteCharAt(name.length() - 1);

        return name.toString();
    }

    /**
     * 获取选择的成员Id
     *
     * @param selectUserList
     * @return
     */
    private String getSelectedUserIds(List<CWSelectUser> selectUserList) {
        StringBuilder userIds = new StringBuilder();
        for (CWSelectUser user : selectUserList) {
            userIds.append(user.getUserId());
            userIds.append(",");
        }

        userIds.deleteCharAt(userIds.length() - 1);

        return userIds.toString();
    }

    /**
     * 默认人员选择显示适配器
     */
    private class DefaultUserShowAdapter implements CWSelectedUserShowView.UserShowAdapter {
        @Override
        public int getItmeCount() {
            return getSelectedUsers().size();
        }

        @Override
        public CWSelectUser getItem(int position, View view) {
            CWSelectUser user = getSelectedUsers().get(position);
            return user;
        }
    }
}
