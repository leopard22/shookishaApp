package com.example.shookisha.entity;

import java.io.Serializable;

public class Categorie implements Serializable {

     private String id;
     private String label;
     private String img;
     private String totalOffers;
     private String checked ;

     public Categorie(String id, String label, String img, String totalOffers, String checked) {
          this.id = id;
          this.label = label;
          this.img = img;
          this.totalOffers = totalOffers;
          this.checked = checked;
     }

     public String getId(){ return  id;}

     public String getLabel() {
          return label;
     }

     public void setLabel(String label) {
          this.label = label;
     }

     public String getImg() {
          return img;
     }

     public void setImg(String img) {
          this.img = img;
     }

     public String getTotalOffers() {
          return totalOffers;
     }

     public void setTotalOffers(String totalOffers) {
          this.totalOffers = totalOffers;
     }

     public String getChecked() {
          return checked;
     }

     public void setChecked(String checked) {
          this.checked = checked;
     }

     @Override
     public String toString() {
          return "Categorie{" +
                  "id='" + id + '\'' +
                  ", label='" + label + '\'' +
                  ", img='" + img + '\'' +
                  ", totalOffers='" + totalOffers + '\'' +
                  ", checked=" + checked +
                  '}';
     }
}
