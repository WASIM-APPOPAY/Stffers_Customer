package com.stuffer.stuffers.adapter.movie;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.models.output.TheaterListOutput;
import com.stuffer.stuffers.views.ExpandableHeightGridView;
import com.stuffer.stuffers.views.MyTextView;

import java.util.ArrayList;

public class TheaterListAdapter extends RecyclerView.Adapter<TheaterListAdapter.MyViewHolder> {

    ArrayList<TheaterListOutput> theaterListOutputArrayList;
    AppCompatActivity appCompatActivity;


    public TheaterListAdapter(ArrayList<TheaterListOutput> theaterListOutputArrayList, AppCompatActivity appCompatActivity) {
        this.theaterListOutputArrayList = theaterListOutputArrayList;
        this.appCompatActivity = appCompatActivity;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public MyTextView theaterName;
        public MyTextView theaterDistance;
        public ExpandableHeightGridView showTimesGrid;


        public MyViewHolder(View view) {
            super(view);

            theaterName = (MyTextView) view.findViewById(R.id.theaterName);
            theaterDistance = (MyTextView) view.findViewById(R.id.theaterDistance);
            showTimesGrid = (ExpandableHeightGridView) view.findViewById(R.id.showTimesGrid);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.theater_list_item, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        TheaterListOutput theaterListOutput = theaterListOutputArrayList.get(position);
        holder.theaterName.setText(theaterListOutput.getTheaterName());
        holder.theaterDistance.setText(theaterListOutput.getTheaterDistance());

        ShowTimesGridAdapter showTimesGridAdapter = new ShowTimesGridAdapter(theaterListOutput.getShowTimeArrayList(), appCompatActivity);

        holder.showTimesGrid.setAdapter(showTimesGridAdapter);
        showTimesGridAdapter.notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return theaterListOutputArrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}
