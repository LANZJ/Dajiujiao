package com.jopool.crow.imkit.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import com.jopool.crow.CWChat;
import com.jopool.crow.R;
import com.jopool.crow.imkit.listeners.GetConversationTitleRightViewProvider;
import com.jopool.crow.imkit.receiver.CWConversationMessageReceiver;
import com.jopool.crow.imkit.receiver.CWManageActivityReceiver;
import com.jopool.crow.imkit.utils.album.BUAlbum;
import com.jopool.crow.imkit.utils.album.entity.ImageItem;
import com.jopool.crow.imkit.view.CWConversationView;
import com.jopool.crow.imlib.db.dao.CWChatDaoFactory;
import com.jopool.crow.imlib.entity.CWCacheUser;
import com.jopool.crow.imlib.entity.CWConversationMessage;
import com.jopool.crow.imlib.entity.CWGroup;
import com.jopool.crow.imlib.entity.CWUser;
import com.jopool.crow.imlib.enums.CWConversationType;
import com.jopool.crow.imlib.enums.CWReadStatus;
import com.jopool.crow.imlib.model.CWGroupModel;
import com.jopool.crow.imlib.model.CWUserModel;
import com.jopool.crow.imlib.task.CWGetMsgTask;
import com.jopool.crow.imlib.task.CWGetUserTask;
import com.jopool.crow.imlib.task.CWGroupGetGroupTask;
import com.jopool.crow.imlib.task.CWMakeMessageReadedTask;
import com.jopool.crow.imlib.utils.CWToastUtil;
import com.jopool.crow.imlib.utils.CWValidator;
import com.jopool.crow.imlib.utils.ChatFileUtils;
import com.jopool.crow.imlib.utils.asynctask.callback.AsyncTaskSuccessCallback;
import com.jopool.crow.imlib.utils.asynctask.helper.Result;
import com.jopool.crow.imlib.utils.io.FileUtils;

import java.io.File;
import java.util.List;

/**
 * 聊天会话
 *
 * @author xuan
 */
public class CWConversationActivity extends CWBaseActivity {
    /**
     * 从图库获取图片
     */
    public static final int CONVERSATION_REQUEST_CODE_FOR_ALBUM = 1000;
    /**
     * 从拍照获取
     */
    public static final int CONVERSATION_REQUEST_CODE_FOR_CAMERA = 1001;
    /**
     *拍照截图
     */

    public static final int CONVERSATION_REQUEST_CODE_FOR_SCREENSHOT =1002;


    public static final String PARAM_TOID = "param.toid";
    public static final String PARAM_CONVERSATION_TYPE = "param.conversation.type";
    public static final String PARAM_TITLE = "param.title";

    protected CWConversationView conversationView;// 聊天布局界面

    private String toId;// 聊天对方Id
    private int conversationTypeInt;// 聊天类型
    private CWConversationType conversationType;
    private String title;// 显示标题

