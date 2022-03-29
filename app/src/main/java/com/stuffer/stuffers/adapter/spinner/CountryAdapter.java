package com.stuffer.stuffers.adapter.spinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.models.Country.Result;
import com.stuffer.stuffers.views.MyTextView;

import java.util.List;

public class CountryAdapter extends BaseAdapter {
    Context mContext;
    List<Result> mList;
    public CountryAdapter(Context context, List<Result> list) {
        this.mContext = context;
        this.mList=list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view1 = LayoutInflater.from(mContext).inflate(R.layout.row_country,parent,false);
        MyTextView tvCountryCode=view1.findViewById(R.id.tvCountryCode);

        tvCountryCode.setText(mList.get(position).getCountryname());
        return view1;
    }
}
