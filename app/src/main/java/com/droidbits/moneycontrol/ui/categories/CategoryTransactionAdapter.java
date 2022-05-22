package com.droidbits.moneycontrol.ui.categories;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.droidbits.moneycontrol.R;
import com.droidbits.moneycontrol.db.categories.Categories;
import com.droidbits.moneycontrol.db.transaction.Transactions;

import java.util.List;

public class CategoryTransactionAdapter extends ArrayAdapter {
        private List<Categories> categories;

        /**
         * Create an Adapter for Icon.
         * @param context context
         * @param categoryIconList String[] List of category icons
         */
    public CategoryTransactionAdapter(final @NonNull Context context, final List<Categories> categoryIconList) {
            super(context, 0, categoryIconList);
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
         * @param categoryIconList String[] array of icons
         */
        public void setListOfCategroies(final List<Categories> categoryIconList) {
            this.categories = categoryIconList;
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
            TextView iconTextView;
            if (convertView == null) {
                View currentView = LayoutInflater.from(getContext()).inflate(
                        R.layout.category_custom_spinner, parent, false);
                iconImageView = currentView.findViewById(R.id.categoryIconImage);
                iconTextView = currentView.findViewById(R.id.categoryIconName);

                if (categories != null) {

                    Categories current = categories.get(position);
                    String iconName = current.getName();
                    int resID = current.getIcon();

                    iconImageView.setImageResource(resID);
                    iconTextView.setText(iconName);
                }

                return currentView;
            } else {
                iconImageView = convertView.findViewById(R.id.categoryIconImage);
                iconTextView = convertView.findViewById(R.id.categoryIconName);

                if (categories != null) {
                    Categories current = categories.get(position);
                    String iconName = current.getName();
                    int resID = current.getIcon();

                    iconImageView.setImageResource(resID);
                    iconTextView.setText(iconName);
                }

                return convertView;
            }

        }
}
