package com.epam.game.controller.forms;

/**
 * Form for Sign Up page.
 * 
 * @author Roman_Spiridonov
 * 
 */
public class SignUpForm {

    private String name;
    private String password;
    private String repassword;
    private String email;
    private String phone = "888888888";
    private boolean agreed = true;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepassword() {
        return repassword;
    }

    public void setRepassword(String repassword) {
        this.repassword = repassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isAgreed() {
        return true;
    }

    public void setAgreed(boolean agreed) {
//        this.agreed = agreed;
    }

}
