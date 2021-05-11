package ca.qc.johnabbott.cs616.healthhaven.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ReminderSampleData {
    public static List<Reminder> reminderData;

    public static List<Reminder> getReminderData(){
        reminderData = new ArrayList<>();

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd hh:mm:ss");

        try {

            // Full Reminder
            reminderData.add(new Reminder()
                    .setId((long) 1)
                    .setBody("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Congue quisque egestas diam in. Praesent semper feugiat nibh sed. Sem nulla pharetra diam sit amet nisl. Sapien et ligula ullamcorper malesuada proin libero nunc consequat. Platea dictumst quisque sagittis purus sit amet. Lectus quam id leo in vitae turpis massa sed elementum. Amet massa vitae tortor condimentum lacinia quis. Euismod elementum nisi quis eleifend quam adipiscing vitae proin sagittis. Id semper risus in hendrerit gravida rutrum. Curabitur gravida arcu ac tortor. Ipsum dolor sit amet consectetur adipiscing elit ut aliquam. Venenatis lectus magna fringilla urna porttitor rhoncus dolor purus non. Turpis egestas integer eget aliquet nibh praesent. Nulla at volutpat diam ut venenatis. Ultrices tincidunt arcu non sodales neque sodales ut. Ultrices tincidunt arcu non sodales neque sodales ut.")
                    .setTitle("Title A")
                    .setDate(format.parse("20191129 15:05:58"))
                    .setTime(format.parse("20191129 15:05:58"))
                    .setHasTime(true));

            // Reminder With No Time
            reminderData.add(new Reminder()
                    .setId((long) 2)
                    .setBody("Praesent semper feugiat nibh sed. Sem nulla pharetra diam sit amet nisl. Sapien et ligula ullamcorper malesuada proin libero nunc consequat. Platea dictumst quisque sagittis purus sit amet. Lectus quam id leo in vitae turpis massa sed elementum. Amet massa vitae tortor condimentum lacinia quis. Euismod elementum nisi quis eleifend quam adipiscing vitae proin sagittis. Id semper risus in hendrerit gravida rutrum. Curabitur gravida arcu ac tortor. Ipsum dolor sit amet consectetur adipiscing elit ut aliquam. Venenatis lectus magna fringilla urna porttitor rhoncus dolor purus non. Turpis egestas integer eget aliquet nibh praesent. Nulla at volutpat diam ut venenatis. Ultrices tincidunt arcu non sodales neque sodales ut. Ultrices tincidunt arcu non sodales neque sodales ut.")
                    .setTitle("Title B"));

            reminderData.add(new Reminder()
                    .setId((long) 3)
                    .setBody("Nulla at volutpat diam ut venenatis. Ultrices tincidunt arcu non sodales neque sodales ut. Ultrices tincidunt arcu non sodales neque sodales ut.")
                    .setTitle("Title C")
                    .setDate(format.parse("20191129 23:05:58"))
                    .setTime(format.parse("20191129 23:05:58"))
                    .setHasTime(true));

            reminderData.add(new Reminder()
                    .setId((long) 4)
                    .setBody("It's almost Christmas!.")
                    .setTitle("Christmas Eve!")
                    .setDate(format.parse("20191224 23:55:58"))
                    .setTime(format.parse("20191224 23:55:58"))
                    .setHasTime(true));

            reminderData.add(new Reminder()
                    .setId((long) 5)
                    .setBody("hahahahahahahahaltrices tincidunt arcu non sodales neque sodales ut. Ultrices tincidunt arcu non sodales neque sodales ut.")
                    .setTitle("Odd Reminder!")
                    .setDate(format.parse("20191224 23:57:58"))
                    .setTime(format.parse("20191224 23:57:58"))
                    .setHasTime(false));

            reminderData.add(new Reminder()
                    .setId((long) 6)
                    .setBody("It's Christmas!")
                    .setTitle("Christmas!")
                    .setDate(format.parse("20191225 00:00:00"))
                    .setTime(format.parse("20191225 00:00:00"))
                    .setHasTime(true)
                    .setRepeatId(25)
                    .setHasRepeat(true)
                    .setRepeats("Monday,Tuesday,Wednesday,Thursday,Friday"));

            reminderData.add(new Reminder()
                    .setId((long) 7)
                    .setBody("It's Christmas!")
                    .setTitle("Christmas!")
                    .setDate(format.parse("20191226 00:00:00"))
                    .setTime(format.parse("20191226 00:00:00"))
                    .setHasTime(true)
                    .setRepeatId(25)
                    .setHasRepeat(true)
                    .setRepeats("Monday,Tuesday,Wednesday,Thursday,Friday"));

            reminderData.add(new Reminder()
                    .setId((long) 8)
                    .setBody("It's Christmas!")
                    .setTitle("Christmas!")
                    .setDate(format.parse("20191227 00:00:00"))
                    .setTime(format.parse("20191227 00:00:00"))
                    .setHasTime(true)
                    .setRepeatId(25)
                    .setHasRepeat(true)
                    .setRepeats("Monday,Tuesday,Wednesday,Thursday,Friday"));

            // Empty Reminder
            /*
            reminderData.add(new Reminder()
                    .setId((long) 3));
            */

        } catch (ParseException e) {
            // shouldn't work
        }

        return new ArrayList<>();
        //return reminderData;
    }
}