    private CWConversationMessageReceiver conversationMessageReceiver;
    private CWManageActivityReceiver manageActivityReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cw_chat_layout_conversation);
        //param
        toId = getIntent().getStringExtra(PARAM_TOID);
        conversationTypeInt = getIntent().getIntExtra(PARAM_CONVERSATION_TYPE,
                0);
        conversationType = CWConversationType.valueOf(conversationTypeInt);
        title = getIntent().getStringExtra(PARAM_TITLE);
        //
        loadView();
        initWidgets();
        setCvTitle();
        //每次进来,更新下新消息
        CWGetMsgTask.getNewMsg(this, toId, conversationType);
        //如果是群聊的话，每次进来请求一下群组信息，并做相应处理
        if (conversationType.equals(CWConversationType.GROUP)) {
            CWGroupGetGroupTask.getGroupInfo(this, toId, new AsyncTaskSuccessCallback<CWGroupGetGroupTask.GetGroupData>() {
                @Override
                public void successCallback(Result<CWGroupGetGroupTask.GetGroupData> result) {

                    CWUserModel.getInstance().cacheUserByGroup(result.getValue());//缓存用户
                    CWGroupModel.getInstance().cacheGroupByGroup(result.getValue());//缓存群组

                    title = CWChat.getInstance().getGetGroupInfoProvider().getGroupById(result.getValue().getId()).getName();
                    title = title + "(" + result.getValue().getGroupUserList().size() + ")";
                    conversationView.getTitleLayout().getTitleTv().setText(title);
                    //
                    boolean isInGroup = false;
                    for (CWGroupGetGroupTask.GetGroupUserData getGroupUserData : result.getValue().getGroupUserList()) {
                        if (getGroupUserData.getUserId().equals(CWUser.getConnectUserId())) {
                            isInGroup = true;
                            break;
                        }
                    }
                    if (!isInGroup) {
                        CWToastUtil.displayTextShort("您已被移出群组");
                        //右上角和底部操作栏去掉
                        conversationView.getTitleLayout().getRightLayout().setVisibility(View.GONE);
                        conversationView.getBottomBarView().setVisibility(View.GONE);
                    }
                }
            });
        }else if(conversationType.equals(CWConversationType.USER)){
            //单聊,进入会话就同步一次用户信息
            CWGetUserTask.getUserInfo(this, toId, new AsyncTaskSuccessCallback<CWCacheUser>() {
                @Override
                public void successCallback(Result<CWCacheUser> result) {
                    CWUser user = CWChat.getInstance().getProviderDelegate().getGetUserInfoProvider()
                            .getUserById(toId);
                    if (null != user) {
                        title = user.getName();
                    }
                    conversationView.getTitleLayout().getTitleTv().setText(title);
                }
            });
        }
    }

    private void loadView() {
        conversationView = (CWConversationView) F(R.id.conversationView);
    }

    /**
     * 设置标题
     */
    private void setCvTitle() {
        if (null == title && CWConversationType.USER.equals(conversationType)) {
            // 单聊，设置title是空，那么就显示人名字
            CWUser user = CWChat.getInstance().getProviderDelegate().getGetUserInfoProvider()
                    .getUserById(toId);
            if (null != user) {
                title = user.getName();
            }
        } else if (null == title && CWConversationType.GROUP.equals(conversationType)) {
            // 群聊，设置title是空，那么就显示群组名称
            CWGroup group = CWChat.getInstance().getProviderDelegate().getGetGroupInfoProvider().getGroupById(toId);
            if (null != group) {
                title = group.getName();
            }
        }
        conversationView.getTitleLayout().getTitleTv().setText(title);
    }

    /**
     * Init View
     */
    private void initWidgets() {
        //设置标题相关数据
        conversationView.loadData(toId,
                CWConversationType.valueOf(conversationTypeInt));
        conversationView.getTitleLayout().getTitleTv().setText("");

        //设置右边图标
        GetConversationTitleRightViewProvider rightViewProvider = CWChat.getInstance().getProviderDelegate().getGetConversationTitleRightViewProvider();
        if (null != rightViewProvider) {
            final View[] views = rightViewProvider.getRightViews();
            final GetConversationTitleRightViewProvider.OnRightViewClickListener[] listeners = rightViewProvider.getRightViewClickListener();
            if (null != views) {
                for (int i = 0; i < views.length; i++) {
                    final int finalI = i;
                    conversationView.getTitleLayout().configRightView(views[i]);
                    views[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            GetConversationTitleRightViewProvider.Params p = new GetConversationTitleRightViewProvider.Params();
                            p.setToId(toId);
                            listeners[finalI].onClick(views[finalI], p);
                        }
                    });
                }
            }
        }
        // 设置消息监听
        conversationMessageReceiver = new CWConversationMessageReceiver() {
            @Override
            protected void onModifyMessageSendStatus(String messageId, int sendStatus) {
                conversationView.refreshChatList();
            }

            @Override
            protected void onModifyMessageReadStatus(String messageId, int readStatus) {
                conversationView.refreshChatList();
            }

            @Override
            protected void onRemoveMessage(String messageId) {
                conversationView.refreshChatList();
            }

            @Override
            protected void onAddMessage(CWConversationMessage addMessage) {
                if (null != addMessage && toId.equals(addMessage.getConversationToId())) {
                    //在聊天界面时，将受到消息再上传服务器修改状态
                    CWMakeMessageReadedTask.makeMessageReaded(CWConversationActivity.this , null , addMessage.getId());
                    // 设置为已读
                    CWChatDaoFactory.getConversationMessageDao()
                            .updateReadStatusByIdWidthUnRead(addMessage.getId(), CWReadStatus.READED);
                    conversationView.refreshChatList();
                }
            }

            @Override
            protected void onRefreshList() {
                conversationView.refreshChatList();
            }
        };
        conversationMessageReceiver.register(this);

        manageActivityReceiver = new CWManageActivityReceiver() {
            @Override
            protected void onFinish(String activityName) {
                if (CWConversationActivity.class.getSimpleName().equals(activityName)) {
                    finish();
                }
            }
        };
        manageActivityReceiver.register();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != conversationMessageReceiver) {
            conversationMessageReceiver.unregister(this);
            conversationMessageReceiver = null;
        }

        if (null != manageActivityReceiver) {
            manageActivityReceiver.unregister();
            manageActivityReceiver = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK != resultCode) {
            return;
        }

        switch (requestCode) {
            case CONVERSATION_REQUEST_CODE_FOR_ALBUM:
                List<ImageItem> imageItemList = BUAlbum.getSelListAndClear();
                if (CWValidator.isEmpty(imageItemList)) {
                    CWToastUtil.displayTextShort("图片选择出现问题，请重新选择～");
                }
                ImageItem selectedItem = imageItemList.get(0);
                //conversationView.sendImageMessage(selectedItem.imagePath);
                CWChat.getInstance().getSendMsgDelegate().sendImageMessage(this, toId, selectedItem.imagePath, conversationType);

                break;
            case CONVERSATION_REQUEST_CODE_FOR_CAMERA:
                String cameraRet = ChatFileUtils.getCameraRet();

                File file = new File(cameraRet);
                Uri uri = Uri.fromFile(new File(cameraRet));
                gotoCut(uri,CONVERSATION_REQUEST_CODE_FOR_SCREENSHOT);

//                String cameraRet = ChatFileUtils.getCameraRet();
//
//                //conversationView.sendImageMessage(cameraRet);
//                CWChat.getInstance().getSendMsgDelegate().sendImageMessage(this, toId, cameraRet, conversationType);
//                FileUtils.deleteFileOrDirectoryQuietly(cameraRet);// 处理完了了就删除
                break;

            case CONVERSATION_REQUEST_CODE_FOR_SCREENSHOT:
                String cameraRets = ChatFileUtils.getCameraRet();

                //conversationView.sendImageMessage(cameraRet);
                CWChat.getInstance().getSendMsgDelegate().sendImageMessage(this, toId, cameraRets, conversationType);
                FileUtils.deleteFileOrDirectoryQuietly(cameraRets);// 处理完了了就删除
                break;

            default:
                break;
        }
    }

    /**
     * 去截图
     *
     * @param uri
     * @param requestCode
     */
    private void gotoCut(Uri uri, int requestCode) {
        if (null == uri) {
            return;
        }
        String filePathName = ChatFileUtils.getCameraRet();
        File file = new File(filePathName);
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // setIntentExtra(intent);
//        intent.putExtra("outputX", 500);
//        intent.putExtra("outputY", 500);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        intent.putExtra("return-data", false);

        startActivityForResult(intent, requestCode);
    }



}
