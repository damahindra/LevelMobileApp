package com.example.levelapp.MainPage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.levelapp.R;
import com.example.levelapp.WisataInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class WisataAdapter extends RecyclerView.Adapter<WisataAdapter.WisataViewHolder> {

//    Realtime database needs
    FirebaseDatabase userDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseRef = userDatabase.getReference();

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
        holder.favoriteIcon.setImageResource(wisata.getIcon());
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
        WisataAdapter adapter;
        ImageView imageWisata;
        TextView namaWisata;
        TextView lokasiWisata, wisataPrice;
        ImageView favoriteIcon;
        int favorite = R.drawable.favorite_icon;
        int clicked;

        public WisataViewHolder(@NonNull View itemView) {
            super(itemView);

//            adapter
            adapter =  WisataAdapter.this;

            imageWisata = itemView.findViewById(R.id.imageWisata);
            namaWisata = itemView.findViewById(R.id.namaWisata);
            lokasiWisata = itemView.findViewById(R.id.lokasiWisata);
            favoriteIcon = itemView.findViewById(R.id.favoriteIcon);
            wisataPrice = itemView.findViewById(R.id.price_info);
            favoriteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    variable to set the icon based on the item is favorite or not
                    Wisata current_item = wisataList.get(getAdapterPosition());

//                    variables to update realtime database
                    boolean current_status = false;
                    String current_path = generatePath(current_item.getId());

                    if (current_item.getIcon() == R.drawable.favorite_icon_clicked) {
                        current_item.setIcon(R.drawable.favorite_icon);
                    }
                    else {
                        current_item.setIcon(R.drawable.favorite_icon_clicked);
                        current_status = true;
                    }
                    favoriteIcon.setImageResource(current_item.getIcon());
//                    update the boolean "favorite" in realtime database
                    databaseRef.child(current_path).child("favorite").setValue(current_status).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(view.getContext(), "failed to add to favorites", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
        private String generatePath(int item_position) {
            String path = "posts/";
            if (String.valueOf(item_position).length() == 1) {
                path = path + "00" + item_position;
            }
            else if (String.valueOf(item_position).length() == 2) {
                path = path + "0" + item_position;
            }
            else if (String.valueOf(item_position).length() == 3) {
                path = path + item_position;
            }
            return path;
        }

        private void setIcon() {

        }
    }
}
