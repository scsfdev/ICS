package com.scsfdev.ics;

import java.util.Date;

public class Shipment {
    private String shipmentNo;
    private String supplierCode;
    private String customerCode;
    private String orderNo;
    private String locId;
    private String partNo;
    private Integer qty;
    private Date date;

    public Shipment() {
        this.shipmentNo = "";
        this.supplierCode = "";
        this.customerCode = "";
        this.locId = "";
        this.orderNo = "";
        this.partNo = "";
        this.qty = 0;
        this.date = null;
    }

    public Shipment(String shipmentNo, String supplierCode, String customerCode, String orderNo, String partNo, Integer qty, Date date) {
        this.shipmentNo = shipmentNo;
        this.supplierCode = supplierCode;
        this.customerCode = customerCode;
        this.orderNo = orderNo;
        this.partNo = partNo;
        this.qty = qty;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getShipmentNo() {
        return shipmentNo;
    }

    public void setShipmentNo(String shipmentNo) {
        this.shipmentNo = shipmentNo;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getLocId() {
        return locId;
    }

    public void setLocId(String locId) {
        this.locId = locId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }
}
