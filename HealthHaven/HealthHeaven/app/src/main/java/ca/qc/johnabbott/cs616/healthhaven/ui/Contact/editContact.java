package ca.qc.johnabbott.cs616.healthhaven.ui.Contact;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import ca.qc.johnabbott.cs616.healthhaven.R;
import ca.qc.johnabbott.cs616.healthhaven.model.Contact;

public class editContact extends AppCompatActivity {

    private editContactFragment fragment;
    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragment = (editContactFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        Intent intent = getIntent();
        Contact contact = intent.getParcelableExtra(ContactsFragment.params.initial_contact);
        if(contact != null){
            fragment.setContact(contact);//Cloned so that you get the different note as it would be referenced to the same note if you did not.
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_contact_edit,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.action_cancel:
                finish();
                break;
            case R.id.action_save://Saves the note changes into the base and updates it.
                contact = editContactFragment.getContact();
                if(contact.getName().equals("") || contact.getName() == null){
                    Toast toast= Toast.makeText(fragment.getContext(), "You can't save contacts without names!", Toast.LENGTH_LONG);
                    View view = toast.getView();
                    view.setBackgroundResource(R.color.pieChartRed);
                    toast.show();
                }
                else{
                    Intent intent = getIntent();
                    intent.putExtra(ContactsFragment.results.final_contact,contact);
                    setResult(Activity.RESULT_OK,intent);
                    //todo send contact to contact list
                    finish();
                }
                break;
            case R.id.action_delete:
                contact = editContactFragment.getContact();
                Intent intent = getIntent();
                intent.putExtra(ContactsFragment.results.final_contact,contact);
                intent.putExtra("delete",true);
                setResult(Activity.RESULT_OK,intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

