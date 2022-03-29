package com.stuffer.stuffers.activity.shop_mall;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.views.MyTextView;
import com.stuffer.stuffers.views.MyTextViewBold;

import org.json.JSONException;
import org.json.JSONObject;


public class AddToCardShopFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "AddToCardShopFragment";
    private ImageView ivItem, ivQr;
    private MyTextViewBold tvTitle, tvShopName, tvTotal;
    private MyTextView tvDisPrice, tvActualPrice;;
    private String stringExtras;
    ImageButton iBtnIncrease, ibtnDecrease;
    int mCounter = 0;


    public AddToCardShopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_add_to_card_shop, container, false);
        Bundle bundle = this.getArguments();
        stringExtras = bundle.getString(AppoConstants.INFO);
        ivItem = (ImageView) mView.findViewById(R.id.ivItem);
        tvTitle = (MyTextViewBold) mView.findViewById(R.id.tvTitle);
        tvDisPrice = (MyTextView) mView.findViewById(R.id.tvDisPrice);
        tvActualPrice = (MyTextView) mView.findViewById(R.id.tvActualPrice);
        ivQr = (ImageView) mView.findViewById(R.id.ivQr);
        tvShopName = (MyTextViewBold) mView.findViewById(R.id.tvShopName);
        tvTotal = (MyTextViewBold) mView.findViewById(R.id.tvTotal);
        iBtnIncrease = (ImageButton) mView.findViewById(R.id.ibtnIncrease);
        ibtnDecrease = (ImageButton) mView.findViewById(R.id.ibtnDecrease);
        iBtnIncrease.setOnClickListener(this);
        ibtnDecrease.setOnClickListener(this);
        setReceivedParam();

        return mView;
    }

    private void setReceivedParam() {
        try {
            JSONObject param = new JSONObject(stringExtras);
            Glide.with(getActivity()).load(param.getString(AppoConstants.PRODUCT_IMAGE)).into(ivItem);
            tvTitle.setText(param.getString(AppoConstants.PRODUCT_NAME));
            Glide.with(getActivity()).load(param.getString(AppoConstants.PRODUCT_QR)).into(ivQr);
            tvShopName.setText("Shop Name : " + param.getString(AppoConstants.PRODUCT_SHOP_NAME));
            String currency = param.getString(AppoConstants.CURRENCYSYMBOL);
            tvDisPrice.setText(currency + " " + param.getString(AppoConstants.DISCOUNT_PRICE));
            tvActualPrice.setText(currency + " " + param.getString(AppoConstants.ACTUAL_PRICE));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibtnIncrease:
                mCounter = mCounter + 1;
                tvTotal.setText(mCounter + ".00");
                break;
            case R.id.ibtnDecrease:
                mCounter = mCounter - 1;
                if (mCounter < 0) {
                    mCounter = 0;
                    return;
                }
                if (mCounter == 0) {
                    tvTotal.setText("0");
                } else {
                    tvTotal.setText(mCounter + ".00");
                }
                break;
        }
    }
}