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
import com.stuffer.stuffers.communicator.DestinationListener;
import com.stuffer.stuffers.models.Country.Result;
import com.stuffer.stuffers.models.output.DetinationCurrency;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyTextView;

import java.util.ArrayList;

public class DestinationDialog extends DialogFragment {
    MyEditText edSearch;
    RecyclerView rvCountry;
    MyButton btnOk;
    ArrayList<DetinationCurrency.Result> mCountry;
    private DestinationAdapter mDestinationAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_county_dialog, container, false);
        Bundle arguments = this.getArguments();
        mCountry = arguments.getParcelableArrayList(AppoConstants.COUNTRY);
        edSearch = view.findViewById(R.id.edSearch);
        edSearch.setHint("Destination Country");
        rvCountry = view.findViewById(R.id.rvCountry);
        btnOk = view.findViewById(R.id.btnOk);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        rvCountry.setLayoutManager(new LinearLayoutManager(getContext()));
        mDestinationAdapter = new DestinationAdapter(getActivity(), mCountry);
        rvCountry.setAdapter(mDestinationAdapter);

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
                mDestinationAdapter.getFilter().filter(charSequence);
            }
        });
        return view;
    }

    public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.DestinationHolder> implements Filterable {
        Context mContext;
        ArrayList<DetinationCurrency.Result> mList;
        ArrayList<DetinationCurrency.Result> mListFilter;
        DestinationListener mListener;

        public DestinationAdapter(Context context, ArrayList<DetinationCurrency.Result> list) {
            this.mContext = context;
            this.mList = list;
            this.mListFilter = list;
            try {
                this.mListener = (DestinationListener) context;
            } catch (ClassCastException e) {
                throw new ClassCastException("Parent must implement CountrySelectListener");
            }
        }

        @NonNull
        @Override
        public DestinationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.row_destination, parent, false);
            return new DestinationHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DestinationHolder holder, int position) {
            holder.tvCountryCode.setText(mListFilter.get(position).getCountry());
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
                        ArrayList<DetinationCurrency.Result> tempFilterList = new ArrayList<>();
                        for (DetinationCurrency.Result mResult : mList) {
                            if (mResult.getCountry().toLowerCase().contains(charSequence.toString().toLowerCase())) {
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
                    mListFilter = (ArrayList<DetinationCurrency.Result>) results.values;
                    notifyDataSetChanged();
                }
            };
        }

        public class DestinationHolder extends RecyclerView.ViewHolder {
            MyTextView tvCountryCode;

            public DestinationHolder(@NonNull View itemView) {
                super(itemView);
                tvCountryCode = itemView.findViewById(R.id.tvCountryCode);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Helper.hideKeyboard(edSearch, mContext);
                        mListener.onDestinationSelect(mListFilter.get(getAdapterPosition()).getRegion(),
                                mListFilter.get(getAdapterPosition()).getCountry(),
                                mListFilter.get(getAdapterPosition()).getPayoutCurrency());
                        dismiss();
                    }
                });
            }
        }

    }
}
