package com.webapp.a4_order_station_driver.feature.order.publicOrderView;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.databinding.FragmentPublicChatBinding;
import com.webapp.a4_order_station_driver.feature.main.orders.OrdersFragment;
import com.webapp.a4_order_station_driver.feature.main.orders.publicO.OrderPublicFragment;
import com.webapp.a4_order_station_driver.feature.main.wallets.publicO.PublicWalletFragment;
import com.webapp.a4_order_station_driver.feature.main.wallets.WalletFragment;
import com.webapp.a4_order_station_driver.feature.order.adapter.PublicChatAdapter;
import com.webapp.a4_order_station_driver.models.Order;
import com.webapp.a4_order_station_driver.models.PublicOrder;
import com.webapp.a4_order_station_driver.models.PublicOrderObject;
import com.webapp.a4_order_station_driver.utils.AppContent;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.PermissionUtil;
import com.webapp.a4_order_station_driver.utils.Photo.PhotoTakerManager;
import com.webapp.a4_order_station_driver.utils.ToolUtil;
import com.webapp.a4_order_station_driver.utils.dialogs.AddBillDialog;
import com.webapp.a4_order_station_driver.utils.dialogs.BillDialog;
import com.webapp.a4_order_station_driver.utils.dialogs.ItemSelectImageDialogFragment;
import com.webapp.a4_order_station_driver.utils.dialogs.ShowLocationDialog;
import com.webapp.a4_order_station_driver.utils.dialogs.WaitDialogFragment;
import com.webapp.a4_order_station_driver.utils.formatter.DecimalFormatterManager;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;
import com.webapp.a4_order_station_driver.utils.location.tracking.OrderGPSTracking;

import org.jetbrains.annotations.NotNull;

