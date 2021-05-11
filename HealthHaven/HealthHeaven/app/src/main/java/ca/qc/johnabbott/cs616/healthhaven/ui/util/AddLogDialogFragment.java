package ca.qc.johnabbott.cs616.healthhaven.ui.util;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.List;

import ca.qc.johnabbott.cs616.healthhaven.R;
import ca.qc.johnabbott.cs616.healthhaven.model.Log;
import ca.qc.johnabbott.cs616.healthhaven.model.LogCategory;
import ca.qc.johnabbott.cs616.healthhaven.ui.HealthCenter.HealthCenter;
import es.dmoral.toasty.Toasty;

/**
 * Custom dialog fragment for creating or manipulating logs
 *
 * @author      William Fedele
 * @studentID   1679513
 * @githubUser  william-fedele
 */

public class AddLogDialogFragment extends DialogFragment {

    private Button saveButton;
    private Button deleteButton;
    private Button cancelButton;
    private EditText duration;
    private EditText description;
    private TextView title;
    private Spinner optionSpinner;
    private boolean newLog = true;
    private Log log;

    private HealthCenter.OnLogListener onLogAddedListener;

    public AddLogDialogFragment setOnLogAddedListener(HealthCenter.OnLogListener onLogAddedListener) {
        this.onLogAddedListener = onLogAddedListener;
        return this;
    }

    public AddLogDialogFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dialog_add_log, container, false);

        deleteButton = root.findViewById(R.id.delete_Button);
        saveButton = root.findViewById(R.id.save_Button);
        cancelButton = root.findViewById(R.id.cancel_Button);

        duration = root.findViewById(R.id.duration_EditText);
        description = root.findViewById(R.id.description_EditText);
        optionSpinner = root.findViewById(R.id.logOptions_Spinner);
        title = root.findViewById(R.id.title_TextView);

        SetSpinnerValues(GetSpinnerValues());

        deleteButton.setOnClickListener(view -> {
            if(log != null) {
                onLogAddedListener.LogDeleted(log);
            }
            dismiss();
        });
        saveButton.setOnClickListener(view -> {
            String type = optionSpinner.getSelectedItem().toString();
            String time = duration.getText().toString();
            String desc = description.getText().toString();

            // all fields must be filled
            if(type.equals("") || time.equals("") || desc.equals("")) {
                Toasty.error(getContext(), "There are still empty fields!", Toast.LENGTH_SHORT, true).show();
            }
            else {
                try {
                    // update the local log with the new data
                    if(!newLog) {
                        log.setType(LogCategory.fromString(optionSpinner.getSelectedItem().toString()));
                        log.setDuration(Double.valueOf(time));
                        log.setDescription(desc);
                        onLogAddedListener.LogUpdated(log);
                    }
                    else {
                        // create a new log with the current data
                        Log log = new Log()
                                .setType(LogCategory.fromString(type))
                                .setDuration(Double.valueOf(time))
                                .setDescription(desc);
                        onLogAddedListener.LogAdded(log);
                    }
                    dismiss();
                } catch(Exception e) {
                    Toasty.error(getContext(), "That data doesn't seem right...", Toast.LENGTH_SHORT, true).show();
                }

            }
        });
        cancelButton.setOnClickListener(view -> dismiss());

        if(!newLog) {
            optionSpinner.setSelection(log.getType().ordinal());
            duration.setText(String.valueOf(log.getDuration()));
            description.setText(log.getDescription());
            title.setText("Edit Log");
        }
        else {
            deleteButton.setVisibility(View.GONE);
            title.setText("New Log");
        }
        return root;
    }

    /**
     * Spinner values are extracted from the LogCategory enum
     * @return
     */
    public List<String> GetSpinnerValues() {
        List<String> constraints = new ArrayList<>();
        for (LogCategory c : LogCategory.values()) {
            constraints.add(capitalizeFirstLetter(c.toString()));
        }
        return constraints;
    }

    /**
     * Set the spinners choices to a predefined list of string values
     * @param values
     */
    public void SetSpinnerValues(List<String> values) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.logspinner_item, R.id.logSpinner_TextView);
        adapter.addAll(values);
        optionSpinner.setAdapter(adapter);
        optionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // to something when a spinner item is selected
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    /**
     *
     * @param original string to be formatted to capital case
     * @return capital case string. ex: arcade -> Arcade, arcade fire -> Arcade fire
     */
    public String capitalizeFirstLetter(String original) {
        original = original.toLowerCase();
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

    /**
     * Set the dialog fragments log in the case of updating/deleting a log.
     * @param log
     */
    public void setData(Log log) {
        newLog = false;
        this.log = log;
    }
}
