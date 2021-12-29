package com.webapp.a4_order_station_driver.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.models.PhotoObject;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.webapp.a4_order_station_driver.utils.ToolUtil.checkTheInternet;

public class APIImageUtil {

    public static Bitmap getBitmapFromImageView(ImageView imageView) {
        return ((BitmapDrawable) imageView.getDrawable()).getBitmap();
    }

    public static MultipartBody.Part bitmapToMultipartBodyPart(Activity activity, Bitmap bitmap, String name) {
        //create a file to write bitmap data
        /*int iUniqueId = (int) (System.currentTimeMillis() & 0xfffffff);
        File file = new File(activity.getCacheDir(), iUniqueId + ".jpg");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Convert bitmap to byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100 ignored for PNG, bos);
        byte[] bitmapdata = bos.toByteArray();

        write the bytes in file
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
        }*/

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        int iUniqueId = (int) (System.currentTimeMillis() & 0xfffffff);
        File file = new File(activity.getCacheDir(), iUniqueId + ".jpg");

        try {
            FileOutputStream fo = new FileOutputStream(file);
            fo.write(bytes.toByteArray());
            fo.flush();
            fo.close();
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

    @SuppressLint("CheckResult")
    public static void loadImage(Context context, ProgressBar progressBar
            , String url, ImageView imgView) {
        if (checkTheInternet()) {
            progressBar.setVisibility(View.VISIBLE);
            imgView.setClickable(false);
            Glide.with(context)
                    .asBitmap()
                    .load(url)
                    .placeholder(R.drawable.img_user)
                    .listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model
                                , Target<Bitmap> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model
                                , Target<Bitmap> target, DataSource dataSource
                                , boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource
                                , @Nullable Transition<? super Bitmap> transition) {
                            imgView.setImageBitmap(resource);
                            imgView.setClickable(true);
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                            progressBar.setVisibility(View.GONE);
                        }
                    });
        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(context, R.string.no_connection, Toast.LENGTH_SHORT).show();
        }
    }

    public static void uploadImage(BaseActivity baseActivity, DialogView<String> dialogView, Bitmap bitmap) {
        dialogView.showDialog(baseActivity.getString(R.string.upload_image));
        new APIUtil<PhotoObject>(baseActivity).getData(AppController.getInstance().getApi()
                        .uploadImage(APIImageUtil.bitmapToMultipartBodyPart(baseActivity, bitmap, "file"))
                , new com.webapp.a4_order_station_driver.utils.listeners.RequestListener<PhotoObject>() {
                    @Override
                    public void onSuccess(PhotoObject photoObject, String msg) {
                        //ToolUtil.showLongToast(photoObject.toString(), baseActivity);
                        Log.e(getClass().getName() + "fileName:", photoObject.toString());
                        dialogView.hideDialog();
                        dialogView.setData(photoObject.getFile_name());
                    }

                    @Override
                    public void onError(String msg) {
                        ToolUtil.showLongToast(msg, baseActivity);
                        dialogView.hideDialog();
                    }

                    @Override
                    public void onFail(String msg) {
                        ToolUtil.showLongToast(msg, baseActivity);
                        dialogView.hideDialog();
                    }
                });
    }
}
