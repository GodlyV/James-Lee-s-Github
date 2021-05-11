package ca.qc.johnabbott.cs616.healthhaven.ui.Emergency;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ca.qc.johnabbott.cs616.healthhaven.R;
import ca.qc.johnabbott.cs616.healthhaven.ui.Calendar.CalendarActivity;
import ca.qc.johnabbott.cs616.healthhaven.ui.HealthCenter.HealthCenter;
import ca.qc.johnabbott.cs616.healthhaven.ui.MedicalID.MedicalIDActivity;

public class EmergencyInformation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_information);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

}
