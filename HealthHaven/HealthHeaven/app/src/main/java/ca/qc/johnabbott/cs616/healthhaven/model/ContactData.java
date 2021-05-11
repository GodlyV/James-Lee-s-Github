package ca.qc.johnabbott.cs616.healthhaven.model;

import java.util.ArrayList;
import java.util.List;

import ca.qc.johnabbott.cs616.healthhaven.model.Contact;


public class ContactData {
    private static List<Contact> data;
    public static List<Contact> getData(){
        data = new ArrayList<>();
        data.add(new Contact(1,"Isabelle","MindGeek","514-519-5555","514-635-5235","issabelle@gmail.com"));
        data.add(new Contact(2,"James","Health Haven","514-519-1829","514-234-2342","james@gmail.com"));
        data.add(new Contact(3,"Brandon","GoGoBikes","514-524-1234","512-234-1234","brandon@gmail.com"));
        data.add(new Contact(4,"William","Health Haven","514-677-0987","123-456-7890","William@gmail.com"));
        data.add(new Contact(5,"Sean","Health Haven","514-412-6128","432-123-8745","Sean@gmail.com"));
        return data;
    }
}
