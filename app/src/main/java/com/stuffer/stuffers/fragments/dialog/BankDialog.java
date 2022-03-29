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
import com.stuffer.stuffers.communicator.BankSelectListener;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyRadioButton;
import com.stuffer.stuffers.views.MyTextView;

import java.util.ArrayList;

public class BankDialog extends DialogFragment {

        private RecyclerView rvCarrier;
        private MyButton btnClose;
        private ArrayList<String> mListCarrier;
        private MyTextView tvTitle;
        private String mTitle;
        private MyButton btnConfirm;
        private boolean allow = false;
        private BankSelectListener mListener;
        private int lastSelectedPosition = -1;


        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.layout_dialog_insurance, container, false);
            rvCarrier = (RecyclerView) view.findViewById(R.id.rvCarrier);
            btnClose = (MyButton) view.findViewById(R.id.btnClose);
            tvTitle = (MyTextView) view.findViewById(R.id.tvTitle);
            btnConfirm = (MyButton) view.findViewById(R.id.btnConfirm);

            Bundle arguments = this.getArguments();
            mListCarrier = arguments.getStringArrayList(AppoConstants.INFO);
            mTitle = arguments.getString(AppoConstants.TITLE);
            tvTitle.setText(mTitle);
            rvCarrier.setLayoutManager(new LinearLayoutManager(getContext()));
            BankAdapter adapter = new BankAdapter(getContext(), mListCarrier);
            rvCarrier.setAdapter(adapter);

            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (allow) {
                        mListener.onBankSelect(lastSelectedPosition);
                    } else {
                        Toast.makeText(getContext(), "Please select your currency ", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            return view;
        }


        public class BankAdapter extends RecyclerView.Adapter<BankAdapter.BankHolder> {
            private Context mContext;
            private ArrayList<String> mList;

            private BankSelectListener mListener;

            public BankAdapter(Context mContext, ArrayList<String> mList) {
                this.mContext = mContext;
                this.mList = mList;
                try {
                    this.mListener = (BankSelectListener) mContext;
                } catch (ClassCastException e) {
                    throw new ClassCastException("parent should implement BankSelectListener");
                }
            }


            @NonNull
            @Override
            public BankHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.row_carrier_item, parent, false);
                return new BankHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull BankHolder holder, int position) {
                holder.bindProcess();
            }


            @Override
            public int getItemCount() {
                return mList.size();
            }

            public class BankHolder extends RecyclerView.ViewHolder {
                MyRadioButton rbtnCarrier;

                public BankHolder(@NonNull View itemView) {
                    super(itemView);
                    rbtnCarrier = itemView.findViewById(R.id.rbtnCarrier);
                    rbtnCarrier.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            lastSelectedPosition = getAdapterPosition();
                            allow = true;
                            notifyDataSetChanged();


                        }
                    });
                }

                public void bindProcess() {
                    rbtnCarrier.setChecked(lastSelectedPosition == getAdapterPosition());
                    rbtnCarrier.setText(mList.get(getAdapterPosition()));
                }
            }
        }

        @Override
        public void onAttach(@NonNull Context context) {
            super.onAttach(context);
            try {
                this.mListener = (BankSelectListener) context;
            } catch (ClassCastException e) {
                throw new ClassCastException("parent should implement BankSelectListener");
            }

        }
    }


