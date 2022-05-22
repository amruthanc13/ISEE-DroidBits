package com.droidbits.moneycontrol.ui.categories;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.droidbits.moneycontrol.R;

public class CategoryBaseAdapter extends BaseAdapter {

    Context context;
    String catList[];
    int catImages[];
    LayoutInflater inflater;


    public CategoryBaseAdapter(Context ctx, String[] cList, int[] cImages){

        this.context = ctx;
        this.catList = cList;
        this.catImages = cImages;
        inflater = LayoutInflater.from(ctx);

    }
    @Override
    public int getCount() {
        return catList.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.fragment_categories, null);
    /*    TextView txtView = (TextView) view.findViewById(R.id.textView);
        ImageView catIcons = (ImageView) view.findViewById(R.id.imageIcon);
        txtView.setText(catList[i]);
        catIcons.setImageResource(catImages[i]);*/
        return view;
    }
}
