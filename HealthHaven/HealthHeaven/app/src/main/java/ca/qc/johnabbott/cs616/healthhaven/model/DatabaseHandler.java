package ca.qc.johnabbott.cs616.healthhaven.model;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.List;

import ca.qc.johnabbott.cs616.healthhaven.sqlite.DatabaseException;
import ca.qc.johnabbott.cs616.healthhaven.sqlite.Table;
import ca.qc.johnabbott.cs616.healthhaven.sqlite.TableFactory;

/**
 * A DatabaseHandler which manages our interaction with
 * SQLiteOpenHelper and the Table and TableFactory classes.
 *
 * Using this we can handle reminders, LOGS, CONTACTS, and MEDICALINFO.
 *
 * @author Brandon Cameron  (1770091@johnabbottcollege.net) (REMINDERS)
 * @author William Fedele   (1679513@johnabbottcollege.net) (LOGS)
 * @author James Lee        (1771609@johnabbottcollege.net) (CONTACTS)
 * @author Sean Meath       (studentID@johnabbottcollege.net) (MEDICALINFO)
 *
 * Implements singleton. Usage: DatabaseHandler.getInstance(getContext)
 *
 * Examples
 * DatabaseHandler.getInstance(getContext).getTableName() (In fragment)
 * OR DatabaseHandler.getInstance(getApplicationContext).getTableName() (In activity)
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "healthhaven.db";
    public static final int DATABASE_VERSION = 1;
    public static final String DATE_FORMAT = "yyyy-MM-dd hh:mm";

    private static DatabaseHandler instance;

    private final Table<Reminder> reminderTable;
    private final Table<Log> logTable;
    private final Table<MedicalInfo> medInfoTable;
    private final Table<Contact> contactTable;

    public DatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        reminderTable = TableFactory.makeFactory(this, Reminder.class)
                .setSeedData(ReminderSampleData.getReminderData())
                .useDateFormat(DATE_FORMAT)
                .getTable();
        logTable = TableFactory.makeFactory(this, Log.class)
                .setSeedData(LogSampleData.getLogData())
                .getTable();
        List medInfo = MedicalInfoSampleData.getMedicalConditionData();
        medInfo.addAll(MedicalInfoSampleData.getAllergyData());
        medInfoTable = TableFactory.makeFactory(this, MedicalInfo.class)
                .setSeedData(medInfo)
                .getTable();
        contactTable = TableFactory.makeFactory(this,Contact.class)
                        .setSeedData(ContactData.getData())
                        .getTable();
    }

    public static DatabaseHandler getInstance(Context context) {
        if(instance == null) {
            instance = new DatabaseHandler(context);
        }
        return instance;
    }

    public Table<Contact> getContactTable(){return contactTable;}

    public Table<Reminder> getReminderTable() {
        return reminderTable;
    }

    public Table<Log> getLogTable() { return logTable; }

    public Table<MedicalInfo> getMedInfoTable() {
        return medInfoTable;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create new table
        db.execSQL(reminderTable.getCreateTableStatement());
        db.execSQL(logTable.getCreateTableStatement());
        db.execSQL(medInfoTable.getCreateTableStatement());
        db.execSQL(contactTable.getCreateTableStatement());
        // Populate
        if (reminderTable.hasInitialData())
            reminderTable.initialize(db);

        if(logTable.hasInitialData())
            logTable.initialize(db);

        if(medInfoTable.hasInitialData())
            medInfoTable.initialize(db);

        if(contactTable.hasInitialData())
            contactTable.initialize(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop old table
        db.execSQL(reminderTable.getDropTableStatement());
        db.execSQL(logTable.getDropTableStatement());
        db.execSQL(medInfoTable.getDropTableStatement());
        db.execSQL(contactTable.getDropTableStatement());


        // Create new table
        db.execSQL(reminderTable.getCreateTableStatement());
        db.execSQL(logTable.getCreateTableStatement());
        db.execSQL(medInfoTable.getCreateTableStatement());

        db.execSQL(logTable.getDropTableStatement());

        // Populate
        if (reminderTable.hasInitialData())
            reminderTable.initialize(db);

        if (logTable.hasInitialData())
            logTable.initialize(db);

        if (medInfoTable.hasInitialData())
            medInfoTable.initialize(db);
        if (contactTable.hasInitialData())
            contactTable.initialize(db);
    }

    /**
     * Clears the log table of all logs
     */
    public void deleteAllLogs() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("log", null, null);
    }
    public void deleteAllContacts() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("contact", null, null);
    }

    /**
     * Clears the medicalInformation table of all info
     */
    public void deleteAllMedicalInfo() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("medicalinfo", null, null);
    }

    /**
     * Clears the reminder table
     */
    public void deleteAllReminders() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("reminder", null, null);
    }

    /**
     *  Add a list of logs to the log table.
     * @param logs
     */
    public void addListLogs(List<Log> logs) {
        for (Log l : logs) {
            try {
                getLogTable().create(l);
            } catch (DatabaseException e) {
                e.printStackTrace();
            }
        }
    }
    public void addListContacts(List<Contact> contacts) {

        for (Contact l : contacts) {
            try {
                getContactTable().create(l);
            } catch (DatabaseException e) {
                e.printStackTrace();
            }
        }
    }
    public void addListMedicalInfo(List<MedicalInfo> medInfoes) {

        for (MedicalInfo m : medInfoes) {
            try {
                getMedInfoTable().create(m);
            } catch (DatabaseException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Populates the reminders table given a list of reminders
     * @param reminders
     */
    public void addListReminders(List<Reminder> reminders) {

        for (Reminder reminder : reminders) {
            try {
                getReminderTable().create(reminder);
            } catch (DatabaseException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isReminderTableExists(){
        // Check if Table Name exists
        String query = "select DISTINCT tbl_name from sqlite_master where tbl_name ='"+reminderTable.getName()+"'";

        SQLiteDatabase db = getReadableDatabase();
        // Get count of tables with that name , 0 means none, 1 means it exists
        try (Cursor cursor = db.rawQuery(query, null)){
            if(cursor!=null){
                if(cursor.getCount()>0){
                    return true;
                }
            }
        }
        return false;
    }

    public int getReminderCount(){
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, reminderTable.getName());
        db.close();
        return (int)count;
    }

    public int getContactCount(){
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, contactTable.getName());
        db.close();
        return (int)count;
    }
}
