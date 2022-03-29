package com.stuffer.stuffers.fragments.finance_fragment.media;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.stuffer.stuffers.R;

import java.util.ArrayList;


public class DisplayImageFragment extends Fragment {

    View mView;
    private String foldePath;
    RecyclerView imageRecycler;
    ArrayList<pictureFacer> allpictures;
    ProgressBar load;

    TextView folderName;

    public DisplayImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_display_image, container, false);
        Bundle arguments = this.getArguments();
        //folderName.setText(arguments.getString("folderName"));
        foldePath = arguments.getString("folderPath");
        allpictures = new ArrayList<>();
        imageRecycler = mView.findViewById(R.id.recycler);
        imageRecycler.addItemDecoration(new MarginDecoration(getActivity()));
        imageRecycler.hasFixedSize();
        load = mView.findViewById(R.id.loader);


        if(allpictures.isEmpty()){
            load.setVisibility(View.VISIBLE);
            allpictures = getAllImagesByFolder(foldePath);
            //imageRecycler.setAdapter(new picture_Adapter(allpictures,getActivity(),this));
            imageRecycler.setAdapter(new picture_Adapter(allpictures,getActivity(),getActivity()));
            load.setVisibility(View.GONE);
        }else{

        }
        return mView;
    }
    public ArrayList<pictureFacer> getAllImagesByFolder(String path){
        ArrayList<pictureFacer> images = new ArrayList<>();
        Uri allVideosuri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.Images.ImageColumns.DATA ,MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE};
        Cursor cursor = getActivity().getContentResolver().query( allVideosuri, projection, MediaStore.Images.Media.DATA + " like ? ", new String[] {"%"+path+"%"}, null);
        try {
            cursor.moveToFirst();
            do{
                pictureFacer pic = new pictureFacer();

                pic.setPicturName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)));

                pic.setPicturePath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));

                pic.setPictureSize(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)));

                images.add(pic);
            }while(cursor.moveToNext());
            cursor.close();
            ArrayList<pictureFacer> reSelection = new ArrayList<>();
            for(int i = images.size()-1;i > -1;i--){
                reSelection.add(images.get(i));
            }
            images = reSelection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return images;
    }

}