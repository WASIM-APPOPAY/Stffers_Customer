package com.stuffer.stuffers.fragments.homeitem;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.wallet.HotelSearchActivity;
import com.stuffer.stuffers.activity.wallet.TravelActivity;
import com.stuffer.stuffers.communicator.StartActivityListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TravelFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TravelFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private StartActivityListener mListener;
    private LinearLayout llHotels,llFlightTicket,llBus,llTrainTicket;

    public TravelFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView= inflater.inflate(R.layout.fragment_travel, container, false);
        llHotels = (LinearLayout)mView.findViewById(R.id.llHotels);
        llFlightTicket =(LinearLayout) mView.findViewById(R.id.llFlightTicket);
        llBus = (LinearLayout) mView.findViewById(R.id.llBus);
        llTrainTicket =(LinearLayout) mView.findViewById(R.id.llTrainTicket);
        llHotels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), HotelSearchActivity.class);
                startActivity(intent);

            }
        });

        llFlightTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), TravelActivity.class);
                intent.putExtra("travel", "flight");
                startActivity(intent);

            }
        });

        llBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TravelActivity.class);
                intent.putExtra("travel", "bus");
                startActivity(intent);

            }
        });

        llTrainTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), TravelActivity.class);
                intent.putExtra("travel", "train");
                startActivity(intent);

            }
        });


        return mView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (StartActivityListener) context;

    }
}