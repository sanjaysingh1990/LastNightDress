package com.eowise.recyclerview.stickyheaders.samples.data;

/**
 * Created by sanjay on 12/29/2015.
 */
public class MySalesPurchasesData {

    private String courier_type;
    private String image_url;
    private String shipping_method;

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    private String profile_pic;

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    private String total_amount;

    public String getCourier_type() {
        return courier_type;
    }

    public void setCourier_type(String courier_type) {
        this.courier_type = courier_type;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getShipping_method() {
        return shipping_method;
    }

    public void setShipping_method(String shipping_method) {
        this.shipping_method = shipping_method;
    }

    public long getOrder_date() {
        return order_time;
    }

    public void setOrder_date(long order_date) {
        this.order_time = order_date;
    }

    public String getOrder_purchase_status() {
        return order_purchase_status;
    }

    public void setOrder_purchase_status(String order_purchase_status) {
        this.order_purchase_status = order_purchase_status;
    }

    public String getOrder_number() {
        return order_number;
    }


    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getPrice_now() {
        return price_now;
    }

    public void setPrice_now(String price_now) {
        this.price_now = price_now;
    }

    private long order_time;
    private String order_purchase_status;

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public long getOrder_time() {
        return order_time;
    }

    public void setOrder_time(long order_time) {
        this.order_time = order_time;
    }

    private String order_number;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    private String order_id;
    private String seller_name;
    private String brand_name;
    private String price_now;

}
