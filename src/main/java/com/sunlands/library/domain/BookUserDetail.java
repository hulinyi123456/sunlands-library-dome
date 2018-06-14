package com.sunlands.library.domain;

import java.io.Serializable;
import java.util.Date;

public class BookUserDetail implements Serializable {
    private Integer id;

    private Integer bookId;

    private Integer userId;

    private Integer adminId;

    private Integer admin2Id;

    private Date borrowTime;

    private Date backTime;

    private String renewal;

    private String status;

    //借阅人名字
    private String borrower;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public Integer getAdmin2Id() {
        return admin2Id;
    }

    public void setAdmin2Id(Integer admin2Id) {
        this.admin2Id = admin2Id;
    }

    public Date getBorrowTime() {
        return borrowTime;
    }

    public void setBorrowTime(Date borrowTime) {
        this.borrowTime = borrowTime;
    }

    public Date getBackTime() {
        return backTime;
    }

    public void setBackTime(Date backTime) {
        this.backTime = backTime;
    }

    public String getRenewal() {
        return renewal;
    }

    public void setRenewal(String renewal) {
        this.renewal = renewal == null ? null : renewal.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getBorrower() {
        return borrower;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }
}