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
import java.util.Arrays;
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
        mDatabaseRef=FirebaseDatabase.getInstance().getReference().child("Data").child("Users");
        auth=FirebaseAuth.getInstance();
        msg=new ArrayList<>();
        adapter=new Adapter(this,R.layout.adapter,msg);
        list.setAdapter(adapter);



        
        authStateListener= firebaseAuth -> {
            FirebaseUser user=firebaseAuth.getCurrentUser();
            if(user!=null){
                //User SignedIn
                UserLoggedIn();

            }else{
                //User SignedOut
                UserLoggedOut();
                // Choose authentication providers
                List<AuthUI.IdpConfig> providers = Arrays.asList(
                        new AuthUI.IdpConfig.EmailBuilder().build());

                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .setIsSmartLockEnabled(false)
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

        if(ChildEventListener==null) {
            ChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    UserHelper data = snapshot.getValue(UserHelper.class);
                    adapter.add(data);
                    //msg.add(data);
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
        if(requestCode==SIGN_IN){
            if(resultCode==RESULT_OK){
                Toast.makeText(this,"Sign In!",Toast.LENGTH_SHORT).show();
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