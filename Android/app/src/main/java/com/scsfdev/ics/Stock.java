package com.scsfdev.ics;

public class Stock {
    private String orderNo;
    private String staffId;
    private String location;
    private String partNo;
    private int qty;
    private boolean stockIn;

    public Stock(String staffId, String orderNo, String location, String partNo, int qty, boolean stockIn) {
        this.setStaffId(staffId);
        this.setOrderNo(orderNo);
        this.setLocation(location);
        this.setPartNo(partNo);
        this.setQty(qty);
        this.setStockIn(stockIn);
    }

    public Stock(String orderNo, String location, String partNo, int qty, boolean stockIn) {
        this.setOrderNo(orderNo);
        this.setLocation(location);
        this.setPartNo(partNo);
        this.setQty(qty);
        this.setStockIn(stockIn);
    }

    public Stock() {
        this.setOrderNo("");
        this.setLocation("");
        this.setStaffId("");
        this.setLocation("");
        this.setPartNo("");
        this.setStockIn(false);
        this.setQty(0);
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public boolean isStockIn() {
        return stockIn;
    }

    public void setStockIn(boolean stockIn) {
        this.stockIn = stockIn;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }
}
