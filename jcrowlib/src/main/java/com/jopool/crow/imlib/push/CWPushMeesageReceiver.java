package com.jopool.crow.imlib.push;

import android.content.Context;

import com.baidu.android.pushservice.PushMessageReceiver;
import com.jopool.crow.imlib.utils.CWLogUtil;
import com.jopool.crow.imlib.utils.CWToastUtil;

import java.util.List;

/**
 * 推送消息接收器
 *
 * @author xuan
 */
public class CWPushMeesageReceiver extends PushMessageReceiver {
    /**
     * 调用PushManager.startWork后，sdk将对push
     * server发起绑定请求，这个过程是异步的。绑定请求的结果通过onBind返回。 如果您需要用单播推送，需要把这里获取的channel
     * id和user id上传到应用server中，再调用server接口用channel id和user id给单个手机或者用户推送。
     *
     * @param context   BroadcastReceiver的执行Context
     * @param errorCode 绑定接口返回值，0 - 成功
     * @param appid     应用id。errorCode非0时为null
     * @param userId    应用user id。errorCode非0时为null
     * @param channelId 应用channel id。errorCode非0时为null
     * @param requestId 向服务端发起的请求id。在追查问题时有用；
     */
    @Override
    public void onBind(Context context, int errorCode, String appid,
                       String userId, String channelId, String requestId) {
        CWLogUtil.d("百度推送onBind:errorCode[" + errorCode + "]appid[" + appid + "]userId[" + userId + "]channelId[" + channelId + "]requestId[" + requestId + "]");
    }

    /**
     * 接收通知点击的函数。
     *
     * @param context             上下文
     * @param title               推送的通知的标题
     * @param description         推送的通知的描述
     * @param customContentString 自定义内容，为空或者json字符串
     */
    @Override
    public void onNotificationClicked(Context context, String title,
                                      String description, String customContentString) {
        CWLogUtil.d("百度推送onBind:title[" + title + "]description[" + description + "]customContentString[" + customContentString + "]");
    }

    /**
     * 接收通知到达的函数。
     *
     * @param context             上下文
     * @param title               推送的通知的标题
     * @param description         推送的通知的描述
     * @param customContentString 自定义内容，为空或者json字符串
     */
    @Override
    public void onNotificationArrived(Context context, String title,
                                      String description, String customContentString) {
        CWLogUtil.d("百度推送onNotificationArrived:title[" + title + "]description[" + description + "]customContentString[" + customContentString + "]");
    }

    /**
     * delTags() 的回调函数。
     *
     * @param context    上下文
     * @param errorCode  错误码。0表示某些tag已经删除成功；非0表示所有tag均删除失败。
     * @param sucessTags 成功删除的tag
     * @param failTags   删除失败的tag
     * @param requestId  分配给对云推送的请求的id
     */
    @Override
    public void onDelTags(Context context, int errorCode,
                          List<String> sucessTags, List<String> failTags, String requestId) {
        CWLogUtil.d("百度推送onDelTags:errorCode[" + errorCode + "]sucessTags[" + sucessTags + "]failTags[" + failTags + "]requestId[" + requestId + "]");
    }

    /**
     * listTags() 的回调函数。
     *
     * @param context   上下文
     * @param errorCode 错误码。0表示列举tag成功；非0表示失败。
     * @param tags      当前应用设置的所有tag。
     * @param requestId 分配给对云推送的请求的id
     */
    @Override
    public void onListTags(Context context, int errorCode, List<String> tags,
                           String requestId) {
        CWLogUtil.d("百度推送onListTags:errorCode[" + errorCode + "]tags[" + tags + "]requestId[" + requestId + "]");

    }

    /**
     * 接收透传消息的函数。
     *
     * @param context             上下文
     * @param message             推送的消息
     * @param customContentString 自定义内容,为空或者json字符串
     */
    @Override
    public void onMessage(Context context, String message,
                          String customContentString) {
        CWLogUtil.d("百度推送onMessage:message[" + message + "]customContentString[" + customContentString + "]");
    }

    /**
     * setTags() 的回调函数。
     *
     * @param context    上下文
     * @param errorCode  错误码。0表示某些tag已经设置成功；非0表示所有tag的设置均失败。
     * @param sucessTags 设置成功的tag
     * @param failTags   设置失败的tag
     * @param requestId  分配给对云推送的请求的id
     */
    @Override
    public void onSetTags(Context context, int errorCode,
                          List<String> sucessTags, List<String> failTags, String requestId) {
        CWLogUtil.d("百度推送onSetTags:errorCode[" + errorCode + "]sucessTags[" + sucessTags + "]failTags[" + failTags + "]requestId[" + requestId + "]");
    }

    /**
     * PushManager.stopWork() 的回调函数。
     *
     * @param context   上下文
     * @param errorCode 错误码。0表示从云推送解绑定成功；非0表示失败。
     * @param requestId 分配给对云推送的请求的id
     */
    @Override
    public void onUnbind(Context context, int errorCode, String requestId) {
        CWLogUtil.d("百度推送onUnbind:errorCode[" + errorCode + "]requestId[" + requestId + "]");
    }

}
