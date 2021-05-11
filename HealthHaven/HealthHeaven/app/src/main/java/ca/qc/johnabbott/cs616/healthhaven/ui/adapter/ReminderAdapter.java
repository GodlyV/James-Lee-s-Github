package ca.qc.johnabbott.cs616.healthhaven.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ca.qc.johnabbott.cs616.healthhaven.R;
import ca.qc.johnabbott.cs616.healthhaven.model.Reminder;

/**
 * A Reminder Adapter to handle reminder data for the RecyclerView and ViewHolder
 *
 * @author      Brandon Cameron
 * @studentID   1770091
 * @githubUser  brcameron
 */

public class ReminderAdapter extends RecyclerView.Adapter<ReminderViewHolder> {

    private List<Reminder> data;
    private OnReminderListener onReminderListener;

    public ReminderAdapter(List<Reminder> data, OnReminderListener onReminderListener){
        this.data = data;
        this.onReminderListener = onReminderListener;
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_reminders, parent, false);
        return new ReminderViewHolder(root, onReminderListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        holder.set(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
