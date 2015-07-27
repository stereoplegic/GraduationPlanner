/*
 * Copyright (C) 2015 Michael Bybee
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * Graduation Planner
 * Takes user input for CUs per term/semester attempted and CUs for each class,
 * giving remaining terms, classes, CUs, cost.
 * Also breaks down, by term, classes, CUs and cost.
 * @author Michael Bybee (mbybee1@wgu.edu)
 */
import java.util.Scanner;
import java.util.ArrayList;
public class GraduationPlanner {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int monthsperterm = 6; //1 term = 6 months
        int costperterm = 2890; //Tuition cost per term
        int minCUperterm = 12; //WGU requires a minimum 12 CUs attempted per term
        System.out.println("How many CUs would you like to take per term at minimum?");
        String selectedCUpertermstring = scan.next();//Start as a string to prevent input errors from halting program.
        /* Validate CU per term input, discarding non-numeric characters
        and entries less than the 12/term minimum */
        while (!selectedCUpertermstring.matches("^[0-9]{2}$") || Integer.parseInt(selectedCUpertermstring) < minCUperterm) {
            System.out.println("Invalid input. Positive integers only.");
            System.out.println("You must attempt at least 12 CUs per term. Try again:");
            selectedCUpertermstring = scan.next();//retry if input invalid
        }
        int selectedCUperterm = Integer.parseInt(selectedCUpertermstring);//Convert to integer for addition later
        String anotherclass = "";
        int totalclasses = 0;//Start total classes at 0 before user input
        int classCU;//No need to initiate yet
        int termCU = 0;//Start CUs for given term at 0 pending input
        int totalCU = 0;//Same for total CUs
        int termnum = 1;//Obviously, the 1st term is labled 1
        int classnum = 1;//Same for the 1st class
        ArrayList<int[]> overall = new ArrayList<>();//Dynamic array for per-term breakdown after finished entering class CUs, since we don't know how many terms are required
        int[] termbreakdown;//Static array inside ArrayList, since we know we'll be displaying terms, # of classes and CUs, and tuition cost
        outerloop://Break label to break out of CU entry loop when finished
        while (!anotherclass.substring(0).equalsIgnoreCase("N")) {
            totalclasses ++;//increment total number of classes
            System.out.println("Enter the number of CUs for term " + termnum +" class " + classnum + " -- class " + totalclasses + " overall:");
            String classCUstring = scan.next();//Again, start as string to prevent user from crashing with invalid input
            while (!classCUstring.matches("^[1-9]$")) {//only single-digit positive integers allowed, since no classes offer double-digit CUs
                System.out.println("CU amount must be a positive integer. Try again:");
                classCUstring = scan.next();
                
            }
            classCU = Integer.parseInt(classCUstring);//convert to integer once validated for addition later
            termCU += classCU;//add current class CUs to term CUs
            totalCU += classCU;//same for total CUs            
            System.out.println("Do you have another class to add?");
            System.out.println("Type \"yes\" or \"y\" for \"Yes\", \"no\" or \"n\" for \"No\" -- not case sensitive.");
            anotherclass = scan.next();
                while (!(anotherclass.substring(0, 1).equalsIgnoreCase("Y"))) {//only count first, case-insensitive letter
                    if (anotherclass.substring(0, 1).equalsIgnoreCase("N")) {//same
                        break outerloop;//escape subsequent loop logic and quit if no more CUs to enter
                    }
                    System.out.println("Not a valid answer. Try again:");
                    anotherclass = scan.next();//retry if invalid input
                }
                if (termCU >= selectedCUperterm) {
                    termbreakdown = new int[] {termnum, classnum, termCU, costperterm};//new static array per term
                    overall.add(termbreakdown);//add static term array to overall dynamic array
                    System.out.println("\nTotal CUs for term " + termnum + ": " + termCU );//give feedback on total CUs for this term before advancing to next term
                    System.out.println("Total classes in term " + termnum + ": " + classnum + "\n");//same, for classes
                    termnum++;//increment term # once CUs per term satisfied
                    termCU = 0;//reset term CU counter to 0 for next term
                    classnum = 0;//same for term class counter
                }
            classnum++;//increment class counter
        }
        termbreakdown = new int[] {termnum, classnum, termCU, costperterm};//final term static array
        overall.add(termbreakdown);//add final term to dynamic array
        int[] totals = {termnum, totalclasses, totalCU, costperterm * termnum};//create static array of overall classes, CUs, and cost
        overall.add(totals);//and add to dynamic array
        System.out.println("\nTotal CUs for term " + termnum + ": " + termCU );//seems like a waste of code to wrap this line and the next in a method - it's only 2 lines duplicated
        System.out.println("Total classes in term " + termnum + ": " + classnum + "\n");
        System.out.println("Terms remaining until GRADUATION: " + termnum + " (" + termnum * monthsperterm + " months remaining)");//give textual feedback of total remaining terms (in months as well)
        System.out.println("Total CUs: " + totalCU + " (in " + totalclasses + " total classes)." );//same for total CUs and classes
        System.out.println("Total remaining tuition cost: $" + costperterm * termnum + "\n");//same for total remaining tuition
        
        /*Display arraylist data for tabular presentation of data*/
        System.out.println("Term         Classes      CUs          Cost");//Headers for arraylist data
        for (int i = 0; i < overall.size() -1; i++) {//loop through per-term arrays
            for (int j = 0; j <= overall.get(i).length - 1; j++) {
                System.out.printf(overall.get(i)[j] + "            ");
            }
            System.out.println();
        }
        System.out.printf("Total        ");//Display overall remaining classes, CUs, and overall tuition cost 
        for (int k = 1; k <= overall.get(overall.size() - 1).length - 1; k++) {
            System.out.printf(overall.get(overall.size() - 1)[k] + "            ");
        }
        System.out.println();
    }
}
