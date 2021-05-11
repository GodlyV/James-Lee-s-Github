package ca.qc.johnabbott.cs616.healthhaven.model;

public class NetworkingExamples {

    //region Getting all data
    /*
    public void getNotes() {
        // get the application with all the set globals
        NotesApplication application = (NotesApplication) getActivity().getApplication();

        // get the connection string. concats ip with port
        String address = application.getConnectionString();

        // get the uuid to get that users data
        String uuid = application.getUserUuid();

        // build the full HttpRequest URL
        String userNotesAddress = address + "user/" + uuid + "/notes";

        // setup a GET request to that URL
        HttpRequest request = new HttpRequest(userNotesAddress, HttpRequest.Method.GET);

        // create an async httprequesttask to handle the HttpRequest in another thread
        HttpRequestTask httpRequestTask = new HttpRequestTask();
        httpRequestTask.setOnResponseListener(new OnResponseListener<HttpResponse>() {
            @Override
            public void onResponse(HttpResponse data) {
                if(data.getResponseCode() != 200)
                    return;

                successfully got the data (200 = successful request)
                trust me this works, change the Lists datatype.
                List<Note> notes = new LinkedList<>(Arrays.asList(Note.parseArray(data.getResponseBody())));

                TODO do stuff with the list of objects.. set adapter and refresh recyclerview?

                TODO do stuff
            }
        });
        httpRequestTask.setOnErrorListener(new OnErrorListener() {
            @Override
            public void onError(Exception error) {
                // failed
            }
        });

        // execute the fully built HttpRequest
        httpRequestTask.execute(request);
    }
     */
    //endregion



}
