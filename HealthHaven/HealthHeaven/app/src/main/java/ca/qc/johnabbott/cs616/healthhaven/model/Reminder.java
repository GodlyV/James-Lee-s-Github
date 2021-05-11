package ca.qc.johnabbott.cs616.healthhaven.model;

import com.google.gson.GsonBuilder;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import ca.qc.johnabbott.cs616.healthhaven.sqlite.Identifiable;

/**
 * A Reminder Class
 *
 * Has a:
 *
 *          @id         unique identifier for local db
 *          @uuid       unique identifier for server db
 *          @title      title of reminder
 *          @body       body of reminder
 *          @date       date of reminder
 *          @time       time of reminder
 *          @hasTime    if specific time is set
 *          @repeats    list of days to repeat reminder
 *          @repeatId   unique identifies for repeated reminders
 *          @hasRepeat  if reminders repeat
 *
 * Reminders always have:           @title, @body, @date
 * Reminders can also have:         @time, @repeats
 * Identifiers:                     @id, @uuid, @repeatId
 * Flags:                           @hasTime, @hasRepeat
 *
 *
 * @author      Brandon Cameron
 * @studentID   1770091
 * @githubUser  brcameron
 */

public class Reminder implements Identifiable<Long, Reminder> {

    //region Inner Classes

    public class Reminders{

        private long id;
        private String title;
        private String body;
        private Date date;
        private Date time;
        private boolean hasTime;
        private String repeats;
        private long repeatId;
        private boolean hasRepeat;
        private EmbeddedCollaborators _embedded;
        private Links _links;

        public long getId() {
            return id;
        }

        public Reminders setId(long id) {
            this.id = id;
            return this;
        }

        public String getTitle() {
            return title;
        }

        public Reminders setTitle(String title) {
            this.title = title;
            return this;
        }

        public String getBody() {
            return body;
        }

        public Reminders setBody(String body) {
            this.body = body;
            return this;
        }

        public Date getDate() {
            return date;
        }

        public Reminders setDate(Date date) {
            this.date = date;
            return this;
        }

        public Date getTime() {
            return time;
        }

        public Reminders setTime(Date time) {
            this.time = time;
            return this;
        }

        public boolean isHasTime() {
            return hasTime;
        }

        public Reminders setHasTime(boolean hasTime) {
            this.hasTime = hasTime;
            return this;
        }

        public String getRepeats() {
            return repeats;
        }

        public Reminders setRepeats(String repeats) {
            this.repeats = repeats;
            return this;
        }

        public long getRepeatId() {
            return repeatId;
        }

        public Reminders setRepeatId(long repeatId) {
            this.repeatId = repeatId;
            return this;
        }

        public boolean isHasRepeat() {
            return hasRepeat;
        }

        public Reminders setHasRepeat(boolean hasRepeat) {
            this.hasRepeat = hasRepeat;
            return this;
        }

        public EmbeddedCollaborators get_embedded() {
            return _embedded;
        }

        public void set_embedded(EmbeddedCollaborators _embedded) {
            this._embedded = _embedded;
        }

        public Links get_links() {
            return _links;
        }

        public Reminders set_links(Links _links) {
            this._links = _links;
            return this;
        }
    }

    public class Collaborators{
        private String name;
        private String uuid;
        private String email;
        private Links _links;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Links get_links() {
            return _links;
        }

        public void set_links(Links _links) {
            this._links = _links;
        }
    }

    public class EmbeddedCollaborators{

        private Collaborators[] collaborators;

        public Collaborators[] getCollaborators() {
            return collaborators;
        }

        public EmbeddedCollaborators setCollaborators(Collaborators[] collaborators) {
            this.collaborators = collaborators;
            return this;
        }
    }

    public class EmbeddedReminders{

        private Reminders[] reminders;

        public Reminders[] getReminders() {
            return reminders;
        }

        public EmbeddedReminders setReminders(Reminders[] reminders) {
            this.reminders = reminders;
            return this;
        }
    }

    public class EmbeddedReminder{

