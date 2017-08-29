package com.zjyeshi.dajiujiao.buyer.circle.itemview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.ioc.ViewUtils;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigappleui.lib.view.listview.BUHighHeightListView;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.my.UserInfoActivity;
import com.zjyeshi.dajiujiao.buyer.activity.my.personal.ShowImageActivity;
import com.zjyeshi.dajiujiao.buyer.chat.MyChatWebViewActivity;
import com.zjyeshi.dajiujiao.buyer.circle.CircleCommentAdapter;
import com.zjyeshi.dajiujiao.buyer.circle.CircleImageView;
import com.zjyeshi.dajiujiao.buyer.circle.CommentActivity;
import com.zjyeshi.dajiujiao.buyer.circle.itementity.CircleContentEntity;
import com.zjyeshi.dajiujiao.buyer.circle.receiver.OnlyNotifyReceiver;
import com.zjyeshi.dajiujiao.buyer.circle.receiver.SetListViewReceiver;
import com.zjyeshi.dajiujiao.buyer.circle.task.CirclePraiseTask;
import com.zjyeshi.dajiujiao.buyer.circle.task.DeleteCircleTask;
import com.zjyeshi.dajiujiao.buyer.circle.task.data.CircleData;
import com.zjyeshi.dajiujiao.buyer.circle.task.data.Evaluate;
import com.zjyeshi.dajiujiao.buyer.circle.task.data.GetPraiseData;
import com.zjyeshi.dajiujiao.buyer.circle.task.data.Member;
import com.zjyeshi.dajiujiao.buyer.circle.task.data.Praise;
import com.zjyeshi.dajiujiao.buyer.circle.view.MultiImageGridView;
import com.zjyeshi.dajiujiao.buyer.circle.view.RemarkView;
import com.zjyeshi.dajiujiao.buyer.common.PassConstans;
import com.zjyeshi.dajiujiao.buyer.dao.DaoFactory;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.utils.CircleUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.buyer.utils.FriendlyTimeUtil;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;
import com.zjyeshi.dajiujiao.buyer.utils.SingleSelectDialogUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import io.rong.imkit.RongIM;

/**
 * 图文，网页地址布局
 * <p/>
 * Created by xuan on 15/10/19.
 */
public class CircleContentView extends BaseView {
    @InjectView(R.id.headIv)
    private ImageView headIv;
    @InjectView(R.id.nameTv)
    private TextView nameTv;
    @InjectView(R.id.contentTv)
    private TextView contentTv;
    @InjectView(R.id.timeTv)
    private TextView timeTv;
    @InjectView(R.id.remarkBtn)
    private ImageView remarkBtn;
    @InjectView(R.id.circleChatIv)
    private ImageView circleChatIv;//聊天
    @InjectView(R.id.singleImageIv)
    private CircleImageView singleImageIv;//图片单个
    @InjectView(R.id.multiImageGv)
    private MultiImageGridView multiImageGv;//2x2图片
    @InjectView(R.id.pageLayout)
    private View pageLayout;//网页布局
    @InjectView(R.id.pageImageIv)
    private ImageView pageImageIv;//网页图标
    @InjectView(R.id.pageTextTv)
    private TextView pageTextTv;//网页标题
    @InjectView(R.id.commentLayout)
    private RelativeLayout commentLayout;//评论布局
    @InjectView(R.id.praiseLayout)
    private LinearLayout praiseLayout;//赞布局
    @InjectView(R.id.praiseNameTv)
    private TextView praiseNameTv;
//    @InjectView(R.id.countNumTv)
//    private TextView countNumTv;
    @InjectView(R.id.remarkLayout)
    private LinearLayout remarkLayout;//赞和评论
    @InjectView(R.id.commentListView)
    private BUHighHeightListView commentListView;//评论列表

    @InjectView(R.id.dividerView)
    private View dividerView;

    public CircleContentView(Context context) {
        super(context);
    }

    public CircleContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void dgInit() {
        inflate(getContext(), R.layout.fragment_circle_imagecontent, this);
        ViewUtils.inject(this, this);
    }

