package ca.qc.johnabbott.cs616.healthhaven.ui.Contact;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ca.qc.johnabbott.cs616.healthhaven.R;
import ca.qc.johnabbott.cs616.healthhaven.model.Contact;
import ca.qc.johnabbott.cs616.healthhaven.model.ContactData;
import ca.qc.johnabbott.cs616.healthhaven.model.DatabaseHandler;
import ca.qc.johnabbott.cs616.healthhaven.model.Log;
import ca.qc.johnabbott.cs616.healthhaven.networking.HttpRequest;
import ca.qc.johnabbott.cs616.healthhaven.networking.HttpRequestTask;
import ca.qc.johnabbott.cs616.healthhaven.networking.HttpResponse;
import ca.qc.johnabbott.cs616.healthhaven.networking.OnErrorListener;
import ca.qc.johnabbott.cs616.healthhaven.networking.OnResponseListener;
import ca.qc.johnabbott.cs616.healthhaven.sqlite.DatabaseException;
import ca.qc.johnabbott.cs616.healthhaven.ui.HealthHavenApplication;
import ca.qc.johnabbott.cs616.healthhaven.ui.adapter.ContactsAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class ContactsFragment extends Fragment {

    private RecyclerView contactsRecyclerView;

    private View root;
    private static List<Contact> data;
    private FloatingActionButton contactsFloatingActionButton;
    private SearchView searchBar;
    private Long contactId;
    private DatabaseHandler db;
    private ContactsAdapter adapter;
    private FloatingActionButton fab;
    private String serverAddress;
    private HealthHavenApplication globalApp;
    private static List<Contact> serverData;
    private ContactsFragment fragment;
    private String uuid;


    public static class params{
        public static final String initial_contact = "initial_contact";
    }
    public static class results{
        public static final String final_contact = "final_contact";
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) globalApp.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public ContactsFragment() {
    }
    public static List<Contact> getData(){
        return data;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_contacts, container, false);
        contactsRecyclerView = root.findViewById(R.id.contacts_RecyclerView);
        contactsFloatingActionButton=root.findViewById(R.id.fab);
        searchBar=root.findViewById(R.id.SearchContacts_SearchView);
        fab = root.findViewById(R.id.fab);
        globalApp = (HealthHavenApplication) getActivity().getApplication();
        serverAddress = globalApp.getConnectionString();


        // creates a database handler
        db = new DatabaseHandler(getContext());

        fragment =this;
        fragment.getFragmentManager(); // The fragment that i've passed is going to be passed into the adapter to be used and accessed elsewhere
        //get the contacts from the server

        if(isNetworkAvailable()) {
            // pull from server
            getContactsAsync();
        }

        try {
            data = DatabaseHandler.getInstance(getContext()).getContactTable().readAll();
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        if(data != null){

            //putContactAsync(ContactData.getData().get(0));
        }



        adapter = new ContactsAdapter(data,fragment,getContext(),fab);

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return true;
            }

        });


        //SORTING
        sortData();
        // set adapter for recyclerview
        contactsRecyclerView.setAdapter(adapter);
        contactsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        return root;
    }

    private void getContactsAsync() {
        if(isNetworkAvailable()){
            String userLogsAddress = serverAddress + "/contact";
            HttpRequest request = new HttpRequest(userLogsAddress, HttpRequest.Method.GET);
            HttpRequestTask httpRequestTask = new HttpRequestTask();
            httpRequestTask.setOnResponseListener(new OnResponseListener<HttpResponse>() {
                @Override
                public void onResponse(HttpResponse response) {
                    if(response.getResponseCode() != 200)
                        return;

                    serverData = new LinkedList<>(Arrays.asList(Contact.parseArray(response.getResponseBody())));
                    //If the server data is empty meaning first time setting up a new phone and importing contacts
                    //serverData.get(7).setUuid("46a95703-f47a-48a7-94e9-3a418705fec4");
                    //deleteContactAsync(serverData.get(7));
                    if(uuid != null){
                        serverData.get(serverData.size()-1).setUuid(uuid);
                        uuid=null;
                        updateContactAsync(serverData.get(serverData.size()-1));//refresh
                    }
                    data = serverData;
                    db.deleteAllContacts();
                    db.addListContacts(serverData);
                    updateAdapter();

                }
            });
            httpRequestTask.execute(request);
        }
        else{

        }

    }

    private void updateAdapter() {
        adapter = new ContactsAdapter(data,fragment,getContext(),fab);
        sortData();
        contactsRecyclerView.setAdapter(adapter);
        contactsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        adapter.notifyDataSetChanged();// make sure to let the adapter know that things have changed.
    }

    private void postContactAsync(Contact contact) {
        if(isNetworkAvailable()) {
            String userNotesAddress = serverAddress + "/contact";
            HttpRequest request = new HttpRequest(userNotesAddress, HttpRequest.Method.POST);
            request.setRequestBody("application/json", contact.format());
            HttpRequestTask httpRequestTask = new HttpRequestTask();
            httpRequestTask.setOnResponseListener(new OnResponseListener<HttpResponse>() {
                @Override
                public void onResponse(HttpResponse data) {
                    if (data.getResponseCode() == 201) {
                        Map<String, List<String>> s = data.getHeaders();
                        uuid = s.get("Location").get(0);
                        String[] links = uuid.split("/");
                        uuid= links[links.length-1];
                        getContactsAsync();
                    }
                }
            });
            httpRequestTask.setOnErrorListener(new OnErrorListener() {
                @Override
                public void onError(Exception error) {
                    int i = 0;
                }
            });
            httpRequestTask.execute(request);
        }
    }
    public void sortData(){
        data.sort(new Comparator<Contact>() { // Sorts by Name
            @Override
            public int compare(Contact rhs, Contact lhs) {
                return rhs.getName().compareTo(lhs.getName()); // returns the one that is the smallest alphabetical name.
            }
        });
        adapter.notifyDataSetChanged();// make sure to let the adapter know that things have changed.
    }

    private void deleteContactAsync(Contact contact) {
        if(isNetworkAvailable()){
            String userLogsAddress = serverAddress + "/contact/" + contact.getUuid();

            HttpRequest request = new HttpRequest(userLogsAddress, HttpRequest.Method.DELETE);
            HttpRequestTask httpRequestTask = new HttpRequestTask();
            httpRequestTask.setOnResponseListener(data -> {
                getContactsAsync();
                serverData.remove(contact);

            });
            httpRequestTask.setOnErrorListener(error -> {
                // failed
            });
            httpRequestTask.execute(request);
        }

    }
    private void updateContactAsync(Contact contact) {
        if(isNetworkAvailable()) {
            String userLogsAddress = serverAddress + "/contact/" + contact.getUuid();

            HttpRequest request = new HttpRequest(userLogsAddress, HttpRequest.Method.PUT);
            request.setRequestBody("application/json", contact.format());
            HttpRequestTask httpRequestTask = new HttpRequestTask();
            httpRequestTask.setOnResponseListener(data -> {
                getContactsAsync();

            });
            httpRequestTask.setOnErrorListener(error -> {
                // failed
            });
            httpRequestTask.execute(request);
        }
    }



    public void updateCurrentContacts(Contact contact, int location, boolean isNewContact,long listId,boolean delete) {

        try {
            //deletes the contact
            if(delete){
                if(!isNewContact){
                    db.getContactTable().deleteByKey(listId);//Removes the Contact in the database
                    adapter.data.remove(location);//removes the contact in the adapter so that it no longer shows
                    deleteContactAsync(contact);//deletes from cloud server
                    adapter.notifyDataSetChanged();
                }
            }
            else if(isNewContact){// New note creation;
                db.getContactTable().create(contact);
                contactId = contact.getId();
                //adapter.data.remove(location);//removes the contact in the adapter so that it no longer shows
                //adapter.notifyItemRemoved(location);//tell the adapter that something changed
                data.add(contact); //resets the contact in the adapter
                postContactAsync(contact);

            }
            else{ // And edit note.
                db.getContactTable().update(contact);//updates the note in the database
                data.remove(location);
                //fabString="Note Updated";
                updateContactAsync(contact);
                data.add(contact); //resets the contact in the adapter
            }
            adapter.notifyDataSetChanged();

        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        //contactId=contact.getId();
        if (isNewContact) {
            location = getContactIndex(contactId); // gets the location of the note in the dataset
        }
        sortData();

    }
    private void setUuid(){
        for (int i = 0; i<data.size();i++){
            if(data.get(i).getId() == serverData.get(i).getId()){
                data.get(i).setUuid(serverData.get(i).getUuid());
                updateCurrentContacts(data.get(i),i,false,0,false);
            }
        }
    }
    private int getContactIndex(Long id) {
        int index = -1;
        for (int i =0; i<data.size();i++ ){ // loops through all of the data to check and see which id corresponds to the location.
            Contact contactData = data.get(i);
            if (contactData.getId() == id){
                index = i;
            }
        }
        return index;
    }
}
