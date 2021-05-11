package ca.qc.johnabbott.cs616.healthhaven.ui.MedicalID;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ca.qc.johnabbott.cs616.healthhaven.R;
import ca.qc.johnabbott.cs616.healthhaven.ui.Contact.Contacts;
import ca.qc.johnabbott.cs616.healthhaven.ui.Emergency.EmergencyInformation;
import ca.qc.johnabbott.cs616.healthhaven.ui.HealthCenter.HealthCenter;
import ca.qc.johnabbott.cs616.healthhaven.ui.Calendar.CalendarActivity;

public class MedicalIDActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_id);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //region BottomNavBar
        // Grab navbar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        // Set the appropriate menu item to highlight it
        MenuItem item = bottomNavigationView.getMenu().getItem(0).setChecked(true);

        // Menu item listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                // Switch case for every possibility
                // MAKE SURE TO NOT MAKE AN INTENT FOR THE CURRENT ACTIVITY.
                Intent intent = null;
                switch(menuItem.getItemId()) {
                    case R.id.emergency_MenuItem:
                        intent = new Intent(MedicalIDActivity.this, Contacts.class);
                        break;
                    case R.id.reminder_MenuItem:
                        intent = new Intent(MedicalIDActivity.this, CalendarActivity.class);
                        break;
                    case R.id.health_MenuItem:
                        intent = new Intent(MedicalIDActivity.this, HealthCenter.class);
                        break;
                    default:
                        break;
                }
                // Will only be null if user selected the current activity
                if(intent != null) {
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });
        //endregion
    }

}
