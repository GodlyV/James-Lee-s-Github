package ca.qc.johnabbott.cs616.healthhaven.ui.Calendar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import ca.qc.johnabbott.cs616.healthhaven.R;
import ca.qc.johnabbott.cs616.healthhaven.model.DatabaseHandler;
import ca.qc.johnabbott.cs616.healthhaven.model.Reminder;
import ca.qc.johnabbott.cs616.healthhaven.sqlite.DatabaseException;
import ca.qc.johnabbott.cs616.healthhaven.ui.Contact.Contacts;
import ca.qc.johnabbott.cs616.healthhaven.ui.HealthCenter.HealthCenter;
import ca.qc.johnabbott.cs616.healthhaven.ui.MedicalID.MedicalIDActivity;
import ca.qc.johnabbott.cs616.healthhaven.ui.util.AddReminderDialogFragment;

/**
 * A Calendar Activity which handles adding and editing of reminders
 *
 * @author      Brandon Cameron
 * @studentID   1770091
 * @githubUser  brcameron
 */

public class CalendarActivity extends AppCompatActivity implements AddReminderDialogFragment.OnAddReminderDialogFragmentListener {

    // Types of action when AddReminderDialogFragment is successful
    public static final String ADD = "Add";
    public static final String EDIT = "Edit";

    private TextView titleTextView;
    private TextView bodyTextView;
    private TextView dateTimeTextView;
    private CalendarFragment fragment;
    private DatabaseHandler databaseHandler;
    private TextView repeatReminderTextView;
    private boolean simpleEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        titleTextView = findViewById(R.id.title_TextView);
        bodyTextView = findViewById(R.id.body_TextView);
        dateTimeTextView = findViewById(R.id.reminder_TextView);
        repeatReminderTextView = findViewById(R.id.repeatReminders_TextView);

        //region BottomNavBar
        // Grab navbar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        // Set the appropriate menu item to highlight it
        MenuItem item = bottomNavigationView.getMenu().getItem(2).setChecked(true);

