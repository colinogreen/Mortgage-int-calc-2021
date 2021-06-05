/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mortgage_int_calc_2021;
import java.util.*;
//import java.util.Calendar;
import java.time.*;
import java.time.Month;
import java.time.Period;
import my.custom.finance.*;

import java.util.Scanner;

//import java.text.ParseException;
import java.text.*;

/**
 *
 * @author colino20_04
 */
public class Mortgage_interest 
{
    
        public static void main(String[] args) {
        
        Scanner line = new Scanner(System.in);
        
        // Most of the engine for this command line script is in https://bitbucket.org/colinogreen/java-custom-classes/src/master/my/custom/finance/Finance_apr.java
        Finance_apr apr = new Finance_apr();
        
        Mortgage_interest mc = new Mortgage_interest();
        
        //mc.debugHashMap(); // Comment out when not in use!
        //mc.debugDateCalendar(); // Comment out when not in use       
        //mc.debugDateTime(); // Comment out when not in use
        
        // Get the values from console input.
        String monthly_repay = mc.getEnterMonthlyRepaymentPrompt(apr, line);
        String int_rate = mc.getEnterInterestRatePrompt(apr, line);
        String mort_remain = mc.getEnterMortgageRemainingPrompt(apr, line);

        String start_date= mc.getStartOrEndDatePrompt(apr, line, true);
               
        if(start_date.trim().equals(""))
        {
            apr.setDefaultDateFrom();
            System.out.println("No start date entered. Will use a default date of "+ apr.getDefaultDateFrom());
        }
        String end_date= mc.getStartOrEndDatePrompt(apr, line, false);
        
        if(end_date.trim().equals(""))
        {
           apr.setDefaultDateTo();
           System.out.println("No end date entered. Will use a default date of " + apr.getDefaultDateTo());
        }        
        

        // Set the values that were entered in the console
        apr.setMonthRepayment(Double.valueOf(monthly_repay));
        apr.setInterestRate(Double.valueOf(int_rate));
        apr.setMortgageRemaining(Double.valueOf(mort_remain));
        apr.setDateToCalculateFrom(start_date);
        apr.setDateToCalculateTo(end_date);
        
        //** Run the program
        apr.processMortgateInterestCalculation();
        
        mc.promptForNextCommand(apr, line);

    }
        
    public void promptForNextCommand(Finance_apr apr, Scanner line)
    {
        System.out.println();
        System.out.println("* Enter a command (Enter -h or help): ");
        String command = line.nextLine();
        this.checkForQuit(command);
        this.processCommand(command, apr, line);
    }
    
    private void processCommand(String command, Finance_apr apr, Scanner line)
    {
        switch (command.toLowerCase())
        {
            
            case "-h":
            case "help":
            this.showhelp(apr, line);
            break;
            case "-s":
            case "summary":
            apr.showSummary(true);
            //this.promptForNextCommand(apr, line);
            break;
            default:
            System.out.println("Try again.");
            //System.out.println();
             
        }
        this.promptForNextCommand(apr, line) ; 
    }
    
    private void showhelp(Finance_apr apr, Scanner line)
    {
        System.out.println("-h or help:\tView help");
        System.out.println("-q or quit: \tQuit this program");
        System.out.println("-s or summary: \tView a summary of this calculation");
        //System.out.println();
        this.promptForNextCommand(apr, line); 
    }
        
    public void checkForQuit(String keyboard_input)
    {
        if(keyboard_input.toLowerCase().equals("quit")|| keyboard_input.toLowerCase().equals("-q"))
        {
            System.out.println("\n== Quit program: " + this.checkForQuitMessage() + " ==");
            System.exit(0);
        }
    }
    
    private String checkForQuitMessage()
    {
        String[] potential_messages = new String[]{"Bye, bye!", "Goodbye!", "Thanks for using my program!", "Visit again soon!"};

        return potential_messages[new Random().nextInt(3)];
    }
        
    private String getEnterMonthlyRepaymentPrompt(Finance_apr apr, Scanner line)
    {
        System.out.println(apr.promptForMonthlyRepayment());
        String monthly_repay= line.nextLine();
        checkForQuit(monthly_repay);
        boolean num_double = apr.checkIfNumberIsADouble(monthly_repay);
        
        if(!num_double)
        {
            System.out.println("Not a valid number");
            return this.getEnterMonthlyRepaymentPrompt(apr, line);
        }
        
        return monthly_repay;
    }
    
