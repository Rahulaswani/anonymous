
package com.sequoiahack.jarvis.parsers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ResponseList {

    @Expose
    private String status;

    @SerializedName("_id")
    @Expose
    private String Id;

    @Expose
    private String message;

    @Expose
    private Integer type;

    @Expose
    private String callback;

    @Expose
    private List<Data> data = new ArrayList<Data>();

    /**
     * 
     * @return
     *     The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * 
     * @param status
     *     The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 
     * @return
     *     The Id
     */
    public String getId() {
        return Id;
    }

    /**
     * 
     * @param Id
     *     The _id
     */
    public void setId(String Id) {
        this.Id = Id;
    }

    /**
     * 
     * @return
     *     The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * 
     * @param message
     *     The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 
     * @return
     *     The type
     */
    public Integer getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 
     * @return
     *     The callback
     */
    public String getCallback() {
        return callback;
    }

    /**
     * 
     * @param callback
     *     The callback
     */
    public void setCallback(String callback) {
        this.callback = callback;
    }

    /**
     * 
     * @return
     *     The data
     */
    public List<Data> getData() {
        return data;
    }

    /**
     *
     * @param data
     *     The data
     */
    public void setData(List<Data> data) {
        this.data = data;
    }
}
