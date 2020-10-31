package ru.dmartyanov.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class BaseConverter {
    private static Integer precise = 10000;

    public static String fromTo(String value, Integer currentBase, Integer targetBase) {
        boolean isDecimal = isDecimal(value);
        if (isDecimal) {
            return convertInteger(extractInteger(value), currentBase, targetBase)
                    + "."
                    + convertDecimal(extractDecimal(value), targetBase, currentBase);
        } else {
            return convertInteger(extractInteger(value), currentBase, targetBase);
        }
    }

    private static boolean isDecimal(String value) {
        return value.contains(".");
    }

    private static String extractInteger(String value) {
        return value.split("\\.")[0];
    }

    private static Integer convertFromHexToDec(char charAt) {
        if (Character.isLetter(charAt)) {
            return charAt - 'a' + 10;
        } else {
            return Integer.parseInt("" + charAt);
        }
    }

    private static String extractDecimal(String value) {
        return value.split("\\.")[1];
    }

    private static String convertDecimal(String value, Integer targetBase, Integer currentBase) {
        List<String> resultValues = new ArrayList<>();
        Double currentValue = calcForm(extractDecimal(fromCurrentToDecForDecimal(value, currentBase)));
        String integerValue;

        do {
            currentValue *= targetBase;
            integerValue = extractInteger(currentValue.toString());
            resultValues.add(integerValue);
            currentValue = calcForm(extractDecimal(currentValue.toString()));
        } while (hasInteger(integerValue));

        return applyPrecise(prepareResult(targetBase, resultValues));
    }

    private static String prepareResult(Integer targetBase, List<String> resultValues) {
        StringBuilder result = new StringBuilder();
        for (String s : resultValues) {
            if (targetBase < 10) {
                result.append(s);
            } else {
                result.append(convertToHex(s));
            }
        }
        return result.toString();
    }

    private static String applyPrecise(String result) {
        StringBuilder prepareResult = new StringBuilder();
        for (int i = 0; i < precise; i++) {
            if (result.length() > precise) {
                prepareResult.append(result.charAt(i));
            } else {
                prepareResult.append("0");
            }
        }
        return prepareResult.toString();
    }

    private static String convertToHex(String s) {
        int integer = Integer.parseInt(s);
        if (integer < 10) {
            return s;
        } else {
            return "" + (char) ('a' + integer - 10);
        }
    }

    private static double calcForm(String value) {
        return Double.parseDouble("0." + value);
    }

    private static boolean hasInteger(String value) {
        return !extractInteger(value).equals("0");
    }

    private static String convertInteger(String value, Integer currentBase, Integer targetBase) {
        String decValue = fromCurrentToDec(value, currentBase);

        return fromDecToTarget(targetBase, decValue);
    }

    private static String fromDecToTarget(Integer targetBase, String decValue) {
        List<String> resultValues = new ArrayList<>();

        int currentValue = Integer.parseInt(decValue);
        int reminderValue;

        if (targetBase != 1) {
            while (currentValue >= targetBase) {
                reminderValue = currentValue % targetBase;
                currentValue /= targetBase;
                resultValues.add(Integer.toString(reminderValue));
            }

            reminderValue = currentValue % targetBase;

            resultValues.add(Integer.toString(reminderValue));
            Collections.reverse(resultValues);
        } else {
            for (int i = 0; i < Integer.valueOf(decValue); i++) {
                resultValues.add("1");
            }
        }

        return prepareResult(targetBase, resultValues);
    }

    private static String fromCurrentToDec(String value, Integer currentBase) {
        List<String> resultValues = new ArrayList<>();
        int length = value.length();

        // From currentBase to decBase
        for (int i = 0; i < length; i++) {
            Integer currentValue = convertFromHexToDec(value.charAt(i));
            double calculatedValue = Math.pow(currentBase, length - i - 1) * currentValue;
            resultValues.add(extractInteger(Double.toString(calculatedValue)));
        }

        int amount = 0;
        for (String s : resultValues) {
            amount += Integer.parseInt(s);
        }
        return Integer.toString(amount);
    }

    private static String fromCurrentToDecForDecimal(String value, Integer currentBase) {
        List<String> resultValues = new ArrayList<>();
        int length = value.length();

        // From currentBase to decBaseForDecimal
        for (int i = 0; i < length; i++) {
            Integer currentValue = convertFromHexToDec(value.charAt(i));
            double calculatedValue = currentValue / Math.pow(currentBase, i + 1);
            resultValues.add(Double.toString(calculatedValue));
        }

        double amount = 0.0;
        for (String s : resultValues) {
            amount += Double.parseDouble(s);
        }
        return Double.toString(amount);
    }

    public static void setPrecise(Integer precise) {
        BaseConverter.precise = precise;
    }
}
