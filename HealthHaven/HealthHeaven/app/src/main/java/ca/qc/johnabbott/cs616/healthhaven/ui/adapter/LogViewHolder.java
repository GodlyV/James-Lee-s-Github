package ca.qc.johnabbott.cs616.healthhaven.ui.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ca.qc.johnabbott.cs616.healthhaven.R;
import ca.qc.johnabbott.cs616.healthhaven.model.Log;

public class LogViewHolder extends RecyclerView.ViewHolder {

    private final View root;
    private final TextView type;
    private final TextView description;
    private final TextView duration;

    public LogViewHolder(@NonNull View root) {
        super(root);
        type = root.findViewById(R.id.listLogCategory_TextView);
        description = root.findViewById(R.id.listLogDescription_TextView);
        duration = root.findViewById(R.id.listLogDuration_TextView);
        this.root = root;
    }

    public void set(Log log) {

        type.setText(log.getType().toString());
        description.setText(log.getDescription());
        duration.setText("" + log.getDuration() + " hours");

        int x = root.getPaddingBottom();
        root.setBackgroundResource(R.drawable.list_item_background_log);
        root.setPadding(x,x,x,x);
    }
}
