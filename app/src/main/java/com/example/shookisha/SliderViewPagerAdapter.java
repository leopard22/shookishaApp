package com.example.shookisha;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.shookisha.entity.ImgOffer;
import com.example.shookisha.shared.Api;
import com.squareup.picasso.Picasso;

public class SliderViewPagerAdapter extends PagerAdapter {

    private Context context;
    //private ImgOffer [] imgOffers;
    private String imageCover;
    private Api api;

    /*SliderViewPagerAdapter(Context context, ImgOffer[]imgOffers){
        this.context = context;
        this.imgOffers = imgOffers;
        api = new Api();
    }*/

    SliderViewPagerAdapter(Context context, String imageCover){
        this.context = context;
        this.imageCover = imageCover;
        api = new Api();
    }

    @Override
    public int getCount() {
        //return imgOffers.length;
        return 1;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);

       /* Picasso.get().load(api.getBaseUrlImg()+imgOffers[position].getOfferImageLabel())
                .placeholder(R.drawable.image_vide)
                .error(R.drawable.image_vide)
                .into(imageView);*/

        Picasso.get().load(api.getBaseUrlImg()+imageCover)
                .placeholder(R.drawable.image_vide)
                .error(R.drawable.image_vide)
                .into(imageView);

        container.addView(imageView);

        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
