package com.stuffer.stuffers.fragments.finance_fragment.media;

import java.util.ArrayList;

public interface imageSelectListener {
    void onImageSelect(PicHolder holder, int position, ArrayList<pictureFacer> pics);
    void onImageSelect(String pictureFolderPath,String folderName);
}
