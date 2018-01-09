package com.example.asus.instagramclonefirebase;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.opengl.GLException;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.security.Permission;
import java.util.UUID;

public class UploadActivity extends AppCompatActivity {
    EditText edit_comment;
    Button bUpload;
    ImageView img;
    Uri selected;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private StorageReference mStorageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        edit_comment = findViewById(R.id.edit_comment);
        bUpload = findViewById(R.id.b_upload);
        img = findViewById(R.id.imageView);

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();    //database referansı

        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();





    }

    public void buttonUpload(View view) {

        UUID uuıdImage = UUID.randomUUID();
        String imageName = "images/"+uuıdImage+".jpg";

        StorageReference storageReference = mStorageRef.child(imageName);

        //Resmi Koyduk
        storageReference.putFile(selected).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) { //RESİMİ EKLEYEBİLİRSEK NE YAPACAZ

                String dowloadURL = taskSnapshot.getDownloadUrl().toString();
                FirebaseUser user =  mAuth.getCurrentUser();
                String userEmail = user.getEmail().toString();
                String userComment = edit_comment.getText().toString();

                UUID uuıd = UUID.randomUUID();
                String  uuidString = uuıd.toString();

                myRef.child("Post").child(uuidString).child("useremail").setValue(userEmail);
                myRef.child("Post").child(uuidString).child("comment").setValue(userComment);
                myRef.child("Post").child(uuidString).child("downloadurl").setValue(dowloadURL);

                Intent i = new Intent(getApplicationContext(),FeedActivity.class);
                startActivity(i);

            }
        }).addOnFailureListener(new OnFailureListener() { // RESİM EKLENEMESSE NE OLACAK
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"FAİL  "+e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }



    public void selectImage(View view) {

        if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

        }else {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i,1);
        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1){
            if(grantResults.length>0 && grantResults[0]!= PackageManager.PERMISSION_GRANTED){
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i,2);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK && data != null){
            selected = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selected);
                img.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);


    }
}
