package ca.qc.johnabbott.cs616.healthhaven.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.ActionMode;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import ca.qc.johnabbott.cs616.healthhaven.R;
import ca.qc.johnabbott.cs616.healthhaven.model.Contact;
import ca.qc.johnabbott.cs616.healthhaven.ui.Contact.ContactsFragment;
import ca.qc.johnabbott.cs616.healthhaven.ui.Contact.editContact;

public class ContactsViewHolder extends RecyclerView.ViewHolder {
    private final TextView contactNameTextView;
    private final ImageView avatarImageView;
    private final View root;
    private final TextView idTextView;
    private final TextView companyTextView;
    private List<Contact> data;
    public Contact contact;
    public final ContactsAdapter contactsAdapter;

    public ContactsViewHolder(@NonNull View root, List<Contact> data, Fragment fragment, Context context, ContactsAdapter contactsAdapter, FloatingActionButton fab) {
        super(root);
        this.data=data;
        contactNameTextView = root.findViewById(R.id.contactName_TextView);
        avatarImageView = root.findViewById(R.id.avatar_ImageView);
        idTextView = root.findViewById(R.id.id_TextView);
        companyTextView=root.findViewById(R.id.company_TextView);
        this.root = root;
        this.contactsAdapter=contactsAdapter;


        //
        this.itemView.setOnTouchListener(new View.OnTouchListener() {
            boolean moved;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent){
                long id = Long.parseLong(idTextView.getText().toString());
                int location = getContactIndex(id);
                contact = ContactsViewHolder.this.contactsAdapter.getData().get(location);


                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        moved = false;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        moved = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        if(moved==false) {
                            //TODO
                            Intent intent = new Intent(context, editContact.class);//THIS NEEDS TO GET TO THE NEW
                            intent.putExtra(ContactsFragment.params.initial_contact,contact);
                            intent.putExtra("contactLocation",location);
                            intent.putExtra("contactId",id);
                            fragment.startActivityForResult(intent,1);
                        }
                        break;
                }
                //contactsAdapter.notifyItemRemoved(location);
                //contactsAdapter.notifyDataSetChanged();

                return true;
            }

        });



    }

    private int getContactIndex(long id) {
        int index = -1;
        for (int i =0; i<contactsAdapter.getData().size();i++ ){ // loops through all of the data to check and see which id corresponds to the location.
            Contact contactData = contactsAdapter.getData().get(i);
            if (contactData.getId() == id){
                index = i;
            }
        }
        return index;
    }


    public void set(Contact contact){
        contactNameTextView.setText(contact.getName());
        if(!contact.getCompany().equals("")){
            companyTextView.setText("("+contact.getCompany()+")");
        }
        idTextView.setText(contact.getId().toString());

    }
}
