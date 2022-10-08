package com.arkislove.binmonitoringactivity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.PorterDuff;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView binNumberValue =  findViewById(R.id.binNumberValue);
        TextView binStatusValue =  findViewById(R.id.binStatusValue);
        TextView fillLevelValue = findViewById(R.id.fillLevelValue);
        TextView binLevel = findViewById(R.id.binLevel);
        ProgressBar binProgress = findViewById(R.id.binProgress);

        int binNumber = 4;

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Bins").child(String.valueOf(binNumber)).child("fill_level");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    String value = snapshot.getValue().toString();
                    binNumberValue.setText(String.valueOf(binNumber));
                    fillLevelValue.setText(value);
                    binLevel.setText(value + " %");

                    String flValueString = fillLevelValue.getText().toString();

                    int flValueInt = Integer.parseInt(flValueString);

                    fillLevelValue.setText(binLevel.getText());

                    binProgress.setMax(100);
                    binProgress.setProgress(flValueInt);

                    int green = 0xFF00FF00;
                    int yellow = 0xFFFFF000;
                    int orange = 0xFFFF8000;
                    int red = 0xFFFF0000;

                    if (flValueInt <= 25) {
                        binProgress.getIndeterminateDrawable().setColorFilter(green, PorterDuff.Mode.SRC_IN);
                        binProgress.getProgressDrawable().setColorFilter(green, PorterDuff.Mode.SRC_IN);
                    } else if (flValueInt <= 50) {
                        binProgress.getIndeterminateDrawable().setColorFilter(yellow, PorterDuff.Mode.SRC_IN);
                        binProgress.getProgressDrawable().setColorFilter(yellow, PorterDuff.Mode.SRC_IN);
                    } else if (flValueInt <= 90)
                    {
                        binProgress.getIndeterminateDrawable().setColorFilter(orange, PorterDuff.Mode.SRC_IN);
                        binProgress.getProgressDrawable().setColorFilter(orange, PorterDuff.Mode.SRC_IN);
                    } else
                    {
                        binProgress.getIndeterminateDrawable().setColorFilter(red, PorterDuff.Mode.SRC_IN);
                        binProgress.getProgressDrawable().setColorFilter(red, PorterDuff.Mode.SRC_IN);
                    }

                    if (flValueInt <= 90)
                        binStatusValue.setText(R.string.fillLevelLow);
                    else
                        binStatusValue.setText(R.string.fillLevelHigh);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}