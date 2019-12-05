package com.example.ryanmanesindividualproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MostImportant extends AppCompatActivity {

    TextView textViewImpBird, textViewImpZip, textViewImpUserEmail, textViewImpImportance;

    //Create Firebase authentication variable, which will store instance of currently logged-in user
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_most_important);

        textViewImpBird = findViewById(R.id.textViewImpBird);
        textViewImpZip = findViewById(R.id.textViewImpZip);
        textViewImpUserEmail = findViewById(R.id.textViewImpUserEmail);
        textViewImpImportance = findViewById(R.id.textViewImpImportance);

        //Get instance of logged-in user to use for logging out
        mAuth = FirebaseAuth.getInstance();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("Birds");

        //Order ascending by "importance" property and use limitToLast(1) to pull most important bird from Firebase
        myRef.orderByChild("importance").limitToLast(1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //Store most important bird object
                Bird findBird = dataSnapshot.getValue(Bird.class);

                //Pull properties off of bird object from Firebase
                String findBirdName = findBird.birdName;
                String findBirdZip = findBird.zip;
                String findBirdUserEmail = findBird.email;
                Integer findBirdImportance = findBird.importance;

                //Update textViews to reflect Firebase values
                textViewImpBird.setText("Bird: "+findBirdName);
                textViewImpZip.setText("Zip Code: "+findBirdZip);
                textViewImpUserEmail.setText("Member Email: "+findBirdUserEmail);

                //Need to convert "Importance" integer to a String so I can concatenate it
                String stringImportance = String.valueOf(findBirdImportance);
                textViewImpImportance.setText("Importance Value: "+stringImportance);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.itemReport) {
            Intent HomeIntent = new Intent(this, Report.class);
            startActivity(HomeIntent);
        } else if (item.getItemId() == R.id.itemSearch){
            Intent SettingsIntent = new Intent(this, Search.class);
            startActivity(SettingsIntent);
        } else if (item.getItemId() == R.id.itemMostImportant){
            Intent MostImportantIntent = new Intent(this, MostImportant.class);
            startActivity(MostImportantIntent);
        } else if (item.getItemId() == R.id.itemLogout){
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();

            //Log out current user
            mAuth.signOut();

            //Redirect to MainActivity, which is the login screen
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}
