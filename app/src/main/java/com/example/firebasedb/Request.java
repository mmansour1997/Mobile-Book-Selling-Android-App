package com.example.firebasedb;

public class Request {
    private String buyerID, sellerID, chatRoomID,itemID,bookName,requestDate;
    public  Request(){};

    public Request(String buyerID, String sellerID, String chatRoomID, String itemID, String bookName, String requestDate) {
        this.buyerID = buyerID;
        this.sellerID = sellerID;
        this.chatRoomID = chatRoomID;
        this.itemID = itemID;
        this.bookName = bookName;
        this.requestDate = requestDate;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getBuyerID() {
        return buyerID;
    }

    public void setBuyerID(String buyerID) {
        this.buyerID = buyerID;
    }

    public String getSellerID() {
        return sellerID;
    }

    public void setSellerID(String sellerID) {
        this.sellerID = sellerID;
    }

    public String getChatRoomID() {
        return chatRoomID;
    }

    public void setChatRoomID(String chatRoomID) {
        this.chatRoomID = chatRoomID;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }
}
