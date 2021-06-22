package com.example.putra;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ThrowOnExtraProperties;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.UUID;

import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;

public class UploadImageActivity extends AppCompatActivity {

    // views for button
    private Button btnSelect, btnUpload;

    // view for image view
    private ImageView imageView;
    private int PICK_IMAGE_REQUEST = 1;

    private Uri mImageUri;
    private  StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private ProgressBar mProgressBar;
    private EditText mEditTextFileName;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    SharedPreferences shp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        shp = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        ActionBar actionBar;
        actionBar = getSupportActionBar();


        btnSelect = findViewById(R.id.btnChoose);
        btnUpload = findViewById(R.id.btnUpload);
        imageView = findViewById(R.id.imgView);
        mProgressBar = findViewById(R.id.progress_bar);
        mEditTextFileName = findViewById(R.id.edit_text_file_name);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads/");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

       btnUpload.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               uploadFile();
           }
       });



    }


    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                    && data != null && data.getData() != null){
            mImageUri = data.getData();
           Picasso.with(this).load(mImageUri).into(imageView);
        }
    }


   private String getFileExtension(Uri uri){
       ContentResolver cR = getContentResolver();
       MimeTypeMap mime = MimeTypeMap.getSingleton();
       return mime.getExtensionFromMimeType(cR.getType(uri));
   }

   private void uploadFile(){
       if (mImageUri != null)
       {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));
           fileReference.putFile(mImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
           {
               @Override
               public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
               {
                   if (!task.isSuccessful())
                   {
                       throw task.getException();
                   }


                   return fileReference.getDownloadUrl();

               }
           }).addOnCompleteListener(new OnCompleteListener<Uri>()
           {
               @Override
               public void onComplete(@NonNull Task<Uri> task)
               {
                   if (task.isSuccessful())
                   {
                       Uri downloadUri = task.getResult();
                       Log.d("FIREBASE", "then: " + downloadUri.toString());


                   Upload upload = new Upload(mEditTextFileName.getText().toString().trim(),
                             downloadUri.toString());
                   mDatabaseRef.push().setValue(upload);
                    System.out.println( "Ky eshte url " + downloadUri.toString());
                        firestore.collection("users")
                                .document(shp.getString(LoginActivity.DocumentId,"defValue"))
                                .update("imageUrl",downloadUri.toString())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                            updateSitter(downloadUri.toString());
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(UploadImageActivity.this,"what " +  e.getMessage(), Toast.LENGTH_LONG).show();
                                        System.out.println("Ky eshte documentId " + LoginActivity.DocumentId);
                                    }
                                });



                   } else
                   {
                       Toast.makeText(UploadImageActivity.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                   }
               }
           });
       }

   }


   private void updateSitter(String imageUrl){
        firestore.collection("sitters")
                .whereEqualTo("user",shp.getLong(LoginActivity.Id,-1))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document: task.getResult()){
                                firestore.collection("sitters")
                                        .document(document.getId())
                                        .update("imageUrl",imageUrl)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(UploadImageActivity.this,"U ndryshua me sukses", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                                Toast.makeText(UploadImageActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UploadImageActivity.this, "failed " + e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
   }




}