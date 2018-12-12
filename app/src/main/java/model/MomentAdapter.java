package model;

import android.content.Context;
import android.graphics.Bitmap;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MomentAdapter extends ArrayAdapter {

    private int resourceID;
    private ListView listView;
    private LruCache<String, Bitmap> imageCache;


    public MomentAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        resourceID = resource;
        int maxCache = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxCache / 8;
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
        if (listView == null) {
            listView = (ListView) parent;
        }

        View view;
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceID,parent,false);
            viewHolder.momentUserName = view.findViewById(R.id.momentUserName);
            viewHolder.icon = view.findViewById(R.id.icon);
            viewHolder.text = view.findViewById(R.id.momentText);
            int[] R_id_images = {R.id.image1,R.id.image2,R.id.image3,R.id.image4,R.id.image5,R.id.image6,R.id.image7,R.id.image8,R.id.image9};
            for (int i=0;i<9;i++){
                viewHolder.images[i] = view.findViewById(R_id_images[i]);
            }
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        JSONObject moment = (JSONObject) getItem(position);
        String momentUserName = null;
        String iconURL = null;
        String text = null;
        JSONArray imagesJSONArry ;
        ArrayList<String> imagesList = new ArrayList<>();
        try {
            momentUserName = Objects.requireNonNull(moment).getString(MomentItem.userName);
            iconURL = moment.getString(String.valueOf(MomentItem.icon));
            text = moment.getString(String.valueOf(MomentItem.text));
            imagesJSONArry = (JSONArray) moment.get(String.valueOf(MomentItem.images));
            for (int i=0;i<imagesJSONArry.length();i++){
                imagesList.add(imagesJSONArry.get(i).toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i=0;i<9;i++){
                viewHolder.images[i].setVisibility(View.GONE);
        }
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
                viewHolder.images[1].setVisibility(View.VISIBLE);
                viewHolder.images[0].setVisibility(View.VISIBLE);
                break;
            case 2:
                viewHolder.images[1].setVisibility(View.VISIBLE);
                ViewGroup.LayoutParams params1 = viewHolder.images[1].getLayoutParams();
                params1.width = 394;
                params1.height = 394;
            case 1:
                viewHolder.images[0].setVisibility(View.VISIBLE);
                ViewGroup.LayoutParams params2 = viewHolder.images[0].getLayoutParams();
                params2.width = 394;
                params2.height = 394;
                break;
            default:
                break;
        }

        viewHolder.momentUserName.setText(momentUserName);
        viewHolder.text.setText(text);
        if (imageCache.get(Objects.requireNonNull(iconURL)) != null) {
            viewHolder.icon.setImageBitmap(imageCache.get(iconURL));
        } else {
            LoadImage(viewHolder.icon,iconURL);
        }
        for (int i = 0;i < imagesList.size();i++){
            String multiImages = imagesList.get(i);
            if (imageCache.get(multiImages) != null) {
                viewHolder.images[i].setImageBitmap(imageCache.get(multiImages));
            } else {
                LoadImage(viewHolder.images[i],multiImages);
            }
        }

        return view;
    }

    class ViewHolder{
        TextView momentUserName;
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

    private void LoadImage(ImageView img, String path)
    {
        AsyncTaskImageLoad async=new AsyncTaskImageLoad(img,imageCache);
        async.execute(path);
    }
}

















