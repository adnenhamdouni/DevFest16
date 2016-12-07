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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import tn.itskills.android.devfest16.models.Post;

public class HomeActivity extends BaseActivity implements ValueEventListener, View.OnClickListener {

    private String TAG = "HomeActivity";

    private Button mSignOutButton, mReadPostMessage;
    private TextView mPostMessage;

    private DatabaseReference mDatabase;

    DatabaseReference dbRefPosts, dbRefUserPosts;

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


        dbRefPosts = mDatabase.child("posts");
        dbRefUserPosts = mDatabase.child("user-posts")
                .child(getUid());



        Query queryRef1 = dbRefPosts.orderByChild("starCount");
        Query queryRef2 = dbRefPosts.orderByChild("starCount").equalTo(0);

        PerformQuery(queryRef2);


    }

    private void PerformQuery(Query queryRef) {
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        // do something with the individual "issues"
                        Log.i(TAG, "HomeActivity:onCreate ====> onDataChange: issue:title = " + issue.getValue());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                Log.i(TAG, "HomeActivity:onCreate ====> onChildAdded : "+snapshot.getKey() + " was " + snapshot.getValue() + " meters tall");

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
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
        for (Post post : mPosts) {
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

        mPosts.clear();
        mPostMessage.setText("");
        Log.i(TAG, "HomeActivity:onDataChange ====> Posts for all users");



        for (DataSnapshot keys : dataSnapshot.child("posts").getChildren()) {
            mPost = keys.getValue(Post.class);

            mPosts.add(mPost);

            Log.i(TAG, "HomeActivity:onDataChange ====> post:title = " + mPost.title);

            mPostMessage.append(mPost.author + ": " + mPost.title + "\n");

        }


        mUserPosts.clear();
        Log.i(TAG, "HomeActivity:onDataChange ====> Posts For user Uid " + getUid());
        for (DataSnapshot keys : dataSnapshot.child("user-posts").child(getUid()).getChildren()) {

            mUserPost = keys.getValue(Post.class);

            mUserPosts.add(mUserPost);
            Log.i(TAG, "HomeActivity:onDataChange ====> post:title " + mUserPost.title);

        }

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        // Getting Post failed, log a message
        Log.i(TAG, "loadPost:onCancelled");
        Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
        mPostMessage.setText("loadPost:onCancelled");
    }
}
