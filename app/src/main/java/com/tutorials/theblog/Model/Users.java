package com.tutorials.theblog.Model;

/**
 * Created by HP on 02-12-2017.
 */

public class Users {
    public String firstname;
    public String image;
    public String lastname;
    public String Email;
    public String Password;

    public Users() {
    }

    public Users(String firstname, String image, String lastname,String Email,String Password) {
        this.firstname = firstname;
        this.image = image;
        this.lastname = lastname;
        this.Email=Email;
        this.Password=Password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
/*
    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }*/

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
