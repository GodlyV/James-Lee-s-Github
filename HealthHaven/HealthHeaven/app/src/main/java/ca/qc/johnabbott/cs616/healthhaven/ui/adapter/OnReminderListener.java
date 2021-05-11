package ca.qc.johnabbott.cs616.healthhaven.ui.adapter;

import android.view.MotionEvent;
import android.view.View;

/**
 * A Reminder Listener allows us to access the ViewHolder information from within our fragment
 * @author Brandon Cameron (1770091@johnabbottcollege.net)
 */

public interface OnReminderListener {
    void onReminderTouch(View root, MotionEvent motionEvent, long id, int position);

    void onLongReminderTouch(View root, MotionEvent motionEvent, long id, int position);
}
