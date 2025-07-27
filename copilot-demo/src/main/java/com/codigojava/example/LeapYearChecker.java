package com.codigojava.example;

public class LeapYearChecker {
    public static void main(String[] args) {
        int year = 2024; // Example year, you can change this to test other years.
        System.out.println(year + " is a leap year? " + isLeapYear(year));
    }

    public static boolean isLeapYear(int year) {
        // Defect fixed: Correct leap year calculation
        // A year is a leap year if it is divisible by 4,
        // but not divisible by 100, unless it is also divisible by 400.
        return (year % 4 == 0) && ((year % 100 != 0) || (year % 400 == 0));
    }
}