public class PublicOrderViewFragment extends Fragment implements
        RequestListener<Bitmap>, DialogView<PublicOrderObject> {

    public final static int page = 504;

    private FragmentPublicChatBinding binding;

    private PublicOrder publicOrder;
    private PublicChatAdapter publicChatAdapter;
    private ItemSelectImageDialogFragment itemSelectImageDialogFragment
            = ItemSelectImageDialogFragment.newInstance();
    private PhotoTakerManager photoTakerManager;
    private PublicOrderViewPresenter presenter;

    private BillDialog billDialog;

    private BaseActivity baseActivity;
    public static double billPrice = 0;
    public static boolean isOpenPublicChat = false;
    private boolean openBillDialog;

    public PublicOrderViewFragment(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
    }

    public static PublicOrderViewFragment newInstance(BaseActivity baseActivity, Order order) {

        PublicOrderViewFragment fragment = new PublicOrderViewFragment(baseActivity);
        Bundle args = new Bundle();
        args.putSerializable(AppContent.ORDER_OBJECT, order);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        photoTakerManager = new PhotoTakerManager(this);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPublicChatBinding.inflate(getLayoutInflater());
        photoTakerManager = new PhotoTakerManager(this);
        if (getArguments() != null) {
            presenter = new PublicOrderViewPresenter(baseActivity, binding, this, photoTakerManager);

            Order order = (Order) getArguments().getSerializable(AppContent.ORDER_OBJECT);
            presenter.getData(order);
            initRecycleView();
            presenter.getMessages(publicChatAdapter, String.valueOf(order.getId()));
            //data();
            click();
        }
        return binding.getRoot();
    }


    private void click() {
        binding.ivUploadMessage.setOnClickListener(view -> presenter.sendMessage(""));
        binding.ivUploadPhoto.setOnClickListener(view -> uploadPhoto());
        binding.ivTracking.setOnClickListener(view -> showLocation());
        binding.ivMore.setOnClickListener(view -> {
            presenter.getData(publicOrder);
            openBillDialog = true;
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        OrdersFragment.viewPagerPage = OrderPublicFragment.viewPagerPage;
        WalletFragment.viewPagerPage = PublicWalletFragment.viewPagerPage;
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
                photoTakerManager.galleryRequest(requireActivity(), AppContent.REQUEST_STUDIO);
            }

            @Override
            public void onCameraClicked() {
                photoTakerManager.cameraRequest(requireActivity(), AppContent.REQUEST_CAMERA);
            }
        });
        itemSelectImageDialogFragment.show(getChildFragmentManager(), "");
    }

    public void showLocation() {
        ShowLocationDialog showLocationDialog = ShowLocationDialog.newInstance(publicOrder);
        showLocationDialog.show(getChildFragmentManager(), "");
    }

    private void openBillDialog() {
        billDialog = BillDialog.newInstance(publicOrder);
        billDialog.show(getChildFragmentManager(), "");
        billDialog.setListener(new BillDialog.Listener() {
            @Override
            public void updatePublicOrder() {
                billDialog.dismiss();
                presenter.getData(publicOrder);
            }

            @Override
            public void addBill() {
                AddBillDialog addBillDialog;
                if (billPrice == 0) {
                    addBillDialog = AddBillDialog.newInstance(publicOrder,
                            getString(R.string.enter_bill_price));
                } else {
                    addBillDialog = AddBillDialog.newInstance(publicOrder,
                            getString(R.string.show_bill));
                }
                addBillDialog.show(getParentFragmentManager(), "");
            }
        });
    }

    //functions
    private void data() {
        binding.tvOrderId.setText((getString(R.string.order) + "#" + publicOrder.getInvoice_number()));

        setPrice();
        String currency = AppController.getInstance().getAppSettingsPreferences().getCountry().getCurrency_code();
        binding.tvOrderDetails.setText(publicOrder.getNote());
        binding.tvDeliveryPrice.setText((DecimalFormatterManager.getFormatterInstance()
                .format(Double.parseDouble(publicOrder.getDelivery_cost())) + " " + currency));
        binding.tvTaxPrice.setText((DecimalFormatterManager.getFormatterInstance()
                .format(Double.parseDouble(publicOrder.getTax())) + " " + currency));
        if (publicOrder.getStatus().equals(AppContent.DELIVERED_STATUS)
                || publicOrder.getStatus().equals(AppContent.CANCELLED_STATUS)) {
            binding.ivMore.setVisibility(View.GONE);
            binding.ivTracking.setVisibility(View.GONE);
            binding.llBottom.setVisibility(View.GONE);
        }
    }

    private void setPrice() {
        if (publicOrder.getPurchase_invoice_value() != null) {
            billPrice = Double.parseDouble(publicOrder.getPurchase_invoice_value());
        } else {
            billPrice = 0;
        }
        Log.e("billPrice:", billPrice + "");

    }


    private void initRecycleView() {
        publicChatAdapter = new PublicChatAdapter(getActivity(), getChildFragmentManager());
        binding.rvChat.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvChat.setItemAnimator(new DefaultItemAnimator());
        binding.rvChat.setAdapter(publicChatAdapter);

        if (PermissionUtil.isPermissionGranted(MediaStore.ACTION_IMAGE_CAPTURE, getContext()))
            PermissionUtil.requestPermission(getActivity(), Manifest.permission.CAMERA
                    , AppContent.REQUEST_PERMISSIONS_R_W_STORAGE_CAMERA);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        presenter.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        presenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void setData(PublicOrderObject publicOrderObject) {
        publicOrder = publicOrderObject.getPublicOrder();
        Log.e("orderdata", publicOrder.toString());
        data();

        if (publicOrder.getStatus().equals(AppContent.DELIVERED_STATUS)
                || publicOrder.getStatus().equals(AppContent.CANCELLED_STATUS)) {
            OrderGPSTracking.newInstance(baseActivity).removeUpdates();
        }
        if (openBillDialog) {
            openBillDialog();
            openBillDialog = false;
        }
    }

    @Override
    public void showDialog(String s) {
        WaitDialogFragment.newInstance().show(getChildFragmentManager(), "");
    }

    @Override
    public void hideDialog() {
        WaitDialogFragment.newInstance().dismiss();
    }

    @Override
    public void onSuccess(Bitmap bitmap, String msg) {
        presenter.uploadImage(photoTakerManager.getCurrentPhotoUri());
    }

    @Override
    public void onError(String msg) {

    }

    @Override
    public void onFail(String msg) {
        ToolUtil.showLongToast(msg, requireActivity());
    }

}
