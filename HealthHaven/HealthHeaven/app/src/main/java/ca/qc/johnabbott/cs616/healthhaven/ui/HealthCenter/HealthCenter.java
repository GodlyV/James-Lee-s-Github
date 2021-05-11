package ca.qc.johnabbott.cs616.healthhaven.ui.HealthCenter;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.AlertDialog;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ca.qc.johnabbott.cs616.healthhaven.R;
import ca.qc.johnabbott.cs616.healthhaven.model.CRUDType;
import ca.qc.johnabbott.cs616.healthhaven.model.DatabaseHandler;
import ca.qc.johnabbott.cs616.healthhaven.model.Log;
import ca.qc.johnabbott.cs616.healthhaven.model.LogCategory;
import ca.qc.johnabbott.cs616.healthhaven.networking.HttpRequest;
import ca.qc.johnabbott.cs616.healthhaven.networking.HttpRequestTask;
import ca.qc.johnabbott.cs616.healthhaven.sqlite.DatabaseException;
import ca.qc.johnabbott.cs616.healthhaven.ui.Calendar.CalendarActivity;
import ca.qc.johnabbott.cs616.healthhaven.ui.Contact.Contacts;
import ca.qc.johnabbott.cs616.healthhaven.ui.HealthHavenApplication;
import ca.qc.johnabbott.cs616.healthhaven.ui.MedicalID.MedicalIDActivity;
import ca.qc.johnabbott.cs616.healthhaven.ui.adapter.LogAdapter;
import ca.qc.johnabbott.cs616.healthhaven.ui.util.AddLogDialogFragment;
import es.dmoral.toasty.Toasty;

/**
 * Health Center/Hub. Keeps track of different types of fitness logs
 *  specified in LogCategory.
 *
 * @author      William Fedele
 * @studentID   1679513
 * @githubUser  william-fedele
 */

public class HealthCenter extends AppCompatActivity {

    //region Globals
    private OnLogListener onLogListener;

    private PieChart chart;
    private LogAdapter logAdapter;
    private RecyclerView logRecyclerView;
    private Boolean grouped = false;

    private String serverAddress;
    private HealthHavenApplication globalApp;
    private String userUuid;
    private List<Pair<CRUDType, Log>> tempData;
    //endregion

