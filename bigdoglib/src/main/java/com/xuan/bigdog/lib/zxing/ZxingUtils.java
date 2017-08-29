package com.xuan.bigdog.lib.zxing;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.xuan.bigdog.lib.zxing.camera.PlanarYUVLuminanceSource;
import com.xuan.bigdog.lib.zxing.decoding.DecodeFormatManager;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Hashtable;
import java.util.Vector;

/**
 * 基于Zxing二维码操作工具类
 *
 * Created by xuan on 15/9/23.
 */
public class ZxingUtils {
    private static final String TAG = "Bigdog.ZxingUtils";

    // ////////////////////////////////////////////生成二维码////////////////////////////////////////////////////////
    /**
     * 生成二维码图片
     *
     * @param content
     * @param config
     * @return
     */
    public static Bitmap encodeToBitmap(String content, ZxConfig config) {
        if (TextUtils.isEmpty(content)) {
            Log.e(TAG, "Content can not be empty");
            return null;
        }

        if (null == config) {
            config = new ZxConfig();
        }

        String encoding = config.getEncoding();
        if (TextUtils.isEmpty(encoding)) {
            encoding = getAppropriateEncoding(content);
        }

        Hashtable<EncodeHintType, String> hints = null;
        if (null != encoding) {
            hints = new Hashtable<EncodeHintType, String>(2);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }

        MultiFormatWriter writer = new MultiFormatWriter();

        BitMatrix result = null;
        try{
            result = writer.encode(content, BarcodeFormat.QR_CODE, config.getBitmapWidth(),
                    config.getBitmapHeight(), hints);
        }catch (WriterException we){
            Log.e(TAG, we.getMessage(), we);
            return null;
        }

        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];

        // All are 0, or black, by default
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? config.getColor() : config.getBgColor();
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

        if (!TextUtils.isEmpty(config.getSaveFileName())) {
            File file = new File(config.getSaveFileName());
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }

            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 70, out);
            }
            catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
                return null;
            }
        }

        return bitmap;
    }

    /**
     * 生成二维码，并保存二维码到本地地址
     *
     * @param content
     * @param saveFileName
     * @return
     */
    public static Bitmap encodeToBitmap(String content, String saveFileName) {
        return encodeToBitmap(content, new ZxConfig(saveFileName));
    }

    // 含有中文类的字符就使用UTF-8编码
    private static String getAppropriateEncoding(CharSequence contents) {
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }

    // ////////////////////////////////////////////读取识别二维码////////////////////////////////////////////////////////
    /**
     * 解码二维码
     *
     * @param bitmap
     * @return
     */
    public static String decodeFromBitmap(Bitmap bitmap) {
        MultiFormatReader multiFormatReader = new MultiFormatReader();

        // 解码的参数
        Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>(2);

        // 可以解析的编码类型，这里设置可扫描的类型，我这里选择了都支持
        Vector<BarcodeFormat> decodeFormats = new Vector<BarcodeFormat>();
        decodeFormats.addAll(DecodeFormatManager.PRODUCT_FORMATS);
        decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
        decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
        hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);

        // 设置继续的字符编码格式为UTF8
        // hints.put(DecodeHintType.CHARACTER_SET, "UTF8");

        // 设置解析配置参数
        multiFormatReader.setHints(hints);

        // 开始对图像资源解码
        Result rawResult = null;
        try {
            rawResult = multiFormatReader.decodeWithState(new BinaryBitmap(new HybridBinarizer(
                    new PlanarYUVLuminanceSource(bitmap))));
        }
        catch (NotFoundException e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }

        return rawResult.getText();
    }

}