    /**
     * 内容是图文+网页
     *
     * @param contentEntity
     */
    public void bindData(final CircleContentEntity contentEntity, final int position) {
        //可选布局先全部隐藏，后续需要的在bindXXX中显示就可以
        singleImageIv.setVisibility(View.GONE);
        multiImageGv.setVisibility(View.GONE);
        pageLayout.setVisibility(View.GONE);

        final List<Praise> praiseList = new ArrayList<Praise>();
        List<Evaluate> evaluateList = new ArrayList<Evaluate>();
        CircleCommentAdapter circleCommentAdapter;//评论列表适配器
        String fromMemberId = contentEntity.getMember().getId();

        initTextView(nameTv, contentEntity.getMember().getName());
        if (Validators.isEmpty(contentEntity.getContent())){
            contentTv.setVisibility(GONE);
        }else{
            contentTv.setVisibility(VISIBLE);
            initTextView(contentTv, contentEntity.getContent());
        }
        //文字收藏
        CircleUtil.circleCollect(contentTv , CircleUtil.getCollectWithContent(fromMemberId , contentEntity.getContent()));
        initTextView(timeTv, FriendlyTimeUtil.friendlyTime(contentEntity.getTime()));
        GlideImageUtil.glidImage(headIv ,  ExtraUtil.getResizePic(contentEntity.getMember().getPic() , 150 , 150) , R.drawable.head_default);
        praiseList.addAll(contentEntity.getPraiseList());
        evaluateList.addAll(evaluateList = contentEntity.getEvaluateList());
        //点击头像进入详情
        headIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Member member = contentEntity.getMember();
                UserInfoActivity.startActivity(getContext() , member.getId());
            }
        });
        nameTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                headIv.performClick();
            }
        });
        final String memberId = contentEntity.getMember().getId();
        if (!memberId.equals(LoginedUser.getLoginedUser().getId())){
            circleChatIv.setVisibility(VISIBLE);
            circleChatIv.setEnabled(true);
            circleChatIv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                  //  ChatManager.getInstance().startConversion(getContext(), contentEntity.getMember().getId());
                    RongIM.getInstance().startPrivateChat(getContext(), contentEntity.getMember().getId(),contentEntity.getMember().getName());
                }
            });

        }else{
            circleChatIv.setVisibility(INVISIBLE);
            circleChatIv.setEnabled(false);
        }
        //点击显示赞和评论弹出框
        remarkBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                RemarkView remarkView = new RemarkView((Activity) getContext(), LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                setPopWindow(contentEntity, position, remarkView);
                //设置默认获取焦点
                remarkView.setFocusable(true);
                //以某个控件的x和y的偏移量位置开始显示窗口
                remarkView.showAsDropDown(remarkLayout, 0, -(int) getResources().getDimension(R.dimen.dimen_117));
                //如果窗口存在，则更新
                remarkView.update();
            }
        });

        // 初始化赞和评论
        if (Validators.isEmpty(praiseList) && Validators.isEmpty(evaluateList)) {
            commentLayout.setVisibility(GONE);
        } else {
            commentLayout.setVisibility(VISIBLE);
            if (Validators.isEmpty(evaluateList)) {
                dividerView.setVisibility(GONE);
            }
            //赞
            if (Validators.isEmpty(praiseList)) {
                praiseLayout.setVisibility(GONE);
                dividerView.setVisibility(GONE);
            } else {
                dividerView.setVisibility(VISIBLE);
                praiseLayout.setVisibility(VISIBLE);
                StringBuilder praisename = new StringBuilder();
                for (Praise praise : praiseList) {
                    praisename.append(praise.getMember().getName());
                    praisename.append("、");
                }
                praisename.deleteCharAt(praisename.length() - 1);
                initTextView(praiseNameTv, praisename.toString());
//                if (praiseList.size() < 4) {
//                    initTextView(countNumTv, "觉得很赞");
//                } else {
//                    initTextView(countNumTv, "等" + String.valueOf(praiseList.size()) + "人");
//                }
            }
            //评论
            circleCommentAdapter = new CircleCommentAdapter(getContext(), evaluateList, contentEntity, String.valueOf(position));
            commentListView.setAdapter(circleCommentAdapter);
        }

        //图文
        if (!Validators.isEmpty(contentEntity.getImageUrls())) {
            if (1 == contentEntity.getImageUrls().length) {
                //单图
                final String url = contentEntity.getImageUrls()[0];
                singleImageIv.setVisibility(View.VISIBLE);
                singleImageIv.setImageMode(CircleImageView.IMAGE_MODE_SINGLE);
//                setSingleImage(singleImageIv, url);
//                loadImageViewTarget(singleImageIv , url);
                GlideImageUtil.loadCircleSingleImageViewTarget(singleImageIv , url);

                //单个图片收藏
                CircleUtil.circleCollect(singleImageIv , CircleUtil.getCollectWithPic(fromMemberId , url));

                singleImageIv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent();
//                        intent.setClass(getContext(), ShowImageActivity.class);
//                        intent.putExtra(PassConstans.IMAGEURL, url);
//                        getContext().startActivity(intent);
                        if (singleImageIv.getDrawable() == getResources().getDrawable(R.drawable.default_img)){
                            ToastUtil.toast("图片正在加载");
                        }else{
                            ShowImageActivity.startActivity(getContext() , url , memberId);
                        }
                    }
                });
            } else {
                //多图
                multiImageGv.setVisibility(View.VISIBLE);
                multiImageGv.bindData(Arrays.asList(contentEntity.getImageUrls()) , fromMemberId);
            }
        }

        if (contentEntity.getPageType() == 2 || contentEntity.getPageType() == 3){
            pageLayout.setVisibility(VISIBLE);
//            initImageView(pageImageIv, contentEntity.getPageImage() , R.drawable.url_default_ic);
            GlideImageUtil.glidImage(pageImageIv , contentEntity.getPageImage() , R.drawable.url_default_ic);
            initTextView(pageTextTv, contentEntity.getPageContent());
            pageLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyChatWebViewActivity.startForUrl(getContext() , contentEntity.getPageUrl() , contentEntity.getMember().getId());
                }
            });

            CircleUtil.circleCollect(pageLayout , CircleUtil.getCollectWithLink(fromMemberId , contentEntity.getPageType()
                    ,contentEntity.getPageImage() , contentEntity.getPageContent() , contentEntity.getPageUrl()));
        }else{
            pageLayout.setVisibility(GONE);
        }

        //设置长按删除
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (contentEntity.getMember().getId().equals(LoginedUser.getLoginedUser().getId())) {
                    List<String> itemList = new ArrayList<String>();
                    List<View.OnClickListener> onClickListenerList = new ArrayList<View.OnClickListener>();
                    itemList.add("删除动态");
                    onClickListenerList.add(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteCircle(contentEntity);
                        }
                    });

                    SingleSelectDialogUtil dialog = new SingleSelectDialogUtil.Builder(getContext())
                            .setItemTextAndOnClickListener(
                                    itemList.toArray(new String[itemList.size()]),
                                    onClickListenerList
                                            .toArray(new View.OnClickListener[onClickListenerList
                                                    .size()])).createInstance();
                    dialog.show();
                }
                return true;
            }
        });
    }

    //popWindow
    private void setPopWindow(final CircleContentEntity contentEntity, final int position , final RemarkView remarkView) {
        remarkView.getContentView().setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    remarkView.dismiss();
                }
            }
        });
        List<Praise> zanList = new ArrayList<Praise>();
        zanList.addAll(contentEntity.getPraiseList());
        //判断自己在不在赞里面
        if (Validators.isEmpty(zanList)){
            remarkView.setZanTv("赞");
        }else{
            for (int i = 0 ; i < zanList.size() ; i ++){
                if (zanList.get(i).getMember().getId().equals(LoginedUser.getLoginedUser().getId())){
                    remarkView.setZanTv("取消赞");
                    break;
                }else{
                    remarkView.setZanTv("赞");
                }
            }
        }
        //赞点击
        remarkView.setZanClick(new OnClickListener() {
            @Override
            public void onClick(View v) {
                remarkView.dismiss();
                if (remarkView.isPraised()){
                    cancelZan(contentEntity , remarkView);
                }else{
                    zan(contentEntity , remarkView);
                }
            }
        });
        //评论点击事件
        remarkView.setComment(new OnClickListener() {
            @Override
            public void onClick(View v) {
                remarkView.dismiss();
                SetListViewReceiver.notifyReceiver(String.valueOf(position + 2));
                Intent intent = new Intent();
                intent.putExtra(PassConstans.CONTENTENTITY, contentEntity);
                intent.putExtra(PassConstans.ISINCOMMENT, "comment");
                intent.putExtra(PassConstans.POSITION, String.valueOf(position));
                intent.setClass(getContext(), CommentActivity.class);
                getContext().startActivity(intent);
            }
        });
    }

    //增加赞
    private void zan(final CircleContentEntity contentEntity , final RemarkView remarkView){
        CirclePraiseTask circlePraiseTask = new CirclePraiseTask(getContext());
        circlePraiseTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<GetPraiseData>() {
            @Override
            public void failCallback(Result<GetPraiseData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });
        circlePraiseTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<GetPraiseData>() {
            @Override
            public void successCallback(Result<GetPraiseData> result) {
                String praiseId = result.getValue().getPraiseId();
                remarkView.dismiss();
                CircleData.Circle circle = DaoFactory.getCircleDao().findById(contentEntity.getId());
                CircleData.Circle userCircle = DaoFactory.getUserCircleDao().findById(contentEntity.getId() , contentEntity.getMember().getId());
                //把自己加入到赞人员列表
                Member member = new Member();
                member.setId(LoginedUser.getLoginedUser().getId());
                member.setName(LoginedUser.getLoginedUser().getName());
                member.setPic(LoginedUser.getLoginedUser().getPic());
                Praise praise = new Praise();
                praise.setMember(member);
                praise.setId(praiseId);
                praise.setCreationTime(new Date());

                List<Praise> tempList = new ArrayList<Praise>();
                tempList.addAll(contentEntity.getPraiseList());
                tempList.add(praise);
                circle.setPraises(tempList);
                DaoFactory.getCircleDao().replace(circle);
                if (null != userCircle){
                    userCircle.setPraises(tempList);
                    DaoFactory.getUserCircleDao().replace(userCircle);
                }
                OnlyNotifyReceiver.notifyReceiver();
            }
        });
        circlePraiseTask.execute(contentEntity.getId(), "false");
    }
    //取消赞
    private void cancelZan(final  CircleContentEntity contentEntity ,final RemarkView remarkView){
        CirclePraiseTask circlePraiseTask = new CirclePraiseTask(getContext());
        circlePraiseTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<GetPraiseData>() {
            @Override
            public void failCallback(Result<GetPraiseData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });
        circlePraiseTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<GetPraiseData>() {
            @Override
            public void successCallback(Result<GetPraiseData> result) {
                String praiseId = result.getValue().getPraiseId();
                remarkView.dismiss();
                CircleData.Circle circle = DaoFactory.getCircleDao().findById(contentEntity.getId());
                CircleData.Circle userCircle = DaoFactory.getUserCircleDao().findById(contentEntity.getId() , contentEntity.getMember().getId());
                //将自己从赞人员列表中删除
               String id = result.getValue().getPraiseId();
                for (int i = 0 ; i < contentEntity.getPraiseList().size() ; i ++){
                    if (contentEntity.getPraiseList().get(i).getId().equals(id)){
                        contentEntity.getPraiseList().remove(i);
                    }
                }
                circle.setPraises(contentEntity.getPraiseList());
                DaoFactory.getCircleDao().replace(circle);
                if (null != userCircle){
                    userCircle.setPraises(contentEntity.getPraiseList());
                    DaoFactory.getUserCircleDao().replace(userCircle);
                }
                OnlyNotifyReceiver.notifyReceiver();
            }
        });
        circlePraiseTask.execute(contentEntity.getId(), "true");
    }


    /**删除动态
     *
     * @param contentEntity
     */

    private void deleteCircle(final CircleContentEntity contentEntity) {
        DeleteCircleTask deleteCircleTask = new DeleteCircleTask(getContext());
        deleteCircleTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        deleteCircleTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                ToastUtil.toast("删除成功");
                DaoFactory.getCircleDao().deleteById(contentEntity.getId(), LoginedUser.getLoginedUser().getId());
                CircleData.Circle userCircle = DaoFactory.getUserCircleDao().findById(contentEntity.getId() , contentEntity.getMember().getId());
                if (null != userCircle){
                    DaoFactory.getUserCircleDao().deleteById(contentEntity.getId() , contentEntity.getMember().getId());
                }
                OnlyNotifyReceiver.notifyReceiver();
            }
        });

        deleteCircleTask.execute(contentEntity.getId());
    }

//    private void loadImageViewTarget(final CircleImageView circleImageView , String url) {
//        circleImageView.setImageResource(R.drawable.default_img);
//        SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>() {
//            @Override
//            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                circleImageView.setImageBitmap(resource);
//            }
//        };
//
//        Glide
//                .with(App.instance) // safer!
//                .load(url)
//                .asBitmap()
//                .placeholder(R.drawable.default_img)
//                .into(target);
//    }
}
