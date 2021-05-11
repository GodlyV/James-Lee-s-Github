package com.company;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class Main {
    private static class DoubleLink<T> {
        public T element;
        public DoubleLink<T> next;
        public DoubleLink<T> prev;
        public DoubleLink() {}
        public DoubleLink(T element) {
            this.element = element;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);  // Create a Scanner object
        boolean isClockwise = true;
        int cases = 0;
        Integer n = userInput(scanner,cases);
        Integer m = userInput(scanner,(cases+1));
        Integer o = userInput(scanner,(cases+2));;

        if((m + o) == 0){
            System.out.println("You have at least remove in one direction, please re-input o");
            o = scanner.nextInt();
        }
        List OutPut = new ArrayList();  //List to hold all the elements of links that we removed
        DoubleLink index = createLink(n);//Creates n amount of links into the double link index

        while(OutPut.size() <n){      //loop through till we get through every link in the doublelink
            if(isClockwise){            //Decides which way to count towards when we remove a link
                index = remove(m,index,isClockwise,OutPut);//remove a link from the double link
                isClockwise = false;    //After removing from clockwise side, remove from the counter clockwise side
            }
            else{
                index = remove(o,index,isClockwise,OutPut);
                isClockwise= true;
            }

        }
        System.out.println(OutPut);     //prints all the links in the order that we removed them from
    }
    public static int userInput(Scanner scanner, int cases){    //asks for user input
        Integer value = null;           //this is to hold the next int that the user inputs
        switch (cases){                 //which number do we want from them?
            case 0:                     //asking for numbers in double link
                System.out.println("How many links in your circle?");
                value = scanner.nextInt(); //Read user input
                while(!(value >0)){
                    System.out.println("n cannot less than 0");
                    value = scanner.nextInt();
                }
                break;

            case 1:                     //asking for which clockwise link to remove
                System.out.println("In clockwise, which link do you want to remove");

                value = scanner.nextInt();

                while(!(value>=0)){
                    System.out.println("You have to move at least by 1");

                    value = scanner.nextInt();
                }
                break;
            case 2:                     //asking for which counter clockwise link to remove
                System.out.println("In counterclockwise, which link do you want to remove");
                value = scanner.nextInt();

                while(!(value >=0)){
                    System.out.println("You have to move clockwise by at least 1 ");
                    value = scanner.nextInt();
                }
                break;

        }
        return value;
    }
    public static DoubleLink remove(Integer steps,DoubleLink link,boolean isClockwise,List OutPut){ //Removes the links
        DoubleLink temp = link;     //temporary link to get to where in the link we need to remove
            for (int i = 1;i<steps;i++){//go to the link we need to get to in clockwise or counter clockwise
                if(isClockwise){
                    temp = temp.next;//this steps to next link
                }
                else{
                    temp = temp.prev;//this steps back to previous link
                }
            }
            if(steps >0){   //Make sure we have moved in the first place before removing any links
                link = temp.next;   //set our link to the next link so since the current one is the one we are removing
                OutPut.add(temp.element);//add the current element to list so we can output in one line later
                temp.prev.next = temp.next;//set the previous' next to the next so it skips over the current one
                temp.next.prev = temp.prev;//set the next's previous to the previous so that it skips over the current one//we removed an element so tick down the amount of links in the double link
            }
            return link;                    //return our link

    }

    public static DoubleLink createLink(Integer n){ //this creates all of our links

        DoubleLink links = new DoubleLink(1);   //creates the link starting at 1
        for (int i = 2; i<= n;i++ ){    //goes through the amount of links in the circle and creates the double link
            DoubleLink temp = new DoubleLink(i);//makes a new temporary link
            if(links.next !=null){  //make sure there is a next element
                links.next.prev = temp;//puts the next elements previous to the new doublelink we created
                temp.next = links.next; //keeps hold of the next temporary that we create
            }
            else if(links.prev == null) {   //if our link does not contain a previous link
                temp.next = links;          //set our temporary's next to the initial link we created
                links.prev = temp;          //set links previous to temporary since we only have one link at this point
            }
            links.next = temp;              //set the next link in chain the the temporary one we created
            temp.prev = links;              //set the previous temporary link to the links

            links = temp;                   //add the temporary to the chain
        }

        return links.next;
    }
}
