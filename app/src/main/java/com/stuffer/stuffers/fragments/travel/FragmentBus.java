package com.stuffer.stuffers.fragments.travel;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.wallet.BusSearchResultActivity;
import com.stuffer.stuffers.views.MyTextView;

public class FragmentBus extends Fragment {

    MyTextView btnSearchBuses;

    View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.bus_fragment, container, false);

        btnSearchBuses = (MyTextView) mView.findViewById(R.id.btnSearchBuses);

        btnSearchBuses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), BusSearchResultActivity.class);
                startActivity(intent);

            }
        });

        return mView;

    }
}
