package com.company;

import java.io.*;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args){
        //Files being assigned
        File file1 = new File("data/test8/in1.txt");
        File file2 = new File ("data/test8/in2.txt");
        File out = new File("data/test8/out.txt");
        //call to merge method
        merge(file1,file2,out);

    }
    public static void merge(File file1, File file2, File out){
        //Put in try catch to make sure to not let an error pass through
        try{
            //File readers are initialized
            FileReader fr1 = new FileReader(file1);
            FileReader fr2 = new FileReader(file2);

            //Scanners being initialized
            Scanner sc1 = new Scanner(fr1);
            Scanner sc2 = new Scanner(fr2);

            //FileWriter being initialized
            FileWriter writer = new FileWriter(out);

            //Strings being declared to empty
            String line1 ="";
            String line2 ="";

            //bools in case of empty files
            boolean EmptyFile1 = false;
            boolean EmptyFile2 = false;

            //Setting files are empty if there are no lines in file
            if (!sc1.hasNextLine()){
                EmptyFile1 = true;
            }
            else{
                //Initialize line1 to the first line
                line1 = sc1.nextLine();
            }
            if(!sc2.hasNextLine()){
                EmptyFile2= true;
            }
            else{
                line2 = sc2.nextLine();
            }
            //initialize the logs
            Log l1 = null;
            Log l2 = null;

            while((sc1.hasNextLine()||sc2.hasNextLine())&&(!EmptyFile1&&!EmptyFile2)){ //No empty files or lines
                //setting the logs to the new lines
                l1 = new Log(line1);
                l2 = new Log(line2);

                //using the compare to method and setting the return to an int.
                int Out = l1.compareTo(l2);
                //If l1 is before l2
                if(Out < 0){
                    //write out the line and check if there is another line in this file
                    writer.write(l1.toString() + System.lineSeparator());

                    if(sc1.hasNextLine()){
                        line1 = (sc1.nextLine());
                    }
                    else{
                        //if reached end of file of file1, empty out the second file
                        writer.write(l2.toString()+System.lineSeparator());

                        while(sc2.hasNextLine()){
                            l2 = new Log(sc2.nextLine());
                            writer.write(l2.toString()+System.lineSeparator());
                        }
                    }
                }
                //
                else if (Out > 0){
                    //if l2 is before l1
                    writer.write(l2.toString() +System.lineSeparator());
                    //check if there is another line in file2
                    if (sc2.hasNextLine()){
                        //assign next line to line2
                        line2 = (sc2.nextLine());
                    }
                    else{
                        writer.write(l1.toString()+System.lineSeparator());
                        //if reached end of file of file2, empty out the first file
                        while(sc1.hasNextLine()){
                            l1 = new Log(sc1.nextLine());
                            writer.write(l1.toString() + System.lineSeparator());
                        }
                    }
                }
                else{
                    //if they are the exact same, then output both of them
                    writer.write(l1.toString()+ System.lineSeparator());
                    writer.write(l2.toString()+ System.lineSeparator());
                    sc1.nextLine();
                    sc2.nextLine();
                }
                //makes sure to actually write them to file.
                writer.flush();
            }
            //The first file is empty
            if(EmptyFile1){
                //initialize the log and write out the current line in second file
                l2 = new Log(line2);
                writer.write(l2.toString() +System.lineSeparator());

                //Empty out the second file since there is nothing in first file
                while(sc2.hasNextLine()) {
                    l2 = new Log(sc2.nextLine());
                    writer.write(l2.toString() + System.lineSeparator());
                }
                //makes sure to write to file
                writer.flush();
            }
            //The second file is empty
            if(EmptyFile2){
                //initialize the first log and write it out
                l1 = new Log(line1);
                writer.write(l1.toString() +System.lineSeparator());
                //Empty out first file as nothing in second file
                while(sc1.hasNextLine()){
                    l1 = new Log(sc1.nextLine());
                    writer.write(l1.toString()+System.lineSeparator());
                }
                //makes sure to write it out
                writer.flush();
            }
            //make sure to close all of the readers,writers and scanners
            sc1.close();
            sc2.close();
            writer.close();
            fr1.close();
            fr2.close();

        }
        catch (IOException ex) {
            //If anything goes wrong in the try

            System.out.println("Error");
        }
        //errors of timestamp
        catch (RuntimeException ex){
            System.out.println(ex.getMessage());
        }
        catch(ParseException ex){
            System.out.println(ex.getMessage());
        }
    }
}
    /**
     * Merges the files `in1` and `in2` into `out`, maintaining the sorted order.
     * @param in1 the first sorted input file.
     * @param in2 the second sorted input file.
     * @param out the destination of the merged files.
     * @throws IOException
     */

