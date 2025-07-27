package com.codigojava.example;

public class Factorial {

    // Method to calculate factorial of a given number
    public static long calculateFactorial(int number) {
        if (number < 0) {
            throw new IllegalArgumentException("Number must be non-negative.");
        }
        long factorial = 1;
        for (int i = 1; i <= number; i++) {
            factorial *= i;
        }
        return factorial;
    }

    // Main method to test the factorial calculation
    public static void main(String[] args) {
        int number = -5; // Example number
        System.out.println("Factorial of " + number + " is: " + calculateFactorial(number));
    }
}
