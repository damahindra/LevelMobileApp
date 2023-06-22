package com.example.levelapp.Transaction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.levelapp.MainPage.WisataAdapter;
import com.example.levelapp.R;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
    List<Transaction> transactions;
    Context context;

    public TransactionAdapter(List<Transaction> transactions, Context context) {
        this.transactions = transactions;
        this.context = context;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.history, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction currentTransaction = transactions.get(position);
        holder.namaWisata.setText(currentTransaction.getPlace_name());
        holder.lokasiWisata.setText(currentTransaction.getPlace_location());
        holder.price.setText("Rp" + currentTransaction.getTotal_price());
        holder.dateTime.setText(currentTransaction.getDateTime());
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView namaWisata, lokasiWisata, dateTime, price;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            namaWisata = itemView.findViewById(R.id.namaWisata);
            lokasiWisata = itemView.findViewById(R.id.lokasiWisata);
            dateTime = itemView.findViewById(R.id.dateTime);
            price = itemView.findViewById(R.id.price_info);
        }
    }
}
