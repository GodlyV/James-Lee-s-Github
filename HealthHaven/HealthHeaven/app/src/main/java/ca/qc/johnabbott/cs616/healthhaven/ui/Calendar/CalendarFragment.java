package ca.qc.johnabbott.cs616.healthhaven.ui.Calendar;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import ca.qc.johnabbott.cs616.healthhaven.R;
import ca.qc.johnabbott.cs616.healthhaven.model.DatabaseHandler;
import ca.qc.johnabbott.cs616.healthhaven.model.Reminder;
import ca.qc.johnabbott.cs616.healthhaven.networking.HttpRequest;
import ca.qc.johnabbott.cs616.healthhaven.networking.HttpRequestTask;
import ca.qc.johnabbott.cs616.healthhaven.networking.HttpResponse;
import ca.qc.johnabbott.cs616.healthhaven.sqlite.DatabaseException;
import ca.qc.johnabbott.cs616.healthhaven.ui.HealthHavenApplication;
import ca.qc.johnabbott.cs616.healthhaven.ui.adapter.OnReminderListener;
import ca.qc.johnabbott.cs616.healthhaven.ui.adapter.ReminderAdapter;
import ca.qc.johnabbott.cs616.healthhaven.ui.util.AddReminderDialogFragment;

/**
 * A Calendar fragment that interacts with dates and reminders
 *
 * @author      Brandon Cameron
 * @studentID   1770091
 * @githubUser  brcameron
 */

/**
 * TODO bugfix: PM -> AM
 * TODO: https://gist.github.com/BrandonSmith/6679223 send notifications (extra)
 *
 */
public class CalendarFragment extends Fragment implements OnReminderListener {

    private View root;
    private TextView repeatReminder;
    private long id;
    private int position;
    private ReminderAdapter adapter;
    private List<Reminder> data;
    private CalendarView calendarView;
    private Calendar currentSelectedDate;
    private RecyclerView reminderRecyclerView;
    private int currentPostion;
    private SwipeRefreshLayout refreshLayout;
    private HealthHavenApplication application;
    private String connectionString;

    public CalendarFragment() {
    }

    /**
     * On Create View
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_calendar, container, false);
        data = new ArrayList<Reminder>();

        calendarView = root.findViewById(R.id.calendar_CalendarView);

        repeatReminder = root.findViewById(R.id.repeatReminders_TextView);

        reminderRecyclerView = root.findViewById(R.id.reminder_RecyclerView);

        application = (HealthHavenApplication) getActivity().getApplication();
        connectionString = application.getConnectionString();

        refreshLayout = root.findViewById(R.id.refreshReminder_SwipeRefreshLayout);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.base0B), getResources().getColor(R.color.base0C), getResources().getColor(R.color.base0E), getResources().getColor(R.color.base09));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //application.getUrlUsers() + "/" + application.getUserUuid() + "/" + "notes"
                        getRemindersAsync();

                    }
                },1000);
            }
        });

        // Get Data
        calendarView.setDate(new Date().getTime());
        currentSelectedDate = Calendar.getInstance();
        try {
            // Get reminders on the same day that is selected
            data = getData();
        } catch (DatabaseException e) {
            e.printStackTrace();
        }

        reminderRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        // Calendar View
        calendarView.setOnDateChangeListener((calendarView, year, month, day) -> {
            currentSelectedDate = new GregorianCalendar(year,month,day);

            try {
                // Get reminders on the same day that is selected
                data = getData();
            } catch (DatabaseException e) {
                e.printStackTrace();
            }
            try {
                updateCurrentReminderPosition(-1);
            } catch (DatabaseException e) {
                e.printStackTrace();
            }
            updateAdapter();
        });

        adapter = new ReminderAdapter(data, this);
        reminderRecyclerView.setAdapter(adapter);
        reminderRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        return root;
    }

    /**
     * On reminder touch
     * @param root
     * @param motionEvent
     * @param id
     * @param position
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReminderTouch(View root, MotionEvent motionEvent, long id, int position) {

        // Edit Reminder
        this.root = root;
        this.id = id;
        this.position = position;

        // Update Current position and Data
        try {
            updateCurrentReminderPosition(position);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }

        // Retrieve reminder
        Reminder tmp = null;
        try {
            tmp = getData().get(position);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }

        // Use dialog to edit
        AddReminderDialogFragment addReminderDialogFragment = new AddReminderDialogFragment();

        Bundle args = new Bundle();
        args.putString("reminderTitle", "Edit");
        args.putLong("currentDate", currentSelectedDate.getTime().getTime());
        args.putBoolean("edit", true);
        args.putString("title", tmp.getTitle());
        args.putString("body", tmp.getBody());
        args.putLong("id", tmp.getId());
        args.putString("uuid", tmp.getUuid());

        args.putBoolean("hasTime", tmp.isHasTime());
        if (tmp.isHasTime()){
            args.putLong("time", tmp.getTime().getTime());
        }

        args.putBoolean("hasRepeats", tmp.isHasRepeat());
        if(tmp.isHasRepeat()){
            args.putLong("repeatId", tmp.getRepeatId());
            args.putString("repeats", tmp.getRepeats());
        }

        addReminderDialogFragment.setArguments(args);

        addReminderDialogFragment.show(getFragmentManager(), "edit reminder dialog");
    }

    /**
     * On long reminder touch
     * @param root
     * @param motionEvent
     * @param id
     * @param position
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onLongReminderTouch(View root, MotionEvent motionEvent, long id, int position) {

        // Edit Reminder
        this.root = root;
        this.id = id;
        this.position = position;

        final int xCoord = (int) motionEvent.getRawX();
        final int yCoord = (int) motionEvent.getRawY();

        try {
            updateCurrentReminderPosition(position);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }

        // Get current reminder
        Reminder tmp = data.get(position);

        getActivity().startActionMode(new ActionMode.Callback2() {
            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {

                // Set correct coordinates
                actionMode.getMenuInflater().inflate(R.menu.menu_reminder_delete, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {

                switch (menuItem.getItemId()){

                    /*case R.id.repeatReminders_MenuItem:
                        repeatReminders(tmp, true, null);
                        break;

                     */

