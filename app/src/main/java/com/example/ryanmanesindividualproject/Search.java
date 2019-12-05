package com.example.ryanmanesindividualproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Search extends AppCompatActivity implements View.OnClickListener {

    Button buttonSearch, buttonAddImportance;
    EditText editTextZipSearch;
    TextView textViewResultsBird, textViewResultsUserEmail, textViewResultsImportance;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        buttonSearch = findViewById(R.id.buttonSearch);
        editTextZipSearch = findViewById(R.id.editTextZipSearch);
        textViewResultsBird = findViewById(R.id.textViewResultsBird);
        textViewResultsUserEmail = findViewById(R.id.textViewResultsUserEmail);
        textViewResultsImportance = findViewById(R.id.textViewResultsImportance);
        buttonAddImportance = findViewById(R.id.buttonAddImportance);

        buttonSearch.setOnClickListener(this);
        buttonAddImportance.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
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
        } else if (item.getItemId() == R.id.itemLogout){
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
            mAuth.signOut();
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("Birds");

        if (v == buttonSearch) {
            String findZipCode = editTextZipSearch.getText().toString();
            Toast.makeText(this, findZipCode, Toast.LENGTH_SHORT).show();
            myRef.orderByChild("zip").equalTo(findZipCode).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Bird findBird = dataSnapshot.getValue(Bird.class);
                    String findBirdName = findBird.birdName;
                    String findBirdUserEmail = findBird.email;
                    Integer findBirdImportance = findBird.importance;
                    textViewResultsBird.setText(findBirdName);
                    textViewResultsUserEmail.setText(findBirdUserEmail);
                    textViewResultsImportance.setText(String.valueOf(findBirdImportance));
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
        } else if (v == buttonAddImportance) {
            final String findZipCode = editTextZipSearch.getText().toString();
            myRef.orderByChild("zip").equalTo(findZipCode).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Bird findBird = dataSnapshot.getValue(Bird.class);
                    String editKey =dataSnapshot.getKey();

                    String findBirdName = findBird.birdName;
                    String findBirdUserEmail = findBird.email;
                    Integer findBirdImportance = findBird.importance;
                    Integer newImportance = findBirdImportance + 1;

                    Bird editBird = new Bird(findBirdUserEmail,findBirdName,findZipCode,newImportance);
                    myRef.child(editKey).setValue(editBird);
                    Toast.makeText(Search.this, "Added 1 to Importance", Toast.LENGTH_SHORT).show();
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
    }
}
