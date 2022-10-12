package com.stuffer.stuffers.commonChat.chatUtils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;


public class PictureSelectUtils {

    public static final int  GET_BY_ALBUM  = 0x11;
    public static final int  GET_BY_CAMERA = 0x12;
    private static Uri  takePictureUri;
    private static File takePictureFile;

    /**
     * 通过相册获取图片
     */
    public static void getByAlbum(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        activity.startActivityForResult(intent, GET_BY_ALBUM);
    }

    /**
     * 通过拍照获取图片
     */
    public static void getByCamera(Activity activity) {
        takePictureUri = createImagePathUri(activity);
        if (takePictureUri != null) {
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            i.putExtra(MediaStore.EXTRA_OUTPUT, takePictureUri);
            activity.startActivityForResult(i, GET_BY_CAMERA);
        } else {
            Toast.makeText(activity, "打开相机失败", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 创建一个图片地址uri,用于保存拍照后的照片
     *
     * @param activity
     * @return 图片的uri
     */
    public static Uri createImagePathUri(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            String displayName = String.valueOf(System.currentTimeMillis());
            ContentValues values = new ContentValues(2);
            values.put(MediaStore.Images.Media.DISPLAY_NAME, displayName);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                takePictureUri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                takePictureUri = activity.getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, values);
            }
        } else {
            String pathName = new StringBuffer().append(FileUtils.getExtPicturesPath()).append(File.separator)
                    .append(System.currentTimeMillis()).append(".jpg").toString();
            takePictureFile = new File(pathName);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                String authority = activity.getPackageName() + ".fileProvider";
                takePictureUri = FileProvider.getUriForFile(activity, authority, takePictureFile);
            } else {
                takePictureUri = Uri.fromFile(takePictureFile);
            }
        }
        return takePictureUri;
    }

    public static String onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        String picturePath = null;
        if (resultCode == activity.RESULT_OK) {
            Uri uri = null;
            switch (requestCode) {
                case GET_BY_ALBUM:
                    uri = data.getData();
                    picturePath = ImageUtils.getImagePath(activity, uri);
                    break;
                case GET_BY_CAMERA:
                    uri = takePictureUri;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        picturePath = ImageUtils.getImagePath(activity, uri);
                    } else {
                        picturePath = takePictureFile.getAbsolutePath();
                    }
                    /*Android Q 以下发送广播通知图库更新，Android Q 以上使用 insert 的方式则会自动更新*/
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                        activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(takePictureFile)));
                    }
                    break;
                default:
                    break;
            }
        }
        return picturePath;
    }
}