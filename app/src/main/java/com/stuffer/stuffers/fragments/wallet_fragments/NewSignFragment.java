package com.stuffer.stuffers.fragments.wallet_fragments;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_IDPATH;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.AgreementListen;
import com.stuffer.stuffers.communicator.OnBankSubmit;
import com.stuffer.stuffers.fragments.finance_fragment.BottomSheetSignature;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MySwitchView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class NewSignFragment extends Fragment implements View.OnClickListener {
    private View mView;
    private String mIdPath;
    private int mCounter;
    private MySwitchView signatureBox;
    private ImageView signature_view;
    private FrameLayout frameSignature;
    private ImageView image1, image2;
    private String imagePath2;
    private String imagePath1;
    private LinearLayout layoutUpload;
    private File mfileSignature;
    AgreementListen mListen;
    private OnBankSubmit mConfirmListener;
    private MyButton btnSubmitForm;

    public NewSignFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_new_sign, container, false);
        btnSubmitForm = mView.findViewById(R.id.btnSubmitForm);
        signatureBox = mView.findViewById(R.id.signatureBox);
        signature_view = mView.findViewById(R.id.signature_view);
        frameSignature = mView.findViewById(R.id.frameSignature);
        layoutUpload = mView.findViewById(R.id.layoutUpload);
        image1 = mView.findViewById(R.id.image1);
        image2 = mView.findViewById(R.id.image2);
        mIdPath = DataVaultManager.getInstance(getActivity()).getVaultValue(KEY_IDPATH);
        mCounter = 1;
        setImage(mIdPath);
        signatureBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    BottomSheetSignature fragmentBottomSheet = new BottomSheetSignature();
                    fragmentBottomSheet.show(getChildFragmentManager(), fragmentBottomSheet.getTag());
                    fragmentBottomSheet.setCancelable(false);

                }
            }
        });
        btnSubmitForm.setOnClickListener(this);
        return mView;

    }


    public void setImage(String path) {
        //mCounter = mCounter + 1;
        if (mCounter == 1) {
            image1.setPadding(0, 0, 0, 0);
            Glide.with(getActivity())
                    .load(path)
                    .into(image1);
            imagePath1 = path;
        } else {
            signature_view.setPadding(0, 0, 0, 0);
            Glide.with(getActivity())
                    .load(path)
                    .centerInside()
                    .into(signature_view);
            imagePath2 = path;
        }


    }

    public void setSignture(Bitmap bitmap, boolean status) {
        if (status) {

            frameSignature.setVisibility(View.VISIBLE);
            signatureBox.setChecked(false);
            signature_view.setImageBitmap(bitmap);
            layoutUpload.setVisibility(View.VISIBLE);
            /*scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });*/
            mfileSignature = new File(getActivity().getCacheDir(), "sign.png");
            try {
                boolean newFile = mfileSignature.createNewFile();
                if (newFile) {
                    //Convert bitmap to byte array
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                    byte[] bitmapdata = bos.toByteArray();
                    //write the bytes in file
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(mfileSignature);
                        fos.write(bitmapdata);
                        fos.flush();
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            mCounter = 2;
            setImage(mfileSignature.getAbsolutePath());


        } else {
            frameSignature.setVisibility(View.GONE);
            layoutUpload.setVisibility(View.GONE);
        }


    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mConfirmListener = (OnBankSubmit) context;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSubmitForm) {
            //Toast.makeText(getActivity(), "submit click", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
    }
}