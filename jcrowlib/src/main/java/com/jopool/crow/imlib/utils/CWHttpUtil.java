package com.jopool.crow.imlib.utils;

import android.util.Log;

import com.jopool.crow.CWChat;
import com.jopool.crow.imlib.utils.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 网络访问请求
 *
 * Created by xuan on 16/2/24.
 */
public class CWHttpUtil {
    private static final String TAG = "CWHttpUtil";

    /**
     * 上传文件
     *
     * @param urlStr
     * @param paramMap
     * @param fileMap
     * @return
     */
    public static CWResponse uploadFile(String urlStr, Map<String, String> paramMap, Map<String, File> fileMap){
        try{
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(60 * 1000);
            conn.setReadTimeout(60 * 1000);

            MultipartEntity bodyEntity = new MultipartEntity();
            bodyEntity.writeDataToBody(conn, paramMap, fileMap);

            return readToString(conn);
        }catch (Exception e){
            Log.e(TAG, e.getMessage(), e);
            CWResponse response = new CWResponse();
            response.setStatusCode(-1);
            response.setReasonPhrase(e.getMessage());
            return response;
        }
    }

    /**
     * 下载文件
     *
     * @param urlStr
     * @param saveFilePath
     * @return
     */
    public static boolean dowloadFile(String urlStr, String saveFilePath){
        boolean isSuccess = false;
        try{
            URL url = new URL(urlStr);

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(60 * 1000);
            conn.setReadTimeout(60 * 1000);

            isSuccess = readToFile(conn, saveFilePath);
        }catch (Exception e){
            Log.e(TAG, e.getMessage(), e);
        }

        return isSuccess;
    }

    /**
     * POST请求
     *
     * @param urlStr
     * @param paramMap
     * @return
     */
    public static CWResponse post(String urlStr, Map<String,String> paramMap){
        //设置通用参数
        if(null == paramMap){
            paramMap = new HashMap<String, String>();
        }
        paramMap.put("appId", CWChat.getInstance().getConfig().getAppId());
        //
        try{
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(30 * 1000);
            conn.setReadTimeout(30 * 1000);
            conn.setDoOutput(true);

            //设置POST参数
            StringBuilder sb = new StringBuilder();
            if(null != paramMap && paramMap.size() > 0){
                int i = 0;
                for(Map.Entry<String, String> entry : paramMap.entrySet()){
                    if(i > 0){
                        sb.append("&");
                    }
                    sb.append(entry.getKey());
                    sb.append("=");
                    sb.append(entry.getValue());
                    i++;
                }
            }
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            //dos.writeBytes(sb.toString());
            dos.write(sb.toString().getBytes());
            dos.flush();
            dos.close();

            //请求获取返回值
            return readToString(conn);
        }catch (Exception e){
            Log.e(TAG, e.getMessage(), e);
            CWResponse response = new CWResponse();
            response.setStatusCode(-1);
            response.setReasonPhrase(e.getMessage());
            return response;
        }
    }

    /**
     * 读取保存到文件
     *
     * @param conn
     * @param saveFile
     * @return
     */
    private static boolean readToFile(HttpURLConnection conn, String saveFile){
        boolean isSuccess = false;

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        InputStream inStream = null;
        try{
            inStream = conn.getInputStream();

            byte[] buffer = new byte[1024];
            int len = 0;
            while( (len = inStream.read(buffer)) !=-1 ){
                outStream.write(buffer, 0, len);
            }

            byte[] data = outStream.toByteArray();
            File file = new File(saveFile);
            FileUtils.writeByteArrayToFile(file, data, false);
            isSuccess = true;
        }catch (Exception e){
            Log.e(TAG, e.getMessage(), e);
        }finally {
            try{
                outStream.close();
                inStream.close();
            }catch (Exception e){
                //Ignore
            }
        }

        return isSuccess;
    }

    /**
     * 读取到串
     *
     * @param conn
     * @return
     */
    private static CWResponse readToString(HttpURLConnection conn) {
        CWResponse response = new CWResponse();

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        InputStream inStream = null;
        try{
            inStream = conn.getInputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while( (len = inStream.read(buffer)) !=-1 ){
                outStream.write(buffer, 0, len);
            }
            byte[] data = outStream.toByteArray();
            response.setStatusCode(conn.getResponseCode());
            response.setReasonPhrase(conn.getResponseMessage());
            response.setResultStr(new String(data, "UTF-8"));
        }catch (Exception e){
            Log.e(TAG, e.getMessage(), e);
            response.setStatusCode(-1);
            response.setReasonPhrase(e.getMessage());
        }finally {
            try{
                outStream.close();
                inStream.close();
            }catch (Exception e){
                //Ignore
            }
        }

        return response;
    }

