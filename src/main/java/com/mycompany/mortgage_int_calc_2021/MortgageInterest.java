
package com.mycompany.mortgage_int_calc_2021;
import java.util.*;
//import java.util.Arrays;
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
 * @author Colin M.
 * This class uses the following external classes:
 * https://github.com/colinogreen/java-custom-classes/blob/master/my/custom/finance/MortgageCalculator.java
 * https://github.com/colinogreen/java-custom-classes/blob/master/my/custom/finance/FinanceApr.java
 */
public class MortgageInterest 
{
    String monthly_repay = "";
    String int_rate = "";
    String mort_remain;
        
    public static void main(String[] args) {
            
        MortgageInterest mi = new MortgageInterest();
        mi.checkIfShowhelpCommandLineArguments(args);
        
        System.out.println("==================================");
        System.out.println("   Mortgage Interest calculator   ");
        System.out.println("        By Colin M.               ");
        System.out.println("==================================");
        System.out.println();
        System.out.println("* Follow the prompts below (Enter -q or quit to exit): ");
        System.out.println();

        Scanner line = new Scanner(System.in);
        
        // Most of the engine for this command line script is in https://bitbucket.org/colinogreen/java-custom-classes/src/master/my/custom/finance/Mortgage_calc.java
        MortgageCalculator mcalc = new MortgageCalculator();
 
        //mc.debugHashMap(); // Comment out when not in use!
        //mc.debugDateCalendar(); // Comment out when not in use       
        //mc.debugDateTime(); // Comment out when not in use
    
        // Get the values from console input.
        if(args.length > 2 && args.length <6)
        {
            // * Attempt to set parameters from command line
            mi.validateArgsEntries(mcalc, args);
        }
        else
        {
            //mi.monthly_repay = mc.showEnterMonthlyRepaymentPrompt(apr, line);
            mi.showEnterMonthlyRepaymentPrompt(mcalc, line);
            mi.showEnterInterestRatePrompt(mcalc, line);
            mi.showEnterMortgageRemainingPrompt(mcalc, line);

            mi.showStartOrEndDateFromPrompt(mcalc, line,null, true);
            mi.showStartOrEndDateFromPrompt(mcalc, line,mcalc.getCalendarDateFrom(), false);

        }
        
        //** Run the program
        mi.runMortgageInterestCalculations(mcalc, line);
        

    }
    private void runMortgageInterestCalculations(MortgageCalculator mcalc,Scanner line)
    {
        mcalc.processMortgateInterestCalculation();
        System.out.println(mcalc.getMortgageInputSummary());
        mcalc.setInitialMortgagePaymentAndInterestTotals();
        this.waitForNextCommand(mcalc, line);        
    } 
    
    private void runMortgageInterestCalculations(MortgageCalculator mcalc,Scanner line, boolean reset_variables)
    {
        mcalc.processMortgateInterestCalculation(reset_variables);
        System.out.println(mcalc.getMortgageInputSummary());
        this.waitForNextCommand(mcalc, line);        
    }  
    private void validateArgsEntries( MortgageCalculator mcalc, String[] args)
    {
        mcalc.setMonthlyRepaymentAmount(args[0].trim(), mcalc.MAX_MONTHLY_REPAYMENT, 1, "monthly_repayment", "Monthly repayment");

        mcalc.setMonthlyInterestAmount(args[1].trim(), mcalc.MAX_MORTGAGE_INT_RATE, 0.1, "monthly_interest", "Monthly Interest rate");

        mcalc.setMortgageRemainingAmount(args[2].trim(), mcalc.MAX_MORTGAGE_LOAN, 1000, "mortgage_remaining", "Mortgage loan remaining");
        //System.out.println("Error list count - setMortgageRemainingAmount: " + apr.getErrorListCount());
        String start_date = (args.length > 3)? args[3]: "";
        String end_date = (args.length > 4)? args[4]: "";
        this.setStartOrEndDate(mcalc, start_date, null, true);

        this.setStartOrEndDate(mcalc, end_date, mcalc.getCalendarDateFrom(), false);
        
        this.exitIfErrors(mcalc);

    }
    
    private void exitIfErrors(MortgageCalculator mcalc)
    {
        if(mcalc.getErrorListCount() >0)
        {
            for(Object e: mcalc.getErrorListValuesArray())
            {
                
                System.out.println((String)e);
            }
            System.out.println("\n== Exiting due to " + mcalc.getErrorListCount() + " error(s) ==");
            System.exit(0);
        }        
    }
        
