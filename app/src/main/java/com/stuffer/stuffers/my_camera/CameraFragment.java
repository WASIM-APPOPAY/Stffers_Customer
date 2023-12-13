package com.stuffer.stuffers.my_camera;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stuffer.stuffers.R;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.priyankvasa.android.cameraviewex.CameraView;
import com.priyankvasa.android.cameraviewex.ErrorLevel;
import com.priyankvasa.android.cameraviewex.Image;
import com.priyankvasa.android.cameraviewex.Modes;
import com.stuffer.stuffers.communicator.PictureListener;


import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;

import kotlin.Unit;


public class CameraFragment extends Fragment {
    private static final String TAG = "CameraFragment";
    private CameraView camera;
    private ImageView ivFlashSwitch;
    private ImageView ivFramePreview;
    private ImageView ivCapturePreview;
    private ImageView ivCloseCapturePreview;

    private RequestManager glideManager;

    private final Matrix matrix = new Matrix();
    private PictureListener mPictureListener;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_camera, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        glideManager = Glide.with(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED) {
            camera.start();
        }
    }

    @Override
    public void onPause() {
        camera.stop();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        camera.destroy();
        super.onDestroyView();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        camera = view.findViewById(R.id.camera);
        ImageView ivCapture = view.findViewById(R.id.ivCaptureButton);
        ivFlashSwitch = view.findViewById(R.id.ivFlashSwitch);
        ivFramePreview = view.findViewById(R.id.ivFramePreview);
        ivCapturePreview = view.findViewById(R.id.ivCapturePreview);
        ivCloseCapturePreview = view.findViewById(R.id.ivCloseCapturePreview);
        ivCloseCapturePreview.setOnClickListener((View v) -> {
            ivCapturePreview.setVisibility(View.GONE);
            v.setVisibility(View.GONE);
        });

        ivCapture.setOnClickListener((View v) -> camera.capture());

        ivFlashSwitch.setOnClickListener((View v) -> {

            @DrawableRes int flashDrawableId;

            switch (camera.getFlash()) {

                case Modes.Flash.FLASH_OFF:
                    flashDrawableId = R.drawable.ic_flash_auto;
                    camera.setFlash(Modes.Flash.FLASH_AUTO);
                    break;

                case Modes.Flash.FLASH_AUTO:
                    flashDrawableId = R.drawable.ic_flash_on;
                    camera.setFlash(Modes.Flash.FLASH_ON);
                    break;

                case Modes.Flash.FLASH_ON:
                    flashDrawableId = R.drawable.ic_flash_off;
                    camera.setFlash(Modes.Flash.FLASH_OFF);
                    break;

                default:
                    return;
            }

            ivFlashSwitch.setImageDrawable(ActivityCompat.getDrawable(requireContext(), flashDrawableId));
        });

        setupCamera();
    }

    private void setupCamera() {

        View view = getView();
        if (view == null) return;

        camera.addCameraOpenedListener(() -> {
            Log.e(TAG, "setupCamera: " + "Camera opened");

            return Unit.INSTANCE;
        });



        camera.addPictureTakenListener((Image image) -> {
            byte[] jpegData;

            YuvImage yuvImage = new YuvImage(
                    image.getData(),
                    ImageFormat.NV21,
                    image.getWidth(),
                    image.getHeight(),
                    null
            );

            final ByteArrayOutputStream jpegDataStream = new ByteArrayOutputStream();

            final float previewFrameScale = 0.4f;

            yuvImage.compressToJpeg(
                    new Rect(0, 0, image.getWidth(), image.getHeight()),
                    (int) (100 * previewFrameScale),
                    jpegDataStream
            );

            jpegData = jpegDataStream.toByteArray();
            // Log.e(TAG, "setupCamera: " + Arrays.toString(jpegData));


            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bm = BitmapFactory.decodeByteArray(jpegData, 0, jpegData.length, options);
            //Log.e(TAG, "showFramePreview: " + jpegData.length);

            String filename = "pippo.png";
            File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File dest = new File(sd, filename);
            byte[] data = image.getData();
            Bitmap bitmap1 = BitmapFactory.decodeByteArray(data, 0, data.length);
            Bitmap bitmap = reduzeBitmapSize(bitmap1, 240000);

            try {
                FileOutputStream out = new FileOutputStream(dest);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }


            //showCapturePreview(image);

            Activity activity = getActivity();
            if (activity != null) activity.runOnUiThread(() -> {
                mPictureListener.onPictureSelect(dest.getAbsolutePath());
                    }
            );

            return Unit.INSTANCE;
        });

        camera.setContinuousFrameListener(
                5f, // Max frame rate
                (Image image) -> {
                    showFramePreview(image);
                    return Unit.INSTANCE;
                }
        );

        camera.addCameraErrorListener((Throwable t, ErrorLevel errorLevel) -> {
            if (errorLevel instanceof ErrorLevel.Warning)
                Log.e(TAG, "setupCamera: " + t);
            else if (errorLevel instanceof ErrorLevel.Error)
                Log.e(TAG, "setupCamera: " + t);
            return Unit.INSTANCE;
        });

        camera.addCameraClosedListener(() -> {
            Log.e(TAG, "setupCamera: " + " camera closed");
            return Unit.INSTANCE;
        });
    }

    public Bitmap reduzeBitmapSize(Bitmap bitmap, int MAX_SIZE) {
        double ratioSquare;
        int bitmapHeight, bitmapWidth;
        bitmapHeight = bitmap.getHeight();
        bitmapWidth = bitmap.getWidth();
        ratioSquare = (bitmapHeight * bitmapWidth) / MAX_SIZE;
        if (ratioSquare <= 1) {
            return bitmap;
        }
        double ratio = Math.sqrt(ratioSquare);
        int requiredHeight = (int) Math.round(bitmapHeight / ratio);
        int requiredWidth = (int) Math.round(bitmapWidth / ratio);
        return Bitmap.createScaledBitmap(bitmap, requiredWidth, requiredHeight, true);

    }

    private void showCapturePreview(@NotNull Image image) {

        final float previewCaptureScale = 0.2f;

        final RequestOptions requestOptions = new RequestOptions()
                .override((int) (image.getWidth() * previewCaptureScale), (int) (image.getHeight() * previewCaptureScale));

        Activity activity = getActivity();
        if (activity != null) activity.runOnUiThread(() -> {
                    glideManager.load(image.getData())
                            .apply(requestOptions)
                            .into(ivCapturePreview);
                    ivCapturePreview.setVisibility(View.VISIBLE);
                    ivCloseCapturePreview.setVisibility(View.VISIBLE);
                }
        );
    }

    private void showFramePreview(@NotNull Image image) {

        byte[] jpegData;

        YuvImage yuvImage = new YuvImage(
                image.getData(),
                ImageFormat.NV21,
                image.getWidth(),
                image.getHeight(),
                null
        );

        final ByteArrayOutputStream jpegDataStream = new ByteArrayOutputStream();

        final float previewFrameScale = 0.4f;

        yuvImage.compressToJpeg(
                new Rect(0, 0, image.getWidth(), image.getHeight()),
                (int) (100 * previewFrameScale),
                jpegDataStream
        );

        jpegData = jpegDataStream.toByteArray();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bm = BitmapFactory.decodeByteArray(jpegData, 0, jpegData.length, options);
        //Log.e(TAG, "showFramePreview: " + jpegData.length);


        if (bm == null) return;

        final RequestOptions requestOptions = new RequestOptions()
                .override((int) (bm.getWidth() * previewFrameScale), (int) (bm.getHeight() * previewFrameScale));

        Activity activity = getActivity();
        if (activity != null) activity.runOnUiThread(() ->
                glideManager.load(rotate(bm, image.getExifInterface().getRotation()))
                        .apply(requestOptions)
                        .into(ivFramePreview)
        );
    }

    private Bitmap rotate(Bitmap bm, int rotation) {

        if (rotation == 0) return bm;

        matrix.setRotate(rotation);

        return Bitmap.createBitmap(
                bm,
                0,
                0,
                bm.getWidth(),
                bm.getHeight(),
                matrix,
                true
        );
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mPictureListener = (PictureListener) context;
    }
}