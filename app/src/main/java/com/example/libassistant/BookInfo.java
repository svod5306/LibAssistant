package com.example.libassistant;

public class BookInfo {
    private String title;
    private String gch;//馆藏号
    private String gcd;//馆藏地
    private String bdate;//借书时间
    private String rdate;//还书事件
    private String cqqk;//超期情况
    private String bookPhoto;//书籍图片
    public BookInfo(String title,String gch,String gcd,String bdate,String rdate,String cqqk,String bookphoto){
        this.title=title;
        this.gcd=gcd;
        this.gch=gch;
        this.bdate=bdate;
        this.rdate=rdate;
        this.cqqk=cqqk;
        this.bookPhoto=bookphoto;
    }

    public String getBdate() {
        return bdate;
    }

    public String getBookPhoto() {
        return bookPhoto;
    }

    public String getCqqk() {
        return cqqk;
    }

    public String getGcd() {
        return gcd;
    }

    public String getGch() {
        return gch;
    }

    public String getRdate() {
        return rdate;
    }

    public String getTitle() {
        return title;
    }
}
