package ca.qc.johnabbott.cs616.healthhaven.ui.util;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ca.qc.johnabbott.cs616.healthhaven.R;
import ca.qc.johnabbott.cs616.healthhaven.model.Reminder;
import ca.qc.johnabbott.cs616.healthhaven.ui.Calendar.CalendarFragment;

/**
 * A Dialog Fragment for adding and editing a Reminder
 *
 * @author      Brandon Cameron
 * @studentID   1770091
 * @githubUser  brcameron
 */

public class AddReminderDialogFragment extends AppCompatDialogFragment {

    private Calendar dateCalendar = Calendar.getInstance();
    private Calendar timeCalendar;
    private final String SET_DATE = "Set Date";
    private final String SET_TIME = "Set Time";
    private long id;
    private boolean timeSet = false;
    private String repeats;
    private String uuid;

    public interface OnAddReminderDialogFragmentListener{
        void onAddReminder(String type, Reminder reminder);
    }

    // Date Format
    final SimpleDateFormat dateFormat = new SimpleDateFormat("'Date: 'EEEE, MMMM d");

    // Time Format
    final SimpleDateFormat timeFormat = new SimpleDateFormat("'Time: 'hh:mm a");

    private TextView dialogTitleTextView;
    private EditText titleEditText;
    private EditText bodyEditText;
    private TextView dateTextView;
    private TextView timeTextView;
    private TextView repeatTextView;
    private TextView repeatTitleTextView;
    private long repeatId = -1;
    private OnAddReminderDialogFragmentListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.layout_add_reminder_dialog, null);

        dialogTitleTextView = view.findViewById(R.id.reminderDialog_TextView);
        titleEditText = view.findViewById(R.id.addTitle_EditText);
        bodyEditText = view.findViewById(R.id.addBody_EditText);
        dateTextView = view.findViewById(R.id.addDate_TextView);
        timeTextView = view.findViewById(R.id.addTime_TextView);
        repeatTextView = view.findViewById(R.id.repeatRemindersDialog_TextView);
        repeatTitleTextView = view.findViewById(R.id.repeatTitleDialog_TextView);
        id = -2;

        // Get title of dialog
        String reminderTitle = getArguments().getString("reminderTitle");
        dialogTitleTextView.setText(reminderTitle + " Reminder:");

        // Get Date
        Date tmpDate = new Date(getArguments().getLong("currentDate"));
        dateCalendar.setTime(tmpDate);
        dateTextView.setText(dateFormat.format(tmpDate));

        repeatTextView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                setRepeatOnClick();
            }
        });

        repeatTitleTextView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                setRepeatOnClick();
            }
        });

        // If editing, populate fields
        if (getArguments().getBoolean("edit")){

            // Title
            titleEditText.setText(getArguments().getString("title"));

            // Body
            bodyEditText.setText(getArguments().getString("body"));

            // Reminder ID
            id = getArguments().getLong("id");

            uuid = getArguments().getString("uuid");

            // If we have a time set
            if (getArguments().getBoolean("hasTime")){

                // Get the Reminder Time
                Date tmpTime = new Date(getArguments().getLong("time"));
                timeCalendar = Calendar.getInstance();
                timeCalendar.setTime(tmpTime);
                timeTextView.setText(timeFormat.format(tmpTime));

                // Time has been set so don't change the time anymore unless explicitly done so
                timeSet = true;
            }

            // Does the reminder repeat
            if(getArguments().getBoolean("hasRepeats")){

                // Get the Repeat ID
                repeatId = getArguments().getLong("repeatId");

                // Get the actual repeats
                repeats = getArguments().getString("repeats");

                repeatTextView.setText(repeats.replace(",",", "));
            }
        }

        builder.setView(view)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton(reminderTitle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String title = titleEditText.getText().toString();
                        String body = bodyEditText.getText().toString();

                        // Never actually set a time
                        if (!timeSet){
                            timeCalendar = null;
                        }

                        if (title.equals("")){
                            title = "New Reminder";
                        }

                        // Update before setting reminder
                        repeats = repeatTextView.getText().toString().replace(" ","");

                        // Not blank repeats and no repeat id has been set yet
                        if (! repeats.equals("") && repeatId == -1){
                            repeatId = id;
                        }

                        Reminder reminder = new Reminder()
                                .setId(id)
                                .setUuid(uuid)
                                .setTitle(title)
                                .setBody(body)
                                .setDate(dateCalendar.getTime())
                                .setTime(timeCalendar == null ? null : timeCalendar.getTime())
                                .setHasTime(timeCalendar != null)
                                .setRepeatId(repeatId)
                                .setRepeats(repeats)
                                .setHasRepeat(repeatId != -1 && ! repeats.equals(""));

                        if (id != -1){
                            reminder.setId(id);
                        }

                        listener.onAddReminder(reminderTitle, reminder);
                    }
                });

        dateTextView.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                DatePickerDialogFragment dialogFragment = DatePickerDialogFragment.create(dateCalendar.getTime(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Set Date
                        dateCalendar.set(Calendar.YEAR, year);
                        dateCalendar.set(Calendar.MONTH, month);
                        dateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        dateTextView.setText(dateFormat.format(dateCalendar.getTime()));
                    }
                });
                dialogFragment.show(getFragmentManager(), "datePicker");
            }
        });

        timeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if (!timeSet){
                    timeCalendar = Calendar.getInstance();
                }

                TimePickerDialogFragment timePickerDialogFragment = TimePickerDialogFragment.create(timeCalendar.getTime(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Set Time
                        timeCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        timeCalendar.set(Calendar.MINUTE, minute);
                        timeTextView.setText(timeFormat.format(timeCalendar.getTime()));
                        timeSet = true;
                    }
                });
                timePickerDialogFragment.show(getFragmentManager(), "timePicker");
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (OnAddReminderDialogFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement OnAddReminderDialogFragmentListener");
        }
    }

    private void setRepeatOnClick(){
        CalendarFragment calendarFragment = (CalendarFragment) getFragmentManager().findFragmentById(R.id.fragment);
        repeats = repeatTextView.getText().toString().replace(" ","");
        calendarFragment.repeatReminders(new Reminder().setHasRepeat(true).setRepeats(repeats), false, repeatTextView);
    }
}
