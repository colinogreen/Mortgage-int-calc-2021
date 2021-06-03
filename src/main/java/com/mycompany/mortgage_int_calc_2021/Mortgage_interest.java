/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mortgage_int_calc_2021;
import java.util.Date;
import java.util.Calendar;
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
        String monthly_repay = mc.getMonthlyRepaymentPrompt(apr, line);
        
        System.out.println(apr.promptForDateOfCalculations(false));
        String end_date= line.nextLine();

        //apr.setMonthRepayment(Integer.parseInt(monthly_repay));
        apr.setMonthRepayment(Double.valueOf(monthly_repay));
        apr.setInterestRate(1.64);
        apr.setMortgageRemaining(23275.71);
        
        //** Run the program
        apr.processMortgateInterestCalculation();


    }
        
    private String getMonthlyRepaymentPrompt(Finance_apr apr, Scanner line)
    {
        System.out.println(apr.promptForMonthlyRepayment());
        String monthly_repay= line.nextLine();
        boolean num_double = apr.checkIfNumberIsADouble(monthly_repay);
        
        if(!num_double)
        {
            System.out.println("Not a valid number");
            return this.getMonthlyRepaymentPrompt(apr, line);
        }
        
        return monthly_repay;
    }
    
    public void run(String[] args)
    {
//        apr = new Finance_apr();
//        
//        apr.setMortgageRemaining(23233.82);
//        apr.setInterestRate(1.64);
//        apr.anotherHello();
    }    
}
