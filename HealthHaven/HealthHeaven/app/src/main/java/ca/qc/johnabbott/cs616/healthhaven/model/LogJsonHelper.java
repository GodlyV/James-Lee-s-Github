package ca.qc.johnabbott.cs616.healthhaven.model;

import java.util.List;

/**
 * Helper class for parsing JSON into Plain Old Java Objects (POJO)
 *
 * @author      William Fedele
 * @studentID   1679513
 * @githubUser  william-fedele
 */

public class LogJsonHelper {
    public class Collaborators {
        private String href;
        private Boolean templated;
    }

    public class EmbeddedLogs {
        private List<Log> logs = null;

        public List<Log> getLogs() {
            return logs;
        }
        public EmbeddedLogs setLogs(List<Log> logs) {
            this.logs = logs;
            return this;
        }
    }


    public class AllLogsPOJO {
        private EmbeddedLogs _embedded;
        private Links _links;

        public EmbeddedLogs getEmbedded() {
            return _embedded;
        }
        public AllLogsPOJO setEmbedded(EmbeddedLogs embeddedNotes) {
            this._embedded = embeddedNotes;
            return this;
        }

        public Links getLinks() {
            return _links;
        }
        public AllLogsPOJO setLinks(Links links) {
            this._links = links;
            return this;
        }
    }

    public class Links {
        private Self self;
        private Log_ notes;
        private Log_ log;
        private Collaborators collaborators;
        private Profile profile;

        public Log_ getLog() {
            return log;
        }

        public Links setLog(Log_ log) {
            this.log = log;
            return this;
        }
        public Profile getProfile() {
            return profile;
        }

        public Links setProfile(Profile profile) {
            this.profile = profile;
            return this;
        }

        public User_ getUser() {
            return user;
        }

        public Links setUser(User_ user) {
            this.user = user;
            return this;
        }

        private User_ user;

        public Self getSelf() {
            return self;
        }

        public Links setSelf(Self self) {
            this.self = self;
            return this;
        }

        public Log_ getLogs() {
            return notes;
        }

        public Links setLogs(Log_ notes) {
            this.notes = notes;
            return this;
        }
        public Collaborators getCollaborators() {
            return collaborators;
        }

        public Links setCollaborators(Collaborators collaborators) {
            this.collaborators = collaborators;
            return this;
        }
    }


    public class User_ {
        private String href;
        private Boolean templated;

        public String getHref() {
            return href;
        }

        public User_ setHref(String href) {
            this.href = href;
            return this;
        }

        public Boolean getTemplated() {
            return templated;
        }

        public User_ setTemplated(Boolean templated) {
            this.templated = templated;
            return this;
        }
    }

    public class Log_ {
        private String href;
    }


    public class Profile {
        private String href;
    }

    public class Self {
        private String href;

        public String getHref() {
            return href;
        }
        public Self setHref(String href) {
            this.href = href;
            return this;
        }
    }


}
