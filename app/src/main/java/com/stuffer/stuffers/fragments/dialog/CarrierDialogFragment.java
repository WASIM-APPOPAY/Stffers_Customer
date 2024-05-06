package com.stuffer.stuffers.fragments.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.CarrierSelectListener;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyRadioButton;
import com.stuffer.stuffers.views.MyTextView;
import com.stuffer.stuffers.views.MyTextViewBold;

import java.util.ArrayList;

public class CarrierDialogFragment extends DialogFragment {

    private RecyclerView rvCarrier;
    private MyButton btnClose;
    private ArrayList<String> mListCarrier;
    private MyTextViewBold tvTitle;
    private String mTitle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_dialog_carrier, container, false);
        rvCarrier = (RecyclerView) view.findViewById(R.id.rvCarrier);
        btnClose = (MyButton) view.findViewById(R.id.btnClose);
        tvTitle = (MyTextViewBold) view.findViewById(R.id.tvTitle);

        Bundle arguments = this.getArguments();
        mListCarrier = arguments.getStringArrayList(AppoConstants.INFO);
        mTitle = arguments.getString(AppoConstants.TITLE);
        rvCarrier.setLayoutManager(new LinearLayoutManager(getContext()));
        CarrierAdapter adapter = new CarrierAdapter(getContext(), mListCarrier);
        rvCarrier.setAdapter(adapter);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    public static class CarrierAdapter extends RecyclerView.Adapter<CarrierAdapter.CarrierHolder> {
        private Context mContext;
        private ArrayList<String> mList;
        private int lastSelectedPosition = -1;
        private CarrierSelectListener mListener;

        public CarrierAdapter(Context mContext, ArrayList<String> mList) {
            this.mContext = mContext;
            this.mList = mList;
            try {
                this.mListener = (CarrierSelectListener) mContext;
            } catch (ClassCastException e) {
                throw new ClassCastException("parent should implement CarrierSelectListener");
            }
        }

        @NonNull
        @Override
        public CarrierHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.row_carrier_item, parent, false);
            return new CarrierHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CarrierHolder holder, int position) {
            holder.bindProcess();
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        public class CarrierHolder extends RecyclerView.ViewHolder {
            MyRadioButton rbtnCarrier;

            public CarrierHolder(@NonNull View itemView) {
                super(itemView);
                rbtnCarrier = itemView.findViewById(R.id.rbtnCarrier);
                rbtnCarrier.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lastSelectedPosition = getAdapterPosition();
                        mListener.onCarrierSelect(getAdapterPosition());
                    }
                });
            }

            public void bindProcess() {
                rbtnCarrier.setChecked(lastSelectedPosition == getAdapterPosition());
                rbtnCarrier.setText(mList.get(getAdapterPosition()));
            }
        }
    }


}
