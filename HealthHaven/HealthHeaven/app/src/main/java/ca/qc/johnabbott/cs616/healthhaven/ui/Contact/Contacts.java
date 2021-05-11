package ca.qc.johnabbott.cs616.healthhaven.ui.Contact;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;

import ca.qc.johnabbott.cs616.healthhaven.R;
import ca.qc.johnabbott.cs616.healthhaven.model.Contact;
import ca.qc.johnabbott.cs616.healthhaven.ui.Calendar.CalendarActivity;
import ca.qc.johnabbott.cs616.healthhaven.ui.Emergency.EmergencyInformation;
import ca.qc.johnabbott.cs616.healthhaven.ui.HealthCenter.HealthCenter;
import ca.qc.johnabbott.cs616.healthhaven.ui.MedicalID.MedicalIDActivity;

public class Contacts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //region BottomNavBar
        // Grab navbar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        // Set the appropriate menu item to highlight it
        MenuItem item = bottomNavigationView.getMenu().getItem(1).setChecked(true);

        // Menu item listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                // Switch case for every possibility
                // MAKE SURE TO NOT MAKE AN INTENT FOR THE CURRENT ACTIVITY.
                Intent intent = null;
                switch(menuItem.getItemId()) {
                    case R.id.medicalID_MenuItem:
                        intent = new Intent(Contacts.this, MedicalIDActivity.class);
                        break;
                    case R.id.reminder_MenuItem:
                        intent = new Intent(Contacts.this, CalendarActivity.class);
                        break;
                    case R.id.health_MenuItem:
                        intent = new Intent(Contacts.this, HealthCenter.class);
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


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contact newNote = new Contact();
                Intent intent = new Intent(getApplicationContext(), editContact.class);
                intent.putExtra(ContactsFragment.params.initial_contact,newNote);
                intent.putExtra("isNewContact",true);
                startActivityForResult(intent,1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Activity code is when we get the result of the intent
        if(resultCode == Activity.RESULT_OK){
            int location =data.getIntExtra("contactLocation",0);
            boolean isNewContact = data.getBooleanExtra("isNewContact",false);
            long listId = data.getLongExtra("contactId",-1);
            boolean delete = data.getBooleanExtra("delete",false);

            Contact contact = data.getParcelableExtra(ContactsFragment.results.final_contact);
            ContactsFragment contactsFragment = (ContactsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);

            contactsFragment.updateCurrentContacts(contact,location,isNewContact,listId,delete);
        }

    }
}
