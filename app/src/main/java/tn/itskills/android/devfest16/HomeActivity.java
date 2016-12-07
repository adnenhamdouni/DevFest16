package tn.itskills.android.devfest16;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import tn.itskills.android.devfest16.models.Post;
import tn.itskills.android.devfest16.models.Student;

public class HomeActivity extends BaseActivity implements ValueEventListener, View.OnClickListener{

    private String TAG = "HomeActivity";

    private Button mSignOutButton, mReadPostMessage;
    private TextView mPostMessage;

    private DatabaseReference mDatabase;

    private Post mPost;
    private ArrayList<Post> mPosts;


    private Post mUserPost;
    private ArrayList<Post> mUserPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPosts = new ArrayList<>();
        mUserPosts = new ArrayList<>();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, NewPostActivity.class));

            }
        });

        initView();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.addValueEventListener(this);
    }


    private void initView() {
        mSignOutButton = (Button) findViewById(R.id.button_sign_out);
        mSignOutButton.setOnClickListener(this);

        mReadPostMessage = (Button) findViewById(R.id.button_read_messages);
        mReadPostMessage.setOnClickListener(this);

        mPostMessage = (TextView) findViewById(R.id.post_message);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_sign_out:
                signOut();
                break;
            case R.id.button_read_messages:
                //readPostMessage();
                break;
        }
    }

    private void readPostMessage() {
        for (Post post: mPosts) {
            mPostMessage.append("post:title = " + post.title + " ");
        }
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }



    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        // Get Post object and use the values to update the UI
        Log.i(TAG, "loadPost:onDataChange");
        String name = dataSnapshot.child("name").getValue().toString();
        mPostMessage.append("SimpleChild: Name = " + name + "\n");

        Student student1 = dataSnapshot.child("student").getValue(Student.class);
        mPostMessage.append("SimpleChild: Student:name = " + student1.name + "\n");

        Student student2 = dataSnapshot.child("students").child("student").getValue(Student.class);
        mPostMessage.append("SimpleChild: Students:Student:ame = " + student2.name + "\n");

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        // Getting Post failed, log a message
        Log.i(TAG, "loadPost:onCancelled");
        Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
        mPostMessage.setText("loadPost:onCancelled");
    }
}
