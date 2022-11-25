package in.kassapos.a1broilers.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.InputStream;

import in.kassapos.a1broilers.image.FileCache;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private static  FileCache fileCache;
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage, Context context) {
        this.bmImage = bmImage;
        if(fileCache==null) {
            fileCache = new FileCache(context);
        }
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        File f=fileCache.getFile(urldisplay);
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}