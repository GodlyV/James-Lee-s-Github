package ca.qc.johnabbott.cs616.healthhaven.ui.Contact;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.Calendar;

import ca.qc.johnabbott.cs616.healthhaven.R;
import ca.qc.johnabbott.cs616.healthhaven.model.Contact;

/**
 * A placeholder fragment containing a simple view.
 */
public class editContactFragment extends Fragment {
    private static Contact contact;
    private View root;
    private EditText name;
    private EditText mobilePhone;
    private EditText homePhone;
    private EditText company;
    private EditText email;

    public editContactFragment() {
    }

    public static Contact getContact() {
        return contact;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_edit_contact, container, false);
        name = root.findViewById(R.id.name_EditText);
        mobilePhone=root.findViewById(R.id.mobilePhone_EditText);
        homePhone=root.findViewById(R.id.homePhone_EditText);
        company=root.findViewById(R.id.company_EditText);
        email=root.findViewById(R.id.email_EditText);

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // this sets the new name to be that of the character inputted.
                String nameText = name.getText().toString();
                contact.setName(nameText);
            }
        });
        mobilePhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // this sets the new mobilePhone to be that of the character inputted.
                String mobilePhoneText = mobilePhone.getText().toString();
                contact.setMobilePhone(mobilePhoneText);
            }
        });
        homePhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // this sets the new homePhone to be that of the character inputted.
                String homePhoneText = homePhone.getText().toString();
                contact.setHomePhone(homePhoneText);
            }
        });
        company.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // this sets the new company to be that of the character inputted.
                String companyText = company.getText().toString();
                contact.setCompany(companyText);
            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // this sets the new company to be that of the character inputted.
                String emailText = email.getText().toString();
                contact.setEmail(emailText);
            }
        });
        return root;
    }

    public void setContact(Contact contact){
        this.contact = contact;
        name.setText(contact.getName());
        mobilePhone.setText(contact.getMobilePhone());
        homePhone.setText(contact.getHomePhone());
        company.setText(contact.getCompany());
        email.setText(contact.getEmail());
    }
}
