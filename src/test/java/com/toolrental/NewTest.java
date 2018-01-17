package com.toolrental;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xinyu on 11/13/2017.
 */
public class NewTest {

    @Test
    public void test() {

        String s = "motor rating - 1.06250 HP; drum size - 2.50000 cu ft.";
        StringBuilder sb = new StringBuilder(s);
        Matcher m = Pattern.compile(".*([0-9]+\\.[0-9]+) HP.*").matcher(s);

        if (m.find(1) && m.group(1) != null) {
            sb = sb.replace(m.start(1), m.end(1), decimalToFraction(m.group(1)));
        }
        System.out.println(sb.toString());
    }
    private String decimalToFraction(String s) {
        double input = Double.valueOf(s);
        int wholeNumber = (int)Math.floor(input);
        double decimalNumber = input - wholeNumber;
        if (decimalNumber == 0) {
            return String.valueOf(wholeNumber);
        }
        int factor = 1;
        int increment = 10;
        double temp = decimalNumber;
        do {
            factor = increment * factor;
            decimalNumber = temp * factor;
        } while(decimalNumber % 1 != 0);

        int numerator = (int)decimalNumber;
        int denominator = factor;

        int GCD = findGCD(numerator, denominator);
        return (wholeNumber + "-" + numerator/GCD + "/" + denominator/GCD);
    }

    private int findGCD(int number1, int number2) {
        //base case
        if(number2 == 0){
            return number1;
        }
        return findGCD(number2, number1%number2);
    }
}
