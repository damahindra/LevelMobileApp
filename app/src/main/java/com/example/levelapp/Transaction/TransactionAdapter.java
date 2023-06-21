package com.example.levelapp.Transaction;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {


    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class TransactionViewHolder extends RecyclerView.ViewHolder {

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
