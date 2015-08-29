package com.sequoiahack.jarvis.utils;

public class Api {
    public static final String END_POINT = "http://www.mocky.io";
    public static final String SEARCH_F = "/v2/55e1a4d2cd3fdd6c02fdb0f9";
    public static final String SEARCH = "/v2/55e1e843cd3fddcb05fdb109";
    public static final boolean ENCODE = true;

    public static class Params {
        public static final String QUERY = "q";
        public static final String CALLBACK = "__callback";
        public static final String LAT = "latitude";
        public static final String LON = "longitude";
        public static final String CONTEXT_ID = "context_id";
    }
}
