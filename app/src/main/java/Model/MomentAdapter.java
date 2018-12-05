package Model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.colin.myapplication.R;

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
            viewHeader.textView = view.findViewById(R.id.momentText);
            view.setTag(viewHeader);
        }else{
            view = convertView;
            viewHeader = (ViewHeader) view.getTag();
        }


        String data = (String) getItem(position);
        viewHeader.textView.setText(data);
//        textView.setText(data);
        return view;
    }
    class ViewHeader{
        TextView textView;
    }

}

















