package com.tutorials.theblog.Data;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tutorials.theblog.Activities.PostDetailsActivity;
import com.tutorials.theblog.Model.Blog;
import com.tutorials.theblog.Model.Users;
import com.tutorials.theblog.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by HP on 05-11-2017.
 */

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder>
{

    private Context context;
    private List<Blog> blogList;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;



    public BlogRecyclerAdapter(Context context, List<Blog> blogList) {
        this.context = context;
        this.blogList = blogList;
    }

    @Override
    public BlogRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {



        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_row,parent,false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(final BlogRecyclerAdapter.ViewHolder holder, int position) {

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();
        Blog blog = blogList.get(position);
        String imageUrl= null;

        final String uId =blog.getUserid();
        DatabaseReference ref = mDatabaseReference.child("MUsers").child(uId);

        ref.addValueEventListener(new ValueEventListener() {

            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {

            Users user = dataSnapshot.getValue(Users.class);
                try
                {
                    String fname= user.getFirstname();
                    String lname= user.getLastname();
                   String imageDp = user.getImage();
                    holder.userName.setText(fname + " "+ lname);
                    Picasso.with(context).load(imageDp).into(holder.dpImage);

                }
                catch (Exception e)
                {

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
         /*
        DatabaseReference ref1 = mDatabaseReference.child("MUsers").child(uId).child("lastname");
        ref1.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {

               lName[0] = dataSnapshot.getValue(String.class);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
        DatabaseReference ref = mDatabaseReference.child("MUsers");

        ref.addValueEventListener(new ValueEventListener() {

            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot valueSnapshot = dataSnapshot.child(uId);
                Iterable<DataSnapshot> valueChildren = valueSnapshot.getChildren();
                ArrayList<Users> values = new ArrayList<>();
                for (DataSnapshot value : valueChildren)
                {
                    String name= value.getValue(String.class);
                    holder.userName.setText(name.);
                }

                fName[0] = dataSnapshot.getValue(String.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        String firstName = fName[0];
        String lastName= lName[0];
        holder.userName.setText(firstName+" "+lastName);*/
        //holder.title.setText("Title: "+mDatabaseReference.child("firstname").getKey() + " " +mDatabaseReference.child("lastname").getKey());
        holder.title.setText("Title: "+blog.getTitle());

       holder.desc.setText("Description: "+ blog.getDesc());
        java.text.DateFormat dateFormat =  java.text.DateFormat.getDateInstance();
        String formattedDate = dateFormat.format(new Date(Long.valueOf(blog.getTimestamp())).getTime());
        holder.timestamp.setText("Created on: "+ formattedDate);

        imageUrl = blog.getImage();

        //Todo: Use picaso library to load image
        Picasso.with(context).load(imageUrl).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView desc;
        public TextView timestamp;
        public ImageView image;
        public ImageView dpImage;
        public TextView userName;


        String userid;
        public ViewHolder(View view, final Context ctx) {
            super(view);
            context=ctx;
            dpImage = (ImageView) view.findViewById(R.id.DpImage);
            userName= (TextView) view.findViewById(R.id.userName);
            title =(TextView) view.findViewById(R.id.postTitleList);
            desc =(TextView) view.findViewById(R.id.postTextList);
            image = (ImageView) view.findViewById(R.id.postImageList);
            timestamp = (TextView) view.findViewById(R.id.timestampList);

            userid= null;

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Blog blog= blogList.get(getAdapterPosition());

                    Intent intent = new Intent(context, PostDetailsActivity.class);
                    intent.putExtra("title", blogList.get(getAdapterPosition()).getTitle());
                    intent.putExtra("desc", blogList.get(getAdapterPosition()).getDesc());
                    intent.putExtra("image", blogList.get(getAdapterPosition()).getImage());

                    ctx.startActivity(intent);


                }
            });
        }
    }
}
