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
        // TODO code application logic here
        //System.out.println("hello world!");
        
        
        Scanner line = new Scanner(System.in);
        
        Finance_apr apr = new Finance_apr();
        
        Mortgage_interest mc = new Mortgage_interest();
        //mc.debugDateCalendar(); // Comment out when not in use
        
        //mc.debugDateTime(); // Comment out when not in use
        
        // Get the values from console input.
        String monthly_repay = mc.getEnterMonthlyRepaymentPrompt(apr, line);
        String int_rate = mc.getEnterInterestRatePrompt(apr, line);
        String mort_remain = mc.getEnterMortgageRemainingPrompt(apr, line);
        
        //System.out.println(apr.promptForDateOfCalculations(false));
        String start_date= mc.getStartOrEndDatePrompt(apr, line, true);
        
        
        if(start_date.trim().equals(""))
        {
           System.out.println("No start date entered. Will use a default date");
        }
        String end_date= mc.getStartOrEndDatePrompt(apr, line, false);
        
        if(end_date.trim().equals(""))
        {
           System.out.println("No end date entered. Will use a default date");
        }        
        

        // Set the values that were entered in the console
        apr.setMonthRepayment(Double.valueOf(monthly_repay));
        apr.setInterestRate(Double.valueOf(int_rate));
        apr.setMortgageRemaining(Double.valueOf(mort_remain));
        apr.setDateToCalculateFrom(start_date);
        apr.setDateToCalculateTo(end_date);
        
        //** Run the program
        apr.processMortgateInterestCalculation();


    }
        
    private String getEnterMonthlyRepaymentPrompt(Finance_apr apr, Scanner line)
    {
        System.out.println(apr.promptForMonthlyRepayment());
        String monthly_repay= line.nextLine();
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
        boolean num_double = apr.checkIfNumberIsADouble(mort_remain);
        
        if(!num_double)
        {
            System.out.println("Not a valid number");
            return this.getEnterMonthlyRepaymentPrompt(apr, line);
        }
        
        return mort_remain;
    }
    
    private String getStartOrEndDatePrompt(Finance_apr apr, Scanner line, boolean start_or_end)
    {
        System.out.println(apr.promptForDateOfCalculations(start_or_end, true));
        String start_or_end_date = line.nextLine();
        
        /* @todo
        Check that date is valid
        */
            
        return start_or_end_date;
    }
    
    public void run(String[] args)
    {
//        apr = new Finance_apr();
//        
//        apr.setMortgageRemaining(23233.82);
//        apr.setInterestRate(1.64);
//        apr.anotherHello();
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
}
