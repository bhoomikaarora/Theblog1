package com.tutorials.theblog.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tutorials.theblog.Data.BlogRecyclerAdapter;
import com.tutorials.theblog.Model.Blog;
import com.tutorials.theblog.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PostDetailsActivity extends AppCompatActivity {
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private TextView title;
    private TextView desc;
    private ImageView imageDet;
    private BlogRecyclerAdapter blogRecyclerAdapter;
    private List<Blog> blogList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("MBlog");
        mDatabaseReference.keepSynced(true);

        title= (TextView) findViewById(R.id.postTitleDet);
        desc= (TextView) findViewById(R.id.postDescDet);
        imageDet = (ImageView) findViewById(R.id.imageViewDets);

 /*       mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               for (DataSnapshot objSnapshot: dataSnapshot.getChildren()) {
                    Blog blog = objSnapshot.getValue(Blog.class);

                    title.setText("Title: "+ blog.title);
                    desc.setText("Title: "+ blog.desc);

                   String imageUrl = blog.image;
                   Picasso.with(getApplicationContext()).load(imageUrl).into(imageDet);

                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    String titlePost =getIntent().getStringExtra("title");
        title.setText("Title:" + titlePost);
        desc.setText("Description: " + getIntent().getStringExtra("desc"));

        String imageUrl = getIntent().getStringExtra("image");
        Picasso.with(getApplicationContext()).load(imageUrl).into(imageDet);







    }
}
