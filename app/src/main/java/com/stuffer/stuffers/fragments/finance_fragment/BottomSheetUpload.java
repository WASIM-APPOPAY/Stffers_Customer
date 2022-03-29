package com.stuffer.stuffers.fragments.finance_fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.PictureListener;
import com.stuffer.stuffers.fragments.finance_fragment.media.MediaActivity;
import com.stuffer.stuffers.my_camera.CameraActivity;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyRadioButton;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class BottomSheetUpload extends BottomSheetDialogFragment implements View.OnClickListener {
    private BottomSheetBehavior mBehaviour;
    MyButton btnClose, btnProceed;
    MyRadioButton rbtCamera, rbtGallery;
    private static final String TAG = "BottomSheetUpload";
    private PictureListener mPictureListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog fBtmShtDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View mView = View.inflate(getActivity(), R.layout.upload_bottom_sheet_dialog, null);
        fBtmShtDialog.setContentView(mView);
        mBehaviour = BottomSheetBehavior.from((View) mView.getParent());
        btnClose = mView.findViewById(R.id.btnClose);
        btnProceed = mView.findViewById(R.id.btnProceed);
        rbtCamera = mView.findViewById(R.id.rbtCamera);
        rbtGallery = mView.findViewById(R.id.rbtGallery);
        btnClose.setOnClickListener(this);
        btnProceed.setOnClickListener(this);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnProceed:
                if (rbtCamera.isChecked()) {
                    checkCameraPermission();
                } else {
                    checkPermission();
                }
                break;
            case R.id.btnClose:
                dismiss();
                break;
        }
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 987);
        } else {
            openCameraActivity();
        }
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(
                getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, AppoConstants.READ_REQUEST);
        } else {
            openGallery();
        }
    }

    private void openCameraActivity() {
        Intent intent = new Intent(getActivity(), CameraActivity.class);
        startActivityForResult(intent, AppoConstants.CAMERA_REQUEST);
    }

    private void openGallery() {
        //Log.e(TAG, "onGalleryImagePickup: Interface successfully called");
        //TODO now open the Gallery so user can pick up the image
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("*/*");
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), AppoConstants.GALLERY_REQUEST);
        Intent intent = new Intent(getActivity(), MediaActivity.class);
        startActivityForResult(intent, AppoConstants.MEDIA_REQUEST);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppoConstants.GALLERY_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                Uri selectedImageUri = data.getData();
                String path;
                try {
                    path = FileUtils.getPath(getActivity(), selectedImageUri);
                    if (path != null) {
                        if (path.contains(AppoConstants.JPG) || path.contains(AppoConstants.JPEG) || path.contains(AppoConstants.PNG)) {
                            String fileSize = FileUtils.getFileSize(new File(path));
                            //Log.e(TAG, "onActivityResult: FileSize :: " + fileSize);
                            updateUploadView(path);
                        } else {
                            //showCommonErrorDialog(promptTitle, promptMessage);
                        }
                    } else {
                        //showCommonErrorDialog(promptTitle, promptMessage);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //showCommonErrorDialog(promptTitle, promptMessage);
                }
            }
        } else if (requestCode == AppoConstants.MEDIA_REQUEST) {
            if (resultCode == RESULT_OK) {
                String stringExtra = data.getStringExtra(AppoConstants.INFO);
                mPictureListener.onPictureSelect(stringExtra);
                dismiss();
            }
        } else if (requestCode == AppoConstants.CAMERA_REQUEST) {
            if (resultCode == RESULT_OK) {
                String stringExtra = data.getStringExtra(AppoConstants.IMAGE_PATH);
                mPictureListener.onPictureSelect(stringExtra);
                dismiss();

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AppoConstants.READ_REQUEST) {
            boolean readPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            boolean writePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
            if (readPermission && writePermission) {
                openGallery();
            } else {
                //forGallery(permissions, grantResults);


            }
        } else if (requestCode == 987) {
            boolean readPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            boolean writePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
            boolean cameraPermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
            if (readPermission && writePermission && cameraPermission) {
                openCameraActivity();
            } else {
                //  forCamera(permissions, grantResults);
            }
        }
    }


    private void updateUploadView(String filePath) {
        // TODO use split for getting NAME
        //Log.e(TAG, "updateUploadView: path :: " + filePath);


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mPictureListener = (PictureListener) context;
    }
}
