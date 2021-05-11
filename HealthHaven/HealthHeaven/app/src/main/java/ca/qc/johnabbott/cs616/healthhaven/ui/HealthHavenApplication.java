package ca.qc.johnabbott.cs616.healthhaven.ui;

import android.app.Application;

public class HealthHavenApplication extends Application {
    private String ipaddress;
    private String port;
    private String userUuid;
    private String protocol;
    private String host;

    private String url;
    private String urlNotes;
    private String urlUsers;
    private String urlCollaborators;
    private String urlProfiles;

    String ian = "7bdba0fe-fe95-4b1c-8247-f2479ee6e380";
    String usef = "3faa2495-f0f1-4408-ae24-d482f37caf1c";
    String aref = "6e840afc-5c0a-4679-bcfa-8a210e50ecfc";
    String jim = "97489bce-1c85-4ff2-b457-ba53589d12cc";
    String sandy = "2c77dafe-1545-432f-b5b1-3a0011cf7036";
    String nobody = "13cea3c0-4b18-471f-9bee-e9060ac62213";


    public String getIpaddress() {
        return ipaddress;
    }

    public String getPort() {
        return port;
    }

    public String getConnectionString() {
        return "http://" + getIpaddress() + ":" + getPort();
    }

    public String getUserUuid() {
        return userUuid;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //ipaddress = "192.168.2.100";
        //ipaddress = "192.168.0.162"; // Note server IP
        ipaddress = "10.39.120.2";
        port = "9999"; // Note server port
        userUuid = aref; // Current 'logged in' user

        url = getProtocol() + "://" + getIpaddress() + ":" + getPort() + "/";
        urlNotes = url + "note";
        urlUsers = url + "user";
        urlCollaborators = url + "collaborator";
        urlProfiles = url + "profile";
    }


    public String getProtocol() {
        return protocol;
    }

    public String getHost() {
        return host;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlNotes() {
        return urlNotes;
    }

    public String getUrlUsers() {
        return urlUsers;
    }

    public String getUrlCollaborators() {
        return urlCollaborators;
    }

    public String getUrlProfiles() {
        return urlProfiles;
    }

}
