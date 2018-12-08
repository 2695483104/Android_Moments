package Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.colin.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MomentAdapter extends ArrayAdapter {

    private int resourceID;
    private ListView listView;
    private LruCache<String, BitmapDrawable> imageCache;
    public MomentAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        resourceID = resource;
        //设置LruCache缓存为最大缓存的1／8；
        int maxCache = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxCache / 8;
        imageCache = new LruCache<String, BitmapDrawable>(cacheSize) {
            @Override
            protected int sizeOf(String key, BitmapDrawable value) {
                return value.getBitmap().getByteCount();
            }
        };
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        View view = LayoutInflater.from(getContext()).inflate(resourceID, parent,false);
//        TextView textView = view.findViewById(R.id.momentText);

        if (listView == null) {
            listView = (ListView) parent;
        }

        View view;
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceID,parent,false);
            viewHolder.icon = view.findViewById(R.id.icon);
            viewHolder.text = view.findViewById(R.id.momentText);
            viewHolder.image1 = view.findViewById(R.id.image1);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }



        //获取每条朋友圈JSON数据
        JSONObject moment = (JSONObject) getItem(position);

        //从JSON取数据
        String iconURL = null;
        String text = null;
//        String image1URL = null;
        try {
            iconURL = moment.getString("icon");
            text = moment.getString("text");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //将view与数据绑定
        viewHolder.text.setText(text);
        viewHolder.icon.setTag(iconURL);
        // 图片--如果本地已有缓存，就从本地读取，否则从网络请求数据
        if (imageCache.get(iconURL) != null) {
            viewHolder.icon.setImageDrawable(imageCache.get(iconURL));
        } else {
            ImageTask it = new ImageTask();
            it.execute(iconURL);
        }
//        viewHolder.icon.post()

        return view;
    }
    class ViewHolder{
        ImageView icon;
        TextView text;
        ImageView image1;
    }


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
    class ImageTask extends AsyncTask<String, Void, BitmapDrawable> {

        private String imageUrl;

        @Override
        protected BitmapDrawable doInBackground(String... params) {
            imageUrl = params[0];
            Bitmap bitmap = downloadImage();
            BitmapDrawable db = new BitmapDrawable(listView.getResources(),
                    bitmap);
            // 如果本地还没缓存该图片，就缓存
            if (imageCache.get(imageUrl) == null) {
                imageCache.put(imageUrl, db);
            }
            return db;
        }

        @Override
        protected void onPostExecute(BitmapDrawable result) {
            // 通过Tag找到我们需要的ImageView，如果该ImageView所在的item已被移出页面，就会直接返回null
            ImageView iv = (ImageView) listView.findViewWithTag(imageUrl);
            if (iv != null && result != null) {
                iv.setImageDrawable(result);
            }
        }

        /**
         * 根据url从网络上下载图片
         *
         * @return
         */
        private Bitmap downloadImage() {
            HttpURLConnection con = null;
            Bitmap bitmap = null;
            try {
                URL url = new URL("http://172.20.10.2:8080/android_http_servers"+imageUrl);
                con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(5 * 1000);
                con.setReadTimeout(10 * 1000);
                bitmap = BitmapFactory.decodeStream(con.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (con != null) {
                    con.disconnect();
                }
            }
            return bitmap;
        }

    }


}

















