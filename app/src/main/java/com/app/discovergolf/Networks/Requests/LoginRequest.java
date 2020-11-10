package com.app.discovergolf.Networks.Requests;

public class LoginRequest {
    String action;
    String email;
    String password;
    String user_type;


    public LoginRequest(String action, String email, String password, String user_type) {
        this.action = action;
        this.email = email;
        this.password = password;
        this.user_type = user_type;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
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

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }
}
