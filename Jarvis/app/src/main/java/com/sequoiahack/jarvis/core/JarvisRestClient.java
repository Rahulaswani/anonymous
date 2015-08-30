package com.sequoiahack.jarvis.core;

import com.sequoiahack.jarvis.parsers.ResponseList;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

import static com.sequoiahack.jarvis.utils.Api.ENCODE;
import static com.sequoiahack.jarvis.utils.Api.Params.CALLBACK;
import static com.sequoiahack.jarvis.utils.Api.Params.LAT;
import static com.sequoiahack.jarvis.utils.Api.Params.LON;
import static com.sequoiahack.jarvis.utils.Api.Params.META;
import static com.sequoiahack.jarvis.utils.Api.Params.QUERY;
import static com.sequoiahack.jarvis.utils.Api.Params.SID;
import static com.sequoiahack.jarvis.utils.Api.SEARCH;


public interface JarvisRestClient {

    @GET(SEARCH)
    public void search(@Query(value = QUERY, encodeValue = ENCODE) String query,
                       @Query(LAT) String lat, //req
                       @Query(LON) String lon, //req
                       @Query(SID) String sId, // return
                       @Query(META) String meta, // return
                       @Query(CALLBACK) String customCallback, // req
                       Callback<ResponseList> callback);
}
