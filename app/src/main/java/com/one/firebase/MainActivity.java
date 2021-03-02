package com.one.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
private DatabaseReference mDatabaseRef;
private Adapter adapter;
private ChildEventListener ChildEventListener;


private FirebaseAuth auth;
private FirebaseAuth.AuthStateListener authStateListener;
private final int SIGN_IN=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<UserHelper> msg;
        ListView list;
        list=findViewById(R.id.list);

        //Initialized the Root Node
        mDatabaseRef=FirebaseDatabase.getInstance().getReference().child("Data").child("Users");

        //Initialized Firebase Auth
        auth=FirebaseAuth.getInstance();
        msg=new ArrayList<>();

        //Custom ArrayAdapter
        adapter=new Adapter(this,R.layout.adapter,msg);
        list.setAdapter(adapter);



        //This will check whether the user is authenticated or not
        //and if they are not then the user will be redirected to
        //login page and ChildEvent Listener will be detached
        authStateListener= firebaseAuth -> {
            FirebaseUser user=firebaseAuth.getCurrentUser();
            if(user!=null){
                //User SignedIn
                UserLoggedIn();

            }else{
                //User SignedOut
                UserLoggedOut();
                // Choose authentication providers
                List<AuthUI.IdpConfig> providers = Collections.singletonList(
                        new AuthUI.IdpConfig.EmailBuilder().build());

                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .build(),
                        SIGN_IN);
            }
        };

    }

    private void UserLoggedOut() {
        adapter.clear();
        if(ChildEventListener!=null) {
            mDatabaseRef.removeEventListener(ChildEventListener);
            ChildEventListener=null;
        }
    }

    private void UserLoggedIn() {
        //If the User is Authenticated then ChildeventListener will
        //check and retrieve all the changes made in the realtime
        //database.

        if(ChildEventListener==null) {
            ChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    UserHelper data = snapshot.getValue(UserHelper.class);
                    adapter.add(data);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    UserHelper data = snapshot.getValue(UserHelper.class);
                    adapter.add(data);
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    UserHelper data = snapshot.getValue(UserHelper.class);
                    adapter.add(data);
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            };
            mDatabaseRef.addChildEventListener(ChildEventListener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //This will exit the app when the user presses back button
        //from the Login page.
        //Otherwise MainActivity Will endlessly thow the user onto the
        // Login Page
        if(requestCode==SIGN_IN){
            if(resultCode==RESULT_OK){
                Toast.makeText(this,"Signed In!",Toast.LENGTH_SHORT).show();
            }else if(resultCode==RESULT_CANCELED){
                Toast.makeText(this,"Sign in Canceled",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        auth.removeAuthStateListener(authStateListener);
        adapter.clear();
    }
}