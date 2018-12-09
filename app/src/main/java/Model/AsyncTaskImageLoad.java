package Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsyncTaskImageLoad extends AsyncTask<String, Integer, Bitmap> {
    private ImageView Image=null;
    public AsyncTaskImageLoad(ImageView img)
    {
        Image=img;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        try
        {
            URL url=new URL("http://172.20.10.2:8080/android_http_servers"+params[0]);
            HttpURLConnection conn=(HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            if(conn.getResponseCode()==200)
            {
                InputStream input=conn.getInputStream();
                Bitmap map=BitmapFactory.decodeStream(input);
                return map;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(Bitmap result)
    {
        if(Image!=null && result!=null)
        {
            Image.setImageBitmap(result);
        }
        super.onPostExecute(result);
    }
}
