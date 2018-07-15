package com.karimtimer.everyboardymotivate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class PostActivity extends AppCompatActivity {

    //private ImageButton imageBtn;
    //private static final int GALLERY_REQUEST_CODE = 2;
    private ImageButton imageBtn;
    private static final int GALLERY_REQUEST_CODE = 2;
    private Uri uri = null;
    private EditText textTitle;
    private EditText textDesc;
    private Button postBtn;
    private StorageReference storage;
    private FirebaseDatabase database;
    private DatabaseReference databaseRef;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;
    private FirebaseUser mCurrentUser;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set the view now
        setContentView(R.layout.activity_post);

        // initializing objects
        postBtn = findViewById(R.id.btn_post);
        textDesc = findViewById(R.id.textEntry);
        //  textTitle = findViewById(R.id.textTitle);
        storage = FirebaseStorage.getInstance().getReference();
        databaseRef = FirebaseDatabase.getInstance().getReference().child("Board");
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("users").child(mCurrentUser.getUid());


        // posting to Firebase
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PostActivity.this, "POSTING…", Toast.LENGTH_SHORT).show();
                // final String PostTitle = textTitle.getText().toString();
                final String PostDesc = textDesc.getText().toString();
                // do a check for empty fields
                if (!TextUtils.isEmpty(PostDesc)) {
                    @SuppressWarnings("VisibleForTests") final DatabaseReference newPost = databaseRef.push();
                    //adding post contents to database reference
                    mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // newPost.child("title").setValue(PostTitle);
                            newPost.child("desc").setValue(PostDesc);
                            newPost.child("name").setValue(dataSnapshot.child("name").getValue())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(PostActivity.this, "posted!", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(PostActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                } else {
                    Toast.makeText(PostActivity.this, "not working…", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}






