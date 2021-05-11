package ca.qc.johnabbott.cs616.healthhaven.model;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import java.net.URL;

import ca.qc.johnabbott.cs616.healthhaven.sqlite.Identifiable;

public class MedicalInfo implements Identifiable<Long, MedicalInfo> {

    //region Internal Classes
    public class MedicalInfoes{
        public class Embedded {
            public MedicalInfo[] medicalInfoes;
        }

        private Embedded _embedded;
    }

    public class Profile{
        public URL href;
    }

    public class Links{
        public class Link{
            public URL href;
            public boolean templated;
        }

        private Link self;
        private Link note;
    }
    //endregion

    public enum Type {
        ALLERGY,
        MEDICAL_CONDITION
    }

    @Expose
    private Type type;
    @Expose
    private String name;
    @Expose
    private String info;

    private long id;
    private String uuid;
    private Links _links;
    private Profile profile;


    public MedicalInfo(){
        this(-1);
    }

    public MedicalInfo(long id){
        this.id = id;
    }

    public MedicalInfo(long id, Type type, String name, String info){
        this.id = id;
        this.type = type;
        this.name = name;
        this.info = info;
    }

    //region Properties
    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public MedicalInfo setId(Long id) {
        this.id = id;
        return this;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getUuid() {
        return uuid;
    }

    public MedicalInfo setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public Links get_links() {
        return _links;
    }

    public void set_links(Links _links) {
        this._links = _links;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile _embeded) {
        this.profile = _embeded;
    }
    //endregion

    public static MedicalInfo parse(String json){
        //System.out.println(json);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        MedicalInfo medInfo = gson.fromJson(json, MedicalInfo.class);

        String href = medInfo._links.self.href.toString();
        String id = href.split("/")[4];
        medInfo.setUuid(id);

        return medInfo;
    }

    public static MedicalInfo[] parseArray(String json){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        MedicalInfoes medInfoes = gson.fromJson(json, MedicalInfoes.class);

        for (MedicalInfo note : medInfoes._embedded.medicalInfoes){
            String href = note._links.self.href.toString();
            String id = href.split("/")[4];
            note.setUuid(id);
        }

        return medInfoes._embedded.medicalInfoes;
    }

    public String format(){
        GsonBuilder builder = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Gson gson = builder.create();
        String json = gson.toJson(this, MedicalInfo.class);
        return json;
    }
}