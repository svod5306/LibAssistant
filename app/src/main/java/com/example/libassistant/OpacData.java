package com.example.libassistant;

public class OpacData {
    private String title;
    private String author;
    private String isbn;
    private String ssh;
    private String cbd;
    private String gcd;
    private String gch;
    private String bookimage;
    public OpacData(String title,String author,String isbn,String ssh,String cbd,String gcd,String gch,String bookimage){
        this.title=title;
        this.author=author;
        this.isbn=isbn;
        this.ssh=ssh;
        this.cbd=cbd;
        this.gcd=gcd;
        this.gch=gch;
        this.bookimage=bookimage;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getBookimage() {
        return bookimage;
    }

    public void setBookimage(String bookimage) {
        this.bookimage = bookimage;
    }

    public String getSsh() {
        return ssh;
    }

    public String getCbd() {
        return cbd;
    }

    public String getGcd() {
        return gcd;
    }

    public String getGch() {
        return gch;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setSsh(String ssh) {
        this.ssh = ssh;
    }

    public void setCbd(String cbd) {
        this.cbd = cbd;
    }

    public void setGcd(String gcd) {
        this.gcd = gcd;
    }

    public void setGch(String gch) {
        this.gch = gch;
    }
}
