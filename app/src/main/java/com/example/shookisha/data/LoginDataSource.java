package com.example.shookisha.data;

import android.os.AsyncTask;

import com.example.shookisha.data.model.LoggedInUser;
import com.example.shookisha.shared.Api;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    String code;
    String displayName;
    String token;
    String userID;
    Boolean ok;
    LoggedInUser user;

    public Result<LoggedInUser> login(String username, String password) {


            // TODO: handle loggedInUser authentication
            new FetchTask().execute(username,password);


            if(code == "true"){
                return new Result.Success<>(user);
            } else{
                return null;
            }


       /*    try {

        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }*/
    }

    private class FetchTask extends AsyncTask<String, Void, String> {

        String lepassword ;
        String leloggin ;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {

            leloggin = strings[0];
            lepassword = strings[1];

            Api api = new Api();

            String userlogin = "{\n" +
                    "  \"password\": \""+lepassword+ "\",\n" +
                    "  \"usernameOrEmail\": \""+leloggin+"\"\n" +
                    "}";

            System.out.println("LoginDataSource userlogin ::"+userlogin);

            try {

                String resultApi = api.post("auth/signin",userlogin);

                System.out.println("ResultatApi "+resultApi);



                return resultApi;

            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject obj = new JSONObject(s);

                JSONObject status = obj.getJSONObject("status");
                code = status.getString("success");

                JSONObject authorization = obj.getJSONObject("authorization");
                token = authorization.getString("accessToken");

                JSONObject user = obj.getJSONObject("user");
                userID = user.getString("id");
                displayName = user.getString("firstname")+" "+user.getString("lastname");

            } catch (JSONException e) {
                code = null;
                e.printStackTrace();
            }

            if (s == null) {
                System.out.println("erreur :: "+s+" "+code);
                ok = null;

            } else {
                System.out.println("LoginDataSource:: resultat api : "+code+" var s ="+s);
                ok = true;
                user = new LoggedInUser(userID,displayName,token);


            }

        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