    public void waitForNextCommand(MortgageCalculator mcalc, Scanner line)
    {
        System.out.println("* Enter a command (Enter -h or help): ");
        String command = line.nextLine();

        this.attemptToGetDateInput(mcalc, line, command);
        this.attemptToGetOverpaymentInput(mcalc, line, command);
        // Check for quit and process command if necessary.
        this.checkForQuit(command.trim());
        this.processCommand(command.trim(), mcalc, line); 

    }
    
    private void attemptToGetOverpaymentInput(MortgageCalculator mcalc, Scanner line, String command)
    {
        mcalc.resetErrorList();
        if(command.contains("-ol") || command.contains("-od"))
        {
            //System.out.println("Command entered:" + Arrays.toString(command.split(" ")));
            this.overpaymentInputListorDelete(command.split(" "), mcalc, line, command);
            
            if(mcalc.getErrorListCount() > 0)
            {
                System.out.println(mcalc.getErrorListMessages(true));
            }
            this.waitForNextCommand(mcalc, line);           
        }
        
        else if(command.contains("-o"))
        {
            System.out.println("== Overpayments: Addition attempt ==");
            if(!overpaymentInputValidateAndProcess(command.split(" "), mcalc, line, command))
            {
                System.out.println(mcalc.getErrorListMessages(true));
            }

            this.waitForNextCommand(mcalc, line);             
        }
        
        // Or fall through to the next command reading user input.
    }
    
    private void overpaymentInputListorDelete(String[] overpay_args,MortgageCalculator mcalc, Scanner line, String command)
    {
        if(overpay_args.length == 1 && overpay_args[0].equals("-ol") )
        {
            System.out.println("== Overpayments: List ==");
            System.out.println(mcalc.listMortgageOverpayments()+ "\n");
        }
        
        if(overpay_args.length == 2 && overpay_args[0].equals("-od"))
        {
            System.out.println("== Overpayments: Edit ==");
            
            if(!mcalc.isLocalDateValid("overpay_date",overpay_args[1], true))
            {
                System.out.println(mcalc.getErrorListMessages(true));
            }
            else
            if(mcalc.isMortgageOverpaymentEntryExists(overpay_args[1]) )
            {
                
                mcalc.removeMortgageOverpaymentEntry(overpay_args[1]);
                System.out.println("Removing overpayment value for the date, " + overpay_args[1] +"\n");
            }
            else
            {
                System.out.println("Could not find overpayment entry for the date, " + mcalc.truncateLongString(overpay_args[1]) +"\n");
            }

        }
    }
    
    private boolean overpaymentInputValidateAndProcess(String[] overpay_args,MortgageCalculator mcalc, Scanner line, String command)
    {
        if(overpay_args.length != 3)
        {
            mcalc.setErrorListItem("arguments_length", "Not enough parameters for overpayment command", true);
            return false;
        }
        
        if(mcalc.overpaymentInputValidateAndProcess(overpay_args[1], overpay_args[2]))
        {
            mcalc.addMortgageOverpayment(overpay_args[1], Double.valueOf(overpay_args[2]));
            System.out.println(mcalc.getMessageString());
            return true;      
        }
        
        return false; // ...if mcalc.addMortgageOverpayment is not processed, as expected.


    }
    /**
     * Find individual date or a date range.
     * @param mcalc
     * @param line
     * @param command 
     */
    private void attemptToGetDateInput(MortgageCalculator mcalc, Scanner line, String command)
    {
        String[] date_range = command.trim().split("\\s+");
        //System.out.println("** Debug: date_range -" + Arrays.toString(date_range));
        // if two dates have been entered with the first parameter, -r (range)...
        if(date_range.length == 3 && date_range[0].equals("-r") )
        {          
            mcalc.getMortgageDayFiguresRangeFromTo(date_range[1], date_range[2]);
            if(mcalc.getErrorListCount() == 0)
            {
                System.out.println("== Selected range: " + date_range[1] + "- " + date_range[2] + " ==" );
                System.out.println(mcalc.getMessageString()); // Display the result
                System.out.println(); 

                System.out.println(mcalc.getMortgageInputSummary());                
            }
            else
            {
                System.out.println(mcalc.getErrorListMessages(true));
            }

            this.waitForNextCommand(mcalc, line) ; 
        }       
         // If a date has been entered, try and retrieve the relevant record for that date
        else if(date_range.length == 2 &&  date_range[0].trim().equals("-d") && mcalc.isDateEnteredValid(date_range[1]))
        {
           
            mcalc.setMortgageIndividualDateRecord(date_range[1]);  
            
            if(mcalc.getErrorListCount() == 0)
            {               
                System.out.println("== Selected date: " + date_range[1] + " ==");
                System.out.println();
                System.out.println(mcalc.getMessageString()); // Display the result
                
                System.out.println(mcalc.getMortgageInputSummary());
            }
            else
            {
                System.out.println(mcalc.getErrorListMessages(true));
            }
            // Attempt to retrieve a date was made so, loop back.               
            this.waitForNextCommand(mcalc, line);           
        }
        
        // Or fall through to the next command attempting to read console input.
    }
    /**
     * Process commands following first run of the calculations
     * @param command
     * @param mcalc
     * @param line 
     */
    private void processCommand(String command, MortgageCalculator mcalc, Scanner line)
    {
        switch (command.toLowerCase())
        {
            
            case "-h":
            case "help":
            this.showhelp(mcalc, line);
            break;
            case "-s":
            case "summary":
            this.showSelectedEntries(mcalc,true);
            break;
            case "-a":
            case "all":
            this.showSelectedEntries(mcalc,false);
            break;
            case "-m":
            case "milestones":
            this.getMortgageMilestonesListFromClass(mcalc);
            break;
            case "-r":
            case "rerun":
            this.runMortgageInterestCalculations(mcalc,line, true);
            break;
            default:
            System.out.println("Try again.");
             
        }
        this.waitForNextCommand(mcalc, line) ; 
    }
    
