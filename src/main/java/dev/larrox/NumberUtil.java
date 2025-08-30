package dev.larrox;

import java.text.NumberFormat;
import java.util.Locale;

public class NumberUtil {

    public String formatBalance(double balance, Locale locale) {
        NumberFormat formatter = NumberFormat.getNumberInstance(locale);
        formatter.setMinimumFractionDigits(0);
        formatter.setMaximumFractionDigits(2);
        return formatter.format(balance);
    }

    public String formatAmount(double amount) {
        return (amount % 1 == 0)
                ? String.format("%,d", (long) amount)
                : String.format("%,.2f", amount);
    }

    public double parseAmount(String input) {
        input = input.toLowerCase().replace(",", "");

        try {
            if (input.endsWith("k")) {
                return Double.parseDouble(input.replace("k", "")) * 1_000;
            } else if (input.endsWith("m")) {
                return Double.parseDouble(input.replace("m", "")) * 1_000_000;
            } else if (input.endsWith("b")) {
                return Double.parseDouble(input.replace("b", "")) * 1_000_000_000;
            } else {
                return Double.parseDouble(input);
            }
        } catch (NumberFormatException e) {
            return -1;
        }
    }

}
