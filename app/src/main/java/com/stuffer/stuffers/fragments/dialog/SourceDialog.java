package com.stuffer.stuffers.fragments.dialog;

import android.content.Context;
import android.os.Bundle;
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
import com.stuffer.stuffers.communicator.CountrySelectListener;
import com.stuffer.stuffers.communicator.IncomeListener;
import com.stuffer.stuffers.models.Country.Result;
import com.stuffer.stuffers.models.output.SourceIncome;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyTextView;

import java.util.ArrayList;

public class SourceDialog extends DialogFragment {
    MyEditText edSearch;
    RecyclerView rvSource;
    MyButton btnOk;
    ArrayList<SourceIncome> mListSource;

    private View mView;
    SourceAdapter sourceAdapter;
    private String mTitle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.layout_source_income, container, false);
        Bundle arguments = this.getArguments();
        mListSource = arguments.getParcelableArrayList(AppoConstants.SOURCE_OF_INCOME);
        mTitle = arguments.getString(AppoConstants.TITLE);
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

        sourceAdapter = new SourceAdapter(getActivity(), mListSource);
        rvSource.setAdapter(sourceAdapter);

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
                sourceAdapter.getFilter().filter(charSequence);

            }
        });

        return mView;
    }

    public class SourceAdapter extends RecyclerView.Adapter<SourceAdapter.SourceHolder> implements Filterable {
        Context mContext;
        ArrayList<SourceIncome> mList;
        ArrayList<SourceIncome> mListFilter;
        IncomeListener mIncomeListener;

        public SourceAdapter(Context mContext, ArrayList<SourceIncome> mList) {
            this.mContext = mContext;
            this.mList = mList;
            this.mListFilter = mList;
            try {
                this.mIncomeListener = (IncomeListener) mContext;
            } catch (ClassCastException e) {
                throw new ClassCastException("Parent must implement CountrySelectListener");
            }
        }

        @NonNull
        @Override
        public SourceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.row_common, parent, false);
            return new SourceHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SourceHolder holder, int position) {
            holder.tvCommonView.setText(mListFilter.get(position).getName());
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
                        ArrayList<SourceIncome> tempFilterList = new ArrayList<>();
                        for (SourceIncome mResult : mList) {
                            if (mResult.getName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
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
                    mListFilter = (ArrayList<SourceIncome>) results.values;
                    notifyDataSetChanged();
                }
            };
        }

        public class SourceHolder extends RecyclerView.ViewHolder {
            MyTextView tvCommonView;

            public SourceHolder(@NonNull View itemView) {
                super(itemView);
                tvCommonView = itemView.findViewById(R.id.tvCommonView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Helper.hideKeyboard(edSearch, mContext);
                        mIncomeListener.onIncomeSelected(mListFilter.get(getAdapterPosition()).getName(),
                                mListFilter.get(getAdapterPosition()).getId());
                        dismiss();
                    }
                });
            }
        }
    }
}
