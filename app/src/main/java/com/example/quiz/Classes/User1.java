package com.example.quiz.Classes;

import android.util.Log;


public class User1 {
    private String userid;
    private String username;
    private String useremail;
    private String usermobileno;
    private String password;


    public User1(String userid, String username, String useremail,String password, String usermobileno) {
        this.userid = userid;
        this.username = username;
        this.useremail = useremail;
        this.usermobileno = usermobileno;
        this.password=password;
    }


    public User1() {

    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getUsermobileno() {
        return usermobileno;
    }

    public void setUsermobileno(String usermobileno) {
        this.usermobileno = usermobileno;
    }

    public String getUserid() {
        return userid;
    }

    public String getPassword() {
        return password;
    }


}