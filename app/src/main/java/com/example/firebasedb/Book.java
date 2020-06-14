package com.example.firebasedb;

public class Book {
    String bookName,sellerID,ISBN,genre;
    String price;
    Long uniqueID;

    public Book() {
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getSellerID() {
        return sellerID;
    }

    public void setSellerID(String sellerID) {
        this.sellerID = sellerID;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Long getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(Long uniqueID) {
        this.uniqueID = uniqueID;
    }

    public Book(String bookName, String sellerID, String ISBN, String genre, String price) {
        this.bookName = bookName;
        this.sellerID = sellerID;
        this.ISBN = ISBN;
        this.genre = genre;
        this.price = price;
        this.uniqueID=null;
    }

    public Book(String bookName, String sellerID, String ISBN, String genre, String price, Long uniqueID) {
        this.bookName = bookName;
        this.sellerID = sellerID;
        this.ISBN = ISBN;
        this.genre = genre;
        this.price = price;
        this.uniqueID = uniqueID;
    }
}
