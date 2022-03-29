package com.stuffer.stuffers.fragments.finance_fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.SignatureCallback;
import com.stuffer.stuffers.views.MyButton;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.kyanogen.signatureview.SignatureView;

import java.util.Objects;

public class BottomSheetSignature extends BottomSheetDialogFragment implements View.OnClickListener {

    private BottomSheetBehavior mBehaviour;
    SignatureCallback mSignature;
    private MyButton btnReset, btnSaveSign;
    private SignatureView signatureView;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog fBtmShtDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View mView = View.inflate(getActivity(), R.layout.signature_bottom_sheet_dialog, null);
        fBtmShtDialog.setContentView(mView);
        mBehaviour = BottomSheetBehavior.from((View) mView.getParent());
        btnReset = mView.findViewById(R.id.btnReset);
        btnSaveSign = mView.findViewById(R.id.btnSaveSign);
        signatureView = (SignatureView) mView.findViewById(R.id.signature_view);
        btnReset.setOnClickListener(this);
        btnSaveSign.setOnClickListener(this);
        return fBtmShtDialog;

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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mSignature= (SignatureCallback) context;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnReset:
                signatureView.clearCanvas();
                break;
            case R.id.btnSaveSign:
                saveBitmap();
                break;
        }
    }
    private void saveBitmap() {
        if (signatureView.isBitmapEmpty()) {
            Toast.makeText(getContext(), "Signature failed", Toast.LENGTH_LONG).show();
            return;
        }

        // View view = mLlCanvas.getRootView();
        /*View view = (View) mLlCanvas.getChildAt(0);
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);*/
        Bitmap bitmap = signatureView.getSignatureBitmap();
        mSignature.onSignatureConfirm(bitmap,true);
        dismiss();
        /*
        Intent intent = new Intent();
        intent.putExtra(MainActivity.KEY_SIGN, encoded);
        setResult(Activity.RESULT_OK, intent);
        finish();*/
    }
}
