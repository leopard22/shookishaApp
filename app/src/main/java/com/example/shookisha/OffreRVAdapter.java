package com.example.shookisha;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shookisha.entity.Offre;
import com.example.shookisha.shared.Api;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class OffreRVAdapter extends RecyclerView.Adapter<OffreRVAdapter.MyViewHolder>  {

    private LayoutInflater inflater;
    private ArrayList<Offre> dataModelList;
    private ItemClickListener mClickListener;
    private Api api;

    public OffreRVAdapter(Context ctx, ArrayList<Offre> dataModelList){

        inflater = LayoutInflater.from(ctx);
        this.dataModelList = dataModelList;
        api = new Api();
    }

    @Override
    public OffreRVAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_offres, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(OffreRVAdapter.MyViewHolder holder, int position) {

        String typepromo = " €";

        if(dataModelList.get(position).getImgOffer().length == 0){
            holder.imageOffer.setImageResource(R.drawable.image_vide);
        }else{
            System.out.println( api.getBaseUrlImg()+dataModelList.get(position).getImgOffer()[0].getOfferImageLabel());
            Picasso.get().load(api.getBaseUrlImg()+dataModelList.get(position).getImgOffer()[0].getOfferImageLabel()).into(holder.imageOffer);
        }

       if(dataModelList.get(position).isInBascket()){
         holder.imgPastille.setVisibility(View.VISIBLE);
        }

       if(dataModelList.get(position).getPromotionTypeId() == 1){
           typepromo = " %";
       }

        holder.labelShopAdress.setText(dataModelList.get(position).getShopOffer()[0].getShopAdress());
        holder.labelOfferReduction.setText( String.valueOf( dataModelList.get(position).getOfferReduction())+typepromo);
        holder.labelOfferTitle.setText(dataModelList.get(position).getOfferTitle());
        holder.labelShopLabel.setText(dataModelList.get(position).getShopOffer()[0].getShopLabel());
        holder.labelOfferPrice.setText( String.valueOf( dataModelList.get(position).getOfferPrice())+" €");
        holder.labelOfferPrice.setPaintFlags(holder.labelOfferPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.labelOfferNetPrice.setText( String.valueOf( dataModelList.get(position).getOfferNetPrice())+" €");

    }

    @Override
    public int getItemCount() {
        return dataModelList.size();
    }


    public Offre getItem(int position) {
        System.out.println("adpter ::  method getItem");
        return dataModelList.get(position);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

         public TextView labelOfferReduction, labelOfferTitle, labelShopLabel, labelOfferPrice, labelOfferNetPrice, labelShopAdress;
         public ImageView imageOffer,imgPastille,imgTag,imgAdress;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgPastille =  itemView.findViewById(R.id.imgPastille);
            imgTag = itemView.findViewById(R.id.imgTag);
            imgAdress = itemView.findViewById(R.id.imgAdress);
            labelShopAdress = itemView.findViewById(R.id.labelShopAdress);
            imageOffer = itemView.findViewById(R.id.imageOffer);
            labelOfferReduction = itemView.findViewById(R.id.labelOfferReduction);
            labelOfferTitle = itemView.findViewById(R.id.labelOfferTitle);
            labelShopLabel = itemView.findViewById(R.id.labelShopLabel);
            labelOfferPrice = itemView.findViewById(R.id.labelOfferPrice);
            labelOfferNetPrice = itemView.findViewById(R.id.labelOfferNetPrice);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView offreRecyclerView) {
        super.onAttachedToRecyclerView(offreRecyclerView);
    }

}
