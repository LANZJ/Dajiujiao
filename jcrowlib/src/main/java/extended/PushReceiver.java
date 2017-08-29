package extended;

import android.content.Context;

import com.jopool.crow.imlib.push.CWPushMeesageReceiver;
import com.jopool.crow.imlib.task.BindChannelIdTask;
import com.jopool.crow.imlib.utils.CWLogUtil;
import com.jopool.crow.imlib.utils.CWToastUtil;

/**
 * 百度推送通知接收
 *
 * @author xuan
 */
public abstract class PushReceiver extends CWPushMeesageReceiver {

    @Override
    public void onBind(Context context, int errorCode, String appid,
                       String userId, String channelId, String requestId) {
        CWLogUtil.d("百度推送onBind:errorCode[" + errorCode + "]appid[" + appid + "]userId[" + userId + "]channelId[" + channelId + "]requestId[" + requestId + "]");
//        //绑定百度推送
//        BindChannelIdTask bindTask = new BindChannelIdTask(context);
//        bindTask.execute(channelId);
    }
}
