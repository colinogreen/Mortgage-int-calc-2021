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
//import my.custom.MessageDisplayer;
/**
 *
 * @author colino20_04
 */
public class Mortgage_interest 
{
        String monthly_repay = "";
        String int_rate = "";
        String mort_remain;           //private MessageDisplayer msgs;
        
    public static void main(String[] args) {
            
        Mortgage_interest mc = new Mortgage_interest();
        mc.checkIfShowhelpCommandLineArguments(args);
        
        System.out.println("==================================");
        System.out.println("   Mortgage Interest calculator   ");
        System.out.println("        By Colin M.               ");
        System.out.println("==================================");
        System.out.println();
        System.out.println("* Follow the prompts below (Enter -q or quit to exit): ");
        System.out.println();

        Scanner line = new Scanner(System.in);
        
        // Most of the engine for this command line script is in https://bitbucket.org/colinogreen/java-custom-classes/src/master/my/custom/finance/Finance_apr.java
        Finance_apr apr = new Finance_apr();
        
        
        
        //mc.debugHashMap(); // Comment out when not in use!
        //mc.debugDateCalendar(); // Comment out when not in use       
        //mc.debugDateTime(); // Comment out when not in use
    
        // Get the values from console input.
        if(args.length == 5)
        {
            // * Attempt to set parameters from command line
            mc.validateArgsEntries(apr, args);
        }
        else
        {
            mc.monthly_repay = mc.getEnterMonthlyRepaymentPrompt(apr, line);
            mc.int_rate = mc.getEnterInterestRatePrompt(apr, line);
            mc.mort_remain = mc.getEnterMortgageRemainingPrompt(apr, line);

            String start_date= mc.getStartOrEndDatePrompt(apr, line, true);

            if(start_date.trim().equals(""))
            {
                apr.setDefaultDateFrom();
                System.out.println("No start date entered. Will use a default date of "+ apr.getCalendarDateFrom());
            }
            String end_date= mc.getStartOrEndDatePrompt(apr, line, false);

            if(end_date.trim().equals(""))
            {
               apr.setDefaultDateTo();
               System.out.println("No end date entered. Will use a default date of " + apr.getCalendarDateTo());
            }                
        }
        // Set the values that were entered in the console
        apr.setMonthRepayment(Double.valueOf(mc.monthly_repay));
        apr.setInterestRate(Double.valueOf(mc.int_rate));
        apr.setMortgageRemaining(Double.valueOf(mc.mort_remain));
        
        //** Run the program
        apr.processMortgateInterestCalculation();
        System.out.println(apr.getMortgageInputSummary());
        mc.waitForNextCommand(apr, line);

    }
        
    private void validateArgsEntries( Finance_apr apr, String[] args)
    {

        if(apr.validateNumberAsDouble("monthly_repayment", args[0], "The monthly repayment entered is invalid (" + args[0].substring(0, Math.min(15, args[0].length())) + ")"))
        {
            this.monthly_repay = args[0];
        }

        if(apr.validateNumberAsDouble("interest_rate", args[1], "The interest rate entered is invalid (" + args[1].substring(0, Math.min(15, args[1].length())) + ")"))
        {
            this.int_rate = args[1];
        } 
        //int_rate = args[1];
        if(apr.validateNumberAsDouble("mortgage_remaining",args[2], "The mortgage amount remaining is invalid (" + args[2].substring(0, Math.min(15, args[2].length())) + ")"))
        {
            this.mort_remain = args[2];
        }             
        //mort_remain = args[2];            
        apr.setCalendarDate(args[3], true); // true start date           
        apr.setCalendarDate(args[4], false); // false for end date 

        this.exitIfErrors(apr); // If supplied arguments are not validated, exit program
    }
    
    private void exitIfErrors(Finance_apr apr)
    {
        if(apr.getErrorListCount() >0)
        {
            for(Object e: apr.getErrorListValuesArray())
            {
                System.out.println((String)e);
            }

            System.exit(0);
        }        
    }
        
