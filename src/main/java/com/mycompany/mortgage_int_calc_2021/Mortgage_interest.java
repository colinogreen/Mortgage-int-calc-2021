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
        System.out.println("hello world!");

        //Finance_apr apr = new Finance_apr(245.21,22800, 3.75);
        Finance_apr apr = new Finance_apr(196.96,23275.71, 1.64);
        
        

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
