package ca.qc.johnabbott.cs616.healthhaven.ui.Emergency;

import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import ca.qc.johnabbott.cs616.healthhaven.ui.Contact.Contacts;
import ca.qc.johnabbott.cs616.healthhaven.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class EmergencyInformationFragment extends Fragment {

    private View root;
    private Button contactsButton;
    private Button nearestHelpButton;
    private Button callAbmulanceButton;


    public EmergencyInformationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root =inflater.inflate(R.layout.fragment_emergency_information, container, false);
        contactsButton = root.findViewById(R.id.contacts_Button);
        nearestHelpButton=root.findViewById(R.id.nearestHelp_Button);
        callAbmulanceButton=root.findViewById(R.id.ambulance_Button);

        //ContactsButton to go to contact information
        contactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Contacts.class);
                startActivity(intent);
            }
        });
        return root;
    }
}
