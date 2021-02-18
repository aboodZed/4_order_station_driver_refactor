package com.webapp.a4_order_station_driver.utils.dialogs;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.databinding.FragmentPublicChatBinding;
import com.webapp.a4_order_station_driver.feature.home.OrdersFragment;
import com.webapp.a4_order_station_driver.feature.home.WalletFragment;
import com.webapp.a4_order_station_driver.models.PublicArrays;
import com.webapp.a4_order_station_driver.models.PublicChatMessage;
import com.webapp.a4_order_station_driver.models.PublicOrder;
import com.webapp.a4_order_station_driver.utils.APIUtils;
import com.webapp.a4_order_station_driver.utils.AppContent;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.NotificationUtil;
import com.webapp.a4_order_station_driver.utils.PermissionUtil;
import com.webapp.a4_order_station_driver.utils.PhotoTakerManager;
import com.webapp.a4_order_station_driver.utils.ToolUtils;
import com.webapp.a4_order_station_driver.utils.dialogs.adapter.PublicChatAdapter;
import com.webapp.a4_order_station_driver.utils.formatter.DecimalFormatterManager;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;
import com.webapp.a4_order_station_driver.utils.view.NavigationView;
import com.webapp.a4_order_station_driver.utils.view.Tracking;

import java.util.ArrayList;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static com.webapp.a4_order_station_driver.utils.AppContent.REQUEST_CAMERA;
import static com.webapp.a4_order_station_driver.utils.AppContent.REQUEST_STUDIO;

public class PublicChatFragment extends DialogFragment implements PhotoTakerManager.PhotoListener {

    private FragmentPublicChatBinding binding;

    private FirebaseStorage storage;
    private DatabaseReference db;
    private ArrayList<PublicChatMessage> messageArrayList;
    private PublicOrder publicOrder;
    private PublicChatAdapter publicChatAdapter;
    private ItemSelectImageDialogFragment itemSelectImageDialogFragment = ItemSelectImageDialogFragment.newInstance();
    private Uri filePath;
    private StorageReference storageReference;
    private PhotoTakerManager photoTakerManager;
    private NavigationView navigationView;
    private Tracking tracking;
    public static double s = 0;
    public static boolean isOpenPublicChat = false;

    public PublicChatFragment(NavigationView navigationView, Tracking tracking) {
        this.navigationView = navigationView;
        this.tracking = tracking;
    }

