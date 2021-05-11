package com.company;

import java.io.CharArrayReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A log entry for the logfile in Asg #1
 * @author YOU!
 */
//Log is an object that holds the date,ipAddress,service name and byte length of certain connections.
    //This class can break down the objects passed in and compares them and tostrings them.
public class Log implements Comparable<Log> {

    // Fields
    private Date timestamp;
    private IPAddress ipAddress;
    private String serviceName;
    private int length;

    // Constructors
    //Splits of the log objects
    public Log(String line) throws ParseException {
        String[] lines = line.split("\\s+");
        if(lines.length !=4){
            throw new RuntimeException("Cannot parse, Fields Missing in line:" + line);
        }
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd'T'HH:mm:ss.SSS");
        Date date = formatter.parse(lines[2]);

        this.ipAddress = new IPAddress(lines[0]);
        this.serviceName = new String(lines[1]);
        this.timestamp =date;
        this.length = Integer.parseInt(lines[3]);
    }

    public Log(Date timestamp) {

        this.timestamp = timestamp;
    }

    public Log(IPAddress ipAddress) {

        this.ipAddress = ipAddress;
    }

    public Log(int length) {

        this.length = length;
    }

    @Override
    //To strings the broken up object
    public String toString() {
        //date formater is super important for the dates that we read
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd'T'HH:mm:ss.SSS");

        return "" + ipAddress + "\t" + serviceName + " \t" + formatter.format(timestamp) + "\t" + length;
    }

    // Getters and setters
    public Date getTimestamp() {

        return timestamp;
    }

    public void setTimestamp(Date timestamp) {

        this.timestamp = timestamp;
    }

    public IPAddress getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(IPAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public int compareTo(Log rhs) {
        //compare the log 1 and log 2 through ip first, then service name, then timestamp. It returns an int which
        //we can determine which should go first through the negative, positive or 0 number
        if(this.ipAddress.compareTo(rhs.ipAddress)!=0){
            return this.ipAddress.compareTo(rhs.ipAddress);
        }
        else if(this.serviceName.compareTo(rhs.serviceName)!=0){
            return this.serviceName.compareTo(rhs.serviceName);
        }
        else if(this.timestamp.compareTo(rhs.timestamp)!=0){
            return this.timestamp.compareTo(rhs.timestamp);
        }

        return 0;
    }
}
