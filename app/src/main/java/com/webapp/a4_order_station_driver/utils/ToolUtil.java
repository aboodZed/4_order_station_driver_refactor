package com.webapp.a4_order_station_driver.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import com.webapp.a4_order_station_driver.models.Message;
import com.webapp.a4_order_station_driver.models.ResetCode;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ToolUtil {
    public static boolean checkTheInternet() {
        ConnectivityManager cm = (ConnectivityManager) AppController.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static void hideSoftKeyboard(Activity activity, EditText editText) {
        if (editText == null)
            return;

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static void showLongToast(String s, Activity activity) {
        Toast.makeText(activity, s, Toast.LENGTH_LONG).show();
    }


    public static String showError(Context context, ResponseBody s) {
        String message = "";
        try {
            JSONObject jObjError = new JSONObject(s.string());
            message = jObjError.getString(AppContent.FIREBASE_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(message) && context != null) {
            message = context.getString(R.string.error);
        }
        return message;
    }

    /*  @SuppressLint("CheckResult")
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
                              return false;
                          }

                          @Override
                          public boolean onResourceReady(Bitmap resource, Object model
                                  , Target<Bitmap> target, DataSource dataSource
                                  , boolean isFirstResource) {
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
              Toast.makeText(contt, R.string.no_connection, Toast.LENGTH_SHORT).show();
          }
      }

      /*public static MultipartBody.Part bitmapToMultipartBodyPart(Activity activity
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
          bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG* bos);
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

      public static RequestBody createBody(HashMap<String, String> hashMap) {
          FormBody.Builder bodyBuilder = new FormBody.Builder();
          Iterator it = hashMap.entrySet().iterator();
          while (it.hasNext()) {
              Map.Entry pair = (Map.Entry) it.next();
              bodyBuilder.add((String) pair.getKey(), (String) pair.getValue());
              it.remove(); // avoids a ConcurrentModificationException
          }
          return bodyBuilder.build();
      }

    public static void notificationBuilder(Activity activity, ResetCode resetCode) {
        Notification.Builder builder = new Notification.Builder(activity);
        builder.setContentTitle(AppContent.APP_NAME)
                .setStyle(new Notification.BigTextStyle().bigText("Your Reset Password Code is: " + resetCode.getCode()))
                .setAutoCancel(true)
                .setContentText(resetCode.getMassage() + "")
                .setSmallIcon(R.drawable.logo);
        final NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(5);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                String channelID = "orderChannel"; // In case you don't add a channel android oreo will not create the notification and it will give you a log error
                NotificationChannel channel = new NotificationChannel(channelID, AppContent.APP_NAME,
                        NotificationManager.IMPORTANCE_DEFAULT);
                channel.setLightColor(Color.GREEN);
                notificationManager.createNotificationChannel(channel);
                builder.setChannelId(channelID);
            } catch (Exception e) {
            }
        }
        Notification notification = builder.build();
        notification.defaults = Notification.FLAG_ONLY_ALERT_ONCE;
        notification.defaults = Notification.FLAG_INSISTENT | Notification.FLAG_AUTO_CANCEL;
        //notification.defaults = Notification.FLAG_AUTO_CANCEL;
        int iUniqueId = (int) (System.currentTimeMillis() & 0xfffffff);
        notificationManager.notify(iUniqueId, notification);
    }*/

    public static String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        return date;
    }

    public static String getTime(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("hh:mm", cal).toString();
        return date;
    }
}