    private void getMortgageMilestonesListFromClass(MortgageCalculator mcalc)
    {
        System.out.println("== Mortgage Milestones ==\n");
        System.out.println(mcalc.getMortgageMilestonesList());
        if(mcalc.getMortgageMilestonesCount() >0)
        {
            System.out.println(mcalc.getMortgageInputSummary());
        }       
    }
    
    private void showSelectedEntries(MortgageCalculator mcalc,Boolean show_summary)
    {
        String list_type = show_summary ? "monthly summary": "all";
        System.out.println("== List of " + list_type + " entries ==\n");
        mcalc.setMortgageSelectedEntries(show_summary);
        System.out.println(mcalc.getMessageString());
        System.out.println(mcalc.getMortgageInputSummary());
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
    
    private void showhelp(MortgageCalculator mcalc, Scanner line)
    {
        System.out.println("-a, all: \tView every day of the mortgage search period in this calculation");
        System.out.println("-d yyyy-mm-dd:\tGet record for that day, if it exists");
        System.out.println("-r yyyy-mm-dd yyyy-mm-dd:\tGet range of records for the two dates, if they exist.");
        System.out.println("-h, help:\tView help");
        System.out.println("-m, milestones:\tShow mortgage milestones for this run");
        System.out.println("-o yyyy-mm-dd 00.00:\tAdd overpayment date and amount before re-run");
        System.out.println("-q, quit: \tQuit this program");
        System.out.println("-r:\tRe-run this program");
        System.out.println("-s, summary: \tView a summary of this calculation");

        //System.out.println();
        this.waitForNextCommand(mcalc, line); 
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
        
    private void showEnterMonthlyRepaymentPrompt(MortgageCalculator mcalc, Scanner line)
    {
        System.out.println(mcalc.promptForMonthlyMortgageRepayment());
        String monthly_repayment= line.nextLine();
        checkForQuit(monthly_repayment);
        //oolean num_double = mcalc.checkIfInputNumberIsADouble(monthly_repayment);
        // setMonthlyRepaymentAmount(String amount, double max_num, double min_num, String field_name, String field_label)
        if(!mcalc.setMonthlyRepaymentAmount(monthly_repayment.trim(), mcalc.MAX_MONTHLY_REPAYMENT, 1, "monthly_repayment", "Monthly repayment"))
        {
            System.out.println(mcalc.getErrorListMessages(true));
            this.showEnterMonthlyRepaymentPrompt(mcalc, line);
        }
        
        //this.monthly_repay = monthly_repayment.trim();
        
    }
    
    private void showEnterInterestRatePrompt(MortgageCalculator mcalc, Scanner line)
    {
        System.out.println(mcalc.promptForInterestRate());
        String interest_rate = line.nextLine();
        this.checkForQuit(interest_rate);

        if(!mcalc.setMonthlyInterestAmount(interest_rate.trim(),mcalc.MAX_MORTGAGE_INT_RATE, 0.1, "interest_rate", "mortgage interest rate"))
        {
            System.out.println(mcalc.getErrorListMessages(true));
            this.showEnterInterestRatePrompt(mcalc, line);
        }        
       
        //return interest_rate;
    }
    
    private void showEnterMortgageRemainingPrompt(MortgageCalculator mcalc, Scanner line)
    {
        System.out.println(mcalc.promptForMortgageRemaining());
        String mort_remaining = line.nextLine();
        this.checkForQuit(mort_remaining);
        
        //boolean num_double = mcalc.checkIfInputNumberIsADouble(mort_remaining);

        if(!mcalc.setMortgageRemainingAmount(mort_remaining.trim(),Double.valueOf(mcalc.MAX_MORTGAGE_LOAN), 1000, "mort_remaining", "mortgage loan amount"))
        {
            System.out.println(mcalc.getErrorListMessages(true));
            this.showEnterMortgageRemainingPrompt(mcalc, line);
        }             
         
        //return mort_remaining;
    }
    /**
     * @deprecated 
     * @param mcalc
     * @param input_number
     * @param max_num
     * @param min_num
     * @param field_name
     * @param field_label
     * @return 
     */
    private boolean isNumberInputValid(MortgageCalculator mcalc,String input_number,double max_num, double min_num, String field_name, String field_label)
    {
        boolean num_double = mcalc.checkIfInputNumberIsADouble(input_number);
        
        if(!num_double)
        {
            System.out.println("Not a valid number");
            return false;
        }
        
        boolean too_large = mcalc.checkIfInputNumberTooLarge(input_number, max_num, field_name, field_label);
        if(too_large)
        {
            System.out.println(mcalc.getErrorListMessages());
            return false;           
        }
        boolean too_small = mcalc.checkIfInputNumberTooSmall(input_number, min_num, field_name, field_label);
        if(too_small)
        {
            System.out.println(mcalc.getErrorListMessages());
            return false;
        }       
        return true;
    }
    private void showStartOrEndDateFromPrompt(MortgageCalculator mcalc, Scanner line,String start_date, boolean start_or_end)
    {
        System.out.println(mcalc.promptForDateOfCalculations(start_or_end, true));
        String start_or_end_date = line.nextLine(); 
        mcalc.resetMessageString();
        if(!this.setStartOrEndDate(mcalc, start_or_end_date, start_date, start_or_end))
        {
            System.out.println(mcalc.getErrorListMessages(true));
            this.showStartOrEndDateFromPrompt(mcalc,line,start_date, start_or_end);
        }
    }
    /**
     * @param mcalc
     * @param line
     * @param start_date
     * @param start_or_end
     * @return 
     */
    //private String setStartOrEndDate(MortgageCalculator mcalc, Scanner line,String start_date, boolean start_or_end)
    private boolean setStartOrEndDate(MortgageCalculator mcalc, String date_input,String start_date, boolean processing_start_date)
    {
        String date_label = (processing_start_date)? "start date": "end date";
//        System.out.println("**DEBUG evaluating getStartOrEndDatePrompt overload: start_date: " + start_date + " - mcalc.MAX_MORTGAGE_TERM: " 
//                + mcalc.MAX_MORTGAGE_TERM + " start_or_end_date = "+ start_or_end_date);
        this.checkForQuit(date_input);
        
        if(date_input.trim().isEmpty()&& processing_start_date)
        {
            //* Set default start date
            mcalc.resetMessageString();
            mcalc.setDefaultDateFrom(); // * Set start date
            System.out.println(mcalc.getMessageString());
            return true;
        } 
        else if(date_input.trim().isEmpty()&& !processing_start_date)
        {
            //* Set default end date
            mcalc.resetMessageString();
            mcalc.setDefaultDateTo(); //* Set end date
            System.out.println(mcalc.getMessageString());
            return true;
        } 
            
        //* Check for invalid input
        if( !mcalc.isDateEnteredValid(date_input))
        {
            // Re-run, as there was an attempt to enter a date but it failed SimpleDateFormat validity check
            mcalc.setErrorListItem(date_label, "The " + date_label+ " (" + date_input + ") " + "appears to be invalid!");
            return false;
        }

        if(processing_start_date == false )
        {
            //* 1a. check the end date to see if it is 40 years or more past the start date. If so, return to prompt after displaying error.
            if(start_date != null && (!mcalc.isLocalDateValid(date_input) || !mcalc.isLocalDateValid(start_date) || mcalc.isDateDifferenceGreaterThanLimit(start_date, date_input, "end date", mcalc.MAX_MORTGAGE_TERM)) )
            {
                return false;
            }

        }
        //* 2. Check that the console input is not an invalid date
        if(!mcalc.setCalendarDate(date_input.trim(), processing_start_date))
        {
            //System.out.println("The date (" + date_input + ") could not be set!");
            return false;
        }
        //* 3. Check that end_date/date_to is greater than start_date.
        else if(processing_start_date == false )
        {
            if(!mcalc.isDateToGreaterThanDateFrom())
            {
                mcalc.setErrorListItem(date_label,"The end date cannot be smaller the start date");
                return false;                   
            }               
        }
           
        return true;
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
