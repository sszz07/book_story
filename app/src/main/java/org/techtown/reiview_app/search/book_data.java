package org.techtown.reiview_app.search;

public class book_data {

    private String title;      // 제목
    private String wirter;      // 작가
    private String make_center;       // 출판사
    private String date;      //날짜
    private String book_image;      //이미지
    private String book_content;
    private String isbn;
    private String itemId;


    public String getBook_content() {
        return book_content;
    }

    public void setBook_content(String book_content) {
        this.book_content = book_content;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWirter() {
        return wirter;
    }

    public void setWirter(String wirter) {
        this.wirter = wirter;
    }

    public String getMake_center() {
        return make_center;
    }

    public void setMake_center(String make_center) {
        this.make_center = make_center;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBook_image() {
        return book_image;
    }

    public void setBook_image(String book_image) {
        this.book_image = book_image;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public book_data(String title, String wirter, String make_center, String date, String book_image, String isbn, String book_content,String itemId) {
        this.title = title;
        this.wirter = wirter;
        this.make_center = make_center;
        this.date = date;
        this.book_image = book_image;
        this.isbn = isbn;
        this.book_content = book_content;
        this.itemId = itemId;
    }
}
