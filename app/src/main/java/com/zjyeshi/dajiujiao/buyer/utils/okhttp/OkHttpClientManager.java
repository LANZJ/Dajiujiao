package com.zjyeshi.dajiujiao.buyer.utils.okhttp;

import android.os.Handler;
import android.os.Looper;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;

/**
 * OkHttp使用封装
 * <p/>
 * Created by xuan on 15/10/16.
 */
public class OkHttpClientManager {
    private static OkHttpClientManager mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;

    private DownloadDelegate mDownloadDelegate = new DownloadDelegate();
    private GetDelegate mGetDelegate = new GetDelegate();
    private UploadDelegate mUploadDelegate = new UploadDelegate();
    private PostDelegate mPostDelegate = new PostDelegate();

    private OkHttpClientManager() {
        mOkHttpClient = new OkHttpClient();
        mDelivery = new Handler(Looper.getMainLooper());
    }

    public static OkHttpClientManager getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpClientManager.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpClientManager();
                }
            }
        }
        return mInstance;
    }

    public UploadDelegate getUploadDelegate() {
        return mUploadDelegate;
    }

    public PostDelegate getPostDelegate() {
        return mPostDelegate;
    }

    public GetDelegate getGetDelegate() {
        return mGetDelegate;
    }

    public DownloadDelegate getDownloadDelegate() {
        return mDownloadDelegate;
    }

    /**
     * Get方式请求提交
     */
    public class GetDelegate {
        public Request buildGetRequest(String url, Object tag, Param[] headers) {
            Request.Builder builder = new Request.Builder()
                    .url(url);
            setTag(builder, tag);
            setHeaders(builder, headers);
            return builder.build();
        }

        public Response get(Request request) throws IOException {
            Call call = mOkHttpClient.newCall(request);
            Response execute = call.execute();
            return execute;
        }

        public Response get(String url, Object tag, Param[] headers) throws IOException {
            final Request request = buildGetRequest(url, tag, headers);
            return get(request);
        }

        public Response get(String url) throws IOException {
            return get(url, null, null);
        }

        public String getAsString(String url, Object tag, Param[] headers) throws IOException {
            Response execute = get(url, tag, headers);
            return execute.body().string();
        }

        public String getAsString(String url) throws IOException {
            return getAsString(url, null, null);
        }
    }

    /**
     * 上传模块
     */
    public class UploadDelegate {
        public Request buildMultipartFormRequest(String url, File[] files,
                                                 String[] fileKeys, Param[] headers, Object tag) {
            Request.Builder builder = new Request.Builder().url(url);
            setHeaders(builder, headers);
            setTag(builder, tag);


            MultipartBuilder multipartBuilder = new MultipartBuilder()
                    .type(MultipartBuilder.FORM);
            if (null != files) {
                RequestBody fileBody = null;
                for (int i = 0; i < files.length; i++) {
                    File file = files[i];
                    String fileName = file.getName();
                    fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                    multipartBuilder.addPart(Headers.of("Content-Disposition",
                                    "form-data; name=\"" + fileKeys[i] + "\"; filename=\"" + fileName + "\""),
                            fileBody);
                }
            }
            RequestBody requestBody = multipartBuilder.build();


            return builder.post(requestBody).build();
        }

        public Response post(Request request) throws IOException {
            return mOkHttpClient.newCall(request).execute();
        }

        public Response post(String url, String[] fileKeys, File[] files, Param[] params, Object tag) throws IOException {
            Request request = buildMultipartFormRequest(url, files, fileKeys, params, tag);
            return post(request);
        }

        public Response post(String url, String fileKey, File file) throws IOException {
            return post(url, new String[]{fileKey}, new File[]{file}, null, null);
        }

    }

    /**
     * 下载相关的模块
     */
    public class DownloadDelegate {
        public void downloadAsyn(final String url, final String destFilePathName, final ResultCallback callback, Object tag) {
            final Request request = new Request.Builder()
                    .url(url)
                    .tag(tag)
                    .build();
            final Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(final Request request, final IOException e) {
                    sendFailedStringCallback(request, e, callback);
                }

                @Override
                public void onResponse(Response response) {
                    InputStream is = null;
                    byte[] buf = new byte[2048];
                    int len = 0;
                    FileOutputStream fos = null;
                    try {
                        is = response.body().byteStream();

                        File file = new File(destFilePathName);
                        File fileParent = file.getParentFile();
                        if (!fileParent.exists()) {
                            fileParent.mkdirs();
                        }

                        fos = new FileOutputStream(file);
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                        }
                        fos.flush();
                        //如果下载文件成功，第一个参数为文件的绝对路径
                        sendSuccessResultCallback(file.getAbsolutePath(), callback);
                    } catch (IOException e) {
                        sendFailedStringCallback(response.request(), e, callback);
                    } finally {
                        try {
                            if (is != null) is.close();
                        } catch (IOException e) {
                        }
                        try {
                            if (fos != null) fos.close();
                        } catch (IOException e) {
                        }
                    }
                }
            });
        }

        public void downloadAsyn(final String url, final String destFilePathName, final ResultCallback callback) {
            downloadAsyn(url, destFilePathName, callback, null);
        }
    }

    /**
     * Post请求模块
     */
    public class PostDelegate {
        private final MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream;charset=utf-8");
        private final MediaType MEDIA_TYPE_STRING = MediaType.parse("text/plain;charset=utf-8");

        private Request buildPostFormRequest(String url, Param[] params, Param[] headers, Object tag) {
            Request.Builder builder = new Request.Builder().url(url);
            setHeaders(builder, headers);
            setTag(builder, tag);

            //参数设置
            FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
            for (Param param : params) {
                formEncodingBuilder.add(param.key, param.value);
            }
            RequestBody requestBody = formEncodingBuilder.build();

            return builder.post(requestBody).build();
        }

        private Request buildPostRequest(String url, RequestBody body, Param[] headers, Object tag) {
            Request.Builder builder = new Request.Builder().url(url);
            setTag(builder, tag);
            setHeaders(builder, headers);
            Request request = builder.build();
            return builder.post(body).build();
        }

        /**
         * 同步的Post请求:写入key-value到请求体
         */
        public Response post(String url, Param[] params, Param[] headers, Object tag) throws IOException {
            Request request = buildPostFormRequest(url, params, headers, tag);
            Response response = mOkHttpClient.newCall(request).execute();
            return response;
        }

        public Response post(String url, Param[] params, Param[] headers) throws IOException {
            return post(url, params, headers, null);
        }

        public Response post(String url, Param[] params) throws IOException {
            return post(url, params, null, null);
        }

        public String postAsString(String url, Param[] params, Param[] headers, Object tag) throws IOException {
            Response response = post(url, params, headers, tag);
            return response.body().string();
        }

        public String postAsString(String url, Param[] params, Param[] headers) throws IOException {
            Response response = post(url, params, headers, null);
            return response.body().string();
        }

        public String postAsString(String url, Param[] params) throws IOException {
            return postAsString(url, params, null, null);
        }

        /**
         * 同步的Post请求:直接将bodyStr以写入请求体
         */
        public Response post(String url, String bodyStr, Param[] headers, Object tag) throws IOException {
            RequestBody body = RequestBody.create(MEDIA_TYPE_STRING, bodyStr);
            Request request = buildPostRequest(url, body, headers, tag);
            Response response = mOkHttpClient.newCall(request).execute();
            return response;
        }

        public Response post(String url, String bodyStr, Param[] headers) throws IOException {
            return post(url, bodyStr, headers, null);
        }

        public Response post(String url, String bodyStr) throws IOException {
            return post(url, bodyStr, null, null);
        }

        /**
         * 同步的Post请求:直接将bodyFile以写入请求体
         */
        public Response post(String url, File bodyFile, Param[] headers, Object tag) throws IOException {
            RequestBody body = RequestBody.create(MEDIA_TYPE_STREAM, bodyFile);
            Request request = buildPostRequest(url, body, headers, tag);
            Response response = mOkHttpClient.newCall(request).execute();
            return response;
        }

        public Response post(String url, File bodyFile, Param[] headers) throws IOException {
            return post(url, bodyFile, headers, null);
        }

        public Response post(String url, File bodyFile) throws IOException {
            return post(url, bodyFile, null, null);
        }

        /**
         * 同步的Post请求:直接将字节流写入请求体
         */
        public Response post(String url, byte[] bodyBytes, Param[] headers, Object tag) throws IOException {
            RequestBody body = RequestBody.create(MEDIA_TYPE_STREAM, bodyBytes);
            Request request = buildPostRequest(url, body, headers, tag);
            Response response = mOkHttpClient.newCall(request).execute();
            return response;
        }

        public Response post(String url, byte[] bodyBytes, Param[] headers) throws IOException {
            return post(url, bodyBytes, headers, null);
        }

        public Response post(String url, byte[] bodyBytes) throws IOException {
            return post(url, bodyBytes, null, null);
        }

    }

    /**
     * 参数
     */
    public static class Param {
        private String key;
        private String value;

        public Param() {
        }

        public Param(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    /**
     * 下载回调
     *
     * @param <T>
     */
    public static abstract class ResultCallback<T> {
        public void onBefore(Request request) {
        }

        public void onAfter() {
        }

        public abstract void onError(Request request, Exception e);

        public abstract void onResponse(T response);
    }


    //=========================内部方法======================================
    private void setHeaders(Request.Builder builder, Param[] headers) {
        if (null != headers) {
            for (Param param : headers) {
                builder.addHeader(param.getKey(), param.getValue());
            }
        }
    }

    private void setTag(Request.Builder builder, Object tag) {
        if (null != tag) {
            builder.tag(tag);
        }
    }

    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    private void sendFailedStringCallback(final Request request, final Exception e, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(request, e);
                callback.onAfter();
            }
        });
    }

    private void sendSuccessResultCallback(final Object object, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(object);
                callback.onAfter();
            }
        });
    }

}
