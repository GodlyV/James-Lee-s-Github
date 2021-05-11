package ca.qc.johnabbott.cs616.healthhaven.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;

import ca.qc.johnabbott.cs616.healthhaven.sqlite.Identifiable;

public class Contact implements Identifiable<Long, Contact>,Parcelable {
    private long id;
    private String name;
    private String company;
    private String mobilePhone;
    private String homePhone;
    private String email;
    private String Uuid;

    public void setId(long id) {
        this.id = id;
    }

    public String getUuid() {
        return Uuid;
    }

    public Contact setUuid(String uuid) {
        this.Uuid = uuid;
        return this;
    }


    public Contact(){
    }

    public Contact(int id, String name, String company, String mobilePhone, String homePhone, String email) {
        this.id=id;
        this.name=name;
        this.company = company;
        this.mobilePhone = mobilePhone;
        this.homePhone=homePhone;
        this.email=email;
    }

    protected Contact(Parcel in) {
        id = in.readLong();
        name = in.readString();
        company = in.readString();
        mobilePhone = in.readString();
        homePhone = in.readString();
        email = in.readString();
        Uuid = in.readString();

    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    public Contact clone(){
        Contact clone = new Contact();
        clone.id=this.id;
        clone.name=this.name;
        clone.company=this.company;
        clone.email=this.email;
        clone.homePhone=this.homePhone;
        clone.mobilePhone=this.mobilePhone;
        clone.Uuid = this.Uuid;
        return clone;
    }

    public Long getId() {
        return id;
    }

    @Override
    public Contact setId(Long id) {
        this.id = id;
        return this;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    @Override
    public String toString() {
        return "";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeString(company);
        parcel.writeString(mobilePhone);
        parcel.writeString(homePhone);
        parcel.writeString(email);
        parcel.writeString(Uuid);
    }

    private EmbeddedContacts _embedded;
    private Links _links;

    public static Contact parse(String json) {
        //Builds the gson object from the json of the server
        GsonBuilder builder = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Gson gson = builder.create();
        //Passes it into a note
        Contact tmp=gson.fromJson(json,Contact.class);
        //Has to use the links class to pull from the json object to get the uuid
        String uuid = tmp._links.self.href;
        //split since we only want the string and not all the extra url stuff
        String[] links = uuid.split("/");

        uuid= links[links.length-1];
        tmp.setUuid(uuid);
        //If there's a reminder then set the has reminder.
        return tmp;
    }

    public static Contact[] parseArray(String json) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        //gets json object into a note array.
        List<Contact> contacts = gson.fromJson(json,Contact.class)._embedded.getContacts();
        //Loop through to set uuid and set the ishasreminder for each note in the note array
        for (int i=0; i<contacts.size();i++){
            String uuid = contacts.get(i)._links.self.href;
            String[] links = uuid.split("/");

            uuid= links[links.length-1];
            contacts.get(i).setUuid(uuid);

        }


        return contacts.toArray(new Contact[contacts.size()]);
    }


    public class EmbeddedContacts {
        private List<Contact> contacts = null;

        public List<Contact> getContacts() {
            return contacts;
        }
        public EmbeddedContacts setContacts(List<Contact> contacts) {
            this.contacts = contacts;
            return this;
        }
    }
    public class AllContacts {
        private EmbeddedContacts _embedded;
        private Links _links;

        public EmbeddedContacts getEmbedded() {
            return _embedded;
        }
        public AllContacts setEmbedded(EmbeddedContacts embeddedContacts) {
            this._embedded = embeddedContacts;
            return this;
        }

        public Links getLinks() {
            return _links;
        }
        public AllContacts setLinks(Links links) {
            this._links = links;
            return this;
        }
    }
    private class Href {
        public String href;

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }
    }
    private class Links {
        public Href self;

        public String getSelf() {
            return self.href;
        }

        public void setSelf(Href self) {
            this.self = self;
        }
    }

    public String format() {
        String json = "{\"id\":\"" + getId().toString() +
                "\",\"name\":\"" + getName()+
                "\",\"company\":\"" + getCompany() +
                "\",\"email\":\"" + getEmail() +
                "\",\"homePhone\":\"" + getHomePhone() +
                "\",\"mobilePhone\":\"" + getMobilePhone() +
                "\"}";
        return json;
    }

}
