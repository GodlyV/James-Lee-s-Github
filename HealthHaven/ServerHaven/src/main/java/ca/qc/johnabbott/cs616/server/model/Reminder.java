package ca.qc.johnabbott.cs616.server.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "reminder")
public class Reminder {

    @Id
    @GeneratedValue(generator = UuidGenerator.generatorName)
    @GenericGenerator(name = UuidGenerator.generatorName, strategy = "ca.qc.johnabbott.cs616.server.model.UuidGenerator")

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "id")
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "body")
    private String body;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "date")
    private Date date;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "time")
    private Date time;

    @Column(name = "hasTime")
    private boolean hasTime;

    @Column(name = "repeats")
    private String repeats;

    @Column(name = "repeatid")
    private long repeatId;

    @Column(name = "hasrepeat")
    private boolean hasRepeat;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public boolean isHasTime() {
        return hasTime;
    }

    public void setHasTime(boolean hasTime) {
        this.hasTime = hasTime;
    }

    public String getRepeats() {
        return repeats;
    }

    public void setRepeats(String repeats) {
        this.repeats = repeats;
    }

    public long getRepeatId() {
        return repeatId;
    }

    public void setRepeatId(long repeatId) {
        this.repeatId = repeatId;
    }

    public boolean isHasRepeat() {
        return hasRepeat;
    }

    public void setHasRepeat(boolean hasRepeat) {
        this.hasRepeat = hasRepeat;
    }
}
