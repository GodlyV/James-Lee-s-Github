package ca.qc.johnabbott.cs616.healthhaven.ui.adapter;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ca.qc.johnabbott.cs616.healthhaven.R;
import ca.qc.johnabbott.cs616.healthhaven.model.Log;
import ca.qc.johnabbott.cs616.healthhaven.ui.HealthCenter.HealthCenter;

public class LogAdapter extends RecyclerView.Adapter<LogViewHolder> {

    private List<Log> data;
    private HealthCenter.OnLogTouchedListener onLogTouchedListener;

    public LogAdapter(List<Log> data, HealthCenter.OnLogTouchedListener onLogTouchedListener) {
        this.data = data;
        this.onLogTouchedListener = onLogTouchedListener;
    }

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_log, parent, false);
        LogViewHolder holder = new LogViewHolder(root);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {
        Log current = data.get(position);
        holder.set(current);
        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            Log log = current;
            boolean isClick;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP)
                    onLogTouchedListener.onLogTouched(log);

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
