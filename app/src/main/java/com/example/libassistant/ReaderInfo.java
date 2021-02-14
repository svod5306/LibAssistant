package com.example.libassistant;

import java.util.List;

public class ReaderInfo {
    private String name;
    private String sex;
    private String department;
    private String job_title;
    private String grade;
    private String canBorrow;
    private String nowBorrow;
    private String overdue;
    private String photo;
    private List<BookInfo> borrowedBooksList;
    private List<BookInfo> historyBookList;

    public ReaderInfo(String name, String sex, String department, String job_title, String grade, String canBorrow, String nowBorrow, String overdue, String photo, List<BookInfo> borrowedBooksList, List<BookInfo> historyBookList) {
        this.name = name;
        this.sex = sex;
        this.department = department;
        this.job_title = job_title;
        this.grade = grade;
        this.canBorrow = canBorrow;
        this.nowBorrow = nowBorrow;
        this.overdue = overdue;
        this.photo = photo;
        this.borrowedBooksList = borrowedBooksList;
        this.historyBookList = historyBookList;
    }


    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public String getDepartment() {
        return department;
    }

    public String getJob_title() {
        return job_title;
    }

    public String getGrade() {
        return grade;
    }

    public String getCanBorrow() {
        return canBorrow;
    }

    public String getNowBorrow() {
        return nowBorrow;
    }

    public String getOverdue() {
        return overdue;
    }

    public String getPhoto() {
        return photo;
    }

    public List<BookInfo> getBorrowedBooksList() {
        return borrowedBooksList;
    }

    public List<BookInfo> getHistoryBookList() {
        return historyBookList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setCanBorrow(String canBorrow) {
        this.canBorrow = canBorrow;
    }

    public void setNowBorrow(String nowBorrow) {
        this.nowBorrow = nowBorrow;
    }

    public void setOverdue(String overdue) {
        this.overdue = overdue;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setBorrowedBooksList(List<BookInfo> borrowedBooksList) {
        this.borrowedBooksList = borrowedBooksList;
    }

    public void setHistoryBookList(List<BookInfo> historyBookList) {
        this.historyBookList = historyBookList;
    }
}
