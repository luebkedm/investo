package com.investo;

public class AppConfig {
    public static final int START_YEAR = 2026;
    public static final int RETIREMENT_YEAR = 2045;
    /**
     * @deprecated Use {@link #RETIREMENT_YEAR} instead.
     */
    @Deprecated
    public static final int END_YEAR = RETIREMENT_YEAR;
    public static final int MONTHS_PER_YEAR = 12;
}