                    // Delete only this reminder
                    case R.id.deleteReminder_MenuItem:
                        try {
                            DatabaseHandler.getInstance(getContext()).getReminderTable().deleteByKey(id);
                            data.remove(position);

                            if (isNetworkAvailable()) {
                                deleteReminderAsync(tmp);
                            }

                            // Remove from UI
                            //noteRecyclerView.removeViewAt(itemPosition);
                            adapter.notifyItemRemoved(position);
                            adapter.notifyItemRangeChanged(position, data.size());
                            updateCurrentReminderPosition(-1);
                        } catch (DatabaseException e) {
                            e.printStackTrace();
                        }
                        break;

                    // Delete all reminders with same reminder id
                    case R.id.deleteAllReminders_MenuItem:
                        try {
                            DatabaseHandler.getInstance(getContext()).getReminderTable().delete(tmp);

                            if (isNetworkAvailable()) {
                                deleteReminderAsync(tmp);
                            }

                            // List of reminders to delete, get before we delete current reminder
                            List<Reminder> toDelete = getRepeats(tmp.getRepeatId());

                            // Remove Current Reminder from data
                            data.remove(position);

                            // Remove reminder we just deleted from DB, don't need to delete again
                            toDelete.remove(tmp);

                            // Remove from UI
                            //noteRecyclerView.removeViewAt(itemPosition);
                            adapter.notifyItemRemoved(position);
                            adapter.notifyItemRangeChanged(position, data.size());


                            if (tmp.isHasRepeat() && tmp.getRepeatId() != -1){

                                // Remove The Repeats
                                for (int i = 0; i < toDelete.size(); i++){

                                    // Delete from Local DB
                                    DatabaseHandler.getInstance(getContext()).getReminderTable().deleteByKey(toDelete.get(i).getId());

                                    // Delete from Server DB
                                    if (isNetworkAvailable()) {
                                        // Change uuid to current reminder and delete
                                        tmp.setUuid(toDelete.get(i).getUuid());
                                        deleteReminderAsync(tmp);
                                    }

                                }
                            }
                            data = getData();
                            updateCurrentReminderPosition(-1);

                        } catch (DatabaseException e) {
                            e.printStackTrace();
                        }
                        break;

