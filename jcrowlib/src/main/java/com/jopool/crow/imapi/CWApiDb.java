package com.jopool.crow.imapi;

import com.jopool.crow.imkit.ui.CWUiModel;
import com.jopool.crow.imkit.ui.uientity.CWUiConversation;
import com.jopool.crow.imlib.entity.CWGroup;
import com.jopool.crow.imlib.entity.CWUser;
import com.jopool.crow.imlib.model.CWGroupModel;
import com.jopool.crow.imlib.model.CWUserModel;

import java.util.List;

/**
 * 本地数据库操作相关API
 * <p/>
 * Created by xuan on 16/11/7.
 */
public class CWApiDb {
    /**
     * 查找缓存的用户
     *
     * @param userId 用户userId
     * @return 返回用户信息
     */
    public CWUser getUserByUserId(String userId) {
        return CWUserModel.getInstance().getByUserId(userId);
    }

    /**
     * 查询缓存的群组信息
     *
     * @param grounpId 群组id
     * @return 返回群组信息
     */
    public CWGroup getGroupByGroupId(String grounpId) {
        return CWGroupModel.getInstance().getByGroupId(grounpId);
    }

    /**
     * 获取会话列表
     *
     * @return 会话列表
     */
    public List<CWUiConversation> getUiConversationList() {
        return CWUiModel.getInstance().getUiConversationList();
    }

}
