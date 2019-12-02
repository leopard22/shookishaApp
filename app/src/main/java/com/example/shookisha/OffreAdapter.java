/*package com.example.shookisha;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shookisha.entity.Offre;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OffreAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<Offre> dataModelList;
    private Context ctx;
    private TextView labelOfferReduction, labelOfferTitle, labelShopLabel, labelOfferPrice, labelOfferNetPrice;
    private ImageView imageOffer,imgPastille,imgTag;

    public OffreAdapter(Context ctx, List<Offre> dataModelList){

        //inflater = LayoutInflater.from(ctx);
        this.ctx = ctx;
        this.dataModelList = dataModelList;
    }

    @Override
    public int getCount() {
        return dataModelList.size();
    }

    @Override
    public Object getItem(int position) {
        System.out.println("adpter ::  method getItem");
        return dataModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //itemView = inflater.inflate(R.layout.list_offres, null);

        View itemView = inflater.inflate(R.layout.list_offres, parent, false);
        imgPastille = (ImageView) itemView.findViewById(R.id.imgPastille);
        imgTag = (ImageView) itemView.findViewById(R.id.imgTag);
        imageOffer = (ImageView) itemView.findViewById(R.id.imageOffer);
        labelOfferReduction = (TextView) itemView.findViewById(R.id.labelOfferReduction);
        labelOfferTitle = (TextView) itemView.findViewById(R.id.labelOfferTitle);
        labelShopLabel = (TextView) itemView.findViewById(R.id.labelShopLabel);
        labelOfferPrice = (TextView) itemView.findViewById(R.id.labelOfferPrice);
        labelOfferNetPrice = (TextView) itemView.findViewById(R.id.labelOfferNetPrice);


//      Picasso.get().load(dataModelList.get(position).getImgOffer()[0].getOfferImageLabel()).into(holder.imageOffer);
        labelOfferReduction.setText( String.valueOf( dataModelList.get(position).getOfferReduction())+"%");
        labelOfferTitle.setText(dataModelList.get(position).getOfferTitle());
        labelShopLabel.setText(dataModelList.get(position).getShopOffer()[0].getShopLabel());
        labelOfferPrice.setText( String.valueOf( dataModelList.get(position).getOfferPrice())+" €");
        labelOfferNetPrice.setText( String.valueOf( dataModelList.get(position).getOfferNetPrice())+" €");

        /*itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(" Adapter getView :: method setOnItemClickListener");
                //System.out.println(dataModelList.get(position).getOfferTitle());

                OffresActivity girdItemClick = new OffresActivity();
                girdItemClick.girdItemClick(dataModelList.get(position));

            }
        });

        return itemView;
    }



}
*/