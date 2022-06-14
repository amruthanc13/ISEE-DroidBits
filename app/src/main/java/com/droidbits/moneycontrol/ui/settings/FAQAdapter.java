package com.droidbits.moneycontrol.ui.settings;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.droidbits.moneycontrol.R;


public class FAQAdapter extends BaseExpandableListAdapter {

    private Context context;


    private String[] faqs = {
            "Q1. How can I add a new Transaction?",
            "Q2. How can I add a new Category?",
            "Q3. How can I delete a Transaction?",
            "Q4. Can I filter Transaction?"
    };

    private String[][] answer = {
            {"You can add a new Transaction by clicking on the 'Add Transaction' Button on Home or Transaction Page"},
            {"You can add a new category from Categories Page"},
            {"You can delete a transaction by swiping the transaction left and clicking on delete icon"},
            {"You can Filter the transaction by clicking on filter icon in the transaction page and giving filter values"}

    };

    private int[][] images = {
            //TODO : change the image id with the new screenshots once developed.
            //change in getChildView accordingly
            //for answer 1
            {R.drawable.all},
            //for answer 2
            {R.drawable.all},
            //for answer 3
            {R.drawable.all}
    };

    public FAQAdapter(final Context helpContext) {
        this.context = helpContext;
    }

    @Override
    public int getGroupCount() {
        return faqs.length;
    }

    @Override
    public int getChildrenCount(final int positionOfGroup) {
        return answer[positionOfGroup].length;
    }


    @Override
    public Object getGroup(final int positionOfGroup) {
        return faqs[positionOfGroup];
    }

    @Override
    public Object getChild(final int positionOfGroup, final int positionOfChild) {
        return answer[positionOfGroup][positionOfChild];
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public long getChildId(final int positionOfGroup, final int positionOfChild) {
        return positionOfChild;
    }

    @Override
    public long getGroupId(final int positionOfGroup) {
        return positionOfGroup;
    }
    
    @Override
    public View getChildView(final int positionOfGroup, final int positionOfChild, final boolean isLastChild,
                             final View convertView, final ViewGroup parent) {
        final String answerFaq = (String) getChild(positionOfGroup, positionOfChild);
        View childView = convertView;
        if (childView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            childView = inflater.inflate(R.layout.faq_answer, null);
        }
        TextView textView = childView.findViewById(R.id.faqAnswer);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setText(answerFaq);

        ImageView imageView = (ImageView) childView.findViewById(R.id.imageView1);
        if (answerFaq == answer[0][0]) {
            imageView.setImageResource(images[0][0]);
        } else if (answerFaq == answer[1][0]) {
            imageView.setImageResource(images[1][0]);
        } else if (answerFaq == answer[2][0]) {
            imageView.setImageResource(images[2][0]);
        } else {
            imageView.setImageResource(0);
        }
        return childView;
    }

    @Override
    public View getGroupView(final int positionOfGroup, final boolean isExpanded,
                             final View convertView, final ViewGroup parent) {
        String questionFaq = (String) getGroup(positionOfGroup);
        View groupView = convertView;
        if (groupView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            groupView = inflater.inflate(R.layout.faq_title, null);
        }
        TextView textview = groupView.findViewById(R.id.faqTitle);
        textview.setTypeface(null, Typeface.BOLD);
        textview.setText(questionFaq);
        return groupView;
    }
    
    @Override
    public boolean isChildSelectable(final int positionOfGroup, final int positionOfChild) {
        return false;
    }
}
