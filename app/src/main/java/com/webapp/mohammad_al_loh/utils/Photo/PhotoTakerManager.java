package com.webapp.mohammad_al_loh.utils.Photo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

import com.webapp.mohammad_al_loh.R;
import com.webapp.mohammad_al_loh.utils.APIImageUtil;
import com.webapp.mohammad_al_loh.utils.AppContent;
import com.webapp.mohammad_al_loh.utils.FileUtil;
import com.webapp.mohammad_al_loh.utils.ImageUtil;
import com.webapp.mohammad_al_loh.utils.PermissionUtil;
import com.webapp.mohammad_al_loh.utils.listeners.RequestListener;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PhotoTakerManager {

    private RequestListener<Bitmap> listener;
    private Handler backgroundHandler;
    private @Nullable
    Uri currentPhotoUri;
    private File currentPhotoFile;

    public PhotoTakerManager(RequestListener<Bitmap> listener) {
        this.listener = listener;
        HandlerThread handlerThread = new HandlerThread("Photos Processor");
        handlerThread.start();
        backgroundHandler = new Handler(handlerThread.getLooper());
    }

    @Nullable
    private Intent getPhotoCameraIntent(Context context) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(context.getPackageManager()) == null) {
            return null;
        }
        currentPhotoFile = FileUtil.createImageFile(context);
        if (currentPhotoFile != null) {
            currentPhotoUri = FileProvider.getUriForFile(
                    context,
                    AppContent.FILE_PROVIDER_AUTHORITY,
                    currentPhotoFile);

            // Grant access to content URI so camera app doesn't crash
            List<ResolveInfo> resolvedIntentActivities = context.getPackageManager()
                    .queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);

            for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
                String packageName = resolvedIntentInfo.activityInfo.packageName;
                context.grantUriPermission(packageName, currentPhotoUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                                | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoUri);
            return takePictureIntent;
        } else {
            return null;
        }
    }

    public void processCameraPhoto(Activity activity) {
        backgroundHandler.post(() -> {
            activity.revokeUriPermission(currentPhotoUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                Bitmap bitmap = ImageUtil.rotateImageIfRequired(activity, currentPhotoFile, currentPhotoUri);
                //Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                if (bitmap == null) {
                    listener.onFail(activity.getString(R.string.error_in_process_the_photo));
                } else {
                    activity.runOnUiThread(() ->
                            listener.onSuccess(resizeImage(bitmap), ""));
                }
            } catch (IOException e) {
                listener.onFail(activity.getString(R.string.error_in_input));
            }
        });
    }

    @Nullable
    private Intent getPhotoGalleryIntent(Context context) {
        Intent galleryPictureIntent = new Intent(Intent.ACTION_PICK
                , MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (galleryPictureIntent.resolveActivity(context.getPackageManager()) == null) {
            return null;
        }
        return galleryPictureIntent;
    }

    public void processGalleryPhoto(final Context context, final Intent data) {
        try {
            currentPhotoUri = data.getData();
            if (currentPhotoUri != null) {
                Bitmap bitmap;
                if (android.os.Build.VERSION.SDK_INT >= 29) {
                    ImageDecoder.Source source = ImageDecoder
                            .createSource(context.getContentResolver(), currentPhotoUri);
                    bitmap = ImageDecoder.decodeBitmap(source);
                    listener.onSuccess(resizeImage(bitmap), "");
                } else {
                    ImageView imageView = new ImageView(context);
                    imageView.setImageURI(currentPhotoUri);
                    try {
                        bitmap = APIImageUtil.getBitmapFromImageView(imageView);
                        listener.onSuccess(resizeImage(bitmap), "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                listener.onFail(context.getString(R.string.error_in_process_the_photo));
            }
        } catch (IOException e) {
            listener.onFail(context.getString(R.string.error_in_input));
        }
    }

    public void deleteLastTakenPhoto() {
        FileUtil.deleteCameraImageWithUri(currentPhotoUri);
    }

    public void galleryRequest(Activity activity, int requestId) {
        activity.startActivityForResult(getPhotoGalleryIntent(activity), requestId);
    }

    public void cameraRequest(Activity activity, int requestId) {
        if (!PermissionUtil.isPermissionGranted(Manifest.permission.CAMERA, activity)) {
            PermissionUtil.requestPermission(activity, Manifest.permission.CAMERA
                    , AppContent.REQUEST_PERMISSIONS_R_W_STORAGE_CAMERA);
        } else {
            activity.startActivityForResult(getPhotoCameraIntent(activity), requestId);
        }
    }

    @Nullable
    public Uri getCurrentPhotoUri() {
        return currentPhotoUri;
    }

    private static Bitmap resizeImage(Bitmap bitmap) {
        return Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * 0.5)
                , (int) (bitmap.getHeight() * 0.5), true);
    }

    public void galleryRequestLauncher(Activity activity, ActivityResultLauncher<Intent> launcher) {
        launcher.launch(getPhotoGalleryIntent(activity));
    }

    public void cameraRequestLauncher(FragmentActivity activity, ActivityResultLauncher<Intent> launcher) {
        if (!PermissionUtil.isPermissionGranted(Manifest.permission.CAMERA, activity)) {
            PermissionUtil.requestPermission(activity, Manifest.permission.CAMERA
                    , AppContent.REQUEST_PERMISSIONS_R_W_STORAGE_CAMERA);
        } else {
            launcher.launch(getPhotoCameraIntent(activity));
        }
    }
}