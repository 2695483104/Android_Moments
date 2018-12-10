package Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.colin.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MomentAdapter extends ArrayAdapter {

    private int resourceID;
    private ListView listView;
    String id = null;
    private LruCache<String, Bitmap> imageCache;


    public MomentAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        resourceID = resource;
        //设置LruCache缓存为最大缓存的1／2；
        int maxCache = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxCache / 2;
        imageCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
Log.e("getView","---------------");
        if (listView == null) {
            listView = (ListView) parent;
        }

        View view;
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceID,parent,false);
            viewHolder.icon = view.findViewById(R.id.icon);
            viewHolder.text = view.findViewById(R.id.momentText);
            int[] R_id_images = {R.id.image1,R.id.image2,R.id.image3,R.id.image4,R.id.image5,R.id.image6,R.id.image7,R.id.image8,R.id.image9};
            for (int i=0;i<9;i++){
                viewHolder.images[i] = view.findViewById(R_id_images[i]);
//                viewHolder.images[i].setVisibility(View.GONE);
            }
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
        JSONArray imagesJSONArry = null;
        ArrayList<String> imagesList = new ArrayList<>();
//        String image1URL = null;
        try {
            iconURL = moment.getString("icon");
            id = text = moment.getString("text");
            imagesJSONArry = (JSONArray) moment.get("images");
//            images.get(0);//取图片地址
//            Log.i("images",imagesJSONArry.get(0).toString());
//            Log.i("imagesJSONArry.length",imagesJSONArry.length()+"");
            for (int i=0;i<imagesJSONArry.length();i++){
                String temp = imagesJSONArry.get(i).toString();
                imagesList.add(imagesJSONArry.get(i).toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 跟具图片数量设置图片排版
        for (int i=0;i<9;i++){
                viewHolder.images[i].setVisibility(View.GONE);
        }
        Log.i("imagesList Switch-",imagesList.size()+"");
        switch (imagesList.size()){
            case 9:
                viewHolder.images[8].setVisibility(View.VISIBLE);
            case 8:
                viewHolder.images[7].setVisibility(View.VISIBLE);
            case 7:
                viewHolder.images[6].setVisibility(View.VISIBLE);
            case 6:
                viewHolder.images[5].setVisibility(View.VISIBLE);
            case 5:
                viewHolder.images[4].setVisibility(View.VISIBLE);
            case 4:
                viewHolder.images[3].setVisibility(View.VISIBLE);
            case 3:
                viewHolder.images[2].setVisibility(View.VISIBLE);
//                ViewGroup.LayoutParams params3 = viewHolder.images[2].getLayoutParams();
//                Log.i("3-width",params3.width+"");
//                Log.i("3-height",params3.height+"");
                viewHolder.images[1].setVisibility(View.VISIBLE);
                viewHolder.images[0].setVisibility(View.VISIBLE);
                break;
            case 2:
                viewHolder.images[1].setVisibility(View.VISIBLE);
                ViewGroup.LayoutParams params1 = viewHolder.images[1].getLayoutParams();
//                Log.i("2-width",params1.width+"");
//                Log.i("2-height",params1.height+"");
                params1.width = 394;
                params1.height = 394;
//                viewHolder.images[1].setLayoutParams(params1);
            case 1:
                viewHolder.images[0].setVisibility(View.VISIBLE);
                ViewGroup.LayoutParams params2 = viewHolder.images[0].getLayoutParams();
//                Log.i("1-width",params2.width+"");
//                Log.i("1-height",params2.height+"");
                params2.width = 394;
                params2.height = 394;
//                Log.i("1-width-a",params2.width+"");
//                Log.i("1-height-a",params2.height+"");
//                viewHolder.images[1].setLayoutParams(params2);
                break;
            default:
                break;
        }

        //将view与数据绑定
        viewHolder.text.setText(text);
//        viewHolder.icon.setTag(text);
//        viewHolder.image1.setTag(imagesString[0]);
//        viewHolder.image2.setTag(imagesString[1]);
//        viewHolder.image3.setTag(imagesString[2]);
//        viewHolder.image4.setTag(imagesString[3]);
//        viewHolder.image5.setTag(imagesString[4]);
//        viewHolder.image6.setTag(imagesString[5]);
//        viewHolder.image7.setTag(imagesString[6]);
//        viewHolder.image8.setTag(imagesString[7]);
//        viewHolder.image9.setTag(imagesString[8]);

        // 图片--如果本地已有缓存，就从本地读取，否则从网络请求数据
        if (imageCache.get(iconURL) != null) {
//            viewHolder.icon.setImageDrawable(imageCache.get(iconURL));
            viewHolder.icon.setImageBitmap(imageCache.get(iconURL));
            Log.i("缓存中取图片",iconURL);
        } else {
            Log.i("下载图片",iconURL);
//            ImageTask it = new ImageTask();
//            it.execute(iconURL,text);
            LoadImage(viewHolder.icon,iconURL);
        }

        Log.i("加载多图",imagesList.size()+"");
        for (int i = 0;i < imagesList.size();i++){
            String multiImages = imagesList.get(i);
            if (imageCache.get(multiImages) != null) {
                viewHolder.images[i].setImageBitmap(imageCache.get(multiImages));
            } else {
                LoadImage(viewHolder.images[i],multiImages);
            }
        }


//        viewHolder.image1.setImageDrawable(imageCache.get(imagesString[0]));
//        viewHolder.image2.setImageDrawable(imageCache.get(imagesString[1]));
//        viewHolder.image3.setImageDrawable(imageCache.get(imagesString[2]));
//        viewHolder.image4.setImageDrawable(imageCache.get(imagesString[3]));
//        viewHolder.image5.setImageDrawable(imageCache.get(imagesString[4]));
//        viewHolder.image6.setImageDrawable(imageCache.get(imagesString[5]));
//        viewHolder.image7.setImageDrawable(imageCache.get(imagesString[6]));
//        viewHolder.image8.setImageDrawable(imageCache.get(imagesString[7]));
//        viewHolder.image9.setImageDrawable(imageCache.get(imagesString[8]));
//
        return view;
    }

    //一个Item中的view
    class ViewHolder{
        ImageView icon;
        TextView text;
        ImageView image1;
        ImageView image2;
        ImageView image3;
        ImageView image4;
        ImageView image5;
        ImageView image6;
        ImageView image7;
        ImageView image8;
        ImageView image9;
        ImageView [] images = {image1,image2,image3,image4,image5,image6,image7,image8,image9};
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
//    class ImageTask extends AsyncTask<String, Void, BitmapDrawable> {
//
//        private String imageUrl;
//        String text;
//
//        @Override
//        protected BitmapDrawable doInBackground(String... params) {
//            imageUrl = params[0];
//            text = params[1];
//            Bitmap bitmap = downloadImage();
//            BitmapDrawable db = new BitmapDrawable(listView.getResources(),
//                    bitmap);
//            // 如果本地还没缓存该图片，就缓存
//            if (imageCache.get(imageUrl) == null) {
//                imageCache.put(imageUrl, db);
//                Log.i("缓存图片",imageUrl);
//            }
//            return db;
//        }
//
//        @Override
//        protected void onPostExecute(BitmapDrawable result) {
//            // 通过Tag找到我们需要的ImageView，如果该ImageView所在的item已被移出页面，就会直接返回null
//            ImageView iv = (ImageView) listView.findViewWithTag(text);
//            if (iv != null && result != null) {
//                iv.setImageDrawable(result);
//            }
//        }
//
////        根据url从网络上下载图片
//        private Bitmap downloadImage() {
//            HttpURLConnection con = null;
//            Bitmap bitmap = null;
//            try {
//                URL url = new URL("http://172.20.10.2:8080/android_http_servers"+imageUrl);
//                con = (HttpURLConnection) url.openConnection();
//                con.setConnectTimeout(5 * 1000);
//                con.setReadTimeout(10 * 1000);
//                bitmap = BitmapFactory.decodeStream(con.getInputStream());
//                Log.i("下载图片",imageUrl.toString());
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                if (con != null) {
//                    con.disconnect();
//                }
//            }
//            return bitmap;
//        }
//
//    }


    private void LoadImage(ImageView img, String path)
    {
        //异步加载图片资源
        AsyncTaskImageLoad async=new AsyncTaskImageLoad(img,imageCache);
        //执行异步加载，并把图片的路径传送过去
        async.execute(path);
    }


}

















