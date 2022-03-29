package com.stuffer.stuffers.activity.wallet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.BankSelectedListener;
import com.stuffer.stuffers.models.bank.Result;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyTextView;

import java.util.ArrayList;
import java.util.List;

public class BankNameActivity extends AppCompatActivity implements BankSelectedListener {
    private static final String TAG = "BankNameActivity";
    RecyclerView rvBanks;
    MyEditText edSearch;
    ImageView ivBankBack;
    List<com.stuffer.stuffers.models.bank.Result> mListBank;
    private BankNameAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_name);
        ivBankBack = findViewById(R.id.ivBankBack);
        edSearch = findViewById(R.id.edSearch);
        rvBanks = findViewById(R.id.rvBanks);
        rvBanks.setLayoutManager(new LinearLayoutManager(this));
        mListBank = getIntent().getExtras().getParcelableArrayList(AppoConstants.INFO);
        //Log.e(TAG, "onCreate: size :: " + mListBank.size());
        adapter = new BankNameAdapter(BankNameActivity.this, mListBank);
        rvBanks.setAdapter(adapter);
        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable charSequence) {
                adapter.getFilter().filter(charSequence);
            }
        });

        ivBankBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onBankSelected(int id, String bankname, String bankcode) {
        //Log.e(TAG, "onBankSelected: id :: " + id);
        //Log.e(TAG, "onBankSelected: bankname :: " + bankname);
        //Log.e(TAG, "onBankSelected: bankcode :: " + bankcode);
        Helper.hideKeyboard(edSearch, BankNameActivity.this);
        Intent intent = new Intent();
        intent.putExtra(AppoConstants.ID, id);
        intent.putExtra(AppoConstants.BANKNAME, bankname);
        intent.putExtra(AppoConstants.BANKCODE, bankcode);
        setResult(Activity.RESULT_OK, intent);
        finish();


    }

    public static class BankNameAdapter extends RecyclerView.Adapter<BankNameAdapter.BankNameHolder> implements Filterable {
        private Context mContext;
        List<Result> mListBank;
        List<Result> mListFilter;
        BankSelectedListener mBankListener;

        BankNameAdapter(Context mContext, List<Result> mListBank) {
            this.mContext = mContext;
            this.mListBank = mListBank;
            this.mListFilter = mListBank;
            try {
                this.mBankListener = (BankSelectedListener) mContext;
            } catch (ClassCastException e) {
                throw new ClassCastException("parent must implement BankSelectedListener");
            }
        }

        @NonNull
        @Override
        public BankNameHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.row_item_banks, parent, false);
            return new BankNameHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BankNameHolder holder, int position) {
            holder.bind();
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
                        mListFilter = mListBank;
                    } else {
                        ArrayList<Result> tempFilterList = new ArrayList<>();
                        for (Result mResult : mListBank) {
                            if (mResult.getBankname().toLowerCase().contains(charSequence.toString().toLowerCase())) {
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

        class BankNameHolder extends RecyclerView.ViewHolder {

            MyTextView tvBankName;
            CardView cardBank;

            BankNameHolder(@NonNull View itemView) {
                super(itemView);
                tvBankName = itemView.findViewById(R.id.tvBankName);
                cardBank = itemView.findViewById(R.id.cardBank);
                cardBank.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBankListener.onBankSelected(mListFilter.get(getAdapterPosition()).getId(),
                                mListFilter.get(getAdapterPosition()).getBankname(),
                                mListFilter.get(getAdapterPosition()).getBankcode());
                    }
                });
            }

            void bind() {
                tvBankName.setText(mListFilter.get(getAdapterPosition()).getBankname());
            }
        }
    }


}
