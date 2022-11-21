package com.stuffer.stuffers.adapter.recyclerview;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.RecyclerViewRowItemClickListener2;
import com.stuffer.stuffers.models.ListCountry;
import com.stuffer.stuffers.views.MyTextView;


import java.util.ArrayList;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryHolder> {
    ArrayList<ListCountry> mListCountries;
    Activity mCtx;
    RecyclerViewRowItemClickListener2 mRowItemClickListener;

    public CountryAdapter(ArrayList<ListCountry> mListCountries, Activity mCtx) {
        this.mListCountries = mListCountries;
        this.mCtx = mCtx;
        mRowItemClickListener = (RecyclerViewRowItemClickListener2) mCtx;
    }

    @NonNull
    @Override
    public CountryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mCtx).inflate(R.layout.row_countries, parent, false);
        return new CountryHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return mListCountries.size();
    }

    public class CountryHolder extends RecyclerView.ViewHolder {

        ImageView ivCountry;
        MyTextView tvCountryName;

        public CountryHolder(@NonNull View itemView) {
            super(itemView);
            ivCountry = itemView.findViewById(R.id.ivCountry);
            tvCountryName = itemView.findViewById(R.id.tvCountryName);
            /*requestBuilder =
                    Glide.with(mCtx)

                            .as(PictureDrawable.class);*/

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mRowItemClickListener.onRowItemClick2(getAdapterPosition());
                }
            });
        }

        public void bind() {
            tvCountryName.setText(mListCountries.get(getAdapterPosition()).getName().toUpperCase());
            Glide.with(mCtx).load(mListCountries.get(getAdapterPosition()).getFlagPath()).into(ivCountry);


        }
    }
}
