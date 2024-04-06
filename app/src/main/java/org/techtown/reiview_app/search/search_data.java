package org.techtown.reiview_app.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class search_data {
    @Expose
    @SerializedName("idx")
    private int idx;

    @Expose
    @SerializedName("email")
    private String email;

    @Expose
    @SerializedName("book_name")
    private String book_name;

    @Expose
    @SerializedName("book_subject")
    private String book_subject;

    public String getBook_subject() {
        return book_subject;
    }

    public void setBook_subject(String book_subject) {
        this.book_subject = book_subject;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }
}
