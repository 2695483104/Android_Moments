package Model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.colin.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MomentAdapter extends ArrayAdapter {

    private int resourceID;
    public MomentAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        resourceID = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        View view = LayoutInflater.from(getContext()).inflate(resourceID, parent,false);
//        TextView textView = view.findViewById(R.id.momentText);

        View view;
        ViewHeader viewHeader = new ViewHeader();
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceID,parent,false);
            viewHeader.icon = view.findViewById(R.id.icon);
            viewHeader.text = view.findViewById(R.id.momentText);
            viewHeader.image1 = view.findViewById(R.id.image1);
            view.setTag(viewHeader);
        }else{
            view = convertView;
            viewHeader = (ViewHeader) view.getTag();
        }
        
        JSONObject moment = (JSONObject) getItem(position);
        String text = null;
        try {
            text = moment.getString("text");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        viewHeader.text.setText(text);

        return view;
    }
    class ViewHeader{
        ImageView icon;
        TextView text;
        ImageView image1;
    }

}

















