package com.stuffer.stuffers.fragments.bottom_fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.api.Constants;
import com.stuffer.stuffers.fragments.dialog.CurrencyDialogFragment;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyRadioButton;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Objects;

public class BottomSonyPlayStation extends BottomSheetDialogFragment implements View.OnClickListener {

    private BottomSheetBehavior mBehaviour;
    private RecyclerView rvPlayStation;
    private MyButton btnClose, btnProcees;
    ArrayList<String> mListParam;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog fBtmShtDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View mView = View.inflate(getActivity(), R.layout.fragment_sony_play_station, null);
        fBtmShtDialog.setContentView(mView);
        mBehaviour = BottomSheetBehavior.from((View) mView.getParent());
        Bundle arguments = this.getArguments();
        mListParam = new ArrayList<>();
        mListParam.add(arguments.getString(Constants.PARAM1));
        mListParam.add(arguments.getString(Constants.PARAM2));
        findIds(mView);
        SonyStationAdapter adapter = new SonyStationAdapter(getActivity(), mListParam);
        rvPlayStation.setAdapter(adapter);

        return fBtmShtDialog;
    }

    private void findIds(View mView) {
        rvPlayStation = (RecyclerView) mView.findViewById(R.id.rvPlayStation);
        btnClose = (MyButton) mView.findViewById(R.id.btnClose);
        btnProcees = (MyButton) mView.findViewById(R.id.btnProceed);
        rvPlayStation.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        btnProcees.setOnClickListener(this);
        btnClose.setOnClickListener(this);
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
            case R.id.btnClose:
                dismiss();
                break;
            case R.id.btnProceed:
                verify();
                break;
        }
    }

    public class SonyStationAdapter extends RecyclerView.Adapter<SonyStationAdapter.SonyHolder> {
        private int lastSelectedPosition = -1;
        private Context mContext;
        private ArrayList<String> mList;

        public SonyStationAdapter(Context mContext, ArrayList<String> items) {
            this.mContext = mContext;
            this.mList = items;
        }

        @NonNull
        @Override
        public SonyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.row_common_item, parent, false);
            return new SonyHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SonyHolder holder, int position) {
            holder.bind();
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        public class SonyHolder extends RecyclerView.ViewHolder {
            MyRadioButton rbtnStation;

            public SonyHolder(@NonNull View itemView) {
                super(itemView);
                rbtnStation = itemView.findViewById(R.id.rbtnCommon);
                rbtnStation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lastSelectedPosition = getAdapterPosition();
                        notifyDataSetChanged();
                        //allow = true;
                        //mSelectListener.onCurrencySelected(mList.get(lastSelectedPosition).getId(), mList.get(lastSelectedPosition).getCurrencyCode());

                    }
                });
            }

            public void bind() {
                rbtnStation.setChecked(lastSelectedPosition == getAdapterPosition());
                rbtnStation.setText(mList.get(getAdapterPosition()));
            }
        }
    }

    private void verify() {

    }
}
