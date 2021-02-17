package com.webapp.a4_order_station_driver.utils.formatter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class DecimalFormatterManager {

    public static NumberFormat formatter;

    public static NumberFormat getFormatterInstance() {
        if (formatter == null) {
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
            formatter = new DecimalFormat("#0.00", symbols);
        }
        return formatter;
    }
}