package com.stuffer.stuffers.fragments.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.CustomCountryListener;
import com.stuffer.stuffers.models.output.CustomArea;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyTextView;

import java.util.ArrayList;

public class CustomCountryDialogFragment extends DialogFragment {

    private ArrayList<CustomArea> mCountryList;
    private EditText edSearch;
    private RecyclerView rvCountry;
    private MyButton btnOk;
    private CustomCountryAdapter countryAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_county_dialog, container, false);
        Bundle arguments = this.getArguments();
        mCountryList = arguments.getParcelableArrayList(AppoConstants.INFO);
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
        countryAdapter = new CustomCountryAdapter(getActivity(), mCountryList);
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

    public class CustomCountryAdapter extends RecyclerView.Adapter<CustomCountryAdapter.CustomCountryHolder> implements Filterable {
        Context mContext;
        ArrayList<CustomArea> mList;
        ArrayList<CustomArea> mListFilter;
        CustomCountryListener mCountryListener;

        public CustomCountryAdapter(Context context, ArrayList<CustomArea> list) {
            this.mContext = context;
            this.mList = list;
            this.mListFilter = list;
            try {
                this.mCountryListener = (CustomCountryListener) context;
            } catch (ClassCastException e) {
                throw new ClassCastException("Parent must implement CountrySelectListener");
            }
        }

        @NonNull
        @Override
        public CustomCountryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.row_country, parent, false);
            return new CustomCountryHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CustomCountryHolder holder, int position) {
            holder.tvCountryCode.setText(mListFilter.get(position).getAreacode_with_name());

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
                        ArrayList<CustomArea> tempFilterList = new ArrayList<>();
                        for (CustomArea mResult : mList) {
                            if (mResult.getAreacode_with_name().toLowerCase().contains(charSequence.toString().toLowerCase())) {
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
                    mListFilter = (ArrayList<CustomArea>) results.values;
                    notifyDataSetChanged();
                }
            };
        }

        public class CustomCountryHolder extends RecyclerView.ViewHolder {
            MyTextView tvCountryCode;

            public CustomCountryHolder(@NonNull View itemView) {
                super(itemView);
                tvCountryCode = itemView.findViewById(R.id.tvCountryCode);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Helper.hideKeyboard(edSearch, getActivity());
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            mCountryListener.onCustomCountryCodeSelect(mListFilter.get(getAdapterPosition()).getAreacode());
                            dismiss();
                        }

                    }
                });
            }
        }
    }


}
