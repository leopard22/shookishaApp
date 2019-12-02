package com.example.shookisha.entity;

import com.example.shookisha.data.model.LoggedInUser;

public class User {

    private Consommateur consommateur;
    private Categorie []categorie;
    private LoggedInUser loggedInUser;

    public User() {
    }

    public Consommateur getConsommateur() {
        return consommateur;
    }

    public void setConsommateur(Consommateur consommateur) {
        this.consommateur = consommateur;
    }

    public Categorie[] getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie[] categorie) {
        this.categorie = categorie;
    }

    public LoggedInUser getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(LoggedInUser loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
}
