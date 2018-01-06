package com.tutorials.theblog.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.tutorials.theblog.Model.Users;
import com.tutorials.theblog.R;

import java.util.HashMap;
import java.util.Map;

public class ProfileUpdate extends AppCompatActivity {
    private DatabaseReference mPostDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private Uri mImageUri;
    private Uri resultUri;
    private  static final int GALLERY_CODE=1;
    private FirebaseDatabase mDatabase;
    private StorageReference mStorage;
    private StorageReference mFirebaseStorage;
    private TextView updateName;
    private Button updateProfile;
    private ImageButton updateImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);
        mDatabase = FirebaseDatabase.getInstance();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
       mStorage= FirebaseStorage.getInstance().getReference().child("MUsers");
        mFirebaseStorage = FirebaseStorage.getInstance().getReference().child("MBlog_Profile_Pics");

        updateName= (TextView) findViewById(R.id.updateName);
        updateImage = (ImageButton) findViewById(R.id.updateProfilePic);
        updateProfile=(Button) findViewById(R.id.updateProfile);
        updateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_CODE);
            }
        });
        getImage(mUser.getUid());
        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savechanges();
            }
        });






    }

    private void savechanges() {
        startActivity(new Intent(ProfileUpdate.this, PostListActivity.class));
        finish();

        /*StorageReference imagePath = mFirebaseStorage.child("MBlog_Profile_Pics")
                .child(resultUri.getLastPathSegment());

        imagePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                String userid = mAuth.getCurrentUser().getUid();
                Uri downloadurl = taskSnapshot.getDownloadUrl();

                DatabaseReference mDatabaseReference = mDatabase.getReference();
                DatabaseReference currenUserDb = mDatabaseReference.child(userid);
                currenUserDb.child("image").setValue(downloadurl.toString());
                startActivity(new Intent(ProfileUpdate.this, PostListActivity.class));


            }
        });*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            mImageUri = data.getData();
            CropImage.activity(mImageUri)
                    .setAspectRatio(1,1).setGuidelines(CropImageView.Guidelines.ON).start(this);

        }
        if(requestCode== CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK)
            {
                resultUri = result.getUri();
                updateImage.setImageURI(resultUri);

            }
            else if(resultCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception error = result.getError();
            }
        }
    }

    public void getImage(String id)
    {
        final String uid = id;

        DatabaseReference mDatabaseReference = mDatabase.getReference();

        DatabaseReference ref = mDatabaseReference.child("MUsers").child(uid);

        ref.addValueEventListener(new ValueEventListener() {

            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {

                Users user = dataSnapshot.getValue(Users.class);
                try
                {
                    //updateName.setText(uid);
                   updateName.setText(user.getFirstname() + " "+ user.getLastname());
                    //updateImage.setImageURI(Uri.parse(user.getImage()));
                    Picasso.with(getApplicationContext()).load(user.getImage()).into(updateImage);

                }
                catch (Exception e)
                {

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}
