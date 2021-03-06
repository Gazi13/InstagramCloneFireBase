package com.example.asus.instagramclonefirebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FeedActivity extends AppCompatActivity {

    //Lists for data from FireBase
    //FireBase den aldığımız verileri tutmak için
    ArrayList<String> userEmailFromFB;
    ArrayList<String> userCommentFromFB;
    ArrayList<String> userImageFromFB;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    post adapter;
    ListView listView;

    //Üst menu tanımlama !?
    //Options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_post,menu);
        return super.onCreateOptionsMenu(menu);

    }

    //Menuden işlem seçme
    //Select from options menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== R.id.add_post){
            Intent i = new Intent(getApplicationContext(),UploadActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);


        userCommentFromFB = new ArrayList<String>();
        userEmailFromFB = new ArrayList<String>();
        userImageFromFB = new ArrayList<String>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef=firebaseDatabase.getReference();

        //post diye oluşturduğumuz bir class (adapter) kullandık
        //the post class that extend arrayadapter
        adapter = new post(userEmailFromFB,userImageFromFB,userCommentFromFB,this);

        //Evey time you add sometinh to the list it with the adapter's form
        //listedeki her bir eleman bizim adapterimizin belirlediği tipte olacak
        //yani her bir eleman email-resim-yorum içeren bir yapıda olacak
        listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);

        //resimlere yorumlara tıklanınca birşey olmasını istiyorsan listener kullanabilirsin
        // You can set a listener like this  listView.setOnItemClickListener to open another page for a post
        //for ex; for make a picture big
        getDataFromFireBase();

    }

    protected void getDataFromFireBase(){
        //Database içinde nereden alıcaz veriyi onu belirle
        //Where is the data in fb
        //-> post
        //->>child--(email-imageURl-comment)
        //->>child--(email-imageURl-comment)
        //...
        DatabaseReference newReferance = firebaseDatabase.getReference("Post");
       newReferance.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {//VERİ DEĞİŞTİKCE YAPILACAKLAR
               //Each child has email-imageURL-commet (child = a post)
               for(DataSnapshot ds : dataSnapshot.getChildren()){
                   HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue(); //Each value has email-URL-comment
                   userEmailFromFB.add(hashMap.get("useremail"));
                   userCommentFromFB.add(hashMap.get("comment"));
                   userImageFromFB.add(hashMap.get("downloadurl"));
                   adapter.notifyDataSetChanged();
               }
           }
           //BİR HATA OLURSA YAPILACAKLAR
           @Override
           public void onCancelled(DatabaseError databaseError) {
               Toast.makeText(getApplicationContext(),"FAİL WHİLE GETTİNG DATA FROM FİREBASE \n"+databaseError.getMessage(),Toast.LENGTH_LONG).show();
           }
       });

    }
}
