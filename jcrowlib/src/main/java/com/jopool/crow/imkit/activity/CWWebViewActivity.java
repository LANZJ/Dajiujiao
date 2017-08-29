package com.jopool.crow.imkit.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jopool.crow.R;
import com.jopool.crow.imkit.view.CWTitleLayout;
import com.jopool.crow.imkit.view.popview.CWWebViewOpLayoutView;
import com.jopool.crow.imlib.utils.CWClipboardUtil;
import com.jopool.crow.imlib.utils.CWToastUtil;

/**
 * 查看网页容器页面
 *
 * @author xuan
 */
public class CWWebViewActivity extends CWBaseActivity {
    public static final String PARAM_MODE_URL = "param.mode.url";
    public static final String PARAM_MODE = "param.mode";

    public static final String PARAM_VISIT = "param.visit";
    public static final String PARAM_DATA = "param.data";

    protected String mode;
    protected String visit;
    protected WebView webView;
    protected CWTitleLayout titleLayout;// 标题栏
    protected CWWebViewOpLayoutView webViewOpItemView;//操作选项

    //子类可能会用到
    protected String textTitle;
    private static Class<?> openWebviewClass = CWWebViewActivity.class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cw_chat_layout_webview);
        mode = getIntent().getStringExtra(PARAM_MODE);
        visit = getIntent().getStringExtra(PARAM_VISIT);
        loadView();
        initWidgets();
    }

    private void loadView() {
        //标题栏目
        titleLayout = (CWTitleLayout) findViewById(R.id.titleLayout);
        //WebView
        webView = (WebView) findViewById(R.id.webView);
        //
        webViewOpItemView = new CWWebViewOpLayoutView(this);
    }

    private void initWidgets() {
        //标题栏初始化
        titleLayout.configReturn("返回", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    CWWebViewActivity.this.finish();
                }
            }
        }).configRightIcon(R.drawable.cw_widgets_title_right_moreicon, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webViewOpItemView.toggle();
            }
        }).configLeftText("关闭", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CWWebViewActivity.this.finish();
            }
        });
        //
        /** 滚动条在页面之上,这样不会留空白边 */
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        //
        WebSettings webSettings = webView.getSettings();
        /** 支持缩放 */
        webSettings.setSupportZoom(true);
        /** 隐藏缩放按钮 */
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUseWideViewPort(true);
        /** 支持JS执行 */
        webSettings.setJavaScriptEnabled(true);
        //
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }
        });
        //
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                textTitle = title;
                titleLayout.configTitle2(title);
            }
        });
        //
        if (PARAM_MODE_URL.equals(mode)) {
            webView.loadUrl(visit);
        } else {
            CWToastUtil.displayTextShort("不支持访问");
        }
        //
        webViewOpItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webViewOpItemView.toggle();
            }
        });
        webViewOpItemView.refreshData(new DefaultOpItemAdapter1(), new DefaultOpItemAdapter2());
    }

    public static void configOpenWebviewClass(Class<?> openWebviewClass) {
        CWWebViewActivity.openWebviewClass = openWebviewClass;
    }

    /**
     * 以url方式访问
     *
     * @param context
     * @param url
     */
    public static void startForUrl(Context context, String url, String data) {
        Intent intent = new Intent();
        intent.setClass(context, openWebviewClass);
        intent.putExtra(PARAM_MODE, PARAM_MODE_URL);
        intent.putExtra(PARAM_VISIT, url);
        intent.putExtra(PARAM_DATA, data);
        context.startActivity(intent);
    }

    /**
     * 第二行数据
     *
     * @return
     */
    protected CWWebViewOpLayoutView.OpItem[] getOpItemListForLine1() {
//		return new CWWebViewOpLayoutView.OpItem[]{
//				obtainSendToUser(),
//				obtainSendToCircle(),
//				obtainOpenBrowser(),
//				obtainCollection()
//		};
        return new CWWebViewOpLayoutView.OpItem[]{
                obtainOpenBrowser()
        };
    }

    /**
     * 第一行数据
     *
     * @return
     */
    protected CWWebViewOpLayoutView.OpItem[] getOpItemListForLine2() {
        return new CWWebViewOpLayoutView.OpItem[]{
                obtainCopyUrl(),
                obtainRefresh()
        };
    }

    public CWWebViewOpLayoutView.OpItem obtainSendToUser() {
        final CWWebViewOpLayoutView.OpItem opItem = new CWWebViewOpLayoutView.OpItem();
        opItem.setIcon(R.drawable.cw_webview_opitem_ic_sendtouser);
        opItem.setText("发送给朋友");
        opItem.setL(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        return opItem;
    }

    public CWWebViewOpLayoutView.OpItem obtainSendToCircle() {
        final CWWebViewOpLayoutView.OpItem opItem = new CWWebViewOpLayoutView.OpItem();
        opItem.setIcon(R.drawable.cw_webview_opitem_ic_sendtocircle);
        opItem.setText("分享到朋友圈");
        opItem.setL(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        return opItem;
    }

    public CWWebViewOpLayoutView.OpItem obtainCollection() {
        final CWWebViewOpLayoutView.OpItem opItem = new CWWebViewOpLayoutView.OpItem();
        opItem.setIcon(R.drawable.cw_webview_opitem_ic_collection);
        opItem.setText("收藏");
        opItem.setL(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        return opItem;
    }

    public CWWebViewOpLayoutView.OpItem obtainOpenBrowser() {
        final CWWebViewOpLayoutView.OpItem opItem = new CWWebViewOpLayoutView.OpItem();
        opItem.setIcon(R.drawable.cw_webview_opitem_ic_openbrowser);
        opItem.setText("在浏览器打开");
        opItem.setL(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(webView.getUrl());
                intent.setData(content_url);
                startActivity(intent);
            }
        });
        return opItem;
    }

    public CWWebViewOpLayoutView.OpItem obtainCopyUrl() {
        final CWWebViewOpLayoutView.OpItem opItem = new CWWebViewOpLayoutView.OpItem();
        opItem.setIcon(R.drawable.cw_webview_opitem_ic_copyurl);
        opItem.setText("复制链接");
        opItem.setL(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CWClipboardUtil.copyText(webView.getUrl());
                CWToastUtil.displayTextShort("已复制");
            }
        });
        return opItem;
    }

    public CWWebViewOpLayoutView.OpItem obtainRefresh() {
        final CWWebViewOpLayoutView.OpItem opItem = new CWWebViewOpLayoutView.OpItem();
        opItem.setIcon(R.drawable.cw_webview_opitem_ic_refresh);
        opItem.setText("刷新");
        opItem.setL(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl(webView.getUrl());
            }
        });
        return opItem;
    }

    private class DefaultOpItemAdapter1 implements CWWebViewOpLayoutView.OpItemAdapter {
        @Override
        public CWWebViewOpLayoutView.OpItem getItem(final int position, final View view) {
            final CWWebViewOpLayoutView.OpItem item = getOpItemListForLine1()[position];
            final View.OnClickListener regionL = item.getL();
            item.setL(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    item.setTag(position);
                    item.getData().put(CWWebViewOpLayoutView.OpItem.DATA_KEY_TITLE, textTitle);
                    item.getData().put(CWWebViewOpLayoutView.OpItem.DATA_KEY_CURRENTURL, webView.getUrl());
                    item.getData().put(CWWebViewOpLayoutView.OpItem.DATA_KEY_OWNERUSERID, getIntent().getStringExtra(PARAM_DATA));
                    regionL.onClick(view);
                }
            });
            return item;
        }

        @Override
        public int getItemCount() {
            return getOpItemListForLine1().length;
        }
    }

    private class DefaultOpItemAdapter2 implements CWWebViewOpLayoutView.OpItemAdapter {
        @Override
        public CWWebViewOpLayoutView.OpItem getItem(final int position, final View view) {
            final CWWebViewOpLayoutView.OpItem item = getOpItemListForLine2()[position];
            final View.OnClickListener regionL = item.getL();
            item.setL(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    item.setTag(position);
                    item.getData().put(CWWebViewOpLayoutView.OpItem.DATA_KEY_TITLE, textTitle);
                    item.getData().put(CWWebViewOpLayoutView.OpItem.DATA_KEY_CURRENTURL, webView.getUrl());
                    item.getData().put(CWWebViewOpLayoutView.OpItem.DATA_KEY_OWNERUSERID, getIntent().getStringExtra(PARAM_DATA));
                    regionL.onClick(view);
                }
            });
            return item;
        }

        @Override
        public int getItemCount() {
            return getOpItemListForLine2().length;
        }
    }

}
