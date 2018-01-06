package com.tutorials.theblog.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tutorials.theblog.R;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1 ;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;
    private Button loginButton;
    private Button createActButton;
    private EditText emailField;
    private EditText passwordField;
   // private SignInButton googleBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //googleBtn = (SignInButton) findViewById(R.id.google_signin);
        loginButton = (Button) findViewById(R.id.login_btn);
        createActButton= (Button) findViewById(R.id.create_act_btn);
        emailField =(EditText) findViewById(R.id.loginEmailEt);
        passwordField=(EditText)findViewById(R.id.loginPasswordEt);
       // GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
         //       .requestEmail()
           //     .build();



        createActButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CreateAccountActivity.class));

            }
        });

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                mUser= firebaseAuth.getCurrentUser();

                if(mUser != null && mUser.isEmailVerified())
                {

                    //Toast.makeText(MainActivity.this,"Signed In", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(MainActivity.this, PostListActivity.class));
                    finish();
                }
                else
                {
                    mAuth.signOut();
                   // Toast.makeText(MainActivity.this,"Not Signed In", Toast.LENGTH_LONG).show();
                }

            }
        };

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!TextUtils.isEmpty(emailField.getText().toString())&& !TextUtils.isEmpty(passwordField.getText().toString()))
                {
                    String email= emailField.getText().toString();
                    String pwd= passwordField.getText().toString();
                    login(email,pwd);
                }
                else if(!TextUtils.isEmpty(emailField.getText().toString()))
                {
                    AlertDialog.Builder builder= new  AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Blank fields");
                    builder.setMessage("Please enter your Password");
                    builder.show();
                    builder.setCancelable(true);
                }
                else if(!TextUtils.isEmpty(passwordField.getText().toString()))
                {
                    AlertDialog.Builder builder= new  AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Blank field");
                    builder.setMessage("Please enter your Email");
                    builder.show();
                    builder.setCancelable(true);
                }
                else
                {
                    AlertDialog.Builder builder= new  AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Blank field");
                    builder.setMessage("Please enter your Email and Password");
                    builder.show();
                    builder.setCancelable(true);
                }

            }
        });
    }

    /*private void signIn() {
        Intent signInIntent = mAuth.Google
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }*/


    private void login(final String email, final String pwd) {

        mAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                {

                    mAuthListener = new FirebaseAuth.AuthStateListener() {
                        @Override
                        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                            mUser= firebaseAuth.getCurrentUser();

                            DatabaseReference mPostDatabase = FirebaseDatabase.getInstance().getReference().child("MUsers");

                            if(mUser.isEmailVerified())
                            {
                                DatabaseReference newPost = mPostDatabase.push();

                                Map<String, String> dataToSave = new HashMap<>();

                                dataToSave.put("Email", email);
                                dataToSave.put("Password", pwd);
                                newPost.setValue(dataToSave);
                                startActivity(new Intent(MainActivity.this, PostListActivity.class));
                                finish();



                                //Toast.makeText(MainActivity.this,"Signed In", Toast.LENGTH_LONG).show();

                            }
                            else
                            {
                                AlertDialog.Builder builder= new  AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("Verify Email-Id");
                                builder.setMessage("Please verify email sent to your email-id");
                                builder.show();
                                builder.setCancelable(true);

                                //Toast.makeText(MainActivity.this,"Please Verify your Email", Toast.LENGTH_LONG).show();
                            }

                        }
                    };


                }
                else
                {
                    AlertDialog.Builder builder= new  AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Invalid Input");
                    builder.setMessage("Email or Password is Incorrect");
                    builder.show();
                    builder.setCancelable(true);

                    //Toast.makeText(MainActivity.this, "Invalid", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_signout)
        {
            mAuth.signOut();
            Toast.makeText(MainActivity.this, "Signed Out", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_add).setVisible(false);
        menu.findItem(R.id.action_profile).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener != null)
        {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
