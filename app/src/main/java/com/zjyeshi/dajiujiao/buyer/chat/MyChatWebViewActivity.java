package com.zjyeshi.dajiujiao.buyer.chat;

import android.content.Intent;
import android.view.View;

import com.jopool.crow.imkit.activity.CWWebViewActivity;
import com.jopool.crow.imkit.view.popview.CWWebViewOpLayoutView;
import com.jopool.crow.imlib.enums.CWConversationType;
import com.zjyeshi.dajiujiao.buyer.activity.my.MyContactActivity;
import com.zjyeshi.dajiujiao.buyer.utils.CircleUtil;

/**
 * Created by wuhk on 2016/8/16.
 */
public class MyChatWebViewActivity extends CWWebViewActivity {

    private String fromUserId;
    private String urlTitle;
    private String url;

    @Override
    protected CWWebViewOpLayoutView.OpItem[] getOpItemListForLine1() {
        return new CWWebViewOpLayoutView.OpItem[]{
                obtainCollection() ,
                obtainSendToCircle() ,
                obtainSendToUser(),
                obtainOpenBrowser()
        };
    }

    //收藏
    @Override
    public CWWebViewOpLayoutView.OpItem obtainCollection() {
        final CWWebViewOpLayoutView.OpItem opItem = super.obtainCollection();
        opItem.setL(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setInfo(opItem);
                CircleUtil.circleCollectOnChat(v , CircleUtil.getCollectWithLink(fromUserId , 2 , "" , urlTitle , url));
//                ToastUtil.toast("收藏");
            }
        });
        return opItem;
    }

    //发送到朋友圈
    @Override
    public CWWebViewOpLayoutView.OpItem obtainSendToCircle() {
        final CWWebViewOpLayoutView.OpItem opItem = super.obtainSendToCircle();
        opItem.setText("分享到酒友圈");
        opItem.setL(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setInfo(opItem);
//                ToastUtil.toast("发送到朋友圈");
                CircleUtil.circleAddUrl(MyChatWebViewActivity.this , 2 , "" , urlTitle ,url);
            }
        });
        return opItem;
    }

    //发送给朋友
    @Override
    public CWWebViewOpLayoutView.OpItem obtainSendToUser() {
        final CWWebViewOpLayoutView.OpItem opItem = super.obtainSendToUser();
        opItem.setL(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setInfo(opItem);
                Intent intent = new Intent();
                intent.setClass(MyChatWebViewActivity.this , MyContactActivity.class);
                startActivityForResult(intent , MyContactActivity.SELECTMEMBER);
            }
        });
        return opItem;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK != resultCode){
            return;
        }

        if(requestCode == MyContactActivity.SELECTMEMBER){
            String id = data.getStringExtra(MyContactActivity.TOMEMBERID);
            ChatManager.getInstance().sendMessage(MyChatWebViewActivity.this ,
                    id , urlTitle ,  url , CWConversationType.USER);
//            CWChat.getInstance().getSendMsgDelegate().sendUrlMessage(MyChatWebViewActivity.this ,
//                    id , urlTitle ,  url , CWConversationType.USER);
        }
    }

    /**赋值相关信息
     *
     * @param opItem
     */
    private void setInfo(CWWebViewOpLayoutView.OpItem opItem){
        fromUserId = (String)opItem.getData().get(CWWebViewOpLayoutView.OpItem.DATA_KEY_OWNERUSERID);
        url = (String)opItem.getData().get(CWWebViewOpLayoutView.OpItem.DATA_KEY_CURRENTURL);
        urlTitle = (String)opItem.getData().get(CWWebViewOpLayoutView.OpItem.DATA_KEY_TITLE);
    }
}
