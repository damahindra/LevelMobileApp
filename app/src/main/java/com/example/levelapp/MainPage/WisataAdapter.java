package com.example.levelapp.MainPage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.levelapp.R;
import com.example.levelapp.WisataInfo;

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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull WisataViewHolder holder, int position) {
        Wisata wisata = wisataList.get(position);
        holder.namaWisata.setText(wisata.getName());
        holder.lokasiWisata.setText(wisata.getPlace());
        holder.wisataPrice.setText("Rp"+ wisata.getPrice());
        Glide.with(holder.itemView.getContext()).load(wisata.getPicture()).into(holder.imageWisata);
        holder.imageWisata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the new activity here
                Wisata selected = wisataList.get(holder.getAdapterPosition());
                Intent info = new Intent(view.getContext(), WisataInfo.class);
                // Pass any necessary data to the new activity using intent extras
                info.putExtra("name", selected.getName());
                info.putExtra("place", selected.getPlace());
                info.putExtra("price", holder.wisataPrice.getText().toString());
                info.putExtra("description", selected.getDescription());
                info.putExtra("image", selected.getPicture());
                view.getContext().startActivity(info);
            }
        });
    }

    @Override
    public int getItemCount() {
        return wisataList.size();
    }

    public class WisataViewHolder extends RecyclerView.ViewHolder {
        ImageView imageWisata;
        TextView namaWisata;
        TextView lokasiWisata, wisataPrice;
        ImageView favoriteIcon;
        int favorite = R.drawable.favorite_icon;
        int clicked;

        public WisataViewHolder(@NonNull View itemView) {
            super(itemView);
            imageWisata = itemView.findViewById(R.id.imageWisata);
            namaWisata = itemView.findViewById(R.id.namaWisata);
            lokasiWisata = itemView.findViewById(R.id.lokasiWisata);
            favoriteIcon = itemView.findViewById(R.id.favoriteIcon);
            wisataPrice = itemView.findViewById(R.id.price_info);

            favoriteIcon.setImageResource(favorite);
            favoriteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (favorite == R.drawable.favorite_icon) {
                        clicked = R.drawable.favorite_icon_clicked;
                        favorite = R.drawable.favorite_icon_clicked;
                    }
                    else {
                        clicked = R.drawable.favorite_icon;
                        favorite = R.drawable.favorite_icon;
                    }
                    favoriteIcon.setImageResource(clicked);
//                    code to add items to favorite
                }
            });
        }
    }
}
