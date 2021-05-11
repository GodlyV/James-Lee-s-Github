package ca.qc.johnabbott.cs616.healthhaven.ui.adapter;

import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import ca.qc.johnabbott.cs616.healthhaven.R;
import ca.qc.johnabbott.cs616.healthhaven.model.Reminder;

/**
 * A Reminder ViewHolder which represents an individual reminder in a list of reminders
 *
 * @author      Brandon Cameron
 * @studentID   1770091
 * @githubUser  brcameron
 */

public class ReminderViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener{

    private final TextView titleTextView;
    private final TextView bodyTextView;
    private final TextView dateTimeTextView;
    private final View root;
    private final OnReminderListener onReminderListener;
    private boolean choosing;
    private long id;
    private int position;
    private Calendar longTouchTime;

    // Time in ms
    private final int LONG_TOUCH = 700;

    public static final SimpleDateFormat FORMAT = new SimpleDateFormat("hh:mm a");

    public ReminderViewHolder(@NonNull View root, OnReminderListener onReminderListener) {
        super(root);

        titleTextView = root.findViewById(R.id.title_TextView);
        bodyTextView = root.findViewById(R.id.body_TextView);
        dateTimeTextView = root.findViewById(R.id.reminder_TextView);
        this.onReminderListener = onReminderListener;
        this.root = root;

        root.setOnTouchListener(this);
    }

    public void set(Reminder reminder){

        dateTimeTextView.setText("");

        // Set Title, Body, and Time of a reminder
        titleTextView.setText(reminder.getTitle());
        bodyTextView.setText(reminder.getBody());

        if (reminder.isHasTime()){
            dateTimeTextView.setText(reminder.getTime() == null ? "" : FORMAT.format(reminder.getTime()));
        }


        // Get id and position to deal with viewholders outside of this class
        id = reminder.getId();
        position = getAdapterPosition();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        switch (motionEvent.getAction()){

            // Touch screen, we are now choosing
            case MotionEvent.ACTION_DOWN:
                choosing = true;
                longTouchTime = Calendar.getInstance();
                break;

            // Stop touching screen, if we were scrolling do nothing
            // Otherwise we were choosing, call onNoteTouch
            case MotionEvent.ACTION_UP:

                // Long touch
                if (Calendar.getInstance().getTimeInMillis() - longTouchTime.getTimeInMillis() > LONG_TOUCH){
                    onReminderListener.onLongReminderTouch(root, motionEvent, id, position);
                }
                // Regular touch
                else if (choosing){
                    onReminderListener.onReminderTouch(root, motionEvent, id, position);
                }
                choosing = false;
                break;

            // Started moving after we touched, we are scrolling; not choosing
            case MotionEvent.ACTION_MOVE:
                choosing = false;
                break;

        }
        return true;
    }
}
