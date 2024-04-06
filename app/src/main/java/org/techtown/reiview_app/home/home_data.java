package org.techtown.reiview_app.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class home_data {

    @Expose
    @SerializedName("idx")
    private int idx;

    @Expose
    @SerializedName("email")
    private String email;

    @Expose
    @SerializedName("image")
    private String image;

    @Expose
    @SerializedName("book_subject")
    private String book_subject;

    @Expose
    @SerializedName("start_page")
    private String start_page;

    @Expose
    @SerializedName("end_page")
    private String end_page;

    @Expose
    @SerializedName("memory_content")
    private String memory_content;

    @Expose
    @SerializedName("after_content")
    private String after_content;

    @Expose
    @SerializedName("make")
    private String make;

    @Expose
    @SerializedName("writer")
    private String writer;

    @Expose
    @SerializedName("present_time")
    private long present_time;



    @Expose
    @SerializedName("memo_count")
    private String memo_count;

    @Expose
    @SerializedName("book_page")
    private String book_page;


    @Expose
    @SerializedName("time")
    private String time;

    //메모 데이터
    @Expose
    @SerializedName("memo")
    private String memo;

    @Expose
    @SerializedName("date")
    private long date;


    @Expose
    @SerializedName("read_page")
    private int read_page;

    @Expose
    @SerializedName("read_time")
    private int read_time;

    @Expose
    @SerializedName("today_read_time")
    private int today_read_time;

    @Expose
    @SerializedName("want_date")
    private String want_date;


    @Expose
    @SerializedName("book_content")
    private String book_content;

    @Expose
    @SerializedName("finish")
    private String finish;

    @Expose
    @SerializedName("my_content")
    private String my_content;


    @Expose
    @SerializedName("start_day")
    private String start_day;


    @Expose
    @SerializedName("book_star")
    private float book_star;


    @Expose
    @SerializedName("record_date")
    private String record_date;


    @Expose
    @SerializedName("subject")
    private String subject;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getRecord_date() {
        return record_date;
    }

    public void setRecord_date(String record_date) {
        this.record_date = record_date;
    }

    public String getStart_day() {
        return start_day;
    }

    public void setStart_day(String start_day) {
        this.start_day = start_day;
    }

    public float getBook_star() {
        return book_star;
    }

    public void setBook_star(float book_star) {
        this.book_star = book_star;
    }

    public String getMy_content() {
        return my_content;
    }

    public void setMy_content(String my_content) {
        this.my_content = my_content;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    public String getBook_content() {
        return book_content;
    }

    public void setBook_content(String book_content) {
        this.book_content = book_content;
    }

    public String getWant_date() {
        return want_date;
    }

    public void setWant_date(String want_date) {
        this.want_date = want_date;
    }

    public int getToday_read_time() {
        return today_read_time;
    }

    public void setToday_read_time(int today_read_time) {
        this.today_read_time = today_read_time;
    }

    public int getRead_time() {
        return read_time;
    }

    public void setRead_time(int read_time) {
        this.read_time = read_time;
    }

    public int getRead_page() {
        return read_page;
    }

    public void setRead_page(int read_page) {
        this.read_page = read_page;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBook_subject() {
        return book_subject;
    }

    public void setBook_subject(String book_subject) {
        this.book_subject = book_subject;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public long getPresent_time() {
        return present_time;
    }

    public void setPresent_time(long present_time) {
        this.present_time = present_time;
    }

    public String getMemo_count() {
        return memo_count;
    }

    public void setMemo_count(String memo_count) {
        this.memo_count = memo_count;
    }

    public String getBook_page() {
        return book_page;
    }

    public void setBook_page(String book_page) {
        this.book_page = book_page;
    }

    public String getStart_page() {
        return start_page;
    }

    public void setStart_page(String start_page) {
        this.start_page = start_page;
    }

    public String getEnd_page() {
        return end_page;
    }

    public void setEnd_page(String end_page) {
        this.end_page = end_page;
    }

    public String getMemory_content() {
        return memory_content;
    }

    public void setMemory_content(String memory_content) {
        this.memory_content = memory_content;
    }

    public String getAfter_content() {
        return after_content;
    }

    public void setAfter_content(String after_content) {
        this.after_content = after_content;
    }
}