    //region Interfaces
    public interface OnLogListener {
        void LogAdded(Log log);
        void LogUpdated(Log log);
        void LogDeleted(Log log);
    }
    public interface OnLogTouchedListener {
        void onLogTouched(Log log);
    }
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_hub);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        globalApp = (HealthHavenApplication) this.getApplication();
        serverAddress = globalApp.getConnectionString();
        userUuid = globalApp.getUserUuid();

        chart = findViewById(R.id.chart);
        chart.setDragDecelerationFrictionCoef(1);
        chart.setHoleRadius(30f);
        chart.setTransparentCircleRadius(0f);

        logRecyclerView = findViewById(R.id.log_RecyclerView);
        tempData = new ArrayList<>();

        //region BottomNavBar

        // Grab navbar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        // Set the appropriate menu item to highlight it
        MenuItem item = bottomNavigationView.getMenu().getItem(3).setChecked(true);

        // Menu item listener
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {

            // Switch case for every possibility
            // MAKE SURE TO NOT MAKE AN INTENT FOR THE CURRENT ACTIVITY.
            Intent intent = null;
            switch(menuItem.getItemId()) {
                case R.id.medicalID_MenuItem:
                    intent = new Intent(HealthCenter.this, MedicalIDActivity.class);
                    break;
                case R.id.emergency_MenuItem:
                    intent = new Intent(HealthCenter.this, Contacts.class);
                    break;
                case R.id.reminder_MenuItem:
                    intent = new Intent(HealthCenter.this, CalendarActivity.class);
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
        });

        //endregion

        onLogListener = new OnLogListener() {
            @Override
            public void LogAdded(Log log) {
                if(isNetworkAvailable()) {
                    postLogAsync(log);
                }
                else {
                    tempData.add(new Pair<>(CRUDType.CREATE, log));
                    Toasty.info(getApplicationContext(), "Creation stashed. Sync to upload.", Toast.LENGTH_SHORT, true).show();
                }
            }

            @Override
            public void LogUpdated(Log log) {
                if(isNetworkAvailable()) {
                    updateLogAsync(log);
                }
                else {
                    tempData.add(new Pair<>(CRUDType.UPDATE, log));
                    Toasty.info(getApplicationContext(), "Modification stashed. Sync to upload.", Toast.LENGTH_SHORT, true).show();
                }
            }

            @Override
            public void LogDeleted(Log log) {
                if(isNetworkAvailable()) {
                    deleteLogAsync(log);
                }
                else {
                    tempData.add(new Pair<>(CRUDType.DELETE, log));
                    Toasty.info(getApplicationContext(), "Deletion stashed. Sync to upload.", Toast.LENGTH_SHORT, true).show();
                }
            }
        };


        if(isNetworkAvailable()) {
            // pull from server
            getLogsAsync();
            Toasty.success(getApplicationContext(), "Pulling logs from the server...", Toast.LENGTH_SHORT, true).show();
        }
        else {
            // show whats stored on the device
            refreshPieChart();
            Toasty.warning(getApplicationContext(), "No internet connection. Showing only locally stored logs.", Toast.LENGTH_SHORT, true).show();
        }
    }

    //region New Log Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_health_hub,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.addLog_MenuItem:
                showLogDialogFragment();
                break;
            case R.id.sync_MenuItem:
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setMessage("Are you sure you want to sync with the server?")
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("Yes", (dialogInterface, i) -> {
                            if(isNetworkAvailable()) {
                                syncData();
                            }
                            else {
                                Toasty.error(getApplicationContext(), "A connection could not be made. Any changes will be lost when the app is shut down.", Toast.LENGTH_LONG, true).show();
                            }

                        })
                        .create();
                dialog.show();
                break;
            case R.id.reset_MenuItem:
                if(isNetworkAvailable()) {
                    // pull from server
                    getLogsAsync();
                }
                else {
                    // show whats stored on the device
                    refreshPieChart();
                }
        }

        return super.onOptionsItemSelected(item);
    }
    //endregion

    //region Log
    
    /**
     * Show the log dialog fragment with an empty context for a new note
     */
    private void showLogDialogFragment() {
        AddLogDialogFragment addLogDialogFragment = new AddLogDialogFragment();
        addLogDialogFragment.setOnLogAddedListener(onLogListener);
        addLogDialogFragment.show(getSupportFragmentManager(), null);
    }

    /**
     * Show the log dialog fragment with a log for context to update
     * @param log
     */
    private void showLogDialogFragment(Log log) {
        AddLogDialogFragment addLogDialogFragment = new AddLogDialogFragment();
        addLogDialogFragment.setOnLogAddedListener(onLogListener);
        addLogDialogFragment.setData(log);
        addLogDialogFragment.show(getSupportFragmentManager(), null);
    }

    /**
     * Get all logs in the local database
     * @return list of logs
     */
    private List<Log> getLogs() {
        List<Log> logs = new ArrayList<>();
        try {
            logs = DatabaseHandler.getInstance(getApplicationContext()).getLogTable().readAll();
            setLogAdapter(logs);

        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        return logs;
    }

    /**
     * Make a server call to retrieve all logs, then setup adapter and initialize pie chart.
     */
    private void getLogsAsync() {

        String userLogsAddress = serverAddress + "/log";

        HttpRequest request = new HttpRequest(userLogsAddress, HttpRequest.Method.GET);
        HttpRequestTask httpRequestTask = new HttpRequestTask();
        httpRequestTask.setOnResponseListener(data -> {
            if(data.getResponseCode() != 200)
                return;

            List<Log> logs = new LinkedList<>(Arrays.asList(Log.parseArray(data.getResponseBody())));
            // set adapter for recyclerview
            setLogAdapter(logs);
            // group logs by their category and setup the pie chart data.
            setupPieChart(groupLogs(logs));
            replaceLocalDb(logs);
            // success
        });
        httpRequestTask.setOnErrorListener(error -> {
            Toasty.error(getApplicationContext(), "An error occurred when pulling logs from the server.", Toast.LENGTH_LONG, true).show();
            // failed
        });
        httpRequestTask.execute(request);
    }

    /**
     * Add a new log to the server database
     * @param log
     */
    private void postLogAsync(Log log) {

        String userLogsAddress = serverAddress + "/log";

        HttpRequest request = new HttpRequest(userLogsAddress, HttpRequest.Method.POST);
        request.setRequestBody("application/json", log.format());
        HttpRequestTask httpRequestTask = new HttpRequestTask();
        httpRequestTask.setOnResponseListener(data -> {
            getLogsAsync();
            Toasty.success(getApplicationContext(), "Log created.", Toast.LENGTH_SHORT, true).show();
            // success
        });
        httpRequestTask.setOnErrorListener(error -> {
            Toasty.error(getApplicationContext(), "An error occurred when attempting to create a log.", Toast.LENGTH_SHORT, true).show();
            // failed
        });
        httpRequestTask.execute(request);
    }

    /**
     * Update a log on the server
     * @param log with modified fields to be updated on the server
     */
    private void updateLogAsync(Log log) {
        String userLogsAddress = serverAddress + "/log/" + log.getUuid();

        HttpRequest request = new HttpRequest(userLogsAddress, HttpRequest.Method.PUT);
        request.setRequestBody("application/json", log.format());
        HttpRequestTask httpRequestTask = new HttpRequestTask();
        httpRequestTask.setOnResponseListener(data -> {
            getLogsAsync();
            Toasty.success(getApplicationContext(), "Log updated.", Toast.LENGTH_SHORT, true).show();
        });
        httpRequestTask.setOnErrorListener(error -> {
            Toasty.error(getApplicationContext(), "An error occurred when attempting to update a log.", Toast.LENGTH_SHORT, true).show();
            // failed
        });
        httpRequestTask.execute(request);
    }

    /**
     * Delete a log from the server
     * @param log with a valid uuid
     */
    private void deleteLogAsync(Log log) {
        String userLogsAddress = serverAddress + "/log/" + log.getUuid();

        HttpRequest request = new HttpRequest(userLogsAddress, HttpRequest.Method.DELETE);
        HttpRequestTask httpRequestTask = new HttpRequestTask();
        httpRequestTask.setOnResponseListener(data -> {
            getLogsAsync();
            Toasty.success(getApplicationContext(), "Log deleted.", Toast.LENGTH_SHORT, true).show();
        });
        httpRequestTask.setOnErrorListener(error -> {
            Toasty.error(getApplicationContext(), "An error occurred when attempting to delete a log.", Toast.LENGTH_SHORT, true).show();
            // failed
        });
        httpRequestTask.execute(request);
    }

    /**
     * Given a list of logs, group them together based on their category, adding durations.
     * @param logs
     * @return Map with the category name string as key, total duration as value.
     */
    private Map<String, Float> groupLogs(List<Log> logs) {
        Map<String, Float> logsGrouped = new HashMap<>();
        for ( Log log : logs ) {
            // if they map contains the category already as a key, add the logs duration to the existing duration
            if(logsGrouped.containsKey(log.getType().toString())) {
                logsGrouped.put(log.getType().toString(), logsGrouped.get(log.getType().toString()) + (float)log.getDuration());
            }
            else {
                logsGrouped.put(log.getType().toString(), (float)log.getDuration());
            }
        }
        return logsGrouped;
    }

    /**
     * Given a category, read all logs into memory and remove any that are not in that category
     * @param logCategory
     */
    private void getLogsByCategory(LogCategory logCategory) {
        List<Log> logs = getLogs();
        logs.removeIf(p -> p.getType() != logCategory);
        setupPieChart(groupLogs(logs));
    }

    /**
     * Setup adapter and recycler view for logs
     * @param data
     */
    public void setLogAdapter(List<Log> data) {
        logAdapter = new LogAdapter(data, log -> showLogDialogFragment(log));

        logRecyclerView.setAdapter(logAdapter);
        logRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
    }

    //endregions

    //region Pie Chart

    /**
     * Setup pie chart
     * @param logMap
     */
    private void setupPieChart(Map<String, Float> logMap) {

        // Convert map of log data to compatible chart format
        List<PieEntry> pieEntryList = new ArrayList<>();
        for (Map.Entry<String, Float> entry : logMap.entrySet() ) {
            pieEntryList.add(new PieEntry(entry.getValue(), entry.getKey()));
        }

        PieDataSet dataSet = new PieDataSet(pieEntryList, "");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data = new PieData(dataSet);

        chart.setData(data);
        chart.setOnChartValueSelectedListener(new com.github.mikephil.charting.listener.OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                // this function is called when a slice of the pie is clicked

                // if you're already grouped, ungroup and show all logs
                if(grouped) {
                    refreshPieChart();
                    grouped = false;
                }
                else {
                    PieEntry pe = (PieEntry) e;
                    getLogsByCategory(LogCategory.fromString(pe.getLabel()));
                    grouped = true;
                }

            }

            @Override
            public void onNothingSelected() {

            }
        });
        Description description = chart.getDescription();
        description.setText("Fitness Data");

        chart.animateY(1000, Easing.EaseInOutExpo);

        // Invalidation is unnecessary when animation is called
        // chart.invalidate();
    }

    /**
     * Refresh pie chart if values might have changed.
     */
    private void refreshPieChart() {
        List<Log> logs = getLogs();
        setupPieChart(groupLogs(logs));
    }
    //endregion

    //region Misc Methods

    /**
     * Check if the device currently has network capability
     * @return
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * Empty temporary data into the server
     */
    private void syncData() {
        if (tempData.size() <= 0) {
            Toasty.info(getApplicationContext(), "There are no changes to sync with the server.", Toast.LENGTH_SHORT, true).show();
            return;
        }
        for (Pair p: tempData) {
            switch((CRUDType)p.first) {
                case CREATE:
                    postLogAsync((Log) p.second);
                    break;
                case UPDATE:
                    updateLogAsync((Log) p.second);
                    break;
                case DELETE:
                    deleteLogAsync((Log) p.second);
                    break;
            }
        }
        tempData = new ArrayList<>();
        Toasty.success(getApplicationContext(), "Successfully synced changes with the server.", Toast.LENGTH_SHORT, true).show();
    }

    /**
     * Clear logs from local db and replace with new list of logs
     * @param logs
     */
    private void replaceLocalDb(List<Log> logs) {
        DatabaseHandler.getInstance(getApplicationContext()).deleteAllLogs();
        DatabaseHandler.getInstance(getApplicationContext()).addListLogs(logs);
    }

    //endregion

}
