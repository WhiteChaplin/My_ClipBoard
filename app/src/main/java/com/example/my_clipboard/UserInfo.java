package com.example.my_clipboard;

public class UserInfo {
    public String userEmail;
    public String userPhone;
    public String userName;

    public UserInfo(){

    }

    public UserInfo(String userEmail, String userPhone, String userName){
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
