package com.example.android.newsapp.Utils;

/**
 * Created by jennifernghinguyen on 1/17/17.
 */

public final class DefaultParameter {

    public static final String DEFAULT_BASE_URL = "https://content.guardianapis.com";
    public static final String DEFAULT_API_KEY = "test";
    public static final String DEFAULT_FIELDS = "trailText,headline,thumbnail";
    public static final String DEFAULT_ORDER_BY = "newest";
    public static final int DEFAULT_PAGE_SIZE =10;
    public static final int DEFAULT_PAGE =1;
    public static final String DEFAULT_TAGS ="contributor";
    public static final String DEFAULT_US_SECTION = "us-news";
    public static final String DEFAULT_SPORT_SECTION = "sport";
    public static final String DEFAULT_TECH_SECTION = "technology";
    public static final String DEFAULT_WORLD_SECTION = "world";

    public static final int DEFAULT_US_CONSTANT = 0;
    public static final int DEFAULT_SPORT_CONSTANT = 1;
    public static final int DEFAULT_TECH_CONSTANT = 2;
    public static final int DEFAULT_WORLD_CONSTANT = 3;

    private DefaultParameter() {
        throw new AssertionError("can't instantiate DefaultParameter");
    }
}
