package io.cyb3rh4ck.github.copilot;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Factorial {

    public static void main(String[] args) {
        int number = 5; // Example number
        try {
            long result = calculateFactorial(number);
            log.info("Factorial of {} is: {}", number, result);
        } catch (IllegalArgumentException e) {
            log.error("Error calculating factorial: {}", e.getMessage());
        }
    }

    // Method to calculate factorial of a number
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
    
}
