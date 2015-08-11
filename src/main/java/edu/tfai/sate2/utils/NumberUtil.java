package edu.tfai.sate2.utils;

import com.frequal.romannumerals.Converter;
import com.google.common.base.Strings;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;
import nl.tudelft.simulation.dsol.interpreter.operations.RETURN;
import nl.tudelft.simulation.dsol.simulators.RealTimeClock;

import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Locale;

public abstract class NumberUtil {
    private final static Locale defaultLocale = Locale.US;

    private final static Converter romanConverter = new Converter();


    public static boolean stringIsNan(String string) {
        if (string.toLowerCase().contains("nan"))
            return true;
        double wave = Double.parseDouble(string);
        if (Double.isNaN(wave)) {
            return true;
        }
        return false;
    }

    public static String format(Number number, int precisionPlaces) {
        return format(number, precisionPlaces,"X.X");
    }

    public static String formatWithNull(Number number, int precisionPlaces) {
        return format(number, precisionPlaces,"null");
    }

    public static String formatWithNA(Number number, int precisionPlaces) {
        return format(number, precisionPlaces,"NA");
    }

    public static String format(Number number, int precisionPlaces, String nullSymbol) {
        if (number == null) {
            return nullSymbol;
        }
        return String.format(defaultLocale, "%." + precisionPlaces + "f", number);
    }

    /**
     * Formats float
     *
     * @param number number to format
     * @param length total length of sting
     */
    public static String format(String number, int length) {
        if (number.length() < length) {
            return String.format("%" + length + "s", number);
        }
        return number;
    }

    /**
     * Foarmats float to exponential
     *
     * @param number          number to format
     * @param precisionPlaces precision places
     * @param toLowerCase     convert to lowercase
     * @return formatted string
     */
    public static String expFormat(Float number, int precisionPlaces, boolean toLowerCase) throws Exception {
        if (number == null) {
            return "X.X";
        }
        if (number.isInfinite()) {
            throw new IllegalArgumentException("The number must be finite!");
        }
        String format = "0.";
        for (int i = 0; i < precisionPlaces; i++) {
            format += "0";
        }
        format += "E00";
        DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance(defaultLocale);
        df.applyPattern(format);
        String st = df.format(number);
        if (toLowerCase) {
            st = st.toLowerCase();
        }
        int end = st.length() - 1;
        if (st.charAt(end - 2) == 'e' || st.charAt(end - 2) == 'E') {
            st = st.substring(0, end - 1) + "+" + st.substring(end - 1, end + 1);
        }
        return st;
    }

    /**
     * Formats integer
     *
     * @param number number to format
     * @param places for integer
     * @return formated string
     */
    public static String formatInt(int number, int places) {
        return String.format(defaultLocale, "%" + places + "s", number).replace(' ', '0');
    }

    /**
     * Converts number to roman number
     */
    public static String convertToRomain(int number) {
        return romanConverter.toRomanNumerals(number);
    }

    public static int convertFromRomain(String romanNumber) {
        try {
            return romanConverter.toNumber(romanNumber);
        } catch (ParseException e) {
            return 1;
        }
    }

    /**
     * Calculates gamrad
     *
     */
    public static float getGamrad(float rad) {
        return (float) Math.pow(10, rad);
    }


    public static Float parse(String string) {
        if (Strings.isNullOrEmpty(string)) {
            return null;
        }

        String tokens[] = string.trim().split("\\s+");
        if (tokens.length > 1) {
            return Floats.tryParse(tokens[1]);
        } else if (tokens.length > 0) {
            return Floats.tryParse(tokens[0]);
        }
        return null;
    }

    /**
     * Parse float. Takes first token
     *
     * @param string string to parse
     * @return parsed float value
     */
    public static Integer parseInteger(String string) {
        if (Strings.isNullOrEmpty(string)) {
            return null;
        }

        String tokens[] = string.trim().split("\\s+");
        if (tokens.length > 1) {
            return Ints.tryParse(tokens[1]);
        } else if (tokens.length > 0) {
            return Ints.tryParse(tokens[0]);
        }
        return null;
    }

    public static Float tryParseFloat(Object string) {
        if (string == null) {
            return null;
        }
        return Floats.tryParse(string.toString());
    }

    public static Double tryParseDouble(Object string) {
        if (string == null) {
            return null;
        }
        return Doubles.tryParse(string.toString());
    }


}
