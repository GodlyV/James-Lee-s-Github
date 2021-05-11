package ca.qc.johnabbott.cs616.healthhaven.ui.MedicalID;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ca.qc.johnabbott.cs616.healthhaven.R;
import ca.qc.johnabbott.cs616.healthhaven.model.DatabaseHandler;
import ca.qc.johnabbott.cs616.healthhaven.model.MedicalInfo;
import ca.qc.johnabbott.cs616.healthhaven.networking.HttpRequest;
import ca.qc.johnabbott.cs616.healthhaven.networking.HttpRequestTask;
import ca.qc.johnabbott.cs616.healthhaven.networking.HttpResponse;
import ca.qc.johnabbott.cs616.healthhaven.networking.OnErrorListener;
import ca.qc.johnabbott.cs616.healthhaven.networking.OnResponseListener;
import ca.qc.johnabbott.cs616.healthhaven.sqlite.DatabaseException;
import ca.qc.johnabbott.cs616.healthhaven.ui.HealthHavenApplication;
import ca.qc.johnabbott.cs616.healthhaven.ui.adapter.MedicalInfoAdapter;
import ca.qc.johnabbott.cs616.healthhaven.model.MedicalInfoSampleData;
import ca.qc.johnabbott.cs616.healthhaven.ui.util.EditMedicalInfoFragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class MedicalIDActivityFragment extends Fragment {

    //region globals
    public boolean hasMoved;
    private DatabaseHandler dbh;
    HealthHavenApplication application;
    List<MedicalInfo> allergies;
    List<MedicalInfo> medicalConditions;
    List<MedicalInfo> tmp;

    MedicalInfoAdapter conditionAdapter;
    MedicalInfoAdapter allergyAdapter;

    RecyclerView medicalConditionRecyclerView;
    RecyclerView allergyRecyclerView;

    MedicalInfoAdapter.OnHolderClickListener onHolderClickListener;

    SwipeRefreshLayout swipeRefreshLayout;
    //endregion

    public MedicalIDActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //get the fragment
        View root = inflater.inflate(R.layout.fragment_medical_id, container, false);

        //Get the application environment setup
        application = (HealthHavenApplication) getActivity().getApplication();

        //region set user image (NOT IMPLEMENTED)

        //get the imageView
        ImageView userImage = root.findViewById(R.id.userImage_ImageView);
        //set the imageView's photo to the user's
        userImage.setImageResource(R.drawable.ic_sentiment_very_satisfied_black_48dp);

        //endregion

        //region get local database

        //get the database handler
        dbh = DatabaseHandler.getInstance(getContext());

        //attempt to get the local server data
        //else get an empty data set
        try {
            medicalConditions = getLocalMedicalConditions();
        } catch (DatabaseException e) {
            medicalConditions = new ArrayList<>();
        }

        //attempt to get the local server data
        //else get an empty data set
        try {
            allergies = getLocalAllergies();
        } catch (DatabaseException e) {
            allergies = new ArrayList<>();
        }

        //endregion

        //region setup recycler views

        //create the viewHolder click method
        onHolderClickListener = new MedicalInfoAdapter.OnHolderClickListener(){
            @Override
            public void onHolderClick(MedicalInfo medInfo) {
                EditMedicalInfoFragment editMedicalInfoFragment = new EditMedicalInfoFragment(medInfo);
                editMedicalInfoFragment.setOnMedicalInfoDeletedListenerListener(new EditMedicalInfoFragment.OnMedicalInfoDeletedListener() {
                    @Override
                    public void onMedicalInfoDeleted(MedicalInfo medInfo) {
                        //try to delete from both local and server databases (server after local in the case of a crash on local portion)
                        try {
                            dbh.getMedInfoTable().delete(medInfo);
                            if(medInfo.getType() == MedicalInfo.Type.MEDICAL_CONDITION){
                                medicalConditions.remove(medInfo);
                                updateMedicalConditions(medicalConditions);
                            }
                            else{
                                allergies.remove(medInfo);
                                updateAllergies(allergies);
                            }
                            delete(medInfo);
                        } catch (DatabaseException e) {
                            Toast.makeText(getContext(), "Error deleting the entry on the local db", Toast.LENGTH_SHORT);
                        }
                    }
                });
                editMedicalInfoFragment.setOnMedicalInfoChangedListener(new EditMedicalInfoFragment.OnMedicalInfoChangedListener() {
                    @Override
                    public void onMedicalInfoChanged(MedicalInfo medInfo) {
                        //try to update both local and server databases (server after local in the case of a crash on local portion)
                        try {
                            dbh.getMedInfoTable().update(medInfo);
                            conditionAdapter.notifyDataSetChanged();
                            allergyAdapter.notifyDataSetChanged();
                            update(medInfo);
                        } catch (DatabaseException e) {
                            Toast.makeText(getContext(), "Error updating the entry on the local db", Toast.LENGTH_SHORT);
                        }
                    }
                });
                editMedicalInfoFragment.show(getFragmentManager(), "edit-medicalInfo");
            }
        };

        //get the two recyclerViews
        medicalConditionRecyclerView = root.findViewById(R.id.medicalConditions_RecyclerView);
        allergyRecyclerView = root.findViewById(R.id.allergies_RecyclerView);

        //create the two adapters
        conditionAdapter = new MedicalInfoAdapter(medicalConditions);
        allergyAdapter = new MedicalInfoAdapter(allergies);

        //set the onclick for the view holder
        allergyAdapter.setOnHolderClick(onHolderClickListener);
        //set it's adapter
        allergyRecyclerView.setAdapter(allergyAdapter);
        //set the layout manager
        allergyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //set the onclick for the view holder
        conditionAdapter.setOnHolderClick(onHolderClickListener);
        //set it's adapter
        medicalConditionRecyclerView.setAdapter(conditionAdapter);
        //set the layout manager
        medicalConditionRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //endregion

        //region setup the buttons

        //get the buttons
        Button addMedicalCondition = root.findViewById(R.id.addMedicalCondition_Button);
        Button addAllergy = root.findViewById(R.id.addAllergy_Button);

        //create the onClick for the buttons
        View.OnClickListener onCLick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditMedicalInfoFragment editMedicalInfoFragment;

                //set the type of the medicalInfo based on the id of the button pressed
                if(view.getId() == R.id.addAllergy_Button)
                    editMedicalInfoFragment = new EditMedicalInfoFragment(MedicalInfo.Type.ALLERGY);
                else
                    editMedicalInfoFragment = new EditMedicalInfoFragment(MedicalInfo.Type.MEDICAL_CONDITION);

                //set the dialog fragments save button
                editMedicalInfoFragment.setOnMedicalInfoChangedListener(new EditMedicalInfoFragment.OnMedicalInfoChangedListener() {
                    @Override
                    public void onMedicalInfoChanged(MedicalInfo medInfo) {
                        //try to add to both local and server databases (server after local in the case of a crash on local portion)
                        try {
                            dbh.getMedInfoTable().create(medInfo);
                            if (medInfo.getType() == MedicalInfo.Type.MEDICAL_CONDITION){
                                medicalConditions.add(medInfo);
                                updateMedicalConditions(medicalConditions);
                            }
                            else{
                                allergies.add(medInfo);
                                updateAllergies(allergies);
                            }
                            add(medInfo);
                        } catch (DatabaseException e) {
                            Toast.makeText(getContext(), "Error adding the entry to the local db", Toast.LENGTH_SHORT);
                        }
                    }
                });
                editMedicalInfoFragment.show(getFragmentManager(), "edit-medicalInfo");
            }
        };

        //set the onclick for the buttons
        addAllergy.setOnClickListener(onCLick);
        addMedicalCondition.setOnClickListener(onCLick);

        //endregion

        //region setup refresh (synchronisation with server)

        //get the swipeRefreshView
        swipeRefreshLayout = root.findViewById(R.id.swiperefresh);

        //set it's on refresh
        //DOES NOT WORK AFTER SWITCHING FRAGMENTS
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //update the recyclerViews with the server data
                getServerAlleriges();
                getServerMedicalConditions();

                //synchronise the local database with the server one
                getServer();

                //stop the refresh animation
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        //endregion

        return root;
    }

    //region get data
    private List<MedicalInfo> getLocalAllergies() throws DatabaseException {

        //read all medicalInfoes
        List<MedicalInfo> medInfo = dbh.getMedInfoTable().readAll();

        //create an empty arrayList
        List<MedicalInfo> allergies = new ArrayList();

        //go through each medicalInfo,
        //add the allergies to the new arrayList
        for (MedicalInfo info : medInfo) {
            if(info.getType() == MedicalInfo.Type.ALLERGY)
                allergies.add(info);
        }

        //return the new arrayList
        return allergies;
    }

    private List<MedicalInfo> getLocalMedicalConditions() throws DatabaseException {

        //read all medicalInfoes
        List<MedicalInfo> medInfo = dbh.getMedInfoTable().readAll();

        //create an empty arrayList
        List<MedicalInfo> conditions = new ArrayList();

        //go through each medicalInfo,
        //add the medicalConditions to the new arrayList
        for (MedicalInfo info : medInfo) {
            if(info.getType() == MedicalInfo.Type.MEDICAL_CONDITION)
                conditions.add(info);
        }

        //return the new arrayList
        return conditions;
    }

    private void getServerAlleriges(){
        //get the userId
        String userUuid = application.getUserUuid();

        //get the server ip
        String connectionString = application.getConnectionString();

        //create the get URL
        String getUrl = connectionString + "/medicalinfo";

        //create an httpRequest and its task
        HttpRequest request = new HttpRequest(getUrl, HttpRequest.Method.GET);
        HttpRequestTask httpRequestTask = new HttpRequestTask();

        //set the on error to toast
        httpRequestTask.setOnErrorListener(new OnErrorListener() {
            @Override
            public void onError(Exception error) {
                Toast.makeText(getContext(), "An error has occured when processing the request, please try again.", Toast.LENGTH_LONG).show();
            }
        });

        //set the on success to populate the allergies arrayList
        httpRequestTask.setOnResponseListener(new OnResponseListener<HttpResponse>() {
            @Override
            public void onResponse(HttpResponse response) {

                //reset the contents
                allergies = new ArrayList<>();

                //get the json response
                String jsonNotes = response.getResponseBody();

                //parse the json to an array of medicalInfoes
                tmp = Arrays.asList(MedicalInfo.parseArray(jsonNotes));

                //go through each medicalInfo,
                //add the allergies to the new arrayList
                for (MedicalInfo info : tmp) {
                    if(info.getType() == MedicalInfo.Type.ALLERGY){
                        allergies.add(info);
                    }
                }

                //update the allergies recyclerView
                updateAllergies(allergies);
            }
        });

        //send the http request
        httpRequestTask.execute(request);
    }

    private void getServerMedicalConditions(){
        //get the userId
        String userUuid = application.getUserUuid();

        //get the server ip
        String connectionString = application.getConnectionString();

        //create the get URL
        String getUrl = connectionString + "/medicalinfo";

        //create an httpRequest and its task
        HttpRequest request = new HttpRequest(getUrl, HttpRequest.Method.GET);
        HttpRequestTask httpRequestTask = new HttpRequestTask();

        //set the on error to toast
        httpRequestTask.setOnErrorListener(new OnErrorListener() {
            @Override
            public void onError(Exception error) {
                Toast.makeText(getContext(), "An error has occured when processing the request, please try again.", Toast.LENGTH_LONG).show();
            }
        });

        //set the on success to populate the allergies arrayList
        httpRequestTask.setOnResponseListener(new OnResponseListener<HttpResponse>() {
            @Override
            public void onResponse(HttpResponse response) {
                //reset the contents
                medicalConditions = new ArrayList<>();

                //get the json response
                String jsonNotes = response.getResponseBody();

                //parse the json to an array of medicalInfoes
                tmp = Arrays.asList(MedicalInfo.parseArray(jsonNotes));

                //go through each medicalInfo,
                //add the medicalConditions to the new arrayList
                for (MedicalInfo info : tmp) {
                    if(info.getType() == MedicalInfo.Type.MEDICAL_CONDITION){
                        medicalConditions.add(info);
                    }
                }

                //update the allergies recyclerView
                updateMedicalConditions(medicalConditions);
            }
        });
        httpRequestTask.execute(request);
    }
    //endregion

    private void getServer(){
        String userUuid = application.getUserUuid();

        //get the server ip
        String connectionString = application.getConnectionString();

        //setup the get URL
        String getUrl = connectionString + "/medicalinfo";

        //setup the http request and its request task
        HttpRequest request = new HttpRequest(getUrl, HttpRequest.Method.GET);
        HttpRequestTask httpRequestTask = new HttpRequestTask();

        //set the on error to toast an error
        httpRequestTask.setOnErrorListener(new OnErrorListener() {
            @Override
            public void onError(Exception error) {
                Toast.makeText(getContext(), "An error has occured when processing the request, please try again.", Toast.LENGTH_LONG).show();
            }
        });

        //set the on response to reset the local database data to the server's
        httpRequestTask.setOnResponseListener(new OnResponseListener<HttpResponse>() {
            @Override
            public void onResponse(HttpResponse response) {

                //reset the tmp arrayList
                tmp = new ArrayList<>();

                //get the json body
                String jsonNotes = response.getResponseBody();

                //parse the json to medicalInfoes
                tmp = Arrays.asList(MedicalInfo.parseArray(jsonNotes));

                //empty the local database
                dbh.deleteAllMedicalInfo();

                //add the server's dataset to the local database
                dbh.addListMedicalInfo(tmp);
            }
        });

        //launch the http request
        httpRequestTask.execute(request);
    }

    //region server modifications
    public void add(MedicalInfo medInfo){
        //get the connection string
        String connectionString = application.getConnectionString();

        //create the URL
        String postUrl = connectionString + "/medicalinfo";

        //make the request
        HttpRequest request = new HttpRequest(postUrl, HttpRequest.Method.POST);

        //set the request body to the medInfo to add
        request.setRequestBody("application/json", medInfo.format());

        //create the request task
        HttpRequestTask httpRequestTask = new HttpRequestTask();

        //set the on error to toast it to the screen
        httpRequestTask.setOnErrorListener(new OnErrorListener() {
            @Override
            public void onError(Exception error) {
                Toast.makeText(getContext(), "An error has occured when processing the request, please try again.", Toast.LENGTH_SHORT).show();
            }
        });

        //set the on response sets the medInfo's uuid
        httpRequestTask.setOnResponseListener(new OnResponseListener<HttpResponse>() {
            @Override
            public void onResponse(HttpResponse response) {
                String uuid = response.getHeaders().get("Location").get(0).split("/")[4];
                medInfo.setUuid(uuid);
            }
        });

        //execute the task
        httpRequestTask.execute(request);
    }

    public void update(MedicalInfo medInfo) {
        //get the connection string
        String connectionString = application.getConnectionString();

        //get the URL
        String repoUrl = connectionString + "/medicalinfo/" + medInfo.getUuid();

        //create the http request
        HttpRequest request = new HttpRequest(repoUrl, HttpRequest.Method.PUT);

        //set the request body to the medInfo to update
        request.setRequestBody("application/json", medInfo.format());

        //create the request task
        HttpRequestTask httpRequestTask = new HttpRequestTask();

        //Set error action to toast to the screen
        httpRequestTask.setOnErrorListener(new OnErrorListener() {
            @Override
            public void onError(Exception error) {
                Toast.makeText(getContext(), "An error has occured when processing the request, please try again.", Toast.LENGTH_SHORT).show();
            }
        });

        //set the on response (currently unused)
        httpRequestTask.setOnResponseListener(new OnResponseListener<HttpResponse>() {
            @Override
            public void onResponse(HttpResponse response) {
            }
        });

        //execute the task
        httpRequestTask.execute(request);
    }

    public void delete(MedicalInfo medInfo) {
        //get the connection string
        String connectionString = application.getConnectionString();

        //get the URL
        String repoUrl = connectionString + "/medicalinfo/" + medInfo.getUuid();

        //create the request and its task
        HttpRequest request = new HttpRequest(repoUrl, HttpRequest.Method.DELETE);
        HttpRequestTask httpRequestTask = new HttpRequestTask();

        //set the tasks on error to toast to the screen
        httpRequestTask.setOnErrorListener(new OnErrorListener() {
            @Override
            public void onError(Exception error) {
                Toast.makeText(getContext(), "An error has occured when processing the request, please try again.", Toast.LENGTH_SHORT).show();
            }
        });

        //set the on response (currently unused)
        httpRequestTask.setOnResponseListener(new OnResponseListener<HttpResponse>() {
            @Override
            public void onResponse(HttpResponse response) {
            }
        });

        //execute the task
        httpRequestTask.execute(request);
    }
    //endregion

    //region ui update
    public void updateAllergies(List<MedicalInfo> medInfo){
        //re-setup the allergy recyclerView
        allergyAdapter = new MedicalInfoAdapter(medInfo);
        allergyAdapter.setOnHolderClick(onHolderClickListener);
        allergyRecyclerView.setAdapter(allergyAdapter);
        allergyAdapter.notifyDataSetChanged();
        allergyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void updateMedicalConditions(List<MedicalInfo> medInfo){
        //re-setup the medCondition recyclerView
        conditionAdapter = new MedicalInfoAdapter(medInfo);
        conditionAdapter.setOnHolderClick(onHolderClickListener);
        medicalConditionRecyclerView.setAdapter(conditionAdapter);
        conditionAdapter.notifyDataSetChanged();
        medicalConditionRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
    //endregion
}