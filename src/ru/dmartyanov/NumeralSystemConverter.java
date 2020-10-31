package ru.dmartyanov;

import java.util.Scanner;
import ru.dmartyanov.utils.*;

public class NumeralSystemConverter {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String input = "";
        Integer currentBase = null;
        Integer targetBase = null;
        String value = null;

        input = inputValue(in);
        if (isNumber(input) && checkRadix(input)) {
            currentBase = Integer.parseInt(input);
        } else {
            System.out.println("error");
            return;
        }

        input = inputValue(in);
        if (isHex(input)) {
            value = input;
        } else {
            System.out.println("error");
            return;
        }

        input = inputValue(in);
        if (isNumber(input) && checkRadix(input)) {
            targetBase = Integer.parseInt(input);
        } else {
            System.out.println("error");
            return;
        }

        BaseConverter.setPrecise(5);
        String result = BaseConverter.fromTo(value, currentBase, targetBase);

        System.out.println(result);
    }

    private static String inputValue(Scanner in) {
        return in.hasNext() ? in.nextLine() : "-1";
    }

    private static boolean isNumber(String input) {
        for (Character c : input.toCharArray()) {
            if (!isDecimal(c)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isDecimal(Character c) {
        return '.' == c || '0' <= c && c <= '9';
    }

    private static boolean isHex(String input) {
        for (Character c : input.toCharArray()) {
            if (!(isDecimal(c) || ('a' <= c && c <= 'z'))) {
                return false;
            }
        }
        return true;
    }

    private static boolean checkRadix(String input) {
        Integer i = Integer.parseInt(input);
        if (0 < i && i < 37) {
            return true;
        } else {
            return false;
        }
    }
}