        private EmbeddedReminders _embedded;

        public EmbeddedReminders get_embedded() {
            return _embedded;
        }

        public EmbeddedReminder set_embedded(EmbeddedReminders _embedded) {
            this._embedded = _embedded;
            return this;
        }
    }

    public class Link{

        private URL href;
        private boolean templated;

        public URL getHref() {
            return href;
        }

        public void setHref(URL href) {
            this.href = href;
        }

        public boolean isTemplated() {
            return templated;
        }

        public void setTemplated(boolean templated) {
            this.templated = templated;
        }

    }

    public class Links{

        private Link self;
        private Link reminder;
        private Link collaborators;

        public Link getSelf() {
            return self;
        }

        public Links setSelf(Link self) {
            this.self = self;
            return this;
        }

        public Link getReminder() {
            return reminder;
        }

        public Links setReminder(Link reminder) {
            this.reminder = reminder;
            return this;
        }

        public Link getCollaborators() {
            return collaborators;
        }

        public Links setCollaborators(Link collaborators) {
            this.collaborators = collaborators;
            return this;
        }
    }

    //endregion

    private long id;
    private String uuid;
    private String title;
    private String body;
    private Date date;
    private Date time;
    private boolean hasTime;
    private String repeats;
    private long repeatId;
    private boolean hasRepeat;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat timeFormat= new SimpleDateFormat("hh:mm");

    /**
     * Create a blank reminder.
     */
    public Reminder() {this(-1);}

    /**
     * Create a blank reminder with a specific ID.
     * @param id
     */
    public Reminder(long id) {
        this.id = id;
        this.date = new Date();
        this.hasTime = false;
        this.repeatId = -1;
        this.repeats = "";
    }

