package com.zjyeshi.dajiujiao.buyer.task.my;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.zjyeshi.dajiujiao.buyer.App;
import com.zjyeshi.dajiujiao.buyer.dao.DaoFactory;
import com.zjyeshi.dajiujiao.buyer.entity.my.BigArea;
import com.zjyeshi.dajiujiao.buyer.entity.my.CodeArea;
import com.zjyeshi.dajiujiao.buyer.entity.my.DetailArea;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.xuan.bigapple.lib.asynctask.helper.Result;

import org.apache.http.util.EncodingUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 数据过多，放在Task中处理
 * Created by wuhk on 2015/11/22.
 */
public class GetAreaDataTask extends BaseTask<NoResultData> {
    public GetAreaDataTask(Context context) {
        super(context);
    }

    @Override
    protected Result<NoResultData> onHttpRequest(Object... params) {
        Result<NoResultData> result = new Result<NoResultData>();
        String jsonStr = getFromAssets("area.json");
        jsonStr = jsonStr.replace(" ", "");
        Map<String,Map<String, String>> areaMap = (Map<String,Map<String, String>>) JSON.parse(jsonStr);
        Set<String> keySet = areaMap.keySet();
        //遍历key集合，获取value
        List<BigArea> bigList = new ArrayList<BigArea>();
        for(String key : keySet) {
            BigArea bigArea = new BigArea();
            bigArea.setCode(key);
            List<DetailArea> detailAreaList = new ArrayList<DetailArea>();
            for (String k : areaMap.get(key).keySet()){
                DetailArea detailArea = new DetailArea();
                detailArea.setCode(k);
                detailArea.setName(areaMap.get(key).get(k));
                detailAreaList.add(detailArea);
            }
            bigArea.setList(detailAreaList);
            bigList.add(bigArea);
        }
        DaoFactory.getAreaDao().insertBatch(bigList);
        result.setSuccess(true);
        return result;
    }

//    /** 读取Asset中的文件
//     *
//     * @param fileName
//     * @return
//     */
//    public String getFromAssets(String fileName){
//        String result="";
//        try {
//            InputStreamReader inputReader = new InputStreamReader(App.instance.getResources().getAssets().open(fileName) );
//            BufferedReader bufReader = new BufferedReader(inputReader);
//            String line="";
//            while((line = bufReader.readLine()) != null)
//                result += line;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//    }

    public String getFromAssets(String fileName){
        String result = "";
        try {
            InputStream in = App.instance.getResources().getAssets().open(fileName);
            //获取文件的字节数
            int lenght = in.available();
            //创建byte数组
            byte[] buffer = new byte[lenght];
            //将文件中的数据读到byte数组中
            in.read(buffer);
            result = EncodingUtils.getString(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
