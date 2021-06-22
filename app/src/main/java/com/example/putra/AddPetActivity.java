package com.example.putra;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;

public class AddPetActivity extends AppCompatActivity {

   AvatarView avatarView;
   EditText name, type, breed, gender, size, birth;
   Button submitBtn;
   Switch vaccinatedSwitch, neuteredSwitch;
   PicassoLoader picassoLoader = new PicassoLoader();
   FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private int PICK_IMAGE_REQUEST = 1;
   private StorageReference mStorageRef;
   private DatabaseReference mDatabaseRef;

   private Uri mImageUri;
    SharedPreferences shp;
    PetAdapter petAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);
        shp = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        bind();
        picassoLoader.loadImage(avatarView,"https://media.istockphoto.com/vectors/dog-icon-with-add-sign-labrador-retriever-icon-and-new-plus-positive-vector-id1145021752","test");
        mStorageRef = FirebaseStorage.getInstance().getReference("uploadsPets/");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploadPets");

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //addPet(getData());
                uploadFile();
            }
        });


        avatarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

    }






   public void bind(){
        name = findViewById(R.id.etEditPetName);
        type = findViewById(R.id.etEditPetType);
        breed = findViewById(R.id.etEditPetBreed);
        gender = findViewById(R.id.etEditPetGender);
        size = findViewById(R.id.etEditPetSize);
        birth = findViewById(R.id.etEditPetBirth);
        avatarView = findViewById(R.id.selectImageViewPet);
        submitBtn = findViewById(R.id.submitBtn);
        vaccinatedSwitch = findViewById(R.id.vaccinatedSwitch);
        neuteredSwitch = findViewById(R.id.neuteredSwitch);
   }




   public void addPet(Map<String, Object> pet){

      firestore.collection("pets")
              .add(pet)
              .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                  @Override
                  public void onSuccess(DocumentReference documentReference) {
                      Toast.makeText(AddPetActivity.this,"Eshte shtuar me sukses",Toast.LENGTH_SHORT).show();
                        finish();
                  }
              })
              .addOnFailureListener(new OnFailureListener() {
                  @Override
                  public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddPetActivity.this, "Diqka ndodhi gabim",Toast.LENGTH_SHORT).show();
                  }
              });

   }

   public Map<String, Object> getData(String imageUrl){
        Map<String, Object> pet = new HashMap<>();

       pet.put("name",name.getText().toString());
       pet.put("type",type.getText().toString());
       pet.put("gender",gender.getText().toString());
       pet.put("breed",breed.getText().toString());
       pet.put("size",size.getText().toString());
       pet.put("yearOfBirth",birth.getText().toString());
       pet.put("id",PetId.generateUniqueId()+"");
       pet.put("user",shp.getLong(LoginActivity.Id,-1)+"");
       if(imageUrl != null || imageUrl.equals("") ) {
           pet.put("imageUrl", imageUrl);
       }else{
           pet.put("imageUrl","");
       }

       if(vaccinatedSwitch.isChecked()){
           pet.put("vaccinated","true");
       }else{
           pet.put("vaccinated","false");
       }

       if(neuteredSwitch.isChecked()){
           pet.put("neutered","true");
       }else{
           pet.put("neutered","false");
       }


       return pet;

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
            Picasso.with(this).load(mImageUri).into(avatarView);
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


                      //  Upload upload = new Upload(mEditTextFileName.getText().toString().trim(),
                       //         downloadUri.toString());
                       // mDatabaseRef.push().setValue(upload);
                        System.out.println( "Ky eshte url " + downloadUri.toString());
                        firestore.collection("pets")
                        .add(getData(downloadUri.toString()))
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(AddPetActivity.this,"Eshte shtuar me sukses",Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        Toast.makeText(AddPetActivity.this,"upload failed " + e.getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                });



                    } else
                    {
                        Toast.makeText(AddPetActivity.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }



}