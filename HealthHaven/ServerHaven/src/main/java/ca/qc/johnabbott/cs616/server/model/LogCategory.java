package ca.qc.johnabbott.cs616.server.model;

public enum LogCategory {
    CARDIO(1), STRENGTH(2), ENDURANCE(3), FLEXIBILITY(4);

    private int logId;

    LogCategory(int logId) {
        this.logId = logId;
    }
    public int getLogId() {
        return logId;
    }

    public static LogCategory fromString(String option) {
        for (LogCategory b : LogCategory.values()) {
            if (b.toString().equalsIgnoreCase(option)) {
                return b;
            }
        }
        return null;
    }
}
