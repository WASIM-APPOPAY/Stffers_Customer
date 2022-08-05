package com.stuffer.stuffers.fragments.dialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.IncomeListener;
import com.stuffer.stuffers.communicator.PurposeListener;
import com.stuffer.stuffers.fragments.dialog.SourceDialog;
import com.stuffer.stuffers.models.output.SourceIncome;
import com.stuffer.stuffers.models.output.TransferPurpose;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyTextView;

import java.util.ArrayList;

public class PurposeDialog extends DialogFragment {
    MyEditText edSearch;
    RecyclerView rvSource;
    MyButton btnOk;
    ArrayList<TransferPurpose> mListPurpose;

    private View mView;
    PurposeAdapter purposeAdapter;
    private String mTitle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.layout_source_income, container, false);
        Bundle arguments = this.getArguments();
        mListPurpose = arguments.getParcelableArrayList(AppoConstants.PURPOSEOFTRANSFER);
        mTitle=arguments.getString(AppoConstants.TITLE);
        edSearch = mView.findViewById(R.id.edSearch);
        rvSource = mView.findViewById(R.id.rvSource);
        btnOk = mView.findViewById(R.id.btnOk);
        edSearch.setHint(mTitle);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        rvSource.setLayoutManager(new LinearLayoutManager(getContext()));
        purposeAdapter = new PurposeAdapter(getActivity(), mListPurpose);
        rvSource.setAdapter(purposeAdapter);
        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                //countryAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable charSequence) {
                purposeAdapter.getFilter().filter(charSequence);

            }
        });


        return mView;
    }

    public class PurposeAdapter extends RecyclerView.Adapter<PurposeAdapter.PurposeHolder> implements Filterable {
        Context mContext;
        ArrayList<TransferPurpose> mList;
        ArrayList<TransferPurpose> mListFilter;
        PurposeListener mPurposeListener;

        public PurposeAdapter(Context mContext, ArrayList<TransferPurpose> mList) {
            this.mContext = mContext;
            this.mList = mList;
            this.mListFilter = mList;
            try {
                this.mPurposeListener = (PurposeListener) mContext;
            } catch (ClassCastException e) {
                throw new ClassCastException("Parent must implement CountrySelectListener");
            }
        }

        @NonNull
        @Override
        public PurposeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.row_common, parent, false);
            return new PurposeHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PurposeHolder holder, int position) {
            holder.tvCommonView.setText(mListFilter.get(position).getPurposeOfTransfer());
        }

        @Override
        public int getItemCount() {
            return mListFilter.size();
        }


        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        mListFilter = mList;
                    } else {
                        ArrayList<TransferPurpose> tempFilterList = new ArrayList<>();
                        for (TransferPurpose mResult : mList) {
                            if (mResult.getPurposeOfTransfer().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                                tempFilterList.add(mResult);
                            }

                        }
                        mListFilter = tempFilterList;

                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = mListFilter;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    mListFilter = (ArrayList<TransferPurpose>) results.values;
                    notifyDataSetChanged();
                }
            };
        }

        public class PurposeHolder extends RecyclerView.ViewHolder {
            MyTextView tvCommonView;

            public PurposeHolder(@NonNull View itemView) {
                super(itemView);
                tvCommonView = itemView.findViewById(R.id.tvCommonView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Helper.hideKeyboard(edSearch, mContext);
                        mPurposeListener.onPurposeConfirm(mListFilter.get(getAdapterPosition()).getPurposeOfTransfer(),
                                mListFilter.get(getAdapterPosition()).getPurposeOfTransferId());
                        dismiss();
                    }
                });
            }
        }
    }
}
