package com.example.putra;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;

public class UploadImageActivity extends AppCompatActivity {

    private int PICK_IMAGE_REQUEST = 1;
    private int TAKE_PICTURE_REQUEST = 2;
    private int CAMERA_PERM_CODE = 101;


    // views for button
    private Button btnSelect, btnUpload, btnTakePic;

    // view for image view
    private ImageView imageView;
    String currentPhotoPath;

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
        btnTakePic = findViewById(R.id.btnTakePic);

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


        btnTakePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              askCameraPremissions();
            }
        });



    }


    private void askCameraPremissions(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        }else{
            System.out.println("ka prem");
           dispatchTakePictureIntent();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Camera Permission is Required to Open Camera", Toast.LENGTH_SHORT).show();
                System.out.println("lll");
            }
        }
    }





    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }





    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.putra.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, TAKE_PICTURE_REQUEST);
            }else {
                System.out.println("pe di ku osht gabimi");
            }
        }
        else{
            System.out.println("qenka null");
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println(requestCode);
        System.out.println("larte duhet te jete requestcode");
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                    && data != null && data.getData() != null){
            mImageUri = data.getData();
           Picasso.with(this).load(mImageUri).into(imageView);
        }
        else if(requestCode == TAKE_PICTURE_REQUEST  ){
            System.out.println("deri tash jem mire");
            File f = new File(currentPhotoPath);
            Picasso.with(this).load(Uri.fromFile(f)).into(imageView);
            mImageUri = Uri.fromFile(f);

        }else{
            System.out.println("test abc");
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


                //   Upload upload = new Upload(mEditTextFileName.getText().toString().trim(),
                 //            downloadUri.toString());
                  // mDatabaseRef.push().setValue(upload);
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