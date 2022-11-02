package com.stuffer.stuffers.fragments.wallet_fragments;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;


import com.stuffer.stuffers.R;
import com.stuffer.stuffers.views.MyTextViewBold;

import org.apache.commons.lang3.StringUtils;


public class SpecificFragment extends Fragment {

    public static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View mView;
    private MyTextViewBold tvTitle;
    private LinearLayout linearPanama, linearDominica,linearPara;

    public SpecificFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_specific, container, false);
        tvTitle = mView.findViewById(R.id.tvTitle);
        tvTitle.setText(Html.fromHtml(getString(R.string.info_payments_methods)));
        linearPanama = mView.findViewById(R.id.linearPanama);
        linearDominica = mView.findViewById(R.id.linearDominica);
        linearPara = mView.findViewById(R.id.linearPara);
        if (!StringUtils.isEmpty(mParam1)) {
            if (mParam1.equalsIgnoreCase("1")) {
                linearPanama.setVisibility(View.VISIBLE);
                linearDominica.setVisibility(View.GONE);
                linearPara.setVisibility(View.GONE);
            } else if (mParam1.equalsIgnoreCase("2")){
                linearDominica.setVisibility(View.VISIBLE);
                linearPanama.setVisibility(View.GONE);
                linearPara.setVisibility(View.GONE);

            } else{
                linearDominica.setVisibility(View.GONE);
                linearPanama.setVisibility(View.GONE);
                linearPara.setVisibility(View.VISIBLE);

            }
        }

        return mView;
    }
}