    public static PublicChatFragment newInstance(PublicOrder order
            , NavigationView navigationView, Tracking tracking) {

        PublicChatFragment fragment = new PublicChatFragment(navigationView, tracking);
        Bundle args = new Bundle();
        args.putSerializable(AppContent.INPUT_ORDER, order);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog);
        photoTakerManager = new PhotoTakerManager(this);
    }

    private void click() {
        binding.ivBack.setOnClickListener(view -> {
            navigationView.navigate(3);
            dismiss();
        });

        binding.ivUploadMessage.setOnClickListener(view -> sendMessage(""));
        binding.ivUploadPhoto.setOnClickListener(view -> uploadPhoto());
        binding.ivTracking.setOnClickListener(view -> showLocation());
        binding.ivMore.setOnClickListener(view -> option());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.fragment_public_chat, container, false);
        binding = FragmentPublicChatBinding.inflate(getLayoutInflater());
        if (getArguments() != null) {
            this.publicOrder = (PublicOrder) getArguments().get(AppContent.INPUT_ORDER);
            data();
            click();
        }
        //data();
        db = FirebaseDatabase.getInstance().getReference(AppContent.FIREBASE_PUBLIC_STORE_CHAT_INSTANCE);
        getMessages();
        initRecycleView();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        setCancelable(false);
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        OrdersFragment.page = 1;
        WalletFragment.page = 1;
        isOpenPublicChat = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        isOpenPublicChat = false;
    }

    public void uploadPhoto() {
        itemSelectImageDialogFragment.setListener(new ItemSelectImageDialogFragment.Listener() {
            @Override
            public void onGalleryClicked() {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, REQUEST_STUDIO);
            }

            @Override
            public void onCameraClicked() {
                Intent intent = photoTakerManager.getPhotoTakingIntent(getActivity());
                if (intent != null) {
                    startActivityForResult(intent, REQUEST_CAMERA);
                }
            }
        });
        itemSelectImageDialogFragment.show(getFragmentManager(), "");
    }

    public void showLocation() {
        ShowLocationDialog showLocationDialog = ShowLocationDialog.newInstance(publicOrder);
        showLocationDialog.show(getFragmentManager(), "");
    }

    public void option() {
        WaitDialogFragment.newInstance().show(getFragmentManager(), "");

        new APIUtils<PublicArrays>(getActivity()).getData(AppController.getInstance()
                .getApi().getPublicOrder(publicOrder.getId()), new RequestListener<PublicArrays>() {
            @Override
            public void onSuccess(PublicArrays publicArrays, String msg) {
                publicOrder = publicArrays.getPublicOrder();
                setPrice();
                openBillDialog();
            }

            @Override
            public void onError(String msg) {
                WaitDialogFragment.newInstance().dismiss();
                ToolUtils.showLongToast(msg, getActivity());
            }

            @Override
            public void onFail(String msg) {
                WaitDialogFragment.newInstance().dismiss();
                ToolUtils.showLongToast(msg, getActivity());
            }
        });
    }

    private void openBillDialog() {
        BillDialog billDialog = BillDialog.newInstance(publicOrder);
        billDialog.show(getFragmentManager(), "");
        billDialog.setListener(() -> updateOrderData(billDialog));
    }

    private void updateOrderData(BillDialog billDialog) {
        new APIUtils<PublicArrays>(getActivity()).getData(AppController.getInstance()
                .getApi().getPublicOrder(publicOrder.getId()), new RequestListener<PublicArrays>() {
            @Override
            public void onSuccess(PublicArrays publicArrays, String msg) {
                WaitDialogFragment.newInstance().dismiss();
                publicOrder = publicArrays.getPublicOrder();
                data();
                if (publicOrder.getStatus().equals(AppContent.DELIVERY_STATUS)
                        || publicOrder.getStatus().equals(AppContent.CANCELLED_STATUS)) {
                    if (tracking != null) {
                        tracking.endGPSTracking();
                    }
                }
                billDialog.dismiss();
            }

            @Override
            public void onError(String msg) {
                WaitDialogFragment.newInstance().dismiss();
                ToolUtils.showLongToast(msg, getActivity());
                billDialog.dismiss();
            }

            @Override
            public void onFail(String msg) {
                WaitDialogFragment.newInstance().dismiss();
                ToolUtils.showLongToast(msg, getActivity());
                billDialog.dismiss();
            }
        });
    }

    //functions
    private void data() {
        setPrice();
        String currency = AppController.getInstance().getAppSettingsPreferences().getCountry().getCurrency_code();
        binding.tvOrderDetails.setText(publicOrder.getNote());
        binding.tvDeliveryPrice.setText(DecimalFormatterManager.getFormatterInstance()
                .format(Double.parseDouble(publicOrder.getDelivery_cost())) + " " + currency);
        binding.tvTaxPrice.setText(DecimalFormatterManager.getFormatterInstance()
                .format(Double.parseDouble(publicOrder.getTax())) + " " + currency);
        if (publicOrder.getStatus().equals(AppContent.DELIVERY_STATUS)
                || publicOrder.getStatus().equals(AppContent.CANCELLED_STATUS)) {
            binding.ivMore.setVisibility(View.GONE);
            binding.ivTracking.setVisibility(View.GONE);
            binding.llBottom.setVisibility(View.GONE);
        }
    }

    private void setPrice() {
        if (publicOrder.getPurchase_invoice_value() != null) {
            s = Double.parseDouble(publicOrder.getPurchase_invoice_value());
        } else {
            s = 0;
        }
    }

    private void initRecycleView() {
        messageArrayList = new ArrayList<>();
        publicChatAdapter = new PublicChatAdapter(messageArrayList, getActivity(), getFragmentManager());
        binding.rvChat.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvChat.setItemAnimator(new DefaultItemAnimator());
        binding.rvChat.setAdapter(publicChatAdapter);
        binding.tvOrderId.setText(getString(R.string.order) + "#" + publicOrder.getInvoice_number());

        if (PermissionUtil.isPermissionGranted(MediaStore.ACTION_IMAGE_CAPTURE, getContext()))
            PermissionUtil.requestPermission(getActivity(), Manifest.permission.CAMERA
                    , AppContent.REQUEST_PERMISSIONS_R_W_STORAGE_CAMERA);
    }

    //get message
    private void getMessages() {
        db.child(publicOrder.getId() + "").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                messageArrayList.add(dataSnapshot.getValue(PublicChatMessage.class));
                publicChatAdapter.notifyDataSetChanged();
                binding.rvChat.scrollToPosition(messageArrayList.size() - 1);
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
    private void sendMessage(String avatarPath) {
        if (ToolUtils.checkTheInternet()) {
            if (!binding.etMessage.getText().toString().equals("") || !avatarPath.isEmpty()) {
                PublicChatMessage publicStoreMessage = new PublicChatMessage();
                publicStoreMessage.setText(binding.etMessage.getText().toString());
                publicStoreMessage.setSender_name(AppController.getInstance().getAppSettingsPreferences().getLogin().getUser().getName());
                publicStoreMessage.setImageUrl(avatarPath);
                publicStoreMessage.setSender_id(AppController.getInstance().getAppSettingsPreferences().getLogin().getUser().getId());
                publicStoreMessage.setSender_avatar_url(AppController.getInstance().getAppSettingsPreferences().getLogin().getUser().getAvatar_url());
                publicStoreMessage.setTime(System.currentTimeMillis() / 1000);
                String key = db.push().getKey();
                db.child(publicOrder.getId() + "").child(key).setValue(publicStoreMessage);
                NotificationUtil.sendMessageNotification(publicOrder.getInvoice_number(), publicOrder.getId() + ""
                        , publicOrder.getClient_id() + "", "public");
            }
            ToolUtils.hideSoftKeyboard(getActivity(), binding.etMessage);
            binding.etMessage.setText("");
        } else {
            ToolUtils.showLongToast(getString(R.string.no_connection), getActivity());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_STUDIO) {
                try {
                    //code
                    if (data != null) {
                        filePath = data.getData();
                        uploadImage();
                    }
                    //code
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                    Toast.makeText(getActivity(), R.string.big_image, Toast.LENGTH_SHORT).show();
                    WaitDialogFragment.newInstance().dismiss();
                }
            } else if (requestCode == REQUEST_CAMERA) {
                try {
                    //code
                    filePath = photoTakerManager.getCurrentPhotoUri();
                    photoTakerManager.processTakenPhoto(getActivity());
                    //code
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                    Toast.makeText(getActivity(), R.string.big_image, Toast.LENGTH_SHORT).show();
                    WaitDialogFragment.newInstance().dismiss();
                }
            }
        }
    }

    private void uploadImage() {
        if (filePath != null) {
            WaitDialogFragment.newInstance().show(getFragmentManager(), "");
            String avatarPath = "images/" + UUID.randomUUID().toString();
            StorageReference ref = storageReference.child(avatarPath);
            ref.putFile(filePath)
                    .addOnSuccessListener(taskSnapshot -> {
                        WaitDialogFragment.newInstance().dismiss();
                        ref.getDownloadUrl().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                sendMessage(String.valueOf(task.getResult()));
                            } else {
                                ToolUtils.showLongToast(getString(R.string.error), getActivity());
                            }
                        });
                    })
                    .addOnFailureListener(e -> {
                        WaitDialogFragment.newInstance().dismiss();
                    })
                    .addOnProgressListener(taskSnapshot -> {

                    });
        }
    }

    @Override
    public void onTakePhotoFailure() {

    }

    @Override
    public void onTakePhotoSuccess(Bitmap bitmap) {
        uploadImage();
    }
}
