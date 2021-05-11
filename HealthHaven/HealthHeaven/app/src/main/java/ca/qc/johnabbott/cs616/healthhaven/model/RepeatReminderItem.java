package ca.qc.johnabbott.cs616.healthhaven.model;

public class RepeatReminderItem {
    private String name;
    private Boolean value;

    public RepeatReminderItem() {
    }

    public RepeatReminderItem(String name, Boolean value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public RepeatReminderItem setName(String name) {
        this.name = name;
        return this;
    }

    public Boolean getValue() {
        return value;
    }

    public RepeatReminderItem setValue(Boolean value) {
        this.value = value;
        return this;
    }
}