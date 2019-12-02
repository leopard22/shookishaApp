package com.example.shookisha;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shookisha.entity.Offre;
import com.example.shookisha.entity.OffreDetail;
import com.example.shookisha.shared.Api;
import com.example.shookisha.utility.ToolsUtility;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class OffreDetailActivity extends AppCompatActivity {
    private TextView dureeoffre,dureecoupon,labelOfferDetailReduction,offerDescription, labelOfferDetailTitle, labelDetailShopLabel, labelOfferDetailPrice, labelOfferDetailNetPrice,labelDetailShopAdress;
    private ImageView imgOfferDetail;
    private ViewPager sliderViewpager;
    private String baseUrl ="http://www.worldcorpgroup.fr/shookisha/picture/offers/";
    private Button btnGenererCodeBare;
    private ImageButton btnGoPreference, btnBackOffreDetail;
    private String userToken="";
    private OffreDetail offreDetail;
    private Activity context;
    private Button btnLibererCoupon;
    private TextView labelNetCoupon,labelPriceCoupon,labelReductionCoupon,labelLieuxCoupon,shopLabelCoupon,labelTitleCoupon,codebarreDuree;
    private ImageView imageProduit,imgcodebarre;
    private Api api;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent i = getIntent();
        Offre uneOffre = (Offre) i.getSerializableExtra("uneOffre");
        userToken = i.getStringExtra("token");

        initActivityOffreDetail();

        api = new Api();

        new OfferDetailTask().execute(uneOffre.getOfferId());

        btnBackOffreDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnGenererCodeBare.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                initLayoutCouponGenerer();

                populateOffreDetailCoupon(offreDetail);


                btnBackOffreDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            }
        });

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private class OfferDetailTask extends AsyncTask<Integer,Void,String>{

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(Integer... integers) {

           // Api api = new Api();
            String resultApi="";
            int objectId = integers[0];
            String jsonStr = "{"+  "\"objectId\": "+objectId+" } ";
            System.out.println(objectId);
            System.out.println(jsonStr);

            try{
                resultApi = api.postAuth("offerdetail",jsonStr,userToken);
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            return resultApi;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.println(" reponse server "+ s );
            try{
                JSONObject obj = new JSONObject(s);
                if (obj.getString("status").compareTo("200") == 0){
                    String offreDetaillData = obj.getString("content");
                    parseOffreDetail(offreDetaillData);
                }else{
                   // Toast.makeText(context.getApplicationContext(), "Veuillez vous connecter à internet ", Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void parseOffreDetail(String offreDetaillData){
        try{
            Log.i("Log_offreDetailtask", "parseOffreDetail called from OfferDetailTask :: onPostExecute");
            System.out.println("parseInfos :: method");
            ObjectMapper mapper = new ObjectMapper();
            offreDetail = mapper.readValue(offreDetaillData, new TypeReference<OffreDetail>(){});
            populateOffreDetail(offreDetail);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void populateOffreDetail(OffreDetail offreDetail){
        String typepromo = " "+offreDetail.getCurrencySymbol();
        ToolsUtility toolsUtility = new ToolsUtility();
        String startDate = toolsUtility.dateToString(offreDetail.getStartDate());
        String endDate = toolsUtility.dateToString(offreDetail.getEndDate());
        SliderViewPagerAdapter adapter = new SliderViewPagerAdapter(this,offreDetail.getImagesLabel() );
        sliderViewpager.setAdapter(adapter);

        offerDescription.setText(offreDetail.getDescription());
        labelDetailShopAdress.setText(offreDetail.getShopAdress()+" "+offreDetail.getCity());
        labelOfferDetailPrice.setText(offreDetail.getPrice()+" "+offreDetail.getCurrencySymbol());
        labelOfferDetailPrice.setPaintFlags(labelOfferDetailPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        labelOfferDetailNetPrice.setText(offreDetail.getNetPrice()+" "+offreDetail.getCurrencySymbol());
        if(offreDetail.getPrtId() == 1){
            typepromo = " %";
        }

        labelOfferDetailReduction.setText((int) offreDetail.getReduction()+typepromo);
        labelOfferDetailTitle.setText(offreDetail.getTitle());
        labelDetailShopLabel.setText(offreDetail.getShopLabel());
        System.out.println("date start offre : "+startDate+" , end offre : "+endDate);
        dureeoffre.setText(getString(R.string.duree_offre,startDate,endDate));
    }

    public void populateOffreDetailCoupon(OffreDetail offreDetail){

        String typepromo = " "+offreDetail.getCurrencySymbol();

        if(offreDetail.getPrtId() == 1){
            typepromo = " %";
        }

        labelPriceCoupon.setText(offreDetail.getPrice()+" "+offreDetail.getCurrencySymbol());
        labelPriceCoupon.setPaintFlags(labelPriceCoupon.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        labelNetCoupon.setText(offreDetail.getNetPrice()+" "+offreDetail.getCurrencySymbol());
        labelReductionCoupon.setText((int) offreDetail.getReduction()+typepromo);

        labelTitleCoupon.setText(offreDetail.getTitle());
        shopLabelCoupon.setText(offreDetail.getShopLabel());

        if( offreDetail.getImagesLabel().isEmpty() ){
            imageProduit.setImageResource(R.drawable.image_vide);
        }
        else {
            Picasso.get().load( api.getBaseUrlImg()+offreDetail.getImagesLabel()).into(imageProduit);
        }

        new CountDownTimer(offreDetail.getCouponDuration()*1000, 1000) {

            public void onTick(long millisUntilFinished) {
                codebarreDuree.setText( getString(R.string.code_barre_Duree, millisUntilFinished / 1000)  );
            }

            public void onFinish() {
                codebarreDuree.setText(getString(R.string.coupon_expire_text));
                //   View.findViewById(R.id.codebarreimg)
                imgcodebarre.setImageResource(R.drawable.coupon_layers);
                btnLibererCoupon.setVisibility(View.INVISIBLE);
            }
        }.start();
    }

    public void initLayoutCouponGenerer(){
        setContentView(R.layout.coupon_generer);

        btnBackOffreDetail = findViewById(R.id.btnBackOffreDetail);
        btnLibererCoupon = findViewById(R.id.btnLibererCoupon);
        labelNetCoupon = findViewById(R.id.labelNetCoupon);
        labelPriceCoupon = findViewById(R.id.labelPriceCoupon);
        labelReductionCoupon = findViewById(R.id.labelReductionCoupon);
        labelLieuxCoupon = findViewById(R.id.labelLieuxCoupon);
        shopLabelCoupon = findViewById(R.id.shopLabelCoupon);
        labelTitleCoupon = findViewById(R.id.labelTitleCoupon);
        imageProduit = findViewById(R.id.imageProduit);
        codebarreDuree = findViewById(R.id.codebarreDuree);
        imgcodebarre = findViewById(R.id.imgcodebarre);
    }

    public void initActivityOffreDetail(){
        setContentView(R.layout.activity_offre_detail);

//initialisation des élément de la page offredetail
        btnBackOffreDetail = findViewById(R.id.btnBackOffreDetail);
        btnGoPreference = findViewById(R.id.btnGoPreference);

        labelOfferDetailReduction = findViewById(R.id.labelOffreDetailReduction);
        labelOfferDetailNetPrice =findViewById(R.id.labelOfferDetailNetPrice);
        labelOfferDetailPrice = findViewById(R.id.labelOfferDetailPrice);
        labelDetailShopAdress = findViewById(R.id.labelDetailShopAdress);
        offerDescription = findViewById(R.id.offerDescription);
        labelDetailShopLabel = findViewById(R.id.labelDetailShopLabel);
        labelOfferDetailTitle = findViewById(R.id.labelOfferDetailTitle);
        //   imgOfferDetail = findViewById(R.id.imgOffreDetail);
        btnGenererCodeBare = findViewById(R.id.btnGenererCodeBare);
        sliderViewpager = findViewById(R.id.sliderViewpager);
        dureecoupon = findViewById(R.id.dureecoupon);
        dureeoffre = findViewById(R.id.dureeoffre);
    }

}
