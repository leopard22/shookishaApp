package com.example.shookisha.utility;

import com.example.shookisha.data.model.LoggedInUser;
import com.example.shookisha.entity.Categorie;
import com.example.shookisha.entity.Consommateur;
import com.example.shookisha.entity.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

public class UserDataSource {
    private User user;
    private String userSource;

    public UserDataSource(String userSource) {
        this.userSource = userSource;
    }

    public User getUser() {

        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            JSONObject obj = new JSONObject(userSource);
            JSONObject content = obj.getJSONObject("content"); //je recupère l'objet content qui contient user, authorization et categories
            JSONObject userData = content.getJSONObject("user");
            JSONObject authorization = content.getJSONObject("authorization");
            JSONArray categorieData = content.getJSONArray("categories");

            LoggedInUser loggedinUser = new LoggedInUser(userData.getString("id"),userData.getString("firstname")+" "+userData.getString("lastname"),authorization.getString("accessToken"));

            Consommateur consommateur = new Consommateur();
            consommateur.setUsername(userData.getString("firstname"));
            consommateur.setFirstname(userData.getString("firstname"));
            consommateur.setLastname(userData.getString("lastname"));
            consommateur.setEmail(userData.getString("email"));
            consommateur.setPrefDistance( (int) Math.round(Double.valueOf(userData.getString("prefDistance")))  );
            consommateur.setBornAt(userData.getString("bornAt"));
            consommateur.setGenId(Integer.parseInt(userData.getString("genId")));

            Categorie [] tabCategori = new Categorie[categorieData.length()];
            for(int i=0; i < categorieData.length(); i++){
                JSONObject catDetail = categorieData.getJSONObject(i);
                tabCategori[i] = new Categorie(catDetail.getString("id"),catDetail.getString("label"),catDetail.getString("img"),catDetail.getString("totalOffers"),catDetail.getString("checked"));

            }

            System.out.println(tabCategori.toString());
            user = new User();

            user.setCategorie(tabCategori);
            user.setConsommateur(consommateur);
            user.setLoggedInUser(loggedinUser);


        }catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }

    public String getUserSource() {
        return userSource;
    }
}
