
package com.sequoiahack.jarvis.parsers;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class ResponseList {

    @Expose
    private String status;
    @Expose
    private String msg;
    @Expose
    private String callback;
    @Expose
    private List<Data> data = new ArrayList<Data>();
    @Expose
    private String meta;
    @Expose
    private String sid;
    @Expose
    private Integer type;

    /**
     * @return The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return The msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * @param msg The msg
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * @return The callback
     */
    public String getCallback() {
        return callback;
    }

    /**
     * @param callback The callback
     */
    public void setCallback(String callback) {
        this.callback = callback;
    }

    /**
     * @return The data
     */
    public List<Data> getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(List<Data> data) {
        this.data = data;
    }

    /**
     * @return The meta
     */
    public String getMeta() {
        return meta;
    }

    /**
     * @param meta The meta
     */
    public void setMeta(String meta) {
        this.meta = meta;
    }

    /**
     * @return The sid
     */
    public String getSid() {
        return sid;
    }

    /**
     * @param sid The sid
     */
    public void setSid(String sid) {
        this.sid = sid;
    }

    /**
     * @return The type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type The type
     */
    public void setType(Integer type) {
        this.type = type;
    }

}