                    case R.id.deleteReminderMenuClose_MenuItem:
                        break;
                }

                updateAdapter();
                adapter.notifyDataSetChanged();
                reminderRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

                actionMode.finish();
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
                // Do nothing
            }

            @Override
            public void onGetContentRect(ActionMode mode, View view, Rect outRect) {
                // Set the popup where you touched
                outRect.set(xCoord,yCoord,xCoord,yCoord);
            }


        }, ActionMode.TYPE_FLOATING);
    }

    /**
     * @param lhs
     * @param rhs
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean isSameDay (Date lhs, Calendar rhs){
        LocalDate cmp = lhs.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return cmp.getDayOfMonth() == rhs.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * updates the data and adapter
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateAdapter(){
        try {
            data = getData();
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        adapter = new ReminderAdapter(data, this);
        reminderRecyclerView.setAdapter(adapter);
        reminderRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }

    /**
     * gets current date
     * @return currentDate
     */
    public Date getCurrentDate(){
        return currentSelectedDate.getTime();
    }

    /**
     * @param repeatId
     * @return list of reminders with same repeatId
     * @throws DatabaseException
     */
    public List<Reminder> getRepeats(long repeatId) throws DatabaseException {
        return DatabaseHandler.getInstance(getContext()).getReminderTable().readAll().stream().filter(d -> d.getRepeatId() == repeatId).collect(Collectors.toList());
    }

    /**
     * takes in a reminder to populate the checkboxes in the dialog,
     * pressing OK will save the current setup and will save it to the DB
     * @param tmp
     * @param editDB
     * @param dialogRepeat
     */
    public void repeatReminders(Reminder tmp, boolean editDB, TextView dialogRepeat){
        String[] selectedDays = tmp.getRepeats().split(",");

        // Build an AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // String array for alert dialog multi choice items
        String[] days = new String[]{
                "Sunday",
                "Monday",
                "Tuesday",
                "Wednesday",
                "Thursday",
                "Friday",
                "Saturday"
        };

        // Boolean array for initial selected items
        final boolean[] checkedDays = new boolean[]{
                false,  // Sunday
                false,  // Monday
                false,  // Tuesday
                false,  // Wednesday
                false,  // Thursday
                false,  // Friday
                false   // Saturday

        };

        for (int i = 0; i< days.length; i++){
            for (int j = 0; j < selectedDays.length; j++){
                if (days[i].equals(selectedDays[j])){
                    checkedDays[i] = true;
                    break;
                }
            }
        }

        builder.setMultiChoiceItems(days, checkedDays, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                // Update the current focused item's checked status
                checkedDays[which] = isChecked;

            }
        });
        builder.setCancelable(false);

        // Set the positive/yes button click listener
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String repeatDays = "";
                for (int i = 0; i<days.length; i++){
                    // If that day is checked
                    if (checkedDays[i]) {
                        // Append to the textView
                        repeatDays += days[i] + ", ";
                    }
                }

                // Get rid of last comma (only if we added something)
                for (int i = 0; i<checkedDays.length; i++){
                    if (checkedDays[i]){
                        repeatDays = repeatDays.substring(0, repeatDays.length()-2);
                        tmp.setHasRepeat(true);
                        tmp.setRepeats(repeatDays.replace(" ",""));
                        if (tmp.getRepeatId() == -1){
                            tmp.setRepeatId(tmp.getId());
                        }
                        break;
                    }
                    else{
                        tmp.setRepeats("");
                        tmp.setHasRepeat(false);
                        tmp.setRepeatId(-1);
                    }
                }

                if (editDB){
                    // Save new repeated days
                    try {
                        DatabaseHandler.getInstance(getContext()).getReminderTable().update(tmp);
                        updateReminderAsync(tmp);
                    } catch (DatabaseException e) {
                        e.printStackTrace();
                    }

                    // Update textView
                    repeatDays = "Repeat: " + repeatDays;
                    repeatReminder.setText(repeatDays);
                }
                // Updating dialog
                else{
                    dialogRepeat.setText(repeatDays);
                }
            }
        });

        // Set the neutral/cancel button click listener
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do something when click the neutral button
            }
        });

        AlertDialog dialog = builder.create();
        // Display the alert dialog on interface
        dialog.show();
    }

    /**
     * used to update/format the textView which shows what days a reminder repeats
     * @param position
     * @throws DatabaseException
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateCurrentReminderPosition(int position) throws DatabaseException {

        if (position >= 0){
            this.currentPostion = position;
            getRemindersAsync();
            this.data = getData();
            repeatReminder.setText("Repeat: " + data.get(position).getRepeats().replace(",",", "));
        }
        else{
            this.currentPostion = -1;
            repeatReminder.setText("Repeat: ");
        }

    }

    /**
     * gets local data
     * @return list of reminders that are on the same day
     * @throws DatabaseException
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Reminder> getData() throws DatabaseException {
        return DatabaseHandler.getInstance(getContext()).getReminderTable().readAll().stream().filter(d -> isSameDay(d.getDate(), currentSelectedDate)).collect(Collectors.toList());
    }

    /**
     * given an id will return a specific Reminder
     * @param id
     * @return
     * @throws DatabaseException
     */
    public Reminder getReminder(long id) throws DatabaseException{
        return DatabaseHandler.getInstance(getContext()).getReminderTable().read(id);
    }

    /**
     * use to check if we are connected to the internet before interacting with server
     * @return
     */
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * Make a server call to retrieve all reminders
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getRemindersAsync() {

        String userReminderAddress = connectionString + "/reminder";

        HttpRequest request = new HttpRequest(userReminderAddress, HttpRequest.Method.GET);
        HttpRequestTask httpRequestTask = new HttpRequestTask();
        httpRequestTask.setOnResponseListener(data -> {
            if(data.getResponseCode() != 200)
                return;

            onResponse(data);

            refreshLayout.setRefreshing(false);

            updateAdapter();
            // success
        });
        httpRequestTask.setOnErrorListener(error -> {
            refreshLayout.setRefreshing(false);
            android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(getActivity())
                    .setMessage("An error occurred while retrieving your reminders.")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            refreshLayout.setRefreshing(true);
                            getRemindersAsync();
                        }
                    })
                    .create();
            dialog.show();


        });
        httpRequestTask.execute(request);
    }

    /**
     * Add a new reminder to the server database
     * @param reminder
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void postReminderAsync(Reminder reminder) {

        String userReminderAddress = connectionString + "/reminder";

        HttpRequest request = new HttpRequest(userReminderAddress, HttpRequest.Method.POST);
        request.setRequestBody("application/json", reminder.format());
        HttpRequestTask httpRequestTask = new HttpRequestTask();
        httpRequestTask.setOnResponseListener(data -> {
            getRemindersAsync();
            //tempData.remove(reminder);
            // success

            // Capture uuid
            reminder.setUuid(data.getHeaders().get("Location").get(0));

            // Update local data
            try {
                updateReminder(reminder);
            } catch (DatabaseException e) {
                e.printStackTrace();
            }

        });
        httpRequestTask.setOnErrorListener(error -> {
            // failed
        });
        httpRequestTask.execute(request);
    }

    /**
     * Update a reminder on the server
     * @param reminder with modified fields to be updated on the server
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateReminderAsync(Reminder reminder) {
        String userLogsAddress = connectionString + "/reminder/" + reminder.getUuid();

        HttpRequest request = new HttpRequest(userLogsAddress, HttpRequest.Method.PUT);
        request.setRequestBody("application/json", reminder.format());
        HttpRequestTask httpRequestTask = new HttpRequestTask();
        httpRequestTask.setOnResponseListener(data -> {
            getRemindersAsync();
            //tempData.remove(reminder);

        });
        httpRequestTask.setOnErrorListener(error -> {
            // failed
        });
        httpRequestTask.execute(request);
    }

    /**
     * Delete a reminder from the server
     * @param reminder with a valid uuid
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void deleteReminderAsync(Reminder reminder) {
        String userReminderAddress = connectionString + "/reminder/" + reminder.getUuid();

        HttpRequest request = new HttpRequest(userReminderAddress, HttpRequest.Method.DELETE);
        HttpRequestTask httpRequestTask = new HttpRequestTask();
        httpRequestTask.setOnResponseListener(data -> {
            getRemindersAsync();
            //tempData.remove(reminder);

        });
        httpRequestTask.setOnErrorListener(error -> {
            // failed
        });
        httpRequestTask.execute(request);
    }

    /**
     * Clear reminders from local db and replace with reminders pulled from server
     * @param reminders
     */
    public void updateDB(List<Reminder> reminders) {
        DatabaseHandler.getInstance(getContext()).deleteAllReminders();
        DatabaseHandler.getInstance(getContext()).addListReminders(reminders);
    }

    /**
     * Given a reminder will update it locally
     * @param reminder
     * @throws DatabaseException
     */
    public void updateReminder(Reminder reminder) throws DatabaseException {
        DatabaseHandler.getInstance(getContext()).getReminderTable().update(reminder);
    }

    /**
     * Called on a successful response from an HttpRequest
     * Updates the Local DB with the Server DB
     * @param data
     */
    public void onResponse(HttpResponse data) {
        updateDB(new LinkedList<>(Arrays.asList(Reminder.parseArray(data.getResponseBody()))));

    }
}
