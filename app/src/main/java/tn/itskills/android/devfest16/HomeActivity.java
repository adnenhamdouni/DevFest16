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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPosts = new ArrayList<>();

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
        //String name = dataSnapshot.child("name").getValue().toString();
        //Student student = dataSnapshot.child("student").getValue(Student.class);

        Student student = dataSnapshot.child("students").child("student").getValue(Student.class);

//                if (!name.isEmpty()) {
//                    mPostMessage.setText("name = "+name);
//                } else {
//                    mPostMessage.setText("No names");
//                }

//                if (student != null) {
//                    mPostMessage.setText("name = "+student.name);
//                } else {
//                    mPostMessage.setText("No students");
//                }

//                for (DataSnapshot child : dataSnapshot.getChildren()) {
//                    for (DataSnapshot single : child.getChildren()) {
//
//                        Object map = single.getValue();
//                        String a = map.toString();
//                        mPostMessage.append(a + "\n");
//                    }
//                }

//                Object post = dataSnapshot.child("user-posts").child(getUid()).getValue();
//
//                if (post != null) {
//                    mPostMessage.setText("post per user = "+post.toString());
//                } else {
//                    mPostMessage.setText("No posts");
//                }

        mPosts.clear();
        mPostMessage.setText("");
        for (DataSnapshot keys : dataSnapshot.child("posts").getChildren()) {
            //String to temporarily store the whole child of the inidividual user's DB node ----> It still produces different order of the keys
            mPost = keys.getValue(Post.class);

            mPosts.add(mPost);

            Log.i(TAG, "post:title = "+mPost.title);

            mPostMessage.append(mPost.author + ": " + mPost.title + "\n");

        }

//                //Map<String, Object> childUpdates = new HashMap<>();
//                String key = mDatabase.child("posts").push().getKey();
//                Object post = dataSnapshot.child("posts/"+ key).getValue();
//
//                mPostMessage.setText("key = " + key + "\n");

//                if (post != null) {
//                    mPostMessage.setText("childUpdates = " + post.toString());
//                } else {
//                    mPostMessage.setText("No childUpdates");
//                }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        // Getting Post failed, log a message
        Log.i(TAG, "loadPost:onCancelled");
        Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
        mPostMessage.setText("loadPost:onCancelled");
    }
}
