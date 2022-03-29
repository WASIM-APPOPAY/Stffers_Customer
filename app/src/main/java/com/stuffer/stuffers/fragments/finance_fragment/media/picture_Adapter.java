package com.stuffer.stuffers.fragments.finance_fragment.media;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.stuffer.stuffers.R;

import java.util.ArrayList;

import static androidx.core.view.ViewCompat.setTransitionName;

public class picture_Adapter extends RecyclerView.Adapter<PicHolder> {

    private ArrayList<pictureFacer> pictureList;
    private Context pictureContx;
    private final imageSelectListener picListerner;

    /**
     *
     * @param pictureList ArrayList of pictureFacer objects
     * @param pictureContx The Activities Context
     * //@param picListerner An interface for listening to clicks on the RecyclerView's items
     */
    public picture_Adapter(ArrayList<pictureFacer> pictureList, Context pictureContx, FragmentActivity picListerner) {
        this.pictureList = pictureList;
        this.pictureContx = pictureContx;
        this.picListerner = (imageSelectListener) picListerner;
    }
    /*public picture_Adapter(ArrayList<pictureFacer> pictureList, Context pictureContx) {
        this.pictureList = pictureList;
        this.pictureContx = pictureContx;

    }*/

    @NonNull
    @Override
    public PicHolder onCreateViewHolder(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View cell = inflater.inflate(R.layout.pic_holder_item, container, false);
        return new PicHolder(cell);
    }

    @Override
    public void onBindViewHolder(@NonNull final PicHolder holder, final int position) {

        final pictureFacer image = pictureList.get(position);

        Glide.with(pictureContx)
                .load(image.getPicturePath())
                .apply(new RequestOptions().centerCrop())
                .into(holder.picture);

        setTransitionName(holder.picture, String.valueOf(position) + "_image");

        holder.picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picListerner.onImageSelect(holder,position, pictureList);
            }
        });

    }

    @Override
    public int getItemCount() {
        return pictureList.size();
    }
}
