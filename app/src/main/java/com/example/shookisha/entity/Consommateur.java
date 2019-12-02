package com.example.shookisha.entity;


import java.io.Serializable;

public class Consommateur implements Serializable {

    private String firstname;
    private String lastname;
    private String bornAt;//date
    private int genId;
    private String email;
    private String password;
    private String username;
    private int prefDistance = 5;
    private String image ;

    public Consommateur(/*String firstname, String lastname, String bornAt, int genId, String email, String password*/) {
       /* this.firstname = firstname;
        this.lastname = lastname;
        this.bornAt = bornAt;
        this.genId = genId;
        this.email = email;
        this.password = password;*/
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getBornAt() {
        return bornAt;
    }

    public void setBornAt(String bornAt) {
        this.bornAt = bornAt;
    }

    public int getGenId() {
        return genId;
    }

    public void setGenId(int genId) {
        this.genId = genId;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPrefDistance() {
        return prefDistance;
    }

    public void setPrefDistance(int prefDistance) {
        this.prefDistance = prefDistance;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "{" +
                "firstname:'" + firstname + '\'' +
                ", lastname:'" + lastname + '\'' +
                ", bornAt:'" + bornAt + '\'' +
                ", genId:" + genId +
                ", email:'" + email + '\'' +
                ", password:'" + password + '\'' +
                ", username:'" + username + '\'' +

                '}';
       // "{" +"bornAt":"1995-08-15","email":"nom@ndj.com","firstname":"prenom","genId":0,"image":null,"lastname":"nom","password":"popo","username":"prenom"+"}";
    }
}
