package com.example.levelapp;

import static java.nio.file.AccessMode.WRITE;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.levelapp.Model.UserData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Receipt extends AppCompatActivity {
    TextView resi_no, resi_name, resi_email, destination_name, destination_place, destination_price, resi_qty, dateTime;

    CardView layout;
    Button continueBtn;
    ImageView backBtn;
    String id;

    private LinearLayout linear;
    private Bitmap bitmap;

    //    for fetching user related data
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String userDataPath;

    FirebaseDatabase userDatabase;
    DatabaseReference databaseRef;
    UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        backBtn = findViewById(R.id.history_backBtn);

//        initialization
        resi_no = findViewById(R.id.resi_no);
        resi_name = findViewById(R.id.resi_nama);
        resi_email = findViewById(R.id.resi_email);
        destination_name = findViewById(R.id.resi_destinasi);
        destination_place = findViewById(R.id.resi_lokasi);
        destination_price = findViewById(R.id.resi_biaya);
        resi_qty = findViewById(R.id.resi_qty);
        dateTime = findViewById(R.id.dateTime);
        continueBtn = findViewById(R.id.btn_download);
        layout = findViewById(R.id.cardView6);

        linear = findViewById(R.id.linearLayout);

//        Generating id for receipt

        Intent intent = getIntent();

        // Check if the intent has extras
        if (intent != null) {
            // Extract the extras from the intent
            id = intent.getStringExtra("uniqueId");
            String buyer_name = intent.getStringExtra("buyer_name");
            String buyer_email = intent.getStringExtra("buyer_email");
            String intent_name = intent.getStringExtra("destination_name");
            String intent_price = intent.getStringExtra("total_price");
            String intent_place = intent.getStringExtra("destination_location");
            String intent_qty = intent.getStringExtra("qty");
            String intent_dateTime = intent.getStringExtra("dateTime");

            //        set the info page
            resi_no.setText(id);
            resi_name.setText(buyer_name);
            resi_email.setText(buyer_email);
            destination_name.setText(intent_name);
            destination_place.setText(intent_place);
            destination_price.setText(intent_price);
            resi_qty.setText(intent_qty);
            dateTime.setText(intent_dateTime);

        }

//        on click listener
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("size", "" + linear.getWidth() + " " + linear.getWidth());
                bitmap = LoadBitmap(linear, linear.getWidth(), linear.getHeight());
                createPdf(id);
                Toast.makeText(Receipt.this, "Receipt saved!", Toast.LENGTH_SHORT).show();
                //Intent download = new Intent(Receipt.this, ReceiptCreate.class);
                //startActivity(download);
                //finish();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private Bitmap LoadBitmap(LinearLayout linear, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        linear.draw(canvas);
        return bitmap;
    }
    private void createPdf(String uniqueId) {
        WindowManager window = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics display = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(display);
        float width = layout.getWidth();
        float height = layout.getHeight();
        int convertWidth = (int)width;
        int convertHeight = (int)height;


        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHeight, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        canvas.drawPaint(paint);
        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHeight, true);
        canvas.drawBitmap(bitmap, 0, 0, null);
        document.finishPage(page);

        String targetPdf = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/LeveL_transaction_" + uniqueId + ".pdf";;
        File file = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(file));
        }
        catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "something wrong, please try again" + e.toString(), Toast.LENGTH_SHORT).show();

            document.close();
            Toast.makeText(this, "download success", Toast.LENGTH_SHORT).show();

        }
    }
}