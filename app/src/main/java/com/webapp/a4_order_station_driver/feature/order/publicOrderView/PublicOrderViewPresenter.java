package com.webapp.a4_order_station_driver.feature.order.publicOrderView;

import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.databinding.FragmentPublicChatBinding;
import com.webapp.a4_order_station_driver.feature.order.adapter.PublicChatAdapter;
import com.webapp.a4_order_station_driver.models.Order;
import com.webapp.a4_order_station_driver.models.PublicChatMessage;
import com.webapp.a4_order_station_driver.models.PublicOrder;
import com.webapp.a4_order_station_driver.models.PublicOrderObject;
import com.webapp.a4_order_station_driver.utils.APIUtil;
import com.webapp.a4_order_station_driver.utils.AppContent;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.NotificationUtil;
import com.webapp.a4_order_station_driver.utils.Photo.PhotoTakerManager;
import com.webapp.a4_order_station_driver.utils.ToolUtil;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;

import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class PublicOrderViewPresenter {

    private BaseActivity baseActivity;
    private DialogView<PublicOrderObject> dialogView;
    private PhotoTakerManager photoTakerManager;
    private StorageReference storageReference;
    private FirebaseStorage storage;
    private DatabaseReference db;
    private PublicOrder publicOrder;
    private FragmentPublicChatBinding binding;

    private int requestCode;

    public PublicOrderViewPresenter(BaseActivity baseActivity, FragmentPublicChatBinding binding
            , DialogView<PublicOrderObject> dialogView, PhotoTakerManager photoTakerManager) {
        this.baseActivity = baseActivity;
        this.binding = binding;
        this.dialogView = dialogView;
        this.photoTakerManager = photoTakerManager;

        db = FirebaseDatabase.getInstance().getReference(AppContent.FIREBASE_PUBLIC_STORE_CHAT_INSTANCE);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

    }

    public void getData(Order order) {
        dialogView.showDialog("");
        new APIUtil<PublicOrderObject>(baseActivity).getData(AppController.getInstance()
                .getApi().getPublicOrder(order.getId()), new RequestListener<PublicOrderObject>() {
            @Override
            public void onSuccess(PublicOrderObject publicOrderObject, String msg) {
                publicOrder = publicOrderObject.getPublicOrder();
                dialogView.setData(publicOrderObject);
                dialogView.hideDialog();
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

    //get message
    public void getMessages(PublicChatAdapter publicChatAdapter, String id) {
        db.child(id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                publicChatAdapter.addItem(dataSnapshot.getValue(PublicChatMessage.class));
                binding.rvChat.scrollToPosition(publicChatAdapter.getItemCount() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //send message
    public void sendMessage(String avatarPath) {
        if (ToolUtil.checkTheInternet()) {
            if (!binding.etMessage.getText().toString().equals("") || !avatarPath.isEmpty()) {
                PublicChatMessage publicStoreMessage = new PublicChatMessage();
                publicStoreMessage.setText(binding.etMessage.getText().toString());
                publicStoreMessage.setSender_name(AppController.getInstance().getAppSettingsPreferences().getUser().getName());
                publicStoreMessage.setImageUrl(avatarPath);
                publicStoreMessage.setSender_id(AppController.getInstance().getAppSettingsPreferences().getUser().getId());
                publicStoreMessage.setSender_avatar_url(AppController.getInstance().getAppSettingsPreferences().getUser().getAvatar_url());
                publicStoreMessage.setTime(System.currentTimeMillis() / 1000);
                String key = db.push().getKey();
                db.child(publicOrder.getId() + "").child(key).setValue(publicStoreMessage);
                NotificationUtil.sendMessageNotification(baseActivity, publicOrder.getInvoice_number()
                        , publicOrder.getId() + "", publicOrder.getClient_id() + ""
                        , AppContent.TYPE_ORDER_PUBLIC);
            }
            ToolUtil.hideSoftKeyboard(baseActivity, binding.etMessage);
            binding.etMessage.setText("");
        } else {
            ToolUtil.showLongToast(baseActivity.getString(R.string.no_connection), baseActivity);
        }
    }

    public void uploadImage(Uri filePath) {
        if (filePath != null) {
            dialogView.showDialog("");
            String avatarPath = "images/" + UUID.randomUUID().toString();
            StorageReference ref = storageReference.child(avatarPath);
            ref.putFile(filePath)
                    .addOnSuccessListener(taskSnapshot -> {
                        dialogView.hideDialog();
                        ref.getDownloadUrl().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                sendMessage(String.valueOf(task.getResult()));
                            } else {
                                ToolUtil.showLongToast(baseActivity.getString(R.string.error), baseActivity);
                            }
                        });
                    })
                    .addOnFailureListener(e -> {
                        dialogView.hideDialog();
                    })
                    .addOnProgressListener(taskSnapshot -> {

                    });
        }
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public void onActivityResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case AppContent.REQUEST_STUDIO:
                    photoTakerManager.processGalleryPhoto(baseActivity, data);
                    break;
                case AppContent.REQUEST_CAMERA:
                    photoTakerManager.processCameraPhoto(baseActivity);
                    break;
            }
        }
    }
}