    private String getEnterInterestRatePrompt(Finance_apr apr, Scanner line)
    {
        System.out.println(apr.promptForInterestRate());
        String int_rate = line.nextLine();
        this.checkForQuit(int_rate);
        boolean num_double = apr.checkIfNumberIsADouble(int_rate);
        
        if(!num_double)
        {
            System.out.println("Not a valid number");
            return this.getEnterMonthlyRepaymentPrompt(apr, line);
        }
        
        return int_rate;
    }
    
    private String getEnterMortgageRemainingPrompt(Finance_apr apr, Scanner line)
    {
        System.out.println(apr.promptForMortgateRemaining());
        String mort_remain = line.nextLine();
        this.checkForQuit(mort_remain);
        boolean num_double = apr.checkIfNumberIsADouble(mort_remain);
        
        if(!num_double)
        {
            System.out.println("Not a valid number");
            return this.getEnterMonthlyRepaymentPrompt(apr, line);
        }
        
        return mort_remain;
    }
    /**
     * 
     * @param apr
     * @param line
     * @param start_or_end (true for start | false for end)
     * @return 
     */
    private String getStartOrEndDatePrompt(Finance_apr apr, Scanner line, boolean start_or_end)
    {
        System.out.println(apr.promptForDateOfCalculations(start_or_end, true));
        String start_or_end_date = line.nextLine();
        this.checkForQuit(start_or_end_date);
        
        if(start_or_end == false &&!start_or_end_date.trim().equals(""))
        {
            apr.setCalendarDate(start_or_end_date.trim(), start_or_end);
            boolean dategreaterthan = apr.isDateToGreaterThanDateFrom();
            this.isDateToGreaterThanDateFromMessage(dategreaterthan, apr, line, start_or_end);      
        }
        
        /* @todo
        Check that date is valid
        */
            
        return start_or_end_date;
    }
    
    
    private String isDateToGreaterThanDateFromMessage(boolean dategreaterthan,Finance_apr apr, Scanner line, boolean start_or_end )
    {
        if(dategreaterthan == false)
        {
            System.out.println("The date to cannot be smaller the date from");
            this.getStartOrEndDatePrompt(apr, line, start_or_end);
        }
        
        return "";
    }
    
//    public void run(String[] args)
//    {
////        apr = new Finance_apr();
////        
////        apr.setMortgageRemaining(23233.82);
////        apr.setInterestRate(1.64);
////        apr.anotherHello();
//    } 
    
    /**
     * Testing Calender.set from input. Delete when not required.
     */    
    private void debugDateCalendar()
    {
        String debug_date = "2021-06-04";
        //** spilt debug date into format accepted by Calendar.set method | e.g: Calendar.set(2022, 2,31)
        
        //Array date_set = new Array(debug_date.split("-").;
        String date_set[] =debug_date.split("-");
        System.out.println("== Debug date split ==");
        System.out.println(Arrays.toString(date_set)); // Arrays is part of java.util.*
        System.out.println("== For Calendar.set(): ==");
        System.out.println(date_set[0] + "," + (Integer.valueOf(date_set[1]) -1) + "," + date_set[0]);
        System.exit(0); // Exit with zero, as if this is normal!
        
    }
    /**
     * Testing LocalDate/Time. Delete when not required
     */
    private void debugDateTime()
    {
        //LocalTime current_time = LocalTime.now();
        LocalDate current_date = LocalDate.now();
        LocalDate future_date =  LocalDate.parse("2022-04-01");
        //LocalDate future_date =  LocalDate.of(2021, 7, 14);

        
        //System.out.println("Current time: " + current_time.toString());
        System.out.println("Current date: " + current_date.toString());
        System.out.println("future date: " + future_date.toString());
        double period = Duration.between(current_date.atStartOfDay(), future_date.atStartOfDay()).toDays();

        System.out.println("Current date - Future date: " + period);
        //System.out.println("Future time: " + future_time.toString());
        System.exit(0);
    }
    
    private void debugHashMap()
    {
        
        System.out.println("== Debugging method, debugHash ==");
    HashMap<String, String> capitalCities = new HashMap<String, String>();

    // Add keys and values (Country, City)
    capitalCities.put("England", "London");
    capitalCities.put("Germany", "Berlin");
    capitalCities.put("Norway", "Oslo");
    capitalCities.put("USA", "Washington DC");
    System.out.println(capitalCities);        
        
        System.exit(0);
    }
}
