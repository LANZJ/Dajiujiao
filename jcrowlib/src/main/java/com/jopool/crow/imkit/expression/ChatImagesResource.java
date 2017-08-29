package com.jopool.crow.imkit.expression;

import com.jopool.crow.R;
import com.jopool.crow.imkit.adapter.ToolsImageAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 图片资源
 *
 * @author xuan
 */
public class ChatImagesResource {
    private static final List<ToolsImageAdapter.ToolItem> toolImageList = new ArrayList<ToolsImageAdapter.ToolItem>();// 照片+拍照等

    private static final List<Expression> expressionList = new ArrayList<Expression>();// 表情
    private static final Map<String, Expression> expressionMap = new HashMap<String, Expression>();

    private static final int[] volumes = new int[8];

    static {
        // 照片+拍照+表情等
        ToolsImageAdapter.ToolItem toolItem = new ToolsImageAdapter.ToolItem();
        toolItem.setIconResid(R.drawable.cw_chat_conversation_bottom_bar_tool_photo);
        toolItem.setNameResid(R.string.cw_conversation_tool_photo);
        toolImageList.add(toolItem);
        toolItem = new ToolsImageAdapter.ToolItem();
        toolItem.setIconResid(R.drawable.cw_chat_conversation_bottom_bar_tool_camera);
        toolItem.setNameResid(R.string.cw_conversation_tool_camera);
        toolImageList.add(toolItem);
        toolItem = new ToolsImageAdapter.ToolItem();
        toolItem.setIconResid(R.drawable.cw_chat_conversation_bottom_bar_tool_expression);
        toolItem.setNameResid(R.string.cw_conversation_tool_expression);
        toolImageList.add(toolItem);

        expressionMap.put("[呲牙]",
                new Expression("[呲牙]", R.drawable.cw_chat_bq0));
        expressionMap.put("[微笑]",
                new Expression("[微笑]", R.drawable.cw_chat_bq1));
        expressionMap.put("[流汗]",
                new Expression("[流汗]", R.drawable.cw_chat_bq2));
        expressionMap.put("[偷笑]",
                new Expression("[偷笑]", R.drawable.cw_chat_bq3));
        expressionMap.put("[再见]",
                new Expression("[再见]", R.drawable.cw_chat_bq4));
        expressionMap.put("[可怜]",
                new Expression("[可怜]", R.drawable.cw_chat_bq5));
        expressionMap.put("[酷]", new Expression("[酷]", R.drawable.cw_chat_bq7));
        expressionMap.put("[抓狂]",
                new Expression("[抓狂]", R.drawable.cw_chat_bq8));
        expressionMap.put("[委屈]",
                new Expression("[委屈]", R.drawable.cw_chat_bq9));
        expressionMap.put("[得意]", new Expression("[得意]",
                R.drawable.cw_chat_bq10));
        expressionMap
                .put("[吐]", new Expression("[吐]", R.drawable.cw_chat_bq11));
        expressionMap.put("[猪头]", new Expression("[猪头]",
                R.drawable.cw_chat_bq13));
        expressionMap.put("[玫瑰]", new Expression("[玫瑰]",
                R.drawable.cw_chat_bq14));
        expressionMap.put("[便便]", new Expression("[便便]",
                R.drawable.cw_chat_bq15));
        expressionMap.put("[地雷]", new Expression("[地雷]",
                R.drawable.cw_chat_bq16));
        expressionMap.put("[菜刀]", new Expression("[菜刀]",
                R.drawable.cw_chat_bq17));
        expressionMap.put("[爱心]", new Expression("[爱心]",
                R.drawable.cw_chat_bq18));
        expressionMap
                .put("[强]", new Expression("[强]", R.drawable.cw_chat_bq19));
        expressionMap
                .put("[弱]", new Expression("[弱]", R.drawable.cw_chat_bq20));
        expressionMap.put("[握手]", new Expression("[握手]",
                R.drawable.cw_chat_bq21));
        expressionMap.put("[胜利]", new Expression("[胜利]",
                R.drawable.cw_chat_bq22));
        expressionMap.put("[抱拳]", new Expression("[抱拳]",
                R.drawable.cw_chat_bq23));
        expressionMap.put("[凋谢]", new Expression("[凋谢]",
                R.drawable.cw_chat_bq24));
        expressionMap.put("[瓢虫]", new Expression("[瓢虫]",
                R.drawable.cw_chat_bq25));
        expressionMap.put("[勾引]", new Expression("[勾引]",
                R.drawable.cw_chat_bq26));
        expressionMap.put("[OK]", new Expression("[OK]",
                R.drawable.cw_chat_bq27));
        expressionMap.put("[爱你]", new Expression("[爱你]",
                R.drawable.cw_chat_bq28));
        expressionMap.put("[咖啡]", new Expression("[咖啡]",
                R.drawable.cw_chat_bq29));

        // 表情
        expressionList.addAll(expressionMap.values());

        // 音量
        volumes[0] = R.drawable.cw_chat_volume_1;
        volumes[1] = R.drawable.cw_chat_volume_2;
        volumes[2] = R.drawable.cw_chat_volume_3;
        volumes[3] = R.drawable.cw_chat_volume_4;
        volumes[4] = R.drawable.cw_chat_volume_5;
        volumes[5] = R.drawable.cw_chat_volume_6;
        volumes[6] = R.drawable.cw_chat_volume_7;
        volumes[7] = R.drawable.cw_chat_volume_8;
    }

    public static List<ToolsImageAdapter.ToolItem> getToolimagelist() {
        return toolImageList;
    }

    public static List<Expression> getExpressionlist() {
        return expressionList;
    }

    public static Map<String, Expression> getExpressionmap() {
        return expressionMap;
    }

    public static int[] getVolumes() {
        return volumes;
    }

}
