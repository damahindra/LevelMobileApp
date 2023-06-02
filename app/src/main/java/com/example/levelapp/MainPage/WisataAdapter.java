package com.example.levelapp.MainPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.levelapp.R;

import java.util.List;

public class WisataAdapter extends RecyclerView.Adapter<WisataAdapter.WisataViewHolder> {
    private Context context;
    private List<Wisata> wisataList;

    public WisataAdapter(Context context, List<Wisata> wisataList) {
        this.context = context;
        this.wisataList = wisataList;
    }

    @NonNull
    @Override
    public WisataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_wisata, parent, false);
        return new WisataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WisataViewHolder holder, int position) {
        Wisata wisata = wisataList.get(position);
        holder.namaWisata.setText(wisata.getNama());
        holder.lokasiWisata.setText(wisata.getLokasi());
        holder.imageWisata.setImageResource(wisata.getFoto());
    }

    @Override
    public int getItemCount() {
        return wisataList.size();
    }

    public class WisataViewHolder extends RecyclerView.ViewHolder {
        ImageView imageWisata;
        TextView namaWisata;
        TextView lokasiWisata;

        public WisataViewHolder(@NonNull View itemView) {
            super(itemView);
            imageWisata = itemView.findViewById(R.id.imageWisata);
            namaWisata = itemView.findViewById(R.id.namaWisata);
            lokasiWisata = itemView.findViewById(R.id.lokasiWisata);
        }
    }
}
