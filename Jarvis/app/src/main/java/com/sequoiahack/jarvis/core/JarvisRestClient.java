package com.sequoiahack.jarvis.core;

import com.sequoiahack.jarvis.parsers.ResponseList;

import retrofit.Callback;
import retrofit.http.POST;
import retrofit.http.Query;

import static com.sequoiahack.jarvis.utils.Api.ENCODE;
import static com.sequoiahack.jarvis.utils.Api.Params.CALLBACK;
import static com.sequoiahack.jarvis.utils.Api.Params.CONTEXT_ID;
import static com.sequoiahack.jarvis.utils.Api.Params.LAT;
import static com.sequoiahack.jarvis.utils.Api.Params.LON;
import static com.sequoiahack.jarvis.utils.Api.Params.QUERY;
import static com.sequoiahack.jarvis.utils.Api.SEARCH;


public interface JarvisRestClient {

    @POST(SEARCH)
    public void search(@Query(value = QUERY, encodeValue = ENCODE) String query,
                       @Query(LAT) String lat,
                       @Query(LON) String lon,
                       @Query(CONTEXT_ID) String contextId,
                       @Query(CALLBACK) String customCallback,
                       Callback<ResponseList> callback);

}
