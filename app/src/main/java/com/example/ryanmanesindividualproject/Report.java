package com.example.ryanmanesindividualproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Report extends AppCompatActivity implements View.OnClickListener {

    EditText editTextBirdName, editTextZip;
    Button buttonReport;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        editTextBirdName = findViewById(R.id.editTextBirdName);
        editTextZip = findViewById(R.id.editTextZip);

        buttonReport = findViewById(R.id.buttonReport);

        buttonReport.setOnClickListener(this);

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

        if (v == buttonReport){
            String UserEmail = mAuth.getCurrentUser().getEmail();
            String ZipCode = editTextZip.getText().toString();
            String BirdName = editTextBirdName.getText().toString();

            Bird newSighting = new Bird (UserEmail,BirdName,ZipCode, 0);

            myRef.push().setValue(newSighting);

            Toast.makeText(this, "Sighting reported", Toast.LENGTH_SHORT).show();
        }
    }
}
