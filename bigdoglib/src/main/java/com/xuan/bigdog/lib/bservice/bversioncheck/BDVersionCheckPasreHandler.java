package com.xuan.bigdog.lib.bservice.bversioncheck;

import com.xuan.bigdog.lib.update.core.PasreUpdateInfoHandler;
import com.xuan.bigdog.lib.update.entity.UpdateInfo;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 接口返回协议解析
 * <p/>
 * Created by xuan on 16/2/16.
 */
public class BDVersionCheckPasreHandler implements PasreUpdateInfoHandler {
    @Override
    public UpdateInfo toUpdateInfo(InputStream is) throws Exception {
        if (is == null) {
            return null;
        }
        String byteData = new String(readStream(is));
        is.close();
        JSONObject jsonObject = new JSONObject(byteData);
        JSONObject result = jsonObject.getJSONObject("result");

        UpdateInfo updateInfo = new UpdateInfo();
        updateInfo.setUrl(result.getString("url"));
        updateInfo.setLastVersion(result.getInt("lastVersion"));
        updateInfo.setForceUpdate("true".endsWith(result
                .getString("forceUpdate")));
        updateInfo.setDescription(result.getString("description"));

        // updateInfo.setLastVersion(2);
        // updateInfo
        // .setUrl("http://p.gdown.baidu.com/2df9a39a3883bf165ff6e7e1f4ce656efba9699c53e1d732c1250aa4f4faf971d317fb67c1aa6a8ebd637c47ef9cfda939a82690ff4b233c22860bd243bf8959f227bd9fbd765074261ccc6531e3bc7f59680fd969faa8ac0fd611d5ab868d75bc4c982cf7e782fc");
        return updateInfo;
    }

    private static byte[] readStream(InputStream inputStream)
            throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] array = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(array)) != -1) {
            outputStream.write(array, 0, len);
        }
        inputStream.close();
        outputStream.close();
        return outputStream.toByteArray();
    }

}
