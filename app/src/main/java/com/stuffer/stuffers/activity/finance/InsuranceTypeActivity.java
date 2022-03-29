package com.stuffer.stuffers.activity.finance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.fragments.dialog.CountryDialogFragment;
import com.stuffer.stuffers.models.Country.Result;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyTextView;

import java.util.ArrayList;

public class InsuranceTypeActivity extends AppCompatActivity {

    private ImageView ivBack;
    private RecyclerView rvInsurance;
    private ArrayList<String> stringArrayListExtra;
    MyEditText edSearch;
    private InsuranceAdapter countryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_type);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        rvInsurance = (RecyclerView) findViewById(R.id.rvInsurance);
        edSearch = findViewById(R.id.edSearch);
        stringArrayListExtra = getIntent().getStringArrayListExtra(AppoConstants.INFO);
        rvInsurance.setLayoutManager(new LinearLayoutManager(InsuranceTypeActivity.this));
        countryAdapter = new InsuranceAdapter(InsuranceTypeActivity.this, stringArrayListExtra);
        rvInsurance.setAdapter(countryAdapter);

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
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public class InsuranceAdapter extends RecyclerView.Adapter<InsuranceAdapter.InsuranceHolder> implements Filterable {
        Context mContext;
        ArrayList<String> mList;
        ArrayList<String> mListFilter;

        public InsuranceAdapter(Context mContext, ArrayList<String> mList) {
            this.mContext = mContext;
            this.mList = mList;
            this.mListFilter = mList;
        }

        @NonNull
        @Override
        public InsuranceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.row_country, parent, false);
            return new InsuranceHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull InsuranceHolder holder, int position) {
            holder.tvInsuranceType.setText(mListFilter.get(position));
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
                        ArrayList<String> tempFilterList = new ArrayList<>();
                        for (String mResult : mList) {
                            if (mResult.toLowerCase().contains(charSequence.toString().toLowerCase())) {
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
                    mListFilter = (ArrayList<String>) results.values;
                    notifyDataSetChanged();
                }
            };
        }

        public class InsuranceHolder extends RecyclerView.ViewHolder {
            MyTextView tvInsuranceType;

            public InsuranceHolder(@NonNull View itemView) {
                super(itemView);
                tvInsuranceType = itemView.findViewById(R.id.tvCountryCode);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        String s = mListFilter.get(getAdapterPosition());
                        Log.e("TAG", "onClick: "+s );
                        intent.putExtra(AppoConstants.INSURANCE_TYPE, s);
                        setResult(Activity.RESULT_OK,intent);
                        finish();
                    }
                });
            }
        }
    }
}