package Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * AsyncTask<Params,Progress,Result>
 * Params:启动任务时输入的参数类型.
 * Progress:后台任务执行中返回进度值的类型.
 * Result:后台任务执行完成后返回结果的类型.
 *
 * doInBackground:必须重写,异步执行后台线程要完成的任务,耗时操作将在此方法中完成.
 * onPreExecute:执行后台耗时操作前被调用,通常用于进行初始化操作.
 * onPostExecute:当doInBackground方法完成后,系统将自动调用此方法,并将doInBackground方法返回的值传入此方法.通过此方法进行UI的更新.
 * onProgressUpdate:当在doInBackground方法中调用publishProgress方法更新任务执行进度后,将调用此方法.通过此方法我们可以知晓任务的完成进度.
 */
public class AsyncTaskImageLoad extends AsyncTask<String, Integer, Bitmap> {
    private ImageView Image=null;
    private LruCache<String, Bitmap> imageCache = null;
    public AsyncTaskImageLoad(ImageView img, LruCache<String, Bitmap> imageCache)
    {
        Image=img;
        this.imageCache = imageCache;
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
                imageCache.put(params[0],map);//缓存图片
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
