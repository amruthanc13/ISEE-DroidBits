package com.droidbits.moneycontrol.ui.transactions;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.droidbits.moneycontrol.R;
import com.droidbits.moneycontrol.db.categories.Categories;
import com.droidbits.moneycontrol.db.transaction.Transactions;
import com.droidbits.moneycontrol.ui.categories.CategoriesViewModel;
import com.droidbits.moneycontrol.utils.CurrencyUtils;
import com.droidbits.moneycontrol.utils.DateUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class TransactionListAdapter extends RecyclerView.Adapter<TransactionListAdapter.TransactionViewHolder> {
    private List<Transactions> transactions;
    private final LayoutInflater layoutInflater;
    private final TransactionsViewModel transactionViewModel;
    private final CategoriesViewModel categoriesViewModel;
    private final OnTransactionNoteListener mOnNoteListener;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    /**
     * Creating adapter for Transaction.
     * @param context context
     * @param onNoteListener the onNotelistener
     * @param transactionVM the view model for creating new transactions
     */
    public TransactionListAdapter(
            final @NonNull Context context,
            final OnTransactionNoteListener onNoteListener,
            final TransactionsViewModel transactionVM,
            final CategoriesViewModel categoryVM

    ) {
        layoutInflater = LayoutInflater.from(context);
        transactionViewModel = transactionVM;
        mOnNoteListener = onNoteListener;
        categoriesViewModel = categoryVM;
    }

    @Override
    public int getItemCount() {
        if (transactions != null) {
            return transactions.size();
        } else {
            return 0;
        }
    }
    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(final @NonNull ViewGroup parent, final int viewType) {
        View itemView = layoutInflater.inflate(R.layout.transaction_listitem, parent, false);
        return new TransactionViewHolder(itemView, mOnNoteListener);

    }

    /**
     * Transactions setter.
     * @param transactionList transactions from db.
     */
    public void setTransactions(final List<Transactions> transactionList) {
        this.transactions = transactionList;
        notifyDataSetChanged();
    }
    /**
     * Initializer for transactions to be displayed.
     * @param holder view holder.
     * @param position position of transactions.
     */
    @Override
    public void onBindViewHolder(final @NonNull TransactionViewHolder holder, final int position) {
        if (transactions != null) {
            Transactions current = transactions.get(position);
            Context context = holder.transactionAmount.getContext();

            //get the Transaction information:
            Long date = current.getDate();
            String type = current.getType();
            Float amount = current.getAmount();
            String category = current.getCategory();


            String amountToString = CurrencyUtils.formatAmount(amount);

            if (type.equals("Expense")) {
                holder.transactionAmount.setTextColor(ContextCompat.getColor(context, R.color.colorExpense));
                amountToString = "- " + amountToString;
            } else {
                holder.transactionAmount.setTextColor(ContextCompat.getColor(context, R.color.colorIncome));
                amountToString = "+ " + amountToString;
            }

            holder.transactionDate.setText(DateUtils.formatDate(date));
            holder.transactionAmount.setText(amountToString);

            Categories categories = categoriesViewModel.getSingleCategory(Integer.parseInt(category));
            holder.transactionCategoryImage.setImageResource(categories.getIcon());
            holder.transactionCategoryTitle.setText(categories.getName());

            holder.deleteTransactionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
                    builder.setTitle("Delete Transaction?");
                    builder.setMessage("Are you sure you want to delete the transaction?");
                    builder.setBackground(context.getDrawable(
                            (R.drawable.alert_dialogue_box)));
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, final int which) {
                            Integer transactionId = current.getId();
                            transactionViewModel.deleteTransaction(transactionId);
                            Toast.makeText(context, "Transaction Deleted!", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, final int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }

            });

            viewBinderHelper.bind(holder.swipeRevealLayout,Integer.toString(current.getId()));
        }
    }




    final class TransactionViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {
        private final ImageView transactionCategoryImage;
        private final TextView transactionCategoryTitle;
        private final TextView transactionDate;
        private final TextView transactionAmount;
        private final OnTransactionNoteListener onNoteListener;
        private final LinearLayout deleteTransactionButton;
        private final LinearLayout fillerEmptySpace;
        private final SwipeRevealLayout swipeRevealLayout;

        /**
         * Transaction view holder.
         * @param itemView view that will hold the transaction list.
         * @param onTransactionNoteListener the onNoteListener
         */
        private TransactionViewHolder(final View itemView, final OnTransactionNoteListener onTransactionNoteListener) {
            super(itemView);

            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    onNoteListener.onTransactionClick(transactions.get(getAdapterPosition()), getAdapterPosition());
                }
            };

            transactionCategoryImage = itemView.findViewById(R.id.transactionCategoryImage);
            transactionCategoryTitle = itemView.findViewById(R.id.transactionCategoryTitle);
            transactionDate = itemView.findViewById(R.id.transactionDate);
            transactionAmount = itemView.findViewById(R.id.transactionAmount);
            fillerEmptySpace = itemView.findViewById(R.id.blankSpace);
            swipeRevealLayout = itemView.findViewById(R.id.swipeRevealLayout);
            deleteTransactionButton = itemView.findViewById(R.id.delete_transaction);

            transactionCategoryTitle.setOnClickListener(clickListener);
            transactionCategoryImage.setOnClickListener(clickListener);
            transactionAmount.setOnClickListener(clickListener);
            transactionDate.setOnClickListener(clickListener);
            fillerEmptySpace.setOnClickListener(clickListener);

            this.onNoteListener = onTransactionNoteListener;
        }

        /**
         * method to catch onlick event of list adapter.
         * @param v view
         */
        @Override
        public void onClick(final View v) {
            onNoteListener.onTransactionClick(transactions.get(getAdapterPosition()), getAdapterPosition());
        }
    }

    public interface OnTransactionNoteListener {
        /**
         * This method is to get the transaction and position of selected transaction.
         * @param transaction Transaction selected transaction
         * @param position int selected position
         */
        void onTransactionClick(Transactions transaction, int position);
    }
}
