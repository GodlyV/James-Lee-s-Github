package ca.qc.johnabbott.cs616.healthhaven.ui.adapter;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import ca.qc.johnabbott.cs616.healthhaven.R;
import ca.qc.johnabbott.cs616.healthhaven.model.MedicalInfo;

public class MedicalInfoViewHolder extends RecyclerView.ViewHolder{

    private final TextView medicalInformationName;
    private final TextView medicalInformationInfo;
    private ConstraintLayout medicalInformationLayout;
    private MedicalInfo medInfo;

    private View.OnTouchListener onTouchListener;
    private final View root;

    private boolean hasMoved;
    private int moved;

    public MedicalInfoViewHolder(@NonNull View root) {
        super(root);

        medicalInformationName = root.findViewById(R.id.name_TextView);
        medicalInformationInfo = root.findViewById(R.id.info_TextView);
        medicalInformationLayout = root.findViewById(R.id.medicalInfo_ConstraintLayout);

        this.root = root;

        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(onTouchListener != null)
                    onTouchListener.onTouch(v, event);
                return true;
            }
        });
    }

    public void set(MedicalInfo medInfo){
        this.medInfo = medInfo;

        medicalInformationName.setText(medInfo.getName());
        medicalInformationInfo.setText(medInfo.getInfo());

        if(medInfo.getType() == MedicalInfo.Type.MEDICAL_CONDITION)
            medicalInformationLayout.setBackgroundResource(R.drawable.list_item_background_condition);
        else
            medicalInformationLayout.setBackgroundResource(R.drawable.list_item_background_allergy);
    }

    public MedicalInfo get(){
        return  medInfo;
    }

    public void hasTouched(){
        this.moved = 0;
        this.hasMoved = false;
    }

    public boolean getHasMoved(){
        return this.hasMoved;
    }

    public void setHasMoved(){
        if(++this.moved > 5)
            this.hasMoved = true;
    }

    public void setOnTouchListener(View.OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }
}