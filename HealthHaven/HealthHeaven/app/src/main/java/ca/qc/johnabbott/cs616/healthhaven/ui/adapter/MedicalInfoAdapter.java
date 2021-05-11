package ca.qc.johnabbott.cs616.healthhaven.ui.adapter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import ca.qc.johnabbott.cs616.healthhaven.R;
import ca.qc.johnabbott.cs616.healthhaven.model.MedicalInfo;

public class MedicalInfoAdapter extends RecyclerView.Adapter<MedicalInfoViewHolder> {

    public interface OnHolderClickListener{
        void onHolderClick(MedicalInfo medInfo);
    }

    private List<MedicalInfo> data;

    private OnHolderClickListener onHolderClickListener;

    public MedicalInfoAdapter(List<MedicalInfo> data){
        this.data = data;
    }

    @NonNull
    @Override
    public MedicalInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_medical_information, parent, false);

        MedicalInfoViewHolder holder = new MedicalInfoViewHolder(root);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MedicalInfoViewHolder holder, int position) {
        holder.set(data.get(position));

        holder.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        holder.hasTouched();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        holder.setHasMoved();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (!holder.getHasMoved()) {
                            if(onHolderClickListener != null)
                                onHolderClickListener.onHolderClick(holder.get());
                        }
                        break;
                    }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setOnHolderClick(OnHolderClickListener onHolderClickListener) {
        this.onHolderClickListener = onHolderClickListener;
    }
}
