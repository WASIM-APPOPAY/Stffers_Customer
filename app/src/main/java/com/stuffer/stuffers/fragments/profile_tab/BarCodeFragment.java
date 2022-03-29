package com.stuffer.stuffers.fragments.profile_tab;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.stuffer.stuffers.R;


public class BarCodeFragment extends Fragment {
    private static final String TAG = "BarCodeFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private ImageView ivEmvBarCode;


    public BarCodeFragment() {
        // Required empty public constructor
    }


    public static BarCodeFragment newInstance(String param1) {
        BarCodeFragment fragment = new BarCodeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Log.e(TAG, "onCreate: barcode called" );
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mViewBar = inflater.inflate(R.layout.fragment_bar_code, container, false);
        ivEmvBarCode = (ImageView) mViewBar.findViewById(R.id.ivEmvBarCode);
        //final byte[] decodedBytes = Base64.decode(mParam1, Base64.DEFAULT);
        //Glide.with(getActivity()).load().into(ivEmvBarCode);
        final byte[] decodedBytes = Base64.decode(mParam1, Base64.DEFAULT);
        Glide.with(getActivity()).load(decodedBytes).into(ivEmvBarCode);

        return mViewBar;
    }
}