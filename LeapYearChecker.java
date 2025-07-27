package com.bharath.copilot;

public class LeapYearChecker {
    public static void main(String[] args) {
        int year = 2024; // Example year, you can change this to test other years.
        System.out.println(year + " is a leap year? " + isLeapYear(year));
    }

    public static boolean isLeapYear(int year) {
        // Defect: Incorrect leap year calculation
        // A year is a leap year if it is divisible by 4.
        // However, years divisible by 100 are not leap years,
        // except years that are divisible by 400.
        if (year % 4 == 0) {
            return true;
        } else {
            return false;
        }
    }
}

