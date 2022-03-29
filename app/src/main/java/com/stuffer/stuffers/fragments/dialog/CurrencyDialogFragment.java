package com.stuffer.stuffers.fragments.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.ConfirmSelectListener;
import com.stuffer.stuffers.communicator.CurrencySelectListener;
import com.stuffer.stuffers.models.output.CurrencyResult;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyRadioButton;

import java.util.ArrayList;

public class CurrencyDialogFragment extends DialogFragment {

    private RecyclerView rvCurrency;
    private MyButton btnClose;
    private MyButton btnConfirm;
    ArrayList<CurrencyResult> parcelableArrayList;
    private ConfirmSelectListener mConfirmListener;
    private boolean allow = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_dialog_currency, container, false);
        rvCurrency = (RecyclerView) view.findViewById(R.id.rvCurrency);
        btnClose = (MyButton) view.findViewById(R.id.btnClose);
        btnConfirm = (MyButton) view.findViewById(R.id.btnConfirm);
        Bundle arguments = this.getArguments();

        parcelableArrayList = arguments.getParcelableArrayList(AppoConstants.INFO);
        rvCurrency.setLayoutManager(new LinearLayoutManager(getContext()));

        CurrencyAdapter adapter = new CurrencyAdapter(getContext(), parcelableArrayList);
        rvCurrency.setAdapter(adapter);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allow) {
                    mConfirmListener.onConfirmSelect();
                } else {
                    Toast.makeText(getContext(), "Please select your currency ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.CurrencyHolder> {
        private Context mContext;
        private ArrayList<CurrencyResult> mList;
        private int lastSelectedPosition = -1;
        private CurrencySelectListener mSelectListener;

        public CurrencyAdapter(Context mContext, ArrayList<CurrencyResult> mList) {
            this.mContext = mContext;
            this.mList = mList;
            try {
                mSelectListener = (CurrencySelectListener) mContext;
            } catch (ClassCastException e) {
                throw new ClassCastException("parent must implement CurrencySelectListener");
            }
        }

        @NonNull
        @Override
        public CurrencyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.row_common_item, parent, false);
            return new CurrencyHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CurrencyHolder holder, int position) {
            holder.bindProcess();
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        public class CurrencyHolder extends RecyclerView.ViewHolder {
            MyRadioButton rbtnCurrency;

            public CurrencyHolder(@NonNull View itemView) {
                super(itemView);
                rbtnCurrency = itemView.findViewById(R.id.rbtnCommon);
                rbtnCurrency.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lastSelectedPosition = getAdapterPosition();
                        notifyDataSetChanged();
                        allow = true;
                        mSelectListener.onCurrencySelected(mList.get(lastSelectedPosition).getId(), mList.get(lastSelectedPosition).getCurrencyCode());

                    }
                });
            }

            public void bindProcess() {
                rbtnCurrency.setChecked(lastSelectedPosition == getAdapterPosition());
                rbtnCurrency.setText(mList.get(getAdapterPosition()).getCurrencyCode());
            }
        }


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mConfirmListener = (ConfirmSelectListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("parent must implement ConfirmSelectListener");
        }

    }
}
