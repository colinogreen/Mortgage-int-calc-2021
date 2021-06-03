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
        System.out.println(apr.promptForMonthlyRepayment());
        String monthly_repay= line.nextLine();

        System.out.println(apr.promptForDateOfCalculations(false));
        String end_date= line.nextLine();
        
        apr.setMonthRepayment(Integer.parseInt(monthly_repay));
        apr.setInterestRate(1.64);
        apr.setMortgageRemaining(23275.71);
        
        //** Run the program
        apr.processMortgateInterestCalculation();
        //Finance_apr apr = new Finance_apr(245.21,22800, 3.75);
        
        
        

        //Mortgage_interest mc = new Mortgage_interest();
        //mc.run(args);

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