    /**
     * Create a Reminder without a specified time.
     * @param id
     * @param uuid
     * @param title
     * @param body
     */
    public Reminder(long id, String uuid, String title, String body) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.date = new Date();
        this.hasTime = false;
        this.repeatId = -1;
        this.repeats = "";
    }

    /**
     * Create a Reminder with a specified date
     * @param id
     * @param uuid
     * @param title
     * @param body
     * @param date
     */
    public Reminder(long id, String uuid, String title, String body, Date date) {
        this.id = id;
        this.uuid = uuid;
        this.title = title;
        this.body = body;
        this.date = date;
        this.hasTime = false;
        this.repeatId = -1;
        this.repeats = "";
    }

    /**
     * Create a Reminder with a specified date and time
     * @param id
     * @param uuid
     * @param title
     * @param body
     * @param date
     * @param time
     */
    public Reminder(long id, String uuid, String title, String body, Date date, Date time) {
        this.id = id;
        this.uuid = uuid;
        this.title = title;
        this.body = body;
        this.date = date;
        this.time = time;
        this.hasTime = true;
        this.repeatId = -1;
        this.repeats = "";
    }

    /**
     * Create a Reminder with a specified repeats
     * @param id
     * @param uuid
     * @param title
     * @param body
     * @param date
     * @param time
     */
    public Reminder(long id, String uuid, String title, String body, Date date, Date time, String repeats) {
        this.id = id;
        this.uuid = uuid;
        this.title = title;
        this.body = body;
        this.date = date;
        this.time = time;
        this.hasTime = true;
        this.repeats = repeats;
        this.hasRepeat = true;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Reminder setId(Long id) {
        this.id = id;
        return this;
    }

    public String getUuid() {
        return uuid;
    }

    public Reminder setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Reminder setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getBody() {
        return body;
    }

    public Reminder setBody(String body) {
        this.body = body;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public Reminder setDate(Date date) {
        this.date = date;
        return this;
    }

    public Date getTime() {
        return time;
    }

    public Reminder setTime(Date time) {
        this.time = time;
        return this;
    }

    public boolean isHasTime() {
        return hasTime;
    }

    public Reminder setHasTime(boolean hasTime) {
        this.hasTime = hasTime;
        return this;
    }

    public long getRepeatId() {
        return repeatId;
    }

    public Reminder setRepeatId(long repeatId) {
        this.repeatId = repeatId;
        return this;
    }

    public boolean isHasRepeat() {
        return hasRepeat;
    }

    public Reminder setHasRepeat(boolean hasRepeat) {
        this.hasRepeat = hasRepeat;
        return this;
    }

    public String getRepeats() {
        return repeats;
    }

    public Reminder setRepeats(String repeats) {
        this.repeats = repeats;
        return this;
    }

    @Override
    public String toString() {
        return "Reminder{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", date=" + date +
                ", time=" + time +
                ", hasTime=" + hasTime +
                ", repeats='" + repeats + '\'' +
                ", repeatId=" + repeatId +
                ", hasRepeat=" + hasRepeat +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reminder reminder = (Reminder) o;
        return id == reminder.id &&
                hasTime == reminder.hasTime &&
                repeatId == reminder.repeatId &&
                hasRepeat == reminder.hasRepeat &&
                Objects.equals(uuid, reminder.uuid) &&
                Objects.equals(title, reminder.title) &&
                Objects.equals(body, reminder.body) &&
                Objects.equals(date, reminder.date) &&
                Objects.equals(time, reminder.time) &&
                Objects.equals(repeats, reminder.repeats);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, title, body, date, time, hasTime, repeats, repeatId, hasRepeat);
    }

    /**
     * Returns a new instance of a copies Reminder
     * @return
     */
    public Reminder clone(){
        Reminder clone = new Reminder();
        clone.id = this.id;
        clone.title = this.title;
        clone.body = this.body;
        clone.uuid = this.uuid;
        clone.date = this.date;
        clone.time = this.time;
        clone.hasTime = this.hasTime;
        clone.repeats = this.repeats;
        clone.hasRepeat = this.hasRepeat;
        clone.repeatId = this.repeatId;
        return clone;
    }

    /**
     * Returns the JSON representation of a Reminder
     * @return
     */
    public String format() {

        String json = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                .serializeNulls()
                .create()
                .toJson(this);

        return json;
    }

    /**
     * Parses JSON into a single Reminder
     * @param json
     * @return
     */
    public static Reminder parse(String json){
        Reminders tmp = new GsonBuilder().setPrettyPrinting().create().fromJson(json, Reminders.class);
        return new Reminder()
                .setId(tmp.getId())
                .setTitle(tmp.getTitle())
                .setBody(tmp.getBody())
                .setDate(tmp.getDate())
                .setTime(tmp.getTime() != null ? tmp.time : null)
                .setHasTime(!(tmp.getTime() == null))
                .setRepeats(tmp.getRepeats())
                .setRepeatId(tmp.getRepeatId())
                .setHasRepeat(!(tmp.getRepeats() == null))
                .setUuid(tmp.get_links().getSelf().getHref().getPath().split("/")[2]);
    }

    /**
     * Parses JSON into an array of Reminders
     * @param json
     * @return
     */
    public static Reminder[] parseArray(String json){
        Reminders[] tmpReminders = new GsonBuilder().setPrettyPrinting().create().fromJson(json, EmbeddedReminder.class).get_embedded().getReminders();

        Reminder[] ret = new Reminder[tmpReminders.length];

        for (int i = 0; i < tmpReminders.length; i++){
            ret[i] = new Reminder()
                    .setId(tmpReminders[i].getId())
                    .setTitle(tmpReminders[i].getTitle())
                    .setBody(tmpReminders[i].getBody())
                    .setDate(tmpReminders[i].getDate())
                    .setTime(tmpReminders[i].getTime() != null ? tmpReminders[i].time : null)
                    .setHasTime(!(tmpReminders[i].getTime() == null))
                    .setRepeats(tmpReminders[i].getRepeats())
                    .setRepeatId(tmpReminders[i].getRepeatId())
                    .setHasRepeat(!(tmpReminders[i].getRepeats() == null))
                    .setUuid(tmpReminders[i].get_links().getSelf().getHref().getPath().split("/")[2]);
        }
        return ret;
    }
}