    public void waitForNextCommand(Finance_apr apr, Scanner line)
    {
        //System.out.println(apr.getMortgageInputSummary());
        System.out.println("* Enter a command (Enter -h or help): ");
        String command = line.nextLine();

        this.attemptToGetDateInput(apr, line, command);
        // Check for quit and process command if necessary.
        this.checkForQuit(command.trim());
        this.processCommand(command.trim(), apr, line); 

    }
    /**
     * Find individual date or a date range.
     * @param apr
     * @param line
     * @param command 
     */
    private void attemptToGetDateInput(Finance_apr apr, Scanner line, String command)
    {
        String[] date_range = command.trim().split("\\s+");
        //System.out.println("** Debug: date_range -" + Arrays.toString(date_range));
        // if two dates have been entered with the first parameter, -r (range)...
        if(date_range.length == 3 && date_range[0].equals("-r") )
        {          
            apr.getMortgageDayFiguresRangeFromTo(date_range[1], date_range[2]);
            if(apr.getErrorListCount() == 0)
            {
                System.out.println("== Selected range: " + date_range[1] + "- " + date_range[2] + " ==" );
                System.out.println(apr.getMessageString()); // Display the result
                System.out.println(); 

                System.out.println(apr.getMortgageInputSummary());                
            }
            else
            {
                System.out.println(apr.getErrorListMessages());
            }

            this.waitForNextCommand(apr, line) ; 
        }       
         // If a date has been entered, try and retrieve the relevant record for that date
        else if(date_range.length == 2 &&  date_range[0].trim().equals("-d") && apr.isDateEnteredValid(date_range[1]))
        {
//            System.out.print("\n++ Debug apr.isDateEnteredValid(date_range[1]): " + apr.isDateEnteredValid(date_range[1]));
//            System.out.print(" | Debug apr.getErrorListCount: " + apr.getErrorListCount());
//            System.out.println(" | Debug date_range.length: " + date_range.length + " ++\n");            
            apr.setMortgageIndividualDateRecord(date_range[1]);  
            
            if(apr.getErrorListCount() == 0)
            {               
                System.out.println("== Selected date: " + date_range[1] + " ==");
                System.out.println();
                System.out.println(apr.getMessageString()); // Display the result
                
                System.out.println(apr.getMortgageInputSummary());
            }
            else
            {
                System.out.println(apr.getErrorListMessages());
            }
            // Attempt to retrieve a date was made so, loop back.               
            this.waitForNextCommand(apr, line) ;           
        }
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
            this.showSelectedEntries(apr,true);
            break;
            case "-a":
            case "all":
            this.showSelectedEntries(apr,false);
            break;
            case "-m":
            case "milestones":
            this.getMortgageMilestonesListFromClass(apr);
            break;
            default:
            System.out.println("Try again.");
             
        }
        this.waitForNextCommand(apr, line) ; 
    }
    
    private void getMortgageMilestonesListFromClass(Finance_apr apr)
    {
        System.out.println("== Mortgage Milestones ==\n");
        System.out.println(apr.getMortgageMilestonesList());
        System.out.println(apr.getMortgageInputSummary());
    }
    
    private void showSelectedEntries(Finance_apr apr,Boolean show_summary)
    {
        String list_type = show_summary ? "monthly summary": "all";
        System.out.println("== List of " + list_type + " entries ==\n");
        apr.setMortgageSelectedEntries(show_summary);
        System.out.println(apr.getMessageString());
        System.out.println(apr.getMortgageInputSummary());
    }
    /**
     * Show command line arguments if requested by user
     * @param args 
     */
    private void checkIfShowhelpCommandLineArguments(String[] args)
    {
        if(args.length == 1 && (args[0].toLowerCase().equals("-h") || args[0].toLowerCase().equals("--help")) )
        {
            System.out.println();
            System.out.println("Five Arguments accepted at this command line are as follows:");
            System.out.println("[monthly repayment] [interest rate] [mortgage remaining] [date from] [date to]");
            System.out.println("-h or --help:\tView help");

            //System.out.println();
            System.exit(0); 
        }

    }
    
    private void showhelp(Finance_apr apr, Scanner line)
    {

        System.out.println("-a or all: \tView every day of the mortgage search period in this calculation");
        System.out.println("-d yyyy-mm-dd:\tGet record for that day, if it exists");
        System.out.println("-r yyyy-mm-dd yyyy-mm-dd:\tGet range of records for the two dates, if they exist.");
        System.out.println("-h or help:\tView help");
        System.out.println("-m or milestones:\tShow mortgage milestones for this run");
        System.out.println("-q or quit: \tQuit this program");
        System.out.println("-s or summary: \tView a summary of this calculation");

        //System.out.println();
        this.waitForNextCommand(apr, line); 
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
        System.out.println(apr.promptForMonthlyMortgageRepayment());
        String monthly_repay= line.nextLine();
        checkForQuit(monthly_repay);
        boolean num_double = apr.checkIfInputNumberIsADouble(monthly_repay);
        
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
        boolean num_double = apr.checkIfInputNumberIsADouble(int_rate);
        
        if(!num_double)
        {
            System.out.println("Not a valid number");
            return this.getEnterMonthlyRepaymentPrompt(apr, line);
        }
        
        return int_rate;
    }
    
    private String getEnterMortgageRemainingPrompt(Finance_apr apr, Scanner line)
    {
        System.out.println(apr.promptForMortgageRemaining());
        String mort_remain = line.nextLine();
        this.checkForQuit(mort_remain);
        boolean num_double = apr.checkIfInputNumberIsADouble(mort_remain);
        
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
        
        if(!start_or_end_date.equals("") && !apr.isDateEnteredValid(start_or_end_date))
        {
            // Re-run, as there was an attempt to enter a date but it failed SimpleDateFormat validity check
            System.out.println("* The date (" + start_or_end_date + ") " + "appears to be invalid!");
            return this.getStartOrEndDatePrompt(apr, line, start_or_end);
        }
        
        if(!start_or_end_date.trim().equals(""))
        {
            if(!apr.setCalendarDate(start_or_end_date.trim(), start_or_end))
            {
                System.out.println("* The date (" + start_or_end_date + ") could not be set!");
                return this.getStartOrEndDatePrompt(apr, line, start_or_end);
            }
            if(start_or_end == false )
            {
                boolean dategreaterthan = apr.isDateToGreaterThanDateFrom();
                this.isDateToGreaterThanDateFromMessage(dategreaterthan, apr, line, start_or_end);                 
            }
     
        }
//        else if(!start_or_end_date.trim().equals(""))
//        {
//            apr.setCalendarDate(start_or_end_date.trim(), start_or_end);
//        }
        
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
