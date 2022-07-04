package com.droidbits.moneycontrol.ui.home;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.droidbits.moneycontrol.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayAdapterWithIcon  extends ArrayAdapter {
    private Integer[] icons;

    public ArrayAdapterWithIcon(final @NonNull Context context, final String[] iconList) {
        super(context, 0, Arrays.asList(iconList));
    }

    /**
     * get the view.
     * @param position position
     * @param convertView View
     * @param parent View
     * @return
     */
    @NonNull
    @Override
    public View getView(final int position, final @Nullable View convertView, final @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    /**
     * get the dropdown view.
     * @param position position
     * @param convertView View
     * @param parent View
     * @return
     */
    @Override
    public View getDropDownView(final int position, final  @Nullable View convertView,
                                final  @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    /**
     * Set the array of icons.
     * @param listOfIcons String[] array of icons
     */
    public void setIcons(final Integer[] listOfIcons) {
        this.icons = listOfIcons;
        notifyDataSetChanged();
    }

    /**
     * The initial view.
     * @param position position
     * @param convertView View
     * @param parent View
     * @return return the view
     */
    private View initView(final int position, final View convertView, final ViewGroup parent) {
        ImageView iconImageView;
        if (convertView == null) {
            View currentView = LayoutInflater.from(getContext()).inflate(
                    R.layout.icon_custom_spinner, parent, false);
            iconImageView = currentView.findViewById(R.id.iconImage);

            if (icons != null) {
                Integer iconId = icons[position];


                iconImageView.setImageResource(iconId);
            }

            return currentView;
        } else {
            iconImageView = convertView.findViewById(R.id.iconImage);

            if (icons != null) {
                Integer iconId = icons[position];
          iconImageView.setImageResource(iconId);
            }

            return convertView;
        }

    }


}
