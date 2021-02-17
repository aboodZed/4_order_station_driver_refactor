package com.webapp.a4_order_station_driver.utils;


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

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Utility class to take photos via a camera intent and do the necessary post-processing
 */
public class PhotoTakerManager {

    public interface PhotoListener {
        void onTakePhotoFailure();

        void onTakePhotoSuccess(Bitmap bitmap);
    }

    private PhotoListener listener;
    private Handler backgroundHandler;
    private @Nullable
    Uri currentPhotoUri;
    private File currentPhotoFile;

    public PhotoTakerManager(PhotoListener listener) {
        this.listener = listener;
        HandlerThread handlerThread = new HandlerThread("Camera Photos Processor");
        handlerThread.start();
        backgroundHandler = new Handler(handlerThread.getLooper());
    }

    @Nullable
    public Intent getPhotoTakingIntent(Context context) {
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

    @Nullable
    public Intent getPhotoGalleryIntent(Context context) {
        Intent galleryPictureIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (galleryPictureIntent.resolveActivity(context.getPackageManager()) == null) {
            return null;
        }
        return galleryPictureIntent;
    }

    public void processGalleryPhoto(final Context context, final Intent data) {
        try {
            Uri uri = data.getData();
            if (uri != null) {
                Bitmap bitmap;
                if (android.os.Build.VERSION.SDK_INT >= 29) {
                    ImageDecoder.Source source = ImageDecoder.createSource(context.getContentResolver(), uri);
                    bitmap = ImageDecoder.decodeBitmap(source);
                } else {
                    bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                }
                listener.onTakePhotoSuccess(resizeImage(bitmap));
            } else {
                listener.onTakePhotoFailure();
            }
        } catch (IOException e) {
            listener.onTakePhotoFailure();
        }
    }

    public void processTakenPhoto(final Context context) {
        backgroundHandler.post(() -> {
            context.revokeUriPermission(
                    currentPhotoUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                Bitmap bitmap = ImageUtil.rotateImageIfRequired(context, currentPhotoFile, currentPhotoUri);
                if (bitmap == null) {
                    listener.onTakePhotoFailure();
                } else {
                    listener.onTakePhotoSuccess(resizeImage(bitmap));
                }
            } catch (IOException exception) {
                listener.onTakePhotoFailure();
            }
        });
    }

    public void deleteLastTakenPhoto() {
        FileUtil.deleteCameraImageWithUri(currentPhotoUri);
    }

    public Uri getCurrentPhotoUri() {
        return currentPhotoUri;
    }

    public static Bitmap resizeImage(Bitmap bitmap) {
        return Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * 0.5), (int)(bitmap.getHeight() * 0.5), true);
    }
}