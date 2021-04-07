package com.example.mycomputer.videostreamingjmn.sliding_menu_adapter;

/**
 * Created by My Computer on 7/16/2016.
 */
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.mycomputer.videostreamingjmn.R;
import com.example.mycomputer.videostreamingjmn.model.item_sliding_menu;

import java.util.Hashtable;
import java.util.List;

public class sliding_menu_adapter extends BaseAdapter{
    private Context context;
    private List<item_sliding_menu> listItem;

    public sliding_menu_adapter(Context context,List<item_sliding_menu> list) {
        this.context = context;
        listItem = list;
    }

    public int getCount(){
        return listItem.size();
    }

    public long getItemId(int position){
        return position;
    }

    public Object getItem(int position){
        return listItem.get(position);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View v = View.inflate(context, R.layout.item_sliding_menu, null);
       // ImageView img = (ImageView) v.findViewById(R.id.item_icon);
        TextView tv = (TextView) v.findViewById(R.id.item_title);
        v.setBackgroundColor(Color.WHITE);
        tv.setTextColor(Color.BLACK);

        Typeface type = Typeface.createFromAsset(context.getAssets(),"fonts/arial.ttf");
       // Typeface s = T
        tv.setTypeface(type);

        item_sliding_menu item = listItem.get(position);
       // img.setImageResource(item.getImgid());
        tv.setText(item.getTitle());

        return v;

    }

}

