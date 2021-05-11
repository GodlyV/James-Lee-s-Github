package ca.qc.johnabbott.cs616.healthhaven.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ca.qc.johnabbott.cs616.healthhaven.sqlite.Identifiable;

/**
 * Used in the Health Center/Hub.
 * Tracks the fitness type, time duration, and a description
 *      for the user to see
 *
 * @author      William Fedele
 * @studentID   1679513
 * @githubUser  william-fedele
 */

public class Log implements Identifiable<Long, Log> {

    private long id;
    private String uuid;
    private LogCategory type;
    private double duration;
    private String description;
    private Date created = new Date();

    private LogJsonHelper.Links _links;

    public LogJsonHelper.Links get_links() {
        return _links;
    }

    public Log set_links(LogJsonHelper.Links _links) {
        this._links = _links;
        return this;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Log setId(Long id) {
        this.id = id;
        return this;
    }

    public Log setId(long id) {
        this.id = id;
        return this;
    }

    public String getUuid() {
        return uuid;
    }

    public Log setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public LogCategory getType() {
        return type;
    }

    public Log setType(LogCategory type) {
        this.type = type;
        return this;
    }

    public double getDuration() {
        return duration;
    }

    public Log setDuration(double duration) {
        this.duration = duration;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Log setDescription(String description) {
        this.description = description;
        return this;
    }

    public Date getCreated() {
        return created;
    }

    public static Log parse(String json) {
        GsonBuilder builder = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

        Gson gson = builder.create();
        Log log = gson.fromJson(json, Log.class);

        String test = log.get_links().getSelf().getHref();
        String uuid = test.split("/")[4];
        log.setUuid(uuid);


        return log;
    }

    public static Log[] parseArray(String json) {

        GsonBuilder builder = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

        Gson gson = builder.create();
        LogJsonHelper.AllLogsPOJO parsed = gson.fromJson(json, LogJsonHelper.AllLogsPOJO.class);
        List<Log> logs = parsed.getEmbedded().getLogs();
        for (int i = 0; i < logs.size(); i++) {

            String test = logs.get(i).get_links().getSelf().getHref();
            String uuid = test.split("/")[4];
            logs.get(i).setUuid(uuid);
        }
        return logs.toArray(new Log[logs.size()]);
    }


    public String format() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String json = "{\"type\":\"" + getType().toString() +
                "\",\"duration\":\"" + String.valueOf(getDuration()) +
                "\",\"description\":\"" + getDescription() +
                "\",\"created\":\"" + dateFormat.format(getCreated()) +
                "\"}";
        return json;
    }
}
