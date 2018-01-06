package com.tutorials.theblog.Activities;


import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.tutorials.theblog.Activities.PostListActivity;
import com.tutorials.theblog.R;

public class CreateAccountActivity extends AppCompatActivity {
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText password;
    private Button createAccountBtn;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private StorageReference mFirebaseStorage;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private ImageButton profilePic;
    private  Uri resultUri = null;
    private final static int GALLERY_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("MUsers");

        mAuth = FirebaseAuth.getInstance();

        mFirebaseStorage = FirebaseStorage.getInstance().getReference().child("MBlog_Profile_Pics");





        firstName = (EditText) findViewById(R.id.firstNameAct);
        lastName = (EditText) findViewById(R.id.lastNameAct);
        email = (EditText) findViewById(R.id.emailAct);
        password = (EditText) findViewById(R.id.passwordAct);
        profilePic = (ImageButton) findViewById(R.id.profilePic);

        createAccountBtn = (Button) findViewById(R.id.createAccountAct);
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_CODE);

            }
        });

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAccount();
            }
        });
    }

/*    private void sendVerificatonEmail() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(CreateAccountActivity.this, MainActivity.class));
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Verification Email Not Sent! Please Try Again", Toast.LENGTH_LONG).show();
                    overridePendingTransition(0,0);
                    finish();
                    overridePendingTransition(0,0);
                    startActivity(getIntent());
                }

            }
        });


    }*/

    private void createNewAccount() {

        final String name = firstName.getText().toString().trim();
        final String lname = lastName.getText().toString().trim();
        String em = email.getText().toString().trim();
        final String pwd = password.getText().toString().trim();
        if (pwd.length() < 6)
        {
            Toast.makeText(getApplicationContext(), "Please enter a password with atleast 6 characters", Toast.LENGTH_LONG).show();
        }



        if (!TextUtils.isEmpty(name)
                && !TextUtils.isEmpty(em) && !TextUtils.isEmpty(pwd)) {

                        mAuth.createUserWithEmailAndPassword(em,pwd).addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {


                                    StorageReference imagePath = mFirebaseStorage.child("MBlog_Profile_Pics")
                                            .child(resultUri.getLastPathSegment());

                                    imagePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                            String userid = mAuth.getCurrentUser().getUid();
                                            Uri downloadurl = taskSnapshot.getDownloadUrl();

                                            DatabaseReference currenUserDb = mDatabaseReference.child(userid);
                                            currenUserDb.child("firstname").setValue(name);
                                            currenUserDb.child("lastname").setValue(lname);
                                            currenUserDb.child("image").setValue(downloadurl.toString());

                                            //send users to postList


                                            Toast.makeText(getApplicationContext(), " Account Created Successfully", Toast.LENGTH_LONG).show();
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            user.sendEmailVerification().addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful())
                                                    {
                                                        Toast.makeText(getApplicationContext(), "Verification Email Sent", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });

                                            Intent intent = new Intent(CreateAccountActivity.this, MainActivity.class );
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);



                                        }
                        });


//                                String userid = mAuth.getCurrentUser().getUid();
//
//                                DatabaseReference currenUserDb = mDatabaseReference.child(userid);
//                                currenUserDb.child("firstname").setValue(name);
//                                currenUserDb.child("lastname").setValue(lname);
//                                currenUserDb.child("image").setValue("none");
//
//
//                                mProgressDialog.dismiss();
//
//                                //send users to postList
//                                Intent intent = new Intent(CreateAccountActivity.this, PostListActivity.class );
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startActivity(intent);



                            }

                        }
                    });


        }
        else if(TextUtils.isEmpty(name))
        {
            AlertDialog.Builder builder= new  AlertDialog.Builder(CreateAccountActivity.this);
            builder.setTitle("Blank fields");
            builder.setMessage("Please enter your First Name");
            builder.show();
            builder.setCancelable(true);
        }
        else if (!TextUtils.isEmpty(em))
        {
            AlertDialog.Builder builder= new  AlertDialog.Builder(CreateAccountActivity.this);
            builder.setTitle("Blank fields");
            builder.setMessage("Please enter your Email");
            builder.show();
            builder.setCancelable(true);
        }
        else if (!TextUtils.isEmpty(pwd))
        {
            AlertDialog.Builder builder= new  AlertDialog.Builder(CreateAccountActivity.this);
            builder.setTitle("Blank fields");
            builder.setMessage("Please enter your Email");
            builder.show();
            builder.setCancelable(true);
        }
        else
        {
            AlertDialog.Builder builder= new  AlertDialog.Builder(CreateAccountActivity.this);
            builder.setTitle("Blank fields");
            builder.setMessage("Please fill all the entries!!");
            builder.show();
            builder.setCancelable(true);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            Uri mImageUri = data.getData();

            CropImage.activity(mImageUri)
                    .setAspectRatio(1,1)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);



        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();

                profilePic.setImageURI(resultUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
