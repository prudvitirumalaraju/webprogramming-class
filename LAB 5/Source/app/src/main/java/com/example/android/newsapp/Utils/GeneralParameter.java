package com.example.android.newsapp.Utils;

/**
 * Created by jennifernghinguyen on 1/18/17.
 */

/**
 * this class holds general variables that can be changed
 */
public final class GeneralParameter {
    public static int page = DefaultParameter.DEFAULT_PAGE;

    public static int totalSizeUSSection = 0;
    public static int totalSizeWorldSection = 0;
    public static int totalSizeTechSection = 0;
    public static int totalSizeSportSection = 0;

    private GeneralParameter() {
        throw new AssertionError("can't instantiate GeneralParameter");
    }
}
