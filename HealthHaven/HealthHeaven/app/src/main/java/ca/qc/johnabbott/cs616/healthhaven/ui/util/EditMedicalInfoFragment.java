package ca.qc.johnabbott.cs616.healthhaven.ui.util;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import ca.qc.johnabbott.cs616.healthhaven.R;
import ca.qc.johnabbott.cs616.healthhaven.model.MedicalInfo;

public class EditMedicalInfoFragment  extends DialogFragment {

    public interface OnMedicalInfoChangedListener{
        void onMedicalInfoChanged(MedicalInfo medInfo);
    }

    public interface OnMedicalInfoDeletedListener{
        void onMedicalInfoDeleted(MedicalInfo medInfo);
    }

    private TextView typeTextView;
    private EditText nameEditText;
    private EditText descriptionEditText;
    private Button saveButton;
    private Button cancelButton;
    private Button deleteButton;

    private OnMedicalInfoChangedListener onMedicalInfoChangedListener;
    private OnMedicalInfoDeletedListener onMedicalInfoDeletedListener;

    private MedicalInfo medInfo;

    //if the type is provided then create an empty info with the type set
    public EditMedicalInfoFragment(MedicalInfo.Type type){
        this.medInfo = new MedicalInfo();
        this.medInfo.setType(type);
    }

    //if the info is given then set the info
    public EditMedicalInfoFragment(MedicalInfo medInfo){
        this.medInfo = medInfo;
    }

    //sets the save button action
    public void setOnMedicalInfoChangedListener(OnMedicalInfoChangedListener onMedicalInfoChangedListener) {
        this.onMedicalInfoChangedListener = onMedicalInfoChangedListener;
    }

    //sets the delete button action
    public void setOnMedicalInfoDeletedListenerListener(OnMedicalInfoDeletedListener onMedicalInfoDeletedListener) {
        this.onMedicalInfoDeletedListener = onMedicalInfoDeletedListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_edit_medical_info, container, false);

        //get the views
        typeTextView = root.findViewById(R.id.type_TextView);
        nameEditText = root.findViewById(R.id.name_EditText);
        descriptionEditText = root.findViewById(R.id.description_EditText);
        saveButton = root.findViewById(R.id.save_Button);
        cancelButton = root.findViewById(R.id.cancel_Button);
        deleteButton = root.findViewById(R.id.delete_Button);

        //set the title views and the visibility of delete
        if(medInfo.getName() == null){
            typeTextView.setText("Add");
            deleteButton.setVisibility(View.GONE);
        }
        else
            typeTextView.setText("Edit");

        //set the text of the editTexts
        nameEditText.setText(medInfo.getName());
        descriptionEditText.setText(medInfo.getInfo());

        //save the medInfo's properties and call the save function then exit the dialog
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                medInfo.setName(nameEditText.getText().toString());
                medInfo.setInfo(descriptionEditText.getText().toString());

                if(onMedicalInfoChangedListener != null)
                    onMedicalInfoChangedListener.onMedicalInfoChanged(medInfo);

                dismiss();
            }
        });

        //call the delete function then exit the dialog
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onMedicalInfoDeletedListener != null)
                    onMedicalInfoDeletedListener.onMedicalInfoDeleted(medInfo);

                dismiss();
            }
        });

        //exit the dialog
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return root;
    }
}
