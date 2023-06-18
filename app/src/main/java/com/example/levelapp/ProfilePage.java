package com.example.levelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.levelapp.MainPage.MainPageActivity;
import com.example.levelapp.Model.UserData;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProfilePage extends AppCompatActivity {

//    Firebase Storage for profile picture update
    FirebaseStorage storage;
    // Create a storage reference from our app
    StorageReference storageRef;

//    Firebase auth for logout
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

//    Firebase Realtime Database
    FirebaseDatabase userDatabase;
    DatabaseReference databaseRef;

//    Components
    Button logoutBtn, updateBtn;
    ImageView profilePicture, backBtn;
    TextView profileName, tvEmail;
    EditText etNoHP, etTanggalLahir;


    int PICK_IMAGE_REQUEST = 1021;
    Uri filePath;
    String newProfilePicture, userDataPath;
    UserData userData, newUserData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

//        Initialization
        userDatabase = FirebaseDatabase.getInstance();
        databaseRef = userDatabase.getReference();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        mAuth = FirebaseAuth.getInstance();
        profilePicture = findViewById(R.id.profilePicture);
        profileName = findViewById(R.id.profileName);
        updateBtn = findViewById(R.id.updateBtn);
        logoutBtn = findViewById(R.id.logoutBtn);
        backBtn = findViewById(R.id.backBtn);
        tvEmail = findViewById(R.id.tvEmail);
        etNoHP = findViewById(R.id.etNoHP);
        etTanggalLahir = findViewById(R.id.etTanggalLahir);
        newUserData = new UserData();

//        Current User
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            newProfilePicture  = "images/" + currentUser.getEmail() + "/newProfilePicture.png";
            userDataPath = "users/" + currentUser.getEmail().replaceAll("@gmail.com", "");
        }

        databaseRef.child(userDataPath).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userData = snapshot.getValue(UserData.class);
                if (userData != null) {
                    String namaLengkap = userData.getNamaDepan() + " " + userData.getNamaBelakang();
                    String email = currentUser.getEmail();
                    String noHp = userData.getNoHP();
                    String tanggallahir = userData.getTanggalLahir();

//                    setting the textview and edittext to user info
                    profileName.setText(namaLengkap);
                    tvEmail.setText(email);
                    etNoHP.setHint(noHp);
                    etTanggalLahir.setHint(tanggallahir);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfilePage.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });

        storageRef.child(newProfilePicture).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Glide.with( ProfilePage.this).load(uri).into(profilePicture);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Task<Uri> defaultProfilePicture = storageRef.child("images/default.png").getDownloadUrl();
                defaultProfilePicture.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with( ProfilePage.this).load(uri).into(profilePicture);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfilePage.this, "Error while loading profile picture", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        on click listeners
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile(newProfilePicture);
                if (!(etNoHP.getText().toString().isEmpty())) {
                    databaseRef.child(userDataPath).child("noHP").setValue(etNoHP.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            etNoHP.setText("");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("Ga kedetek", "Gabisa inimah brow");
                        }
                    });
                }
                if (!(etTanggalLahir.getText().toString().isEmpty())) {
                    databaseRef.child(userDataPath).child("tanggalLahir").setValue(etTanggalLahir.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            etTanggalLahir.setText("");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("Ga kedetek", "Gabisa inimah brow");
                        }
                    });
                }
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
    }
    // Override onActivityResult method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();

            try {
                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(getContentResolver(), filePath);
                profilePicture.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    private void updateProfile(String newProfilePicture) {
        if (filePath != null) {
            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref = storageRef.child(newProfilePicture);

            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    //            delete the old profile picture first
                    ref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {

                        // adding listeners on upload
                        // or failure of image
                        @Override
                        public void onSuccess(Void unused) {
                            sendData(ref, progressDialog);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ProfilePage.this, "There is a problem, please try again later", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    sendData(ref, progressDialog);
                }
            });
        }
    }

    private void logOut() {
        mAuth.signOut();
        Intent toSignIn = new Intent(ProfilePage.this, LoginPage.class);
        startActivity(toSignIn);
    }

    private void sendData(StorageReference ref, ProgressDialog progressDialog) {
        ref.putFile(filePath).addOnSuccessListener(
                        new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(
                                    UploadTask.TaskSnapshot taskSnapshot)
                            {
                                // Image uploaded successfully
                                // Dismiss dialog
                                progressDialog.dismiss();
                                Toast.makeText(ProfilePage.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                            }
                        })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {

                        // Error, Image not uploaded
                        progressDialog.dismiss();
                        Toast.makeText(ProfilePage.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    // Progress Listener for loading
                    // percentage on the dialog box
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        progressDialog.setMessage("Uploaded " + (int)progress + "%");
                    }
                });
    }
}