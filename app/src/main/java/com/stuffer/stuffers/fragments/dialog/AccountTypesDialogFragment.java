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
import com.stuffer.stuffers.communicator.AccountSelectedListener;
import com.stuffer.stuffers.communicator.ConfirmSelectListener;
import com.stuffer.stuffers.models.bank.account.Result;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyRadioButton;

import java.util.List;

public class AccountTypesDialogFragment extends DialogFragment {

    List<Result> resultAccTypes;
    private MyButton btnConfirm, btnClose;
    RecyclerView rvAccounts;
    private boolean allow = false;
    ConfirmSelectListener mConfirmSelectListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_dialog_account, container, false);
        Bundle arguments = this.getArguments();

        resultAccTypes = arguments.getParcelableArrayList(AppoConstants.INFO);
        btnConfirm = (MyButton) view.findViewById(R.id.btnConfirm);
        btnClose = (MyButton) view.findViewById(R.id.btnClose);
        rvAccounts = (RecyclerView) view.findViewById(R.id.rvAccounts);
        rvAccounts.setLayoutManager(new LinearLayoutManager(getContext()));
        AccountAdapter accountAdapter = new AccountAdapter(getActivity(), resultAccTypes);
        rvAccounts.setAdapter(accountAdapter);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConfirmSelectListener.onConfirmSelect();

            }
        });

        return view;
    }

    public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountHolder> {
        Context context;
        List<Result> resultAccTypes;
        private int lastSelectedPosition = -1;
        AccountSelectedListener mSelectedListener;

        public AccountAdapter(Context context, List<Result> resultAccTypes) {
            this.context = context;
            this.resultAccTypes = resultAccTypes;
            try {
                mSelectedListener = (AccountSelectedListener) context;
            } catch (ClassCastException e) {
                throw new ClassCastException("parent must implement AccountSelectedListener");
            }
        }

        @NonNull
        @Override
        public AccountHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.row_common_item, parent, false);
            return new AccountHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AccountHolder holder, int position) {
            holder.bindProcess();
        }

        @Override
        public int getItemCount() {
            return resultAccTypes.size();
        }

        public class AccountHolder extends RecyclerView.ViewHolder {
            MyRadioButton rbtnCurrency;

            public AccountHolder(@NonNull View itemView) {
                super(itemView);
                rbtnCurrency = itemView.findViewById(R.id.rbtnCommon);
                rbtnCurrency.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lastSelectedPosition = getAdapterPosition();
                        notifyDataSetChanged();
                        allow = true;
                        mSelectedListener.onAccountSelect(resultAccTypes.get(lastSelectedPosition).getTypename(), lastSelectedPosition);

                    }
                });
            }

            public void bindProcess() {
                rbtnCurrency.setChecked(lastSelectedPosition == getAdapterPosition());
                rbtnCurrency.setText(resultAccTypes.get(getAdapterPosition()).getTypename());
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            this.mConfirmSelectListener = (ConfirmSelectListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException("parent must implement AccountSelectedListener");
        }
    }
}