        // Menu item listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                // Switch case for every possibility
                // MAKE SURE TO NOT MAKE AN INTENT FOR THE CURRENT ACTIVITY.
                Intent intent = null;
                switch(menuItem.getItemId()) {
                    case R.id.medicalID_MenuItem:
                        intent = new Intent(CalendarActivity.this, MedicalIDActivity.class);
                        break;
                    case R.id.emergency_MenuItem:
                        intent = new Intent(CalendarActivity.this, Contacts.class);
                        break;
                    case R.id.health_MenuItem:
                        intent = new Intent(CalendarActivity.this, HealthCenter.class);
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

    /**
     * On Create Options Menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_calendar,menu);
        return true;
    }

    /**
     * Add Reminder Menu Item
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            // ADD REMINDER
            case R.id.addReminder_MenuItem:
                fragment = (CalendarFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
                AddReminderDialogFragment addReminderDialogFragment = new AddReminderDialogFragment();

                Bundle args = new Bundle();
                args.putString("reminderTitle", "Add");
                args.putLong("currentDate", fragment.getCurrentDate().getTime());
                addReminderDialogFragment.setArguments(args);

                addReminderDialogFragment.show(getSupportFragmentManager(), "add reminder dialog");
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when we add/edit a reminder
     * Will add or edit depending on the requirements
     * Can modify repeated reminders, rearrange them, or add completely new ones
     * @param type
     * @param reminder
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onAddReminder(String type, Reminder reminder){

        fragment = (CalendarFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        boolean create = false;
        simpleEdit = false;

        try {
            if (type == ADD){

                if (! reminder.isHasRepeat()){
                    // No repeats; Create only one instance
                    DatabaseHandler.getInstance(fragment.getContext()).getReminderTable().create(reminder);

                    if (fragment.isNetworkAvailable()){
                        fragment.postReminderAsync(reminder);
                    }
                }

                create = true;
            }
            else if (type == EDIT){
// TODO Does not update date/time properly??? (1PM -> 1AM)

                // Edit first reminder
                DatabaseHandler.getInstance(fragment.getContext()).getReminderTable().update(reminder);

                if (fragment.isNetworkAvailable()){
                    fragment.updateReminderAsync(reminder);
                }

                // Edit all repeats
                if (reminder.isHasRepeat()){



                    // List of reminders to update
                    List<Reminder> toUpdate = fragment.getRepeats(reminder.getRepeatId());

                    // If there is more than one repeating reminder already created
                    if (toUpdate.size() > 1){
                        //=================================================================================
                        // Check to see if we've changed the days to repeat on
                        //=================================================================================

                        // Update old version of each reminder
                        Reminder oldReminder1 = DatabaseHandler.getInstance(fragment.getContext()).getReminderTable().read(toUpdate.get(0).getId());

                        // Update old version of each reminder
                        Reminder oldReminder2 = DatabaseHandler.getInstance(fragment.getContext()).getReminderTable().read(toUpdate.get(1).getId());

                        // Compare repeats to see if that's changed
                        if (!oldReminder1.getRepeats().equals(reminder.getRepeats()) || !oldReminder2.getRepeats().equals(reminder.getRepeats())){
                            //=================================================================================
                            // If it has we want to delete the old repeating reminders and make new ones
                            // =================================================================================

                            // Get list of reminders to delete
                            List<Reminder> toDelete = fragment.getRepeats(reminder.getRepeatId());

                            // Remove The Repeats
                            for (int i = 0; i < toDelete.size(); i++){

                                // Delete from Local DB
                                DatabaseHandler.getInstance(fragment.getContext()).getReminderTable().deleteByKey(toDelete.get(i).getId());

                                // Delete from Server DB
                                if (fragment.isNetworkAvailable()){
                                    fragment.deleteReminderAsync(toDelete.get(i));
                                }

                            }
                        }
                        else{
                            //=================================================================================
                            // Otherwise just update normally
                            //=================================================================================
                            simpleEdit = true;
                            for (int i=0; i< toUpdate.size(); i++){

                                // Update old version of each reminder
                                oldReminder1 = DatabaseHandler.getInstance(fragment.getContext()).getReminderTable().read(toUpdate.get(i).getId());

                                oldReminder1
                                        .setTitle(reminder.getTitle())
                                        .setBody(reminder.getBody())
                                        .setTime(reminder.getTime())
                                        .setHasTime(reminder.isHasTime());

                                // Update the DB
                                DatabaseHandler.getInstance(fragment.getContext()).getReminderTable().update(oldReminder1);

                                if (fragment.isNetworkAvailable()){
                                    fragment.updateReminderAsync(oldReminder1);
                                }
                            }
                        }
                    }

                    if (!simpleEdit){
                        // Editing; First item of a now repeated reminder, delete and create all new repeated reminders
                        DatabaseHandler.getInstance(fragment.getContext()).getReminderTable().deleteByKey(reminder.getId());

                        if (fragment.isNetworkAvailable()){
                            fragment.deleteReminderAsync(reminder);
                        }

                        create = true;
                    }

                }
                // If hasRepeat is false yet there is still a repeatId, disconnect from the chain
                else if (reminder.getRepeatId() != -1){
                    reminder.setRepeatId(-1);

                    DatabaseHandler.getInstance(fragment.getContext()).getReminderTable().update(reminder);

                    if (fragment.isNetworkAvailable()){
                        fragment.updateReminderAsync(reminder);
                    }

                }
            }
            repeatReminderTextView.setText("Repeat: " + reminder.getRepeats().replace(",",", "));
            fragment.updateAdapter();

            if (create && reminder.isHasRepeat()){

                // Format to get string value of day of week
                SimpleDateFormat format = new SimpleDateFormat("EEEE");

                // Create first instance
                DatabaseHandler.getInstance(fragment.getContext()).getReminderTable().create(reminder);


                // If this is a new reminder
                if (reminder.getRepeatId() < 0){
                    reminder.setRepeatId(reminder.getId());

                    // Update first instance
                    DatabaseHandler.getInstance(fragment.getContext()).getReminderTable().update(reminder);
                }

                if (fragment.isNetworkAvailable()){
                    fragment.postReminderAsync(reminder);
                }


                // Get calendar and set to reminder date
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(reminder.getDate());

                // Increment to Next Day So we don't create for current date again
                calendar.add(Calendar.DAY_OF_MONTH, 1);

                // Get current month
                int month = calendar.get(Calendar.MONTH);

                // Copy the data from reminder
                Reminder copyReminder = reminder
                        .clone()
                        .setId((long)-1)
                        .setUuid("");

                // Get list of days to repeats
                List<String> repeatDays = Arrays.asList(reminder.getRepeats().split(","));


                // Loop until the end of the month
                while (calendar.get(Calendar.MONTH) == month){

                    String dayString = format.format(calendar.getTime());

                    for (int i = 0; i<repeatDays.size(); i++){
                        // Match; Add Date
                        if (dayString.equals(repeatDays.get(i))){

                            // Update reminder Date
                            copyReminder.setDate(calendar.getTime());

                            // Create in DB
                            DatabaseHandler.getInstance(fragment.getContext()).getReminderTable().create(copyReminder);

                            // Create in Server DB
                            if (fragment.isNetworkAvailable()){
                                fragment.postReminderAsync(copyReminder);
                            }
                            break;
                        }
                    }

                    // Increment to Next Day
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                }
            }
            fragment.updateAdapter();
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }
}
