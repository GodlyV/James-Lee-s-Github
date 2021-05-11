package ca.qc.johnabbott.cs616.healthhaven.model;

        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.List;

public class LogSampleData {
    public static List<Log> logData;

    public static List<Log> getLogData() {
        logData = new ArrayList<>();

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd hh:mm:ss");


        logData.add(new Log()
                .setId((long) 1)
                .setType(LogCategory.CARDIO)
                .setDuration(1.65)
                .setDescription("Lorem ipsum dolor sit amet"));

        logData.add(new Log()
                .setId((long) 2)
                .setType(LogCategory.STRENGTH)
                .setDuration(3)
                .setDescription("consectetur adipiscing elit"));

        logData.add(new Log()
                .setId((long) 3)
                .setType(LogCategory.ENDURANCE)
                .setDuration(1.25)
                .setDescription("sed do eiusmod tempor"));

        logData.add(new Log()
                .setId((long) 4)
                .setType(LogCategory.FLEXIBILITY)
                .setDuration(0.5)
                .setDescription("incididunt ut labore et dolore"));

        logData.add(new Log()
                .setId((long) 5)
                .setType(LogCategory.CARDIO)
                .setDuration(1.65)
                .setDescription("magna aliqua"));

        logData.add(new Log()
                .setId((long) 6)
                .setType(LogCategory.STRENGTH)
                .setDuration(2)
                .setDescription("Ut enim ad minim veniam"));

        logData.add(new Log()
                .setId((long) 7)
                .setType(LogCategory.ENDURANCE)
                .setDuration(1)
                .setDescription("quis nostrud exercitation ullamco laboris"));

        logData.add(new Log()
                .setId((long) 8)
                .setType(LogCategory.FLEXIBILITY)
                .setDuration(0.75)
                .setDescription("nisi ut aliquip ex ea commodo"));



        //return logData;
        return new ArrayList<>();
    }
}
