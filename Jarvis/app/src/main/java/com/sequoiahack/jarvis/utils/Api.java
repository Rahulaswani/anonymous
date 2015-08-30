package com.sequoiahack.jarvis.utils;

public class Api {
    public static final String END_POINT = "http://seqhack.ngrok.io";
    public static final String SEARCH = "/search";
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
