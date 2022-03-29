package com.stuffer.stuffers.activity.shop_mall;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.ShopListener;
import com.stuffer.stuffers.models.shop_model.ShopModel;
import com.stuffer.stuffers.utils.Helper;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.kyanogen.signatureview.SignatureView;

import java.util.ArrayList;
import java.util.Objects;


public class BottomShopOnMallFragment extends BottomSheetDialogFragment implements View.OnClickListener, ShopListener {
    private static final String TAG = "BottomShopOnMallFragmen";

    private BottomSheetBehavior mBehaviour;
    private RecyclerView rvShop;
    private ImageView ivClick;
    ShopListener mListener;

    public BottomShopOnMallFragment() {
        // Required empty public constructor
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog fBtmShtDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View mView = View.inflate(getActivity(), R.layout.fragment_bottom_shop_on_mall, null);
        fBtmShtDialog.setContentView(mView);
        mBehaviour = BottomSheetBehavior.from((View) mView.getParent());
        rvShop = mView.findViewById(R.id.rvShop);
        ivClick = mView.findViewById(R.id.ivClick);
        rvShop.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        ArrayList<ShopModel> mList = Helper.getShopItems(getActivity());
        ShopAdapter adapter = new ShopAdapter(getActivity(), mList, this);
        rvShop.setAdapter(adapter);
        ivClick.setOnClickListener(this);
        return fBtmShtDialog;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivClick:
                dismiss();
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        View touchOutsideView = Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow())
                .getDecorView()
                .findViewById(com.google.android.material.R.id.touch_outside);
        touchOutsideView.setClickable(false);
        touchOutsideView.setFocusable(false);

        mBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
        mBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                switch (i) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        mBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
    }

    @Override
    public void onShopItemClick(int pos, String title) {
        //Log.e(TAG, "onShopItemClick: " + title);
        mListener.onShopItemClick(pos,title);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (ShopListener) context;

    }
}