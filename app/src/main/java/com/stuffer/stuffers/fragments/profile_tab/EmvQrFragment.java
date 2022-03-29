package com.stuffer.stuffers.fragments.profile_tab;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.stuffer.stuffers.R;


public class EmvQrFragment extends Fragment {

    private static final String TAG = "EmvQrFragment";
    private static final String ARG_PARAM1 = "param1";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private ImageView ivEmvQrCode;

    public EmvQrFragment() {
        // Required empty public constructor
    }

    public static EmvQrFragment newInstance(String param1) {
        EmvQrFragment fragment = new EmvQrFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Log.e(TAG, "onCreate: called" );
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mViewQr = inflater.inflate(R.layout.fragment_emv_qr, container, false);
        ivEmvQrCode=(ImageView)mViewQr.findViewById(R.id.ivEmvQrCode);
        final byte[] decodedBytes = Base64.decode(mParam1, Base64.DEFAULT);
        Glide.with(getActivity()).load(decodedBytes).into(ivEmvQrCode);

        return mViewQr;
    }
}