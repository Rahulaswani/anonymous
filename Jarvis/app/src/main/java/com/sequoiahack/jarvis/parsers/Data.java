
package com.sequoiahack.jarvis.parsers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @Expose
    private String id;

    @Expose
    private String name;

    @SerializedName("ext_name")
    @Expose
    private String extName;

    @SerializedName("sub_title")
    @Expose
    private String subTitle;

    @Expose
    private String img;

    @Expose
    private Location location;

    @Expose
    private String timings;

    @Expose
    private String price;

    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The extName
     */
    public String getExtName() {
        return extName;
    }

    /**
     * @param extName The ext_name
     */
    public void setExtName(String extName) {
        this.extName = extName;
    }

    /**
     * @return The subTitle
     */
    public String getSubTitle() {
        return subTitle;
    }

    /**
     * @param subTitle The sub_title
     */
    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    /**
     * @return The img
     */
    public String getImg() {
        return img;
    }

    /**
     * @param img The img
     */
    public void setImg(String img) {
        this.img = img;
    }

    /**
     * @return The location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * @param location The location
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * @return The timings
     */
    public String getTimings() {
        return timings;
    }

    /**
     * @param timings The timings
     */
    public void setTimings(String timings) {
        this.timings = timings;
    }

    /**
     * @return The price
     */
    public String getPrice() {
        return price;
    }

    /**
     * @param price The price
     */
    public void setPrice(String price) {
        this.price = price;
    }

}
