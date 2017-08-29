package com.jopool.crow.imkit.listeners;

import com.jopool.crow.R;

/**
 * Created by 巨柏网络 on 2016/5/13.
 */
public interface TopNoticeProvider {

    void getNotice(String toId, ShowNoticeCallback showNoticeCallback);

    class Notice {
        private boolean show = false;
        private String noticeText = "";
        private UIModel uiModel = new UIModel();

        public String getNoticeText() {
            return noticeText;
        }

        public void setNoticeText(String noticeText) {
            this.noticeText = noticeText;
        }

        public boolean isShow() {
            return show;
        }

        public void setShow(boolean show) {
            this.show = show;
        }

        public UIModel getUiModel() {
            return uiModel;
        }

        public void setUiModel(UIModel uiModel) {
            this.uiModel = uiModel;
        }
    }

    class UIModel {
        private int noticeIconResid = R.drawable.cw_chat_conversation_notice_icon_notice;//公告图标
        private int collapseIconResid = R.drawable.cw_chat_conversation_notice_icon_collapse;//收起图标
        private int collapseLines = 1;
        private int expandIconResid = R.drawable.cw_chat_conversation_notice_icon_expand;//展开图标
        private int expandLines = 5;
        private boolean useAnimation = false;

        public int getNoticeIconResid() {
            return noticeIconResid;
        }

        public int getCollapseIconResid() {
            return collapseIconResid;
        }

        public void setCollapseIconResid(int collapseIconResid) {
            this.collapseIconResid = collapseIconResid;
        }

        public int getCollapseLines() {
            return collapseLines;
        }

        public void setCollapseLines(int collapseLines) {
            this.collapseLines = collapseLines;
        }

        public int getExpandIconResid() {
            return expandIconResid;
        }

        public void setExpandIconResid(int expandIconResid) {
            this.expandIconResid = expandIconResid;
        }

        public int getExpandLines() {
            return expandLines;
        }

        public void setExpandLines(int expandLines) {
            this.expandLines = expandLines;
        }

        public void setNoticeIconResid(int noticeIconResid) {
            this.noticeIconResid = noticeIconResid;
        }

        public boolean isUseAnimation() {
            return useAnimation;
        }

        public void setUseAnimation(boolean useAnimation) {
            this.useAnimation = useAnimation;
        }
    }

    interface ShowNoticeCallback {
        void show(Notice notice);
    }

}
