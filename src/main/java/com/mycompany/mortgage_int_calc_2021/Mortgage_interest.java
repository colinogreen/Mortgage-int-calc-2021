/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mortgage_int_calc_2021;
import java.util.Date;
import java.util.Calendar;
import java.time.LocalDate;
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

        Finance_apr apr = new Finance_apr();
        
        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();
        
        //date1.set();
        date2.set(2022, 2,31);

        //SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");        
        SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");        
        String date1_string = ft.format(date1.getTime());
        String date2_string = ft.format(date2.getTime());

        long diff = date2.getTimeInMillis() - date1.getTimeInMillis();

        float dayCount = (float) diff / (24 * 60 * 60 * 1000);
        
        System.out.println("== Calculating the days between " + date1_string + " and " + date2_string + " ==");
        System.out.println("== dayCount = " + dayCount);
        //Initializing the date formatter
        DateFormat Date = DateFormat.getDateInstance();
        //Initializing Calender Object
        Calendar cals = Calendar.getInstance();
        //Displaying the actual date
        System.out.println("The original Date: " + cals.getTime());
        //Using format() method for conversion
        String currentDate = Date.format(cals.getTime());
        System.out.println("Formatted Date: " + currentDate);
        
        System.out.println("== Lets do a Local date addition ==");
        SimpleDateFormat today_date = new SimpleDateFormat ("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(today_date.format(date1.getTime()));
        //LocalDate date = LocalDate.parse("2021-02-24");
        
        // Mortgate rate calc debug figures | START ...//
        // **30/5/2021 Mortgage remain = 23274.67   curr rate:1.64% repayment: 196.96 Redemption: March 2033
        double mort_remain = 23274.67;
        double mort_remain_new;
        double apr_int_rate = 1.64;
        double day_int_rate = (apr_int_rate / 365);
        double day_int_charge;
        double month_repayment = 196.96;
        //... Mortgate rate calc debug figures | END //
        //add n days
        
        Float f = dayCount;
        int days_count = f.intValue();
        //int days_count = java.;
        LocalDate date_add = date.plusDays(days_count);
        LocalDate date_add_single;
        for(int i = 0; i <= days_count; i++)
        {
            date_add_single = date.plusDays(i);
            if(i != 0 && date_add_single.getDayOfMonth() == 1)
            {
                mort_remain -= month_repayment; // deduct monthly mortgage repayment if it is the 1st of a month and not the first run of the loop (which may take into account first day, anyway.
            }
            day_int_charge = (day_int_rate * mort_remain / 100);
            
            //System.out.println(i +") On date "+date+" plus " + i + " days is "+date_add_single);
            //System.out.println(i +") On date "+ date_add_single +" the mortgate remaining is " + String.format("%.2f",mort_remain) + " and for apr: " + apr_int_rate + ", the daily interest charge is "+ String.format("%.2f",day_int_charge) + "\nThe day of the month is " + date_add_single.getDayOfMonth());
            System.out.println(i +") On date "+ date_add_single +" the mortgate remaining is " + String.format("%.2f",mort_remain) + " and for apr: " + apr_int_rate + ", the daily interest charge is "+ String.format("%.2f",day_int_charge));
            
            mort_remain += day_int_charge;
            
            
            
            //mort_remain = 23274.67;
        }
        
        System.out.println("== Final amount of days: ==");
        System.out.println("Date "+date+" plus " + days_count + " days is "+date_add);

        Mortgage_interest mc = new Mortgage_interest();
        mc.run(args);

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
