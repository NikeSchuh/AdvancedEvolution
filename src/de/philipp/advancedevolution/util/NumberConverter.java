package de.philipp.advancedevolution.util;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class NumberConverter {

    public static final NavigableMap<Long, String> suffixes = new TreeMap<>();
    static {
        suffixes.put(1_000L, "K");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

    public static String IntegerToRoman(int n){
        String roman="";
        int repeat;
        int magnitude[]={1000,900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String symbol[]={"M","CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

        for(int x=0; x<magnitude.length; x++){
            repeat=n/magnitude[x];
            for(int i=1; i<=repeat; i++){
                roman=roman + symbol[x];
            }
            n=n%magnitude[x];
        }
        return roman;
    }

    public static String format(long value) {
        if (value < 1000) return "" + value;
        int exp = (int) (Math.log(value) / Math.log(1000));
        return String.format("%.3f%c",
                value / Math.pow(1000, exp),
                "KMGTPEZY".charAt(exp-1));
    }

    public static String asPercent(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format((value * 100)) + "%";
    }

    public static String formatE(long value) {
        if (value < 1000) return "" + value;
        int exp = (int) (Math.log(value) / Math.log(1000));
        return String.format("%.1f%c",
                value / Math.pow(1000, exp),
                "KMGTPEZY".charAt(exp-1));
    }

    public static String format(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(value);
    }

}
