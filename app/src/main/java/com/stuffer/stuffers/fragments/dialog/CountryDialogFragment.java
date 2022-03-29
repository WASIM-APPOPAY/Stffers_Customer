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
import com.stuffer.stuffers.models.Country.Result;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyTextView;

import java.util.ArrayList;

public class CountryDialogFragment extends DialogFragment {
    MyEditText edSearch;
    RecyclerView rvCountry;
    MyButton btnOk;
    ArrayList<Result> mCountry;
    private CountryAdapter countryAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_county_dialog, container, false);
        Bundle arguments = this.getArguments();
        mCountry = arguments.getParcelableArrayList(AppoConstants.COUNTRY);
        edSearch = view.findViewById(R.id.edSearch);
        rvCountry = view.findViewById(R.id.rvCountry);
        btnOk = view.findViewById(R.id.btnOk);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        rvCountry.setLayoutManager(new LinearLayoutManager(getContext()));
        countryAdapter = new CountryAdapter(getActivity(), mCountry);
        rvCountry.setAdapter(countryAdapter);

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
                countryAdapter.getFilter().filter(charSequence);
            }
        });
        return view;
    }

    public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountyHolder> implements Filterable {
        Context mContext;
        ArrayList<Result> mList;
        ArrayList<Result> mListFilter;
        CountrySelectListener mCountryListener;

        public CountryAdapter(Context context, ArrayList<Result> list) {
            this.mContext = context;
            this.mList = list;
            this.mListFilter = list;
            try {
                this.mCountryListener = (CountrySelectListener) context;
            } catch (ClassCastException e) {
                throw new ClassCastException("Parent must implement CountrySelectListener");
            }
        }

        @NonNull
        @Override
        public CountyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.row_country, parent, false);
            return new CountyHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CountyHolder holder, int position) {
            holder.tvCountryCode.setText(mListFilter.get(position).getCountryname());
            holder.tvCountryCode.setHint(mListFilter.get(position).getCountrycode());
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
                        ArrayList<Result> tempFilterList = new ArrayList<>();
                        for (Result mResult : mList) {
                            if (mResult.getCountryname().toLowerCase().contains(charSequence.toString().toLowerCase())) {
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
                    mListFilter = (ArrayList<Result>) results.values;
                    notifyDataSetChanged();
                }
            };
        }

        public class CountyHolder extends RecyclerView.ViewHolder {
            MyTextView tvCountryCode;

            public CountyHolder(@NonNull View itemView) {
                super(itemView);
                tvCountryCode = itemView.findViewById(R.id.tvCountryCode);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Helper.hideKeyboard(edSearch,mContext);
                        mCountryListener.onCountrySelected(mListFilter.get(getAdapterPosition()).getCountryname(), mListFilter.get(getAdapterPosition()).getCountrycode(), mListFilter.get(getAdapterPosition()).getId(), getAdapterPosition());
                        dismiss();
                    }
                });
            }


        }

    }


}
