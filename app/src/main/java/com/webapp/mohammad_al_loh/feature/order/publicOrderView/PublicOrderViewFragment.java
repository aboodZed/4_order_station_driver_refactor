package com.webapp.mohammad_al_loh.feature.order.publicOrderView;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.annotations.NotNull;
import com.webapp.mohammad_al_loh.R;
import com.webapp.mohammad_al_loh.databinding.FragmentPublicChatBinding;
import com.webapp.mohammad_al_loh.feature.main.orders.OrdersFragment;
import com.webapp.mohammad_al_loh.feature.main.orders.publicO.OrderPublicFragment;
import com.webapp.mohammad_al_loh.feature.main.wallets.WalletFragment;
import com.webapp.mohammad_al_loh.feature.main.wallets.publicO.PublicWalletFragment;
import com.webapp.mohammad_al_loh.feature.order.adapter.PublicChatAdapter;
import com.webapp.mohammad_al_loh.models.Order;
import com.webapp.mohammad_al_loh.models.PublicOrder;
import com.webapp.mohammad_al_loh.models.PublicOrderObject;
import com.webapp.mohammad_al_loh.utils.AppContent;
import com.webapp.mohammad_al_loh.utils.AppController;
import com.webapp.mohammad_al_loh.utils.PermissionUtil;
import com.webapp.mohammad_al_loh.utils.Photo.PhotoTakerManager;
import com.webapp.mohammad_al_loh.utils.ToolUtil;
import com.webapp.mohammad_al_loh.utils.dialogs.AddBillDialog;
import com.webapp.mohammad_al_loh.utils.dialogs.BillDialog;
import com.webapp.mohammad_al_loh.utils.dialogs.ItemSelectImageDialogFragment;
import com.webapp.mohammad_al_loh.utils.dialogs.ShowLocationDialog;
import com.webapp.mohammad_al_loh.utils.dialogs.WaitDialogFragment;
import com.webapp.mohammad_al_loh.utils.formatter.DecimalFormatterManager;
import com.webapp.mohammad_al_loh.utils.language.BaseActivity;
import com.webapp.mohammad_al_loh.utils.listeners.DialogView;
import com.webapp.mohammad_al_loh.utils.listeners.RequestListener;
import com.webapp.mohammad_al_loh.utils.location.tracking.OrderGPSTracking;

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

    private ActivityResultLauncher<Intent> launcher;

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
            onActivityResulting();
        }
        return binding.getRoot();
    }

    private void onActivityResulting() {
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> presenter.onActivityResult(result.getResultCode(), result.getData()));
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
                presenter.setRequestCode(AppContent.REQUEST_STUDIO);
                photoTakerManager.galleryRequestLauncher(requireActivity(), launcher);
            }

            @Override
            public void onCameraClicked() {
                presenter.setRequestCode(AppContent.REQUEST_STUDIO);
                photoTakerManager.cameraRequestLauncher(requireActivity(), launcher);
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
