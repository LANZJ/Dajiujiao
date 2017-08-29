package com.jopool.crow.imkit.view;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.jopool.crow.R;
import com.jopool.crow.imkit.listeners.TopNoticeProvider;

/**
 * 会话界面顶部通知栏
 * Created by zhum on 2016/5/12.
 */
public class CWConversationNoticeView extends CWBaseLayout {
    private ImageView noticeIv;
    private CWExpandableTextView expandableTv;
    private ImageView expandableIv;

    public CWConversationNoticeView(Context context) {
        super(context);
    }

    public CWConversationNoticeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onViewInit() {
        loadView();
        initWidgits();
    }

    private void loadView() {
        inflate(getContext(), R.layout.cw_chat_view_conversation_notice_view,
                this);
        noticeIv = (ImageView) findViewById(R.id.noticeIv);
        expandableTv = (CWExpandableTextView) findViewById(R.id.expandableTv);
        expandableIv = (ImageView) findViewById(R.id.expandableIv);
    }

    public void initNotice(final TopNoticeProvider.Notice notice){
        noticeIv.setImageResource(notice.getUiModel().getNoticeIconResid());
        expandableTv.setText(notice.getNoticeText());
        expandableTv.setCollapseLines(notice.getUiModel().getCollapseLines());
        expandableTv.setExpandedLines(notice.getUiModel().getExpandLines());
        expandableTv.collapse(false);
        expandableTv.setMovementMethod(ScrollingMovementMethod.getInstance());
        expandableTv.setOnExpandListener(new CWExpandableTextView.OnExpandListener() {
            @Override
            public void onExpand(CWExpandableTextView parent) {
                expand(notice);
            }
        });
        expandableTv.setOnCollapseListener(new CWExpandableTextView.OnCollapseListener() {
            @Override
            public void onCollapse(CWExpandableTextView parent) {
                collapse(notice);
            }
        });
        expandableTv.setOnTapListener(new CWExpandableTextView.OnTapListener() {
            @Override
            public void onTap(CWExpandableTextView view) {
                expandableTv.toggle(notice.getUiModel().isUseAnimation());
            }
        });
        collapse(notice);
        if(notice.isShow()){
            setVisibility(View.VISIBLE);
        }else{
            setVisibility(View.GONE);
        }
    }

    private void initWidgits() {
        initNotice(new TopNoticeProvider.Notice());
    }

    private void expand(TopNoticeProvider.Notice notice){
        expandableIv.setImageResource(notice.getUiModel().getExpandIconResid());
    }

    private void collapse(TopNoticeProvider.Notice notice){
        expandableIv.setImageResource(notice.getUiModel().getCollapseIconResid());
    }

}
