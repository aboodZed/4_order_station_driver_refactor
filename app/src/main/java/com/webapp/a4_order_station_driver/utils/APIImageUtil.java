package com.webapp.a4_order_station_driver.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class APIImageUtil {

    public static Bitmap getBitmapFromImageView(ImageView imageView) {
        return ((BitmapDrawable) imageView.getDrawable()).getBitmap();
    }

    public static MultipartBody.Part bitmapToMultipartBodyPart(Activity activity
            , Bitmap bitmap, String name) {
        //create a file to write bitmap data
        int iUniqueId = (int) (System.currentTimeMillis() & 0xfffffff);
        File file = new File(activity.getCacheDir(), iUniqueId + ".jpg");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Convert bitmap to byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

        //write the bytes in file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //pass it like this
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(name, file.getName(), requestFile);
    }

    public static String bitmapToBase64(Bitmap bitmap) {
        Bitmap bp = getResizedBitmap(bitmap, 400, 400);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        String s = "data:image/jpeg;base64," + Base64.encodeToString(bos.toByteArray(), Base64.NO_WRAP);
        return s;
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);
        // RECREATE THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);
        return resizedBitmap;
    }
}
