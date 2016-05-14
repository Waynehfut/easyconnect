package com.waynehfut.qrcode;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * Created by Wayne on 2016/5/15.
 * Site:www.waynehfut.com
 * Mail:waynehfut@gmail.com
 */
public class QRCodeGenerator {

    private static QRCodeGenerator sQRCodeGenerator;

    private QRCodeGenerator() {

    }

    public static QRCodeGenerator newInstance() {
        if (sQRCodeGenerator == null) {
            sQRCodeGenerator = new QRCodeGenerator();
        }
        return sQRCodeGenerator;
    }

    public Bitmap GenerateQRCode(String url) throws WriterException {
        if (url == null || url.equals("")) {
            return null;
        }
        BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, 200, 200);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();

        int[] pixels = new int[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (bitMatrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels,0,width,0,0,width,height);
        return bitmap;
    }


}
