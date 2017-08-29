package com.zjyeshi.dajiujiao.buyer.task.seller;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.jopool.crow.imlib.db.dao.CWChatDaoFactory;
import com.jopool.crow.imlib.entity.CWConversation;
import com.jopool.crow.imlib.entity.CWConversationMessage;
import com.jopool.crow.imlib.enums.CWConversationType;
import com.jopool.crow.imlib.enums.CWMessageContentType;
import com.zjyeshi.dajiujiao.buyer.dao.AddressUserDao;
import com.zjyeshi.dajiujiao.buyer.dao.DaoFactory;
import com.zjyeshi.dajiujiao.buyer.entity.contact.AddressUser;
import com.zjyeshi.dajiujiao.buyer.entity.enums.UserEnum;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.store.homepage.ALLStoreData;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.task.data.store.homepage.GetNearbyShopList;
import com.xuan.bigapple.lib.asynctask.helper.Result;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 查看商品采购列表
 * Created by wuhk on 2015/11/6.
 */
public class GetProductBuyTask extends BaseTask<GetNearbyShopList> {
    public GetProductBuyTask(Context context) {
        super(context);
    }

    @Override
    protected Result<GetNearbyShopList> onHttpRequest(Object... params) {
        HashMap<String ,String> paramMap = new HashMap<String , String>();
        Result<GetNearbyShopList> result = postCommon(UrlConstants.PURCHASEPRODUCT , paramMap);

        if (result.isSuccess()){
            GetNearbyShopList retData = JSON.parseObject(result.getMessage(), GetNearbyShopList.class);
            result.setMessage(retData.getMessage());
            if (retData.codeOk()){
                result.setValue(retData.getResult());
                List<ALLStoreData> allStoreDataList = new ArrayList<ALLStoreData>();
                allStoreDataList.addAll(result.getValue().getList());
                //将所有上级店铺信息插入数据库
                DaoFactory.getShopsDao().insertBatch(allStoreDataList);

                //卖家端将上级店铺加入聊天消息，这里先取消掉
                for (ALLStoreData allStoreData : allStoreDataList){
                    //店家信息加入聊天消息
                    CWConversation cwConversation = CWChatDaoFactory.getConversationDao().findByToId(allStoreData.getShop().getMember());
                    if (null == cwConversation){
                        CWConversation conversation = new CWConversation();
                        conversation.setToId(allStoreData.getShop().getMember());
                        conversation.setConversationType(CWConversationType.USER);
                        conversation.setOwnerUserId(LoginedUser.getLoginedUser().getId());
                        conversation.setPriority(AddressUserDao.getUserConversationPriority(getShopRole()));
                        conversation.setExt("");
                        conversation.setModifyTime(new Date());
                        conversation.setCreationTime(new Date());
                        CWChatDaoFactory.getConversationDao().insert(conversation);
                    }
                    //店家信息加入聊天消息联系人
                    AddressUser addressUser = new AddressUser();
                    addressUser.setOwnerUserId(LoginedUser.getLoginedUser().getId());
                    addressUser.setName(allStoreData.getShop().getName());
                    addressUser.setUserId(allStoreData.getShop().getMember());
                    addressUser.setAvatar(allStoreData.getShop().getPic());
                    addressUser.setPriority(AddressUserDao.getUserConversationPriority(getShopRole()));
                    DaoFactory.getAddressUserDao().replaceOrInsert(addressUser);
                }
            }else{
                result.setSuccess(false);
            }
        }
        return result;
    }

    private  int getShopRole(){
        int role = 0;
        UserEnum selfRole = LoginedUser.getLoginedUser().getUserEnum();
        if (selfRole.equals(UserEnum.TERMINAL)){
            role = UserEnum.DEALER.getValue();
        }else if (selfRole.equals(UserEnum.DEALER)){
            role = UserEnum.TERMINAL.getValue();
        }
        return  role;
    }
}
