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
import com.stuffer.stuffers.communicator.RecyclerViewRowItemCLickListener;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyRadioButton;

import java.util.ArrayList;

public class FromAccountDialogFragment extends DialogFragment {

    private RecyclerView rvActiveAccount;
    private MyButton btnOk, btnCancel;
    private ArrayList<String> mListAccounts;
    private boolean allow = false;
    private ConfirmSelectListener mConfirmSelectListener;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_from_account, container, false);
        rvActiveAccount = (RecyclerView) view.findViewById(R.id.rvActiveAccount);
        btnOk = (MyButton) view.findViewById(R.id.btnOk);
        btnCancel = (MyButton) view.findViewById(R.id.btnCancel);
        rvActiveAccount.setLayoutManager(new LinearLayoutManager(getContext()));
        Bundle arguments = this.getArguments();
        mListAccounts = arguments.getStringArrayList(AppoConstants.INFO);
        FromAccountAdapter adapter = new FromAccountAdapter(getActivity(), mListAccounts);
        rvActiveAccount.setAdapter(adapter);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allow) {
                    mConfirmSelectListener.onConfirmSelect();
                } else {
                    Toast.makeText(getContext(), "Please select from account", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
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
        mConfirmSelectListener = (ConfirmSelectListener) context;

    }

    public class FromAccountAdapter extends RecyclerView.Adapter<FromAccountAdapter.FromAccountHolder> {
        private ArrayList<String> mList;
        private Context mContext;
        private RecyclerViewRowItemCLickListener mListener;
        private int lastSelectedPosition = -1;

        public FromAccountAdapter(Context context, ArrayList<String> list) {
            this.mContext = context;
            this.mList = list;
            try {
                this.mListener = (RecyclerViewRowItemCLickListener) context;
            } catch (ClassCastException e) {
                throw new ClassCastException("parent must implement RecyclerViewRowItemCLickListener");
            }
        }

        @NonNull
        @Override
        public FromAccountHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.row_from_account_items, parent, false);
            return new FromAccountHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FromAccountHolder holder, int position) {
            holder.bindProcess();
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        public class FromAccountHolder extends RecyclerView.ViewHolder {
            MyRadioButton rbtnAccount;

            public FromAccountHolder(@NonNull View itemView) {
                super(itemView);
                rbtnAccount = itemView.findViewById(R.id.rbtnAccount);
                rbtnAccount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lastSelectedPosition = getAdapterPosition();
                        notifyDataSetChanged();
                        allow = true;
                        mListener.onRowItemClick(lastSelectedPosition);
                    }
                });
            }

            public void bindProcess() {
                rbtnAccount.setChecked(lastSelectedPosition == getAdapterPosition());
                rbtnAccount.setText(mList.get(getAdapterPosition()));
            }
        }
    }
}
