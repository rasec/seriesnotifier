package com.projects.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.projects.seriesnotifier.R;

public class IconListViewAdapter extends ArrayAdapter<String> {

    private List<String> items;
    private int icon;
    private Context context;
    public OnClickListener addSerie;
    
    public IconListViewAdapter(Context context, int textViewResourceId, List<String> items, int icon) {
            super(context, textViewResourceId, items);
            this.items = items;
            this.icon = icon;
            this.context = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.list_item_icon, null);
        }
        String text = items.get(position);
        	
        //poblamos la lista de elementos
        	
        TextView tt = (TextView) v.findViewById(R.id.listText);
        ImageView im = (ImageView) v.findViewById(R.id.listIcon);
        
        im.setOnClickListener(addSerie);
        
        if (im!= null) {
        	im.setImageResource(this.icon);
        }                        
        if (tt != null) {             
            tt.setText(text);                             
        }    	                    	                        
        
        return v;
   }
    
    
}
