package com.sequoiahack.jarvis.utils;

public class Api {
    public static final String END_POINT = "http://seqhack.ngrok.io/search";
    public static final String SEARCH_F = "/v2/55e1a4d2cd3fdd6c02fdb0f9";
    public static final String SEARCH = "/v2/55e1e843cd3fddcb05fdb109";
    public static final boolean ENCODE = true;

    public static class Params {
        public static final String QUERY = "q";
        public static final String CALLBACK = "callback";
        public static final String LAT = "lat";
        public static final String LON = "lng";
        public static final String SID = "sid";
        public static final String META = "meta";
    }
}