    /**
     * 文件上传请求实体
     *
     * Created by xuan on 16/2/25.
     */
    private static class MultipartEntity {

        /**
         * Standard CLRF line ending
         */
        private static final char[] CLRF = new char[] { '\r', '\n' };

        /**
         * Double CLRF line ending
         */
        private static final char[] DOUBLE_CLRF = new char[] { '\r', '\n', '\r', '\n' };

        /**
         * Boundary start token
         */
        private static final String BOUNDARY_START = "---------------------------HttpAPIFormBoundary";

        /**
         * 把数据写到请求体中去
         *
         * @param connection
         * @param paramMap
         * @param fileMap
         * @throws Exception
         */
        public void writeDataToBody(HttpURLConnection connection, Map<String, String> paramMap, Map<String, File> fileMap) throws Exception {
            String boundary = BOUNDARY_START + new Random().nextLong();

            // Open the connection and set the correct header
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            connection.setRequestMethod("POST");// POST模式
            connection.setUseCaches(false);

            boundary = "--" + boundary;

            OutputStream os = connection.getOutputStream();

            Writer writer = null;
            try {
                writer = new OutputStreamWriter(os);
                for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                    // Write the start of our request
                    writer.write(boundary);
                    writer.write(CLRF);
                    writer.write("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"");
                    // Write a newline before the content
                    writer.write(DOUBLE_CLRF);
                    // Write the data
                    writer.write(entry.getValue());

                    writer.write(CLRF);
                }
                for (Map.Entry<String, File> entry : fileMap.entrySet()) {
                    // Write the start of our request
                    writer.write(boundary);
                    writer.write(CLRF);
                    writer.write("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"");


                    writer.write("; filename=\"" + entry.getValue().getName() + "\"");
                    writer.write(CLRF);

                    // Get the mime type
                    String type = URLConnection.guessContentTypeFromName(entry.getValue().getName());
                    if (type == null) {
                        type = "application/octet-stream";
                    }

                    // Write the mime type
                    writer.write("Content-Type: ");
                    writer.write(type);
                    writer.write(DOUBLE_CLRF);

                    writer.flush();

                    InputStream input = null;
                    try {
                        input = new FileInputStream(entry.getValue());
                        byte[] buffer = new byte[1024];
                        while (true) {
                            int read = input.read(buffer, 0, buffer.length);
                            if (read == -1) {
                                break;
                            }
                            os.write(buffer, 0, read);
                        }
                        os.flush();
                    }catch (Exception e){
                        throw new Exception(e);
                    }finally {
                        try {
                            input.close();
                        }catch (Exception e){
                            //Ignore
                        }
                    }

                    writer.write(CLRF);
                }

                // Set the final boundary
                boundary = boundary + "--";
                // Write a boundary to let the server know the previous content area is finished
                writer.write(boundary);
                // Write a final newline
                writer.write(CLRF);
            }catch (Exception e){
                throw new Exception(e);
            }finally {
                try {
                    writer.close();
                }catch (Exception e){
                    //Ignore
                }
            }
        }
    }
    public static class CWResponse {
        /** 返回状态码；成功200 */
        private int statusCode;
        /** 返回状态短语 */
        private String reasonPhrase;
        /** 返回结果：当返回是字符串时才有 */
        private String resultStr;
        /** 返回结果：当返回是文件时才有 */
        private File resultFile;

        public CWResponse() {
            this(-1, null);
        }

        public CWResponse(int statusCode) {
            this(statusCode, null);
        }

        public CWResponse(int statusCode, String reasonPhrase) {
            this.statusCode = statusCode;
            this.reasonPhrase = reasonPhrase;
        }

        /**
         * 判断网络是否返回正常，正常码＝200
         *
         * @return
         */
        public boolean isStatusOk() {
            return 200 == statusCode;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        public String getReasonPhrase() {
            return reasonPhrase;
        }

        public void setReasonPhrase(String reasonPhrase) {
            this.reasonPhrase = reasonPhrase;
        }

        public String getResultStr() {
            return resultStr;
        }

        public void setResultStr(String resultStr) {
            this.resultStr = resultStr;
        }

        public File getResultFile() {
            return resultFile;
        }

        public void setResultFile(File resultFile) {
            this.resultFile = resultFile;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("statusCode[" + statusCode + "]");
            sb.append("reasonPhrase[" + reasonPhrase + "]");
            sb.append("resultStr[" + resultStr + "]");
            return sb.toString();
        }

    }

}
