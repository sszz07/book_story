package org.techtown.reiview_app;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class login_data {
    @Expose
    @SerializedName("idx")
    private int idx;

    @Expose
    @SerializedName("email")
    private String email;

    @Expose
    @SerializedName("password")
    private String password;

    @Expose
    @SerializedName("profile_image")
    private String profile_image;

    @Expose
    @SerializedName("nick")
    private String nick;


    @Expose
    @SerializedName("other_nick")
    private String other_nick;

    @Expose
    @SerializedName("other_email")
    private String other_email;

    @Expose
    @SerializedName("other_image")
    private String other_image;

    @Expose
    @SerializedName("image")
    private String image;


    @Expose
    @SerializedName("following")
    private int following;


    @Expose
    @SerializedName("follwer")
    private int follwer;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public int getFollwer() {
        return follwer;
    }

    public void setFollwer(int follwer) {
        this.follwer = follwer;
    }

    public String getOther_nick() {
        return other_nick;
    }

    public void setOther_nick(String other_nick) {
        this.other_nick = other_nick;
    }

    public String getOther_email() {
        return other_email;
    }

    public void setOther_email(String other_email) {
        this.other_email = other_email;
    }

    public String getOther_image() {
        return other_image;
    }

    public void setOther_image(String other_image) {
        this.other_image = other_image;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}
